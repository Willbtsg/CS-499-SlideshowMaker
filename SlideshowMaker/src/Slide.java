import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class name: Slide
 *
 * This class contains information necessary for displaying slideshow images, including timing and transitions.
 */

public class Slide {

    /**
     * String m_name- contains filepath for desired image
     * private BufferedImage m_image- contains scaled image for this Slide
     * Transition m_forward- reference to specified Transition used when moving to next slide
     * Transition m_backwards- reference to Transition to be used when returning to this slide
     * Transition m_hasTransition- flag to indicate whether or not this Slide has transitions
     */
    private String m_name;
    private BufferedImage m_image;
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

        try {
            BufferedImage orgImage = ImageIO.read(new File(name));
            Image tempImage = orgImage.getScaledInstance(500, 313, Image.SCALE_SMOOTH);
            m_image = new BufferedImage(500, 313, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = m_image.createGraphics();
            g2d.drawImage(tempImage, 0, 0, null);
            g2d.dispose();

        } catch (IOException e) {
            e.printStackTrace();
        }

        m_hasTransitions = false;

        return;
    }

    /**
     * Function will likely be deleted later since JSONObjects are now handled by the SlideFactory
     * @param j
     */
    public Slide(JSONObject j)
    {
        m_name = (String) j.get("name");

        try {
            BufferedImage orgImage = ImageIO.read(new File(m_name));
            Image tempImage = orgImage.getScaledInstance(500, 313, Image.SCALE_SMOOTH);
            m_image = new BufferedImage(500, 313, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = m_image.createGraphics();
            g2d.drawImage(tempImage, 0, 0, null);
            g2d.dispose();

        } catch (IOException e) {
            e.printStackTrace();
        }

        m_forward = new LRWipe();
        m_backwards = new RLWipe();
        m_hasTransitions = true;

    }

    //https://stackoverflow.com/a/53226346/5763413
    @SuppressWarnings("unchecked")
    /**
     * Creates a JSONObject of the Slide
     * @return a JSONObject
     *
     */
    public  JSONObject toJSON()
    {
        JSONObject obj = new JSONObject();
        try
        {
            obj.put("name", m_name) ;
            obj.put("hasTransitions", m_hasTransitions);

            if (m_hasTransitions) //only try to write Transition information if the Slide has Transitions
            {
                obj.put("forward", m_forward.getType()); //since m_backwards is dependent on the type of m_forward, only one has to be stored
                obj.put("transTime", m_forward.getTime());
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

    /**
     * Returns the image assigned to this Slide
     * @return m_image- Image to be shown in JLabel
     */
    public Image getImage() { return m_image; }

    /**
     * Returns type/class name of Transition being used to move to next Slide
     * @return- retrieves String of type/class name from Transition and returns it
     */
    public String getForward() { return m_forward.getType(); }

    /**
     * Used to check whether or not this Slide has Transitions
     * @return m_hasTransitions- indicates whether or not this Slide uses Transitions
     */
    public Boolean hasTransitions() { return m_hasTransitions; }

    /**
     * Assigns forward Transition to the Slide and sets m_hasTransitions to true
     * @param forward- reference to Transition being assigned to the Slide
     */
    public void setForward(Transition forward)
    {
        m_forward = forward;
        m_hasTransitions = true;

        return;
    }

    /**
     * Assigns backwards Transition to the Slide and sets m_hasTransitions to true
     * @param backwards- reference to Transition being assigned to the Slide
     */
    public void setBackwards(Transition backwards)
    {
        m_backwards = backwards;
        m_hasTransitions = true;

        return;

    }

    /**
     * Passes image and timing information necessary for forward Transition execution
     * @param imgLabel- JLabel used to display Image
     * @param ImageB- Destination Image (the one not being currently displayed)
     */
    public void nextSlide(JLabel imgLabel, Image ImageB) {
        m_forward.doTrans(imgLabel, m_image, ImageB);
    }

    /**
     * Passes image and timing information necessary for backwards Transition execution
     * @param imgLabel- JLabel used to display Image
     * @param ImageB- Destination Image (the one not being currently displayed)
     */
    public void returnToSlide(JLabel imgLabel, Image ImageB) {
        m_backwards.doTrans(imgLabel, ImageB, m_image);
    }
}
