package slideshow;

import javax.swing.*;
import java.awt.*;
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
        GridLayout grid = new GridLayout(0,3);
        setLayout(grid);
        ImageIcon audioIcon = new ImageIcon("images\\audioicon.png");
        if (dir.isDirectory())
        {
            for (File file : dir.listFiles(audioFilter))
            {
                JLabel thumbnail = new JLabel(file.getName());
                thumbnail.setIcon(audioIcon);
                add(thumbnail);
            }
        }
    }

}
