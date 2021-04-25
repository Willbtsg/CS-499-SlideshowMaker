package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.URWipe;

/**
 * Class name: URWipeTests
 *
 * This class tests the concrete subclass URWipe (inherits from Transition class).
 */
public class URWipeTests {

	// Instantiate an object to perform test methods on
	private final URWipe transition = new URWipe();
	
	// Test get type method
	@Test
	void testGetType() {
		assertEquals("URWipe", transition.getType());
	}
	
	// Test set and get time method
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
