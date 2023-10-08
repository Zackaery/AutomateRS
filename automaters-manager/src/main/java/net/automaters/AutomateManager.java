package net.automaters;

import com.formdev.flatlaf.FlatIntelliJLaf;
import net.automaters.gui.themes.AutomateLaf;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static net.automaters.gui.main.InterfaceHandler.initManager;

public class AutomateManager extends JFrame {

    public static JFrame mainFrame = new JFrame();
    public static String versionNumber = "v0.01";

    public static final int managerWidth = 800;
    public static final int managerHeight = 575;

    public AutomateManager() throws IOException {
        initManager(mainFrame);
    }

    public static void main(String[] args) throws IOException {
        AutomateLaf.registerCustomDefaultsSource("themes");
        AutomateLaf.setup();
        new AutomateManager();
    }
}

