package slideshow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

/**
 * Class name: SlideshowPlayer
 *
 * This class is used to load and display the slideshow selected by the user.
 * The class extends JFrame and can be opened through the editor or as an independent playback application..
 *
 */

public class SlideshowPlayer extends JFrame  {

    /**
     * static SlideshowPlayer instance- keeps track of SlideshowPlayer instance for Singleton implementation
     * String m_pathPrefix- keeps track of which folder to find the images in
     * JLabel m_imageLabel- label where slide images are displayed
     * ArrayList<Slide> m_SlideList- contains Slide objects with information necessary for slideshow
     * int m_currentSlideIndex- indicates with Slide in the list is being displayed
     * JPanel m_controlPanel- contains user controls for playback
     * JButton m_nextSlide- loads next slide in list
     * JButton m_previousSlide- loads previous slide in list
     * Jukebox m_Jukebox- object used to handle audio playback during the slideshow
     * Timer m_automationTimer- Timer used to countdown to automated Slide transition
     * JButton m_Pause- JButton present only during automated Slideshow. Used to pause m_automationTimer and m_Jukebox
     * Boolean m_paused- Used to track whether or not an automated Slideshow has been paused
     * private long m_slideStart- stores time that Slide was most recently started/resumed during automated playback
     * long m_timeElapsed- stores amount of time the Slide has run during automated playback. Used to maintain proper timing after resuming
     */
    private static SlideshowPlayer instance;
    private String m_pathPrefix;
    private JLabel m_imageLabel;
    private Slideshow m_Slideshow;
    private int m_currentSlideIndex;
    private JPanel m_controlPanel;
    private JButton m_nextSlide;
    private JButton m_previousSlide;
    private final Jukebox m_Jukebox;
    private Timer m_automationTimer;
    private JButton m_Pause;
    private Boolean m_paused;
    private long m_slideStart;
    private long m_timeElapsed;

    /**
     * Main function is used to create the JFrame when SlideshowPlayer is run as an independent application
     */
    public static void main(String[] args)
    {
        SlideshowPlayer.getInstance();
        //System.out.println("BUILD SUCCESS!");
    }

    /**
     * Constructor function used to initialize the SlideshowPlayer JFrame
     * Creates all necessary GUI components and calls functions used to create Slides
     */
    private SlideshowPlayer()
    {
        setTitle("Slideshow Player");
        setLayout(null);

        m_pathPrefix = "images"; //set directory name

        m_imageLabel = new JLabel();
        m_imageLabel.setBounds(150, 50, 500, 313);
        add(m_imageLabel);

        m_Slideshow = DBWizard.getInstance().getSlideshow(); //construct Slideshow using the layout file

        m_controlPanel = new JPanel();
        m_controlPanel.setLayout(null);
        m_controlPanel.setBackground(Color.LIGHT_GRAY);
        m_controlPanel.setBounds(0, 400, 784, 70);
        m_controlPanel.setBorder(BorderFactory.createLineBorder(Color.black, 3));

        if (m_Slideshow.getAutomated()) //see if Slideshow is set for automated playback...
        {
            setAutomatedControls(); //...if it is, configure the controls for automated playback
        } else {
            setManualControls(); //...if it isn't, configure the controls for manual playback
        }

        add(m_controlPanel);

        //Change appearance of JFrame
        setSize(800, 500); //800 width and 500 height
        setLocationRelativeTo(null);
        setVisible(true); //making the frame visible
        setResizable(false); //disable maximize button

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        m_Jukebox = Jukebox.getInstance();
        m_Jukebox.setSoundList(m_Slideshow.getSoundList()); //load Jukebox with sound data


        m_currentSlideIndex = -1; //initialize index
        showSlide(1, false);

        if (m_Slideshow.getAutomated()) //creates Timer to keep automated Slideshow going
        {
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {

                    timedShowSlide(1, false); //when Timer goes off, load new Slide and start Timer again

                }
            };

            m_automationTimer = new Timer((int) m_Slideshow.getSlide(m_currentSlideIndex).getTime(), taskPerformer);
            m_automationTimer.start(); //start a Timer that lasts the amount of time Slide should display
            m_slideStart = System.currentTimeMillis();
            m_paused = false;
        }

