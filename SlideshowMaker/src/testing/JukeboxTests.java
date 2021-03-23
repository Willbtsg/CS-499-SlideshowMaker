package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import slideshow.Jukebox;

/**
 * Class name: JukeboxTests
 *
 * This class tests the methods of the Jukebox class.
 */

public class JukeboxTests {
	
	// Instantiate an object to perform test methods on
	private static Jukebox testJukebox = Jukebox.getInstance();
	
	// Test get instance method
	@Test
	void testGetInstance() {
		assertEquals(testJukebox, Jukebox.getInstance());
	}
	
	// Test add song
	@Test
	void testAddSong() {
		testJukebox.addSong("SampleClip");
		assertEquals("SampleClip", testJukebox.getSoundList().get(0));
	}
	
	// Test set and get methods for sound list
	@Test
	void testSetandGetSoundList() {
		ArrayList<String> testSoundList = new ArrayList<String>();
		testSoundList.add("SampleClip1");
		testSoundList.add("SampleClip2");
		
		testJukebox.setSoundList(testSoundList);
		assertEquals("SampleClip1", testJukebox.getSoundList().get(0));
		assertEquals("SampleClip2", testJukebox.getSoundList().get(1));
	}
}
