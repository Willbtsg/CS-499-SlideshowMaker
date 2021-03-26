package slideshow;

import transitions.Transition;
import transitions.TransitionLibrary;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private static AudioLibrary m_AudioLibrary;
    private JPanel m_SettingsPanel;
    private TransitionLibrary m_TransitionLibrary;
    private JPanel m_controlPanel;
    private JButton getImages;
    private JButton getAudio;
    private JButton addTransitions;
    private boolean automated;
    private double slideInterval;

    public static void main(String[] args)
    {
        SlideshowEditor.getInstance();
    }

    public SlideshowEditor()
    {
        // INITIALIZING THE WINDOW

        setTitle("Slideshow Editor");
        setLayout(new BorderLayout());

        // CREATING THE TIMELINE

        JPanel timelineAndExport = new JPanel();
        timelineAndExport.setLayout(new BorderLayout());

        Timeline timeline = Timeline.getInstance(this);
        JScrollPane spTimeline = new JScrollPane(timeline);
        spTimeline.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spTimeline.getVerticalScrollBar().setUnitIncrement(20);
        spTimeline.setPreferredSize(new Dimension(320,800));
        Border spTimelineBorder = BorderFactory.createTitledBorder("Timeline");
        spTimeline.setBorder(spTimelineBorder);
        timelineAndExport.add(spTimeline, BorderLayout.NORTH);

        JButton export = new JButton("Save Slideshow into Directory");
        timelineAndExport.add(export, BorderLayout.SOUTH);

        add(timelineAndExport, BorderLayout.EAST);

        // CREATING THE LIBRARY & SETTINGS TABS

        JTabbedPane libraries = new JTabbedPane();

        m_ImageLibrary = ImageLibrary.getInstance(this, timeline);
        JScrollPane spImages = new JScrollPane(m_ImageLibrary);
        spImages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spImages.getVerticalScrollBar().setUnitIncrement(20);
        libraries.add("Images", spImages);

        // TODO: The below method call will need to be fed the timeline
        m_AudioLibrary = AudioLibrary.getInstance();
        JScrollPane spAudio = new JScrollPane(m_AudioLibrary);
        spAudio.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spAudio.getVerticalScrollBar().setUnitIncrement(20);
        libraries.add("Audio", spAudio);
        
        m_SettingsPanel = new JPanel(new GridLayout(10,0));
        automated = false;
        slideInterval = 5.0;
        addSettingControls();
        libraries.add("Settings", m_SettingsPanel);

        libraries.setPreferredSize(new Dimension(1000,800));
        add(libraries, BorderLayout.WEST);

        // CONFIGURING THE WINDOW

        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
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
     * This function is used to set setting controls for settings panel
     */
    private void addSettingControls()
    {
    	// Declare components to be stored in settings panel
    	JCheckBox automatedCheckBox = new JCheckBox("Automatic Slideshow");
    	JLabel slideIntervalLabel = new JLabel("Slide Interval (In seconds)");
    	JTextField slideIntervalTF = new JTextField("5.0");
    	JButton slideIntervalJB = new JButton("Submit Changes");
    	
    	// Event listener for Checkbox to change between automatic and manual slideshow
        automatedCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	automated = !automated;
            }
        });
        
        // Event listener for Jbutton to change slide interval
        slideIntervalJB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	try {
            		String temp = slideIntervalTF.getText();
            		slideInterval = Float.parseFloat(temp);
            	}
            	catch(Exception e) {
            		System.out.println(slideIntervalTF.getText()+ " cannot be converted to float: " + e.getMessage());
            	}
            }
        });
    	
        // Add all components to settings panel
    	m_SettingsPanel.add(automatedCheckBox);
    	m_SettingsPanel.add(slideIntervalLabel);
    	m_SettingsPanel.add(slideIntervalTF);
    	m_SettingsPanel.add(slideIntervalJB);
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
