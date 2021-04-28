package slideshow;

import transitions.Transition;
import transitions.TransitionLibrary;

import org.json.simple.JSONObject;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.IIOException;
import java.util.ArrayList;

/**
 * Class name: Slide
 *
 * This class contains information necessary for displaying slideshow images, including timing and transitions.
 */

public class Slide {

    /**
     * String m_name- contains filepath for desired image
     * private BufferedImage m_image- contains scaled image for this Slide
     * Transition m_forward- reference to specified Transition used when moving forward to this Slide
     * Transition m_backwards- reference to Transition to be used when moving backwards from this Slide
     * Transition m_hasTransition- flag to indicate whether or not this Slide has transitions
     * int m_time- amount of time to display Slide image during automated Slideshow
     */
    private String m_name;
    private BufferedImage m_image;
    private Transition m_forward;
    private Transition m_backwards;
    private Boolean m_hasTransitions;
    private long m_time;
    private static final GraphicsConfiguration config = 
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

    /**
     * Constructor for Slide object. Sets filename before assigning Transitions
     * @param name- accepts image name (i.e. filepath) as argument
     */
    public Slide (String name)
    {
        m_name = name;
        setImage(name);
        m_hasTransitions = false;
    }

    /**
     * Constructor for Slide object. Pulls all data form JSONObject
     * @param j- JSONObject containing Slide information
     */
    public Slide (JSONObject j)
    {
        m_name = (String) j.get("name");

        setImage(m_name);

        m_time = (long) j.get("imgTime");

        if ((Boolean) j.get("hasTransitions")) //only try to assign Transitions if the Slide is supposed to have them
        {

            TransitionLibrary transitionLibrary = TransitionLibrary.getInstance(); //retrieve TransitionLibrary Singleton
            ArrayList<transitions.Transition> tempTransitions;

            tempTransitions = transitionLibrary.retrieveTransitions((String) j.get("forward"));

            for (Transition transition : tempTransitions)
            {
                transition.setTime((long) j.get("transTime"));
            }

            m_forward = tempTransitions.get(0);
            m_backwards = tempTransitions.get(1);
            m_hasTransitions = true;
        }
        else {
            m_hasTransitions = false;
        }
    }

