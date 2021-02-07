package pkgImageTransitions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

//=============================================================================
/** Class: ImageViewer
 *  Purpose: This class implements the main window for an image viewer utility.
 *    It allows the user to select a directory of images (.jpg and/or .gif)
 *    and display them sequentially.  The user can switch images by clicking
 *    a button to move forward or backward in the list of images or by 
 *    using a timer to produce a slideshow.
 *  Author: Dr. Rick Coleman
 *  Date: April 2008
 */
//=============================================================================
public class ImageTransitionsMain extends JFrame
{
	/** Programmer ID */
	public String m_sID = "Dr. Rick Coleman";
	
	/** Main screen width - based on screen width */
	public int m_iScnWidth;
	
	/** Main screen height - based on screen height */
	public int m_iScnHeight;
		
	/** Panel displaying the images */
	public ImagePanel m_ImagePanel;
	
	/** Panel holding the buttons */
	private JPanel m_ButtonPanel;
	
	/** Display Options button */
	private JButton m_DisplayOptionsBtn;
	
	/** Select image directory button */
	private JButton m_SelectImageDirBtn;
	
	/** Switch to previous image button */
	private JButton m_PrevImageBtn;
	
	/** Switch to next image button */
	private JButton m_NextImageBtn;
	
	/** Exit button */
	private JButton m_ExitBtn;
	
	//------------------------------------------
	// Display option variables
	//------------------------------------------
	/** Scale images flag */
	private boolean m_bScaleImages = true;
	
	/** Show image types flag. Default (3) is show both */
	private int m_iShowTypes = 3;
	
	/** Change images manually flag */
	private boolean m_bChangeManually = true;
	
	/** Time delay is using timer to change */
	private int m_iTimeDelay = 5;
	
	//------------------------------------------
	// Miscellaneous variables
	//------------------------------------------
	/** Image directory to show */
	private String m_sImageDir;
	
	/** Vector of image names */
	private Vector<String> m_vImageNames = null;
	
	/** Index of the current image */
	private int m_iCurImageIdx;
	
	/** Image currently displayed */
	private BufferedImage  m_TheImage = null;	

	/** Timer for slideshows */
	private Timer m_SSTimer;

	//---------------------------------------------------
	/** Default constructor */
	//---------------------------------------------------
	public ImageTransitionsMain()
	{
		//------------------------------------------
		// Set all parameters for this JFrame object
		//------------------------------------------
		
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        m_iScnWidth = d.width - 10;
        m_iScnHeight = d.height - 10;
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocation(5, 5);
        this.setTitle("Slide Show Transitions Demonstration");
		this.setSize(m_iScnWidth, m_iScnHeight);
		this.setResizable(false);
		this.getContentPane().setLayout(null); // We'll do our own layouts, thank you.
		this.getContentPane().setBackground(Color.gray); // Set visible area to gray

		// Create the image panel
		m_ImagePanel = new ImagePanel(this);
		this.getContentPane().add(m_ImagePanel); // Add the panel to the window

		// Create the button panel
		m_ButtonPanel = new JPanel();
		m_ButtonPanel.setSize(this.getSize().width, 35);
		m_ButtonPanel.setLocation(0, this.getSize().height-60);
		m_ButtonPanel.setBackground(Color.lightGray); // Set the panel color
		m_ButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		// Use the default Flow Layout manager
		this.getContentPane().add(m_ButtonPanel);
		
		// Create the Display Options button
//		m_DisplayOptionsBtn = new JButton(new ImageIcon("Images/DisplayOptions.jpg"));
		m_DisplayOptionsBtn = new JButton(new ImageIcon(getClass().getResource("DisplayOptions.jpg")));

		m_DisplayOptionsBtn.setSize(20, 20);
		m_DisplayOptionsBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_DisplayOptionsBtn.setToolTipText("Click to set display options.");
		m_DisplayOptionsBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Handle setting the display options
						setDisplayOptions();
					}
				});
		m_ButtonPanel.add(m_DisplayOptionsBtn);	
		
		// Create the select image directory button
//		m_SelectImageDirBtn = new JButton(new ImageIcon("Images/OpenDirectory.jpg"));
		m_SelectImageDirBtn = new JButton(new ImageIcon(getClass().getResource("OpenDirectory.jpg")));
		m_SelectImageDirBtn.setSize(20, 20);
		m_SelectImageDirBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_SelectImageDirBtn.setToolTipText("Click to select directory of images to view.");
		m_SelectImageDirBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Handle getting the image directory to show
						getImageDir();
						if(m_sImageDir != null)
						{
							buildImageList();
							showImage(m_iCurImageIdx); // Show first image
						}
						// Are we doing a slideshow with timer?
						if(!m_bChangeManually)
						{
							doTimerSlideShow();
						}
					}
				});
		m_ButtonPanel.add(m_SelectImageDirBtn);	
		
		// Create the previous image button
