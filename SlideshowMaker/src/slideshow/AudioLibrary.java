package slideshow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Class name: AudioLibrary
 *
 * This class allows the user to view and select the sound files they want in their Slideshow
 */

public class AudioLibrary extends JPanel {

    /**
     * AudioLibrary instance- instance of AudioLibrary for Singleton
     * Timeline associatedTimeline- reference to Timeline object sound data will be added to
     */
    private static AudioLibrary instance;
    private Timeline associatedTimeline;

    // Accesses the slideshow directory
    // TODO: This directory will need to be user-inputted
    private File dir = new File("images");

    // An array of the image types our app supports
    private String[] extensions = new String[]{ "wav", "aif", "aiff" };

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

    private AudioLibrary(Timeline timeline)
    {
        associatedTimeline = timeline; //set reference to destination Timeline
        // Setting the GridLayout to (0,n) causes a new row
        // to be created after every n images
        GridLayout grid = new GridLayout(0,4);
        setLayout(grid);
        ImageIcon audioIcon = new ImageIcon("images\\audioicon.png");
        if (dir.isDirectory()) //if directory is valid
        {
            for (File file : dir.listFiles(audioFilter)) //for every wav, aif, or aiff file in the directory...
            {
                JPanel libraryItem = new JPanel(); //create a new JPanel to display sound info
                libraryItem.setLayout(new BorderLayout());
                JPanel buttonAndTitle = new JPanel();
                buttonAndTitle.setLayout(new BorderLayout());

                JLabel audioTitle = new JLabel(file.getName(), SwingConstants.CENTER);
                buttonAndTitle.add(audioTitle, BorderLayout.NORTH);
                JButton addButton = new JButton("Add"); //add button for adding sound to Timeline

                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        associatedTimeline.addSound(file.getName()); //add sound to the Timeline
                        associatedTimeline.revalidate(); //update the Timeline GUI
                    }
                });

                buttonAndTitle.add(addButton, BorderLayout.SOUTH);
                libraryItem.add(buttonAndTitle, BorderLayout.SOUTH);

                JLabel icon = new JLabel("", SwingConstants.CENTER);
                icon.setIcon(audioIcon);
                libraryItem.add(icon, BorderLayout.CENTER); //set audio icon

                libraryItem.setPreferredSize(new Dimension(200,200));
                add(libraryItem); //add JPanel of sound data to library
            }
        }
    }

    /**
     * This function returns the instance of AudioLibrary. If no instance exists, then one is created.
     *
     * @param timeline- reference to Timeline object to add sounds to
     * @return instance- pointer to instance of AudioLibrary to be used
     */
    public static AudioLibrary getInstance(Timeline timeline)
    {
        if (instance == null) {
            instance = new AudioLibrary(timeline);
        }
        return instance;
    }

}
