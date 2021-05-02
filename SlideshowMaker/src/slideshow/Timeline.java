package slideshow;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.util.*;

/**
 * Class name: Timeline
 *
 * This class allows users to manage the sound and image files they have added to their Slideshow
 */

public class Timeline extends JPanel {

    /**
     * Timeline instance- instance of Timeline for Singleton
     * JTabbedPane timelinePanes- JTabbedPane containing the separate panels used for the Slide and Audio timelines
     * ArrayList<JPanel> timelineSlides- contains slide data to be displayed in Timeline's "Slides" tab
     * ArrayList<JPanel> timelineAudio- contains sound data to be displayed in Timeline's "Audio" tab
     * JPanel slidePanel- JPanel used to display timelineSlides
     * JPanel audioPanel- JPanel used to display timelineAudio
     * JScrollPane slideScroll- scrollPane used to wrap slidePanel
     * JScrollPane audioScroll- scrollPane used to wrap audioPanel
     * ArrayList<Slide> slideList- list of Slide objects to be used in a Slideshow
     * ArrayList<String> soundList- list of sound filepaths to be used in a Slideshow
     * ArrayList<JPanel> slideDurationPanels- JPanels used for displaying slide interval data in Timeline items
     * ArrayList<JLabel> slideDurations- JLabels from Timeline items containing that item's set interval
     * ArrayList<JComboBox> transitionDurations- JComboBoxes from Timeline items containing Transition lengths
     * JLabel runtimeLabel- label used to to display total audio and (if automated) slideshow runtimes
     * double totalSlideTime- total time (in seconds) that the automated Slideshow will last
     * int totalAudioTime- total time(in seconds) that the soundtrack for the Slideshow will last
     * double defaultSlideDuration- default slide interval for Timeline items in an automated Slideshow
     * boolean automated- flag used to indicate whether the Slideshow being created will be automated or not
     */
    private static Timeline instance;
    private JTabbedPane timelinePanes;
    private ArrayList<JPanel> timelineSlides;
    private ArrayList<JPanel> timelineAudio;
    private JPanel slidePanel;
    private JPanel audioPanel;
    private JScrollPane slideScroll;
    private JScrollPane audioScroll;
    private ArrayList<Slide> slideList;
    private ArrayList<String> soundList;
    private ArrayList<JPanel> slideDurationPanels;
    private ArrayList<JLabel> slideDurations;
    private ArrayList<JCheckBox> defaultDurationChecks;
    private ArrayList<JComboBox> transitionDurations;
    private JLabel runtimeLabel;
    private JPanel runtimeGraph;
    private JScrollPane runtimeScroll;
    private JTabbedPane runtime;
    private ArrayList<JPanel> slideTimings;
    private ArrayList<JLabel> lblSlideTimings;
    private ArrayList<JPanel> audioTimings;
    private ArrayList<JLabel> lblAudioTimings;
    private JPanel slideTimes;
    private JPanel audioTimes;
    private double totalSlideTime = 0.0;
    private int totalAudioTime = 0;
    private Double defaultSlideDuration = 3.0;
    private boolean automated = false;

    /////////////
    // METHODS //
    /////////////

    /**
     * Constructor for Timeline. Creates scrollpanes used to display Slideshow information
     */
    private Timeline()
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Timeline"));
        setSize(420, 750);

        JPanel items = new JPanel();
        items.setLayout(new BorderLayout());

        timelinePanes = new JTabbedPane();
        timelineSlides = new ArrayList<JPanel>(); //create list of objects to display Slide data
        timelineAudio = new ArrayList<JPanel>(); //create list of object to display sound data
        slidePanel = new JPanel(); //create JPanel for Slide tab
        audioPanel = new JPanel(); //create JPanel for Audio tab

        slideList = new ArrayList();
        soundList = new ArrayList();

        slideTimings = new ArrayList<>();
        lblSlideTimings = new ArrayList<>();

        audioTimings = new ArrayList<>();
        lblAudioTimings = new ArrayList<>();

        slideDurationPanels = new ArrayList<JPanel>();
        slideDurations = new ArrayList<JLabel>();
        defaultDurationChecks = new ArrayList<>();
        transitionDurations = new ArrayList();

        GridLayout grid = new GridLayout(2,1); //layout to be used for Slide and Audio tabs
        slidePanel.setLayout(grid); //set tab layouts
        audioPanel.setLayout(grid);

        slideScroll = new JScrollPane(slidePanel); //create scroll panel for slidePanel
        slideScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        slideScroll.getVerticalScrollBar().setUnitIncrement(20);
        slideScroll.setPreferredSize(new Dimension(320, 720));
        timelinePanes.add("Slides", slideScroll);

        audioScroll = new JScrollPane(audioPanel); //create scroll panel for audioPanel
        audioScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        audioScroll.getVerticalScrollBar().setUnitIncrement(20);
        audioScroll.setPreferredSize(new Dimension(320, 720));
        timelinePanes.add("Audio", audioScroll);

        items.add(timelinePanes, BorderLayout.NORTH);

        runtimeLabel = new JLabel();
        updateRuntimeLabel();

        items.add(runtimeLabel, BorderLayout.SOUTH);

        add(items, BorderLayout.WEST);

        runtimeGraph = new JPanel();
        runtimeGraph.setLayout(new BorderLayout());

