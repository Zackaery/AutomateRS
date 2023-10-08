package net.automaters.gui.main;

import javax.swing.*;
import java.io.IOException;

import static net.automaters.AutomateManager.*;

public class InterfaceHandler {


    public static void initManager(JFrame frame) throws IOException {
        // --- mainFrame ---
        frame.setTitle("AutomateRS - Manager " + versionNumber);
        new MainComponents();
    }
}
