package pkgImageTransitions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import pkgImageTransitions.Transitions.Trans_CrossDissolve;
import pkgImageTransitions.Transitions.Trans_PushDown;
import pkgImageTransitions.Transitions.Trans_PushLeft;
import pkgImageTransitions.Transitions.Trans_PushRight;
import pkgImageTransitions.Transitions.Trans_PushUp;
import pkgImageTransitions.Transitions.Trans_WipeDown;
import pkgImageTransitions.Transitions.Trans_WipeLeft;
import pkgImageTransitions.Transitions.Trans_WipeRight;
import pkgImageTransitions.Transitions.Trans_WipeUp;

//============================================================================
/** This class implements the panel on which all images are drawn */
//===========================================================================
public class ImagePanel extends JPanel
{
	/** the parent JFrame */
	private JFrame m_Parent;

	/** The image to draw*/
	private BufferedImage theImage = null;
	
	/** Screen panel width */
	protected int m_iPanelWidth;
	
	/** Screen panel height */
	protected int m_iPanelHeight;

	/** Current image being displayed on the screen */
	public BufferedImage m_CurrentImage;
	
	/** Next image to be displayed after this transition */
	public BufferedImage m_NextImage;

	//----- Transition variables -----
	/** Vector of transition objects */
	private Vector<Transition> m_vTransitions;
	
	/** Random number generator used to select the transition to use */
	private Random m_RandGen;
	
	//--------------------------------------------------------
	/** Default constructor */
	//--------------------------------------------------------
	public ImagePanel(JFrame parent)
	{
		m_Parent = parent;
		this.setSize(m_Parent.getSize().width-5, 
				m_Parent.getSize().height-60);   // Set the size 
		this.setLocation(0, 0);       // Set the location in the window
		this.setBackground(Color.BLACK); // Set the panel color
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // Give it a border
		this.setLayout(null); // No layout manager.  We�ll place everything
		m_iPanelWidth = this.getWidth();
		m_iPanelHeight = this.getHeight();
//		System.out.println("Creating imagepanel size = " + m_iPanelWidth + " x " + m_iPanelHeight);
		// Create the off-screen BufferedImages for use by the transitions
		m_CurrentImage = new BufferedImage(m_iPanelWidth, m_iPanelHeight, BufferedImage.TYPE_INT_RGB);
		m_NextImage = new BufferedImage(m_iPanelWidth, m_iPanelHeight, BufferedImage.TYPE_INT_RGB);

		// Create an instance of all transitions available in the Transitions folder
		// For now we just hard code this.  Later read the directory and dynamically
		// create them
		m_vTransitions = new Vector<Transition>();
		// Index 0
		Transition baseTrans = new Transition(this); // Make sure everything gets initialized here
		m_vTransitions.add(baseTrans);
		// Index 1
		Trans_WipeLeft twl = new Trans_WipeLeft();
		m_vTransitions.add(twl);
		// Index 2
		Trans_WipeRight twr = new Trans_WipeRight();
		m_vTransitions.add(twr);
		// Index 3
		Trans_WipeUp twu = new Trans_WipeUp();
		m_vTransitions.add(twu);
		// Index 4
		Trans_WipeDown twd = new Trans_WipeDown();
		m_vTransitions.add(twd);
		// Index 5
		Trans_CrossDissolve tcd = new Trans_CrossDissolve();
		m_vTransitions.add(tcd);
		// Index 6
		Trans_PushLeft tpl = new Trans_PushLeft();
		m_vTransitions.add(tpl);
		// Index 7
		Trans_PushRight tpr = new Trans_PushRight();
		m_vTransitions.add(tpr);
		// Index 8
		Trans_PushUp tpu = new Trans_PushUp();
		m_vTransitions.add(tpu);
		// Index 9
		Trans_PushDown tpd = new Trans_PushDown();
		m_vTransitions.add(tpd);
		
		m_RandGen = new Random();
		
		// Draw black frame as first image
		m_CurrentImage.getGraphics().setColor(Color.BLACK);
		m_CurrentImage.getGraphics().drawRect(0, 0, m_iPanelWidth, m_iPanelHeight);
	}
	
	//--------------------------------------------------------
	/** Set the reference to the image to be drawn. */
	//--------------------------------------------------------
	public void setImage(BufferedImage nextImg)
	{
		theImage = nextImg;
		
		// Scale and copy this into the m_NextImage buffer
	    // Note: If the image is too large or too small we try to scale it
        int imgWidth = nextImg.getWidth(this);	// this panel is the ImageObserver required.
        int imgHeight = nextImg.getHeight(this);
        int posX, posY; // Position to place upper-left corner of image
        int imgScaleWidth, imgScaleHeight; // In case we need to scale - Using class vars temporarily
        
        // Set the upper left position of the image
        if(imgWidth <  m_iPanelWidth)
        	posX = (m_iPanelWidth - imgWidth) / 2;
        else
        	posX = 0;
        if(imgHeight < m_iPanelHeight)
        	posY = (m_iPanelHeight - imgHeight) / 2;
        else
        	posY = 0;
        // See if we need to scale it
        if((posX == 0) || (posY == 0))
        {
        	if(imgWidth > imgHeight) // Scale by width
        	{
        		imgScaleWidth = m_iPanelWidth;
        		imgScaleHeight = (int)Math.round(((double)m_iPanelWidth / (double)imgWidth) * 
        				(double)imgHeight);
        	}
        	else // Scale by height
        	{
        		imgScaleHeight = m_iPanelHeight;
        		imgScaleWidth = (int)Math.round(((double)m_iPanelHeight / (double)imgHeight) * 
        				(double)imgWidth);
        	}
        }
        else // Draw it normal size
        {
    		imgScaleWidth = imgWidth;
    		imgScaleHeight = imgHeight;
        }
        
        // Draw into m_NextImage
		Graphics gNext = m_NextImage.getGraphics();
		gNext.drawImage(nextImg, posX, posY, imgScaleWidth, imgScaleHeight, this);

		// Randomly select a transition and pass this on to it
		int transIdx = m_RandGen.nextInt(m_vTransitions.size());
		
		// Do random transition taking 2.0 seconds
		m_vTransitions.elementAt(transIdx).DrawImageTransition(this, m_CurrentImage, m_NextImage, 2.0);
		// For testing a single transition comment out the above line and put the appropriate
		//   index in the vector of transitions in the call to m_vTransitions.elemantAt(i)
//		m_vTransitions.elementAt(5).DrawImageTransition(this, m_CurrentImage, m_NextImage, 2.0);  
	}
	
	//--------------------------------------------------------
	/** Override the paint function to draw the image. */
	//--------------------------------------------------------
	public void paint(Graphics g)
	{
		this.paintComponent(g);
		this.paintBorder(g);
		if(this.theImage != null)
		{
			g.drawImage(m_CurrentImage, 0, 0, null);
		}
		else
		{
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, m_iPanelWidth, m_iPanelHeight);
		}

	}
}
