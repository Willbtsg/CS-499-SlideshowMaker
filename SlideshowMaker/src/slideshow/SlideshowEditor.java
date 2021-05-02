package slideshow;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


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
     * Timeline timeline- contains Timeline GUI component
     * boolean automated- indicates whether or not the Slideshow made will be automated
     * double slideInterval- if the Slideshow is automated, this indicates how long each Slide is shown
     * JFrame settingsFrame- frame that pops up when user wishes to adjust automation settings
     * boolean settingsPresent- indicates whether or not settings frame is already loaded, prevents duplicates
     * String directory- filepath to the directory being used by the Editor
     * JTabbedPane libraries- contains the Image and Audio libraries used to construct the Slideshow
     */
    private static SlideshowEditor instance;
    private static ImageLibrary m_ImageLibrary;
    private static AudioLibrary m_AudioLibrary;
    private static Timeline timeline;
    private boolean automated;
    private double slideInterval = 0.0;
    private JFrame settingsFrame;
    private boolean settingsPresent = false;
    private String directory;
    private JTabbedPane libraries;

    public static void main(String[] args)
    {
        SlideshowEditor.getInstance();
    }

    /**
     * Constructor function used to initialize the SlideshowEditor JFrame
     * Creates all necessary GUI components and enables them for use
     */
    public SlideshowEditor()
    {
        // INITIALIZING THE WINDOW
    	
        try { //set theme for SlideshowEditors
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Slideshow Editor");
        setLayout(new BorderLayout());

        // CREATING THE TIMELINE

        timeline = Timeline.getInstance();
        add(timeline, BorderLayout.EAST);

        // CREATING THE MEDIA LIBRARIES

        libraries = new JTabbedPane();

        m_ImageLibrary = ImageLibrary.getInstance(timeline, null); //initialize libraries with null since user has not selected a directory
        JScrollPane spImages = new JScrollPane(m_ImageLibrary);
        spImages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spImages.getVerticalScrollBar().setUnitIncrement(20);
        libraries.add("Images", spImages);

        m_AudioLibrary = AudioLibrary.getInstance(timeline, null);
        JScrollPane spAudio = new JScrollPane(m_AudioLibrary);
        spAudio.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spAudio.getVerticalScrollBar().setUnitIncrement(20);
        libraries.add("Audio", spAudio);

        libraries.setPreferredSize(new Dimension(1000,750));
        add(libraries, BorderLayout.WEST);

        libraries.setBorder(BorderFactory.createTitledBorder("Media"));

        libraries.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                timeline.setActivePane(libraries.getSelectedIndex()); //makes sure Timeline tab matches selected library
            }
        });

        timeline.enablePaneMatch(libraries);

        // CONFIGURING THE WINDOW

        JMenuBar topMenu = new JMenuBar(); //create a menu bar for the window
        setJMenuBar(topMenu);

        JMenu fileMenu = new JMenu("File");
        topMenu.add(fileMenu);

        JMenuItem newDirectory = new JMenuItem("Set Directory"); //allow user to set directory for Slideshow creation
        newDirectory.addActionListener(event -> changeDirectory());
        newDirectory.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)); //add keyboard shortcut Ctrl + O
        fileMenu.add(newDirectory);

        JMenuItem exportSlideshow = new JMenuItem("Export Slideshow");
        exportSlideshow.addActionListener(event -> timeline.exportSlideshow(automated));
        exportSlideshow.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)); //add keyboard shortcut Ctrl + S
        fileMenu.add(exportSlideshow);

        JMenuItem closeProgram = new JMenuItem("Exit"); //add Exit Program button to File menu
        closeProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(closeProgram);

        JMenu settingsMenu = new JMenu("Settings");
        topMenu.add(settingsMenu);

        JMenuItem changeSettings = new JMenuItem("Change Playback Settings");
        changeSettings.addActionListener(new ActionListener() {
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

        settingsMenu.add(changeSettings);

        JMenu helpMenu = new JMenu("Help");
        topMenu.add(helpMenu);

        JMenuItem tutorial = new JMenuItem("Tutorial");
        String tutorialMsg = "<html>Creating a slideshow is easy!<br><ol><li>Open the folder containing the image and audio files you'd like to use. (File -> Set Directory)</li>" +
                "<li>To add image and audio files to your slideshow, just click \"Add\" under the given image/audio file.</li>" +
                "<li>To set your slideshow to play back automatically, open the settings window (Settings -> Change Playback Settings) and click the \"Automatic Slideshow\" checkbox. <br>From there, click the \"Submit Changes\" button to save your settings. (To set it back to manual, just do the reverse!)</li>" +
                "<li>To change the default slide duration of an automatic slideshow, open the settings window and change the value in the \"Default Slide Duration\" textbox. <br>From there, click the \"Submit Changes\" button to save your settings.</li>" +
                "<li>To change the duration of an individual slide in an automatic slideshow, uncheck the \"Use Default Slide Duration\" at the bottom of the slide item in the Timeline.<br>From there, click the \"Change Duration\" button to the right of the slide duration value and enter a new value.<br>(To set it back to the default slide duration, just recheck the \"Use Default Slide Duration\" checkbox!)</li>" +
                "<li>To set a transition to play at the beginning of a slide, select a transition type from the \"Transition Type\" dropdown at the top of a slide item.</li>" +
                "<li>To change the duration of a slide's transition, select a transition duration from the \"Transition Duration\" dropdown at the top of a slide item.<br>(This dropdown will only be enabled if a transition has been set.)</li>" +
                "<li>To move a slide up/down the timeline, click the \"Move Up/Down\" button at the bottom of a slide item.</li>" +
                "<li>To remove a slide from your slideshow, click the \"Remove\" button at the bottom on a slide item.</li>" +
                "<li>Audio items in the Timeline work the same as slide items, except they can't use transitions.</li>" +
                "<li>To export your slideshow, navigate to File -> Export Slideshow. From there, give your slideshow a name and click save!<br>Your slideshow is now stored in the same folder as your image and audio files.</li></ol>" +
                "NOTE: The left column of blocks in the \"Timing\" area of the screen, present when creating an automatic slideshow, represents slides. The right column represents audio clips.<br></html>";
        tutorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, tutorialMsg, "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        helpMenu.add(tutorial);

        ImageIcon windowIcon = new ImageIcon("images\\SlideshowIcon.png");
        // TODO: Uncomment below for JAR
        //ImageIcon windowIcon = new ImageIcon(getClass().getClassLoader().getResource("SlideshowIcon.png"));
        Image icon = windowIcon.getImage();
        setIconImage(icon);
        setSize(new Dimension(1500, 750));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);


        String welcomeMsg = "<html><div style='text-align: center;'>Welcome to the greatest slideshow creator ever made.<br>To start creating a slideshow, " +
                "use the \"File\" menu in the top left corner<br>and select \"Set Directory\" and find the directory containing the images and audio you'd like to work with.<br>" +
                "This will also be the directory you'll save your slideshow into.<br>Go ahead. Select one. (You know you want to.)</div></html>";
        JOptionPane.showMessageDialog(null, welcomeMsg, "Welcome to Slideshow Editor!", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * This function is used to pop-up the settings panel
     */
    private void settingsPopup()
    {
        settingsFrame = new JFrame("Slideshow Settings");
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints  c = new GridBagConstraints();
        // Set initial parameters for grid bag constraints
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 0.5;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.CENTER;

        JCheckBox automatedCheckBox = new JCheckBox("Automatic Slideshow");

        JLabel slideIntervalLabel = new JLabel("Default Slide Duration (sec): ");
        JTextField slideIntervalTF = new JTextField(String.valueOf(slideInterval));
        slideIntervalTF.setPreferredSize(new Dimension(50,25));

        if (automated) //only allow user to set a Slide interval if "Automatic Slideshow" is selected
        {
            automatedCheckBox.setSelected(true);
            slideIntervalTF.setEnabled(true);
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

        JButton submitChanges = new JButton("Submit Changes");
        submitChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                automated = automatedCheckBox.isSelected();
                try {
                    slideInterval = Double.parseDouble(slideIntervalTF.getText());
                    if (slideInterval <= 0 && automated)
                    {
                        automated = false;
                        JOptionPane.showMessageDialog(null, "Invalid Slide Interval entered!");
                    }
                    else if(slideInterval > 0) {
	                    timeline.setDefaultSlideDuration(slideInterval);
	                    timeline.setSlideDurationVisible(automated);
	                    timeline.setTimingVisible(automated);
	                    pack();
	                    setLocationRelativeTo(null);
	                    settingsPresent = false;
	                    settingsFrame.dispose();
                    }
                    else {
                    	automated = false;
                    	settingsPresent = false;
                    	timeline.setSlideDurationVisible(automated);
                        timeline.setTimingVisible(automated);
                        pack();
                        setLocationRelativeTo(null);
                    	settingsFrame.dispose();
                    }
                } catch (Exception ex) {
                    if (automated)
                    {
                        automated = false;
                        JOptionPane.showMessageDialog(null, "Invalid Slide Interval entered!");
                    }
                    else {
                        settingsPresent = false;
                        settingsFrame.dispose();
                    }
                }
            }
        });

        settingsPanel.add(automatedCheckBox, c);
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 2;
        settingsPanel.add(slideIntervalLabel, c);
        c.gridx = 2;
        c.gridwidth = 2;
        settingsPanel.add(slideIntervalTF, c);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 3;
        settingsPanel.add(submitChanges, c);
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

        ImageIcon windowIcon = new ImageIcon("images\\SlideshowIcon.png");
        // TODO: Uncomment below for JAR
        //ImageIcon windowIcon = new ImageIcon(getClass().getClassLoader().getResource("SlideshowIcon.png"));
        Image icon = windowIcon.getImage();
        settingsFrame.setIconImage(icon);
        settingsFrame.setSize(new Dimension(290,140));
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setVisible(true);
    }

    /**
     * This function allows the user to select the directory they would like to use when constructing their Slideshow
     */
    private void changeDirectory()
    {
        directory = SlideshowManager.getDirectory(this); //use SlideshowManager to select a directory

        if (directory != null) //if a valid directory was selected...
        {
            JFrame loading = new JFrame("Loading...");
            Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
            loading.setIconImage(icon);
            loading.setResizable(false);
            loading.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            loading.setSize(new Dimension(250,30));
            loading.setLocationRelativeTo(null);
            loading.setVisible(true);

            timeline.reset();
            timeline.setSlideDurationVisible(automated);
            timeline.setDefaultSlideDuration(slideInterval);

            m_ImageLibrary = ImageLibrary.resetLibrary(timeline, directory); //...reset the libraries to purge the current contents...
            JScrollPane spImages = new JScrollPane(m_ImageLibrary);
            spImages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            spImages.getVerticalScrollBar().setUnitIncrement(20);
            libraries.remove(0);
            libraries.add("Images", spImages);
            
            m_AudioLibrary.resetAudio();
            m_AudioLibrary = AudioLibrary.resetLibrary(timeline, directory);
            JScrollPane spAudio = new JScrollPane(m_AudioLibrary);
            spAudio.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            spAudio.getVerticalScrollBar().setUnitIncrement(20);
            libraries.remove(0);
            libraries.add("Audio", spAudio);

            loading.dispose();
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
