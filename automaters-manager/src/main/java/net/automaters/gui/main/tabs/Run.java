package net.automaters.gui.main.tabs;

import net.automaters.api.ui.DynamicGridLayout;
import net.automaters.api.panels.InfoPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.awt.BorderLayout.CENTER;
import static net.automaters.AutomateManager.managerWidth;
import static net.automaters.api.panels.FindJComponent.*;
import static net.automaters.api.panels.ReadContents.readFileContents;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.utils.file_managers.IconManager.*;
import static net.automaters.api.utils.file_managers.ProcessManager.getProcessPIDs;
import static net.automaters.gui.main.MainComponents.*;
import static net.unethicalite.api.game.Game.logout;

public class Run {

    public static JPanel panelRunInfo = new JPanel();
    private final InfoPanel infoPanel = new InfoPanel();

    public static JButton buttonStart = new JButton("Start Account(s)");
    public static JButton buttonStop = new JButton("Stop Account(s)");

    public static JLabel start;

    private static final Path managerPath = Paths.get(System.getProperty("user.home"),
            ".openosrs", "data", "AutomateRS", "Manager");


    public Run() {
        initRunComponents();
    }

    private void createInfoPanel(JPanel panel, String title, String description) {
        panel.setLayout(new DynamicGridLayout(2, 1, 0, 0));
        panel.setBounds(0, 0, managerWidth-100, 100);
        infoPanel.setBounds(0, 0, managerWidth, 100);
        infoPanel.setContent(title, description);
        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(new JSeparator(), CENTER);
    }

