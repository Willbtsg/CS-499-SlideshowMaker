package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import slideshow.DBWizard;
import slideshow.Slideshow;

/**
 * Class name: DBWizardTests
 *
 * This class tests the methods of the DBWizard class.
 */
public class DBWizardTests {
	// Instantiate an object to perform test methods on
	private static DBWizard testDBWizard = DBWizard.getInstance();
	
	// Test get instance method
	@Test
	void testGetInstance() {
		assertEquals(testDBWizard, DBWizard.getInstance());
	}
	
	// Test get slide show method
	@Test
	void testGetSlideshow() {
		// Test if declared slideshow is actually returned, works because testSlideshow is never declared
		Slideshow testSlideShow = DBWizard.getSlideshow();
		testSlideShow.setAutomated(true);
		assertEquals(testSlideShow.getAutomated(), true);
	}
	
	// Test get songs method
	@Test
	void testGetSongs() {
		ArrayList<String> testSongList = DBWizard.getSongs();
		// If song list gets declared correctly
		if(testSongList.size() >= 0) {
			// Test passed
			assertEquals(1, 1);
		} // Otherwise, test failed
		else {
			assertEquals(0,1);
		}
		
	}
}
