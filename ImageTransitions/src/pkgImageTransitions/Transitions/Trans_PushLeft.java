package pkgImageTransitions.Transitions;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pkgImageTransitions.Transition;

public class Trans_PushLeft extends Transition
{
	
	//---------------------------------------------------
	// Default constructor
	//---------------------------------------------------
	public Trans_PushLeft()
	{
		m_sType = "PUSH_LEFT";
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
	// Note 2: We need some way of estimating the time required
	//    to do a draw otherwise the time value is useless.
	//    Some platforms execute the transitions very rapidly
	//    others take much longer
	// Basic algorithm:
	//   For each iteration
	//      Gradually decreasing in width sections of A are drawn then gradually increasing in width
	//			sections of B are draw in place of A giving the appearance of A gradually sliding
	//	        to the left while B slides in from the right.
	//---------------------------------------------------------
	public void DrawImageTransition(JPanel imgPanel, BufferedImage ImageA, BufferedImage ImageB, double time)
	{
		Graphics gPan = imgPanel.getGraphics();
		Graphics gA = ImageA.getGraphics();
		
		// Dimension holders
		int dAX2, sAX1;			// Source and destination dimensions for imageA 
		int sBX2;				// Source dimensions for imageB
		int imgWidth, imgHeight;
		int incX;					// X increment each time
		int numIterations = 50;		// Number of iterations in the sweep
		int timeInc;				// Milliseconds to pause each time
		timeInc = (int)(time * 1000) / numIterations;
		
		imgWidth = imgPanel.getWidth();
		imgHeight = imgPanel.getHeight();
		incX = imgWidth / numIterations;		// Do 1/20 each time to start
		
		// Initialize the dimensions for section of ImageA to move in itself
		sAX1 = incX;			// Source left X of rectangle to move from A
		dAX2 = imgWidth - incX; // Destination right X of rectangle to move from A
		
		// Initialize the dimensions for section of ImageB to draw into ImageA
		sBX2 = incX;
//		dBX2 = imgHeight;

        // Draw the scaled current image if necessary
		gPan.drawImage(ImageA, 0, 0, imgPanel);

		// Draw image A
		for(int i=0; i<numIterations; i++)
		{
			// Move section of A to the screen
			gPan.drawImage(ImageA, 0, 0, dAX2, imgHeight, sAX1, 0, imgWidth, imgHeight, null);
			// Move section of B to the screen
			gPan.drawImage(ImageB, dAX2, 0, imgWidth, imgHeight, 0, 0, sBX2, imgHeight, null); // Draw portion of ImageB into ImageA
//			gPan.drawImage(ImageA, 0,0, imgPanel); // Copy ImageA into panel
			// Take a bigger section next time
			sAX1 += incX;
			dAX2 -= incX;
			sBX2 += incX;
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