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

    public static ImageLibrary getInstance()
    {
        if (instance == null) {
            instance = new ImageLibrary();
        }
        return instance;
    }

    public ImageLibrary()
    {
        setLayout(null);
        setBackground(Color.gray);
        setBounds(0,70,1100,693);
        setBorder(BorderFactory.createLineBorder(Color.black, 3));
        // Setting the GridLayout to (0,n) causes a new row
        // to be created after every n images
        GridLayout grid = new GridLayout(0,2);
        setLayout(grid);
        // TODO: Make ImageLibrary scrollable
        // TODO: Provide a way to interact with the images
        //       (able to drag them to timeline or something like that)
        // TODO: Scale the images (stretch goal?)
        // TODO: Get the names of the files to go across the bottom of the image
        //       (or get rid of the names entirely)
        if (dir.isDirectory())
        {
            for (File file : dir.listFiles(imageFilter))
            {
                JLabel thumbnail = new JLabel(file.getName());
                ImageIcon img = new ImageIcon(dir + "\\" + file.getName());
                thumbnail.setIcon(img);
                add(thumbnail);
            }
        }
    }

}
