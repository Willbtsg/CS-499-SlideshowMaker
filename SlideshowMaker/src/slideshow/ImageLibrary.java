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

    private Timeline associatedTimeline;
    private SlideshowEditor associatedEditor;

    /////////////
    // METHODS //
    /////////////

    public ImageLibrary(SlideshowEditor editor, Timeline timeline)
    {
        associatedEditor = editor;
        associatedTimeline = timeline;
        // Setting the GridLayout to (0,n) causes a new row
        // to be created after every n images
        GridLayout grid = new GridLayout(0,4);
        setLayout(grid);
        if (dir.isDirectory())
        {
            for (File file : dir.listFiles(imageFilter))
            {
                JPanel libraryItem = new JPanel();
                libraryItem.setLayout(new BorderLayout());
                JPanel buttonAndTitle = new JPanel();
                buttonAndTitle.setLayout(new BorderLayout());

                JLabel imgTitle = new JLabel(file.getName(), SwingConstants.CENTER);
                buttonAndTitle.add(imgTitle, BorderLayout.NORTH);
                JButton addButton = new JButton("Add");
                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        associatedTimeline.addSlide(dir + "\\" + file.getName());
                        associatedEditor.revalidate();
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
                libraryItem.add(thumbnail, BorderLayout.CENTER);

                libraryItem.setPreferredSize(new Dimension(200,300));
                add(libraryItem);
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
                        associatedEditor.revalidate();
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

    public static ImageLibrary getInstance(SlideshowEditor GUI, Timeline timeline)
    {
        if (instance == null) {
            instance = new ImageLibrary(GUI, timeline);
        }
        return instance;
    }

}
