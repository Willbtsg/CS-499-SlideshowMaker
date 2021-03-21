package slideshow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;

// Sources used:
// https://stackoverflow.com/a/11301085
// https://stackoverflow.com/a/18963970
// https://www.tutorialspoint.com/how-can-we-implement-a-scrollable-jpanel-in-java

public class ImageLibrary extends JPanel {

    ////////////
    // FIELDS //
    ////////////

    private static ImageLibrary instance;

    // Accesses the slideshow directory
    // TODO: This directory will need to be user-inputted
    private File dir = new File("images");

    // An array of the image types our app supports
    private String[] extensions = new String[]{
            "jpg", "jpeg", "jfif", "png"
    };

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

    public ImageLibrary()
    {
        // Setting the GridLayout to (0,n) causes a new row
        // to be created after every n images
        GridLayout grid = new GridLayout(0,3);
        setLayout(grid);
        // TODO: Provide a way to interact with the images
        //       (able to drag them to timeline or something like that)
        // TODO: Change the position of the file names?
        if (dir.isDirectory())
        {
            for (File file : dir.listFiles(imageFilter))
            {
                JLabel thumbnail = new JLabel(file.getName());
                ImageIcon img = new ImageIcon(dir + "\\" + file.getName());
                double proportion = 200.0/img.getIconWidth();
                double db_newHeight = proportion*img.getIconHeight();
                int newHeight = (int)Math.round(db_newHeight);
                Image imgIcon = img.getImage().getScaledInstance(200,newHeight,Image.SCALE_REPLICATE);
                img = new ImageIcon(imgIcon);
                thumbnail.setIcon(img);
                add(thumbnail);
            }
            // THIS IS JUST A TESTING LOOP FOR ADDING A BUNCH OF PICS TO THE LIBRARY
            for (int i = 0; i < 40; i++)
            {
                JLabel thumbnail = new JLabel("download (1).png");
                ImageIcon img = new ImageIcon("images\\download (1).png");
                double proportion = 200.0/img.getIconWidth();
                double db_newHeight = proportion*img.getIconHeight();
                int newHeight = (int)Math.round(db_newHeight);
                Image imgIcon = img.getImage().getScaledInstance(200,newHeight,Image.SCALE_REPLICATE);
                img = new ImageIcon(imgIcon);
                thumbnail.setIcon(img);
                add(thumbnail);
            }
        }
    }

    public static ImageLibrary getInstance()
    {
        if (instance == null) {
            instance = new ImageLibrary();
        }
        return instance;
    }

}
