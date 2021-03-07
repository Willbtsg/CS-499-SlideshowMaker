import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class CrossFade extends Transition {

    /**
     * Public constructor for CrossFade. Sets m_type attribute from Transition abstract class to "CrossFade"
     */
    public CrossFade()
    {
        m_type = "CrossFade";
    }

    /**
     * Replaces ImageA in imgLabel with ImageB
     *
     * NOTE: Function adapted from code written by Dr. Rick Coleman and provided by Dr. Harry Delugach
     *
     * @param imgLabel- JLabel where Images are displayed
     * @param ImageA- Image currently being displayed
     * @param ImageB- new Image to be displayed
     */
    void doTrans(JLabel imgLabel, Image ImageA, Image ImageB)
    {
        Graphics2D gPan = (Graphics2D) imgLabel.getGraphics();

        // Create a BufferedImage ARGB to hold the image to overlay
        BufferedImage ImageB_ARGB = new BufferedImage(ImageA.getWidth(null), ImageA.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw ImageB into ImageB_ARGB
        ImageB_ARGB.getGraphics().drawImage(ImageB, 0, 0, null);
        // Set up the initial fade data
        // Create a rescale filter op
        float alphaInc = 0.20f;
        float[] scales = { 1.0f, 1.0f, 1.0f, alphaInc};
        float[] offsets = new float[4];
        RescaleOp rop = new RescaleOp(scales, offsets, null);

        // Draw image A -- appears we need to do this fade longer
        // Each time we redraw ImageB_ARGB over ImageA we add just a bit more
        for(int i=0; i<15; i++)
        {
            // Draw B over A. Note: Can't do the first draw directly into the screen panel
            //	because that drawImage only works with BufferedImages as the destination.
            gPan.drawImage(ImageB_ARGB, rop, 0, 0); // Copy ImageA into panel
            // Note: Can not pause here like we do in the other transitions because
            //     cross dissolve takes longer than a simple blit draw
        }

        // And one final draw to the panel to be sure it's all there
        gPan.drawImage(ImageB, 0,0, imgLabel);
    }

}
