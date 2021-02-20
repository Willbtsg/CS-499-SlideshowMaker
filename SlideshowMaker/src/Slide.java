import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.Image;
import java.util.ArrayList;

/**
 * Class name: Slide
 *
 * This class contains information necessary for displaying slideshow images, including timing and transitions.
 */

public class Slide {

    /**
     * String m_name- contains filepath for desired image
     * private Image m_image- contains scaled image for this Slide
     * Transition m_forward- reference to specified Transition used when moving to next slide
     * Transition m_backwards- reference to Transition to be used when returning to this slide
     * Transition m_hasTransition- flag to indicate whether or not this Slide has transitions
     */
    private String m_name;
    private Image m_image;
    private Transition m_forward;
    private Transition m_backwards;
    private Boolean m_hasTransitions;

    /**
     * Constructor for Slide object. Sets filename before assigning Transitions
     * @param name- accepts image name (i.e. filepath) as argument
     */
    public Slide (String name)
    {
        m_name = name;

        Image tempImage;

        ImageIcon currentImage = new ImageIcon(name);
        tempImage = currentImage.getImage();
        m_image = tempImage.getScaledInstance(500, 313, Image.SCALE_SMOOTH);

        m_hasTransitions = false;

        return;
    }

    public Slide(JSONObject j)
    {
        m_name = (String) j.get("name");

        Image tempImage;

        ImageIcon currentImage = new ImageIcon(getName());
        tempImage = currentImage.getImage();
        m_image = tempImage.getScaledInstance(500, 313, Image.SCALE_SMOOTH);
        m_forward = new LRWipe();
        m_backwards = new RLWipe();
        m_hasTransitions = true;

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
            obj.put("hasTransitions",m_hasTransitions);

            if (m_hasTransitions)
            {
                obj.put("forward",m_forward.getType());
            }

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

    public Image getImage() { return m_image; }

    public String getForward() { return m_forward.getType(); }

    public Boolean hasTransitions() { return m_hasTransitions; }

    public void setForward(Transition forward)
    {
        m_forward = forward;
        m_hasTransitions = true;

        return;
    }

    public void setBackwards(Transition backwards) { m_backwards = backwards; }

    public void nextSlide(JLabel imgPanel, Image ImageB, double time) {
        m_forward.doTrans(imgPanel, m_image, ImageB, time);
    }

    public void returnToSlide(JLabel imgLabel, Image ImageB, double time) {
        m_backwards.doTrans(imgLabel, ImageB, m_image, time);
    }
}
