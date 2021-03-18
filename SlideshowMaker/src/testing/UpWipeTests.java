package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.UpWipe;

public class UpWipeTests {

	
	private final UpWipe transition = new UpWipe();
	@Test
	void testGetType() {
		assertEquals("UpWipe", transition.getType());
	}
	
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
