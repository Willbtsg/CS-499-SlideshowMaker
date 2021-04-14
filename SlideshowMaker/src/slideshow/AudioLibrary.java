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
     * Clip currentClip - current clip being played by the user
     * JButton prevButton - previous button pressed to keep track of which button was pressed last
     * boolean isPlaying - keeps track of whether a clip is playing or not
     */
    private static AudioLibrary instance;
    private Timeline associatedTimeline;
    private Clip currentClip;
    private JButton prevButton;
    private boolean isPlaying;
    
    // Accesses the slideshow directory
    private File dir;

    // An array of the image types our app supports
    private String[] extensions = new String[]{ "wav", "aif", "aiff" };


    /////////////
    // METHODS //
    /////////////

    private AudioLibrary(Timeline timeline, String directory)
    {
        if (directory != null)
        {
            dir = new File(directory);

            // Filter to parse the directory for compatible image files
            FilenameFilter audioFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    for (String ext : extensions) {
                        if (name.endsWith("." + ext))
                            return true;
                    }
                    return false;
                }
            };

            isPlaying = false; //Set initial boolean for isPlaying to false

            associatedTimeline = timeline; //set reference to destination Timeline
            setLayout(new GridBagLayout()); //set dynamic size to keep items in panel till size determined
            GridBagConstraints  c = new GridBagConstraints();
            // Set initial parameters for grid bag constraints
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0.5;
            c.weighty = 0.5;

            int itemCounter = 0; //keeps track of how many items are in the library

            ImageIcon audioIcon = new ImageIcon("images\\audioicon.png");
            ImageIcon tempPlayIcon = new ImageIcon("images\\playbuttonicon.png");
            ImageIcon tempStopIcon = new ImageIcon("images\\stopbuttonicon.png");

            // TODO: Uncomment below for JAR
            // ImageIcon audioIcon = new ImageIcon(getClass().getClassLoader().getResource("audioicon.png"));
            // ImageIcon tempPlayIcon = new ImageIcon(getClass().getClassLoader().getResource("playbuttonicon.png"));
            // ImageIcon tempStopIcon = new ImageIcon(getClass().getClassLoader().getResource("stopbuttonicon.png"));
            
            // Transform temp stop and play icons and store them in new variables
            Image image = tempPlayIcon.getImage(); // transform it 
            Image newimg = image.getScaledInstance(13, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            ImageIcon playIcon = new ImageIcon(newimg); 
            
            image = tempStopIcon.getImage(); // transform it 
            newimg = image.getScaledInstance(13, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            ImageIcon stopIcon = new ImageIcon(newimg); 
            
            if (dir.isDirectory()) //if directory is valid
            {
                for (File file : dir.listFiles(audioFilter)) //for every wav, aif, or aiff file in the directory...
                {
                    JPanel libraryItem = new JPanel(); //create a new JPanel to display sound info
                    libraryItem.setLayout(new BorderLayout());
                    JPanel buttonAndTitle = new JPanel();
                    buttonAndTitle.setLayout(new BorderLayout());

                    int tempLength = getAudioLength((file));
                    String audioInfo = "<html>" + file.getName() + "<br>(" + calculateMinSecLength(tempLength) + ")</html>";
                    JLabel audioTitle = new JLabel("<html><div style='text-align: center;'>" + audioInfo + "</div></html>", SwingConstants.CENTER);
                    audioTitle.setBorder(new EmptyBorder(10,0,10,0));

                    buttonAndTitle.add(audioTitle, BorderLayout.NORTH);

                    JPanel buttons = new JPanel();
                    JButton addButton = new JButton("Add"); //add button for adding sound to Timeline
                    JButton playButton = new JButton(); //add button for playing sound
                    playButton.setIcon(playIcon);
                    

                    addButton.addActionListener(new ActionListener() //add sound to the Timeline
                    {
                        @Override
                        public void actionPerformed(ActionEvent e){
                            associatedTimeline.addSound(dir + "//" + file.getName(), tempLength); //pass in filepath and audio length
                        }
                    });

                    playButton.addActionListener(new ActionListener() //add audio preview to library item
                    {
                        @Override
                        public void actionPerformed(ActionEvent e){
                            // If no song is currently playing
                            if(!isPlaying) {
                                try {
                                    //Set isPlaying to true
                                    isPlaying = true;

                                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file); //read in sound file
                                    currentClip = AudioSystem.getClip(); //copy to Clip object
                                    currentClip.open(audioStream);

                                    //Start song
                                    currentClip.start();
                                    //Set button to stop button
                                    playButton.setIcon(stopIcon);

                                    prevButton = playButton;

                                    // Add line listener to keep track of clip status
                                    currentClip.addLineListener(new LineListener() {
                                        @Override
                                        public void update(LineEvent event) {
                                            LineEvent.Type type = event.getType(); //Get event type

                                            //If playback ends
                                            if (type == LineEvent.Type.STOP) {
                                                //Reset isPlaying
                                                isPlaying = false;
                                                //Stop Clip
                                                currentClip.stop();
                                                currentClip.close();
                                                //Set button back to play
                                                playButton.setIcon(playIcon);
                                            }
                                        }
                                    });

                                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            else if(prevButton != playButton) {
                                try {
                                    //Stop Clip
                                    currentClip.stop();
                                    currentClip.close();
                                    //Set button text back to play
                                    playButton.setIcon(playIcon);

                                    //Set isPlaying to true
                                    isPlaying = true;

                                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file); //read in sound file
                                    currentClip = AudioSystem.getClip(); //copy to Clip object
                                    currentClip.open(audioStream);

                                    //Start song
                                    currentClip.start();
                                    //Set button to stop button
                                    playButton.setIcon(stopIcon);

                                    prevButton = playButton;

                                    // Add line listener to keep track of clip status
                                    currentClip.addLineListener(new LineListener() {
                                        @Override
                                        public void update(LineEvent event) {
                                            LineEvent.Type type = event.getType(); //Get event type

                                            //If playback ends
                                            if (type == LineEvent.Type.STOP) {
                                                //Reset isPlaying
                                                isPlaying = false;
                                                //Stop Clip
                                                currentClip.stop();
                                                currentClip.close();
                                                //Set button back to play
                                                playButton.setIcon(playIcon);
                                            }
                                        }
                                    });

                                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            // Stop playing song
                            else {
                                //Reset isPlaying
                                isPlaying = false;
                                //Stop Clip
                                currentClip.stop();
                                currentClip.close();
                                //Set button back to play
                                playButton.setIcon(playIcon);
                            }
                        }
                    });

                    buttons.add(addButton, BorderLayout.WEST);
                    buttons.add(playButton, BorderLayout.EAST);
                    buttonAndTitle.add(buttons, BorderLayout.SOUTH);
                    libraryItem.add(buttonAndTitle, BorderLayout.SOUTH);

                    JLabel icon = new JLabel("", SwingConstants.CENTER);
                    icon.setIcon(audioIcon);
                    libraryItem.add(icon, BorderLayout.CENTER); //set audio icon

                    libraryItem.setPreferredSize(new Dimension(200,200));
                    add(libraryItem, c); //add JPanel of sound data to library

                    // Update grid x coordinate
                    c.gridx++;
                    // If 4 elements put in 1 row
                    if(++itemCounter%4 == 0) {
                        // Increment y and set x back to 0
                        c.gridy++;
                        c.gridx = 0;
                    }
                }
                // If panel isn't at minimum capacity
                if (itemCounter < 12) {
                	// Loop through till 12 panels have been added
                	while(itemCounter < 12) {
	                	// Create empty panel
	                	JPanel spaceFill = new JPanel();
	                	JLabel emptyLB = new JLabel("");
	                	spaceFill.add(emptyLB);
	                	add(spaceFill, c);
	                	
	                    // Update grid x coordinate
	                    c.gridx++;
	                    // If 4 elements put in 1 row
	                    if(++itemCounter%4 == 0) {
	                        // Increment y and set x back to 0
	                        c.gridy++;
	                        c.gridx = 0;
	                    }
                	}
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
    public String calculateMinSecLength(int tempLength)
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
     * @param directory- reference to directory to pull audio files from
     *
     * @return instance- pointer to instance of AudioLibrary to be used
     */
    public static AudioLibrary getInstance(Timeline timeline, String directory)
    {
        if (instance == null) {
            instance = new AudioLibrary(timeline, directory);
        }
        return instance;
    }

    /**
     * Resets library contents whenever a new directory is opened
     * @param timeline- reference to Timeline object to add sounds to
     * @param directory- reference to directory to pull audio files from
     *
     * @return new instance of AudioLibrary
     */
    public static AudioLibrary resetLibrary(Timeline timeline, String directory)
    {
        instance = null;
        AudioLibrary newLib = getInstance(timeline, directory);
        return newLib;
    }
}
