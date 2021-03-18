package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.DownWipe;

public class DownWipeTests {
	
	private final DownWipe transition = new DownWipe();
	@Test
	void testGetType() {
		assertEquals("DownWipe", transition.getType());
	}
	
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