        m_Jukebox.playAll();

    }

    /**
     * Retrieves the desired Slide (next or previous) from m_SlideList
     * @param indexShift- indicates whether to get next or previous slide
     * @param skip- indicates whether or not checking for a Transition is required
     * @return Boolean- returns whether a new Slide was loaded (true) or if the requested index was invalid
     */
    private Boolean showSlide(int indexShift, boolean skip)
    {

        int tempIndex = m_currentSlideIndex + indexShift; //use tempIndex in case requested index is invalid
        boolean usedTransition = false; //flag to signal if a Transition was used

        try {

            if (!skip)
            {
                if (indexShift > 0) { //if next Slide is desired...

                    if (m_Slideshow.getSlide(tempIndex).hasTransitions()) { //...see if a Transition will be used...
                        m_Slideshow.getSlide(tempIndex).nextSlide(m_imageLabel); //...perform the Transition...

                        usedTransition = true; //...and mark that a Transition was used
                    }
                } else { //if the previous slide is desired...

                    if (m_Slideshow.getSlide(m_currentSlideIndex).hasTransitions()) { //...see if a Transition will be used...
                        Image nextImage = m_Slideshow.getSlide(tempIndex).getImage(); //...get the image from the previous Slide Slide...
                        m_Slideshow.getSlide(m_currentSlideIndex).returnToSlide(m_imageLabel, nextImage); //...perform the Transition...

                        usedTransition = true; //...and mark that a Transition was used
                    }
                }
            }

            if (!usedTransition) //if no Transition is needed, just load the desired image
            {
                m_imageLabel.setIcon(new ImageIcon(m_Slideshow.getSlide(tempIndex).getImage()));
            }

            m_currentSlideIndex = tempIndex; //if Slide was changed, update the index

        } catch (IndexOutOfBoundsException e){ //if tempIndex is invalid, the Slide is not changed

            return false;
        }

        return true;

    }

    /**
     * Used only with automated Slideshow. Guarantees that m_automationTimer is reset for the new Slide
     *
     * @param indexShift- indicates whether to get next or previous Slide
     * @param skip- indicates whether or not a Transition should be used/checked for
     */
    private void timedShowSlide(int indexShift, Boolean skip)
    {
        m_automationTimer.stop(); //stop current Timer
        m_timeElapsed = 0;

        if (showSlide(indexShift, skip)) //if this new Slide is not the last one...
        {
            m_automationTimer.setInitialDelay((int) m_Slideshow.getSlide(m_currentSlideIndex).getTime());
            m_slideStart = System.currentTimeMillis();
            m_automationTimer.start(); //...start the Timer with the new Slide's delay
        }
        else { //if the Slideshow is now over...

            m_Jukebox.pausePlayback(); //...stop Jukebox from playing...

            JOptionPane.showMessageDialog(null, "This Slideshow is now over.\nThank you for using our program.", "Have a Nice Day!", JOptionPane.PLAIN_MESSAGE);

            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)); //...and exit SlideshowPlayer

        }

    }

    /**
     * This functions specifies JButton behavior to be used when a Slideshow is set for manual playback
     */
    private void setAutomatedControls()
    {
        m_nextSlide = new JButton("Next Slide");
        m_nextSlide.setBounds(530, 20, 120, 20);
        m_controlPanel.add(m_nextSlide);

        m_nextSlide.addActionListener(event -> timedShowSlide(1, true));

        m_previousSlide = new JButton("Previous Slide");
        m_previousSlide.setBounds(150, 20, 120, 20);
        m_controlPanel.add(m_previousSlide);

        m_previousSlide.addActionListener(event -> timedShowSlide(-1, true));

        m_Pause = new JButton("Pause");
        m_Pause.setBounds(350, 20, 100, 20);
        m_controlPanel.add(m_Pause);

        m_Pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (m_paused) //if the Slideshow is already paused, resume it and the Jukebox
                {
                    m_slideStart = System.currentTimeMillis() - m_timeElapsed; //offset Timer start to account for Pause
                    m_automationTimer.start();
                    m_Jukebox.resumePlayback();
                    m_Pause.setText("Pause");
                    m_paused = false;
                }
                else { //otherwise, pause the Slideshow and the Jukebox

                    m_automationTimer.stop(); //stop Slide timer and update delay so it doesn't reset when resumed
                    m_timeElapsed = System.currentTimeMillis() - m_slideStart; //calculate amount of time Slide has been active

                    m_automationTimer.setInitialDelay((int) (m_Slideshow.getSlide(m_currentSlideIndex).getTime() - m_timeElapsed));

                    m_Jukebox.pausePlayback();
                    m_Pause.setText("Resume");
                    m_paused = true;
                }
            }
        });
    }

    /**
     * This function specifies JButton behavior to be used when a Slideshow is set for manual playback
     */
    private void setManualControls()
    {
        m_nextSlide = new JButton("Next Slide");
        m_nextSlide.setBounds(500, 20, 100, 20);
        m_controlPanel.add(m_nextSlide);

        m_nextSlide.addActionListener(event -> showSlide(1, false));

        m_previousSlide = new JButton("Previous Slide");
        m_previousSlide.setBounds(200, 20, 120, 20);
        m_controlPanel.add(m_previousSlide);

        m_previousSlide.addActionListener(event -> showSlide(-1, false));
    }

    /**
     * This function returns the instance of SlideshowPlayer. If no instance exists, then one is created.
     *
     * @return instance- pointer to instance of SlideshowPlayer to be used
     */
    public static SlideshowPlayer getInstance()
    {
        if (instance == null) {
            instance = new SlideshowPlayer();
        }
        return instance;
    }
}
