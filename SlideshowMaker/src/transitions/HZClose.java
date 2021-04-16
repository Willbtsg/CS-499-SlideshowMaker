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

        // Initialize the dimensions for section of newImage
        bXL = incX;
        bXR = imgWidth - incX;

        // Draw image A
        for (int i = 0; i < numIterations; i++) {
            // Draw part of B into A
            gPan.drawImage(newImage, 0, 0, bXL, imgHeight, 0, 0, bXL, imgHeight, null); //Draw left portion of newImage in imgLabel
            gPan.drawImage(newImage, bXR, 0, imgWidth, imgHeight, bXR, 0, imgWidth, imgHeight, null); //Draw right portion of newImage

            bXL += incX;  // Take a bigger section next time
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
