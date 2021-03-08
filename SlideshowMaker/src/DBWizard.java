import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

public class DBWizard {

    /**
     * static DBWizard instance- contains instance of DBWizard for Singleton implementation
     * String DBName- contains filepath of slideshow layout file
     */

    private static DBWizard instance;
    private static String DBNAME = "images/test.json";

    /**
     * Writes the list of slides out to the master database.
     * This function will be moved to the editor once it has been written
     * @param obj- JSONObject containing data to be written to the database.
     */
    public static void writeDB(JSONObject obj)
    {

        try(FileWriter file =  new FileWriter(DBNAME))
        {
            file.write(obj.toString());
            file.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Reads the Master list of slides from the database
     * @return A Slideshow object containing all Slide information
     *
     */
    public static Slideshow getSlideshow()
    {
        JSONParser parser = new JSONParser();
        ArrayList<Slide> theList = new ArrayList<Slide>();
        SlideFactory slideFactory = SlideFactory.getInstance();
        Slideshow slideshow = new Slideshow();

        try {
            Object obj = parser.parse(new FileReader(DBNAME));
            //
            //Read JSON file
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray slideList = (JSONArray) jsonObject.get("SlideList");

            for (Object j : slideList) {
                theList.add(slideFactory.makeSlide((JSONObject) j)); //use the SlideFactory to make the specified Slide
            }

            slideshow.setSlideList(theList);
            slideshow.setAutomated((Boolean) jsonObject.get("Automated"));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return slideshow;
    }

    public static ArrayList<String> getSongs()
    {
        JSONParser parser = new JSONParser();
        ArrayList<String> theList = new ArrayList<String>();

        try {
            Object obj = parser.parse(new FileReader(DBNAME));
            //
            //Read JSON file
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray soundList = (JSONArray) jsonObject.get("SoundList");

            for (Object j : soundList) {

                JSONObject tempJ = (JSONObject) j;
                theList.add((String) tempJ.get("name")); //use the ClipFactory to make the specified Clip
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return theList;
    }

    /**
     * This function returns the instance of DBWizard. If no instance exists, then one is created.
     *
     * @return instance- pointer to instance of DBWizard to be used
     */
    public static DBWizard getInstance()
    {
        if (instance == null) {
            instance = new DBWizard();
        }
        return instance;
    }

}
