package transitions;
import javax.swing.*;
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

    /**
     * This function is used when the user adds Transition data to Slides in the SlideshowEditor
     * @return
     */
    public String retrievalGUI()
    {

        String transitionInfo = new String();

        String transitions[] = {"LRWipe", "RLWipe", "UpWipe", "DownWipe", "CrossFade"}; //set list of Transition options

        String transChoice;
        transChoice = (String) JOptionPane.showInputDialog(null, "What transition would you like to use?", "Transition Select", JOptionPane.PLAIN_MESSAGE, null, transitions, transitions[0]);

        if (transChoice != null) //if user made a selection, proceed with Transition creation
        {

            if (retrieveTransitions(transChoice).size() > 0) //if Transition selected was valid, proceed
            {
                transitionInfo = transChoice + ": "; //save Transition selection to output message

                Double times[] = {1.0, 2.0, 3.0, 4.0, 5.0}; //set options for Transition length

                Double timeChoice;
                timeChoice = (Double) JOptionPane.showInputDialog(null, "How many seconds do you want your transition to last?", "Transition Select", JOptionPane.PLAIN_MESSAGE, null, times, times[0]);

                if (timeChoice != null) //if user made a valid Transition length selection...
                {
                    transitionInfo += timeChoice.toString();
                    transitionInfo += " seconds";

                } else { //if a valid length was not selected...
                    return "None";
                }
            }
        }

        return transitionInfo;

    }

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

}
