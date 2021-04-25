package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.HZClose;

/**
 * Class name: HZCloseTests
 *
 * This class tests the concrete subclass HZClose (inherits from Transition class).
 */
public class HZCloseTests {
	
	// Instantiate an object to perform test methods on
	private final HZClose transition = new HZClose();
	
	// Test get type method
	@Test
	void testGetType() {
		assertEquals("HZClose", transition.getType());
	}
	
	// Test set and get time method
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
