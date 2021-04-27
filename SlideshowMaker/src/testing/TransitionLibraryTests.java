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
	}
}
