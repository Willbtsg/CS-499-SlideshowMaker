package slideshow;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

public class Timeline extends JPanel {

    ////////////
    // FIELDS //
    ////////////

    private static Timeline instance;
    private ArrayList<JPanel> timelineItems;
    private SlideshowEditor associatedEditor;
    private Slideshow slideshow;

    /////////////
    // METHODS //
    /////////////

    public Timeline(SlideshowEditor editor)
    {
        timelineItems = new ArrayList<>();
        associatedEditor = editor;
        slideshow = new Slideshow();

        GridLayout grid = new GridLayout(0,1);
        setLayout(grid);
    }

    public void addSlide(String filePath)
    {
        File image = new File(filePath);

        JPanel timelineItem = new JPanel();
        timelineItem.setLayout(new BorderLayout());

        JPanel buttonsAndTitle = new JPanel();
        buttonsAndTitle.setLayout(new BorderLayout());

        JLabel imgTitle = new JLabel(image.getName(), SwingConstants.CENTER);
        buttonsAndTitle.add(imgTitle, BorderLayout.NORTH);

        JPanel buttons = new JPanel();
        JButton moveUp = new JButton("Move Up");
        moveUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentItemIndex = timelineItems.indexOf(timelineItem);
                if (currentItemIndex > 0)
                {
                    for (int i = 0; i < timelineItems.size(); i++)
                        remove(timelineItems.get(i));
                    JPanel temp = timelineItems.get(currentItemIndex-1);
                    timelineItems.set(currentItemIndex-1, timelineItem);
                    timelineItems.set(currentItemIndex, temp);
                    for (int i = 0; i < timelineItems.size(); i++)
                    {
                        timelineItems.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i+1)));
                        add(timelineItems.get(i));
                    }
                    associatedEditor.revalidate();
                }
            }
        });
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: When there's only one timelineItem, deletion works on the backend but not on the frontend
                // (if you delete the last remaining slide, the slide appears to stay there, but when you
                //  add a new slide, only that slide will be showing. so this is a GUI-only issue.)
                remove(timelineItem);
                associatedEditor.revalidate();
            }
        });
        JButton moveDown = new JButton("Move Down");
        moveDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentItemIndex = timelineItems.indexOf(timelineItem);
                if (currentItemIndex < timelineItems.size()-1)
                {
                    for (int i = 0; i < timelineItems.size(); i++)
                        remove(timelineItems.get(i));
                    JPanel temp = timelineItems.get(currentItemIndex+1);
                    timelineItems.set(currentItemIndex+1, timelineItem);
                    timelineItems.set(currentItemIndex, temp);
                    for (int i = 0; i < timelineItems.size(); i++)
                    {
                        timelineItems.get(i).setBorder(BorderFactory.createTitledBorder(String.valueOf(i+1)));
                        add(timelineItems.get(i));
                    }
                    associatedEditor.revalidate();
                }
            }
        });

        buttons.add(moveUp, BorderLayout.WEST);
        buttons.add(deleteButton, BorderLayout.CENTER);
        buttons.add(moveDown, BorderLayout.EAST);

        buttonsAndTitle.add(buttons, BorderLayout.SOUTH);
        timelineItem.add(buttonsAndTitle, BorderLayout.SOUTH);

        JLabel thumbnail = new JLabel("", SwingConstants.CENTER);
        ImageIcon img = new ImageIcon(filePath);
        double proportion = 200.0/img.getIconWidth();
        double db_newHeight = proportion*img.getIconHeight();
        int newHeight = (int)Math.round(db_newHeight);
        Image imgIcon = img.getImage().getScaledInstance(200,newHeight,Image.SCALE_REPLICATE);
        img = new ImageIcon(imgIcon);
        thumbnail.setIcon(img);
        timelineItem.add(thumbnail, BorderLayout.CENTER);

        JComboBox<String> transitionOptions = new JComboBox<>();
        transitionOptions.addItem("No Transition");
        transitionOptions.addItem("Wipe Right");
        transitionOptions.addItem("Wipe Left");
        transitionOptions.addItem("Wipe Up");
        transitionOptions.addItem("Wipe Down");
        transitionOptions.addItem("Crossfade");
        timelineItem.add(transitionOptions, BorderLayout.NORTH);

        Border timelineItemBorder = BorderFactory.createTitledBorder(String.valueOf(timelineItems.size()+1));
        timelineItem.setBorder(timelineItemBorder);

        timelineItem.setPreferredSize(new Dimension(220,400));
        timelineItems.add(timelineItem);
        add(timelineItems.get(timelineItems.size()-1));
    }

    public static Timeline getInstance(SlideshowEditor editor)
    {
        if (instance == null) {
            instance = new Timeline(editor);
        }
        return instance;
    }

}
