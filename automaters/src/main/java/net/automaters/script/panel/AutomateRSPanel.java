package net.automaters.script.panel;

import net.automaters.gui.GUI;
import net.automaters.gui.utils.EventDispatchThreadRunner;
import net.automaters.script.AutomateRSConfig;
import net.automaters.script.panel.auto_login.ProfilePanel;
import net.automaters.util.api.client.ui.components.PluginInfoPanel;
import net.runelite.api.Client;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.ToggleButton;
import net.unethicalite.api.entities.Players;

import javax.annotation.Nullable;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import static net.automaters.gui.GUI.*;
import static net.automaters.gui.GUI.started;
import static net.automaters.script.AutomateRS.debug;
import static net.automaters.script.AutomateRS.scriptStarted;

public class AutomateRSPanel extends PluginPanel {
    @Inject
    @Nullable
    private Client client;

    @Inject
    private AutomateRSConfig automateRSConfig;

    public static boolean accountsAdded;

    private final JPanel container = new JPanel();
    private final JPanel scriptPanel = new JPanel();

    private final JLabel titleLabel = new JLabel();

    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JButton pauseButton = new JButton("Pause");


    private final PluginInfoPanel loginProfileTitle = new PluginInfoPanel();
    private final PluginInfoPanel noAccountsTitle = new PluginInfoPanel();

    private final String PROFILE_NAME = "Profile Name";

    private final SpinnerModel model = new SpinnerNumberModel(301, 301, 578, 1);
    private final JPanel loginPanel = new JPanel();
    private final JPanel accountPanel = new JPanel();
    private final JPanel worldPanel = new JPanel();
    private final JTextField profileLabel = new JTextField(PROFILE_NAME);
    private final JLabel usernameLabel = new JLabel("Username:");
    private final JLabel passwordLabel = new JLabel("Password:");
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton saveProfile = new JButton("Save profile");

    private final ToggleButton selectWorld = new ToggleButton("Select world");
    private final JSpinner selectedWorld = new JSpinner(model);
    private final JLabel worldLabel = new JLabel("World:");
    private final ToggleButton saveLastWorld = new ToggleButton("Save last world");

    private static boolean selectWorldBool;

    private static final int iterations = 100000;
    private GUI GUI;

    public void init() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        container.setBorder(new EmptyBorder(10, 5, 10, 0));
        container.setLayout(new DynamicGridLayout(0, 1, 0, 0));

        scriptPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        scriptPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        scriptPanel.setLayout(new DynamicGridLayout(3, 1, 0, 5));

        setImage("resources/net.automaters.script/panel/AutomateRS.png", titleLabel);

        container.add(titleLabel, BorderLayout.CENTER);
        scriptPanel.add(startButton, BorderLayout.CENTER);

        loginPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        loginPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        loginPanel.setLayout(new DynamicGridLayout(7, 1, 0, 5));

        accountPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        accountPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        accountPanel.setLayout(new DynamicGridLayout(0, 1, 0, 5));

        worldPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        worldPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        worldPanel.setLayout(new DynamicGridLayout(4, 1, 0, 5));

        profileLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        profileLabel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e)
            {
                if (profileLabel.getText().equals(PROFILE_NAME))
                {
                    profileLabel.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e)
            {
                if (profileLabel.getText().isEmpty())
                {
                    profileLabel.setText(PROFILE_NAME);
                }
            }
        });

        loginProfileTitle.setContent("Add an account",
                "Input your account details below, then click save to have your account added in your saved profiles.");
        loginPanel.add(loginProfileTitle);
        loginPanel.add(profileLabel);
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(saveProfile);

        noAccountsTitle.setContent("No profiles saved",
                "You do not have any profiles saved, please add a new profile from the panel above.");
        accountPanel.add(noAccountsTitle);

        worldPanel.add(selectWorld);

        add(container, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);
        add(accountPanel, BorderLayout.CENTER);
        add(worldPanel, BorderLayout.CENTER);
        add(scriptPanel, BorderLayout.SOUTH);

        saveProfile.addActionListener(e -> {
            accountPanel.remove(noAccountsTitle);
            String profileText = String.valueOf(profileLabel.getText());
            String usernameText = String.valueOf(usernameField.getText());
            String passwordText = String.valueOf(passwordField.getPassword());

            if (profileLabel.equals(PROFILE_NAME) || usernameField.equals("")) {
                return;
            }
            if ((profileLabel.getText() == ":") || (usernameField.getText() == ":")) {
                JOptionPane.showMessageDialog(null, "You may not use colons in your label or login name", "Account Switcher", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String data;
            data = profileText + ":" + usernameText + ":" + passwordText;
            try
            {
                if (!addProfile(data))
                {
                    return;
                }

                redrawProfiles();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            {
                debug(e.toString());
            }
        });

        selectWorld.addActionListener(e -> {
            if (!selectWorldBool) {
                worldPanel.add(worldLabel);
                worldPanel.add(selectedWorld);
                worldPanel.add(saveLastWorld);
                selectWorldBool = true;
                worldPanel.repaint();
                worldPanel.revalidate();
            } else {
                worldPanel.remove(worldLabel);
                worldPanel.remove(selectedWorld);
                worldPanel.remove(saveLastWorld);
                selectWorldBool = false;
                worldPanel.repaint();
                worldPanel.revalidate();
            }
        });

        startButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                var local = Players.getLocal();
                if (local == null) {
                    debug("Local Player not located");
                    return;
                }
                if (!started) {
                    selectedBuild = loadBuildFromGUI();
                    scriptPanel.remove(startButton);
                    scriptPanel.add(pauseButton);
                    scriptPanel.add(stopButton);
                    scriptPanel.repaint();
                    scriptPanel.revalidate();
                } else {
                    scriptStarted = true;
                    scriptPanel.remove(startButton);
                    scriptPanel.add(pauseButton);
                    scriptPanel.add(stopButton);
                    scriptPanel.repaint();
                    scriptPanel.revalidate();
                    debug("Started - AutomateRS");
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                startButton.setBackground(new Color(60, 60, 60));
                startButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                startButton.setBackground(null);
                startButton.repaint();
            }
        });

        pauseButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                scriptStarted = false;
                scriptPanel.remove(pauseButton);
                scriptPanel.add(startButton);
                scriptPanel.add(stopButton);
                scriptPanel.repaint();
                scriptPanel.revalidate();
                debug("Paused - AutomateRS");
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                pauseButton.setBackground(new Color(60, 60, 60));
                pauseButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                pauseButton.setBackground(null);
                pauseButton.repaint();
            }
        });

        stopButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                started = false;
                scriptStarted = false;
                scriptPanel.remove(pauseButton);
                scriptPanel.remove(stopButton);
                scriptPanel.add(startButton);
                scriptPanel.repaint();
                scriptPanel.revalidate();
                debug("Stopped - AutomateRS");
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                stopButton.setBackground(new Color(60, 60, 60));
                stopButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                stopButton.setBackground(null);
                stopButton.repaint();
            }
        });

        decryptAccounts();
        repaint();
        revalidate();
    }

    private void decryptAccounts() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        redrawProfiles();
        accountPanel.setLayout(new DynamicGridLayout(0, 1, 0, 3));
        add(accountPanel, BorderLayout.CENTER);
        if (!accountsAdded) {
            accountPanel.add(noAccountsTitle);
        }
    }

    void redrawProfiles() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        accountPanel.removeAll();
        addAccounts(getProfileData());
        revalidate();
        repaint();
    }

    private void addAccount(String data)
    {
        ProfilePanel profile = new ProfilePanel(client, data, automateRSConfig, this);
        accountPanel.add(profile);
        revalidate();
        repaint();
    }

    private void addAccounts(String data)
    {
//        debug("data = "+ data);
        data = data.trim();
        if (!data.contains(":"))
        {
            accountsAdded = true;
            return;
        }
        Arrays.stream(data.split("\\n")).forEach(this::addAccount);
    }

    private boolean addProfile(String data) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        return setProfileData(
                getProfileData() + data + "\n");
    }

    public void removeProfile(String data) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        setProfileData(
                getProfileData().replaceAll(data + "\\n", ""));
        revalidate();
        repaint();

    }

    private void setSalt(byte[] bytes)
    {
        automateRSConfig.salt(base64Encode(bytes));
    }

    private byte[] getSalt()
    {
        if (automateRSConfig.salt().length() == 0)
        {
            return new byte[0];
        }
        return base64Decode(automateRSConfig.salt());
    }

    private SecretKey getAesKey() throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        if (getSalt().length == 0)
        {
            byte[] b = new byte[16];
            SecureRandom.getInstanceStrong().nextBytes(b);
            setSalt(b);
        }
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec("test".toCharArray(), getSalt(), iterations, 128);
        return factory.generateSecret(spec);
    }

    private String getProfileData() throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {
        String tmp = automateRSConfig.profilesData();
        if (tmp.startsWith("¬"))
        {
            tmp = tmp.substring(1);
            return decryptText(base64Decode(tmp), getAesKey());
        }
        return tmp;
    }

    private boolean setProfileData(String data) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {
        byte[] enc = encryptText(data, getAesKey());
        if (enc.length == 0)
        {
            return false;
        }
        String s = "¬" + base64Encode(enc);
        automateRSConfig.profilesData(s);
        return true;
    }

    private byte[] base64Decode(String data)
    {
        return Base64.getDecoder().decode(data);
    }

    private String base64Encode(byte[] data)
    {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Encrypts login info
     *
     * @param text text to encrypt
     * @return encrypted string
     */
    private static byte[] encryptText(String text, SecretKey aesKey) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException
    {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec newKey = new SecretKeySpec(aesKey.getEncoded(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, newKey);
        return cipher.doFinal(text.getBytes());
    }

    private static String decryptText(byte[] enc, SecretKey aesKey) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException
    {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec newKey = new SecretKeySpec(aesKey.getEncoded(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, newKey);
        return new String(cipher.doFinal(enc));
    }

    private static void showErrorMessage(String title, String text)
    {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(ClientUI.getFrame(),
                text,
                title,
                JOptionPane.ERROR_MESSAGE));
    }

    private static String htmlLabel(String text)
    {
        return "<html><body><span style = 'color:white'>" + text + "</span></body></html>";
    }

    @Override
    public void onActivate()
    {
        super.onActivate();
        titleLabel.requestFocusInWindow();
    }

    private String loadBuildFromGUI() {
        try {
            EventDispatchThreadRunner.runOnDispatchThread(() -> {
                try {
                    GUI = new GUI();
                    GUI.open();
                    debug("Launching AutomateRS - GUI");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, true);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (started) {
            debug("SELECTED BUILD = " + selectedBuild);
            return selectedBuild;
        } else {
            return null;
        }
    }

}
