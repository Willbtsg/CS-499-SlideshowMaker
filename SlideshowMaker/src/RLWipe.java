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
     * Replaces ImageA in imgLabel with ImageB over the course of "time" seconds
     *
     * NOTE: Function adapted from code written by Dr. Rick Coleman and provided by Dr. Harry Delugach
     *
     * @param imgLabel- JLabel where Images are displayed
     * @param ImageA- Image currently being displayed
     * @param ImageB- new Image to be displayed
     * @param time- length of the Transition in seconds
     */
    void doTrans(JLabel imgLabel, Image ImageA, Image ImageB, double time)
    {
        Graphics gPan = imgLabel.getGraphics();

        // Dimension holders
        int bX;		// Dimensions for imageB
        int imgWidth, imgHeight;
        int incX;					// X increment each time
        int numIterations = 50;		// Number of iterations in the sweep
        int timeInc;				// Milliseconds to pause each time
        timeInc = (int)(time * 1000) / numIterations;

        imgWidth = imgLabel.getWidth();
        imgHeight = imgLabel.getHeight();
        incX = imgWidth / numIterations;		// Do 1/numIterations each time

        // Initialize the dimensions for section of ImageB to draw into ImageA
        bX = imgWidth - incX;

        // Draw the scaled current image if necessary
        gPan.drawImage(ImageA, 0, 0, imgLabel);

        // Draw image A
        for(int i=0; i<numIterations; i++)
        {
            // Draw part of B over A on the screen
            gPan.drawImage(ImageB, bX, 0, imgWidth, imgHeight, bX, 0, imgWidth, imgHeight, null); // Draw portion of ImageB into ImageA
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

        gPan.drawImage(ImageB, 0,0, imgLabel);
    }

}
