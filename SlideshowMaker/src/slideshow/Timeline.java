package slideshow;

import transitions.TransitionLibrary;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
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
 * This class allows users to manage the sound anf image files they have added to their Slideshow
 */

public class Timeline extends JTabbedPane {

    /**
     * Timeline instance- instance of Timeline for Singleton
     * ArrayList<JPanel> timelineSlides- contains slide data to be displayed in Timeline's "Slides" tab
     * ArrayList<JPanel> timelineAudio- contains sound data to be displayed in Timeline's "Audio" tab
     * JPanel slidePanel- JPanel used to display timelineSlides
     * JPanel audioPanel- JPanel used to display timelineAudio
     * TransitionLibrary m_TransitionLibrary- allows user to set a Transition and validates the selection
     */
    private static Timeline instance;
    private ArrayList<JPanel> timelineSlides;
    private ArrayList<JPanel> timelineAudio;
    private JPanel slidePanel;
    private JPanel audioPanel;
    private JScrollPane slideScroll;
    private JScrollPane audioScroll;
    private TransitionLibrary m_TransitionLibrary;

    private ArrayList<Slide> slides;
    private ArrayList<String> sounds;

    /////////////
    // METHODS //
    /////////////

    private Timeline()
    {
        timelineSlides = new ArrayList<JPanel>(); //create list of objects to display Slide data
        timelineAudio = new ArrayList<JPanel>(); //create list of object to display sound data
        slidePanel = new JPanel(); //create JPanel for Slide tab
        audioPanel = new JPanel(); //create JPanel for Audio tab
        m_TransitionLibrary = TransitionLibrary.getInstance();

        slides = new ArrayList<>();
        sounds = new ArrayList<>();

        GridLayout grid = new GridLayout(0,1); //layout to be used for Slide and audio tabs
        slidePanel.setLayout(grid); //set tab layouts
        audioPanel.setLayout(grid);

        slideScroll = new JScrollPane(slidePanel); //create scroll panel for slidePanel
        slideScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        slideScroll.getVerticalScrollBar().setUnitIncrement(20);
        slideScroll.setPreferredSize(new Dimension(320, 835));
        add("Slides", slideScroll);

        audioScroll = new JScrollPane(audioPanel); //create scroll panel for audioPanel
        audioScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        audioScroll.getVerticalScrollBar().setUnitIncrement(20);
        audioScroll.setPreferredSize(new Dimension(320, 790));
        add("Audio", audioScroll);

        setBorder(BorderFactory.createTitledBorder("Timeline"));
    }

