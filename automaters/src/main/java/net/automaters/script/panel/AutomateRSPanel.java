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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private final JPanel titlePanel = new JPanel();
    private final JPanel scriptPanel = new JPanel();

    private final JLabel titleLabel = new JLabel();

    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JButton pauseButton = new JButton("Pause");


    private final PluginInfoPanel loginProfileTitle = new PluginInfoPanel();
    private final PluginInfoPanel noAccountsTitle = new PluginInfoPanel();
    private final PluginInfoPanel updateTitle = new PluginInfoPanel();
    private final PluginInfoPanel profileTitle = new PluginInfoPanel();

    private final String PROFILE_NAME = "Profile Name";

    private final SpinnerModel model = new SpinnerNumberModel(301, 301, 578, 1);
    private final JPanel updatePanel = new JPanel();
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
    private static final int iterations = 100000;
    private GUI GUI;

    public void init() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        titlePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        titlePanel.setBorder(new EmptyBorder(10, 5, 10, 0));
        titlePanel.setLayout(new DynamicGridLayout(0, 1, 0, 0));

        updatePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        updatePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        updatePanel.setLayout(new DynamicGridLayout(0, 1, 0, 5));

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
            {
                setImage("resources/net.automaters.script/panel/AutomateRS.png", titleLabel);
            }
            // --- ADD COMPONENTS ---
            {
                titlePanel.add(titleLabel, BorderLayout.CENTER);
            }
        }

        // --- UPDATE PANEL ---
        {
            // --- SET COMPONENTS ---
            {
                updateTitle.setContent("Update Available",
                        "Please click the button below to download the newest update.");
                /**
                 *  --- add in logic here to check github for updates
                 */

                // if need to update {
                addUpdateButton = true;
                //}

                updateButton.addActionListener(e -> {

                    // Local file path
                    String username = System.getProperty("user.home");
                    String directoryPath = username + File.separator + ".openosrs" + File.separator + "plugins";
                    String baseFileName = "automaters";

                    // Find the latest version of the file in the directory
                    File directory = new File(directoryPath);
                    File[] matchingFiles = directory.listFiles((dir, name) -> name.startsWith(baseFileName));
                    String latestVersion = "0.0.0"; // Initialize with a low version

                    if (matchingFiles != null) {
                        Pattern versionPattern = Pattern.compile(baseFileName + "-(\\d+\\.\\d+\\.\\d+)\\.jar");
                        for (File file : matchingFiles) {
                            String fileName = file.getName();
                            Matcher matcher = versionPattern.matcher(fileName);
                            if (matcher.matches()) {
                                String version = matcher.group(1);
                                if (version.compareTo(latestVersion) > 0) {
                                    latestVersion = version;
                                }
                            }
                        }
                    }

                    // Increment the latest version by 1 here
                    String[] versionParts = latestVersion.split("\\.");
                    int major = Integer.parseInt(versionParts[0]);
                    int minor = Integer.parseInt(versionParts[1]);
                    int patch = Integer.parseInt(versionParts[2]);
                    patch++;

                    // Construct the new version string
                    latestVersion = String.format("%d.%d.%d", major, minor, patch);

                    // Construct the final localFilePath
                    String localFilePath = directoryPath + File.separator + baseFileName + "-" + latestVersion + ".jar";

                    System.out.println("Latest Version: " + latestVersion);
                    System.out.println("Local File Path: " + localFilePath);
                    String githubRawURL = "https://raw.githubusercontent.com/Zackaery/Account-Builder/main/automaters-0.0.1.jar";

                    try {
                        File localFile = new File(localFilePath);
                        URL githubURL = new URL(githubRawURL);

                        long githubLastModified = githubURL.openConnection().getDate();
                        long localLastModified = localFile.lastModified();

                        System.out.println("Local File Last Modified: " + localLastModified);
                        System.out.println("GitHub File Last Modified: " + githubLastModified);

                        if (githubLastModified > localLastModified) {
                            // Download the updated file
                            try (InputStream in = githubURL.openStream()) {
                                Path tempFile = Files.createTempFile("automaters-0.0.1", ".jar");
                                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);

                                // Replace the old file with the updated file
                                Files.move(tempFile, localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                System.out.println("File updated to: " + localFile.getName() + " Please restart the client!");
                                JOptionPane.showMessageDialog(null, "File updated: " + localFile.getName() + " Please restart the client!", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Failed to update file.", "Update Failed", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            System.out.println("File is up to date.");
                            JOptionPane.showMessageDialog(null, "File is up to date.", "No Updates", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Failed to check for updates.", "Update Check Failed", JOptionPane.ERROR_MESSAGE);
                    }



                });

            }
            // --- ADD COMPONENTS ---
            {
                if (addUpdateButton) {
                    updatePanel.add(updateTitle, BorderLayout.NORTH);
                    updatePanel.add(updateButton, BorderLayout.CENTER);
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
            }
            // --- ADD COMPONENTS ---
            {
                scriptPanel.add(startButton, BorderLayout.CENTER);
            }
        }

        // --- ADD COMPONENTS TO MAIN PANEL --
        {
            add(titlePanel, BorderLayout.NORTH);
            if (addUpdateButton) {
                add(updatePanel, BorderLayout.CENTER);
            }
            add(loginPanel, BorderLayout.CENTER);
            decryptAccounts();
            add(accountPanel, BorderLayout.CENTER);
            add(scriptPanel, BorderLayout.SOUTH);
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
