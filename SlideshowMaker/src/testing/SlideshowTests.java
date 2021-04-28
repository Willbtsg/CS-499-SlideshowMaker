package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import slideshow.Slide;
import slideshow.Slideshow;

/**
 * Class name: SlideshowTests
 *
 * This class tests the methods of the Slide show class.
 */
public class SlideshowTests {
	// Instantiate an object to perform test methods on
	private Slideshow testSlideshow = new Slideshow();
	
	// Test toJSON function
	@Test
	void testToJSON() {
		// Initialize arraylist of slides and sounds
		ArrayList<Slide> testSlideList = new ArrayList<Slide>();
		testSlideList.add(new Slide("TestSlide1"));
		
		ArrayList<String> testSoundList = new ArrayList<String>();
		testSoundList.add("String1");
		
		// Set slidelist and soundlist in slideshow
		testSlideshow.setSlideList(testSlideList);
		testSlideshow.setSoundList(testSoundList);
		
		// Set m_automated
		testSlideshow.setAutomated(false);
		
		// Create JSON object from slideshow
		JSONObject testJSON = testSlideshow.toJSON();
		
		// Test that slide values were correctly converted
		assertEquals("[{\"imgTime\":0,\"name\":\"TestSlide1\",\"hasTransitions\":false}]", testJSON.get("SlideList").toString());
	}
	
	// Test set and get slidelist functions (Will throw error, but that doesn't matter as faulty names are passed)
	@Test
	void testSetandGetSlideList() {
		// Initialize arraylist of slides
		ArrayList<Slide> testSlideList = new ArrayList<Slide>();
		testSlideList.add(new Slide("TestSlide1"));
		testSlideList.add(new Slide("TestSlide2"));
		
		// Set slidelist in slideshow
		testSlideshow.setSlideList(testSlideList);
		
		assertEquals("TestSlide1", testSlideshow.getSlideList().get(0).getName());
		assertEquals("TestSlide2", testSlideshow.getSlideList().get(1).getName());	
	}
	
	// Test set and get automated functions
	@Test
	void testSetandGetAutomated() {
		// False test case
		testSlideshow.setAutomated(false);
		assertEquals(false, testSlideshow.getAutomated());
		
		//True test case
		testSlideshow.setAutomated(true);
		assertEquals(true, testSlideshow.getAutomated());
	}

	// Test set and get soundlist functions
	@Test
	void testSetandGetSoundList() {
		// Initialize arraylist of strings
		ArrayList<String> testSlideList = new ArrayList<String>();
		testSlideList.add("TestSound1");
		testSlideList.add("TestSound2");
		
		// Set soundlist in slideshow
		testSlideshow.setSoundList(testSlideList);
		
		assertEquals("TestSound1", testSlideshow.getSoundList().get(0));
		assertEquals("TestSound2", testSlideshow.getSoundList().get(1));	
	}
	
	// Test set and get progenitor function
	@Test
	void testSetandGetProgenitor() {
		// Set slide length to test string
		testSlideshow.setProgenitor("testProgenitor");
		
		// Test if string is testLength
		assertEquals("testProgenitor", testSlideshow.getProgenitor());		
	}
}
