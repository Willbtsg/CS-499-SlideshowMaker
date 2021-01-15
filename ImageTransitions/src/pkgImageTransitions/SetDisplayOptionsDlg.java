package pkgImageTransitions;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

//=============================================================================
/** Class: SetDisplayOptionsDlg
 *  Purpose: This class implements a modal dialog box to allow the user to
 *     set all the display options for the ImageViewer application.
 *  Author: Dr. Rick Coleman
 *  Date: April 2008
 */
//=============================================================================
public class SetDisplayOptionsDlg extends JDialog
{
	/** OK button */
	private JButton m_OKBtn;
	
	/** Cancel button */
	private JButton m_CancelBtn;

	/** Exit status */
	private int m_iExitStatus;
	
	/** Title font used for large labels */
	public static final Font SysTitleFontB = new Font("SansSerif", Font.BOLD, 14);
	
	/** Label font used for small labels */
	public static final Font SysSmallLabelFontB = new Font("SansSerif", Font.BOLD, 12);

	/** Scale image checkbox */
	private JCheckBox m_ScaleImageCKB;
	
	/** Show JPG images only radio button */
	private JRadioButton m_ShowJPGOnlyRB;
	
	/** Show GIF images only radio button */
	private JRadioButton m_ShowGIFOnlyRB;
	
	/** Show Both JPG and GIF images radio button */
	private JRadioButton m_ShowBothRB;
	
	/** Button group to hold the Image Types radio button group */
	private ButtonGroup m_ImageTypeBG;
	
	/** Change images manually radio button */
	private JRadioButton m_ChangeManuallyRB;
	
	/** Change images using timer radio button */
	private JRadioButton m_ChangeByTimerRB;
	
	/** Display time button group */
	private ButtonGroup m_DisplayTimeBG;
	
	/** Text field for the seconds delay if display by timer is used */
	private JTextField m_TimeDelayTF;
	
