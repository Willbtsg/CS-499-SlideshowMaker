package slideshow;
import javax.swing.*;
import java.awt.*;

/**
 * Class name: Transition
 *
 * This is an abstract class that provides a template to the specific transition objects assigned to Slides
 */

abstract class Transition {

    protected String m_type; //contains Transitions's type (same as class name)
    protected long m_time; //contains number of milliseconds for Transition to last

    public String getType() { return m_type; }
    public void setTime(long time) { m_time = time; }
    public double getTime() { return m_time; }
    abstract void doTrans(JLabel imgLabel, Image newImage); //replace Image currently in imgLabel with newImage

}
