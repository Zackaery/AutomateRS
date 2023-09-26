package net.automaters.util.file_managers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.util.file_managers.FileManager.PATH_RESOURCES;

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

    public BufferedImage loadImage(final String imagePath) {
        String image = PATH_RESOURCES + imagePath;
        debug("Image: "+ image);
        if (imageCache.containsKey(image)) {

            debug("Image Cache: "+ imageCache.get(image));
            return imageCache.get(image);
        }

        debug("Image: "+ image);
        try (InputStream imageInputStream = loadFile(image)) {
            if (imageInputStream == null) {
                return null;
            }
            debug("Image Input Stream: "+ imageInputStream);
            BufferedImage bufferedImage =  ImageIO.read(imageInputStream);

            debug("Buffered Image: "+ bufferedImage);
            imageCache.put(image, bufferedImage);
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}