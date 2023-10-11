package net.automaters;

import net.automaters.api.ui.CustomTitleBarFrame;
import net.automaters.gui.main.MainComponents;
import net.automaters.gui.themes.AutomateLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static net.automaters.api.utils.file_managers.IconManager.AUTOMATERS_ICON;
import static net.automaters.api.utils.file_managers.IconManager.AUTOMATERS_TITLE;

public class AutomateManager extends JFrame {

//    public static JFrame mainFrame = new CustomTitleBarFrame();
    public static JFrame mainFrame = new JFrame();
    public static String versionNumber = "v0.01";

    public static final int managerWidth = 800;
    public static final int managerHeight = 575;

    public static String hexColorAccent = "FF4400"; // Replace with your hexadecimal color code
    public static Color AUTOMATE_ORANGE = Color.decode("#" + hexColorAccent); // Add '#' before the hexadecimal code

    public AutomateManager() throws IOException {
        // --- mainFrame ---
        mainFrame.setTitle("AutomateRS - Manager " + versionNumber);
        mainFrame.setIconImage(AUTOMATERS_ICON.getImage());
        new MainComponents();

    }

    public static void main(String[] args) throws IOException {
        AutomateLaf.registerCustomDefaultsSource("themes");
        AutomateLaf.setup();
        new AutomateManager();
    }
}

