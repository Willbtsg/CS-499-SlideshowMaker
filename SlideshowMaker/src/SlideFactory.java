import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class name: SlideFactory
 *
 * This class is used to construct new Slide objects, either from a filename or from a JSONObject
 * This class is implemented as a Singleton
 */

public class SlideFactory {

    private static SlideFactory instance;

    /**
     * This function returns the instance of SlideFactory. If no instance exists, then one is created.
     *
     * @return instance- pointer to instance of SlideFactory to be used
     */
    public static SlideFactory getInstance()
    {
        if (instance == null)
        {
            instance = new SlideFactory();
        }
        return instance;
    }

    /**
     * Makes a new Slide using the provided filename. This funciton will be used by the SlideshowEditor
     * @param name- filename of Image to be attached to the Slide
     * @return newSlide- returns new Slide object
     */
    public Slide makeSlide(String name) {
        Slide newSlide = new Slide(name);

        return newSlide;
    }

    /**
     * Creates a new Slide object from a JSONObject
     * @param j- JSONObject that contains necessary information about the new Slide
     * @return newSlide- returns new Slide object
     */
    public Slide makeSlide(JSONObject j) {
        TransitionLibrary transitionLibrary = TransitionLibrary.getInstance(); //retrieve TransitionLibrary Singleton
        ArrayList<Transition> tempTransitions;

        Slide newSlide = new Slide((String) j.get("name"));

        if ((Boolean) j.get("hasTransitions")) //only try to assign Transitions if the Slide is supposed to have them
        {
            tempTransitions = transitionLibrary.retrieveTransitions((String) j.get("forward"));

            for (Transition transition : tempTransitions)
            {
                transition.setTime((long) j.get("transTime"));
            }

            newSlide.setForward(tempTransitions.get(0));
            newSlide.setBackwards(tempTransitions.get(1));
        }

        return newSlide;
    }
}
