package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import slideshow.SlideshowManager;
import slideshow.Slideshow;

/**
 * Class name: SlideshowManagerTests
 *
 * This class tests the methods of the SlideshowManager class.
 */
public class SlideshowManagerTests {
	// Instantiate an object to perform test methods on
	private static SlideshowManager testSlideshowManager = SlideshowManager.getInstance();
	
	// Test get instance method
	@Test
	void testGetInstance() {
		assertEquals(testSlideshowManager, SlideshowManager.getInstance());
	}
	
	// Test get slide show method
	@Test
	void testGetSlideshow() {
		// Test if declared slideshow is actually returned, works because testSlideshow is never declared
		Slideshow testSlideShow = SlideshowManager.getSlideshow();
		testSlideShow.setAutomated(true);
		assertEquals(testSlideShow.getAutomated(), true);
	}
}
