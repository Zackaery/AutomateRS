package net.automaters.script.panel.auto_login;

import net.automaters.script.AutomateRS;
import net.automaters.script.AutomateRSConfig;
import net.automaters.script.panel.AutomateRSPanel;
import net.automaters.util.file_managers.ImageManager;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.ImageUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static net.automaters.script.AutomateRS.debug;
import static net.automaters.script.panel.AutomateRSPanel.*;


public class ProfilePanel extends JPanel {

    private static final ImageIcon DELETE_ICON;
    private static final ImageIcon DELETE_HOVER_ICON;
    private static final ImageIcon START_ICON;
    private static final ImageIcon START_HOVER_ICON;

    static
    {
        final BufferedImage deleteImg = ImageManager.getInstance().loadImage("resources/net.automaters.script/panel/delete_icon.png");
        DELETE_ICON = new ImageIcon(deleteImg);
        DELETE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(deleteImg, -100));

        final BufferedImage startImg = ImageManager.getInstance().loadImage("resources/net.automaters.script/panel/start_icon.png");
        START_ICON = new ImageIcon(startImg);
        START_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(startImg, -100));
    }

    AutomateRSConfig config;
    private final String loginText;
    private String password = null;

    public ProfilePanel(final Client client, final String data, final AutomateRSConfig automateRSConfig, final AutomateRSPanel parent)
    {
        String[] parts = data.split(":", 3);
        this.loginText = parts[1];
        if (parts.length == 3)
        {
            this.password = parts[2];
        }

        final ProfilePanel panel = this;

        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARKER_GRAY_COLOR);

        JPanel labelWrapper = new JPanel(new BorderLayout());
        labelWrapper.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        labelWrapper.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.DARK_GRAY_COLOR),
                BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_COLOR)
        ));

        JPanel panelActions = new JPanel(new BorderLayout(3, 0));
        panelActions.setBorder(new EmptyBorder(0, 0, 0, 8));
        panelActions.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        JLabel delete = new JLabel();
        accountsAdded = true;
        delete.setIcon(DELETE_ICON);
        delete.setToolTipText("Delete account profile");
        delete.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                panel.getParent().remove(panel);
                try
                {
                    parent.removeProfile(data);
                }
                catch (InvalidKeySpecException | NoSuchAlgorithmException | IllegalBlockSizeException |
                       InvalidKeyException | BadPaddingException | NoSuchPaddingException ex)
                {
                    debug(e.toString());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                delete.setIcon(DELETE_HOVER_ICON);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                delete.setIcon(DELETE_ICON);
            }
        });

        JLabel start = new JLabel();
        start.setIcon(START_ICON);
        start.setToolTipText("Start account profile");
        start.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                debug("starting account");
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                start.setIcon(START_HOVER_ICON);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                start.setIcon(START_ICON);
            }
        });

        panelActions.add(start, BorderLayout.CENTER);
        panelActions.add(delete, BorderLayout.EAST);

        JLabel label = new JLabel();
        label.setText(parts[0]);
        label.setBorder(null);
        label.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        label.setPreferredSize(new Dimension(0, 24));
        label.setForeground(Color.WHITE);
        label.setBorder(new EmptyBorder(0, 8, 0, 0));

        labelWrapper.add(label, BorderLayout.CENTER);
        labelWrapper.add(panelActions, BorderLayout.EAST);
        label.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (SwingUtilities.isLeftMouseButton(e) && (client.getGameState() == GameState.LOGIN_SCREEN || client.getGameState() == GameState.LOGIN_SCREEN_AUTHENTICATOR))
                {
                    client.setUsername(loginText);
                    client.setPassword(password);
                }
            }
        });

        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setBorder(new EmptyBorder(8, 0, 8, 0));
        bottomContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        bottomContainer.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (SwingUtilities.isLeftMouseButton(e) && (client.getGameState() == GameState.LOGIN_SCREEN || client.getGameState() == GameState.LOGIN_SCREEN_AUTHENTICATOR))
                {
                    client.setUsername(loginText);
                    client.setPassword(password);
                }
            }
        });

        add(labelWrapper, BorderLayout.NORTH);

    }
}
