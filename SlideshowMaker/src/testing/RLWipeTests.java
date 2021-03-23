package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.RLWipe;

/**
 * Class name: RLWipeTests
 *
 * This class tests the concrete subclass RLWipe (inherits from Transition class).
 */
public class RLWipeTests {

	// Instantiate an object to perform test methods on
	private final RLWipe transition = new RLWipe();
	
	// Test get type method
	@Test
	void testGetType() {
		assertEquals("RLWipe", transition.getType());
	}
	
	// Test set and get time method
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
