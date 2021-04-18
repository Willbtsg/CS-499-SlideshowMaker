package transitions;
import javax.swing.*;
import java.awt.*;

/**
 * Class name: UpWipe
 *
 * This class extends the abstract class "Transition" and replaces the old image with the new one from bottom-to-top using
 * a Wipe transition
 */

public class UpWipe extends Transition {

    /**
     * Public constructor for UpWipe. Sets m_type attribute from Transition abstract class to "UpWipe"
     */
    public UpWipe() {
        m_type = "UpWipe";
    }

    /**
     * Draws newImage in imgLabel using UpWipe effect
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
        int bY;       // Dimensions for newImage
        int imgWidth, imgHeight;
        int incY;                    // Y increment each time
        int numIterations = (int) (m_time * 0.05); // Number of steps in the Transition
        int timeInc; // Milliseconds to pause each time
        timeInc = (int) m_time / numIterations; //make each step last the same amount of time

        imgWidth = imgLabel.getWidth();
        imgHeight = imgLabel.getHeight();
        incY = imgHeight / numIterations;

        int fixY = numIterations - (imgHeight % numIterations);
        //since dividing dimensions by numIterations may have a truncated result...
        //...this is used to adjust the increment at certain point to ensure the Transition
        //...affects the entire image

        // Initialize the dimensions for section of newImage to draw
        bY = imgHeight - incY;

        for (int i = 1; i <= numIterations; i++) {
            // Draw part of B into A
            gPan.drawImage(newImage, 0, bY, imgWidth, imgHeight, 0, bY, imgWidth, imgHeight, null); // Draw portion of newImage in imgLabel

            if (i == fixY)
                incY += 1;

            bY -= incY;  // Take a bigger section next time
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
