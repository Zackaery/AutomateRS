package net.automaters.overlay.panel.auto_login;

import net.automaters.overlay.panel.AutomateRSPanel;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.ui.ColorScheme;
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
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.overlay.panel.AutomateRSPanel.*;
import static net.automaters.script.Variables.guiStarted;
import static net.automaters.util.file_managers.FileManager.PATH_AUTOMATE_RS_RESOURCES;
import static net.automaters.util.file_managers.IconManager.*;
import static net.unethicalite.api.game.Game.logout;
import static net.automaters.script.Variables.scriptStarted;

public class ProfilePanel extends JPanel {

    public static String profileName = null;
    private static String loginText = null;
    private static String password = null;
    private static Boolean useWorld = null;
    private static Integer world = null;
    private static JLabel startLabel;
    private static JLabel deleteLabel;
    static ImageIcon LOADING_ICON = new ImageIcon(PATH_AUTOMATE_RS_RESOURCES+ "util" +File.separator+ "loading_spinner.gif");
    Client thisClient;

    public ProfilePanel(final Client client, final String data, final AutomateRSPanel parent)
    {
        startLabel = new JLabel(START_ICON);
        deleteLabel = new JLabel(DELETE_ICON);
        thisClient = client;
        String[] parts = data.split(":", 5);
        profileName = parts[0];
        loginText = parts[1];
        if (parts.length == 5)
        {
            password = parts[2];
            useWorld = Boolean.valueOf(parts[3]);
            world = Integer.valueOf(parts[4]);
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

        JPanel panelActions = new JPanel(new BorderLayout(8, 0));
        panelActions.setBorder(new EmptyBorder(0, 0, 0, 8));
        panelActions.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        accountsAdded = true;
        deleteLabel.setIcon(set("util\\delete.png"));
        deleteLabel.setToolTipText("Delete - "+profileName);
        deleteLabel.addMouseListener(new MouseAdapter()
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
                deleteLabel.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                deleteLabel.setIcon(DELETE_HOVER_ICON);
                deleteLabel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                deleteLabel.setIcon(DELETE_ICON);
                deleteLabel.repaint();
            }
        });

        startLabel.setIcon(START_ICON);
        startLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e) {
                if (startLabel.getIcon() == START_ICON || startLabel.getIcon() == START_HOVER_ICON) {
                    if (useWorld && client.getWorld() != world) {
                        startLabel.setIcon(LOADING_ICON);
                        prepareLogin();
                    } else {
                        startLabel.setIcon(LOADING_ICON);
                        login(client);
                    }
                }
                if (startLabel.getIcon() == STOP_HOVER_ICON) {
                    startLabel.setIcon(LOADING_ICON);
                    scriptStarted = false;
                    guiStarted = false;
                    logout();
                }
                startLabel.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if (startLabel.getIcon() == START_ICON) {
                    startLabel.setIcon(START_HOVER_ICON);
                    startLabel.setToolTipText("Login - "+profileName);
                }
                if (startLabel.getIcon() == STOP_ICON) {
                    startLabel.setIcon(STOP_HOVER_ICON);
                    startLabel.setToolTipText("Logout - "+profileName);
                }
                if (startLabel.getIcon() == LOADING_ICON) {
                    startLabel.setIcon(LOADING_ICON);
                    startLabel.setToolTipText("Logging in - "+profileName);
                }
                startLabel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                displayProfileStatus(client);
            }
        });

        panelActions.add(startLabel, BorderLayout.CENTER);
        panelActions.add(deleteLabel, BorderLayout.EAST);

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

    public static void displayProfileStatus(Client client) {
        if (client != null) {
            if (client.getGameState() == GameState.LOGIN_SCREEN || client.getGameState() == GameState.LOGIN_SCREEN_AUTHENTICATOR) {
                startLabel.setIcon(START_ICON);
            } else if (client.getGameState() == GameState.LOGGING_IN || client.getGameState() == GameState.LOADING) {
                startLabel.setIcon(LOADING_ICON);
            } else {
                startLabel.setIcon(STOP_ICON);
            }
            startLabel.repaint();
        }
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