	//------------------------------------------------
	/** Default constructor */
	//------------------------------------------------
	public SetDisplayOptionsDlg(Frame owner, boolean modal)
	{
		super(owner, modal); // Call the super class constructor
		// Create the dialog frame
		this.setSize(340, 240);
		// Set the location so that the dialog box pops up centered on the owner
		this.setLocation((owner.getWidth() - this.getWidth()) / 2 , 
				         (owner.getHeight() - this.getHeight())/ 2); 
		this.setTitle("Set View Options");
		this.setLayout(null); // We'll do our own layouts, thank you.
		this.getContentPane().setBackground(Color.lightGray); // Set visible area to light gray
		// Don't let user close dialog with X-button
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		// Add the label above the check box
		JLabel lbl = new JLabel("Image Size");
		lbl.setSize(100, 20);
		lbl.setFont(SysTitleFontB);
		lbl.setLocation(80,5);
		this.add(lbl);
		
		// Create the check box
		m_ScaleImageCKB = new JCheckBox("Scale image to fit screen");
		m_ScaleImageCKB.setSize(200, 20);
		m_ScaleImageCKB.setLocation(70, 25);
		m_ScaleImageCKB.setSelected(true); 
		m_ScaleImageCKB.setBackground(Color.lightGray);
		this.add(m_ScaleImageCKB);
		
		// Add the label above the radio buttons
		lbl = new JLabel("Show Image Types");  // Reuse the lbl reference defined above
		lbl.setSize(150, 20);
		lbl.setFont(SysTitleFontB);
		lbl.setLocation(20,50);
		this.add(lbl);
		
		// Create the JPG images only radio button
		m_ShowJPGOnlyRB = new JRadioButton("JPG images only");
		m_ShowJPGOnlyRB.setSize(120, 20);
		m_ShowJPGOnlyRB.setLocation(20, 70);
		m_ShowJPGOnlyRB.setSelected(false);
		m_ShowJPGOnlyRB.setBackground(Color.lightGray);
		this.add(m_ShowJPGOnlyRB);
		
		// Create the GIF images only radio button
		m_ShowGIFOnlyRB = new JRadioButton("GIF images only");
		m_ShowGIFOnlyRB.setSize(120, 20);
		m_ShowGIFOnlyRB.setLocation(20, 90);
		m_ShowGIFOnlyRB.setSelected(false);
		m_ShowGIFOnlyRB.setBackground(Color.lightGray);
		this.add(m_ShowGIFOnlyRB);
		
		// Create the show both types radio button
		m_ShowBothRB = new JRadioButton("JPG and GIF");
		m_ShowBothRB.setSize(120, 20);
		m_ShowBothRB.setLocation(20, 110);
		m_ShowBothRB.setSelected(true);
		m_ShowBothRB.setBackground(Color.lightGray);
		this.add(m_ShowBothRB);
		
		// Create the button group
		m_ImageTypeBG = new ButtonGroup();
		m_ImageTypeBG.add(m_ShowJPGOnlyRB); // Add the JPG only radio button
		m_ImageTypeBG.add(m_ShowGIFOnlyRB); // Add the GIF only radio button
		m_ImageTypeBG.add(m_ShowBothRB);    // Add the show both radio button
		
		// Add the label above the radio buttons
		lbl = new JLabel("Display Time");  // Reuse the lbl reference defined above
		lbl.setSize(150, 20);
		lbl.setFont(SysTitleFontB);
		lbl.setLocation(190,50);
		this.add(lbl);
		
		// Create the change manually radio button
		m_ChangeManuallyRB = new JRadioButton("Change manually");
		m_ChangeManuallyRB.setSize(150, 20);
		m_ChangeManuallyRB.setLocation(170, 70);
		m_ChangeManuallyRB.setSelected(true);
		m_ChangeManuallyRB.setBackground(Color.lightGray);
		this.add(m_ChangeManuallyRB);
		
		// Create the change by timer radio button
		m_ChangeByTimerRB = new JRadioButton("Use set time interval");
		m_ChangeByTimerRB.setSize(150, 20);
		m_ChangeByTimerRB.setLocation(170, 90);
		m_ChangeByTimerRB.setSelected(false);
		m_ChangeByTimerRB.setBackground(Color.lightGray);
		this.add(m_ChangeByTimerRB);
		
		// Create the button group
		m_DisplayTimeBG = new ButtonGroup();
		m_DisplayTimeBG.add(m_ChangeManuallyRB); // Add the change manually radio button
		m_DisplayTimeBG.add(m_ChangeByTimerRB); // Add the change by timer radio button

		// Create the time interval text field
		m_TimeDelayTF = new JTextField();
		m_TimeDelayTF.setSize(50, 20);
		m_TimeDelayTF.setLocation(170, 110);
		m_TimeDelayTF.setEditable(true);
		m_TimeDelayTF.setText("5"); // Default to 5 sec delay
		m_TimeDelayTF.setHorizontalAlignment(JTextField.RIGHT); // Align right
		this.add(m_TimeDelayTF);
		
		// Add the label next to the text field
		lbl = new JLabel("sec. delay");  // Reuse the lbl reference defined above
		lbl.setSize(150, 20);
		lbl.setFont(SysSmallLabelFontB);
		lbl.setLocation(230,110);
		this.add(lbl);
		
		// Create the OK button
		m_OKBtn = new JButton("OK");
		m_OKBtn.setSize(50, 20);
		m_OKBtn.setLocation(75, 170);
		m_OKBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_OKBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Handle OK
						m_iExitStatus = 0; // Normal exit
						setVisible(false);
					}
				});
		this.add(m_OKBtn);	
		
		// Create the Cancel button
		m_CancelBtn = new JButton("Cancel");
		m_CancelBtn.setSize(50, 20);
		m_CancelBtn.setLocation(200, 170);
		m_CancelBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_CancelBtn.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//	Handle cancel
						m_iExitStatus = 1;
						setVisible(false);
					}
				});
		this.add(m_CancelBtn);	
		
	}
	
	//------------------------------------------------
	/** Get the exit status */
	//------------------------------------------------
	public int getExitStatus()
	{
		return m_iExitStatus;
	}
	
	//------------------------------------------------
	/** Get the scale image setting */
	//------------------------------------------------
	public boolean getScaleImage()
	{
		return m_ScaleImageCKB.isSelected();
	}

	//------------------------------------------------
	/** Get the show types setting */
	//------------------------------------------------
	public int getShowTypes()
	{
		// Return which radio button is selected
		if(m_ShowJPGOnlyRB.isSelected()) return 1;
		else if(m_ShowGIFOnlyRB.isSelected()) return 2;
		else return 3;
	}
	
	//------------------------------------------------
	/** Get the change images by setting */
	//------------------------------------------------
	public boolean getChangeManually()
	{
		return this.m_ChangeManuallyRB.isSelected();
	}

	//------------------------------------------------
	/** Get the time delay setting */
	//------------------------------------------------
	public int getTimeDelay()
	{
		try
		{
			int td = Integer.parseInt(m_TimeDelayTF.getText());
			return td;
		}
		catch(Exception e) // Oops, not a valid integer string
		{
			String message = "Illegal number format for time delay. Defaulting to 5 sec.";
			JOptionPane.showMessageDialog(null, 
					(Object)message, "Number Format Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return 5; // Return the default of 5 sec
	}

}
