package slideshow;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Timeline extends JPanel {

    ////////////
    // FIELDS //
    ////////////

    private static Timeline instance;
    private SlideshowEditor associatedEditor;
    private Slideshow slideshow;

    /////////////
    // METHODS //
    /////////////

    public Timeline(SlideshowEditor editor)
    {
        associatedEditor = editor;
        slideshow = new Slideshow();

        GridLayout grid = new GridLayout(0,1);
        setLayout(grid);
    }

    public void addSlide(String filePath)
    {
        System.out.println("Calling addSlide() with " + filePath);

        File image = new File(filePath);

        JPanel timelineItem = new JPanel();
        timelineItem.setLayout(new BorderLayout());
        JPanel buttonAndTitle = new JPanel();
        buttonAndTitle.setLayout(new BorderLayout());

        JLabel imgTitle = new JLabel(image.getName(), SwingConstants.CENTER);
        buttonAndTitle.add(imgTitle, BorderLayout.NORTH);
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Deletion doesn't work at all when there's only one timelineItem
                remove(timelineItem);
                associatedEditor.revalidate();
            }
        });
        buttonAndTitle.add(deleteButton, BorderLayout.SOUTH);
        timelineItem.add(buttonAndTitle, BorderLayout.SOUTH);

        JLabel thumbnail = new JLabel("", SwingConstants.CENTER);
        ImageIcon img = new ImageIcon(filePath);
        double proportion = 200.0/img.getIconWidth();
        double db_newHeight = proportion*img.getIconHeight();
        int newHeight = (int)Math.round(db_newHeight);
        Image imgIcon = img.getImage().getScaledInstance(200,newHeight,Image.SCALE_REPLICATE);
        img = new ImageIcon(imgIcon);
        thumbnail.setIcon(img);
        timelineItem.add(thumbnail, BorderLayout.CENTER);

        timelineItem.setPreferredSize(new Dimension(200,300));
        add(timelineItem);
    }

    public static Timeline getInstance(SlideshowEditor editor)
    {
        if (instance == null) {
            instance = new Timeline(editor);
        }
        return instance;
    }

}
