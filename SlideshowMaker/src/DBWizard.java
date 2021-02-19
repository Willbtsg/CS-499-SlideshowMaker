import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DBWizard {


    private static DBWizard instance;
    private static String DBNAME = "images/test.json";

    /**
     * Writes the list of slides out to the master database.
     * This function will be moved to the editor once it has been written
     * @param sl list of slides to be written to the database.
     */
    public static void writeDB(ArrayList<Slide> sl)
    {
        JSONArray list = new JSONArray();
        JSONObject obj = new JSONObject();

        for (Slide s:sl)
        {
            list.add(s.toJSON());
        }

        obj.put("SlideList", list);

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
     * Reads the Master list of movies from the database
     * @return An ArrayList of Movie objects
     *
     */
    public static ArrayList<Slide> readDB()
    {
        JSONParser parser = new JSONParser();

        ArrayList<Slide> theList = new ArrayList<Slide>();

        try {
            Object obj = parser.parse(new FileReader(DBNAME));
            //
            //Read JSON file
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray movieList = (JSONArray) jsonObject.get("SlideList");

            for (Object j : movieList) {
                theList.add(new Slide((JSONObject) j));
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
