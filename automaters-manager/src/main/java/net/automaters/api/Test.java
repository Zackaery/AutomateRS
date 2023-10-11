package net.automaters.api;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test extends JFrame {
    private JButton maximizeButton;

    public Test() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Create a custom maximize button
        maximizeButton = new JButton("Maximize");
        maximizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement your custom maximize behavior here
                // For example, toggle between maximize and restore
                if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    setExtendedState(JFrame.NORMAL); // Restore
                    maximizeButton.setText("Maximize");
                } else {
                    setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize
                    maximizeButton.setText("Restore");
                }
            }
        });

        // Add the maximize button to a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(maximizeButton);

        // Add the button panel and other components to the content pane
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Remove window decorations (title bar, minimize, maximize, close buttons)
        setUndecorated(true);

        // Make sure to set a background color so the content is visible
        getContentPane().setBackground(Color.WHITE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Test frame = new Test();
            frame.setVisible(true);
        });
    }
}
