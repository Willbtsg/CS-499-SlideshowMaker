package slideshow;

import transitions.Transition;

import transitions.TransitionLibrary;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
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
     * boolean automated- indicates whether or not the Slideshow made will be automated
     * double slideInterval- if the Slideshow is automated, this indicates how long each Slide is shown
     * JFrame settingsFrame- frame that pops up when user wishes to adjust automation settings
     * boolean settingsPresent- indicates whether or not settings frame is already loaded, prevents duplicates
     */
    private static SlideshowEditor instance;
    private static ImageLibrary m_ImageLibrary;
    private static AudioLibrary m_AudioLibrary;
    private static Timeline timeline;
    private boolean automated;
    private double slideInterval = 0.0;
    private JFrame settingsFrame;
    private boolean settingsPresent = false;

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

        JPanel timelineAndButtons = new JPanel();
        timelineAndButtons.setLayout(new BorderLayout());

        timeline = Timeline.getInstance();
        timelineAndButtons.add(timeline, BorderLayout.NORTH);

        JPanel settingsAndExport = new JPanel();
        settingsAndExport.setLayout(new BorderLayout());

        JButton settings = new JButton("Edit Slideshow Settings");
        settings.setMargin(new Insets(7,7,7,7));

        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!settingsPresent)
                {
                    settingsPresent = true;
                    settingsPopup();
                }
                else
                    settingsFrame.toFront();
            }
        });

        settingsAndExport.add(settings,BorderLayout.NORTH);

        JButton export = new JButton("Save Slideshow into Directory");
        export.setMargin(new Insets(7,7,7,7));

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeline.exportSlideshow(automated);
            }
        });
        settingsAndExport.add(export, BorderLayout.SOUTH);

        timelineAndButtons.add(settingsAndExport, BorderLayout.SOUTH);
        add(timelineAndButtons, BorderLayout.EAST);

        // CREATING THE MEDIA LIBRARIES

        JTabbedPane libraries = new JTabbedPane();

        m_ImageLibrary = ImageLibrary.getInstance(timeline);
        JScrollPane spImages = new JScrollPane(m_ImageLibrary);
        spImages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spImages.getVerticalScrollBar().setUnitIncrement(20);
        libraries.add("Images", spImages);

        m_AudioLibrary = AudioLibrary.getInstance(timeline);
        JScrollPane spAudio = new JScrollPane(m_AudioLibrary);
        spAudio.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spAudio.getVerticalScrollBar().setUnitIncrement(20);
        libraries.add("Audio", spAudio);

        libraries.setPreferredSize(new Dimension(1000,800));
        add(libraries, BorderLayout.WEST);

        libraries.setBorder(BorderFactory.createTitledBorder("Media"));

        libraries.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                timeline.setSelectedIndex(libraries.getSelectedIndex()); //makes sure Timeline tab matches selected library
            }
        });

        timeline.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                libraries.setSelectedIndex(timeline.getSelectedIndex()); //makes selected library matches Timeline tab
            }
        });

        // CONFIGURING THE WINDOW

        setSize(new Dimension(1400, 800));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * This function is used to pop-up the settings panel
     */
    private void settingsPopup()
    {
        settingsFrame = new JFrame("Slideshow Settings");
        JPanel settingsPanel = new JPanel();

        JCheckBox automatedCheckBox = new JCheckBox("Automatic Slideshow");

        JLabel slideIntervalLabel = new JLabel("Default Slide Duration (sec): ");
        JTextField slideIntervalTF = new JTextField(String.valueOf(slideInterval));
        slideIntervalTF.setPreferredSize(new Dimension(50,25));

        if (automated) //only allow user to set a Slide interval if "Automatic Slideshow" is selected
        {
            automatedCheckBox.setSelected(true);
        } else
        {
            automatedCheckBox.setSelected(false);
            slideIntervalTF.setEnabled(false);
        }

        automatedCheckBox.addActionListener(new ActionListener() //makes interval field active/inactive as user checks/unchecks the box
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (automatedCheckBox.isSelected())
                {
                    slideIntervalTF.setEnabled(true);
                    slideIntervalTF.setText("3.0");
                }
                else {
                    slideIntervalTF.setEnabled(false);
                    slideIntervalTF.setText("0.0");
                }
            }
        });

        JLabel error = new JLabel("Invalid slide interval!");
        error.setVisible(false);

        JButton submitChanges = new JButton("Submit Changes");
        submitChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                automated = automatedCheckBox.isSelected();
                try {
                    slideInterval = Double.parseDouble(slideIntervalTF.getText());
                    timeline.setSlideDurationVisible(automated);
                    timeline.setDefaultSlideDuration(Double.parseDouble(slideIntervalTF.getText()));
                    settingsPresent = false;
                    settingsFrame.dispose();
                } catch (Exception ex) {
                    if (automated)
                    {
                        error.setVisible(true);
                        automated = false;
                    }
                    else {
                        settingsPresent = false;
                        settingsFrame.dispose();
                    }
                }
            }
        });

        settingsPanel.add(automatedCheckBox);
        settingsPanel.add(slideIntervalLabel);
        settingsPanel.add(slideIntervalTF);
        settingsPanel.add(error);
        settingsPanel.add(submitChanges);
        settingsFrame.add(settingsPanel);

        //allows user to save new interval by hitting enter
        slideIntervalTF.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitChanges.doClick();
                }
            }
        });

        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                settingsPresent = false;
            }
        });

        settingsFrame.setSize(new Dimension(290,140));
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setVisible(true);
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
