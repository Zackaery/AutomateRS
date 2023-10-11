package net.automaters.api.ui;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class CustomTabbedPaneUI extends BasicTabbedPaneUI {
    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        // Customize the tab height here (e.g., set it to 40 pixels)
        return 50;
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        // Customize the tab width here (e.g., set it to 100 pixels)
        return 100;
    }
}
