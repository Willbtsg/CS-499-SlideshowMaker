package slideshow;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class name: DBWizard
 *
 * This class is used to write to and read from the slideshow layout file. The files being used are .json.
 * This class is implemented as a Singleton.
 */

public class SlideshowManager {

    /**
     * static DBWizard instance- contains instance of DBWizard for Singleton implementation
     * String DBName- contains complete filepath for slideshow layout file
     * String workingDir- contains filepath to chosen slideshow directory
     */
    private static SlideshowManager instance;
    private static String DBNAME;
    private static String workingDir;

    /**
     * Writes the list of slides out to the master database.
     * This function will be moved to the editor once it has been written
     * @param obj- JSONObject containing data to be written to the database.
     */
    public static void writeDB(JSONObject obj)
    {
        String outputMessage;
        String fileName = JOptionPane.showInputDialog(null, "Enter a filename for your slideshow layout file:", "File Select", JOptionPane.QUESTION_MESSAGE);

        if (fileName != null)
        {
            if (!fileName.equals("")) //if user typed in a filename...
            {
                DBNAME = workingDir + "\\" + fileName + ".json";

                try (FileWriter file = new FileWriter(DBNAME)) //try to write the json data to that file
                {
                    file.write(obj.toString());
                    file.flush();
                    outputMessage = "<html><div style='text-align: center;'>Your slideshow was saved!<br>" +
                            "If you want to make another slideshow, whether with this directory<br> or a different one, " +
                            "just click OK and stick around.<br>Otherwise, once you've clicked OK, feel free to close the app - all your work is saved!";
                    JOptionPane.showMessageDialog(null, outputMessage, "Save Successful!", JOptionPane.INFORMATION_MESSAGE);

                } catch (IOException e) //if the file's name is invalid, let the user know
                {
                    e.printStackTrace();
                    outputMessage  ="<html><div style='text-align: center;'>We're sorry, we were unable to save your slideshow.<br>" +
                            "Your filename was either too long or contained one or more of the following characters: '\\ / : * \" &lt > |'<br>" +
                            "Please pick a new name and try again. </div></html>";
                    JOptionPane.showMessageDialog(null, outputMessage, "Save Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
            else //if the user hit enter and left the text field blank, express disappointment
            {
                DBNAME = null;
                outputMessage  ="<html><div style='text-align: center;'>We're " +
                        "incredibly disappointed right now.<br>You failed to specify a file name, so we can't save the incredible" +
                        " beauty that is your slideshow.<br>Click OK and please, try again so that the world can bask in the glory of your creation.</div></html>";
                JOptionPane.showMessageDialog(null, outputMessage, "Save Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Creates a Slideshow object using Slide and sound info from a json file
     * @return A Slideshow object containing all Slide information
     *
     */
    public static Slideshow getSlideshow()
    {
        // TODO: This will need to be user-set
        DBNAME = "images/test.json";

        JSONParser parser = new JSONParser();
        ArrayList<Slide> slideList = new ArrayList<Slide>();
        ArrayList<String> soundList = new ArrayList();
        Slideshow slideshow = new Slideshow();

        try {
            Object obj = parser.parse(new FileReader(DBNAME));
            //
            //Read JSON file
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray tempSlides = (JSONArray) jsonObject.get("SlideList");
            JSONArray tempSounds = (JSONArray) jsonObject.get("SoundList");

            for (Object j : tempSlides) {
                slideList.add(new Slide((JSONObject) j)); //convert JSON data into Slide objects
            }

            slideshow.setSlideList(slideList); //set Slideshow's m_SlideList

            if (tempSounds != null)
            {
                for (Object j : tempSounds) {
                    JSONObject tempJ = (JSONObject) j;
                    soundList.add((String) tempJ.get("name")); //convert JSON data to String of sound's filename
                }
            }

            slideshow.setSoundList(soundList); //set Slideshow's m_SoundList
            slideshow.setSoundLength((String) jsonObject.get("AudioLength"));

            slideshow.setAutomated((Boolean) jsonObject.get("Automated"));

            if (slideshow.getAutomated()) //if the Slideshow is automated (i.e. has a set runtime)...
            {
                slideshow.setSlideLength((String) jsonObject.get("SlideshowLength")); //...calculate the total runtime
            }

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return slideshow;
    }

    /**
     * This function pops up a file explorer which the user will use to select a directory to work with.
     *
     * @return Returns the user-selected file path as a string
     */
    public static String getDirectory()
    {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int open = fileChooser.showOpenDialog(null);

        if (open == JFileChooser.APPROVE_OPTION)
        {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            workingDir = filePath;
            return filePath;
        }
        else
            return null;
    }

    /**
     * This function returns the instance of DBWizard. If no instance exists, then one is created.
     *
     * @return instance- pointer to instance of DBWizard to be used
     */
    public static SlideshowManager getInstance()
    {
        if (instance == null) {
            instance = new SlideshowManager();
        }
        return instance;
    }

}
