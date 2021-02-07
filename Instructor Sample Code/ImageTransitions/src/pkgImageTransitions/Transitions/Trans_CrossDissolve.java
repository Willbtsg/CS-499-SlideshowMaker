package pkgImageTransitions.Transitions;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JPanel;

import pkgImageTransitions.Transition;

public class Trans_CrossDissolve extends Transition
{
	//---------------------------------------------------
	// Default constructor
	//---------------------------------------------------
	public Trans_CrossDissolve()
	{
		m_sType = "CROSS_DISSOLVE";
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
	// Cross dissolves need to be more gradual, i.e. more
	//   redraws with a smaller increment on the alpha
	//---------------------------------------------------------
	public void DrawImageTransition(JPanel imgPanel, BufferedImage ImageA, BufferedImage ImageB, double time)
	{
		Graphics gPan = imgPanel.getGraphics();
		Graphics2D gA = ImageA.createGraphics();
		
		int timeInc;				// Milliseconds to pause each time
		timeInc = (int)(time * 1000) / 40;
		// Create a BufferedImage ARGB to hold the image to overlay
		BufferedImage ImageB_ARGB = new BufferedImage(ImageB.getWidth(), ImageB.getHeight(), BufferedImage.TYPE_INT_ARGB);
		// Draw ImageB into ImageB_ARGB
		ImageB_ARGB.getGraphics().drawImage(ImageB, 0, 0, null);
		// Set up the initial fade data
		// Create a rescale filter op 
		float alphaInc = 0.20f;
		float[] scales = { 1.0f, 1.0f, 1.0f, alphaInc};
		float[] offsets = new float[4];
		RescaleOp rop = new RescaleOp(scales, offsets, null);

        // Draw the scaled current image if necessary
		gPan.drawImage(ImageA, 0, 0, imgPanel);

		// Draw image A -- appears we need to do this fade longer
		// Each time we redraw ImageB_ARGB over ImageA we add just a bit more
		for(int i=0; i<15; i++)
		{
			// Draw B over A. Note: Can't do the first draw directly into the screen panel
			//	because that drawImage only works with BufferedImages as the destination.
			gA.drawImage(ImageB_ARGB, rop, 0, 0); // Draw portion of ImageB into ImageA
			gPan.drawImage(ImageA, 0,0, imgPanel); // Copy ImageA into panel
			// Note: Can not pause here like we do in the other transitions because
			//     cross dissolve takes longer than a simple blit draw
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
