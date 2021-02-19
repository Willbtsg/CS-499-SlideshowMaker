import javax.swing.*;
import java.awt.*;

public class RLWipe extends Transition{

    void doTrans(JLabel imgPanel, Image ImageA, Image ImageB, double time)
    {
        Graphics gPan = imgPanel.getGraphics();
        //Graphics gA = ImageA.getGraphics();

        // Dimension holders
        int bX1, bX2;		// Dimensions for imageB
        int imgWidth, imgHeight;
        int incX;					// X increment each time
        int numIterations = 50;		// Number of iterations in the sweep
        int timeInc;				// Milliseconds to pause each time
        timeInc = (int)(time * 1000) / numIterations;

        imgWidth = imgPanel.getWidth();
        imgHeight = imgPanel.getHeight();
        incX = imgWidth / numIterations;		// Do 1/numIterations each time

        // Initialize the dimensions for section of ImageB to draw into ImageA
        bX1 = imgWidth - incX;
        bX2 = incX;

        // Draw the scaled current image if necessary
        gPan.drawImage(ImageA, 0, 0, imgPanel);

        // Draw image A
        for(int i=0; i<numIterations; i++)
        {
            // Draw part of B over A on the screen
            gPan.drawImage(ImageB, bX1, 0, imgWidth, imgHeight, bX1, 0, imgWidth, imgHeight, null); // Draw portion of ImageB into ImageA
            bX2 = bX1;
            bX1 -= incX;  // Move another section to the left of the previous section
            // Pause a bit so we can actually see the transition
            try
            {
                Thread.sleep(timeInc);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }
        // Move m_NextImage into m_CurrentImage for next time -  May not need this
        //ImageA.getGraphics().drawImage(ImageB, 0, 0, imgPanel);
        // And one final draw to the panel to be sure it's all there
        gPan.drawImage(ImageB, 0,0, imgPanel);
    }

}
