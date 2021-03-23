package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.UpWipe;


/**
 * Class name: UpWipeTests
 *
 * This class tests the concrete subclass UpWipe (inherits from Transition class).
 */
public class UpWipeTests {

	// Instantiate an object to perform test methods on
	private final UpWipe transition = new UpWipe();
	
	// Test get type method
	@Test
	void testGetType() {
		assertEquals("UpWipe", transition.getType());
	}
	
	// Test set and get time method
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
