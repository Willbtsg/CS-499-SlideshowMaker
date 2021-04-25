package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.DLWipe;

/**
 * Class name: DLWipeTests
 *
 * This class tests the concrete subclass DLWipe (inherits from Transition class).
 */
public class DLWipeTests {
	
	// Instantiate an object to perform test methods on
	private final DLWipe transition = new DLWipe();
	
	// Test get type method
	@Test
	void testGetType() {
		assertEquals("DLWipe", transition.getType());
	}
	
	// Test set and get time method
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
