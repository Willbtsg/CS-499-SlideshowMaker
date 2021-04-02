package slideshow;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Class name: AudioLibrary
 *
 * This class allows the user to view and select the sound files they want in their Slideshow
 */

public class AudioLibrary extends JPanel {

    /**
     * AudioLibrary instance- instance of AudioLibrary for Singleton
     * Timeline associatedTimeline- reference to Timeline object sound data will be added to
     */
    private static AudioLibrary instance;
    private Timeline associatedTimeline;

    // Accesses the slideshow directory
    // TODO: This directory will need to be user-inputted
    private File dir = new File("images");

    // An array of the image types our app supports
    private String[] extensions = new String[]{ "wav", "aif", "aiff" };

    // Filter to parse the directory for compatible image files
    private FilenameFilter audioFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            for (String ext : extensions) {
                if (name.endsWith("." + ext))
                    return true;
            }
            return false;
        }
    };

    /////////////
    // METHODS //
    /////////////

    private AudioLibrary(Timeline timeline)
    {
        associatedTimeline = timeline; //set reference to destination Timeline
        setLayout(new GridLayout(3,4)); //set size to 3 row minimum to keep library items at preferred size

        int itemCounter = 0; //keeps track of how many items are in the library

        ImageIcon audioIcon = new ImageIcon("images\\audioicon.png");
        if (dir.isDirectory()) //if directory is valid
        {
            for (File file : dir.listFiles(audioFilter)) //for every wav, aif, or aiff file in the directory...
            {
                JPanel libraryItem = new JPanel(); //create a new JPanel to display sound info
                libraryItem.setLayout(new BorderLayout());
                JPanel buttonAndTitle = new JPanel();
                buttonAndTitle.setLayout(new BorderLayout());

                JLabel audioTitle = new JLabel(file.getName(), SwingConstants.CENTER);
                audioTitle.setBorder(new EmptyBorder(10,0,10,0));

                int tempLength = getAudioLength(file); //gets length of audio clip in seconds
                String audioLength = calculateAudioLength(tempLength); //converts second length into MINUTES:SECONDS

                audioTitle.setText(audioTitle.getText() + " (" + audioLength + ")"); //add audio length to title label

                buttonAndTitle.add(audioTitle, BorderLayout.NORTH);
                JButton addButton = new JButton("Add"); //add button for adding sound to Timeline

                addButton.addActionListener(new ActionListener() //add sound to the Timeline
                {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        associatedTimeline.addSound(dir + "//" + file.getName(), tempLength); //pass in filepath and audio length
                    }
                });

                buttonAndTitle.add(addButton, BorderLayout.SOUTH);
                libraryItem.add(buttonAndTitle, BorderLayout.SOUTH);

                JLabel icon = new JLabel("", SwingConstants.CENTER);
                icon.setIcon(audioIcon);
                libraryItem.add(icon, BorderLayout.CENTER); //set audio icon

                libraryItem.setPreferredSize(new Dimension(200,200));
                add(libraryItem); //add JPanel of sound data to library

                if (itemCounter++ == 12) //when number of items maxes out grid...
                {
                    setLayout(new GridLayout(0, 4)); //set grid to increase dynamically
                }
            }
        }
    }

    /**
     * Used to open an audio clip in order to determine its length in seconds
     * @param soundFile- audio file to determine length of
     * @return- length in seconds
     */
    public int getAudioLength(File soundFile)
    {

        int audioLength = 0; //default length of 0

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile); //read in sound file
            Clip tempClip = AudioSystem.getClip(); //copy to Clip object to read length
            tempClip.open(audioStream);
            audioLength = (int) tempClip.getMicrosecondLength(); //get length in microseconds
            audioLength /= Math.pow(10, 6); //convert to seconds
            tempClip.close(); //close audio data

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        return audioLength; //return length in seconds
    }

    /**
     * Converts length in seconds to MINUTES:SECONDS format
     * @param tempLength- length of audio clip in seconds
     * @return- String in MINUTES:SECONDS format
     */
    public String calculateAudioLength(int tempLength)
    {
        String minLength = (int) Math.floor(tempLength / 60) + ":"; //calculate number of complete minutes this audio lasts
        int secLength = tempLength % 60; //determine how many extra seconds there are
        String audioLength;

        if (secLength < 9) //combine minLength and secLength with appropriate number of places
        {
            audioLength = minLength + "0" + secLength;
        }
        else {
            audioLength = minLength + secLength;
        }

        return audioLength;
    }

    /**
     * This function returns the instance of AudioLibrary. If no instance exists, then one is created.
     *
     * @param timeline- reference to Timeline object to add sounds to
     * @return instance- pointer to instance of AudioLibrary to be used
     */
    public static AudioLibrary getInstance(Timeline timeline)
    {
        if (instance == null) {
            instance = new AudioLibrary(timeline);
        }
        return instance;
    }

}
