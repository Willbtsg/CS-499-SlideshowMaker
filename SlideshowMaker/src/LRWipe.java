import javax.swing.*;
import java.awt.*;

public class LRWipe extends Transition{

    public LRWipe()
    {
        m_type = "LRWipe";
    }

    void doTrans(JLabel imgPanel, Image ImageA, Image ImageB, double time)
    {
        Graphics gPan = imgPanel.getGraphics();
        //Graphics gA = gPan;

        // Dimension holders
        int bX1, bX2;		// Dimensions for imageB
        int imgWidth, imgHeight;
        int incX;					// X increment each time
        int numIterations = 50;		// Number of iterations in the sweep
        int timeInc;				// Milliseconds to pause each time
        timeInc = (int)(time * 1000) / numIterations;

        imgWidth = imgPanel.getWidth();
        imgHeight = imgPanel.getHeight();
        incX = imgWidth / numIterations;		// Do 1/20 each time to start

        // Initialize the dimensions for section of ImageB to draw into ImageA
        bX1 = 0;
        bX2 = incX;

        // Draw the scaled current image if necessary
        //gPan.drawImage(ImageA, 0, 0, imgPanel);

        // Draw image A
        for(int i=0; i<numIterations; i++)
        {
            // Draw part of B into A
            gPan.drawImage(ImageB, 0, 0, bX2, imgHeight, 0, 0, bX2, imgHeight,null); // Draw portion of ImageB into ImageA
            //bX1 = bX2;
            bX2 += incX;  // Take a bigger section next time
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
        // Move m_NextImage into m_CurrentImage for next time -  May not need this
        //gA.drawImage(ImageB, 0, 0, imgPanel);
        // And one final draw to the panel to be sure it's all there
        gPan.drawImage(ImageB, 0,0, imgPanel);
    }
}
