package net.automaters.gui.main.tabs;

import javax.swing.*;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static net.automaters.AutomateManager.managerWidth;
import static net.automaters.api.panels.findJComponent.*;
import static net.automaters.api.panels.readContents.readFileContents;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.utils.file_managers.ProcessManager.getProcessPIDs;
import static net.automaters.gui.main.MainComponents.*;

public class Run {

    public static JPanel panelRunBotList = new JPanel();
    public static JPanel panelRunButton = new JPanel();

    public static JButton buttonStart = new JButton("Start bot(s)");
    public static JButton buttonStop = new JButton("Stop Bot(s)");

    private static final Path managerPath = Paths.get(System.getProperty("user.home"),
            ".openosrs", "data", "AutomateRS", "Manager");


    public Run() {
        initRunComponents();
    }

    private void initRunComponents() {

        scrollPane.setViewportView(panelRunBotList);

        panelRunBotList.setLayout(new BoxLayout(panelRunBotList, BoxLayout.Y_AXIS));
        panelRunButton.setLayout(null);

        panelRunButton.setBounds(0, 200, managerWidth, 100);
        buttonStart.setBounds(420, 32, 90, 23);
        buttonStop.setBounds(520, 32, 90, 23);

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

        panelRunButton.add(buttonStart);
        panelRunButton.add(buttonStop);

        tabRun.add(scrollPane);
        tabRun.add(panelRunButton);

    }

    private void performAction(Boolean start) throws IOException, InterruptedException {
        List<String> pids = getProcessPIDs();
        debug("after getting pids");

        // Loop through bot rows
        for (Component component : panelRunBotList.getComponents()) {

            debug("inside for component loop");
            if (component instanceof JPanel) {
                debug("component is instance of JPanel");
                JPanel botRowPanel = (JPanel) component;

                JCheckBox checkbox = findCheckbox(botRowPanel);
                if (checkbox != null && checkbox.isSelected()) {
                    if (start) {
                        if (shouldStartBot(botRowPanel, pids)) {
                            debug("should start bot");
                            startBot(botRowPanel);
                        } else {
                            debug("should not start bot");
                        }
                    } else {
                        if (shouldStopBot(botRowPanel, pids)) {
                            debug("should stop bot");
                            stopBot(botRowPanel);
                        } else {
                            debug("should not stop bot");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Bot not selected!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private static boolean shouldStartBot(JPanel botRowPanel, List<String> pids) throws IOException {

        JLabel botNameLabel = findLabel(botRowPanel);
        String botName = botNameLabel.getText().trim();
        String botNameSub = botName.split("@")[0].trim();
        Path pidFilePath = Path.of(String.valueOf(managerPath), botNameSub, "pidlist.txt");

        if (botName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Bot name is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return false; // Bot name is empty
        }

        if (Files.exists(pidFilePath)) {
            String fileContent = readFileContents(pidFilePath);
            if (pids.contains(fileContent.trim())) {
                JOptionPane.showMessageDialog(null, botName + " already running!");
                return false; // Bot is already running
            }
        }

        JOptionPane.showMessageDialog(null, botName + " starting bot!");
        return true; // Start the bot
    }

    private static void startBot(JPanel botRowPanel) throws IOException, InterruptedException {

        debug("starting bot");
        JComboBox<String> scriptDropdown = findDropdown(botRowPanel, "scriptDropdown");
        JComboBox<String> profileDropdown = findDropdown(botRowPanel, "profileDropdown");
        JComboBox<String> proxyDropdown = findDropdown(botRowPanel, "proxyDropdown");
        JComboBox<String> worldDropdown = findDropdown(botRowPanel, "worldDropdown");

        String scriptValue = scriptDropdown.getSelectedItem().toString();
        String profileValue = profileDropdown.getSelectedItem().toString();
        String proxyValue = proxyDropdown.getSelectedItem().toString();
        String worldValue = worldDropdown.getSelectedItem().toString();

        if ("Script".equals(scriptValue)) {
            JOptionPane.showMessageDialog(null, "No script selected", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Script not selected, don't start the bot
        }

        String botName = findLabel(botRowPanel).getText().trim();
        String botNameSub = botName.split("@")[0].trim();
        String command = buildCommand(botName, scriptValue, worldValue, profileValue, proxyValue);
        Path pidFilePath = Path.of(String.valueOf(managerPath), botNameSub, "pidlist.txt");

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

    private boolean shouldStopBot(JPanel botRowPanel, List<String> pids) throws IOException {

        JLabel botNameLabel = findLabel(botRowPanel);
        String botName = botNameLabel.getText().trim();
        String botNameSub = botName.split("@")[0].trim();
        Path pidFilePath = Path.of(String.valueOf(managerPath), botNameSub, "pidlist.txt");

        if (Files.exists(pidFilePath)) {
            String fileContent = readFileContents(pidFilePath);
            if (pids.contains(fileContent.trim())) {
                return true; // Bot should be stopped
            }
        }

        return false; // Bot should not be stopped
    }

    private void stopBot(JPanel botRowPanel) {

        JLabel botNameLabel = findLabel(botRowPanel);
        String botName = botNameLabel.getText().trim();
        String botNameSub = botName.split("@")[0].trim();
        Path pidFilePath = Path.of(String.valueOf(managerPath), botNameSub, "pidlist.txt");

        try {
            String fileContent = readFileContents(pidFilePath);
            ProcessBuilder processBuilder = new ProcessBuilder("taskkill", "/F", "/PID", fileContent.trim());
            Process stopProcess = processBuilder.start();
            stopProcess.waitFor();
            JOptionPane.showMessageDialog(null, "Bot " + botName + " stopped successfully!");
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to stop " + botName + ".", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String buildCommand(String botName, String scriptValue, String worldValue, String profileValue, String proxyValue) {
        // Specify the path to the JAR file you want to launch
        String jarFilePath = "C:\\Users\\Lindz\\IdeaProjects\\devious-client\\runelite-client\\build\\libs\\runelite-client-1.0.20-EXPERIMENTAL-shaded.jar"; // Update with the actual path

        // Create the base command
        StringBuilder command = new StringBuilder("java -jar ");
        command.append(jarFilePath);

        // Add bot-specific parameters
        command.append(" --account ").append(botName);
        command.append(" --script \"").append(scriptValue).append("\"");

        // Add optional parameters
        if (!"World".equals(worldValue)) {
            try {
                int intValue = Integer.parseInt(worldValue);
                command.append(" --world ").append(intValue);
            } catch (NumberFormatException e) {
                // Handle the error or log a message
            }
        }
        if (!"Profile".equals(profileValue)) {
            command.append(" --scriptargs ").append(profileValue);
        }
        if (!"Proxy".equals(proxyValue)) {
            command.append(" --proxy ").append(proxyValue);
        }

        return command.toString();
    }

}
