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
     * JTabbedPane timelinePanes-
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
     * JPanel runtimeDisplay-
     * int totalSlideTime- total time (in seconds) that the automated Slideshow will last
     * double defaultDuration- default slide interval for Timeline items in an automated Slideshow
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
    private ArrayList<JComboBox> transitionDurations;
    private JLabel runtimeLabel;
    private double totalSlideTime = 0.0;
    private int totalAudioTime = 0;
    private Double defaultSlideDuration = 3.0;
    private boolean automated = false;

    /////////////
    // METHODS //
    /////////////

    private Timeline()
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Timeline"));
        setSize(320, 750);

        timelinePanes = new JTabbedPane();
        timelineSlides = new ArrayList<JPanel>(); //create list of objects to display Slide data
        timelineAudio = new ArrayList<JPanel>(); //create list of object to display sound data
        slidePanel = new JPanel(); //create JPanel for Slide tab
        audioPanel = new JPanel(); //create JPanel for Audio tab

        slideList = new ArrayList();
        soundList = new ArrayList();

        slideDurationPanels = new ArrayList<JPanel>();
        slideDurations = new ArrayList<JLabel>();
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

        add(timelinePanes, BorderLayout.NORTH);

        runtimeLabel = new JLabel();
        updateRuntimeLabel();

        add(runtimeLabel, BorderLayout.SOUTH);
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

        String transitionOptions[] = {"None", "Wipe Right", "Wipe Left", "Wipe Up", "Wipe Down", "Crossfade"}; //array of Transitions to choose from
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
                }

                thisSlide.setTransitions(transition); //set Transition information in Slide object

                if (transSelect.getSelectedItem() != "None") //only have timing dropdown enabled if a transition is in use
                    transLength.setEnabled(true);
                else
                    transLength.setEnabled(false);

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

        JButton deleteButton = new JButton("Delete"); //create button to remove slide from Timeline

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        
        removeFocusFromAllObjects(thisSlideDisplay);
        slidePanel.add(thisSlideDisplay);
        revalidate(); //update GUI after adding new component
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

        JButton deleteButton = new JButton("Delete"); //create button to remove audio from Timeline

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                audioPanel.remove(thisSound); //remove sound from GUI
                timelineAudio.remove(thisSound); //remove sound from backend GUI list
                totalAudioTime -= soundLength;
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

        removeFocusFromAllObjects(thisSound); // Get rid of 
        audioPanel.add(thisSound); //add new sound to GUI
        revalidate();
    }
    
    /**
     * Removes focusable dotted line from all components
     * @param container - object with components to set focus for 
     */
    public void removeFocusFromAllObjects(Container container) {
        container.setFocusable(false);
        for (Component child : container.getComponents()) {
            if (child instanceof Container) {
                removeFocusFromAllObjects((Container) child);
            } else {
                child.setFocusable(false);
            }
        }
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

    public void updateRuntimeLabel()
    {
        String runtimeOutput = "";

        if (automated)
        {
            double transitionRuntime = 0.0;

            for (JComboBox transition : transitionDurations) {
                if (transition.isEnabled()) {
                    transitionRuntime += (Double) transition.getSelectedItem();
                }
            }

            runtimeOutput += "Slideshow Runtime: ";
            runtimeOutput += calculateMinSecLength((int) (totalSlideTime + transitionRuntime));
            runtimeOutput += "   |   ";
        }

        runtimeOutput += "Audio Runtime: ";
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
                    slideshow.calculateLength();
                    slideshow.setSoundList(soundList); //add Timeline's audio information to the Slideshow
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
            slideDuration.setVisible(visible);
        }

        totalSlideTime = 0.0;

        if (automated)
        {
            for (JLabel interval : slideDurations) {
                try {
                    totalSlideTime += Double.parseDouble(interval.getText());
                } catch (Exception ex) {
                    totalSlideTime += defaultSlideDuration;
                }
            }
        }

        updateRuntimeLabel();
    }

    /**
     * Sets default interval for Slides in an automated slideshow
     * @param duration- default interval in seconds
     */
    public void setDefaultSlideDuration(Double duration) { defaultSlideDuration = duration; }

    public void setActivePane(int index)
    {
        timelinePanes.setSelectedIndex(index);
    }

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
        slidePanel.removeAll();
        slidePanel.setLayout(new GridLayout(2, 1));
        timelineSlides.clear();
        slideDurationPanels.clear();
        slideDurations.clear();
        slideList.clear();

        audioPanel.removeAll();
        audioPanel.setLayout(new GridLayout(2, 1));
        timelineAudio.clear();
        soundList.clear();

        totalSlideTime = 0.0;
        totalAudioTime = 0;

        repaint();
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
