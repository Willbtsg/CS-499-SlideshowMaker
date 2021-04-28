package slideshow;

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
     * long m_slideshowLength- contains String displaying the sum of Slide and Transition timing (i.e. runtime for automated Slideshow)
     * long m_AudioLength- contains String displaying the sum of sound file runtimes
     * String m_Progenitor- tag used to indicate if the Slideshow layout file was created by SlideshowEditor
     */
    private Boolean m_automated;
    private ArrayList<Slide> m_SlideList;
    private ArrayList<String> m_SoundList;
    private String m_slideshowLength;
    private String m_AudioLength;
    private String m_Progenitor;

    /**
     * Constructor for Slideshow. No functionality other than initializing variables
     */
    public Slideshow()
    {
        m_automated = false;
        m_SlideList = new ArrayList();
        m_SoundList = new ArrayList();
        m_Progenitor = new String();
    }

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

        for (String s : m_SoundList)
        {
            JSONObject soundtrack = new JSONObject();
            soundtrack.put("name", s);
            soundList.add(soundtrack);
        }

        obj.put("SoundList", soundList);
        obj.put("AudioLength", m_AudioLength);

        obj.put("Progenitor", "SlideshowEditor"); //used to indicate that the json file was created by Slideshow Editor

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
     * Sets Slideshow length in MIN:SEC format
     * @param slideLength- String showing Slideshow runtime in MIN:SEC format
     */
    public void setSlideLength(String slideLength) { m_slideshowLength = slideLength; }

    /**
     * Set total audio runtime in MIN:SEC format
     * @param soundLength- String showing audio runtime in MIN:SEC format
     */
    public void setSoundLength(String soundLength) { m_AudioLength = soundLength; }

    /**
     * Retrieves Slide object to be displayed
     * @param index- indicates which Slide should be retrieved
     * @return
     */
    public Slide getSlide(int index) { return m_SlideList.get(index); }

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
     * Sets String to indicate if the Slideshow information comes from a file created by SlideshowEditor
     * @param progenitor- String containing information about what program created the Slideshow layout file
     */
    public void setProgenitor(String progenitor)
    {
        m_Progenitor = progenitor;
    }

    /**
     * Retrieves String that indicates whether the layout file was created by Slideshow editor
     * @return
     */
    public String getProgenitor()
    {
        return m_Progenitor;
    }

}
