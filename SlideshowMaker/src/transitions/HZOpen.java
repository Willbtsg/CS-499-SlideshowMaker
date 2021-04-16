package transitions;
import javax.swing.*;
import java.awt.*;

/**
 * Class name: HZOpen
 *
 * This class extends the abstract class "Transition" and is used to transition between images in a manner similar to sliding
 * doors opening horizontally
 */

public class HZOpen extends Transition{

    /**
     * Public constructor for HZOpen. Sets m_type attribute from Transition abstract class to "HZOpen"
     */
    public HZOpen()
    {
        m_type = "HZOpen";
    }

    /**
     * Draws newImage in imgLabel using HZOpen effect
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
        int bXL, bXR;		// Dimensions for newImage
        int imgWidth, imgHeight, imgMiddle;
        int incX; // X increment each time
        int numIterations = (int) (m_time * 0.05); // Number of steps in the Transition
        int timeInc; // Milliseconds to pause each time
        timeInc = (int) m_time / numIterations; //make each step last the same amount of time

        imgWidth = imgLabel.getWidth();
        imgHeight = imgLabel.getHeight();
        imgMiddle = imgWidth / 2;
        incX = imgWidth / (numIterations * 2);

        // Initialize the dimensions for section of newImage
        bXL = imgMiddle - incX;
        bXR = imgMiddle + incX;

        // Draw image A
        for(int i=0; i<numIterations; i++)
        {
            // Draw part of B into A
            gPan.drawImage(newImage, bXL, 0, imgMiddle, imgHeight, bXL, 0, imgMiddle, imgHeight,null); //Draw left portion of newImage in imgLabel
            gPan.drawImage(newImage, imgMiddle, 0, bXR, imgHeight, imgMiddle, 0, bXR, imgHeight,null); //Draw right portion of newImage

            bXL -= incX;  // Take a bigger section next time
            bXR += incX;

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
