import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
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
     */

    private static SlideshowPlayer instance;
    private String m_pathPrefix;
    private JLabel m_imageLabel;
    private ArrayList<Slide> m_SlideList;
    private int m_currentSlideIndex;
    private JPanel m_controlPanel;
    private JButton m_nextSlide;
    private JButton m_previousSlide;
    private Jukebox m_Jukebox;

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
    private SlideshowPlayer() {
        setTitle("SlideshowPlayer");
        setLayout(null);

        m_pathPrefix = "images"; //set directory name
        m_currentSlideIndex = -1; //initialize index

        m_imageLabel = new JLabel();
        m_imageLabel.setBounds(150, 50, 500, 313);
        add(m_imageLabel);

        m_SlideList = DBWizard.getInstance().getSlides(); //construct SlideList using the layout file

        if (m_currentSlideIndex < 0) { //loads first image in slideshow
            m_imageLabel.setIcon(new ImageIcon(m_SlideList.get(0).getImage()));
            m_currentSlideIndex = 0;
        }

        m_controlPanel = new JPanel();
        m_controlPanel.setLayout(null);
        m_controlPanel.setBackground(Color.LIGHT_GRAY);
        m_controlPanel.setBounds(0, 400, 784, 70);
        m_controlPanel.setBorder(BorderFactory.createLineBorder(Color.black, 3));

        m_nextSlide = new JButton("Next Slide");
        m_nextSlide.setBounds(500, 20, 100, 20);
        m_controlPanel.add(m_nextSlide);

        m_nextSlide.addActionListener(event -> getSlide(1));

        m_previousSlide = new JButton("Previous Slide");
        m_previousSlide.setBounds(200, 20, 120, 20);
        m_controlPanel.add(m_previousSlide);

        m_previousSlide.addActionListener(event -> getSlide(-1));

        add(m_controlPanel);

        //Change appearance of JFrame
        setSize(800, 500);//800 width and 500 height
        setLocationRelativeTo(null);
        setVisible(true);//making the frame visible

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        m_Jukebox = Jukebox.getInstance();
        m_Jukebox.setSoundList((DBWizard.getInstance().getSongs()));
        m_Jukebox.playAll(); //after loading Jukebox with song data, tell it to play until it runs out

    }

    /**
     * Reads data necessary for creation of Slide objects, then creates the Slides
     * This function will be adapted for the editor when it is written
     * @return theList- returns list of Slide objects to be presented in the slideshow
     */
    private ArrayList<Slide> getSlideList()
    {
        File data = new File(m_pathPrefix);

        ArrayList<Slide> theList = new ArrayList<Slide>();

        for (String imageName : data.list()){

            if (imageName.endsWith(".jpg") || imageName.endsWith(".jpeg"))
            {
                theList.add(new Slide(m_pathPrefix + "/" + imageName)); //create Slide with full filepath in name
            }
        }

        return theList;
    }

    /**
     * Retrieves the desired Slide (next or previous) from m_SlideList
     * @param indexShift- indicates whether to get next or previous slide
     */
    private void getSlide(int indexShift)
    {

        int tempIndex = m_currentSlideIndex + indexShift; //use tempIndex in case requested index is invalid

        try {

            if (indexShift > 0) { //if next Slide is desired...

                if (m_SlideList.get(m_currentSlideIndex).hasTransitions()) { //...see if a Transition will be used...
                    Image nextImage = m_SlideList.get(tempIndex).getImage(); //...get the image for the next Slide...
                    m_SlideList.get(m_currentSlideIndex).nextSlide(m_imageLabel, nextImage, 2); //...and perform the Transition
                }
                else { //otherwise, set new image without use of a Transition
                    m_imageLabel.setIcon(new ImageIcon(m_SlideList.get(tempIndex).getImage()));
                }
            }
            else { //if the previous slide is desired...

                if (m_SlideList.get(tempIndex).hasTransitions()) { //...see if a Transition will be used
                    Image currentImage = m_SlideList.get(m_currentSlideIndex).getImage(); //...store the image form the current Slide...
                    m_SlideList.get(tempIndex).returnToSlide(m_imageLabel, currentImage, 2); //...and perform the Transition
                }
                else { //otherwise, set new image without use of a Transition
                    m_imageLabel.setIcon(new ImageIcon(m_SlideList.get(tempIndex).getImage()));
                }
            }

            m_currentSlideIndex = tempIndex; //if Slide was changed, update the index

        } catch (IndexOutOfBoundsException e){ //if tempIndex is invalid, the Slide is not changed
            return;
        }

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
