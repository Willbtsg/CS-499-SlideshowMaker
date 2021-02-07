import javax.swing.*;
import java.awt.*;
import java.io.File;
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
     * Image m_imageScale- used to scale slide images for display
     * JLabel m_imageLabel- label where slide images are displayed
     * ArrayList<Slide> m_SlideList- contains Slide objects with information necessary for slideshow
     * int m_currentSlideIndex- indicates with Slide in the list is being displayed
     * JPanel m_controlPanel- contains user controls for playback
     * JButton m_nextSlide- loads next slide in list
     * JButton m_previousSlide- loads previous slide in list
     */

    private static SlideshowPlayer instance;
    private String m_pathPrefix;
    private Image m_imageScale;
    private JLabel m_imageLabel;
    private ArrayList<Slide> m_SlideList;
    private int m_currentSlideIndex;
    private JPanel m_controlPanel;
    private JButton m_nextSlide;
    private JButton m_previousSlide;

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
     * Creates all necessary GUI components and calls funcitons used to create Slides
     */
    private SlideshowPlayer()
    {
        setTitle("SlideshowPlayer");
        setLayout(null);

        m_pathPrefix = "images"; //set directory name
        m_currentSlideIndex = -1; //initialize index

        m_imageLabel = new JLabel();
        m_imageLabel.setBounds(150, 50, 500, 313);
        add(m_imageLabel);

        m_SlideList = getSlideList();

        if (m_currentSlideIndex < 0){ //loads first image in slideshow
            getSlide(++m_currentSlideIndex);
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
        setSize(800,500);//800 width and 500 height
        setLocationRelativeTo(null);
        setVisible(true);//making the frame visible

        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    /**
     * Reads data necessary for creation of Slide objects, then creates the Slides
     * @return theList- returns list of Slide objects to be presented in the slideshow
     */
    private ArrayList<Slide> getSlideList()
    {
        File data = new File(m_pathPrefix);

        ArrayList<Slide> theList = new ArrayList<Slide>();

        for (String imageName : data.list()){
            theList.add(new Slide(m_pathPrefix + "/" + imageName)); //create Slide with full filepath in name
        }

        return theList;
    }

    /**
     * Retrieves the desired Slide (next or previous) from m_SlideList
     * @param indexShift- indicates whether to get next or previous slide
     */
    private void getSlide(int indexShift)
    {

        int tempIndex = m_currentSlideIndex + indexShift;

        try {

            ImageIcon currentImage = new ImageIcon(m_SlideList.get(tempIndex).getName());
            m_imageScale = currentImage.getImage();
            m_imageScale = m_imageScale.getScaledInstance(500, 313, Image.SCALE_SMOOTH);

            //m_imageLabel.setBounds(263, 30, 220, 160);
            m_imageLabel.setIcon(new ImageIcon(m_imageScale)); //set the scaled poster in the JLabel

            m_currentSlideIndex = tempIndex;

        } catch (IndexOutOfBoundsException e){
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