    //https://stackoverflow.com/a/53226346/5763413
    @SuppressWarnings("unchecked")
    /**
     * Creates a JSONObject of the Slide
     * @return a JSONObject
     *
     */
    public JSONObject toJSON()
    {
        JSONObject obj = new JSONObject();
        try
        {
            obj.put("imgTime", m_time);
            obj.put("name", m_name);
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
     * used to convert an image filename into a scaled BufferedImage to be used in the Slideshow
     * @param name- name of image file to be read and scaled
     */
    public void setImage(String name)
    {
    	// Get screen size width and height
    	Dimension scrnSize = Toolkit. getDefaultToolkit().getScreenSize();
    	int scrnWidth = 1400;
    	int scrnHeight = 800;
    	
        try {
        	// Read in original image
            BufferedImage orgImage = ImageIO.read(new File(name));
            
            
            // Declare temp image variable and proportion variables for sizing the image
            Image tempImage;
            double proportionW = 1;
            double proportionH = 1;
            
            // If landscape photo
            if (orgImage.getHeight() < orgImage.getWidth()) {
            	// Get height proportion value
            	proportionH = (double) (orgImage.getHeight())/(double) (orgImage.getWidth());
            	
            	// If new height exceeds boundary height
            	if ((int) (scrnWidth*proportionH) > (int) (scrnHeight*0.85)){ 
            		// Resize to where the bottom or top of image isn't cut off
                	proportionW = (double) (orgImage.getWidth())/(double) (orgImage.getHeight());
                	tempImage = orgImage.getScaledInstance((int) ((scrnHeight*0.85)*proportionW),(int) (scrnHeight*0.85), Image.SCALE_SMOOTH);
                	
                	// Reset H value 
                	proportionH = 1;
            	}else { // Otherwise, resize image as is
            		tempImage = orgImage.getScaledInstance((int) (scrnWidth),(int) (scrnWidth*proportionH), Image.SCALE_SMOOTH);
            	}
            } else { // If portrait or square photo
            	proportionW = (double) (orgImage.getWidth())/(double) (orgImage.getHeight());
            	tempImage = orgImage.getScaledInstance((int) ((scrnHeight*0.85)*proportionW),(int) (scrnHeight*0.85), Image.SCALE_SMOOTH);
            }
            
            // Load image with full boundary area
            m_image = new BufferedImage(scrnWidth,(int) (scrnHeight*0.85), BufferedImage.TYPE_INT_ARGB);
            
            // Calculate offsets for image placement
            int xOffset = (int)((scrnWidth - ((scrnHeight*0.85)*proportionW))/2);
            int yOffset = (int)(((scrnHeight*0.85) - (scrnWidth*proportionH))/2);
            
            // Start drawing
            Graphics2D g2d = m_image.createGraphics();
            g2d.setColor(Color.BLACK); // Set color to black
            
            // Fill background with black square to cover whole boundary
            g2d.fillRect(0, 0, scrnWidth, (int)(scrnHeight*0.85));
            
            // Determine draw case and draw image
            if (proportionH < 1) {
            	g2d.setColor(Color.WHITE); // Set color to white
            	g2d.fillRect(0, yOffset,(int) (scrnWidth),(int) (scrnWidth*proportionH)); // Put rectangle that fills behind the picture with white (to display transparent images)
            	g2d.drawImage(tempImage, 0, yOffset, null);
            }
            else {
            	g2d.setColor(Color.WHITE); // Set color to white
            	g2d.fillRect(xOffset, 0,(int) ((scrnHeight*0.85)*proportionW),(int) (scrnHeight*0.85)); // Put rectangle that fills behind the picture with white (to display transparent images)
            	g2d.drawImage(tempImage, xOffset, 0, null);
            }
            g2d.dispose(); // Stop drawing

        } 
        catch (IIOException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the image assigned to this Slide
     * @return m_image- Image to be shown in JLabel
     */
    public Image getImage() { return m_image; }

    /**
     * Sets amount of time for Slide to be displayed during an automated Slideshow
     * @param time- display time
     */
    public void setTime(long time) { m_time = time; }

    /**
     * Returns time that a Slide is displayed during an automated Slideshow
     * @return
     */
    public double getTime() { return m_time; }

    /**
     * Used to check whether or not this Slide has Transitions
     * @return m_hasTransitions- indicates whether or not this Slide uses Transitions
     */
    public Boolean hasTransitions() { return m_hasTransitions; }

    /**
     * Returns how long the Transition is supposed to last (used when calculating Slideshow runtime)
     * @return
     */
    public double getTransTime() { return m_forward.getTime(); }

    /**
     * Sets both Transitions at once based off the users selection from the Transition dropdown in Timeline
     * @param transition- what Transition (if any) the user wishes to use
     */
    public void setTransitions(String transition)
    {
        if (!transition.equals("None"))
        {
            TransitionLibrary transitionLibrary = TransitionLibrary.getInstance();
            ArrayList<Transition> transitions = transitionLibrary.retrieveTransitions(transition);
            m_forward = transitions.get(0);
            m_backwards = transitions.get(1);
            m_hasTransitions = true;
        }
        else
        {
            m_hasTransitions = false;
        }
    }

    /**
     * If the Slide has Transitions, set their duration to the long being passed in
     * @param length- new duration for this Slide's Transitions
     */
    public void setTransitionLength(long length)
    {
        if (m_hasTransitions)
        {
            m_forward.setTime(length);
            m_backwards.setTime(length);
        }
    }

    /**
     * Passes image and timing information necessary for forward Transition execution
     * @param imgLabel- JLabel used to display Image
     */
    public void nextSlide(JLabel imgLabel) {
        m_forward.doTrans(imgLabel, m_image);
    }

    /**
     * Passes image and timing information necessary for backwards Transition execution
     * @param imgLabel- JLabel used to display Image
     * @param desiredImage- Destination Image (the one not being currently displayed)
     */
    public void returnToSlide(JLabel imgLabel, Image desiredImage) {
        m_backwards.doTrans(imgLabel, desiredImage);
    }
}
