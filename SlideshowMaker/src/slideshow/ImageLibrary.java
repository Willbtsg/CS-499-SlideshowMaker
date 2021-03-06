package slideshow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

// Sources used:
// https://stackoverflow.com/a/11301085
// https://stackoverflow.com/a/18963970
// https://www.tutorialspoint.com/how-can-we-implement-a-scrollable-jpanel-in-java

/**
 * Class name: ImageLibrary
 *
 * This class allows the user to view and select the image files they want in their Slideshow
 */

public class ImageLibrary extends JPanel {

    /**
     * ImageLibrary instance- instance of ImageLibrary for Singleton
     * Timeline associatedTimeline- reference to Timeline object image data will be added to
     */
    private static ImageLibrary instance;
    private Timeline associatedTimeline;

    // Accesses the slideshow directory
    private File dir;

    // An array of the image types our app supports
    private String[] extensions = new String[]{ "jpg", "jpeg", "jfif", "png" };

    /////////////
    // METHODS //
    /////////////

    /**
     * Constructor for ImageLibrary. If a directory has been selected, this constructor reads in the image data and creates
     * JPanels to display them in.
     *
     * @param timeline- Timeline object to send data to when adding images to Slideshow
     * @param directory- directory to use when retrieving image data
     */
    private ImageLibrary(Timeline timeline, String directory)
    {
        if (directory != null)
        {
            dir = new File(directory);

            // Filter to parse the directory for compatible image files
            FilenameFilter imageFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    for (String ext : extensions) {
                        if (name.endsWith("." + ext))
                            return true;
                    }
                    return false;
                }
            };

            associatedTimeline = timeline; //set reference to destination Timeline
            setLayout(new GridBagLayout()); //set dynamic size to keep items in panel till size determined
            GridBagConstraints  c = new GridBagConstraints();
            // Set initial parameters for grid bag constraints
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0.5;
            c.weighty = 0.5;

            int itemCounter = 0; //keeps track of how many items are in the library

            if (dir.isDirectory()) //if directory is valid
            {
                for (File file : dir.listFiles(imageFilter)) //for every valid image in the directory
                {

                    JPanel libraryItem = new JPanel(); //create a new JPanel to display image data
                    libraryItem.setLayout(new BorderLayout());
                    JPanel buttonAndTitle = new JPanel();
                    buttonAndTitle.setLayout(new BorderLayout());

                    JLabel imgTitle = new JLabel(file.getName(), SwingConstants.CENTER); //set image name in Timeline
                    imgTitle.setBorder(new EmptyBorder(10,0,10,0));

                    buttonAndTitle.add(imgTitle, BorderLayout.NORTH);
                    JButton addButton = new JButton("Add");

                    addButton.addActionListener(new ActionListener() //add image to the Timeline
                    {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            associatedTimeline.addSlide(dir + "\\" + file.getName());
                        }
                    });

                    buttonAndTitle.add(addButton, BorderLayout.SOUTH);
                    libraryItem.add(buttonAndTitle, BorderLayout.SOUTH);

                    JLabel thumbnail = new JLabel("", SwingConstants.CENTER);
                    ImageIcon img = new ImageIcon(dir + "\\" + file.getName());
                    double proportion = 200.0/img.getIconWidth();
                    double db_newHeight = proportion*img.getIconHeight();
                    int newHeight = (int)Math.round(db_newHeight);
                    Image imgIcon = img.getImage().getScaledInstance(200,newHeight,Image.SCALE_REPLICATE);
                    img = new ImageIcon(imgIcon);
                    thumbnail.setIcon(img);
                    libraryItem.add(thumbnail, BorderLayout.CENTER); //after loading and scaling the image, add it to the info panel

                    libraryItem.setPreferredSize(new Dimension(200,300));
                    add(libraryItem, c); //add new image info panel to the library

                    // Update grid x coordinate
                    c.gridx++;
                    // If 4 elements put in 1 row
                    if(++itemCounter%4 == 0) {
                        // Increment y and set x back to 0
                        c.gridy++;
                        c.gridx = 0;
                    }
                }
                // If panel isn't at minimum capacity
                if (itemCounter < 12) {
                	// Loop through till 12 panels have been added
                	while(itemCounter < 12) {
	                	// Create empty panel
	                	JPanel spaceFill = new JPanel();
	                	JLabel emptyLB = new JLabel("");
	                	spaceFill.add(emptyLB);
	                	add(spaceFill, c);
	                	
	                    // Update grid x coordinate
	                    c.gridx++;
	                    // If 4 elements put in 1 row
	                    if(++itemCounter%4 == 0) {
	                        // Increment y and set x back to 0
	                        c.gridy++;
	                        c.gridx = 0;
	                    }
                	}
                }
            }
        }

    }

    /**
     * This function returns the instance of ImageLibrary. If no instance exists, then one is created.
     *
     * @param timeline- reference to Timeline object to add images to
     * @return instance- pointer to instance of ImageLibrary to be used
     */
    public static ImageLibrary getInstance(Timeline timeline, String directory)
    {
        if (instance == null) {
            instance = new ImageLibrary(timeline, directory);
        }
        return instance;
    }

    /**
     * Resets library contents whenever a new directory is opened
     * @param timeline- reference to Timeline object to add sounds to
     * @param directory- reference to directory to pull image files from
     *
     * @return new instance of ImageLibrary
     */
    public static ImageLibrary resetLibrary(Timeline timeline, String directory)
    {
        instance = null;
        ImageLibrary newLib = getInstance(timeline, directory);
        return newLib;
    }
}
