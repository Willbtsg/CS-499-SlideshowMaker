package pkgImageTransitions;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

//==========================================================================================
// class Transition
// Define the parent class that all image transitions will extend.  This way new transitions
//   can be easily added to the application later.  This class also contains two static
//   images into which the previously scaled for screen display images are stored for the
//   current image and the one to be transitioned to.
//
// Author: Dr. Rick Coleman
// Date: January 2016
//==========================================================================================
public class Transition
{
	/** Name of this transition */
	protected String m_sType;
	
	//--------------------------------------------------------
	/** Default constructor - Used only for sub-classes. */
	//--------------------------------------------------------
	public Transition()
	{
		// Sub-classes must set their type
	}
	
	//--------------------------------------------------------
	/** Parameterized constructor for parent class. */
	//--------------------------------------------------------
	public Transition(JPanel imgPanel)
	{
		m_sType = "NO_TRANS";
	}
	
	//--------------------------------------------------------
	/** Perform the transition from one image to another */
	// All subclasses must over ride this.  This only does
	//  an instant switch from one to the other
	// Args:  
	//  imgPanel - Panel the images are drawn into
	//	ImageA - Current Image on the screen
	//  ImageB - Image to transition to
	//  time - Number of seconds to take to do this transition
	//---------------------------------------------------------
	public void DrawImageTransition(JPanel imgPanel, BufferedImage ImageA, BufferedImage ImageB, double time)
	{
		// Get all the graphics objects
		Graphics gPan = imgPanel.getGraphics();
		Graphics gA = ImageA.getGraphics();
		
		// Default just copy ImageB into ImageA then copy to screen
	    gA.drawImage(ImageB, 0, 0, null);
	    gPan.drawImage(ImageA, 0, 0, imgPanel);
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
