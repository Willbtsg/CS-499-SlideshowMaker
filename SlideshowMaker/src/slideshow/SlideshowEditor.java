package slideshow;

import transitions.Transition;
import transitions.TransitionLibrary;

import javax.swing.*;
import javax.swing.border.Border;
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
    private static ImageLibrary m_ImageLibrary;
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
        setTitle("Slideshow Editor");
        setLayout(new BorderLayout());

        JTabbedPane libraries = new JTabbedPane();
        m_ImageLibrary = ImageLibrary.getInstance();
        JScrollPane spImages = new JScrollPane(m_ImageLibrary);
        spImages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        libraries.add("Images", spImages);
        m_AudioLibrary = AudioLibrary.getInstance();
        JScrollPane spAudio = new JScrollPane(m_AudioLibrary);
        spAudio.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        libraries.add("Audio", spAudio);
        libraries.setPreferredSize(new Dimension(1000,800));
        add(libraries, BorderLayout.WEST);

        // THE FOLLOWING LINES ARE A TEMP TIMELINE
        JPanel timeline = new JPanel();
        timeline.setPreferredSize(new Dimension(400,800));
        Border timelineBorder = BorderFactory.createTitledBorder("Timeline");
        timeline.setBorder(timelineBorder);
        add(timeline, BorderLayout.EAST);

        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /**
     * This function is used to make m_ImageLibrary the active JPanel
     */
    private void showImageLibrary()
    {
        //m_AudioLibrary.setVisible(false);
        m_ImageLibrary.setVisible(true);
    }

    /**
     * This function is used to make m_AudioLibrary the active JPanel
     */
    private void showAudioLibrary()
    {
        m_ImageLibrary.setVisible(false);
        //m_AudioLibrary.setVisible(true);
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