        JPanel timingWrapper = new JPanel();
        timingWrapper.setLayout(new GridLayout(0,2));
        slideTimes = new JPanel();
        audioTimes = new JPanel();
        slideTimes.setLayout(new BoxLayout(slideTimes, BoxLayout.Y_AXIS));
        audioTimes.setLayout(new BoxLayout(audioTimes, BoxLayout.Y_AXIS));
        timingWrapper.add(slideTimes);
        timingWrapper.add(audioTimes);
        runtimeGraph.add(timingWrapper, BorderLayout.NORTH);

        runtimeScroll = new JScrollPane(runtimeGraph);
        runtimeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        runtimeScroll.getVerticalScrollBar().setUnitIncrement(20);
        runtimeScroll.setPreferredSize(new Dimension(100, 720));

        runtime = new JTabbedPane();
        runtime.add("Timing", runtimeScroll);
    }

    /**
     * Used by ImageLibrary to add new image to the Slideshow. Creates JPanel with Slide information to display in slidePanel
     * @param filePath- filepath of image being added to Timeline
     */
    public void addSlide(String filePath) {
        Slide thisSlide = new Slide(filePath);
        slideList.add(thisSlide);

        File image = new File(filePath); //retrieve image

        JPanel thisSlideDisplay = new JPanel(); //create new panel to display slide information
        thisSlideDisplay.setLayout(new BorderLayout());

        JPanel buttonsAndTitle = new JPanel();
        buttonsAndTitle.setLayout(new BorderLayout());

        JLabel imgTitle = new JLabel(image.getName(), SwingConstants.CENTER);
        imgTitle.setBorder(new EmptyBorder(5, 0, 5, 0));
        buttonsAndTitle.add(imgTitle, BorderLayout.NORTH);

        JPanel slideDuration = new JPanel();
        slideDuration.setLayout(new BorderLayout());
        JCheckBox defaultDuration = new JCheckBox("Use Default Slide Duration");
        defaultDuration.setSelected(true);
        slideDuration.add(defaultDuration, BorderLayout.NORTH);
        defaultDurationChecks.add(defaultDuration);

        JPanel durationAdjust = new JPanel();
        durationAdjust.setLayout(new BorderLayout());
        JLabel lblHeader = new JLabel("Duration (sec): ");
        JLabel durationLabel = new JLabel("", SwingConstants.CENTER);
        JButton changeDuration = new JButton("Change Duration");
        slideDurations.add(durationLabel);
        durationAdjust.add(lblHeader, BorderLayout.WEST);
        durationAdjust.add(durationLabel, BorderLayout.CENTER);
        durationAdjust.add(changeDuration, BorderLayout.EAST);
        durationAdjust.setVisible(false);
        slideDuration.add(durationAdjust, BorderLayout.SOUTH);

        defaultDuration.addActionListener(new ActionListener() //only show textfield for different slide interval if "Use Default" is unchecked
        {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    totalSlideTime -= Double.parseDouble(durationLabel.getText());
                } catch (Exception ex) {
                    totalSlideTime -= defaultSlideDuration;
                }

                totalSlideTime += defaultSlideDuration;
                durationLabel.setText(String.valueOf(defaultSlideDuration));
                updateSlideTiming(slideList.indexOf(thisSlide));
                updateRuntimeLabel();

                if (defaultDuration.isSelected()) {
                    durationAdjust.setVisible(false);
                } else {
                    durationAdjust.setVisible(true);
                }
            }
        });

        changeDuration.addActionListener(new ActionListener() //adds modal popup for changing Slide interval
        {
            @Override
            public void actionPerformed(ActionEvent e) {

                String newValue = JOptionPane.showInputDialog(null, "Enter New Slide Interval");

                try
                {
                    if (Double.parseDouble(newValue) > 0) //change set interval if users selects a valid duration greater than zero
                    {
                        totalSlideTime -= Double.parseDouble(durationLabel.getText());
                        totalSlideTime += Double.parseDouble(newValue);
                        durationLabel.setText(newValue);
                        updateSlideTiming(slideList.indexOf(thisSlide));
                        updateRuntimeLabel();
                    }
                }
                catch (Exception ex) //inform user that their input was invalid
                {
                    JOptionPane.showMessageDialog(null, "Invalid interval. Slide will not be updated.");
                }
            }
        });

        slideDuration.setVisible(automated);
        slideDurationPanels.add(slideDuration);
        buttonsAndTitle.add(slideDuration, BorderLayout.SOUTH);

        if (automated)
        {
            totalSlideTime += defaultSlideDuration;
            updateRuntimeLabel();
        }

        JPanel transitionDropdowns = new JPanel();
        transitionDropdowns.setLayout(new BorderLayout()); //create panel for Transition settings

        JPanel transitionDropdownLabels = new JPanel(); //create label for Transition dropdown boxes
        transitionDropdownLabels.setLayout(new BorderLayout());
        JLabel transSelectLabel = new JLabel("Transition Type:");
        transSelectLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
        transitionDropdownLabels.add(transSelectLabel, BorderLayout.WEST);
        JLabel transLengthLabel = new JLabel("Transition Length (sec):", SwingConstants.CENTER);
        transLengthLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
        transitionDropdownLabels.add(transLengthLabel, BorderLayout.EAST);
        transitionDropdowns.add(transitionDropdownLabels, BorderLayout.NORTH);

        JPanel transitionComboBoxes = new JPanel(); //create panel for Transition dropdowns
        transitionComboBoxes.setLayout(new BorderLayout());

        String transitionOptions[] = { "None", "Wipe Right", "Wipe Left", "Wipe Up", "Wipe Down", "Crossfade",
                "Wipe Down & Right", "Wipe Up & Left", "Wipe Down & Left", "Wipe Up & Right",
                "Horizontal Open", "Horizontal Close", "Vertical Open", "Vertical Close" }; //array of Transitions to choose from

        JComboBox<String> transSelect = new JComboBox<>(transitionOptions);
        transSelect.setPreferredSize(new Dimension(130, 20));
        transitionComboBoxes.add(transSelect, BorderLayout.WEST); //add dropdown with Transition options

        Double lengthOptions[] = {0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
        JComboBox transLength = new JComboBox(lengthOptions);
        transLength.setPreferredSize(new Dimension(130, 20));
        transLength.setEnabled(false);
        transitionComboBoxes.add(transLength, BorderLayout.EAST); //add dropdown with Transition timing options
        transitionDurations.add(transLength);

        transSelect.addActionListener(new ActionListener() //change corresponding Slide Transition whenever dropdown is changed
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userTrans = (String) transSelect.getSelectedItem();
                String transition = "None"; //set None as default for if no actual Transition is selected

                switch (userTrans) //use switch statement to convert dropdown selection into format expected by backend
                {
                    case "Wipe Right":
                        transition = "LRWipe";
                        break;
                    case "Wipe Left":
                        transition = "RLWipe";
                        break;
                    case "Wipe Up":
                        transition = "UpWipe";
                        break;
                    case "Wipe Down":
                        transition = "DownWipe";
                        break;
                    case "Crossfade":
                        transition = "CrossFade";
                        break;
                    case "Wipe Down & Right":
                        transition = "DRWipe";
                        break;
                    case "Wipe Up & Left":
                        transition = "ULWipe";
                        break;
                    case "Wipe Down & Left":
                        transition = "DLWipe";
                        break;
                    case "Wipe Up & Right":
                        transition = "URWipe";
                        break;
                    case "Horizontal Open":
                        transition = "HZOpen";
                        break;
                    case "Horizontal Close":
                        transition = "HZClose";
                        break;
                    case "Vertical Open":
                        transition = "VTOpen";
                        break;
                    case "Vertical Close":
                        transition = "VTClose";
                        break;
                }

                thisSlide.setTransitions(transition); //set Transition information in Slide object

                if (transSelect.getSelectedItem() != "None") //only have timing dropdown enabled if a transition is in use
                {
                    transLength.setEnabled(true);
                }
                else
                {
                    transLength.setEnabled(false);
                }


                updateSlideTiming(slideList.indexOf(thisSlide));

                Double transitionLength = (Double) transLength.getSelectedItem(); //make sure Transition timing is saved when switching
                transitionLength *= 1000;
                long transitionLengthMs = transitionLength.longValue();
                thisSlide.setTransitionLength(transitionLengthMs); //set Transition length in milliseconds in Slide object
                updateRuntimeLabel();
            }
        });

        transLength.addActionListener(new ActionListener() //update Slide's Transition timing when dropdown is changed
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                Double transitionLength = (Double) transLength.getSelectedItem();
                updateSlideTiming(slideList.indexOf(thisSlide));
                transitionLength *= 1000;
                long transitionLengthMs = transitionLength.longValue();
                thisSlide.setTransitionLength(transitionLengthMs); //set Transition length in milliseconds in Slide object
                updateRuntimeLabel();
            }
        });

        transitionDropdowns.add(transitionComboBoxes, BorderLayout.SOUTH);

        thisSlideDisplay.add(transitionDropdowns, BorderLayout.NORTH);

        JPanel buttons = new JPanel();
        JButton moveUp = new JButton("Move Up"); //create button to move slide towards beginning of Timeline

        moveUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentItemIndex = timelineSlides.indexOf(thisSlideDisplay); //get index of selected Slide
                if (currentItemIndex > 0) //if the Slide can be moved up
                {
                    moveSlideTimingUp(currentItemIndex);
                    for (int i = 0; i < timelineSlides.size(); i++) {
                        slidePanel.remove(timelineSlides.get(i)); //clear the display
                    }

                    timelineSlides.remove(currentItemIndex);
                    timelineSlides.add(currentItemIndex - 1, thisSlideDisplay); //adjust the slide's display position in the backend
                    slideList.remove(currentItemIndex);
                    slideList.add(currentItemIndex - 1, thisSlide); //also update position of Slide item itself
                    slideDurations.remove(currentItemIndex);
                    slideDurations.add(currentItemIndex - 1, durationLabel);

                    for (int i = 0; i < timelineSlides.size(); i++) {
                        timelineSlides.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i + 1)));
                        slidePanel.add(timelineSlides.get(i)); //add all slides back int he new order
                    }

                    revalidate(); //update the GUI
                }
            }
        });

        JButton deleteButton = new JButton("Remove"); //create button to remove slide from Timeline

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int removeIndex = timelineSlides.indexOf(thisSlideDisplay);
                slidePanel.remove(thisSlideDisplay); //remove slide from display
                timelineSlides.remove(thisSlideDisplay); //remove slide from Timeline database
                slideDurationPanels.remove(slideDuration);
                slideDurations.remove(durationLabel);
                transitionDurations.remove(transLength);

                if (automated) //if Slideshow is automated, update the total runtime
                {
                    try {
                        totalSlideTime -= Double.parseDouble(durationLabel.getText());
                    } catch (Exception ex) {
                        totalSlideTime -= defaultSlideDuration;
                    }
                    updateRuntimeLabel();
                }

                if (timelineSlides.size() == 1) {
                    slidePanel.setLayout(new GridLayout(2, 1));
                }

                slideList.remove(thisSlide); //remove from list of Slide data as well as the GUI component

                for (int i = 0; i < timelineSlides.size(); i++) {
                    timelineSlides.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i + 1)));
                    slidePanel.add(timelineSlides.get(i)); //add all slides back in the new order
                }
                removeSlideTiming(removeIndex);
                repaint(); //update display to reflect changes
                revalidate();
            }
        });

        JButton moveDown = new JButton("Move Down"); //create button to move slide towards end of Timeline

        moveDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentItemIndex = timelineSlides.indexOf(thisSlideDisplay);

                if (currentItemIndex < timelineSlides.size() - 1) {
                    moveSlideTimingDown(currentItemIndex);
                    for (int i = 0; i < timelineSlides.size(); i++) {
                        slidePanel.remove(timelineSlides.get(i)); //clear GUI
                    }

                    timelineSlides.remove(currentItemIndex);
                    timelineSlides.add(currentItemIndex + 1, thisSlideDisplay); //adjust the slide's display position in the backend
                    slideList.remove(currentItemIndex);
                    slideList.add(currentItemIndex + 1, thisSlide); //also update position of Slide item itself
                    slideDurations.remove(currentItemIndex);
                    slideDurations.add(currentItemIndex + 1, durationLabel);

                    for (int i = 0; i < timelineSlides.size(); i++) {
                        timelineSlides.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i + 1)));
                        slidePanel.add(timelineSlides.get(i)); //add all slides back to GUI in new order
                    }

                    revalidate(); //update GUI
                }
            }
        });

        buttons.add(moveUp, BorderLayout.WEST);
        buttons.add(deleteButton, BorderLayout.CENTER);
        buttons.add(moveDown, BorderLayout.EAST);

        buttonsAndTitle.add(buttons, BorderLayout.CENTER);

        thisSlideDisplay.add(buttonsAndTitle, BorderLayout.SOUTH); //add control buttons and image name to Panel

        JLabel thumbnail = new JLabel("", SwingConstants.CENTER);
        ImageIcon img = new ImageIcon(filePath);
        double proportion = 200.0 / img.getIconWidth();
        double db_newHeight = proportion * img.getIconHeight();
        int newHeight = (int) Math.round(db_newHeight);
        Image imgIcon = img.getImage().getScaledInstance(200, newHeight, Image.SCALE_REPLICATE);
        img = new ImageIcon(imgIcon);
        thumbnail.setIcon(img);
        thisSlideDisplay.add(thumbnail, BorderLayout.CENTER); //create image icon then add to display

        Border timelineItemBorder = BorderFactory.createTitledBorder(String.valueOf(timelineSlides.size() + 1));
        thisSlideDisplay.setBorder(timelineItemBorder);

        thisSlideDisplay.setPreferredSize(new Dimension(200, 400));
        timelineSlides.add(thisSlideDisplay);

        if (timelineSlides.size() == 2) //set layout to dynamically increase once Timeline has enough items
        {
            slidePanel.setLayout(new GridLayout(0, 1));
        }

        if (timelineSlides.size() > 1) //if necessary, prep scrollpane to adjust to bottom to make it clear new panel was added
        {
            JScrollBar verticalBar = slideScroll.getVerticalScrollBar();
            verticalBar.addAdjustmentListener(new ScrollAdjuster(verticalBar)); //set scrollbar to adjust to bottom to show new data
        }
        
        slidePanel.add(thisSlideDisplay);
        revalidate(); //update GUI after adding new component

        try {
            addSlideTiming(timelineSlides.size(), Double.parseDouble(slideDurations.get(slideDurations.size()-1).getText()));
        } catch (Exception e) {
            addSlideTiming(timelineSlides.size(), defaultSlideDuration);
        }
    }

    /**
     * Adds a new slide timing block to the timing area.
     *
     * @param slideNum The number to be displayed for the new timing block.
     * @param slideLength The length of the slide being represented by the block.
     */
    public void addSlideTiming(int slideNum, double slideLength)
    {
        JPanel slideTiming = new JPanel();
        JLabel lblSlideTiming = new JLabel(String.valueOf(slideNum), SwingConstants.CENTER);
        lblSlideTiming.setVerticalAlignment(SwingConstants.CENTER);
        int timingHeight = (int)(slideLength * 10);
        slideTiming.setMinimumSize(new Dimension(40, timingHeight));
        slideTiming.setPreferredSize(new Dimension(40, timingHeight));
        slideTiming.setMaximumSize(new Dimension(40, timingHeight));
        slideTiming.add(lblSlideTiming);
        slideTiming.setBorder(BorderFactory.createTitledBorder(""));

        slideTimings.add(slideTiming);
        lblSlideTimings.add(lblSlideTiming);

        slideTiming.setAlignmentY(TOP_ALIGNMENT);
        slideTimes.add(slideTiming);
    }

    /**
     * Removes an existing slide timing block.
     *
     * @param slideNum The number of the slide timing block to be removed.
     */
    public void removeSlideTiming(int slideNum)
    {
        lblSlideTimings.remove(slideNum);
        slideTimings.remove(slideNum);
        slideTimes.remove(slideNum);

        for (int i = 0; i < lblSlideTimings.size(); i++)
            lblSlideTimings.get(i).setText(String.valueOf(i+1));
    }

    /**
     * Updates the specified Slide timing block. Called whenever that Slide's information may have been updated
     *
     * @param slideIndex The new default slide length.
     */
    public void updateSlideTiming(int slideIndex)
    {
        double slideTime; //create variable for total Slide time

        try {
            slideTime = Double.parseDouble(slideDurations.get(slideIndex).getText()); //use custom Slide duration...
        } catch (Exception e) {
            slideTime = defaultSlideDuration; //...or just use the default
        }

        if (transitionDurations.get(slideIndex).isEnabled()) {
            slideTime += (Double) transitionDurations.get(slideIndex).getSelectedItem(); //add Transition time if applicable
        }

        slideTime *= 10; //multiply the result by 10 to get height for the timing block

        slideTimings.get(slideIndex).setMinimumSize(new Dimension(40, (int) (slideTime))); //update timing block size
        slideTimings.get(slideIndex).setPreferredSize(new Dimension(40, (int) slideTime));
        slideTimings.get(slideIndex).setMaximumSize(new Dimension(40, (int) slideTime));
        slideTimings.get(slideIndex).repaint();
        slideTimings.get(slideIndex).revalidate(); //make sure changes appear on GUI

    }

    /**
     * Updates timing block for all Slides. Called when the default Slide duration is changed
     */
    public void updateAllSlideTimings()
    {
        for (int i=0; i<slideTimings.size(); i++)
        {
            updateSlideTiming(i);
        }
    }

    /**
     * Moves a slide timing block up the timing area by one step.
     *
     * @param slideNum The number of the slide to be moved up.
     */
    public void moveSlideTimingUp(int slideNum)
    {
        for (int i = 0; i < slideTimings.size(); i++)
            slideTimes.remove(slideTimings.get(i));

        JLabel lblTemp = lblSlideTimings.get(slideNum);
        lblSlideTimings.remove(slideNum);
        lblSlideTimings.add(slideNum-1, lblTemp);

        JPanel pnlTemp = slideTimings.get(slideNum);
        slideTimings.remove(slideNum);
        slideTimings.add(slideNum-1, pnlTemp);

        for (int i = 0; i < slideTimings.size(); i++)
            slideTimes.add(slideTimings.get(i));

        for (int i = 0; i < lblSlideTimings.size(); i++)
            lblSlideTimings.get(i).setText(String.valueOf(i+1));
    }

    /**
     * Moves a slide timing block down the timing area by one step.
     *
     * @param slideNum The number of the slide to be moved down.
     */
    public void moveSlideTimingDown(int slideNum)
    {
        for (int i = 0; i < slideTimings.size(); i++)
            slideTimes.remove(slideTimings.get(i));

        JLabel lblTemp = lblSlideTimings.get(slideNum);
        lblSlideTimings.remove(slideNum);
        lblSlideTimings.add(slideNum+1, lblTemp);

        JPanel pnlTemp = slideTimings.get(slideNum);
        slideTimings.remove(slideNum);
        slideTimings.add(slideNum+1, pnlTemp);

        for (int i = 0; i < slideTimings.size(); i++)
            slideTimes.add(slideTimings.get(i));

        for (int i = 0; i < lblSlideTimings.size(); i++)
            lblSlideTimings.get(i).setText(String.valueOf(i+1));
    }

    /**
     * Used by AudioLibrary when adding new sound to the Timeline
     * @param soundName- name of sound being added to Timeline
     */
    public void addSound(String soundName, int soundLength)
    {
        soundList.add(soundName);

        JPanel thisSound = new JPanel(); //new JPanel to display sound data in Timeline
        thisSound.setLayout(new BorderLayout());

        JPanel buttonsAndTitle = new JPanel();
        buttonsAndTitle.setLayout(new BorderLayout());

        File audioFile = new File(soundName);
        String audioInfo = "<html>" + audioFile.getName() + "<br>(" + calculateMinSecLength(soundLength) + ")</html>";
        JLabel audioTitle = new JLabel("<html><div style='text-align: center;'>" + audioInfo + "</div></html>", SwingConstants.CENTER);
        audioTitle.setBorder(new EmptyBorder(5,0,5,0));

        buttonsAndTitle.add(audioTitle, BorderLayout.NORTH);

        totalAudioTime += soundLength;
        updateRuntimeLabel();

        JPanel buttons = new JPanel();
        JButton moveUp = new JButton("Move Up"); //create button to move audio closer to beginning of Timeline

        moveUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentItemIndex = timelineAudio.indexOf(thisSound); //get index of selected sound
                if (currentItemIndex > 0) //if the sound can be moved up
                {
                    moveAudioTimingUp(currentItemIndex);
                    for (int i = 0; i < timelineAudio.size(); i++)
                    {
                        audioPanel.remove(timelineAudio.get(i)); //clear the list of soundPanel elements
                    }

                    timelineAudio.remove(currentItemIndex);
                    timelineAudio.add(currentItemIndex - 1, thisSound); //rearrange the selected sound for GUI display
                    soundList.remove(currentItemIndex);
                    soundList.add(currentItemIndex - 1, soundName); //also update position of sound in backend

                    for (int i = 0; i < timelineAudio.size(); i++)
                    {
                        timelineAudio.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i+1)));
                        audioPanel.add(timelineAudio.get(i)); //put sounds into the soundPanel in the new order
                    }

                    revalidate(); //update the GUI
                }
            }
        });

        JButton deleteButton = new JButton("Remove"); //create button to remove audio from Timeline

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int audioIndex = timelineAudio.indexOf(thisSound);
                audioPanel.remove(thisSound); //remove sound from GUI
                timelineAudio.remove(thisSound); //remove sound from backend GUI list
                totalAudioTime -= soundLength;
                removeAudioTiming(audioIndex);
                updateRuntimeLabel();

                if (timelineAudio.size() == 1)
                {
                    audioPanel.setLayout(new GridLayout(2, 1));
                }

                soundList.remove(soundName); //remove sound from backend list used for Slideshow

                for (int i = 0; i < timelineAudio.size(); i++)
                {
                    timelineAudio.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i+1)));
                    audioPanel.add(timelineAudio.get(i)); //put sounds into the soundPanel in the new order
                }
                repaint(); //update GUI with modified list of sounds
                revalidate();
            }
        });

        JButton moveDown = new JButton("Move Down"); //create button to move audio closer to end of Timeline

        moveDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentItemIndex = timelineAudio.indexOf(thisSound);
                if (currentItemIndex < timelineAudio.size()-1) //if the sound can be moved down
                {
                    moveAudioTimingDown(currentItemIndex);
                    for (int i = 0; i < timelineAudio.size(); i++)
                    {
                        audioPanel.remove(timelineAudio.get(i)); //clear the elements of the audioPanel GUI
                    }

                    timelineAudio.remove(currentItemIndex);
                    timelineAudio.add(currentItemIndex + 1, thisSound); //rearrange the selected sound for GUI display
                    soundList.remove(currentItemIndex);
                    soundList.add(currentItemIndex + 1, soundName); //also update position of sound in backend

                    for (int i = 0; i < timelineAudio.size(); i++)
                    {
                        timelineAudio.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i+1)));
                        audioPanel.add(timelineAudio.get(i)); //load the GUI with the modified sound order
                    }

                    revalidate(); //update the GUI
                }
            }
        });

        buttons.add(moveUp, BorderLayout.WEST);
        buttons.add(deleteButton, BorderLayout.CENTER);
        buttons.add(moveDown, BorderLayout.EAST);

        buttonsAndTitle.add(buttons, BorderLayout.SOUTH);
        thisSound.add(buttonsAndTitle, BorderLayout.SOUTH);

        JLabel thumbnail = new JLabel("", SwingConstants.CENTER);
        ImageIcon img = new ImageIcon("images\\audioicon.png");
        // TODO: Uncomment below for JAR
        // ImageIcon img = = new ImageIcon(getClass().getClassLoader().getResource("audioicon.png"));
        double proportion = 200.0/img.getIconWidth();
        double db_newHeight = proportion*img.getIconHeight();
        int newHeight = (int)Math.round(db_newHeight);
        Image imgIcon = img.getImage().getScaledInstance(200,newHeight,Image.SCALE_REPLICATE);
        img = new ImageIcon(imgIcon);
        thumbnail.setIcon(img);
        thisSound.add(thumbnail, BorderLayout.CENTER); //load and set icon for sound files

        Border timelineItemBorder = BorderFactory.createTitledBorder(String.valueOf(timelineAudio.size()+1));
        thisSound.setBorder(timelineItemBorder);

        thisSound.setPreferredSize(new Dimension(200, 400));
        timelineAudio.add(thisSound); //add new sound to backend list

        if (timelineAudio.size() == 2) //set layout to dynamically increase once Timeline has enough items
        {
            audioPanel.setLayout(new GridLayout(0, 1));
        }

        if (timelineAudio.size() > 1) //if necessary, prep scrollpane to adjust to bottom to make it clear new panel was added
        {
            JScrollBar verticalBar = audioScroll.getVerticalScrollBar();
            verticalBar.addAdjustmentListener(new ScrollAdjuster(verticalBar)); //set scrollbar to adjust to bottom to show new data
        }

        audioPanel.add(thisSound); //add new sound to GUI

        addAudioTiming(timelineAudio.size(), soundLength);

        revalidate();
    }

    /**
     * Adds an audio timing block to the timing area.
     *
     * @param audioNum The number of the audio block to be added.
     * @param audioLength The length of the audio to be added.
     */
    public void addAudioTiming(int audioNum, double audioLength)
    {
        JPanel audioTiming = new JPanel();
        JLabel lblAudioTiming = new JLabel(String.valueOf(audioNum), SwingConstants.CENTER);
        lblAudioTiming.setVerticalAlignment(SwingConstants.CENTER);
        int timingHeight = (int)(audioLength * 10);
        audioTiming.setMinimumSize(new Dimension(40, timingHeight));
        audioTiming.setPreferredSize(new Dimension(40, timingHeight));
        audioTiming.setMaximumSize(new Dimension(40, timingHeight));
        audioTiming.add(lblAudioTiming, SwingConstants.CENTER);
        audioTiming.setBorder(BorderFactory.createTitledBorder(""));

        audioTimings.add(audioTiming);
        lblAudioTimings.add(lblAudioTiming);

        audioTiming.setAlignmentY(TOP_ALIGNMENT);
        audioTimes.add(audioTiming);
    }

    /**
     * Removes an audio timing block from the timing area.
     *
     * @param audioNum The number of the audio block to be removed.
     */
    public void removeAudioTiming(int audioNum)
    {
        lblAudioTimings.remove(audioNum);
        audioTimings.remove(audioNum);
        audioTimes.remove(audioNum);

        for (int i = 0; i < lblAudioTimings.size(); i++)
            lblAudioTimings.get(i).setText(String.valueOf(i+1));
    }

    /**
     * Moves an audio block up the timing area by one step.
     *
     * @param audioNum The number of the audio block to be moved up.
     */
    public void moveAudioTimingUp(int audioNum)
    {
        for (int i = 0; i < audioTimings.size(); i++)
            audioTimes.remove(audioTimings.get(i));

        JLabel lblTemp = lblAudioTimings.get(audioNum);
        lblAudioTimings.remove(audioNum);
        lblAudioTimings.add(audioNum-1, lblTemp);

        JPanel pnlTemp = audioTimings.get(audioNum);
        audioTimings.remove(audioNum);
        audioTimings.add(audioNum-1, pnlTemp);


        for (int i = 0; i < audioTimings.size(); i++)
            audioTimes.add(audioTimings.get(i));

        for (int i = 0; i < lblAudioTimings.size(); i++)
            lblAudioTimings.get(i).setText(String.valueOf(i+1));
    }

    /**
     * Moves an audio block down the timing area by one step.
     *
     * @param audioNum The number of the audio block to be moved down.
     */
    public void moveAudioTimingDown(int audioNum)
    {
        for (int i = 0; i < audioTimings.size(); i++)
            audioTimes.remove(audioTimings.get(i));

        JLabel lblTemp = lblAudioTimings.get(audioNum);
        lblAudioTimings.remove(audioNum);
        lblAudioTimings.add(audioNum+1, lblTemp);

        JPanel pnlTemp = audioTimings.get(audioNum);
        audioTimings.remove(audioNum);
        audioTimings.add(audioNum+1, pnlTemp);

        for (int i = 0; i < audioTimings.size(); i++)
            audioTimes.add(audioTimings.get(i));

        for (int i = 0; i < lblAudioTimings.size(); i++)
            lblAudioTimings.get(i).setText(String.valueOf(i+1));
    }

    /**
     * This AdjustmentListener monitors the most recently updated JScrollPane. When adding a new item causes the
     * scrollbar to adjust, this listener sets the display position to the bottom of the scrollbar
     */
    static class ScrollAdjuster implements AdjustmentListener {

        JScrollBar parent; //JScrollBar that this listener is being attached to

        public ScrollAdjuster(JScrollBar parentScroll)
        {
            parent = parentScroll;
        }

        public void adjustmentValueChanged(final AdjustmentEvent e) //when the JScrollBar is adjusted...
        {
            Adjustable adjustable = e.getAdjustable();
            adjustable.setValue(adjustable.getMaximum()); //...set scroll position to bottom of bar...
            parent.removeAdjustmentListener(this); //...and remove this listener now that its task is complete
        }
    }

    /**
     * Adds/removes the timing panel to/from the Editor GUI.
     *
     * @param visible If true, add panel, else, remove panel
     */
    public void setTimingVisible(boolean visible)
    {
        if (visible)
            add(runtime, BorderLayout.EAST);
        else
            remove(runtime);
    }

    /**
     * Accepts length in seconds and converts to MINUTES:SECONDS format
     * @param tempLength- length of item in seconds
     * @return- returns String with length in MINUTES:SECONDS format
     */
    public String calculateMinSecLength(int tempLength)
    {
        String minLength = (int) Math.floor(tempLength / 60) + ":"; //calculate number of complete minutes this audio lasts
        int secLength = tempLength % 60; //determine how many extra seconds there are
        String itemLength;

        if (secLength < 10) //combine minLength and secLength with appropriate number of places
        {
            itemLength = minLength + "0" + secLength;
        }
        else {
            itemLength = minLength + secLength;
        }

        return itemLength;
    }

    /**
     * Displays the total Audio and (if applicable) Slideshow runtimes using the data put into the Timeline
     */
    public void updateRuntimeLabel()
    {
        String runtimeOutput = "";

        if (automated) //only calculate Slideshow runtime if the Slideshow is automated
        {
            double transitionRuntime = 0.0;

            for (JComboBox transition : transitionDurations) //calculate the length of all Transitions used in the Slideshow
            {
                if (transition.isEnabled()) {
                    transitionRuntime += (Double) transition.getSelectedItem();
                }
            }

            runtimeOutput += "Slideshow Runtime: ";
            runtimeOutput += calculateMinSecLength((int) (totalSlideTime + transitionRuntime)); //display the combined length of all Slides and Transitions
            runtimeOutput += "   |   ";
        }

        runtimeOutput += "Audio Runtime: "; //regardless of automation status, display the total Audio runtime
        runtimeOutput += calculateMinSecLength(totalAudioTime);

        runtimeLabel.setText(runtimeOutput);
        runtimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        runtimeLabel.setBorder(new EmptyBorder(7,0,7,0));
    }

    /**
     * Converts items in Timeline into a Slideshow for export to desired file
     * @param automated- indicates whether or not this Slideshow should be automated
     */
    public void exportSlideshow(boolean automated)
    {
        if (!slideList.isEmpty())
        {
            Slideshow slideshow = new Slideshow(); //create new Slideshow object to hold data
            slideshow.setAutomated(automated);

            if (automated) //export Slideshow with slide intervals if user has selected automated playback
            {
                int errorIndex = 0;

                try
                {
                    double slideDurationDb;
                    long slideDuration;

                    for (int i = 0; i < slideList.size(); i++)
                    {
                        errorIndex = i;

                        try {
                            slideDurationDb = Double.parseDouble(slideDurations.get(i).getText());
                        } catch (Exception ex) {
                            slideDurationDb = defaultSlideDuration;
                        }

                        slideDuration = (long)(slideDurationDb*1000);
                        slideList.get(i).setTime(slideDuration);
                    }
                    slideshow.setSlideList(slideList); //add Timeline's Slides to the Slideshow
                    slideshow.setSlideLength(calculateMinSecLength((int) totalSlideTime));
                    slideshow.setSoundList(soundList); //add Timeline's audio information to the Slideshow
                    slideshow.setSoundLength((calculateMinSecLength(totalAudioTime)));
                    SlideshowManager.writeDB(slideshow.toJSON()); //call SlideshowManager to write Slideshow's JSON data to file
                }
                catch (Exception e)
                {
                    errorIndex++;
                    String errorMsg = "Invalid value entered for the duration of slide " + errorIndex;
                    JOptionPane.showMessageDialog(SlideshowEditor.getInstance(), errorMsg);
                }
            }
            else //otherwise, export Slideshow
            {
                for (Slide slide : slideList) //ensures Slide time in layout file is set to zero if Slideshow is manual
                {
                    slide.setTime(0);
                }
                slideshow.setSlideList(slideList); //add Timeline's Slides to the Slideshow
                slideshow.setSoundList(soundList); //add Timeline's audio information to the Slideshow
                slideshow.setSoundLength((calculateMinSecLength(totalAudioTime)));
                SlideshowManager.writeDB(slideshow.toJSON()); //call SlideshowManager to write Slideshow's JSON data to file
            }
        }
        else
            JOptionPane.showMessageDialog(null, "You don't have any slides yet!");
    }

    /**
     * This function sets the visibility of the slide duration options depending on whether
     * the slideshow is set to automatic.
     *
     * @param visible The visibility settings of the slide duration options.
     */
    public void setSlideDurationVisible(boolean visible)
    {
        automated = visible;

        for (JPanel slideDuration : slideDurationPanels)
        {
            slideDuration.setVisible(visible); //set all duration panels to visible
        }

        totalSlideTime = 0.0;

        if (automated)
        {
            JLabel interval;

            for (int i = 0; i<slideDurations.size(); i++) //for every Slide...
            {
                interval = slideDurations.get(i); //get the duration label

                if (defaultDurationChecks.get(i).isSelected()) //if the duration label should be set to the new default, do so
                {
                    interval.setText(String.valueOf(defaultSlideDuration));
                }

                totalSlideTime += Double.parseDouble(interval.getText()); //add the value of the duration label to the total runtime
            }
        }

        updateAllSlideTimings(); //update all timing blocks
        updateRuntimeLabel(); //update the runtime label
    }

    /**
     * Sets default interval for Slides in an automated slideshow
     * @param duration- default interval in seconds
     */
    public void setDefaultSlideDuration(Double duration) { defaultSlideDuration = duration; }

    /**
     * Changes the pane displayed by timelinePanes. Used to make sure Timeline matches the active library
     * @param index- index of the pane to be displayed
     */
    public void setActivePane(int index) { timelinePanes.setSelectedIndex(index); }

    /**
     * Chanes the displayed library pane whenever the pane changes in Timeline.
     * @param libraries- reference to JTabbedPane containing Image and Audio libraries
     */
    public void enablePaneMatch(JTabbedPane libraries)
    {
        timelinePanes.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                libraries.setSelectedIndex(timelinePanes.getSelectedIndex()); //makes selected library matches Timeline tab
            }
        });
    }

    /**
     * This function resets all data within Timeline. It is called whenever the slideshow directory is changed
     */
    public void reset()
    {
        slidePanel.removeAll(); //clear the GUI component of the Slide timeline
        slidePanel.setLayout(new GridLayout(2, 1)); //reset the layout to preserve sizing
        timelineSlides.clear(); //remove all Slide timeline data stored in the backend
        slideDurationPanels.clear();
        slideDurations.clear();
        slideList.clear();

        audioPanel.removeAll(); //clear the GUI component of the Audio timeline
        audioPanel.setLayout(new GridLayout(2, 1)); //reset the layout to preserve sizing
        timelineAudio.clear(); //remove all Audio timeline data stored in the backend
        soundList.clear();

        totalSlideTime = 0.0; //reset the Slideshow and Audio runtimes
        totalAudioTime = 0;

        repaint(); //update the GUI to display a blank Timeline
        revalidate();
    }

    /**
     * This function returns the instance of Timeline. If no instance exists, then one is created.
     *
     * @return instance- pointer to instance of Timeline to be used
     */
    public static Timeline getInstance()
    {
        if (instance == null) {
            instance = new Timeline();
        }
        return instance;
    }

}
