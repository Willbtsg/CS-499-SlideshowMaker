package slideshow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

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
     * JLabel m_imageLabel- label where slide images are displayed
     * JLabel m_slideCount- label where slide count is displayed
     * Slideshow m_Slideshow- contains information necessary for image and audio playback
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
     * boolean hasBeenPlayed- used to indicate whether or not the automated Slideshow has been started (or if a manual show's music has been started)
     * boolean m_automated- indicates whether or not the Player is in automated or manual playback mode
     * boolean m_transitionRunning- indicates whether or not a Transition thread is active
     * boolean m_pauseQueued- flag used to indicate if the user has tried to pause during a Transition
     */
    private static SlideshowPlayer instance;
    private JLabel m_imageLabel;
    private JLabel m_slideCount;
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
    private boolean hasBeenPlayed;
    private boolean m_automated;
    private boolean m_transitionRunning;
    private boolean m_pauseQueued;

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
    private SlideshowPlayer() {
        try { //set theme for SlideshowPlayer
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageIcon windowIcon = new ImageIcon("images\\SlideshowIcon.png");
        // TODO: Uncomment below for JAR
        //ImageIcon windowIcon = new ImageIcon(getClass().getClassLoader().getResource("SlideshowIcon.png"));
        Image icon = windowIcon.getImage();
        setIconImage(icon);

        int scrnWidth = 1400;
        int scrnHeight = 800;

        setTitle("Slideshow Player");
        setLayout(null);

        JMenuBar topMenu = new JMenuBar(); //create a menu bar for the window
        setJMenuBar(topMenu);

        JMenu fileMenu = new JMenu("File");
        topMenu.add(fileMenu);

        JMenuItem openShow = new JMenuItem("Open Slideshow"); //allow user to set directory for Slideshow creation
        openShow.addActionListener(event -> initializeSlideshow());
        openShow.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)); //add keyboard shortcut Ctrl + O
        fileMenu.add(openShow);

        JMenuItem closeProgram = new JMenuItem("Exit"); //add Exit Program button to File menu
        closeProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(closeProgram);

        JMenu helpMenu = new JMenu("Help");
        topMenu.add(helpMenu);

        JMenuItem tutorial = new JMenuItem("Tutorial");
        String tutorialMsg = "<html>Playing a slideshow is easy!<br><ol><li>Select File -> Open Slideshow.</li>" +
                "<li>Navigate to the folder containing your slideshow and click \"Open\".</li>" +
                "<li>Select the slideshow you'd like to play from the dropdown list of options.</li>" +
                "<li>If you've opened a manual slideshow, click the forward and backward buttons to go through the show.<br>If the manual slideshow contains music, click the play/pause button to play/pause the music.</li>" +
                "<li>If you've opened an automatic slideshow, click the play/pause button to play/pause the show.<br>You can also skip through the show using the forward skip and backward skip buttons.</li></ol></html>";
        tutorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, tutorialMsg, "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        helpMenu.add(tutorial);

        m_imageLabel = new JLabel();
        m_imageLabel.setBounds(0, 0, scrnWidth, (int) (scrnHeight * 0.85));
        add(m_imageLabel);

        m_controlPanel = new JPanel(new GridBagLayout());
        m_controlPanel.setBounds(0, (int) (scrnHeight*0.85), scrnWidth, (int)(scrnHeight*0.08));

        add(m_controlPanel);

        m_Slideshow = new Slideshow();
        m_Jukebox = Jukebox.getInstance();

        //Change appearance of JFrame
        setSize(scrnWidth, scrnHeight);
        setLocationRelativeTo(null);
        setResizable(false); //disable maximize button
        setVisible(true); //making the frame visible

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        m_paused = false;
        m_automated = false;
        m_transitionRunning = false;
        m_pauseQueued = false;
        m_currentSlideIndex = -1;

        String welcomeMsg = "<html><div style='text-align: center;'>Welcome to the greatest slideshow player ever made.<br>To start playing a slideshow, " +
                "use the \"File\" menu in the top left corner<br>and select \"Open Slideshow\" to find the directory containing the slideshow you'd like to play.<br>" +
                "From there, you'll use the given dropdown box to select<br>which slideshow in that directory you'd like to play.<br>Go ahead. Select one. (You know you want to.)</div></html>";
        JOptionPane.showMessageDialog(null, welcomeMsg, "Welcome to Slideshow Player!", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This function is used to start the Slideshow whenever the user picks a new one. If the user doesn't pick a new show,
     * it resumes any currently executing Slideshows
     */
    public void initializeSlideshow()
    {
        boolean needToResume = false; //flag used to indicate if a automated Slideshow will need to be resumed if file selection is aborted

        if (m_automated && !m_paused) //if Slideshow is automated, pause everything
        {
            m_Pause.doClick();
            needToResume = true;
        } else {
            m_Jukebox.pausePlayback(); //if the Slideshow is manual, just pause the music
        }

        String tempPath = SlideshowManager.selectSlideshow(this); //get the user's selection

        if (tempPath != null) //if the user made a selection (as opposed to choosing to exit and resume the current Slideshow)
        {
            JFrame loading = new JFrame("Loading...");
            Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
            loading.setIconImage(icon);
            loading.setResizable(false);
            loading.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            loading.setSize(new Dimension(250,30));
            loading.setLocationRelativeTo(null);
            loading.setVisible(true);

            Slideshow tempShow = m_Slideshow;
            m_Slideshow = SlideshowManager.getSlideshow(tempPath); //construct Slideshow using the layout file

            if (m_Slideshow == null) //if the data in the Slideshow file was unusable, say so
            {
                loading.dispose();
                String errorMessage = "<html><div style='text-align: center;'>Error constructing slideshow.<br>" +
                        "The data in this file has been corrupted or references an image/audio file<br>" +
                        "that no longer exists.</div></html>";

                JOptionPane.showMessageDialog(null, errorMessage, "Error Loading Slideshow", JOptionPane.ERROR_MESSAGE);

                m_Slideshow = tempShow;
                if (m_Slideshow.getAutomated() && !m_paused)
                {
                    m_slideStart = System.currentTimeMillis() - m_timeElapsed; //offset Timer start to account for Pause
                    m_automationTimer.start();
                }
                if (!m_paused)
                    m_Jukebox.resumePlayback();
                return;
            }
            else if (!m_Slideshow.getProgenitor().equals("SlideshowEditor"))
            {
                loading.dispose();
                String errorMessage = "<html><div style='text-align: center;'>Error constructing slideshow.<br>" +
                        "The file you selected was not created by the Slideshow Editor.</div></html>";

                JOptionPane.showMessageDialog(null, errorMessage, "Error Loading Slideshow", JOptionPane.ERROR_MESSAGE);

                m_Slideshow = tempShow;
                if (m_Slideshow.getAutomated() && !m_paused)
                {
                    m_slideStart = System.currentTimeMillis() - m_timeElapsed; //offset Timer start to account for Pause
                    m_automationTimer.start();
                }
                if (!m_paused)
                    m_Jukebox.resumePlayback();
                return;
            }

            hasBeenPlayed = false;
            m_Jukebox.stopPlayback(); //completely stop the Jukebox

            // Reset control panel
            m_controlPanel.removeAll();
            m_controlPanel.revalidate();
            m_controlPanel.repaint();

            m_automated = m_Slideshow.getAutomated();

            if (m_automated) //see if Slideshow is set for automated playback...
            {
                setAutomatedControls(); //...if it is, configure the controls for automated playback
            } else {
                setManualControls(); //...if it isn't, configure the controls for manual playback
            }
            m_slideCount.setMinimumSize(new Dimension(50, 15));
            m_slideCount.setPreferredSize(new Dimension(50, 15));
            m_slideCount.setMaximumSize(new Dimension(50, 15));

            m_Jukebox.setSoundList(m_Slideshow.getSoundList()); //load Jukebox with sound data

            m_currentSlideIndex = -1; //initialize index
            changeSlide(1, false);

            loading.dispose();

        } else if (m_currentSlideIndex != -1) { //if the user didn't pick a new Slideshow, resume the current one

            m_imageLabel.setIcon(new ImageIcon(m_Slideshow.getSlide(m_currentSlideIndex).getImage()));

            if (needToResume) //if an automated Slideshow was paused to pull up the file menu, resume playback
            {
                m_Pause.doClick();
            } else if (!m_automated && !m_paused) //if a manual Slideshow's music was paused to pull up the file menu, resume playback
            {
                m_Jukebox.resumePlayback();
            }
        }
    }

    /**
     * Calls current Slide's Transition to update the display.
     * @param indexShift- indicates whether to get next or previous slide
     * @param skip- indicates whether or not checking for a Transition is required
     * @throws IndexOutOfBoundsException- trows exception when performing next/previous slide at end/beginning of Slideshow
     */
    public boolean doImageTransition(int indexShift, boolean skip) throws IndexOutOfBoundsException
    {
        int tempIndex = m_currentSlideIndex + indexShift; //use tempIndex in case requested index is invalid
        boolean usedTransition = false; //flag to signal if a Transition was used

        try {
            if (!skip) {
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
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }

        return true;
    }

    /**
     * Retrieves the desired Slide (next or previous) from m_SlideList and updates timers (if Slideshow is automated).
     * Runs in a thread so it doesn't lock up the GUI.
     * @param indexShift- indicates whether to get next or previous slide
     * @param skip- indicates whether or not checking for a Transition is required
     */
    private void changeSlide(int indexShift, boolean skip)
    {
        if (!m_transitionRunning) //only start new Transition thread if one isn't currently running
        {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    m_transitionRunning = true;

                    if (m_automated) //use this logic if the Slideshow is automated
                    {
                        if (m_automationTimer != null)
                        {
                            m_automationTimer.stop(); //stop current Timer
                            m_timeElapsed = 0;
                        }

                        boolean changed; //flag used to indicate if image was actually changed

                        changed = doImageTransition(indexShift, skip); //perform the Transition set between the two Slides

                        if (changed) //if this new Slide is not the last one...
                        {
                            if (m_automationTimer != null)
                            {
                                m_automationTimer.setInitialDelay((int) m_Slideshow.getSlide(m_currentSlideIndex).getTime());
                                m_slideStart = System.currentTimeMillis();
                                m_automationTimer.start(); //...start the Timer with the new Slide's delay
                            }
                        } else if (m_currentSlideIndex != 0) //if the Slideshow is now over...
                        {
                            m_Jukebox.stopPlayback(); //...stop the Jukebox...

                            //...thank the user for using our program...
                            JOptionPane.showMessageDialog(null, "<html><div style='text-align: center;'>This " +
                                    "Slideshow is now over.<br>Thank you for using our program.</div></html>", "Have a Nice Day!", JOptionPane.PLAIN_MESSAGE);

                            m_currentSlideIndex = -1; //reset the Slideshow to the beginning
                            m_Jukebox.playAll(); //...cue the music...

                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            m_transitionRunning = false; //reset this flag to false so the Slideshow can be reset
                            changeSlide(1, true); //...after giving the Jukebox a brief time to prepare, reset the Slides to the start...

                            m_paused = false; //reset this flag so the Slideshow can be paused at its beginning
                            m_Pause.doClick(); //...and pause the Slideshow at the beginning

                        } else { //if hitting Previous Slide on first slide, restart the Slide's timer
                            m_automationTimer.setInitialDelay(m_automationTimer.getDelay());
                            m_slideStart = System.currentTimeMillis();
                            m_automationTimer.start();
                        }

                        if (m_pauseQueued) //if the user tried to pause during the Transition, pause now that the Transition is over
                        {
                            m_pauseQueued = false;
                            m_transitionRunning = false;
                            m_Pause.doClick();
                        }

                    } else { //if the Slideshow is manual, use this logic
                        doImageTransition(indexShift, skip);
                    }

                    m_transitionRunning = false;
                    updateSlideCount(); //update Slide-counting label

                }

            }).start();
        }
    }

    /**
     * Update the slide count label
     */
    private void updateSlideCount() {
        m_slideCount.setText((m_currentSlideIndex + 1) + " of " + m_Slideshow.getSlideList().size());
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
        Image newimg = image.getScaledInstance(16, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        ImageIcon nextIcon = new ImageIcon(newimg);

        image = tempPrevIcon.getImage(); // transform it
        newimg = image.getScaledInstance(16, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        ImageIcon prevIcon = new ImageIcon(newimg);

        m_nextSlide = new JButton();
        m_nextSlide.setIcon(nextIcon);
        c.gridx = 3;
        m_controlPanel.add(m_nextSlide, c);

        m_nextSlide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                changeSlide(1, true); //show the previous timed Slide

                if (m_paused) //if the Slideshow is paused, stop the timer that just started
                {
                    m_paused = false;
                    m_Pause.doClick();
                }
            }
        });

        m_previousSlide = new JButton();
        m_previousSlide.setIcon(prevIcon);
        c.gridx = 1;
        m_controlPanel.add(m_previousSlide, c);

        m_previousSlide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                changeSlide(-1, true); //show the previous timed Slide

                if (m_paused) //if the Slideshow is paused, stop the timer that just started
                {
                    m_paused = false;
                    m_Pause.doClick();
                }
            }
        });

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
        c.gridx = 2;
        m_controlPanel.add(m_Pause, c);

        //Set icon for pause button
        m_Pause.setIcon(playIcon);

        m_Pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (m_paused) //if the Slideshow is already paused, resume it and the Jukebox
                {
                    if (!hasBeenPlayed)
                    {
                        ActionListener taskPerformer = new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {

                                changeSlide(1, false); //when Timer goes off, load new Slide and start Timer again

                            }
                        };

                        m_automationTimer = new Timer((int) m_Slideshow.getSlide(m_currentSlideIndex).getTime(), taskPerformer);
                        m_automationTimer.start(); //start a Timer that lasts the amount of time Slide should display
                        m_slideStart = System.currentTimeMillis();
                        m_paused = false;
                        hasBeenPlayed = true;
                        m_Pause.setIcon(pauseIcon);
                        m_Jukebox.playAll(); //play the Jukebox with all its new music
                        m_nextSlide.setEnabled(true);
                        m_previousSlide.setEnabled(true);
                    }
                    else
                    {
                        m_slideStart = System.currentTimeMillis() - m_timeElapsed; //offset Timer start to account for Pause
                        m_automationTimer.start();
                        m_Jukebox.resumePlayback();
                        m_Pause.setIcon(pauseIcon);
                        m_paused = false;
                    }
                }
                else if (m_transitionRunning) {
                    m_pauseQueued = true;
                }
                else { //otherwise, pause the Slideshow and the Jukebox if a Transition isn't in progress

                    m_automationTimer.stop(); //stop Slide timer and update delay so it doesn't reset when resumed
                    m_timeElapsed = System.currentTimeMillis() - m_slideStart; //calculate amount of time Slide has been active

                    m_automationTimer.setInitialDelay((int) (m_Slideshow.getSlide(m_currentSlideIndex).getTime() - m_timeElapsed));

                    m_Jukebox.pausePlayback();
                    m_Pause.setIcon(playIcon);
                    m_paused = true;
                }
            }
        });

        // Initialize the slidecount label
        c.gridx = 0;
        m_slideCount = new JLabel((m_currentSlideIndex + 1) + " of " + m_Slideshow.getSlideList().size());
        m_controlPanel.add(m_slideCount, c);

        //Create empty label to get the spacing right
        c.gridx = 4;
        JLabel spaceFill = new JLabel("");
        spaceFill.setMinimumSize(new Dimension(50, 15));
        spaceFill.setPreferredSize(new Dimension(50, 15));
        spaceFill.setMaximumSize(new Dimension(50, 15));
        m_controlPanel.add(spaceFill, c);

        m_nextSlide.setEnabled(false);
        m_previousSlide.setEnabled(false);

        m_paused = true;
    }

    /**
     * This function specifies JButton behavior to be used when a Slideshow is set for manual playback
     */
    private void setManualControls()
    {
        if (m_Slideshow.getSoundList().size() > 0)
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
            c.gridx = 3;
            m_controlPanel.add(m_nextSlide, c);

            m_nextSlide.addActionListener(event -> changeSlide(1, false));

            m_previousSlide = new JButton();
            m_previousSlide.setIcon(prevIcon);
            c.gridx = 1;
            m_controlPanel.add(m_previousSlide, c);

            m_previousSlide.addActionListener(event -> changeSlide(-1, false));

            ImageIcon tempPlayIcon = new ImageIcon("images\\playbuttonicon.png");
            ImageIcon tempPauseIcon = new ImageIcon("images\\pausebuttonicon.png");

            image = tempPlayIcon.getImage(); // transform it
            newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            ImageIcon playIcon = new ImageIcon(newimg);

            image = tempPauseIcon.getImage(); // transform it
            newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            ImageIcon pauseIcon = new ImageIcon(newimg);

            m_Pause = new JButton("Music");
            c.gridx = 2;
            m_controlPanel.add(m_Pause, c);

            //Set icon for pause button
            m_Pause.setIcon(playIcon);

            m_Pause.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (m_paused) //if the Jukebox is already paused, resume it
                    {
                        if (!hasBeenPlayed)
                        {
                            m_paused = false;
                            hasBeenPlayed = true;
                            m_Pause.setIcon(pauseIcon);
                            m_Jukebox.playAll(); //play the Jukebox with all its new music
                        }
                        else {
                            m_Jukebox.resumePlayback();
                            m_Pause.setIcon(pauseIcon);
                            m_paused = false;
                        }
                    }
                    else { //otherwise, pause the Jukebox

                        m_Jukebox.pausePlayback();
                        m_Pause.setIcon(playIcon);
                        m_paused = true;
                    }
                }
            });

            // Initialize the slidecount label
            c.gridx = 0;
            m_slideCount = new JLabel((m_currentSlideIndex + 1) + " of " + m_Slideshow.getSlideList().size());
            m_controlPanel.add(m_slideCount, c);

            //Create empty label to get the spacing right
            c.gridx = 4;
            JLabel spaceFill = new JLabel("");
            spaceFill.setMinimumSize(new Dimension(50, 15));
            spaceFill.setPreferredSize(new Dimension(50, 15));
            spaceFill.setMaximumSize(new Dimension(50, 15));
            m_controlPanel.add(spaceFill, c);
        }
        else {
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

            m_nextSlide.addActionListener(event -> changeSlide(1, false));

            m_previousSlide = new JButton();
            m_previousSlide.setIcon(prevIcon);
            c.gridx = 1;
            m_controlPanel.add(m_previousSlide, c);

            m_previousSlide.addActionListener(event -> changeSlide(-1, false));

            // Initialize the slidecount label
            c.gridx = 0;
            m_slideCount = new JLabel((m_currentSlideIndex + 1) + " of " + m_Slideshow.getSlideList().size());
            m_controlPanel.add(m_slideCount, c);

            //Create empty label to get the spacing right
            c.gridx = 3;
            JLabel spaceFill = new JLabel("");
            spaceFill.setMinimumSize(new Dimension(50, 15));
            spaceFill.setPreferredSize(new Dimension(50, 15));
            spaceFill.setMaximumSize(new Dimension(50, 15));
            m_controlPanel.add(spaceFill, c);
        }

        m_paused = true;
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