//		m_PrevImageBtn = new JButton(new ImageIcon("Images/BackArrow.jpg"));
		m_PrevImageBtn = new JButton(new ImageIcon(getClass().getResource("BackArrow.jpg")));
		m_PrevImageBtn.setSize(20, 20);
		m_PrevImageBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_PrevImageBtn.setToolTipText("View previous image.");
		m_PrevImageBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Show the previous image
						showPreviousImage();
					}
				});
		m_ButtonPanel.add(m_PrevImageBtn);	
		
		// Create the next image button
//		m_NextImageBtn = new JButton(new ImageIcon("Images/NextArrow.jpg"));
		m_NextImageBtn = new JButton(new ImageIcon(getClass().getResource("NextArrow.jpg")));
		m_NextImageBtn.setSize(20, 20);
		m_NextImageBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_NextImageBtn.setToolTipText("View next image.");
		m_NextImageBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Show the next image
						showNextImage();
					}
				});
		m_ButtonPanel.add(m_NextImageBtn);	

		// Create the exit button
//		m_ExitBtn = new JButton(new ImageIcon("Images/Exit.jpg"));
		m_ExitBtn = new JButton(new ImageIcon(getClass().getResource("Exit.jpg")));
		m_ExitBtn.setSize(20, 20);
		m_ExitBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_ExitBtn.setToolTipText("Click to exit the application.");
		m_ExitBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						// Exit the application with status=0 (normal exit)
						System.exit(0);
					}
				});
		m_ButtonPanel.add(m_ExitBtn);	
		
		// Make the window visible
		this.setVisible(true);
	}

	//----------------------------------------------------------------------
	/** Show a dialog box for the user to set the display options */
	//----------------------------------------------------------------------
	private void setDisplayOptions()
	{
		int retVal;
		
		// Create and show a dialog box
		SetDisplayOptionsDlg dlg = new SetDisplayOptionsDlg(this, true);
		dlg.setVisible(true); // show it
		retVal = dlg.getExitStatus();
		if(retVal == 0) // If the user clicked OK get the values
		{
			m_bScaleImages = dlg.getScaleImage();
			m_iShowTypes = dlg.getShowTypes();
			m_bChangeManually = dlg.getChangeManually();
			m_iTimeDelay = dlg.getTimeDelay();
		}
		dlg.dispose(); // Destroy the dialog box
	}
	
	//----------------------------------------------------------------------
	/** Show an open file dialog box in order to get the directory of
	 *   images to display. */
	//----------------------------------------------------------------------
	private void getImageDir()
	{
		int retValue;	// Return value from the JFileChooser
		
	     JFileChooser chooser = new JFileChooser();	// Create the file chooser dialog box
	     chooser.setDialogTitle("Select Image Directory"); // Set dialog title
	     chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Only select dirs
	     chooser.setApproveButtonText("Select");
	     retValue = chooser.showOpenDialog(this); // Show the dialog box
	     if(retValue == JFileChooser.APPROVE_OPTION) // User selected a file
	     {
	    	 // Got a directory so get it's full path
	    	 m_sImageDir = chooser.getSelectedFile().getAbsolutePath();
	     }
//		System.out.println("Dir: " + m_sImageDir);
	}
	
	//----------------------------------------------------------------------
	/** Build the list of images to show */
	//----------------------------------------------------------------------
	private void buildImageList()
	{
        File		chosenDir; // Directory of images
        File[]		fileList;  // Array of files in the directory
        String		fileName;  // Name of a file

        // Create the vector of names
        if(m_vImageNames != null) // If we already have one
        	m_vImageNames.removeAllElements(); // Clean it out
        else                      // If we don't have one
        	m_vImageNames = new Vector(); // Create a new one.
        // Open the directory
        chosenDir = new File(m_sImageDir);
        if(chosenDir != null)	// If we opened it successfully
        {
        	fileList = chosenDir.listFiles(); // Get a list of all files
        	// Go through the list and get the complete path of all image
        	// files (those with .jpg and/or .gif)
        	for(int i=0; i<fileList.length; i++)
        	{
        		fileName = fileList[i].getAbsolutePath(); // Get path name
        		// Is it a .jpg file?
        		if((fileName.endsWith(".jpg")) || (fileName.endsWith(".JPG")))  
        		{
        			// 1 == show only JPG      3 == show JPG and GIF
        			if((m_iShowTypes == 1) || (m_iShowTypes == 3))
        				m_vImageNames.add(fileName); // Add this one to the list
        		}
        		else if((fileName.endsWith(".jpeg")) || (fileName.endsWith(".JPEG")))  
        		{
        			// 1 == show only JPG      3 == show JPG and GIF
        			if((m_iShowTypes == 1) || (m_iShowTypes == 3))
        				m_vImageNames.add(fileName); // Add this one to the list
        		}
        		// Is it a .gif file?
        		else if((fileName.endsWith(".gif")) || (fileName.endsWith(".GIF"))) // Is it a .gif file?
        		{
        			// 2 == show only GIF      3 == show JPG and GIF
        			if((m_iShowTypes == 2) || (m_iShowTypes == 3))
        				m_vImageNames.add(fileName); // Add this one to the list
        		}
        	} // end for loop
        	m_iCurImageIdx = 0; // Initialize the current image index 
        } // end if(chosenDir != null)
        // You can add the loop below just to check.  Then comment it out
        
/*        for(int i=0; i< m_vImageNames.size(); i++)
        {
        	fileName = (String)(m_vImageNames.elementAt(i));
        	System.out.println(fileName);
        }
*/        
	}
	
	//----------------------------------------------------------------------
	/** Show the image at index. */
	//----------------------------------------------------------------------
	private void showImage(int idx)
	{
        File		imageFile; // the jpg or gif file
		// Make sure we have an image file
        if((m_vImageNames.size() < 0) || (idx >= m_vImageNames.size()))
        {
			JOptionPane.showMessageDialog(this, 
					"Error: Unable to display image " + idx + ". Does not exist.", 
					"Error Loading Image", JOptionPane.ERROR_MESSAGE);
			return;
        }
        imageFile = new File((String)(m_vImageNames.elementAt(idx)));
		if(!imageFile.exists()) // If we failed to opened it
		{
			JOptionPane.showMessageDialog(this, 
					"Error: Unable to load " + (String)(m_vImageNames.elementAt(idx)), 
					"Error Loading Image", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// Load the image
        // Use ImageIO and pass a BufferedImage.TYPE_INT_RGB to ImagePanel
        if(m_TheImage != null)
        	m_TheImage = null; // Clear the previous image
        try
        {
        	m_TheImage = ImageIO.read(imageFile);
        	if (m_TheImage.getType() != BufferedImage.TYPE_INT_RGB) 
        	{
                BufferedImage bi2 =
                    new BufferedImage(m_TheImage.getWidth(), m_TheImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics big = bi2.getGraphics();
                big.drawImage(m_TheImage, 0, 0, null);
                m_TheImage = bi2;
        	}
        }
        catch (IOException e)
        {
			JOptionPane.showMessageDialog(this, 
					"Error: Unable to load " + (String)(m_vImageNames.elementAt(idx)), 
					"Error Loading Image", JOptionPane.ERROR_MESSAGE);
			return;
        }
        m_ImagePanel.setImage(m_TheImage);
	}
	
	//----------------------------------------------------------------------
	/** Show the previous image. */
	//----------------------------------------------------------------------
	private void showPreviousImage()
	{
		if(m_iCurImageIdx > 0)
		{
			m_iCurImageIdx--; // Decrement to previous image
			showImage(m_iCurImageIdx); // Show it
		}
	}
	
	//----------------------------------------------------------------------
	/** Show the next image. */
	//----------------------------------------------------------------------
	private void showNextImage()
	{
		if(m_iCurImageIdx < (m_vImageNames.size() - 1))
		{
			m_iCurImageIdx++; // Increment to next image
			showImage(m_iCurImageIdx); // Show it
		}
	}

	//----------------------------------------------------------------------
	/** Show the next image. */
	//----------------------------------------------------------------------
	private void doTimerSlideShow()
	{
		// Disable the previous and next buttons while the slideshow runs
		m_PrevImageBtn.setEnabled(false);
		m_NextImageBtn.setEnabled(false);
		
		// Create a javax.swing.timer
		m_SSTimer = new Timer(m_iTimeDelay * 1000,
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Show the next image
					if(m_iCurImageIdx < m_vImageNames.size() - 1)
					{
						showNextImage();
					}
					else
					{
						m_SSTimer.stop();
						// Enable the previous and next buttons again
						m_PrevImageBtn.setEnabled(true);
						m_NextImageBtn.setEnabled(true);
					}
				}
			});
		m_SSTimer.setRepeats(true); // Repeat till we kill it
		m_SSTimer.start();  // Start the timer
	}
	
	//----------------------------------------------------------------------
	/** Main function for this demonstration
	 * @param args - Array of strings from the command line
	 */
	//----------------------------------------------------------------------
	public static void main(String[] args) 
	{
		// When you start this application this function gets called by the
		//  operating system.  Main just creates an ImageViewer object.
		//  To follow the execution trail from here go to the ImageViewer
		//  constructor.
		ImageTransitionsMain IV = new ImageTransitionsMain();
	}

}
