package transitions;

import javax.swing.*;
import java.awt.*;


/**
 * Class name: ULWipe
 *
 * This class extends the abstract class "Transition" and is used to transition between images using a upward & right-to-left Wipe
 */

public class ULWipe extends Transition {

    /**
     * Public constructor for ULWipe. Sets m_type attribute from Transition abstract class to "ULWipe"
     */
    public ULWipe()
    {
        m_type = "ULWipe";
    }

    /**
     * Draws newImage in imgLabel using ULWipe effect
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

        int fixX = numIterations - (imgWidth % numIterations);
        int fixY = numIterations - (imgHeight % numIterations);
        //since dividing dimensions by numIterations may have a truncated result...
        //...these are used to adjust the increments at certain point to ensure the Transition
        //...affects the entire image

        // Initialize the dimensions for section of newImage
        bX = imgWidth - incX;
        bY = imgHeight - incY;

        // Draw image A
        for(int i=1; i<=numIterations; i++)
        {
            // Draw part of B into A
            gPan.drawImage(newImage, bX, bY, imgWidth, imgHeight, bX, bY, imgWidth, imgHeight,null); // Draw portion of newImage in imgLabel

            if (i == fixX)
                incX += 1;
            if (i == fixY)
                incY += 1;

            bX -= incX;  // Take a bigger section next time
            bY -= incY;
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
