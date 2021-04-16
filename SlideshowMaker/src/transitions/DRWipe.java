package transitions;

import javax.swing.*;
import java.awt.*;


/**
 * Class name: DRWipe
 *
 * This class extends the abstract class "Transition" and is used to transition between images using a downward & left-to-right Wipe
 */

public class DRWipe extends Transition {

    /**
     * Public constructor for DRWipe. Sets m_type attribute from Transition abstract class to "DRWipe"
     */
    public DRWipe()
    {
        m_type = "DRWipe";
    }

    /**
     * Draws newImage in imgLabel using DRWipe effect
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
        int bX, bY; // Dimensions for newImage
        int imgWidth, imgHeight;
        int incX; // X increment each time
        int incY; // Y increment each time
        int numIterations = (int) (m_time * 0.05); // Number of steps in the Transition
        int timeInc; // Milliseconds to pause each time
        timeInc = (int) m_time / numIterations; //make each step last the same amount of time

        imgWidth = imgLabel.getWidth();
        imgHeight = imgLabel.getHeight();
        incX = imgWidth / numIterations;
        incY = imgHeight / numIterations;		// Do 1/50 each time to start

        // Initialize the dimensions for section of newImage
        bY = 0;

        // Initialize the dimensions for section of newImage
        bX = incX;

        // Draw image A
        for(int i=0; i<numIterations; i++)
        {
            // Draw part of B into A
            gPan.drawImage(newImage, 0, 0, bX, bY, 0, 0, bX, bY,null); // Draw portion of newImage in imgLabel
            bX += incX;  // Take a bigger section next time
            bY += incY;
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
