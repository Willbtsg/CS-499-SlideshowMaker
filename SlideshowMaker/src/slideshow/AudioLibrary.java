package slideshow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

public class AudioLibrary extends JPanel {

    ////////////
    // FIELDS //
    ////////////

    private static AudioLibrary instance;

    // Accesses the slideshow directory
    // TODO: This directory will need to be user-inputted
    private File dir = new File("images");

    // An array of the image types our app supports
    private String[] extensions = new String[]{
            "wav", "aif", "aiff"
    };

    // Filter to parse the directory for compatible image files
    private FilenameFilter audioFilter = new FilenameFilter() {
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

    public static AudioLibrary getInstance()
    {
        if (instance == null) {
            instance = new AudioLibrary();
        }
        return instance;
    }

    public AudioLibrary()
    {
        // Setting the GridLayout to (0,n) causes a new row
        // to be created after every n images
        GridLayout grid = new GridLayout(0,4);
        setLayout(grid);
        ImageIcon audioIcon = new ImageIcon("images\\audioicon.png");
        if (dir.isDirectory())
        {
            for (File file : dir.listFiles(audioFilter))
            {
                JPanel libraryItem = new JPanel();
                libraryItem.setLayout(new BorderLayout());
                JPanel buttonAndTitle = new JPanel();
                buttonAndTitle.setLayout(new BorderLayout());

                JLabel audioTitle = new JLabel(file.getName(), SwingConstants.CENTER);
                buttonAndTitle.add(audioTitle, BorderLayout.NORTH);
                JButton addButton = new JButton("Add");
                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Add test success!");
                    }
                });
                buttonAndTitle.add(addButton, BorderLayout.SOUTH);
                libraryItem.add(buttonAndTitle, BorderLayout.SOUTH);

                JLabel icon = new JLabel("", SwingConstants.CENTER);
                icon.setIcon(audioIcon);
                libraryItem.add(icon, BorderLayout.CENTER);

                libraryItem.setPreferredSize(new Dimension(200,200));
                add(libraryItem);
            }
        }
    }

}
