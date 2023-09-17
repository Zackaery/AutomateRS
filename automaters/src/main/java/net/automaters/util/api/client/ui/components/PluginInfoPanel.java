package net.automaters.util.api.client.ui.components;

import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.shadowlabel.JShadowedLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A component to display an info message (to be used on a plugin panel)
 * Example uses are: Input your account details below, then click save to have your account added in your saved profiles.
 */
public class PluginInfoPanel extends JPanel
{
    private final JLabel textTitle = new JShadowedLabel();
    private final JLabel textDescription = new JShadowedLabel();

    public PluginInfoPanel()
    {
        setOpaque(false);
        setBorder(new EmptyBorder(0, 10, 0, 10));
        setLayout(new BorderLayout());

        textTitle.setForeground(Color.WHITE);
        textTitle.setHorizontalAlignment(SwingConstants.CENTER);

        textDescription.setFont(FontManager.getRunescapeSmallFont());
        textDescription.setForeground(Color.GRAY);
        textDescription.setHorizontalAlignment(SwingConstants.CENTER);

        add(textTitle, BorderLayout.NORTH);
        add(textDescription, BorderLayout.CENTER);

        setVisible(false);
    }

    /**
     * Changes the content of the panel to the given parameters.
     * The description has to be wrapped in html so that its text can be wrapped.
     */
    public void setContent(String title, String description)
    {
        textTitle.setText(title);
        textDescription.setText("<html><body style = 'text-align:center'>" + description + "</body></html>");
        setVisible(true);
    }
}