package pkgImageTransitions.Transitions;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pkgImageTransitions.Transition;

public class Trans_PushRight extends Transition
{
	//---------------------------------------------------
	// Default constructor
	//---------------------------------------------------
	public Trans_PushRight()
	{
		m_sType = "PUSH_RIGHT";
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
	//	        to the right while B slides in from the left.
	//---------------------------------------------------------
	public void DrawImageTransition(JPanel imgPanel, BufferedImage ImageA, BufferedImage ImageB, double time)
	{
		Graphics gPan = imgPanel.getGraphics();
		Graphics gA = ImageA.getGraphics();
		
		// Dimension holders
		int sAX2, dAX1;		// Dimensions for imageA - ImageA Source X2, ImageA Destination X2
		int sBX1, dBX2;		// Dimensions for imageB - ImageB Source X2, ImageB Destination X1
		int imgWidth, imgHeight;
		int incX;					// X increment each time
		int numIterations = 50;		// Number of iterations in the sweep
		int timeInc;				// Milliseconds to pause each time
		timeInc = (int)(time * 1000) / numIterations;
		
		imgWidth = imgPanel.getWidth();
		imgHeight = imgPanel.getHeight();
		incX = imgWidth / numIterations;		// Do 1/20 each time to start
		
		// Initialize the dimensions for section of ImageA to move in itself
		sAX2 = imgWidth - incX;		// right X of rectangle to move rightward in A
		dAX1 = incX;				// left X of rectangle on screen to move section of A

		// Initialize the dimensions for section of ImageB to draw into ImageA
		sBX1 = imgWidth - incX;
		dBX2 = incX;

		// Draw the scaled current image if necessary
		gPan.drawImage(ImageA, 0, 0, imgPanel);

		// Draw image A
		for(int i=0; i<numIterations; i++)
		{
			// Move section of A to the right
			gPan.drawImage(ImageA, dAX1, 0, imgWidth, imgHeight, 0, 0, sAX2, imgHeight, null);
			// Draw part of B into A
			gPan.drawImage(ImageB, 0, 0, dBX2, imgHeight, sBX1, 0, imgWidth, imgHeight, null); // Draw portion of ImageB into ImageA
			// Take a bigger section next time
			sAX2 -= incX;
			dAX1 += incX;
			sBX1 -= incX;
			dBX2 += incX;
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