    /**
     * Used by ImageLibrary to add new image to the Slideshow. Creates JPanel with Slide information to display in slidePanel
     * @param filePath- filepath to image being added to Timeline
     */
    public void addSlide(String filePath)
    {
        Slide slide = new Slide(filePath);
        slides.add(slide);

        File image = new File(filePath); //retrieve image

        JPanel thisSlide = new JPanel(); //create new panel to display slide information
        thisSlide.setLayout(new BorderLayout());

        JPanel buttonsAndTitle = new JPanel();
        buttonsAndTitle.setLayout(new BorderLayout());

        JLabel imgTitle = new JLabel(image.getName(), SwingConstants.CENTER);
        imgTitle.setBorder(new EmptyBorder(5,0,5,0));
        buttonsAndTitle.add(imgTitle, BorderLayout.NORTH);

        JPanel buttons = new JPanel();
        JButton moveUp = new JButton("Move Up"); //create button to move slide towards beginning of Timeline

        moveUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentItemIndex = timelineSlides.indexOf(thisSlide); //get index of selected Slide
                if (currentItemIndex > 0) //if the Slide can be moved up
                {
                    for (int i = 0; i < timelineSlides.size(); i++)
                    {
                        slidePanel.remove(timelineSlides.get(i)); //clear the display
                    }

                    timelineSlides.remove(currentItemIndex);
                    timelineSlides.add(currentItemIndex - 1, thisSlide); //adjust the slide's position in the backend
                    slides.remove(currentItemIndex);
                    slides.add(currentItemIndex -1 , slide);

                    for (int i = 0; i < timelineSlides.size(); i++)
                    {
                        timelineSlides.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i+1)));
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
                slidePanel.remove(thisSlide); //remove slide from display
                timelineSlides.remove(thisSlide); //remove slide from Timeline database
                slides.remove(slide);
                for (int i = 0; i < timelineSlides.size(); i++)
                {
                    timelineSlides.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i+1)));
                    slidePanel.add(timelineSlides.get(i)); //add all slides back int he new order
                }
                repaint(); //update display to reflect changes
                revalidate();
            }
        });

        JButton moveDown = new JButton("Move Down"); //create button to move slide towards end of Timeline

        moveDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentItemIndex = timelineSlides.indexOf(thisSlide);

                if (currentItemIndex < timelineSlides.size()-1)
                {
                    for (int i = 0; i < timelineSlides.size(); i++)
                    {
                        slidePanel.remove(timelineSlides.get(i)); //clear GUI
                    }

                    timelineSlides.remove(currentItemIndex);
                    timelineSlides.add(currentItemIndex + 1, thisSlide); //reposition slide in backend
                    slides.remove(currentItemIndex);
                    slides.add(currentItemIndex + 1, slide);

                    for (int i = 0; i < timelineSlides.size(); i++)
                    {
                        timelineSlides.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i+1)));
                        slidePanel.add(timelineSlides.get(i)); //add all slides back to GUI in new order
                    }

                    revalidate(); //update GUI
                }
            }
        });

        buttons.add(moveUp, BorderLayout.WEST);
        buttons.add(deleteButton, BorderLayout.CENTER);
        buttons.add(moveDown, BorderLayout.EAST);

        buttonsAndTitle.add(buttons, BorderLayout.SOUTH);
        thisSlide.add(buttonsAndTitle, BorderLayout.SOUTH); //add control buttons and image name to Panel

        JLabel thumbnail = new JLabel("", SwingConstants.CENTER);
        ImageIcon img = new ImageIcon(filePath);
        double proportion = 200.0/img.getIconWidth();
        double db_newHeight = proportion*img.getIconHeight();
        int newHeight = (int)Math.round(db_newHeight);
        Image imgIcon = img.getImage().getScaledInstance(200,newHeight,Image.SCALE_REPLICATE);
        img = new ImageIcon(imgIcon);
        thumbnail.setIcon(img);
        thisSlide.add(thumbnail, BorderLayout.CENTER); //create image icon then add to display

        JPanel transitionDropdowns = new JPanel();
        transitionDropdowns.setLayout(new BorderLayout());

        JPanel transitionDropdownLabels = new JPanel();
        transitionDropdownLabels.setLayout(new BorderLayout());
        JLabel transSelectLabel = new JLabel("Transition Type:");
        transSelectLabel.setBorder(new EmptyBorder(0,0,5,0));
        transitionDropdownLabels.add(transSelectLabel, BorderLayout.WEST);
        JLabel transLengthLabel = new JLabel("Transition Length (sec):", SwingConstants.CENTER);
        transLengthLabel.setBorder(new EmptyBorder(0,0,5,0));
        transitionDropdownLabels.add(transLengthLabel, BorderLayout.EAST);
        transitionDropdowns.add(transitionDropdownLabels, BorderLayout.NORTH);

        JPanel transitionComboBoxes = new JPanel();
        transitionComboBoxes.setLayout(new BorderLayout());
        JComboBox<String> transSelect = new JComboBox<>();
        transSelect.setPreferredSize(new Dimension(145,20));
        transSelect.addItem("None");
        transSelect.addItem("Wipe Right");
        transSelect.addItem("Wipe Left");
        transSelect.addItem("Wipe Up");
        transSelect.addItem("Wipe Down");
        transSelect.addItem("Crossfade");
        transitionComboBoxes.add(transSelect, BorderLayout.WEST);
        JComboBox<Double> transLength = new JComboBox<>();
        transLength.setPreferredSize(new Dimension(145,20));
        transLength.addItem(0.5);
        transLength.addItem(1.0);
        transLength.addItem(1.5);
        transLength.addItem(2.0);
        transLength.addItem(2.5);
        transLength.addItem(3.0);
        transLength.addItem(3.5);
        transLength.addItem(4.0);
        transLength.addItem(4.5);
        transLength.addItem(5.0);
        transLength.setEnabled(false);
        transSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (transSelect.getSelectedItem() != "None")
                    transLength.setEnabled(true);
                else
                    transLength.setEnabled(false);
            }
        });
        transitionComboBoxes.add(transLength, BorderLayout.EAST);
        transitionDropdowns.add(transitionComboBoxes, BorderLayout.SOUTH);

        thisSlide.add(transitionDropdowns, BorderLayout.NORTH);

        Border timelineItemBorder = BorderFactory.createTitledBorder(String.valueOf(timelineSlides.size()+1));
        thisSlide.setBorder(timelineItemBorder);

        thisSlide.setPreferredSize(new Dimension(200, 400));
        timelineSlides.add(thisSlide);
        slidePanel.add(thisSlide);

        JScrollBar verticalBar = slideScroll.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
    }

    /**
     * Used by AudioLibrary when adding new sound to the Timeline
     * @param soundName- name of sound being added to Timeline
     */
    public void addSound(String soundName)
    {
        String sound = new String(soundName);
        sounds.add(sound);


        JPanel thisSound = new JPanel(); //new JPanel to display sound data in Timeline
        thisSound.setLayout(new BorderLayout());

        JPanel buttonsAndTitle = new JPanel();
        buttonsAndTitle.setLayout(new BorderLayout());

        File audioFile = new File(soundName);
        JLabel audioTitle = new JLabel(audioFile.getName(), SwingConstants.CENTER);
        audioTitle.setBorder(new EmptyBorder(5,0,5,0));
        buttonsAndTitle.add(audioTitle, BorderLayout.NORTH);

        JPanel buttons = new JPanel();
        JButton moveUp = new JButton("Move Up");

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
                    timelineAudio.add(currentItemIndex - 1, thisSound); //rearrange the selected sound
                    sounds.remove(currentItemIndex);
                    sounds.add(currentItemIndex - 1, sound);

                    for (int i = 0; i < timelineAudio.size(); i++)
                    {
                        timelineAudio.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i+1)));
                        audioPanel.add(timelineAudio.get(i)); //put sounds into the soundPanel in the new order
                    }

                    revalidate(); //update the GUI
                }
            }
        });

        JButton deleteButton = new JButton("Delete");

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                audioPanel.remove(thisSound); //remove sound from GUI
                timelineAudio.remove(thisSound); //remove sound from backend
                sounds.remove(sound);
                for (int i = 0; i < timelineAudio.size(); i++)
                {
                    timelineAudio.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i+1)));
                    audioPanel.add(timelineAudio.get(i)); //put sounds into the soundPanel in the new order
                }
                repaint(); //update GUI with modified list of sounds
                revalidate();
            }
        });

        JButton moveDown = new JButton("Move Down");

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
                    timelineAudio.add(currentItemIndex + 1, thisSound); //rearrange the sound in the backend
                    sounds.remove(currentItemIndex);
                    sounds.add(currentItemIndex + 1, sound);

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
        audioPanel.add(thisSound); //add new sound to GUI
        JScrollBar vertical = audioScroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

        JScrollBar verticalBar = audioScroll.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
    }

    public void createSlideshow(boolean automated, long slideInterval)
    {
        Slideshow slideshow = new Slideshow();
        slideshow.setAutomated(automated);
        for (Slide slide : slides)
            slide.setTime(slideInterval);
        slideshow.setSlideList(slides);
        slideshow.setSoundList(sounds);
        DBWizard.writeDB(slideshow.toJSON());
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
