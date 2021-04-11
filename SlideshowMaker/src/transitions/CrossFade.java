package transitions;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CrossFade extends Transition {

    /**
     * Public constructor for CrossFade. Sets m_type attribute from Transition abstract class to "CrossFade"
     */
    public CrossFade()
    {
        m_type = "CrossFade";
    }

    /**
     * Draws newImage in imgLabel using CrossFade effect
     *
     * NOTE: Function adapted from code written by Dr. Rick Coleman and provided by Dr. Harry Delugach
     *
     * @param imgLabel- JLabel where Images are displayed
     * @param newImage- new Image to be displayed
     */
    public void doTrans(JLabel imgLabel, Image newImage)
    {
        Graphics2D gPan = (Graphics2D) imgLabel.getGraphics();

        // Create a BufferedImage ARGB to hold the image to overlay
        BufferedImage newImage_ARGB = new BufferedImage(newImage.getWidth(null), newImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw newImage into newImage_ARGB
        newImage_ARGB.getGraphics().drawImage(newImage, 0, 0, null);

        int numIterations = (int) (m_time * 0.01) + 5; // Number of steps to the Transition scales based off length
        int timeInc; // Milliseconds to pause each time
        timeInc = (int) m_time / numIterations; //each step of the Transition lasts the same amount of time

        float alphaInc = (float) 1 / numIterations; //alpha should adjust around the same amount each time

        //Information and formula for alpha scaling comes from: https://javagraphics.blogspot.com/2008/06/crossfades-what-is-and-isnt-possible.html
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1-(1-alphaInc)*(1-alphaInc));
        gPan.setComposite(composite); //set alpha scale to be used when drawing the new Image

        // Draw image A -- appears we need to do this fade longer
        // Each time we redraw newImage_ARGB in imgLabel we add just a bit more
        for(int i=0; i<numIterations; i++)
        {
            // Draw B over A
            gPan.drawImage(newImage_ARGB, 0, 0, null);

            try
            {
                Thread.sleep(timeInc);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }

        //NOTE: we remove the original alpha composite because the max opacity we can get with this method is 97%
        gPan.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1)); //set alpha to fully opaque

        gPan.drawImage(newImage_ARGB, 0, 0, null); // finish drawing new image with full opacity
    }

}
