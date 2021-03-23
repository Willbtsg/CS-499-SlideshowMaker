package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.DownWipe;

/**
 * Class name: DownWipeTests
 *
 * This class tests the concrete subclass DownWipe (inherits from Transition class).
 */
public class DownWipeTests {
	
	private final DownWipe transition = new DownWipe();
	
	// Test get type method
	@Test
	void testGetType() {
		assertEquals("DownWipe", transition.getType());
	}
	
	// Test set and get time method
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
