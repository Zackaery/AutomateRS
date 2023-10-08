package net.automaters.gui.main;

import net.automaters.gui.main.tabs.*;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static net.automaters.AutomateManager.*;
import static net.automaters.api.panels.readContents.*;
import static net.automaters.api.panels.readContents.loadProfileList;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.utils.file_managers.IconManager.AUTOMATERS_TITLE;
import static net.automaters.gui.main.tabs.BotList.*;
import static net.automaters.gui.main.tabs.ProfileList.panelProfileList;
import static net.automaters.gui.main.tabs.ProxyList.panelProxyList;
import static net.automaters.gui.main.tabs.Run.panelRunBotList;
import static net.automaters.gui.main.tabs.ScriptList.panelScriptList;

public class MainComponents {
    public static JLabel labelTitle;
    public static JTabbedPane tabbedPanel;

    public static JScrollPane scrollPane = new JScrollPane();

    public static JPanel mainPanel = new JPanel();
    public static JPanel tabRun = new JPanel();
    public static JPanel tabBotList = new JPanel();
    public static JPanel tabProxyList = new JPanel();
    public static JPanel tabProfileList = new JPanel();
    public static JPanel tabScriptList = new JPanel();
    public static JPanel tabSchedulePanel = new JPanel();

    public MainComponents() {
        addTitle();
        addTabbedPanel();
        createComponents();
    }

    private void addTitle() {

        // --- labelTitle ---
        labelTitle = new JLabel(AUTOMATERS_TITLE);
        labelTitle.setBounds(managerWidth / 2, 10, labelTitle.getWidth(), labelTitle.getHeight());
        labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitle.setFont(labelTitle.getFont().deriveFont(labelTitle.getFont().getSize() + 4f));

    }

    private void addTabbedPanel() {

        // --- tabbedPanel ---
        tabbedPanel = new JTabbedPane();

        scrollPane.setBounds(0, 0, managerWidth, 500);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tabbedPanel.addTab("Start/Stop", null, tabRun, null);
        tabRun.setLayout(null);

        tabbedPanel.addTab("Bot List", null, tabBotList, null);
        tabBotList.setLayout(null);

        tabbedPanel.addTab("Proxy List", null, tabProxyList, null);
        tabProxyList.setLayout(null);

        tabbedPanel.addTab("Profile List", null, tabProfileList, null);
        tabProfileList.setLayout(null);

        tabbedPanel.addTab("Script List", null, tabScriptList, null);
        tabScriptList.setLayout(null);

        tabbedPanel.addTab("Scheduling", null, tabSchedulePanel, null);
        tabSchedulePanel.setLayout(null);
        initComponents();
        debug("scroll pane bounds: "+scrollPane.getBounds());
    }

