package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.VTClose;

/**
 * Class name: VTCloseTests
 *
 * This class tests the concrete subclass VTClose (inherits from Transition class).
 */
public class VTCloseTests {
	
	// Instantiate an object to perform test methods on
	private final VTClose transition = new VTClose();
	
	// Test get type method
	@Test
	void testGetType() {
		assertEquals("VTClose", transition.getType());
	}
	
	// Test set and get time method
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
