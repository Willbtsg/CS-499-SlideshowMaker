import javax.swing.*;
import java.awt.*;

/**
 * Class name: RLWipe
 *
 * This class extends the abstract class "Transition" and is used to transition between images using a right-to-left Wipe
 */

public class RLWipe extends Transition{

    /**
     * Public constructor for RLWipe. Sets m_type attribute from Transition abstract class to "RLWipe"
     */
    public RLWipe()
    {
        m_type = "RLWipe";
    }

    /**
     * Draws newImage in imgLabel using RLWipe effect
     *
     * NOTE: Function adapted from code written by Dr. Rick Coleman and provided by Dr. Harry Delugach
     *
     * @param imgLabel- JLabel where Images are displayed
     * @param newImage- new Image to be displayed
     */
    void doTrans(JLabel imgLabel, Image newImage)
    {
        Graphics gPan = imgLabel.getGraphics();

        // Dimension holders
        int bX;		// Dimensions for newImage
        int imgWidth, imgHeight;
        int incX;					// X increment each time
        int numIterations = 50;		// Number of iterations in the sweep
        int timeInc;				// Milliseconds to pause each time
        timeInc = (int) m_time / numIterations;

        imgWidth = imgLabel.getWidth();
        imgHeight = imgLabel.getHeight();
        incX = imgWidth / numIterations;		// Do 1/numIterations each time

        // Initialize the dimensions for section of newImage
        bX = imgWidth - incX;

        for(int i=0; i<numIterations; i++)
        {
            // Draw part of newImage within imgLabel
            gPan.drawImage(newImage, bX, 0, imgWidth, imgHeight, bX, 0, imgWidth, imgHeight, null); // Draw portion of newImage
            bX -= incX;  // Move another section to the left of the previous section
            // Pause a bit so we can actually see the transition
            try
            {
                Thread.sleep(timeInc);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }

        gPan.drawImage(newImage, 0,0, imgLabel);
    }

}
