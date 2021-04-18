package transitions;
import javax.swing.*;
import java.awt.*;

/**
 * Class name: HZClose
 *
 * This class extends the abstract class "Transition" and is used to transition between images in a manner similar to sliding
 * doors closing horizontally
 */

public class HZClose extends Transition{

    /**
     * Public constructor for HZClose. Sets m_type attribute from Transition abstract class to "HZClose"
     */
    public HZClose()
    {
        m_type = "HZClose";
    }

    /**
     * Draws newImage in imgLabel using HZClose effect
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
        int bXL, bXR;        // Dimensions for newImage
        int imgWidth, imgHeight;
        int incX; // X increment each time
        int numIterations = (int) (m_time * 0.05); // Number of steps in the Transition
        int timeInc; // Milliseconds to pause each time
        timeInc = (int) m_time / numIterations; //make each step last the same amount of time

        imgWidth = imgLabel.getWidth();
        imgHeight = imgLabel.getHeight();
        incX = imgWidth / (numIterations * 2);

        //the next three variables are used to correct the pixel offset created by truncation when calculating increment size
        boolean fixCheck; //flag to see if it's time to make the correction
        boolean fixed = (imgWidth / numIterations) % 2 == 0; //sets flag to indicate if correction is necessary
        int pixelsReplaced = 0; //keeps track of progress to determine when to make the correction

        // Initialize the dimensions for section of newImage
        bXL = incX;
        bXR = imgWidth - incX;

        // Draw image A
        for (int i = 1; i <= numIterations; i++) {
            // Draw part of B into A
            gPan.drawImage(newImage, 0, 0, bXL, imgHeight, 0, 0, bXL, imgHeight, null); //Draw left portion of newImage in imgLabel
            gPan.drawImage(newImage, bXR, 0, imgWidth, imgHeight, bXR, 0, imgWidth, imgHeight, null); //Draw right portion of newImage

            if (!fixed) //if the increment needs to be adjusted
            {
                pixelsReplaced += (incX * 2); //calculate Transition progress

                // The following line calculates the number of pixels still unchanged and divides them by the number of steps remaining...
                // ...in order to see how big the remaining pieces of the Wipe would be if they were of equal size.
                // If this number is divisible by 2, add 1 to the increment to correct the truncation error and ensure the Wipe displays the entire image
                fixCheck = (((imgHeight - pixelsReplaced) / (numIterations - i)) % 2) == 0;

                if (fixCheck)
                {
                    incX += 1;
                    fixed = true; //set the flag so these checks aren't done more than necessary
                }
            }

            bXL += incX;  // update the boundaries of the Wipe
            bXR -= incX;

            // Pause a bit
            try {
                Thread.sleep(timeInc);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        gPan.drawImage(newImage, 0, 0, imgLabel);
    }
}