    private void createComponents() {
        Container contentPane = mainFrame.getContentPane();

        mainFrame.getContentPane().setLayout(null);
        GroupLayout contentPaneLayout = new GroupLayout(contentPane);

        contentPaneLayout.setHorizontalGroup(contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(labelTitle)
                                        .addComponent(tabbedPanel, GroupLayout.PREFERRED_SIZE, 750, Short.MAX_VALUE)
                        ))
                .addComponent(mainPanel));
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelTitle)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tabbedPanel, GroupLayout.PREFERRED_SIZE, 405, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addComponent(mainPanel));
        contentPane.setLayout(contentPaneLayout);

        mainFrame.pack();

        mainFrame.setSize(managerWidth, managerHeight);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        debug("frame.setVisible(true)\n");

    }

    public static void populateBotListActual() {
        System.out.println("populateBotListActual");
        if (panelBotListActual != null) {
            panelBotListActual.removeAll();
        }
        // Show a JOptionPane with the message "We made it here"
        // Inside your populateBotList method or any other suitable location
        JOptionPane.showMessageDialog(mainPanel, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);
        try {

            // Check if the file exists
            if (Files.exists(pathBotFile)) {
                java.util.List<String> botLines = Files.readAllLines(pathBotFile);

                for (String botName : botLines) {
                    if (!botName.trim().isEmpty()) {
                        JPanel botRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned row panel

                        // Checkbox for the bot
                        JCheckBox checkbox = new JCheckBox();

                        // Text field for "offline"
                        JTextField statusTextField = new JTextField("Offline");
                        statusTextField.setEditable(false);

                        // Label for the bot name (read from the file)
                        JLabel nameLabel = new JLabel("   " + botName);

                        // Add components to the row panel
                        botRowPanel.add(checkbox);
                        botRowPanel.add(statusTextField);
                        botRowPanel.add(nameLabel);

                        System.out.println("panelBotListActual.add");
                        // Add the row to the scroll panel
                        panelBotListActual.add(botRowPanel);
                        System.out.println("panelBotListActual BOUNDS = "+panelBotListActual.getBounds());
                    }
                }
            }

            // Refresh the layout of panelBotListActual panel if needed
            panelBotListActual.revalidate();
            panelBotListActual.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Method to populate the bot list
    public static void populateBotList() {

        System.out.println("populateBotList");
        if (panelRunBotList != null) {
            panelRunBotList.removeAll();
        }
        // Show a JOptionPane with the message "We got here"
        // Inside your populateBotList method or any other suitable location
        JOptionPane.showMessageDialog(mainPanel, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);
        try {

            // Check if the file exists
            if (Files.exists(pathBotFile)) {
                List<String> botLines = Files.readAllLines(pathBotFile);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL; // Allow components to expand horizontally

                for (String botName : botLines) {
                    if (!botName.trim().isEmpty()) {
                        JPanel botRowPanel = new JPanel(new GridBagLayout());

                        // Checkbox for the bot
                        JCheckBox checkbox = new JCheckBox();
                        gbc.gridx = 0;
                        gbc.weightx = 0; // Don't let the checkbox expand
                        botRowPanel.add(checkbox, gbc);

                        // Text field for "offline"
                        JTextField statusTextField = new JTextField("Offline");
                        statusTextField.setEditable(false);
                        gbc.gridx = 1;
                        gbc.weightx = 0; // Don't let the field expand
                        botRowPanel.add(statusTextField, gbc);

                        // Label for the bot name (read from the file)
                        JLabel nameLabel = new JLabel("   " + botName);
                        gbc.gridx = 2;
                        gbc.weightx = 1; // Expand this field
                        nameLabel.setName("botName");
                        botRowPanel.add(nameLabel, gbc);

                        // Dropdown for "script"
                        String[] scriptItems = loadScriptList(pathScriptFile);

                        // Always add "Script" as the first item in the list
                        String[] scriptitemsWithDefault = new String[scriptItems.length + 1];
                        scriptitemsWithDefault[0] = "Script";
                        System.arraycopy(scriptItems, 0, scriptitemsWithDefault, 1, scriptItems.length);

                        JComboBox<String> scriptDropdown = new JComboBox<>(scriptitemsWithDefault);
                        gbc.gridx = 3;
                        gbc.weightx = 0; // Don't let the dropdown expand
                        scriptDropdown.setName("scriptDropdown");
                        botRowPanel.add(scriptDropdown, gbc);

                        // Dropdown for "world"
                        String[] worldItems = loadWorldList(pathWorldFile);

                        // Always add "Script" as the first item in the list
                        String[] worlditemsWithDefault = new String[worldItems.length + 1];
                        worlditemsWithDefault[0] = "World";
                        System.arraycopy(worldItems, 0, worlditemsWithDefault, 1, worldItems.length);

                        JComboBox<String> worldDropdown = new JComboBox<>(worlditemsWithDefault);
                        gbc.gridx = 4;
                        gbc.weightx = 0; // Don't let the world expand
                        worldDropdown.setName("worldDropdown");
                        botRowPanel.add(worldDropdown, gbc);

                        // Dropdown for "proxy"
                        String[] proxyItems = loadProxyList(pathProxyFile);

                        // Always add "Proxy" as the first item in the list
                        String[] proxyitemsWithDefault = new String[proxyItems.length + 1];
                        proxyitemsWithDefault[0] = "Proxy";
                        System.arraycopy(proxyItems, 0, proxyitemsWithDefault, 1, proxyItems.length);

                        JComboBox<String> proxyDropdown = new JComboBox<>(proxyitemsWithDefault);
                        gbc.gridx = 5;
                        gbc.weightx = 0; // Don't let the dropdown expand
                        proxyDropdown.setName("proxyDropdown");
                        botRowPanel.add(proxyDropdown, gbc);

                        // Dropdown for "profile"
                        String[] profileItems = loadProfileList(pathProfileFile);

                        // Always add "Profile" as the first item in the list
                        String[] profileitemsWithDefault = new String[profileItems.length + 1];
                        profileitemsWithDefault[0] = "Profile";
                        System.arraycopy(profileItems, 0, profileitemsWithDefault, 1, profileItems.length);

                        JComboBox<String> profileDropdown = new JComboBox<>(profileitemsWithDefault);
                        gbc.gridx = 6;
                        gbc.weightx = 0; // Don't let the dropdown expand
                        profileDropdown.setName("profileDropdown");
                        botRowPanel.add(profileDropdown, gbc);


                        System.out.println("panelRunBotList.add");
                        // Add the row to the scroll panel
                        panelRunBotList.add(botRowPanel, gbc);

                        System.out.println("panelRunBotList BOUNDS = "+panelRunBotList.getBounds());
                    }
                }
            }

            // Refresh the layout of botlistpanel if needed
            panelRunBotList.revalidate();
            panelRunBotList.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void populateProxyList() {
        if (panelProxyList != null) {
            panelProxyList.removeAll();
        }

        // Show a JOptionPane with the message "We got here"
        JOptionPane.showMessageDialog(mainPanel, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);

        try {
            // Check if the file exists
            if (Files.exists(pathProxyFile)) {
                List<String> botLines = Files.readAllLines(pathProxyFile);

                for (String botName : botLines) {
                    if (!botName.trim().isEmpty()) {
                        JPanel botRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned row panel

                        // Checkbox for the proxy
                        JCheckBox checkbox = new JCheckBox();

                        // Label for the bot name (read from the file)
                        JLabel nameLabel = new JLabel(botName);

                        // Add components to the row panel
                        botRowPanel.add(checkbox);
                        botRowPanel.add(nameLabel);

                        // Add the row to the scroll panel
                        panelProxyList.add(botRowPanel);

                    }
                }
            }

            // Refresh the layout of proxylist panel if needed
            panelProxyList.revalidate();
            panelProxyList.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void populateProfileList() {
        if (panelProfileList != null) {
            panelProfileList.removeAll();
        }

        // Show a JOptionPane with the message "We got here"
        JOptionPane.showMessageDialog(mainPanel, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);

        try {

            // Check if the file exists
            if (Files.exists(pathProfileFile)) {
                List<String> botLines = Files.readAllLines(pathProfileFile);

                for (String botName : botLines) {
                    if (!botName.trim().isEmpty()) {
                        JPanel botRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned row panel

                        // Checkbox for the proxy
                        JCheckBox checkbox = new JCheckBox();

                        // Label for the bot name (read from the file)
                        JLabel nameLabel = new JLabel(botName);

                        // Add components to the row panel
                        botRowPanel.add(checkbox);
                        botRowPanel.add(nameLabel);

                        // Add the row to the scroll panel
                        panelProfileList.add(botRowPanel);
                    }
                }
            }

            // Refresh the layout of panelProxyList panel if needed
            panelProfileList.revalidate();
            panelProfileList.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // below is populate script panel
    // below is populate profile list
    public static void populateScriptList() {
        if (panelScriptList != null) {
            panelScriptList.removeAll();
        }

        // Show a JOptionPane with the message "We got here"
        JOptionPane.showMessageDialog(mainPanel, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);

        try {

            // Check if the file exists
            if (Files.exists(pathScriptFile)) {
                List<String> botLines = Files.readAllLines(pathScriptFile);

                for (String botName : botLines) {
                    if (!botName.trim().isEmpty()) {
                        JPanel botRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned row panel

                        // Checkbox for the proxy
                        JCheckBox checkbox = new JCheckBox();

                        // Label for the bot name (read from the file)
                        JLabel nameLabel = new JLabel(botName);

                        // Add components to the row panel
                        botRowPanel.add(checkbox);
                        botRowPanel.add(nameLabel);

                        // Add the row to the scroll panel
                        panelScriptList.add(botRowPanel);
                    }
                }
            }

            // Refresh the layout of panelProxyList panel if needed
            panelScriptList.revalidate();
            panelScriptList.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initComponents() {
        new Run();
        new BotList();
        new ProxyList();
        new ProfileList();
        new ScriptList();
        new Scheduling();
        checkAndCreateFiles();
        populateBotList();
        populateBotListActual();
        populateProxyList();
        populateProfileList();
        populateScriptList();
    }

}
