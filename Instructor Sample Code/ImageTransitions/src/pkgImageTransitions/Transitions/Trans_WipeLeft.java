package pkgImageTransitions.Transitions;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pkgImageTransitions.Transition;

public class Trans_WipeLeft extends Transition
{
	
	//---------------------------------------------------
	// Default constructor
	//---------------------------------------------------
	public Trans_WipeLeft()
	{
		m_sType = "WIPE_LEFT";
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
	//      Copy from B iterationIndex * incX of B onto the screen overwriting Image A that is there
	//	        Sections of B are drawn from right to left
	//---------------------------------------------------------
	public void DrawImageTransition(JPanel imgPanel, BufferedImage ImageA, BufferedImage ImageB, double time)
	{
		Graphics gPan = imgPanel.getGraphics();
		Graphics gA = ImageA.getGraphics();
		
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
