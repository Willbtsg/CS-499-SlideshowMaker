package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

import slideshow.AudioLibrary;
import slideshow.Timeline;

/**
 * Class name: AudioLibraryTests
 *
 * This class tests the methods of the AudioLibrary class.
 */

public class AudioLibraryTests {
	// Instantiate an object to perform test methods on
	private static AudioLibrary testAudioLibrary = AudioLibrary.getInstance(Timeline.getInstance(),"test_images");
	
	// Test get instance and reset library method
	@Test
	void testGetAndResetInstance() {
		// Reset library
		testAudioLibrary = AudioLibrary.resetLibrary(Timeline.getInstance(), "test_images");
		// Test true when given wrong directory (it should see that it's not null and grab instance already there)
		assertEquals(testAudioLibrary, AudioLibrary.getInstance(Timeline.getInstance(),"images"));
		// Test true when given right directory
		assertEquals(testAudioLibrary, AudioLibrary.getInstance(Timeline.getInstance(),"test_images"));
	}
	
	// Test get audio length method
	@Test
	void testGetAudioLength() {
		// Create a valid and invalid file
		File validFile = new File("test_images/Sample.wav");
		File invalidFile = new File("/usr/local/bin/geeks");
		
		// Test that method returns 211 for sample file and 0 for invalid file
		assertEquals(211, testAudioLibrary.getAudioLength(validFile));
		assertEquals(0, testAudioLibrary.getAudioLength(invalidFile));
	}
	
	// Test get instance and reset library method
	@Test
	void testCalculateMinSecLength() {
		// Test that method returns "3:31" string when passed 211 int
		assertEquals("3:31", testAudioLibrary.calculateMinSecLength(211));
	}
}
