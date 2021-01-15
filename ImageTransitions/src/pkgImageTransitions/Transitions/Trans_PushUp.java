package pkgImageTransitions.Transitions;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pkgImageTransitions.Transition;

public class Trans_PushUp extends Transition
{
	//---------------------------------------------------
	// Default constructor
	//---------------------------------------------------
	public Trans_PushUp()
	{
		m_sType = "PUSH_UP";
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
	//	        up while B slides up to replace it.
	//---------------------------------------------------------
	public void DrawImageTransition(JPanel imgPanel, BufferedImage ImageA, BufferedImage ImageB, double time)
	{
		Graphics gPan = imgPanel.getGraphics();
		Graphics gA = ImageA.getGraphics();
		
		// Dimension holders
		int sAY1, dAY2;			// Dimensions for imageA 
		int sBY2, dBY1;		// Dimensions for imageB - ImageB Source Y2, ImageB Destination Y1
		int imgWidth, imgHeight;
		int incY;					// Y increment each time
		int numIterations = 50;		// Number of iterations in the sweep
		int timeInc;				// Milliseconds to pause each time
		timeInc = (int)(time * 1000) / numIterations;
		
		imgWidth = imgPanel.getWidth();
		imgHeight = imgPanel.getHeight();
		incY = imgHeight / numIterations;		// Do 1/20 each time to start
		
		// Initialize the dimensions for section of ImageA to move to screen
		sAY1 =  incY;
		dAY2 = imgHeight - incY; // bottom Y of rectangle to move upward in A

		// Initialize the dimensions for section of ImageB to draw into ImageA
		sBY2 = incY;
//		dBY1 
		
        // Draw the scaled current image if necessary
		gPan.drawImage(ImageA, 0, 0, imgPanel);

		// Draw image A
		for(int i=0; i<numIterations; i++)
		{
			// Move section of A up
			gPan.drawImage(ImageA, 0, 0, imgWidth, dAY2, 0, sAY1, imgWidth, imgHeight, null);
			// Draw part of B into A
			gPan.drawImage(ImageB, 0, dAY2, imgWidth, imgHeight, 0, 0, imgWidth, sBY2, null); // Draw portion of ImageB into ImageA
			// Take a bigger section next time
			sAY1 += incY;
			dAY2 -= incY;
			sBY2 += incY;
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