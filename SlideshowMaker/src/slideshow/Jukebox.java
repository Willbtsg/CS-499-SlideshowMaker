package slideshow;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class name: Jukebox
 *
 * This class is used to play the songs and sounds that have been chosen to accompany the slideshow
 * This class is implemented as a Singleton.
 */

public class Jukebox {

    /**
     * static Jukebox instance- static reference to Jukebox for Singleton
     * ArrayList<String> m_SoundList- contains list of filepaths to use for audio playback
     * Clip currentClip- contains the loaded prepared sound file being played
     * AudioListener listener- LineListener used to signal when the current sound has finished
     * AudioInputStream audioStream- used to read in sound file and prepare it for playback
     * Boolean paused- indicates whether or not the Jukebox is currently paused
     * long soundPosition- saves microsecond position of paused sound to ensure playback resumes at proper spot
     */
    private static Jukebox instance;
    private ArrayList<String> m_soundList;
    private Clip currentClip;
    private AudioListener listener;
    private AudioInputStream audioStream;
    private Boolean paused;
    private long soundPosition;
    private Boolean lastSong;
    private Boolean enMediaRes;
    private int songsPlayed;
    private boolean playing;

    /**
     * Constructor for Jukebox object. Initializes m_soundList
     */
    private Jukebox()
    {
        m_soundList = new ArrayList<>();
        enMediaRes = false;
    }

    /**
     * Used to add a new sound to m_SoundList
     * @param clipName- filepath for new sound
     */
    public void addSong(String clipName) { m_soundList.add(clipName); }

    /**
     * Used to retrieve m_soundList for display or for writing to layout file
     * @return m_soundList- list of filepaths for sounds the Jukebox can play
     */
    public int getSoundListSize() { return m_soundList.size(); }

    /**
     * Sets entire m_soundList at once
     * @param soundList- new list of filepaths to be stored within Jukebox
     */
    public void setSoundList(ArrayList<String> soundList)
    {
        m_soundList = new ArrayList<String>(); //clear current Jukebox library

        for (String s : soundList)
        {
            addSong(s); //add new filepaths to m_soundList
        }
    }

    /**
     * Prepares new sound file for playback
     * @param clipName- name of file that will be played
     */
    public void prepClip(String clipName)
    {

        try {
            File soundFile = new File(clipName); //open sound file
            audioStream = AudioSystem.getAudioInputStream(soundFile); //read in sound file
            currentClip = AudioSystem.getClip(); //copy to Jukebox's Clip object for playback
            listener = new AudioListener();
            currentClip.addLineListener(listener); //attach new AudioListener to Clip


        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        return;

    }

    /**
     * This LineListener monitors audio playback. When playback is complete, it signals the Jukebox to close the
     * current file and (if applicable) play the next one in the list
     *
     * Source: https://gist.github.com/aneveux/2642923
     */
    class AudioListener implements LineListener {

        /**
         * This method allows to be notified for each event while playing a
         * sound
         */
        @Override
        public synchronized void update(final LineEvent event) {
            final LineEvent.Type eventType = event.getType();
            if (eventType == LineEvent.Type.STOP && !paused) { //checks paused to prevent ending songs prematurely
                notifyAll();
            }
        }

        /**
         * This method allows to wait until a sound is completely played
         *
         * @throws InterruptedException
         *             as we work with thread, this exception can occur
         */
        public synchronized void waitUntilDone() throws InterruptedException {
            paused = false;
            wait();
        }
    }

    /**
     * Handles playback of current sound file
     * @param currentSound- name of desired sound file
     * @return null- returns null when playback is complete to reset currentClip object
     */
    public Clip playSound(String currentSound)
    {
        prepClip(currentSound); //open sound file and prepare for playback

        try {

            currentClip.open(audioStream); //open the prepared clip

            try {
                currentClip.start(); //begin audio playback
                listener.waitUntilDone(); //set AudioListener to monitor when playback is complete
            } finally {
                currentClip.close(); //when playback is complete, close the clip
            }

        } catch (IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Used by SlideshowPlayer to play all sounds.
     * Playback ceases when all sounds have been played (or when the application is closed).
     * By playing music in separate Threads, this allows Slide playback to continue uninterrupted
     */
    public void playAll()
    {
        final int[] songIndex = {-1}; //set starting index to iterate through songs
        final int[] tempIndex = new int[1]; //temporary index to check for IndexOutOfBounds exception
        songsPlayed = 0;
        lastSong = false; //flag to indicate when there are no more sound files to play
        enMediaRes = true; //flag to indicate whether or not the Jukebox is trying to play its soundtrack

        new Thread(new Runnable() {

            @Override
            public void run() {
                while(!lastSong) //as long as there is more music to played after the current sound...
                {
                    if (!playing) { //...if no sound is actively playing...
                        tempIndex[0] = songIndex[0] + 1; //...increment tempIndex...

                        if (tempIndex[0] >= m_soundList.size()) { //...if tempIndex is invalid...
                            lastSong = true; //...indicate that playback is complete
                        } else {
                            currentClip = playSound(m_soundList.get(++songIndex[0])); //Otherwise, play the next sound
                        }
                    }
                }
            }
        }).start();

        enMediaRes = false;

    }

    /**
     * Pauses Jukebox playback. Only used during automated Slideshow
     */
    public void pausePlayback()
    {

        paused = true;

        if (currentClip != null && currentClip.isRunning()) //if there is a sound playing...
        {
            soundPosition = currentClip.getMicrosecondPosition(); //...save its current timestamp...
            currentClip.stop(); //...and pause it
        }

    }

    /**
     * Resumes Jukebox Playback. Only used during automated Slideshow
     */
    public void resumePlayback()
    {
        paused = false;

        if (currentClip != null) { //if a sound was playing before the Jukebox was paused...
            currentClip.setMicrosecondPosition(soundPosition); //...set the sound to its previous timestamp...
            currentClip.start(); //...and resume playback
        }
    }

    /**
     * This function is used to short-circuit the playAll function if the user decides to end playback early
     */
    public void stopPlayback()
    {
        if (enMediaRes)
        {
            lastSong = true;
            currentClip.stop();
        }
    }

    /**
     * This function returns the instance of Jukebox. If no instance exists, then one is created.
     *
     * @return instance- pointer to instance of Jukebox to be used
     */
    public static Jukebox getInstance()
    {
        if (instance == null)
        {
            instance = new Jukebox();
        }

        return instance;
    }

}
