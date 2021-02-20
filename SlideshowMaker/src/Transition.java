import javax.swing.*;
import java.awt.*;

/**
 * Class name: Transition
 *
 * This is an abstract class that provides a template to the specific transition objects assigned to Slides
 */

abstract class Transition {

    protected String m_type; //contains transitions's type (same as class name)

    public String getType() { return m_type; }
    abstract void doTrans(JLabel imgLabel, Image ImageA, Image ImageB, double time); //over the course of "time", replace ImageA with ImageB  within imgLabel

}
