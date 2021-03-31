package slideshow;

import transitions.Transition;
import transitions.TransitionLibrary;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
     * JPanel m_Setting Panel- allows user to set automation status and Slide interval
     * boolean automated- indicates whether or not the Slideshow made will be automated
     * double slideInterval- if the Slideshow is automated, this indicates how long each Slide is shown
     */
    private static SlideshowEditor instance;
    private static ImageLibrary m_ImageLibrary;
    private static AudioLibrary m_AudioLibrary;

    private boolean automated;
    private double slideInterval = 4.0;

    private JFrame popup;
    private boolean popupPresent = false;

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

        Timeline timeline = Timeline.getInstance();
        timelineAndButtons.add(timeline, BorderLayout.NORTH);

        JPanel settingsAndExport = new JPanel();
        settingsAndExport.setLayout(new BorderLayout());

        JButton settings = new JButton("Edit Slideshow Settings");
        settings.setMargin(new Insets(7,7,7,7));
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!popupPresent)
                {
                    popupPresent = true;
                    settingsPopup();
                }
                else
                    popup.toFront();
            }
        });
        settingsAndExport.add(settings,BorderLayout.NORTH);

        JButton export = new JButton("Save Slideshow into Directory");
        export.setMargin(new Insets(7,7,7,7));
        settingsAndExport.add(export, BorderLayout.SOUTH);

        timelineAndButtons.add(settingsAndExport, BorderLayout.SOUTH);
        add(timelineAndButtons, BorderLayout.EAST);

        // CREATING THE LIBRARY & SETTINGS TABS

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

        libraries.setPreferredSize(new Dimension(1000,895));
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

        setSize(new Dimension(1400, 895));
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
        popup = new JFrame("Slideshow Settings");
        JPanel settingsPanel = new JPanel();

        JCheckBox automatedCheckBox = new JCheckBox("Automatic Slideshow");

        JLabel slideIntervalLabel = new JLabel("Slide Interval (in seconds)");
        JTextField slideIntervalTF = new JTextField(String.valueOf(slideInterval));
        slideIntervalTF.setPreferredSize(new Dimension(50,25));

        if (automated)
            automatedCheckBox.setSelected(true);
        else
        {
            automatedCheckBox.setSelected(false);
            slideIntervalTF.setEnabled(false);
        }

        automatedCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (automatedCheckBox.isSelected())
                    slideIntervalTF.setEnabled(true);
                else
                    slideIntervalTF.setEnabled(false);
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
                    popupPresent = false;
                    popup.dispose();
                } catch (Exception ex) {
                    if (automated)
                        error.setVisible(true);
                    else {
                        popupPresent = false;
                        popup.dispose();
                    }
                }
            }
        });

        settingsPanel.add(automatedCheckBox);
        settingsPanel.add(slideIntervalLabel);
        settingsPanel.add(slideIntervalTF);
        settingsPanel.add(error);
        settingsPanel.add(submitChanges);
        popup.add(settingsPanel);

        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        popup.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                popupPresent = false;
            }
        });

        popup.setSize(new Dimension(290,140));
        popup.setResizable(false);
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);
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
