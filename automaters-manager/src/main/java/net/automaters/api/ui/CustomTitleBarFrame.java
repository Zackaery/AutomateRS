package net.automaters.api.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomTitleBarFrame extends JFrame {
    private int initialX;
    private int initialY;

    public CustomTitleBarFrame() {
        setUndecorated(true); // Remove the default window decorations
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Create a custom title bar component
        JPanel customTitleBar = new JPanel();
        customTitleBar.setBackground(Color.LIGHT_GRAY);
        customTitleBar.setPreferredSize(new Dimension(getWidth(), 30));
        customTitleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Record initial mouse coordinates
                initialX = e.getX();
                initialY = e.getY();
            }
        });
        customTitleBar.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Calculate new frame position based on mouse movement
                int newX = getLocation().x + e.getX() - initialX;
                int newY = getLocation().y + e.getY() - initialY;

                // Update the frame's position
                setLocation(newX, newY);
            }
        });

        // Add components to the frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(customTitleBar, BorderLayout.NORTH);

        // Add other content to the frame
        JTextArea textArea = new JTextArea();
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

}