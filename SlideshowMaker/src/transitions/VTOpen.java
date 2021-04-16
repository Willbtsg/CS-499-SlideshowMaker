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

        // Initialize the dimensions for section of newImage
        bYT = imgMiddle - incY;
        bYB = imgMiddle + incY;

        // Draw image A
        for(int i=0; i<numIterations; i++)
        {
            // Draw part of B into A
            gPan.drawImage(newImage, 0, bYT, imgWidth, imgMiddle, 0, bYT, imgWidth, imgMiddle,null); //Draw top portion of newImage in imgLabel
            gPan.drawImage(newImage, 0, imgMiddle, imgWidth, bYB, 0, imgMiddle, imgWidth, bYB,null); //Draw bottom portion of newImage

            bYT -= incY;  // Take a bigger section next time
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
