import javax.swing.*;
import java.awt.*;

abstract class Transition {

    protected String m_type;

    public String getType() { return m_type; }
    abstract void doTrans(JLabel imgPanel, Image ImageA, Image ImageB, double time);

}
