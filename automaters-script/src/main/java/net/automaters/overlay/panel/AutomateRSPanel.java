package net.automaters.overlay.panel;

import net.automaters.gui.GUI;
import net.automaters.gui.utils.EventDispatchThreadRunner;
import net.automaters.overlay.panel.auto_login.ProfilePanel;
import net.automaters.script.AutomateRS;
import net.automaters.script.AutomateRSConfig;
import net.automaters.api.client.ui.components.PluginInfoPanel;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.ToggleButton;

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
import java.text.ParseException;
import java.util.Arrays;
import java.util.Base64;

import static net.automaters.api.client.ui.components.InfoPanel.buildLinkPanel;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.gui.GUI.*;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.Variables.*;
import static net.automaters.util.file_managers.FileManager.*;
import static net.automaters.util.file_managers.IconManager.AUTOMATERS_ICON;
import static net.automaters.util.file_managers.IconManager.AUTOMATERS_TITLE;

public class AutomateRSPanel extends PluginPanel {
    @Inject
    @Nullable
    private Client client;

    @Inject
    ConfigManager configManager;

    @Inject
    private AutomateRSConfig automateRSConfig;

    public static boolean accountsAdded;

    private final JPanel titlePanel = new JPanel();
    private final JPanel scriptPanel = new JPanel();

    private final JLabel titleLabel = new JLabel(AUTOMATERS_TITLE);

    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JButton pauseButton = new JButton("Pause");


    private final PluginInfoPanel loginProfileTitle = new PluginInfoPanel();
    private final PluginInfoPanel noAccountsTitle = new PluginInfoPanel();
    private final PluginInfoPanel updateTitle = new PluginInfoPanel();
    private final PluginInfoPanel updatedTitle = new PluginInfoPanel();
    private final PluginInfoPanel restartTitle = new PluginInfoPanel();


    private final String PROFILE_NAME = "Profile Name";

    private final SpinnerModel model = new SpinnerNumberModel(301, 301, 578, 1);
    private final JPanel updatePanel = new JPanel();
    private final JPanel updateActionsContainer = new JPanel();
    private final JPanel loginPanel = new JPanel();
    private final JPanel accountPanel = new JPanel();
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

    public static boolean selectWorldBool;

    private boolean addUpdateButton;
    private final JButton updateButton = new JButton("Update Now");

    public static int useWorld;
    public static boolean boolWorld;
    private static final int iterations = 100000;
    private static GUI GUI;

    AutomateRS automateRS = new AutomateRS();

