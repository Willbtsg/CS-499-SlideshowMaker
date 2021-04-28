package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import transitions.Transition;
import transitions.TransitionLibrary;

/**
 * Class name: TransitionLibraryTests
 *
 * This class tests the methods of the transition library class.
 */
public class TransitionLibraryTests {
	
	// Instantiate an object to perform test methods on
	private static TransitionLibrary testLibrary = TransitionLibrary.getInstance();
	
	// Test get instance method
	@Test
	void testGetInstance() {
		assertEquals(testLibrary, TransitionLibrary.getInstance());
	}
	
	// Test retrieve transitions method
	@Test
	void testRetrieveTransitions() {
		// Test LRWipe addition
		ArrayList<Transition> testTransitionList = new ArrayList<Transition>();
		testTransitionList = testLibrary.retrieveTransitions("LRWipe");
		assertEquals(testTransitionList.get(0).getType(), "LRWipe");
		assertEquals(testTransitionList.get(1).getType(), "RLWipe");
		
		// Test RLWipe addition
		testTransitionList = testLibrary.retrieveTransitions("RLWipe");
		assertEquals(testTransitionList.get(0).getType(), "RLWipe");
		assertEquals(testTransitionList.get(1).getType(), "LRWipe");

		// Test UpWipe addition
		testTransitionList = testLibrary.retrieveTransitions("UpWipe");
		assertEquals(testTransitionList.get(0).getType(), "UpWipe");
		assertEquals(testTransitionList.get(1).getType(), "DownWipe");
		
		// Test DownWipe addition
		testTransitionList = testLibrary.retrieveTransitions("DownWipe");
		assertEquals(testTransitionList.get(0).getType(), "DownWipe");
		assertEquals(testTransitionList.get(1).getType(), "UpWipe");
		
		// Test crossfade addition
		testTransitionList = testLibrary.retrieveTransitions("CrossFade");
		assertEquals(testTransitionList.get(0).getType(), "CrossFade");
		assertEquals(testTransitionList.get(1).getType(), "CrossFade");
		
		// Test drwipe addition
		testTransitionList = testLibrary.retrieveTransitions("DRWipe");
		assertEquals(testTransitionList.get(0).getType(), "DRWipe");
		assertEquals(testTransitionList.get(1).getType(), "ULWipe");
		
		// Test ulwipe addition
		testTransitionList = testLibrary.retrieveTransitions("ULWipe");
		assertEquals(testTransitionList.get(0).getType(), "ULWipe");
		assertEquals(testTransitionList.get(1).getType(), "DRWipe");
		
		// Test dlwipe addition
		testTransitionList = testLibrary.retrieveTransitions("DLWipe");
		assertEquals(testTransitionList.get(0).getType(), "DLWipe");
		assertEquals(testTransitionList.get(1).getType(), "URWipe");
		
		// Test urwipe addition
		testTransitionList = testLibrary.retrieveTransitions("URWipe");
		assertEquals(testTransitionList.get(0).getType(), "URWipe");
		assertEquals(testTransitionList.get(1).getType(), "DLWipe");
		
		// Test hzopen addition
		testTransitionList = testLibrary.retrieveTransitions("HZOpen");
		assertEquals(testTransitionList.get(0).getType(), "HZOpen");
		assertEquals(testTransitionList.get(1).getType(), "HZClose");
		
		// Test hzclose addition
		testTransitionList = testLibrary.retrieveTransitions("HZClose");
		assertEquals(testTransitionList.get(0).getType(), "HZClose");
		assertEquals(testTransitionList.get(1).getType(), "HZOpen");
		
		// Test vtopen addition
		testTransitionList = testLibrary.retrieveTransitions("VTOpen");
		assertEquals(testTransitionList.get(0).getType(), "VTOpen");
		assertEquals(testTransitionList.get(1).getType(), "VTClose");
		
		// Test vtclose addition
		testTransitionList = testLibrary.retrieveTransitions("VTClose");
		assertEquals(testTransitionList.get(0).getType(), "VTClose");
		assertEquals(testTransitionList.get(1).getType(), "VTOpen");
	}
}
