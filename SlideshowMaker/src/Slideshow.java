import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;

/**
 * Class name: Slideshow
 *
 * This class contains all Slides and the list of sounds to be played by the Jukebox. It also contains other information
 * relevant to playback configuration (e.g. m_automated)
 */
public class Slideshow {

    /**
     * Boolean m_automated- indicates whether or not this Slideshow is to be viewed with automated or manual playback
     * ArrayList<Slide> m_SlideList- collection of Slides to be displayed
     * ArrayList<String> m_SoundList- contains names of audio files to be played by Jukebox
     * long m_SlideshowLength- contains sum of Slide and Transition timing (i.e. runtime for automated Slideshow)
     * long m_AudioLength- contains sum of sound file runtimes
     */
    private Boolean m_automated;
    private ArrayList<Slide> m_SlideList;
    private ArrayList<String> m_SoundList;
    private long m_slideshowLength;
    private long m_AudioLength;

    /**
     * Converts Slideshow to JSONObject. Used when writing Slideshow information to layout file
     * @return obj- JSONObject for DBWizard/SlideshowManager to use when writing JSON file
     */
    public JSONObject toJSON()
    {

        JSONObject obj = new JSONObject();
        JSONArray slideList = new JSONArray();
        JSONArray soundList = new JSONArray();

        for (Slide s : m_SlideList)
        {
            slideList.add(s.toJSON());
        }

        obj.put("SlideList", slideList);
        obj.put("Automated", m_automated);

        if (m_automated)
        {
            obj.put("SlideshowLength", m_slideshowLength);

        }

        soundList.addAll(m_SoundList);

        obj.put("SoundList", soundList);
        obj.put("AudioLength", m_AudioLength);

        return obj;

    }

    /**
     * Sets list of Slides that make up Slideshow
     * @param slideList- list of Slide Objects
     */
    public void setSlideList(ArrayList slideList) { m_SlideList = slideList; }

    /**
     * returns list of Slides (not currently used, may be removed)
     * @return
     */
    public ArrayList<Slide> getSlideList() { return m_SlideList; }

    /**
     * Sets flag to indicate whether or not the SlideshowPlayer will allow for fully manual playback control
     * @param automated- Boolean to indicate playback mode
     */
    public void setAutomated(Boolean automated) { m_automated = automated; }

    /**
     * Returns whether or not automated playback is used in this Slideshow
     * @return
     */
    public Boolean getAutomated() { return m_automated; }

    /**
     * Returns combined length of Slides and their forward Transitions
     * @return
     */
    public long getLength() { return m_slideshowLength; }

    /**
     * Calculates sum of Slides' display times and Transition lengths and saves as m_SlideshowLength (i.e. total runtime)
     */
    public void calculateLength()
    {
        int tempLength = 0;

        for (Slide s : m_SlideList)
        {
            tempLength += s.getTime();

            if (s.hasTransitions())
            {
                tempLength += s.getTransTime();
            }
        }

        m_slideshowLength = tempLength;
    }

    /**
     * Retrieves Slide object to be displayed
     * @param index- indicates which Slide should be retrieved
     * @return
     */
    public Slide getSlide(int index) { return m_SlideList.get(index); }

    /**
     * Adds a new Slide to the end of m_SlideList
     * @param newSlide- new Slide to be added to end of m_SlideList
     */
    public void addSlide(Slide newSlide) { m_SlideList.add(newSlide); }

    /**
     * Adds a new Slide to a specific position in m_SlideList
     * @param newSlide- new Slide to be added
     * @param index- position for new Slide to be added at
     */
    public void addSlide(Slide newSlide, int index) { m_SlideList.add(index, newSlide); }

    /**
     * Retrieves list of sounds used with this Slideshow (used to fill Jukebox)
     * @return
     */
    public ArrayList getSoundList() { return m_SoundList; }

    /**
     * Sets ArrayList that contains names of all sound files associated with this Slideshow
     * @param soundList- collection of Strings (i.e. filenames for sounds)
     */
    public void setSoundList(ArrayList soundList) { m_SoundList = soundList; }

    /**
     * Adds a new filename to m_SoundList
     * @param sound- filename of new sound for this Slideshow
     */
    public void addSound(String sound) { m_SoundList.add(sound); }
}
