import org.json.simple.JSONObject;

/**
 * Class name: Slide
 *
 * This class contains information necessary for displaying slideshow images, including timing and transitions.
 */

public class Slide {

    /**
     * String m_name- contains filepath for desired image
     * Transition m_forward- reference to specified Transition used when moving to next slide
     * Transition m_backward- reference to Transition to be used when moving to previous slide
     */
    private String m_name;
    //private Transition m_forward;
    //private Transition m_backward;

    /**
     * Constructor for Slide object. Sets filename before assigning Transitions
     * @param name- accepts image name (i.e. filepath) as argument
     */
    public Slide (String name)
    {
        m_name = name;

        return;
    }

    public Slide(JSONObject j)
    {
        m_name = (String) j.get("name");
        //m_forward = (Transition)j.get("forward")

    }

    //https://stackoverflow.com/a/53226346/5763413
    @SuppressWarnings("unchecked")
    /**
     * Creates a JSONObject of the movie
     * @return a JSONObject
     *
     */
    public  JSONObject toJSON()
    {
        JSONObject obj = new JSONObject();
        try
        {
            obj.put("name",m_name) ;

        }

        catch (Exception e) {e.printStackTrace();}

        return obj;
    }

    /**
     * Returns the name of the Slide's image
     * @return m_name- String of image name
     */
    public String getName()
    {
        return m_name;
    }

}
