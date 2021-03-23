package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.LRWipe;


/**
 * Class name: LRWipeTests
 *
 * This class tests the concrete subclass LRWipe (inherits from Transition class).
 */
public class LRWipeTests {
	
	// Instantiate an object to perform test methods on
	private final LRWipe transition = new LRWipe();
	
	// Test get type method
	@Test
	void testGetType() {
		assertEquals("LRWipe", transition.getType());
	}
	
	// Test set and get time method
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
