package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import transitions.CrossFade;

public class CrossFadeTests {
	
	private final CrossFade transition = new CrossFade();
	@Test
	void testGetType() {
		assertEquals("CrossFade", transition.getType());
	}
	
	@Test
	void testSetandGetTime() {
		transition.setTime(2000);
		assertEquals(2000, transition.getTime());
	}
}
