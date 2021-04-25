package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;

import slideshow.ImageLibrary;
import slideshow.Timeline;

public class ImageLibraryTests {

	// Instantiate an object to perform test methods on
	private static ImageLibrary testImageLibrary = ImageLibrary.getInstance(Timeline.getInstance(),"test_images");
	
	// Test get instance and reset library method
	@Test
	void testGetAndResetInstance() {
		// Reset library
		testImageLibrary = ImageLibrary.resetLibrary(Timeline.getInstance(), "test_images");
		
		// Test true when given wrong directory (it should see that it's not null and grab instance already there)
		assertEquals(testImageLibrary, ImageLibrary.getInstance(Timeline.getInstance(),"images"));
		// Test true when given right directory
		assertEquals(testImageLibrary, ImageLibrary.getInstance(Timeline.getInstance(),"test_images"));
	}
}
