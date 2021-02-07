
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

    /**
     * Returns the name of the Slide's image
     * @return m_name- String of image name
     */
    public String getName()
    {
        return m_name;
    }

}