    private void initRunComponents() {

        JScrollPane scrollPane = new JScrollPane();
        initScrollPane(scrollPane, panelRunAccountList);

        createInfoPanel(panelRunInfo, "Running Accounts", "Select the account(s) you'd like to run.<br>Select your configurations.<br>Press the Start Account(s) button.</html>");

        panelRunAccountList.setLayout(new BoxLayout(panelRunAccountList, BoxLayout.Y_AXIS));
        panelRunComponents.setLayout(null);

        panelRunComponents.setBounds(0, 300, managerWidth, 45);
        buttonStart.setBounds((managerWidth/2)-100-65, 10, 120, 25);
        buttonStop.setBounds((managerWidth/2)-100+65, 10, 120, 25);

        buttonStart.addActionListener(e -> {
            try {
                performAction(true);
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonStop.addActionListener(e -> {
            try {
                performAction(false);
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        panelRunComponents.add(buttonStart);
        panelRunComponents.add(buttonStop);

        tabRun.add(scrollPane);
        tabRun.add(panelRunComponents);
        tabRun.add(panelRunInfo);

    }

    private void performAction(Boolean start) throws IOException, InterruptedException {
        List<String> pids = getProcessPIDs();
        debug("after getting pids");

        // Loop through account rows
        for (Component component : panelRunAccountList.getComponents()) {

            debug("inside for component loop");
            if (component instanceof JPanel) {
                debug("component is instance of JPanel");
                JPanel accountRowPanel = (JPanel) component;

                JCheckBox checkbox = findCheckbox(accountRowPanel);
                if (checkbox != null && checkbox.isSelected()) {
                    if (start) {
                        if (shouldStartAccount(accountRowPanel, pids)) {
                            debug("should start account");
                            startAccount(accountRowPanel);
                        } else {
                            debug("should not start account");
                        }
                    } else {
                        if (shouldStopAccount(accountRowPanel, pids)) {
                            debug("should stop account");
                            stopAccount(accountRowPanel);
                        } else {
                            debug("should not stop account");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Account not selected!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static boolean shouldStartAccount(JPanel accountRowPanel, List<String> pids) throws IOException {

        JLabel accountNameLabel = findLabel(accountRowPanel);
        String accountName = accountNameLabel.getText().trim();
        String accountNameSub = accountName.split("@")[0].trim();
        Path pidFilePath = Path.of(String.valueOf(managerPath), accountNameSub, "pidlist.txt");

        if (accountName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Account name is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return false; // Account name is empty
        }

        if (Files.exists(pidFilePath)) {
            String fileContent = readFileContents(pidFilePath);
            if (pids.contains(fileContent.trim())) {
                JOptionPane.showMessageDialog(null, accountName + " already running!");
                return false; // Account is already running
            }
        }

        JOptionPane.showMessageDialog(null, accountName + " starting account!");
        return true; // Start the account
    }

    public static void startAccount(JPanel accountRowPanel) throws IOException, InterruptedException {

        debug("starting account");
        JComboBox<String> scriptDropdown = findDropdown(accountRowPanel, "scriptDropdown");
        JComboBox<String> profileDropdown = findDropdown(accountRowPanel, "profileDropdown");
        JComboBox<String> proxyDropdown = findDropdown(accountRowPanel, "proxyDropdown");
        JComboBox<String> worldDropdown = findDropdown(accountRowPanel, "worldDropdown");

        String scriptValue = scriptDropdown.getSelectedItem().toString();
        String profileValue = profileDropdown.getSelectedItem().toString();
        String proxyValue = proxyDropdown.getSelectedItem().toString();
        String worldValue = worldDropdown.getSelectedItem().toString();

        if ("Script".equals(scriptValue)) {
            JOptionPane.showMessageDialog(null, "No script selected", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Script not selected, don't start the account
        }

        String accountName = findLabel(accountRowPanel).getText().trim();
        String accountNameSub = accountName.split("@")[0].trim();

        String command = buildCommand(accountName, scriptValue, worldValue, profileValue, proxyValue);
        Path pidFilePath = Path.of(String.valueOf(managerPath), accountNameSub, "pidlist.txt");

        Process process = Runtime.getRuntime().exec(command);
        Thread.sleep(2000);

        if (process.isAlive()) {
            Files.createDirectories(pidFilePath.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(pidFilePath)) {
                writer.write(String.valueOf(process.pid()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            // The process failed to start
        }
    }

    public static boolean shouldStopAccount(JPanel accountRowPanel, List<String> pids) throws IOException {

        JLabel accountNameLabel = findLabel(accountRowPanel);
        String accountName = accountNameLabel.getText().trim();
        String accountNameSub = accountName.split("@")[0].trim();
        Path pidFilePath = Path.of(String.valueOf(managerPath), accountNameSub, "pidlist.txt");

        if (Files.exists(pidFilePath)) {
            String fileContent = readFileContents(pidFilePath);
            if (pids.contains(fileContent.trim())) {
                return true; // Account should be stopped
            }
        }

        return false; // Account should not be stopped
    }

    public static void stopAccount(JPanel accountRowPanel) {

        JLabel accountNameLabel = findLabel(accountRowPanel);
        String accountName = accountNameLabel.getText().trim();
        String accountNameSub = accountName.split("@")[0].trim();
        Path pidFilePath = Path.of(String.valueOf(managerPath), accountNameSub, "pidlist.txt");

        try {
            String fileContent = readFileContents(pidFilePath);
            ProcessBuilder processBuilder = new ProcessBuilder("taskkill", "/F", "/PID", fileContent.trim());
            Process stopProcess = processBuilder.start();
            stopProcess.waitFor();
            JOptionPane.showMessageDialog(null, "Account " + accountName + " stopped successfully!");
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to stop " + accountName + ".", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static String buildCommand(String accountName, String scriptValue, String worldValue, String profileValue, String proxyValue) {
        // Specify the path to the JAR file you want to launch
        String jarFilePath = "C:\\Users\\Lindz\\IdeaProjects\\devious-client\\runelite-client\\build\\libs\\runelite-client-1.0.20-EXPERIMENTAL-shaded.jar"; // Update with the actual path

//        --account=user:pass
//        --proxy=ip:port:user:pass
//        --script=name
//        --scriptArgs=arg1,arg2,...
        // Create the base command
        StringBuilder command = new StringBuilder("java -jar ");
        command.append(jarFilePath);

        // Add account-specific parameters
        command.append(" --account=").append(accountName);
        command.append(" --script=\"").append(scriptValue).append("\"");

        // Add optional parameters
        if (!"World".equals(worldValue)) {
            try {
                int intValue = Integer.parseInt(worldValue);
                command.append(" --world=").append(intValue);
            } catch (NumberFormatException e) {
                // Handle the error or log a message
            }
        }
        if (!"Profile".equals(profileValue)) {
            command.append(" --scriptargs=").append(profileValue);
        }
        if (!"Proxy".equals(proxyValue)) {
            command.append(" --proxy=").append(proxyValue);
        }

        return command.toString();
    }

}
