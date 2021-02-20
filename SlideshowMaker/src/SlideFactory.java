import org.json.simple.JSONObject;

import java.util.ArrayList;

public class SlideFactory {

    private static SlideFactory instance;

    public static SlideFactory getInstance()
    {
        if (instance == null)
        {
            instance = new SlideFactory();
        }
        return instance;
    }

    public Slide makeSlide(String name)
    {
        Slide newSlide = new Slide(name);

        return newSlide;
    }

    public Slide makeSlide(JSONObject j)
    {
        TransitionLibrary transitionLibrary = TransitionLibrary.getInstance();
        ArrayList<Transition> tempTransitions = new ArrayList();

        Slide newSlide = new Slide((String) j.get("name"));

        if ((Boolean) j.get("hasTransitions"))
        {
            tempTransitions = transitionLibrary.retrieveTransitions((String) j.get("forward"));

            newSlide.setForward(tempTransitions.get(0));
            newSlide.setBackwards(tempTransitions.get(1));
        }



        return newSlide;
    }
}
