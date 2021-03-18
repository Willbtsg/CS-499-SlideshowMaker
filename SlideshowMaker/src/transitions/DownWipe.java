package transitions;
import javax.swing.*;
import java.awt.*;

public class DownWipe extends Transition {

    /**
     * Public constructor for DownWipe. Sets m_type attribute from Transition abstract class to "DownWipe"
     */
    public DownWipe()
    {
        m_type = "DownWipe";
    }

    /**
     * Draws newImage in imgLabel using DownWipe effect
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
        int bY1, bY2;		// Dimensions for newImage
        int imgWidth, imgHeight;
        int incY;					// Y increment each time
        int numIterations = 50;		// Number of iterations in the sweep
        int timeInc;				// Milliseconds to pause each time
        timeInc = (int) m_time / numIterations;

        imgWidth = imgLabel.getWidth();
        imgHeight = imgLabel.getHeight();
        incY = imgHeight / numIterations;		// Do 1/20 each time to start

        // Initialize the dimensions for section of newImage
        bY1 = 0;
        bY2 = incY;

        // Draw image A
        for(int i=0; i<numIterations; i++)
        {
            // Draw part of B into A
            gPan.drawImage(newImage, 0, bY1, imgWidth, bY2, 0, bY1, imgWidth, bY2, null); // Draw portion of newImage
            bY1 = bY2;
            bY2 += incY;  // Take a bigger section next time
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
