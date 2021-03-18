package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.swing.JLabel;

import transitions.Transition;
import transitions.LRWipe;

import org.junit.jupiter.api.Test;

import javafx.scene.image.Image;

/**
 * Class name: TransitionTests
 *
 * This class tests an abstract class, so it can't be directly instantiated, and concrete subclasses must be used to test some methods.
 */

public class TransitionTests {
	
	private final Transition transition = new LRWipe();
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
