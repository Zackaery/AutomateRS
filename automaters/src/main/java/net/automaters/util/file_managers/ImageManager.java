package net.automaters.util.file_managers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static net.automaters.api.utils.Debug.debug;

public class ImageManager extends ResourceManager {

    private static ImageManager instance;

    private final Map<String, BufferedImage> imageCache = new HashMap<>();

    private ImageManager() {
    }

    synchronized public static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    public BufferedImage loadImage(final String relativeImagePath) {
        String image = "resources/net.automaters.script/" + relativeImagePath;
        if (imageCache.containsKey(image)) {
            return imageCache.get(image);
        }

        try (InputStream imageInputStream = loadFile(image)) {
            if (imageInputStream == null) {
                return null;
            }
            BufferedImage bufferedImage =  ImageIO.read(imageInputStream);
            imageCache.put(image, bufferedImage);
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}