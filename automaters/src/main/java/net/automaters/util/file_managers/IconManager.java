package net.automaters.util.file_managers;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static net.automaters.util.file_managers.FileManager.PATH_RESOURCES;

public class IconManager {

    public static final ImageIcon AUTOMATERS_TITLE;
    public static final ImageIcon AUTOMATERS_ICON;
    public static final ImageIcon ARROW_RIGHT_ICON;

    static {
        AUTOMATERS_TITLE = set("script\\title.png");
        AUTOMATERS_ICON = set("script\\icon.png");
        ARROW_RIGHT_ICON = set("util\\arrow_right.png");
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

        System.out.println("Image Width: " + width);
        System.out.println("Image Height: " + height);

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
