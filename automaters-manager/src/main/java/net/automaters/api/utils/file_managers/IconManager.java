package net.automaters.api.utils.file_managers;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class IconManager {

    public static final ImageIcon AUTOMATERS_TITLE;
    public static final ImageIcon AUTOMATERS_ICON;

    public static final ImageIcon ARROW_UP_ICON;
    public static final ImageIcon ARROW_RIGHT_ICON;
    public static final ImageIcon ARROW_DOWN_ICON;
    public static final ImageIcon ARROW_LEFT_ICON;

    public static final ImageIcon DELETE_ICON;
    public static final ImageIcon DELETE_HOVER_ICON;
    public static final ImageIcon START_ICON;
    public static final ImageIcon START_HOVER_ICON;
    public static final ImageIcon STARTED_ICON;
    public static final ImageIcon STOP_ICON;
    public static final ImageIcon STOP_HOVER_ICON;
    public static final ImageIcon LOADING_ICON;

    static {
        AUTOMATERS_TITLE = set("manager\\title.png");
        AUTOMATERS_ICON = set("script\\icon.png");

        ARROW_UP_ICON = set("util\\arrow_up.png");
        ARROW_RIGHT_ICON = set("util\\arrow_right.png");
        ARROW_DOWN_ICON = set("util\\arrow_down.png");
        ARROW_LEFT_ICON = set("util\\arrow_left.png");

        DELETE_ICON = set("util\\delete.png");
        DELETE_HOVER_ICON = set("util\\delete_hovered.png");
        START_ICON = set("util\\start.png");
        START_HOVER_ICON = set("util\\start_hovered.png");
        STARTED_ICON = set("util\\started.png");
        STOP_ICON = set("util\\stop.png");
        STOP_HOVER_ICON = set("util\\stop_hovered.png");
        LOADING_ICON = set("util\\loading_spinner.gif");
    }

    public static ImageIcon set(String imagePath) {
        return new ImageIcon(ImageManager.getInstance().loadImage(imagePath));
    }
    public static BufferedImage convert(ImageIcon icon) {
        if (icon == null) {
            return null;
        }
        Image image = icon.getImage();

        int width = image.getWidth(null);
        int height = image.getHeight(null);

        // Validate width and height
        if (width <= 0 || height <= 0) {
            return null; // Handle invalid dimensions gracefully
        }

        BufferedImage bufferedImage = new BufferedImage(
                image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bufferedImage;
    }
}
