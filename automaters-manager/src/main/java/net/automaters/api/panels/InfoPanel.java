package net.automaters.api.panels;

import net.runelite.client.ui.FontManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A component to display an info message (to be used on a plugin panel)
 * Example uses are: Input your account details below, then click save to have your account added in your saved profiles.
 */
public class InfoPanel extends JPanel {
    private final JLabel textTitle = new JLabel();
    private final JLabel textDescription = new JLabel();

    public InfoPanel() {
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 0, 10));
        setLayout(new BorderLayout());

        textTitle.setFont(new Font("Arial", Font.BOLD, 20));
        textTitle.setForeground(Color.WHITE);
        textTitle.setHorizontalAlignment(SwingConstants.CENTER);

        textDescription.setFont(new Font("Arial", Font.PLAIN, 12));
        textDescription.setForeground(Color.LIGHT_GRAY);
        textDescription.setHorizontalAlignment(SwingConstants.CENTER);

        add(textTitle, BorderLayout.NORTH);
        add(textDescription, BorderLayout.CENTER);

        setVisible(false);
    }

    /**
     * Changes the content of the panel to the given parameters.
     * The description has to be wrapped in html so that its text can be wrapped.
     */
    public void setContent(String title, String description) {
        textTitle.setText(title);
        textDescription.setText("<html><body style = 'text-align:center'>" + description + "</body></html>");
        setVisible(true);
    }
}