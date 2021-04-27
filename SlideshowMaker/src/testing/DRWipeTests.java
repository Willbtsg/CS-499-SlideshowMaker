package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.DRWipe;

/**
 * Class name: DRWipeTests
 *
 * This class tests the concrete subclass DRWipe (inherits from Transition class).
 */
public class DRWipeTests {
	
	// Instantiate an object to perform test methods on
	private final DRWipe transition = new DRWipe();
	
	// Test get type method
	@Test
	void testGetType() {
		assertEquals("DRWipe", transition.getType());
	}
	
	// Test set and get time method
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
