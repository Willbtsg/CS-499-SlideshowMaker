package transitions;
import javax.swing.*;
import java.awt.*;

/**
 * Class name: VTOpen
 *
 * This class extends the abstract class "Transition" and is used to transition between images in a manner similar to sliding
 * doors opening vertically
 */

public class VTOpen extends Transition {

    /**
     * Public constructor for VTOpen. Sets m_type attribute from Transition abstract class to "VTOpen"
     */
    public VTOpen()
    {
        m_type = "VTOpen";
    }

    /**
     * Draws newImage in imgLabel using VTOpen effect
     *
     * NOTE: Function adapted from code written by Dr. Rick Coleman and provided by Dr. Harry Delugach
     *
     * @param imgLabel- JLabel where Images are displayed
     * @param newImage- new Image to be displayed
     */
    public void doTrans(JLabel imgLabel, Image newImage)
    {
        Graphics gPan = imgLabel.getGraphics();

        // Dimension holders
        int bYT, bYB; // Dimension for newImage
        int imgWidth, imgHeight, imgMiddle;
        int incY;					// Y increment each time
        int numIterations = (int) (m_time * 0.05); // Number of steps in the Transition
        int timeInc; // Milliseconds to pause each time
        timeInc = (int) m_time / numIterations; //make each step last the same amount of time

        imgWidth = imgLabel.getWidth();
        imgHeight = imgLabel.getHeight();
        imgMiddle = imgHeight / 2;
        incY = imgHeight / (numIterations * 2);

        //the next three variables are used to correct the pixel offset created by truncation when calculating increment size
        boolean fixCheck; //flag to see if its time to make the correction
        boolean fixed = false; //flag to say whether or not the correction has been made
        int pixelsReplaced = 0; //keeps track of progress to determine when to make the correction

        // Initialize the dimensions for section of newImage
        bYT = imgMiddle - incY;
        bYB = imgMiddle + incY;

        // Draw image A
        for(int i=1; i<=numIterations; i++)
        {
            // Draw part of B into A
            gPan.drawImage(newImage, 0, bYT, imgWidth, bYB, 0, bYT, imgWidth, bYB,null); //Draw larger portion of newImage in imgLabel

            if (!fixed) //if the increment has not been adjusted
            {
                pixelsReplaced += (incY * 2); //calculate Transition progress

                // The following line calculates the number of pixels still unchanged and divides them by the number of steps remaining...
                // ...in order to see how big the remaining pieces of the Wipe would be if they were of equal size.
                // If this number is divisible by 2, add 1 to the increment to correct the truncation error and ensure the Wipe displays the entire image
                fixCheck = (((imgHeight - pixelsReplaced) / (numIterations - i)) % 2) == 0;

                if (fixCheck)
                {
                    incY += 1;
                    fixed = true; //set the flag so these checks aren't done more than necessary
                }
            }

            bYT -= incY;  // update the boundaries of the Wipe
            bYB += incY;

            // Pause a bit
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
