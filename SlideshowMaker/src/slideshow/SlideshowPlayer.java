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
    private Dimension m_screenSize = Toolkit. getDefaultToolkit(). getScreenSize();
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
    }

    /**
     * Constructor function used to initialize the SlideshowPlayer JFrame
     * Creates all necessary GUI components and calls functions used to create Slides
     */
    private SlideshowPlayer()
    {
        try { //set theme for SlideshowPlayer
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageIcon windowIcon = new ImageIcon("images\\SlideshowIcon.png");
        Image icon = windowIcon.getImage();
        setIconImage(icon);

        String slideshowPath = SlideshowManager.selectSlideshow(this);

        int scrnWidth = 1400;
        int scrnHeight = 800;

        setTitle("Slideshow Player");
        setLayout(null);

        JMenuBar topMenu = new JMenuBar(); //create a menu bar for the window
        setJMenuBar(topMenu);

        JMenu fileMenu = new JMenu("File");
        topMenu.add(fileMenu);

        JMenuItem openShow = new JMenuItem("Open Slideshow"); //allow user to set directory for Slideshow creation
        //openShow.addActionListener(event -> SlideshowManager.selectSlideshow());
        fileMenu.add(openShow);

        JMenuItem closeProgram = new JMenuItem("Exit"); //add Exit Program button to File menu
        closeProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(closeProgram);

        m_pathPrefix = "images"; //set directory name

        m_imageLabel = new JLabel();
        m_imageLabel.setBounds(0, 0, scrnWidth, (int) (scrnHeight*0.85));
        add(m_imageLabel);

        m_Slideshow = SlideshowManager.getInstance().getSlideshow(slideshowPath); //construct Slideshow using the layout file

        m_controlPanel = new JPanel(new GridBagLayout());
        m_controlPanel.setBackground(Color.LIGHT_GRAY);
        m_controlPanel.setBounds(0, (int) (scrnHeight*0.85), scrnWidth - 16, (int)(scrnHeight*0.09));
        m_controlPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        if (m_Slideshow.getAutomated()) //see if Slideshow is set for automated playback...
        {
            setAutomatedControls(); //...if it is, configure the controls for automated playback
        } else {
            setManualControls(); //...if it isn't, configure the controls for manual playback
        }

        add(m_controlPanel);
        
        //Change appearance of JFrame
        setSize(scrnWidth, scrnHeight);
        removeFocusFromAllObjects(this);
        setLocationRelativeTo(null);
        setResizable(false); //disable maximize button
        setVisible(true); //making the frame visible
        

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
     * Removes focusable dotted line from all components
     * @param container - object with components to set focus for 
     */
    public void removeFocusFromAllObjects(Container container) {
        container.setFocusable(false);
        for (Component child : container.getComponents()) {
            if (child instanceof Container) {
                removeFocusFromAllObjects((Container) child);
            } else{
                child.setFocusable(false);
            }
        }
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
        else if (m_currentSlideIndex != 0){ //if the Slideshow is now over...

            m_Jukebox.pausePlayback(); //...stop Jukebox from playing...

            JOptionPane.showMessageDialog(null, "<html><div style='text-align: center;'>This " +
                    "Slideshow is now over.<br>Thank you for using our program.</div></html>", "Have a Nice Day!", JOptionPane.PLAIN_MESSAGE);

            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)); //...and exit SlideshowPlayer

        }
        else { //if hitting Previous Slide on first slide, restart the Slide's timer
            m_automationTimer.setInitialDelay(m_automationTimer.getDelay());
            m_slideStart = System.currentTimeMillis();
            m_automationTimer.start();
        }

    }

    /**
     * This functions specifies JButton behavior to be used when a Slideshow is set for manual playback
     */
    private void setAutomatedControls()
    {
    	// Set initial gridbag constraint parameters
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridy = 0;
    	c.weightx = 0.5;
    	c.weighty = 0.5;
    	
        ImageIcon tempNextIcon = new ImageIcon("images\\skipforwardicon.png");
        ImageIcon tempPrevIcon = new ImageIcon("images\\skipbackicon.png");
        
        // Transform temp pause and play icons and store them in new variables
        Image image = tempNextIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ImageIcon nextIcon = new ImageIcon(newimg); 
        
        image = tempPrevIcon.getImage(); // transform it 
        newimg = image.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ImageIcon prevIcon = new ImageIcon(newimg); 
        
        m_nextSlide = new JButton();
        m_nextSlide.setIcon(nextIcon);
    	c.gridx = 2;
        m_controlPanel.add(m_nextSlide, c);

        m_nextSlide.addActionListener(event -> timedShowSlide(1, true));

        m_previousSlide = new JButton();
        m_previousSlide.setIcon(prevIcon);
        c.gridx = 0;
        m_controlPanel.add(m_previousSlide, c);

        m_previousSlide.addActionListener(event -> timedShowSlide(-1, true));

        ImageIcon tempPlayIcon = new ImageIcon("images\\playbuttonicon.png");
        ImageIcon tempPauseIcon = new ImageIcon("images\\pausebuttonicon.png");
        
        // Transform temp pause and play icons and store them in new variables
        image = tempPlayIcon.getImage(); // transform it 
        newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ImageIcon playIcon = new ImageIcon(newimg); 
        
        image = tempPauseIcon.getImage(); // transform it 
        newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ImageIcon pauseIcon = new ImageIcon(newimg); 
        
        m_Pause = new JButton();
        c.gridx = 1;
        m_controlPanel.add(m_Pause);
        
        //Set icon for pause button
        m_Pause.setIcon(pauseIcon);

        m_Pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (m_paused) //if the Slideshow is already paused, resume it and the Jukebox
                {
                    m_slideStart = System.currentTimeMillis() - m_timeElapsed; //offset Timer start to account for Pause
                    m_automationTimer.start();
                    m_Jukebox.resumePlayback();
                    m_Pause.setIcon(pauseIcon);
                    m_paused = false;
                }
                else { //otherwise, pause the Slideshow and the Jukebox

                    m_automationTimer.stop(); //stop Slide timer and update delay so it doesn't reset when resumed
                    m_timeElapsed = System.currentTimeMillis() - m_slideStart; //calculate amount of time Slide has been active

                    m_automationTimer.setInitialDelay((int) (m_Slideshow.getSlide(m_currentSlideIndex).getTime() - m_timeElapsed));

                    m_Jukebox.pausePlayback();
                    m_Pause.setIcon(playIcon);
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
    	
    	// Set initial gridbag constraint parameters
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridy = 0;
    	c.weightx = 0.5;
    	c.weighty = 0.5;
    	
        ImageIcon tempNextIcon = new ImageIcon("images\\nexticon.png");
        ImageIcon tempPrevIcon = new ImageIcon("images\\previousicon.png");
        
        // Transform temp pause and play icons and store them in new variables
        Image image = tempNextIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(24, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ImageIcon nextIcon = new ImageIcon(newimg); 
        
        image = tempPrevIcon.getImage(); // transform it 
        newimg = image.getScaledInstance(24, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ImageIcon prevIcon = new ImageIcon(newimg); 
        
        m_nextSlide = new JButton();
        m_nextSlide.setIcon(nextIcon);
        c.gridx = 2;
        m_controlPanel.add(m_nextSlide, c);

        m_nextSlide.addActionListener(event -> showSlide(1, false));

        m_previousSlide = new JButton();
        m_previousSlide.setIcon(prevIcon);
        c.gridx = 0;
        m_controlPanel.add(m_previousSlide, c);

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
