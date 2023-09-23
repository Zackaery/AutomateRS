package net.automaters.script.panel.auto_login;

import net.automaters.script.AutomateRS;
import net.automaters.script.AutomateRSConfig;
import net.automaters.script.panel.AutomateRSPanel;
import net.automaters.util.file_managers.ImageManager;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.ImageUtil;
import net.unethicalite.api.input.Keyboard;

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

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.panel.AutomateRSPanel.*;

public class ProfilePanel extends JPanel {

    private AutomateRS plugin;

    private static final ImageIcon DELETE_ICON;
    private static final ImageIcon DELETE_HOVER_ICON;
    private static final ImageIcon START_ICON;
    private static final ImageIcon START_HOVER_ICON;

    static
    {
        final BufferedImage deleteImg = ImageManager.getInstance().loadImage("panel/delete_icon.png");
        DELETE_ICON = new ImageIcon(deleteImg);
        DELETE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(deleteImg, -100));

        final BufferedImage startImg = ImageManager.getInstance().loadImage("panel/start_icon.png");
        START_ICON = new ImageIcon(startImg);
        START_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(startImg, -100));
    }

    AutomateRSConfig config;
    private static String loginText = null;
    private static String password = null;
    private static Boolean useWorld = null;
    private static Integer world = null;
    Client thisClient;

    public ProfilePanel(final Client client, final String data, final AutomateRSConfig automateRSConfig, final AutomateRSPanel parent)
    {

        thisClient = client;
        String[] parts = data.split(":", 5);
        this.loginText = parts[1];
        if (parts.length == 5)
        {
            this.password = parts[2];
            this.useWorld = Boolean.valueOf(parts[3]);
            this.world = Integer.valueOf(parts[4]);
        }
        AutomateRSPanel.useWorld = world;
        AutomateRSPanel.boolWorld = useWorld;



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
                if (useWorld && client.getWorld() != world) {
                    prepareLogin();
                } else {
                    login(client);
                }
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
                    init(client);
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
                    init(client);
                }
            }
        });

        add(labelWrapper, BorderLayout.CENTER);

    }



    private void prepareLogin() {
        if (useWorld && thisClient.getWorld() != world) {
            thisClient.loadWorlds();
        } else {
            thisClient.promptCredentials(false);
        }
    }

    public static void init(Client client) {
        login(client);
    }
    private static void login(Client client) {
        client.setUsername(loginText);
        client.setPassword(password);
        Keyboard.sendEnter();
        Keyboard.sendEnter();
    }
}
