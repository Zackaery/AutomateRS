package net.automaters.api.utils.file_managers;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import static net.automaters.api.utils.Debug.debug;

public class FontManager extends ResourceManager {

    private static final String FONT_DIR = "fonts/";

    private static final String[] FONT_FILES = {
            "Roboto-Regular.ttf"
    };
    public static Font ROBOTO_REGULAR = new Font("Roboto-Regular", Font.PLAIN, 12);

    static {
        loadFonts();
    }

    private static void loadFonts() {
        for (String font : FONT_FILES) {
            String relativeFontPath = FONT_DIR + font;

            if (!loadFont(relativeFontPath)) {
                debug("Failed to load font: " + font);
            }
        }
    }

    private static boolean loadFont(final String relativeFontPath) {
        try (InputStream fontInputStream = loadFile(relativeFontPath)) {

            if (fontInputStream == null) {
                return false;
            }

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontInputStream));
            return true;

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        return false;
    }
}
