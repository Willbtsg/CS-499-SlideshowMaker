package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.ULWipe;

/**
 * Class name: ULWipeTests
 *
 * This class tests the concrete subclass ULWipe (inherits from Transition class).
 */
public class ULWipeTests {

	// Instantiate an object to perform test methods on
	private final ULWipe transition = new ULWipe();
	
	// Test get type method
	@Test
	void testGetType() {
		assertEquals("ULWipe", transition.getType());
	}
	
	// Test set and get time method
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
