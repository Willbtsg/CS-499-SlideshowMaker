package transitions;
import java.util.ArrayList;

/**
 * Class name: TransitionLibrary
 *
 * This class provides the requested forward Transition, along with the appropriate backwards Transition
 * (Example: If going from Slide 1 to Slide 2 uses LRWipe, going back to Slide 1 from Slide 2 would use RLWipe)
 * Since TransitionLibrary acts as an unchanging object source, it is implemented as a Singleton
 */

public class TransitionLibrary {

    private static TransitionLibrary instance;

    /**
     * Returns instance of TransitionLibrary. If no instance exists, one is created.
     * @return instance- reference to the TransitionLibrary Singleton
     */
    public static TransitionLibrary getInstance() {

        if (instance == null)
        {
            instance = new TransitionLibrary();
        }

        return instance;
    }

    /**
     * Supplies calling object with the appropriate pair of Transition objects
     * @param forward- String used to specify the type of Transition used when moving to the next Slide
     * @return desiredTransitions- ArrayList containing requested Transition at index 0, and the appropriate partner Transition at index 1
     */
    public ArrayList<Transition> retrieveTransitions(String forward)
    {
        ArrayList<Transition> desiredTransitions = new ArrayList<Transition>();

        switch (forward)
        {
            case "LRWipe" :
                desiredTransitions.add(new LRWipe());
                desiredTransitions.add(new RLWipe());
                break;

            case "RLWipe" :
                desiredTransitions.add(new RLWipe());
                desiredTransitions.add(new LRWipe());
                break;

            case "UpWipe" :
                desiredTransitions.add(new UpWipe());
                desiredTransitions.add(new DownWipe());
                break;

            case "DownWipe" :
                desiredTransitions.add(new DownWipe());
                desiredTransitions.add(new UpWipe());
                break;

            case "CrossFade" :
                desiredTransitions.add(new CrossFade());
                desiredTransitions.add(new CrossFade());
                break;
        }

        return desiredTransitions;
    }
}
