package slideshow;

import transitions.Transition;
import transitions.TransitionLibrary;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class name: SlideshowEditor
 *
 * This class manages the GUI components necessary for creating and exporting a Slideshow.
 */

public class SlideshowEditor extends JFrame {

    /**
     * SlideshowEditor instance- instance of SlideshowEditor used for Singleton implementation
     * JPanel m_ImageLibrary- contains the ImageLibrary GUI component
     * JPanel m_AudioLibrary- contains the AudioLibrary GUI component
     * TransitionLibrary m_TransitionLibrary- contains reference to TransitionLibrary for adding Transition to Slides
     * JPanel m_controlPanel- contains the buttons used to swap between active libraries
     * JButton getImages- loads the ImageLibrary
     * JButton getAudio- loads the AudioLibrary
     * JButton addTransitions- used to add Transitions to the selected Slide in Timeline
     */
    private static SlideshowEditor instance;
    private static JPanel m_ImageLibrary;
    private static JPanel m_AudioLibrary;
    private TransitionLibrary m_TransitionLibrary;
    private JPanel m_controlPanel;
    private JButton getImages;
    private JButton getAudio;
    private JButton addTransitions;

    public static void main(String[] args)
    {
        SlideshowEditor.getInstance();
    }

    public SlideshowEditor()
    {

        setTitle("SlideshowPlayer");
        setLayout(null);

        /**
         *
         * EVAN, PUT YOUR LIBRARIES HERE
        m_ImageLibrary = m_ImageLibrary.getInstance();
        m_AudioLibrary = m_AudioLibrary.getInstance();

        add(m_ImageLibrary);
         */

        m_TransitionLibrary = TransitionLibrary.getInstance();

        m_controlPanel = new JPanel();
        m_controlPanel.setLayout(null);
        m_controlPanel.setBackground(Color.LIGHT_GRAY);
        m_controlPanel.setBounds(0, 0, 1384, 70);
        m_controlPanel.setBorder(BorderFactory.createLineBorder(Color.black, 3));

        getImages = new JButton("Image Library");
        getImages.setBounds(50, 20, 120, 20);
        m_controlPanel.add(getImages);

        getImages.addActionListener(event -> showImageLibrary());

        getAudio = new JButton("Audio Library");
        getAudio.setBounds(300, 20, 120, 20);
        m_controlPanel.add(getAudio);

        getAudio.addActionListener(event -> showAudioLibrary());

        addTransitions = new JButton("Add Transitions");
        addTransitions.setBounds(1200, 20, 150, 20);
        m_controlPanel.add(addTransitions);

        //this function will eventually pull the selected Slide form timeline to use as an argument
        //addTransitions.addActionListener(event -> addSlideTransitions(testSlide));

        add(m_controlPanel);

        //Change appearance of JFrame
        setSize(1400,800); //1400 width and 800 height
        setLocationRelativeTo(null);
        setVisible(true); //making the frame visible

        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    /**
     * This function is used to make m_ImageLibrary the active JPanel
     */
    private void showImageLibrary()
    {
        remove(m_AudioLibrary);
        add(m_ImageLibrary);
    }

    /**
     * This function is used to make m_AudioLibrary the active JPanel
     */
    private void showAudioLibrary()
    {
        remove(m_ImageLibrary);
        add(m_AudioLibrary);
    }

    /**
     * This button is used when adding Transitions to the Slide selected in Timeline
     * @param selectedSlide- Slide from Timeline the user wishes to add Transitions to
     */
    private void addSlideTransitions(Slide selectedSlide)
    {
        ArrayList<Transition> desiredTransitions = new ArrayList<Transition>();

        desiredTransitions = m_TransitionLibrary.retrievalGUI(); //use TransitionLibrary GUI-based retrieval function

        if (desiredTransitions.size() > 0) //if user didn't decide to stop adding the Transition...
        {
            selectedSlide.setForward(desiredTransitions.get(0)); //...set the forwards and backwards Transitions
            selectedSlide.setBackwards(desiredTransitions.get(1));
        }
    }

    /**
     * This function returns the instance of SlideshowPlayer. If no instance exists, then one is created.
     *
     * @return instance- pointer to instance of SlideshowPlayer to be used
     */
    public static SlideshowEditor getInstance()
    {
        if (instance == null) {
            instance = new SlideshowEditor();
        }
        return instance;
    }
}
