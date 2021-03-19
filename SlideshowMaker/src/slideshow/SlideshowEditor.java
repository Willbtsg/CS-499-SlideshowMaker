package slideshow;

import javax.swing.*;
import java.awt.*;

public class SlideshowEditor extends JFrame {

    private static SlideshowEditor instance;
    private static JPanel m_ImageLibrary;
    private static JPanel m_AudioLibrary;
    private JPanel m_controlPanel;
    private JButton getImages;
    private JButton getAudio;

    public static void main(String[] args)
    {
        SlideshowEditor.getInstance();
    }

    public SlideshowEditor()
    {

        setTitle("SlideshowPlayer");
        setLayout(null);

        /**
         *
         * EVAN, PUT YOUR LIBRARIES HERE
        m_ImageLibrary = m_ImageLibrary.getInstance();
        m_AudioLibrary = m_AudioLibrary.getInstance();

        add(m_ImageLibrary);
         */

        m_controlPanel = new JPanel();
        m_controlPanel.setLayout(null);
        m_controlPanel.setBackground(Color.LIGHT_GRAY);
        m_controlPanel.setBounds(0, 0, 1384, 70);
        m_controlPanel.setBorder(BorderFactory.createLineBorder(Color.black, 3));

        getImages = new JButton("Image Library");
        getImages.setBounds(50, 20, 120, 20);
        m_controlPanel.add(getImages);

        getImages.addActionListener(event -> showImageLibrary());

        getAudio = new JButton("Audio Library");
        getAudio.setBounds(1200, 20, 120, 20);
        m_controlPanel.add(getAudio);

        getAudio.addActionListener(event -> showAudioLibrary());

        add(m_controlPanel);

        //Change appearance of JFrame
        setSize(1400,800); //1400 width and 800 height
        setLocationRelativeTo(null);
        setVisible(true); //making the frame visible
        setResizable(false); //disable maximize button

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void showImageLibrary()
    {
        remove(m_AudioLibrary);
        add(m_ImageLibrary);
    }

    private void showAudioLibrary()
    {
        remove(m_ImageLibrary);
        add(m_AudioLibrary);
        System.out.println("ooga");
    }

    /**
     * This function returns the instance of SlideshowPlayer. If no instance exists, then one is created.
     *
     * @return instance- pointer to instance of SlideshowPlayer to be used
     */
    public static SlideshowEditor getInstance()
    {
        if (instance == null) {
            instance = new SlideshowEditor();
        }
        return instance;
    }
}
