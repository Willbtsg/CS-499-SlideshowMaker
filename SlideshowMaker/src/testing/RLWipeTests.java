package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.RLWipe;

public class RLWipeTests {

	
	private final RLWipe transition = new RLWipe();
	@Test
	void testGetType() {
		assertEquals("RLWipe", transition.getType());
	}
	
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
