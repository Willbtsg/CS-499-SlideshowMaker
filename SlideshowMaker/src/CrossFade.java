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
     * Draws newImage in imgLabel using CrossFade effect
     *
     * NOTE: Function adapted from code written by Dr. Rick Coleman and provided by Dr. Harry Delugach
     *
     * @param imgLabel- JLabel where Images are displayed
     * @param newImage- new Image to be displayed
     */
    void doTrans(JLabel imgLabel, Image newImage)
    {
        Graphics2D gPan = (Graphics2D) imgLabel.getGraphics();

        // Create a BufferedImage ARGB to hold the image to overlay
        BufferedImage newImage_ARGB = new BufferedImage(newImage.getWidth(null), newImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw newImage into newImage_ARGB
        newImage_ARGB.getGraphics().drawImage(newImage, 0, 0, null);
        // Set up the initial fade data
        // Create a rescale filter op
        float alphaInc = 0.20f;
        float[] scales = { 1.0f, 1.0f, 1.0f, alphaInc};
        float[] offsets = new float[4];
        RescaleOp rop = new RescaleOp(scales, offsets, null);

        // Draw image A -- appears we need to do this fade longer
        // Each time we redraw newImage_ARGB in imgLabel we add just a bit more
        for(int i=0; i<15; i++)
        {
            // Draw B over A. Note: Can't do the first draw directly into the screen panel
            //	because that drawImage only works with BufferedImages as the destination.
            gPan.drawImage(newImage_ARGB, rop, 0, 0); // Copy newImage_ARGB into panel
            // Note: Can not pause here like we do in the other transitions because
            //     cross dissolve takes longer than a simple blit draw
        }

        // And one final draw to the panel to be sure it's all there
        gPan.drawImage(newImage, 0,0, imgLabel);
    }

}
