package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import slideshow.Slide;

/**
 * Class name: SlideTests
 *
 * This class tests the methods of the Slide class.
 */
public class SlideTests {
	// Instantiate an object to perform test methods on
	private Slide testSlide = new Slide("test_images/audioicon.png");
	
	// Test toJSON and JSONObject constructor function
	@Test
	void testToJSON() {
		// Create JSON object and add imgTime to it
		JSONObject testJSON = testSlide.toJSON();
		testJSON.put("imgTime", (long) 20);
		
		// Call constructor that uses testJSON file to reset slide
		testSlide = new Slide(testJSON);
		
		// test if name is correct and that the time was added
		assertEquals("test_images/audioicon.png", testSlide.getName());
		assertEquals((long) 20, testSlide.getTime());
	}
	
	// Test set image functions
	@Test
	void testSetandGetImage() {
		// Slide(String name) function calls setImage function
		Slide newSlide = new Slide("test_images/audioicon.png");
		
		assertEquals("test_images/audioicon.png", newSlide.getName());	// Check that name is correct
		
		// If image was actually set to a value image object will not be null 
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
		assertEquals("test_images/audioicon.png", testSlide.getName());
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
}
