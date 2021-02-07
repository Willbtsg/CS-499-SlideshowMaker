package pkgImageTransitions.Transitions;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pkgImageTransitions.Transition;

public class Trans_PushDown extends Transition
{
	//---------------------------------------------------
	// Default constructor
	//---------------------------------------------------
	public Trans_PushDown()
	{
		m_sType = "PUSH_DOWN";
	}
	
	//---------------------------------------------------
	/** Perform the transition from one image to another */
	// Args:  
	//  imgPanel - Panel the images are drawn into
	//	ImageA - Current Image on the screen
	//  ImageB - Image to transition to
	//  time - Number of seconds to take to do this transition
	// Note both off screen BufferedImages have already been
	// scaled to exactly fit in the area of the imgPanel
	// Basic algorithm:
	//   For each iteration
	//      Gradually decreasing in width sections of A are drawn then gradually increasing in width
	//			sections of B are draw in place of A giving the appearance of A gradually sliding
	//	        down while B slides down to replace it.
	//---------------------------------------------------------
	public void DrawImageTransition(JPanel imgPanel, BufferedImage ImageA, BufferedImage ImageB, double time)
	{
		Graphics gPan = imgPanel.getGraphics();
		Graphics gA = ImageA.getGraphics();
		
		// Dimension holders
		int sAY2, dAY1;		// Dimensions for imageA - ImageA Source Y1, ImageA Destination Y1
		int sBY1, dBY2;		// Dimensions for imageB - ImageB Source Y1, ImageB Destination Y2
		int imgWidth, imgHeight;
		int incY;					// Y increment each time
		int numIterations = 50;		// Number of iterations in the sweep
		int timeInc;				// Milliseconds to pause each time
		timeInc = (int)(time * 1000) / numIterations;
		
		imgWidth = imgPanel.getWidth();
		imgHeight = imgPanel.getHeight();
		incY = imgHeight / numIterations;		// Do 1/20 each time to start
		
		// Initialize the dimensions for section of ImageA to move in itself
		sAY2 = imgHeight - incY;
		dAY1 = incY;

		// Initialize the dimensions for section of ImageB to draw into ImageA
		dBY2 = incY;
		sBY1 = imgHeight - incY;
		
        // Draw the scaled current image if necessary
		gPan.drawImage(ImageA, 0, 0, imgPanel);

		// Draw image A
		for(int i=0; i<numIterations; i++)
		{
			// It appears that moving image A down within itself will not work because
			//  pixels become corrupted.  Try drawing both image parts directly to the scren
			// Move section of A down
			gPan.drawImage(ImageA, 0, dAY1, imgWidth, imgHeight, 0, 0, imgWidth, sAY2, null);
			// Draw part of B into A
			gPan.drawImage(ImageB, 0, 0, imgWidth, dBY2, 0, sBY1, imgWidth, imgHeight, null); // Draw portion of ImageB into ImageA
			// Take a bigger section next time
			sAY2 -= incY;
			dAY1 += incY;
			dBY2 += incY;
			sBY1 -= incY;
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
		ImageA.getGraphics().drawImage(ImageB, 0, 0, imgPanel);
		// And one final draw to the panel to be sure it's all there
		gPan.drawImage(ImageA, 0,0, imgPanel); 
	}
	
	//---------------------------------------------------
	/** Identifies what type of transition this is. */
	// See defined types above
	//---------------------------------------------------
	public String getTransitionType()
	{
		return m_sType;
	}

}