    public void refreshPanel() {
        removeAll();
        repaint();
        revalidate();
        try {
            init();
        } catch (IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException |
                 InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
    public void init() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        titlePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        titlePanel.setBorder(new EmptyBorder(10, 5, 10, 0));
        titlePanel.setLayout(new DynamicGridLayout(0, 1, 0, 0));

        updatePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        updatePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        updatePanel.setLayout(new DynamicGridLayout(0, 1, 0, 5));

        updateActionsContainer.setBorder(new EmptyBorder(10, 0, 0, 0));
        updateActionsContainer.setLayout(new GridLayout(0, 1, 0, 10));

        loginPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        loginPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        loginPanel.setLayout(new DynamicGridLayout(11, 1, 0, 5));

        accountPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        accountPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        accountPanel.setLayout(new DynamicGridLayout(0, 1, 0, 5));

        scriptPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        scriptPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        scriptPanel.setLayout(new DynamicGridLayout(3, 1, 0, 5));

        // --- TITLE IMAGE PANEL ---
        {
            // --- SET COMPONENTS ---

            // --- ADD COMPONENTS ---
            {
                titlePanel.add(titleLabel, BorderLayout.CENTER);
            }
        }

        // --- UPDATE PANEL ---
        {
            // --- SET COMPONENTS ---
            {
                updateActionsContainer.add(buildLinkPanel(AUTOMATERS_ICON, "Download the newest update on our", "Discord server", URL_DISCORD_DOWNLOAD));
                updateTitle.setContent("Update Available",
                        "Please click the panel below to download the newest update.");
                updatedTitle.setContent("Up to Date",
                        "No updates are available. You already have the latest version.");
                restartTitle.setContent("Please Import & Restart Devious",
                        "Please import your newest download to: \n" + PATH_PLUGINS_FOLDER + "\n Restart Devious Client once completed.");

                /**
                 *  --- add in logic here to check github for updates
                 */

                try {
                    if (shouldUpdate()) {
                        debug("Update Available.");
                        addUpdateButton = true;
                        repaint();
                        revalidate();
                    } else {
                        debug("You're on the latest update.");
                        repaint();
                        revalidate();
                    }
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
                updateButton.addActionListener(e -> {
                    downloadUpdate();
                    updatePanel.removeAll();
                    updatePanel.add(restartTitle, BorderLayout.NORTH);
                    removeAll();
                    add(titlePanel);
                    add(updatePanel);
                    repaint();
                    revalidate();
                    if (!automateRSConfig.hideRestartPopup()) {
                        Object[] options = {"OK", "Don't show again"};
                        int result = JOptionPane.showOptionDialog(null,
                                "You've successfully downloaded the newest update. \n \n " +
                                        "Please relaunch the Devious client.",
                                "Update successfully downloaded", JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options,
                                options[0]);
                        if (result == 1) {
                            // User clicked "Don't show again" button
                            configManager.setConfiguration("automaters", "hideRestartPopup", true);
                        }
                    }
                });
            }

            // --- ADD COMPONENTS ---
            {
                if (addUpdateButton) {
                    updatePanel.add(updateTitle, BorderLayout.NORTH);
                    removeAll();
                    add(titlePanel);
                    add(updatePanel);
                    add(updateActionsContainer);
                } else {
                    updatePanel.add(updatedTitle, BorderLayout.NORTH);
                }
            }
        }

        // --- LOGIN PANEL ---
        {
            // --- SET COMPONENTS ---
            {
                loginProfileTitle.setContent("Add an account",
                        "Input your account details below, then click save to have your account added in your saved profiles.");
                profileLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
                profileLabel.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (profileLabel.getText().equals(PROFILE_NAME)) {
                            profileLabel.setText("");
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (profileLabel.getText().isEmpty()) {
                            profileLabel.setText(PROFILE_NAME);
                        }
                    }
                });

                selectWorld.addActionListener(e -> {
                    if (!selectWorldBool) {
                        loginPanel.remove(saveProfile);
                        loginPanel.add(worldLabel);
                        loginPanel.add(selectedWorld);
                        loginPanel.add(saveProfile);
                        selectWorldBool = true;
                        useWorld = 301;
                        loginPanel.repaint();
                        loginPanel.revalidate();
                    } else {
                        loginPanel.remove(worldLabel);
                        loginPanel.remove(selectedWorld);
                        selectWorldBool = false;
                        loginPanel.repaint();
                        loginPanel.revalidate();
                    }
                });

                selectedWorld.addChangeListener(e -> {
                    useWorld = Integer.parseInt(selectedWorld.getValue().toString());
                });

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
                    data = profileText + ":" + usernameText + ":" + passwordText + ":" + selectWorldBool + ":" + useWorld;
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
            }

            // --- ADD COMPONENTS ---
            {
                loginPanel.add(loginProfileTitle);
                loginPanel.add(profileLabel);
                loginPanel.add(usernameLabel);
                loginPanel.add(usernameField);
                loginPanel.add(passwordLabel);
                loginPanel.add(passwordField);
                loginPanel.add(selectWorld);
                loginPanel.add(saveProfile);
            }
        }

        // --- ACCOUNT PANEL ---
        {
            // --- SET COMPONENTS ---
            {
                noAccountsTitle.setContent("No profiles saved",
                        "You do not have any profiles saved, please add a new profile from the panel above.");
            }
            // --- ADD COMPONENTS ---
            {
                accountPanel.add(noAccountsTitle);
            }
        }

        // --- SCRIPT PANEL ---
        {
            // --- SET COMPONENTS ---
            {
                startButton.addMouseListener(new MouseAdapter()
                {

                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        if (client != null && client.getGameState() == GameState.LOGGED_IN) {
                            if (localPlayer == null) {
                            } else if (!guiStarted) {
                            GUI.selectedBuild = loadBuildFromGUI();
                                selectedBuild = "ALPHA_TESTER";
                                guiStarted = true;
//                                AutomateRS.scriptStarted = true;
                                scriptPanel.remove(startButton);
                                scriptPanel.add(pauseButton);
                                scriptPanel.add(stopButton);
                                scriptPanel.repaint();
                                scriptPanel.revalidate();
                                scriptTimer = (System.currentTimeMillis() - elapsedTime);
                            } else {
                                selectedBuild = "ALPHA_TESTER";
                                scriptStarted = true;
                                scriptPanel.remove(startButton);
                                scriptPanel.add(pauseButton);
                                scriptPanel.add(stopButton);
                                scriptPanel.repaint();
                                scriptPanel.revalidate();
                                scriptTimer = (System.currentTimeMillis() - elapsedTime);
                                debug("Started - AutomateRS");
                            }
                        } else {
                            scriptTimer = 0;
                            elapsedTime = 0;
                            JOptionPane.showMessageDialog(null, "You're not logged in, please login before starting the plugin.", "Starting... AutomateRS", JOptionPane.ERROR_MESSAGE);
                            debug("You're not logged in.");
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
                        scriptPanel.remove(pauseButton);
                        scriptPanel.remove(stopButton);
                        scriptPanel.add(startButton);
                        scriptPanel.repaint();
                        scriptPanel.revalidate();
                        automateRS.stop();
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
            }
            // --- ADD COMPONENTS ---
            {
                scriptPanel.add(startButton, BorderLayout.CENTER);
            }
        }

        // --- ADD COMPONENTS TO MAIN PANEL --
        {
            if (!addUpdateButton || automateRSConfig.alwaysShowPanel()) {
                add(titlePanel, BorderLayout.NORTH);
                add(updatePanel, BorderLayout.CENTER);
                if (addUpdateButton) {
                    add(updateActionsContainer);
                }
                add(loginPanel, BorderLayout.CENTER);
                decryptAccounts();
                add(accountPanel, BorderLayout.CENTER);
                add(scriptPanel, BorderLayout.SOUTH);
            }
        }

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
        debug("profile = "+data);
        ProfilePanel profile = new ProfilePanel(client, data, automateRSConfig, this);
        accountPanel.add(profile);
        revalidate();
        repaint();
    }

    private void addAccounts(String data)
    {
        data = data.trim();
        if (!data.contains(":"))
        {
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

    public static String loadBuildFromGUI() {
        try {
            EventDispatchThreadRunner.runOnDispatchThread(() -> {
                try {
                    GUI = new GUI();
                    GUI.start();
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

        if (guiStarted) {
            debug("SELECTED BUILD = " + GUI.selectedBuild);
            return GUI.selectedBuild;
        } else {
            return null;
        }
    }

}
