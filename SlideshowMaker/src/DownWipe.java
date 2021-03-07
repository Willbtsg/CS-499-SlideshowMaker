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
     * Replaces ImageA in imgLabel with ImageB
     *
     * NOTE: Function adapted from code written by Dr. Rick Coleman and provided by Dr. Harry Delugach
     *
     * @param imgLabel- JLabel where Images are displayed
     * @param ImageA- Image currently being displayed
     * @param ImageB- new Image to be displayed
     */
    void doTrans(JLabel imgLabel, Image ImageA, Image ImageB)
    {
        Graphics gPan = imgLabel.getGraphics();

        // Dimension holders
        int bY1, bY2;		// Dimensions for imageA
        int imgWidth, imgHeight;
        int incY;					// Y increment each time
        int numIterations = 50;		// Number of iterations in the sweep
        int timeInc;				// Milliseconds to pause each time
        timeInc = (int) m_time / numIterations;

        imgWidth = imgLabel.getWidth();
        imgHeight = imgLabel.getHeight();
        incY = imgHeight / numIterations;		// Do 1/20 each time to start

        // Initialize the dimensions for section of ImageB to draw into ImageA
        bY1 = 0;
        bY2 = incY;

        // Draw the scaled current image if necessary
        gPan.drawImage(ImageA, 0, 0, imgLabel);

        // Draw image A
        for(int i=0; i<numIterations; i++)
        {
            // Draw part of B into A
            gPan.drawImage(ImageB, 0, bY1, imgWidth, bY2, 0, bY1, imgWidth, bY2, null); // Draw portion of ImageB into ImageA
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
        gPan.drawImage(ImageB, 0,0, imgLabel);
    }

}
