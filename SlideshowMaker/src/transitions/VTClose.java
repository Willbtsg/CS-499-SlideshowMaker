package transitions;
import javax.swing.*;
import java.awt.*;

/**
 * Class name: VTClose
 *
 * This class extends the abstract class "Transition" and is used to transition between images in a manner similar to sliding
 * doors closing vertically
 */

public class VTClose extends Transition{

    /**
     * Public constructor for VTClose. Sets m_type attribute from Transition abstract class to "VTClose"
     */
    public VTClose()
    {
        m_type = "VTClose";
    }

    /**
     * Draws newImage in imgLabel using VTClose effect
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
        int bYT, bYB;        // Dimensions for newImage
        int imgWidth, imgHeight;
        int incY; // X increment each time
        int numIterations = (int) (m_time * 0.05); // Number of steps in the Transition
        int timeInc; // Milliseconds to pause each time
        timeInc = (int) m_time / numIterations; //make each step last the same amount of time

        imgWidth = imgLabel.getWidth();
        imgHeight = imgLabel.getHeight();
        incY = imgHeight / (numIterations * 2);

        // Initialize the dimensions for section of newImage
        bYT = incY;
        bYB = imgHeight - incY;

        // Draw image A
        for (int i = 0; i < numIterations; i++) {
            // Draw part of B into A
            gPan.drawImage(newImage, 0, 0, imgWidth, bYT, 0, 0, imgWidth, bYT, null); //Draw top portion of newImage in imgLabel
            gPan.drawImage(newImage, 0, bYB, imgWidth, imgHeight, 0, bYB, imgWidth, imgHeight, null); //Draw bottom portion of newImage

            bYT += incY;  // Take a bigger section next time
            bYB -= incY;

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
