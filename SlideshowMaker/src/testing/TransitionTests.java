package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.swing.JLabel;

import transitions.Transition;
import transitions.LRWipe;

import org.junit.jupiter.api.Test;

/**
 * Class name: TransitionTests
 *
 * This class tests an abstract class, so it can't be directly instantiated, and concrete subclasses must be used to test some methods.
 */

public class TransitionTests {
	// Instantiate an object to perform test methods on
	private final Transition transition = new LRWipe();
	
	// Test get type method
	@Test
	void testGetType() {
		assertEquals("LRWipe", transition.getType());
	}
	
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
