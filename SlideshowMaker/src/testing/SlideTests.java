package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import slideshow.Slide;
import transitions.LRWipe;
import transitions.RLWipe;

/**
 * Class name: SlideTests
 *
 * This class tests the methods of the Slide class.
 */
public class SlideTests {
	// Instantiate an object to perform test methods on
	private Slide testSlide = new Slide("images/download.png");
	
	// Test toJSON and JSONObject constructor function
	@Test
	void testToJSON() {
		JSONObject testJSON = testSlide.toJSON();
		testJSON.put("imgTime", (long) 20);
		testSlide = new Slide(testJSON);
		assertEquals("images/download.png", testSlide.getName());
		assertEquals((long) 20, testSlide.getTime());
	}
	
	// Test set image functions
	@Test
	void testSetandGetImage() {
		// Slide(String name) function calls setimage function
		Slide newSlide = new Slide("images/audioicon.png");
		
		assertEquals("images/audioicon.png", newSlide.getName());	
		
		if(newSlide.getImage() != null) {
			// Test passed
			assertEquals(1, 1);
		} // Otherwise, test failed
		else {
			assertEquals(0,1);
		}
	}
	
	// Test get name function
	@Test
	void testGetName() {
		assertEquals("images/download.png", testSlide.getName());
	}
	
	// Test set and get time functions
	@Test
	void testSetandGetTime() {
		testSlide.setTime((long) 50);
		assertEquals((long) 50, testSlide.getTime());
	}
	
	// Test has transitions function
	@Test
	void testHasTransitions() {
		// Initial value should be false
		assertEquals(false, testSlide.hasTransitions());
		
		// Sets the value of m_transitions to true
		testSlide.setTransitions("LRWipe");
		
		assertEquals(true, testSlide.hasTransitions());
	}
	
	// Test set and get forward and backwards functions
	@Test
	void testSetandGetForwardandBackwards() {
		// Sets the forwards and backwards// Sets the value of m_transitions to true
		testSlide.setTransitions("RLWipe");
		
		assertEquals("RLWipe", testSlide.getForward());
		assertEquals("LRWipe", testSlide.getBackwards());
	}
}
