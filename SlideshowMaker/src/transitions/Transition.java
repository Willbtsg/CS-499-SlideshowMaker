package transitions;
import javax.swing.*;
import java.awt.*;

/**
 * Class name: Transition
 *
 * This is an abstract class that provides a template to the specific transition objects assigned to Slides
 */

public abstract class Transition {

    protected String m_type; //contains Transitions's type (same as class name)
    protected long m_time; //contains number of milliseconds for Transition to last
    
    public Transition() {m_type = null;};
    public String getType() { return m_type; }
    public void setTime(long time) { m_time = time; }
    public long getTime() { return m_time; }
    public abstract void doTrans(JLabel imgLabel, Image newImage); //replace Image currently in imgLabel with newImage

}
