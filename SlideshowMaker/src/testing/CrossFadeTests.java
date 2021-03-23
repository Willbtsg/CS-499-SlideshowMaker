package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.CrossFade;

/**
 * Class name: CrossFadeTests
 *
 * This class tests the concrete subclass CrossFade (inherits from Transition class).
 */
public class CrossFadeTests {
	
	// Instantiate an object to perform test methods on
	private final CrossFade transition = new CrossFade();
	
	// Test get type method
	@Test
	void testGetType() {
		assertEquals("CrossFade", transition.getType());
	}
	
	// Test set and get time method
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
