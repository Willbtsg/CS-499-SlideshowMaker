import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Slideshow {

    private Boolean m_automated;
    private ArrayList<Slide> m_SlideList;
    private int m_slideshowLength;

    public JSONObject toJSON(JSONObject obj)
    {
        JSONArray slideList = new JSONArray();

        for (Slide s : m_SlideList)
        {
            slideList.add(s.toJSON());
        }

        obj.put("SlideList", slideList);
        obj.put("Automated", m_automated);

        if (m_automated)
        {
            obj.put("SlideshowLength", m_slideshowLength);
        }

        return obj;

    }

    public void setSlideList(ArrayList slideList) { m_SlideList = slideList; }
    public ArrayList<Slide> getSlideList() { return m_SlideList; }

    public void setAutomated(Boolean status) { m_automated = status; }
    public Boolean getAutomated() { return m_automated; }

    public void setLength(int length) { m_slideshowLength = length; }
    public int getLength() { return m_slideshowLength; }

    public Slide getSlide(int index) { return m_SlideList.get(index); }
    public void addSlide(Slide newSlide) { m_SlideList.add(newSlide); }
    public void addSlide(Slide newSlide, int index) { m_SlideList.add(index, newSlide); }
}
