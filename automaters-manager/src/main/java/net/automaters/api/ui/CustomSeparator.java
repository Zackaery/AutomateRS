package net.automaters.api.ui;

import javax.swing.*;
import java.awt.*;

public class CustomSeparator extends JComponent {
    private Color separatorColor;
    private int separatorWidth;
    private int separatorHeight;

    public CustomSeparator(Color color, int width, int height) {
        separatorColor = color;
        separatorWidth = width;
        separatorHeight = height;
        setPreferredSize(new Dimension(separatorWidth, separatorHeight)); // Set the preferred size for a horizontal separator
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(separatorColor);
        g.fillRect(0, 0, separatorWidth, separatorHeight);
    }
}
