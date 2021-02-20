import java.util.ArrayList;

public class TransitionLibrary {

    private static TransitionLibrary instance;

    public static TransitionLibrary getInstance() {

        if (instance == null)
        {
            instance = new TransitionLibrary();
        }

        return instance;
    }

    public ArrayList<Transition> retrieveTransitions(String forward)
    {
        ArrayList<Transition> desiredTransitions = new ArrayList();

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
        }

        return desiredTransitions;
    }
}
