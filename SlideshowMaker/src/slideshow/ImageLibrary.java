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
    // TODO: This directory will need to be user-inputted
    private File dir = new File("images");

    // An array of the image types our app supports
    // TODO: Accept only jpg and jpeg
    private String[] extensions = new String[]{ "jpg", "jpeg", "jfif", "png" };

    // Filter to parse the directory for compatible image files
    private FilenameFilter imageFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            for (String ext : extensions) {
                if (name.endsWith("." + ext))
                    return true;
            }
            return false;
        }
    };

    /////////////
    // METHODS //
    /////////////

    private ImageLibrary(Timeline timeline)
    {
        associatedTimeline = timeline; //set reference to destination Timeline
        // Setting the GridLayout to (0,n) causes a new row
        // to be created after every n images
        GridLayout grid = new GridLayout(0,4);
        setLayout(grid);
        if (dir.isDirectory()) //if directory is valid
        {
            for (File file : dir.listFiles(imageFilter)) //for every valid image in the directory
            {
                JPanel libraryItem = new JPanel(); //create a new JPanel to display image data
                libraryItem.setLayout(new BorderLayout());
                JPanel buttonAndTitle = new JPanel();
                buttonAndTitle.setLayout(new BorderLayout());

                JLabel imgTitle = new JLabel(file.getName(), SwingConstants.CENTER);
                imgTitle.setBorder(new EmptyBorder(10,0,10,0));
                buttonAndTitle.add(imgTitle, BorderLayout.NORTH);
                JButton addButton = new JButton("Add");

                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        associatedTimeline.addSlide(dir + "\\" + file.getName()); //add image to the Timeline
                        associatedTimeline.revalidate(); //update Timeline GUI
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
                add(libraryItem); //add new image info panel to the library
            }
            // THIS IS JUST A TESTING LOOP FOR ADDING A BUNCH OF PICS TO THE LIBRARY
            for (int i = 0; i < 30; i++)
            {
                JPanel libraryItem = new JPanel();
                libraryItem.setLayout(new BorderLayout());
                JPanel buttonAndTitle = new JPanel();
                buttonAndTitle.setLayout(new BorderLayout());

                JLabel imgTitle = new JLabel("download (1).png", SwingConstants.CENTER);
                buttonAndTitle.add(imgTitle, BorderLayout.NORTH);
                JButton addButton = new JButton("Add");
                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        associatedTimeline.addSlide("images\\download (1).png");
                        associatedTimeline.revalidate();
                    }
                });
                buttonAndTitle.add(addButton, BorderLayout.SOUTH);
                libraryItem.add(buttonAndTitle, BorderLayout.SOUTH);

                JLabel thumbnail = new JLabel("", SwingConstants.CENTER);
                ImageIcon img = new ImageIcon("images\\download (1).png");
                double proportion = 200.0/img.getIconWidth();
                double db_newHeight = proportion*img.getIconHeight();
                int newHeight = (int)Math.round(db_newHeight);
                Image imgIcon = img.getImage().getScaledInstance(200,newHeight,Image.SCALE_REPLICATE);
                img = new ImageIcon(imgIcon);
                thumbnail.setIcon(img);
                libraryItem.add(thumbnail, BorderLayout.CENTER);

                libraryItem.setPreferredSize(new Dimension(200,200));
                add(libraryItem);
            }
        }
    }

    /**
     * This function returns the instance of ImageLibrary. If no instance exists, then one is created.
     *
     * @param timeline- reference to Timeline object to add images to
     * @return instance- pointer to instance of ImageLibrary to be used
     */
    public static ImageLibrary getInstance(Timeline timeline)
    {
        if (instance == null) {
            instance = new ImageLibrary(timeline);
        }
        return instance;
    }

}
