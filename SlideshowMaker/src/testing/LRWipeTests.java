package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.LRWipe;

public class LRWipeTests {
	
	private final LRWipe transition = new LRWipe();
	@Test
	void testGetType() {
		assertEquals("LRWipe", transition.getType());
	}
	
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
