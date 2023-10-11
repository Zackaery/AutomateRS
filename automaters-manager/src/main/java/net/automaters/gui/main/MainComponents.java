package net.automaters.gui.main;

import com.formdev.flatlaf.ui.FlatTitlePane;
import net.automaters.api.ui.CustomSeparator;
import net.automaters.api.ui.CustomTabbedPaneUI;
import net.automaters.gui.main.tabs.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static net.automaters.AutomateManager.*;
import static net.automaters.api.panels.FindJComponent.findCheckbox;
import static net.automaters.api.panels.FindJComponent.findLabel;
import static net.automaters.api.panels.ReadContents.*;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.utils.file_managers.FileManager.PATH_AUTOMATE_RS;
import static net.automaters.api.utils.file_managers.IconManager.*;
import static net.automaters.api.utils.file_managers.IconManager.STOP_HOVER_ICON;
import static net.automaters.api.utils.file_managers.ProcessManager.getProcessPIDs;
import static net.automaters.gui.main.tabs.Run.*;
import static net.unethicalite.api.game.Game.logout;

public class MainComponents {
    public static JLabel labelTitle;
    public static JLabel labelIcon;
    public static JTabbedPane tabbedPanel;
    public static JTabbedPane tabbedPanelScheduling = new JTabbedPane(JTabbedPane.TOP);

    public static CustomSeparator lineSeparator = new CustomSeparator(AUTOMATE_ORANGE, managerWidth, 1);

    public static JPanel mainPanel = new JPanel();
    public static JPanel tabRun = new JPanel();
    public static JPanel tabAccountList = new JPanel();
    public static JPanel tabProxyList = new JPanel();
    public static JPanel tabProfileList = new JPanel();
    public static JPanel tabPluginList = new JPanel();
    public static JPanel tabSchedulePanel = new JPanel();


    public static JPanel panelRunAccountList = new JPanel();
    public static JPanel panelRunComponents = new JPanel();

    public static JPanel panelAccountListActual = new JPanel();
    public static JPanel panelAccountListComponents = new JPanel();

    public static JPanel panelProxyList = new JPanel();
    public static JPanel panelProxyComponents = new JPanel();

    public static JPanel panelProfileList = new JPanel();
    public static JPanel panelProfileComponents = new JPanel();

    public static JPanel panelPluginList = new JPanel();
    public static JPanel panelPluginComponents = new JPanel();

    public static JPanel panelRunSchedule = new JPanel();
    public static JPanel panelCreateSchedule = new JPanel();

    public static final String PATH_AUTOMATE_RS_MANAGER = PATH_AUTOMATE_RS + "Manager";
    public static final Path pathAccountFile = Path.of(PATH_AUTOMATE_RS_MANAGER + File.separator + "accountlist.txt");
    public static final Path pathPluginFile = Path.of(PATH_AUTOMATE_RS_MANAGER + File.separator + "PluginList.txt");
    public static final Path pathProxyFile = Path.of(PATH_AUTOMATE_RS_MANAGER + File.separator + "proxylist.txt");
    public static final Path pathProfileFile = Path.of(PATH_AUTOMATE_RS_MANAGER + File.separator + "profilelist.txt");

    public static final Dimension dimensionComboBox = new Dimension(100, 25);

    private int initialX;
    private int initialY;
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

    }

    private void addTabbedPanel() {

        // --- tabbedPanel ---
        tabbedPanel = new JTabbedPane(JTabbedPane.LEFT);

        tabRun.setLayout(null);
        tabAccountList.setLayout(null);
        tabProxyList.setLayout(null);
        tabProfileList.setLayout(null);
        tabPluginList.setLayout(null);
        tabSchedulePanel.setLayout(null);


        tabbedPanel.addTab("Start/Stop", null, tabRun, null);
        tabbedPanel.addTab("Account List", null, tabAccountList, "Add Accounts");
        tabbedPanel.addTab("Proxy List", null, tabProxyList, "Add Proxys");
        tabbedPanel.addTab("Profile List", null, tabProfileList, "Add Profiles");
        tabbedPanel.addTab("Plugin List", null, tabPluginList, "Add Plugins");
        tabbedPanel.addTab("Scheduling", null, tabSchedulePanel, "Configure Scheduling");
        initComponents();
    }

    private void createComponents() {
        Container contentPane = mainFrame.getContentPane();

        mainFrame.getContentPane().setLayout(null);
        GroupLayout contentPaneLayout = new GroupLayout(contentPane);

        contentPaneLayout.setHorizontalGroup(contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(labelTitle)
                                .addComponent(lineSeparator)
                                .addComponent(tabbedPanel, 600, managerWidth, managerWidth)))
                        .addComponent(mainPanel));
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(labelTitle)
                                .addComponent(lineSeparator)
                                .addComponent(tabbedPanel, 300, managerHeight, managerHeight)
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

        debug("Tabbed Panel Bounds: "+tabbedPanel.getBounds());
        debug("Line Separator Bounds: "+lineSeparator.getBounds());
    }

    public static void populateAccountListActual() {
        //System.out.println("[POPULATE ACCOUNT LIST ACTUAL]");
        if (panelAccountListActual != null) {
            panelAccountListActual.removeAll();
        }
        // Show a JOptionPane with the message "We made it here"
        // Inside your populateAccountList method or any other suitable location
        //JOptionPane.showMessageDialog(mainPanel, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);
        try {

            // Check if the file exists
            if (Files.exists(pathAccountFile)) {
                java.util.List<String> accountLines = Files.readAllLines(pathAccountFile);

                for (String accountName : accountLines) {
                    if (!accountName.trim().isEmpty()) {
                        JPanel accountRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned row panel

                        // Checkbox for the account
                        JCheckBox checkbox = new JCheckBox();

//                        // Text field for "offline"
//                        JTextField statusTextField = new JTextField("Offline");
//                        statusTextField.setEditable(false);

                        // Label for the account name (read from the file)
                        JLabel nameLabel = new JLabel(accountName);

                        // Add components to the row panel
                        accountRowPanel.add(checkbox);
//                        accountRowPanel.add(statusTextField);
                        accountRowPanel.add(nameLabel);

                        //System.out.println("Adding - accountRowPanel: "+nameLabel.getText());
                        // Add the row to the scroll panel
                        panelAccountListActual.add(accountRowPanel);
                        //System.out.println("Panel Bounds: "+panelAccountListActual.getBounds()+"\n");
                    }
                }
            }

            // Refresh the layout of panelAccountListActual panel if needed
            panelAccountListActual.revalidate();
            panelAccountListActual.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void populateAccountList() {
        if (panelRunAccountList != null) {
            panelRunAccountList.removeAll();
        }

        try {
            if (Files.exists(pathAccountFile)) {
                List<String> accountLines = Files.readAllLines(pathAccountFile);

                for (String accountInfo : accountLines) {
                    if (!accountInfo.trim().isEmpty()) {
                        JPanel accountRowPanel = createAccountRowPanel(accountInfo);
                        panelRunAccountList.add(accountRowPanel);
                    }
                }

                // Revalidate and repaint the panelRunAccountList
                panelRunAccountList.revalidate();
                panelRunAccountList.repaint();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static JPanel createAccountRowPanel(String accountInfo) throws IOException, InterruptedException {
        JPanel accountRowPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 0));

        // Extracted email from accountInfo
        String[] parts = accountInfo.split(":");
        String email = parts.length >= 1 ? parts[0] : "";

        // Create JComboBox components
        String[] pluginItems = loadFileList(pathPluginFile);
        String[] pluginItemsWithDefault = new String[pluginItems.length + 1];
        pluginItemsWithDefault[0] = "Plugin";
        System.arraycopy(pluginItems, 0, pluginItemsWithDefault, 1, pluginItems.length);
        JComboBox<String> pluginDropdown = createDropdown(pluginItemsWithDefault, dimensionComboBox, "pluginDropdown");


        String[] freeWorlds = {"Go Back", "301", "308", "316", "326", "335", "371", "379", "380", "382", "383", "384", "394", "397", "398",
                "399", "417", "418", "430", "431", "433", "434", "435", "436", "437", "451", "452", "453",
                "454", "455", "456", "460", "462", "463", "464", "465", "469", "470", "471", "475", "476",
                "483", "497", "498", "499", "562", "563", "570"};
        String[] membersWorlds = {"Go Back", "302", "303", "304", "305", "306", "307", "309", "310", "311", "312", "313", "314", "315",
                "317", "320", "321", "323", "324", "325", "327", "328", "329", "330", "331", "332", "334",
                "336", "337", "338", "339", "340", "341", "342", "343", "344", "346", "347", "348", "350",
                "351", "352", "354", "355", "356", "357", "358", "359", "360", "362", "367", "368", "370",
                "374", "375", "376", "377", "386", "387", "388", "390", "395", "441", "443", "444", "445",
                "459", "463", "464", "465", "466", "477", "478", "480", "481", "482", "484", "485", "486",
                "487", "488", "489", "490", "491", "492", "493", "494", "495", "496", "505", "506", "508",
                "509", "510", "511", "512", "513", "514", "515", "516", "517", "518", "519", "520", "521",
                "522", "523", "524", "525", "531", "532", "534", "535", "580"};

        JComboBox<String> worldDropdown = createWorldDropdown("Select World", freeWorlds, membersWorlds, dimensionComboBox, "worldDropdown");

        String[] proxyItems = loadFileList(pathProxyFile);
        String[] proxyItemsWithDefault = new String[proxyItems.length + 1];
        proxyItemsWithDefault[0] = "Proxy";
        System.arraycopy(proxyItems, 0, proxyItemsWithDefault, 1, proxyItems.length);
        JComboBox<String> proxyDropdown = createDropdown(proxyItemsWithDefault, dimensionComboBox, "proxyDropdown");

        String[] profileItems = loadFileList(pathProfileFile);
        String[] profileItemsWithDefault = new String[profileItems.length + 1];
        profileItemsWithDefault[0] = "Profile";
        System.arraycopy(profileItems, 0, profileItemsWithDefault, 1, profileItems.length);
        JComboBox<String> profileDropdown = createDropdown(profileItemsWithDefault, dimensionComboBox, "profileDropdown");

        // Text field for "offline"
        JTextField statusTextField = new JTextField("Offline");
        statusTextField.setHorizontalAlignment(SwingConstants.CENTER);
        statusTextField.setEditable(false);

        // startIcon for the account
        JLabel startIcon = new JLabel();
        startIcon.setIcon(START_ICON);
        startIcon.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mousePressed(MouseEvent e) {

                List<String> pids = null;
                try {
                    pids = getProcessPIDs();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                debug("after getting pids");
                // Get the source component that triggered the event (in this case, the startIcon)
                Component source = e.getComponent();

                // Find the parent JPanel (accountRowPanel) that contains the source component
                while (source != null && !(source instanceof JPanel)) {
                    source = source.getParent();
                }

                if (source != null && source instanceof JPanel) {
                    JPanel accountRowPanel = (JPanel) source;
                    if (startIcon.getIcon() == START_ICON || startIcon.getIcon() == START_HOVER_ICON) {
                        startIcon.setIcon(STOP_ICON);
                        statusTextField.setText("Running");
                        debug("clicked start for account: " + email);
                        try {
                            if (shouldStartAccount(accountRowPanel, pids)) {
                                debug("should start account");
                                startAccount(accountRowPanel);
                            } else {
                                debug("should not start account");
                            }
                        } catch (IOException | InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Account not selected!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (startIcon.getIcon() == STOP_HOVER_ICON) {
                        startIcon.setIcon(START_ICON);
                        statusTextField.setText("Offline");
                        debug("clicked stop for account: " + email);

                        // Loop through account rows
                        for (Component component : panelRunAccountList.getComponents()) {

                            debug("inside for component loop");
                            if (component instanceof JPanel) {
                                debug("component is instance of JPanel");
                                JPanel accountRowPanel = (JPanel) component;
                                try {
                                    if (shouldStopAccount(accountRowPanel, pids)) {
                                        debug("should stop account");
                                        stopAccount(accountRowPanel);
                                    } else {
                                        debug("should not stop account");
                                    }
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Account not selected!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if (startIcon.getIcon() == START_ICON) {
                    startIcon.setIcon(START_HOVER_ICON);
                    debug("hovered start for account: "+email);
                }
                if (startIcon.getIcon() == STOP_ICON) {
                    startIcon.setIcon(STOP_HOVER_ICON);
                    debug("hovered stop for account: "+email);

                }
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if (startIcon.getIcon() == START_HOVER_ICON) {
                    startIcon.setIcon(START_ICON);
                    debug("mouseExited start for account: "+email);
                }
                if (startIcon.getIcon() == STOP_HOVER_ICON) {
                    startIcon.setIcon(STOP_ICON);
                    debug("mouseExited stop for account: "+email);

                }
            }
        });


        JCheckBox checkbox = new JCheckBox();
        accountRowPanel.add(startIcon);
        accountRowPanel.add(statusTextField, BorderLayout.CENTER);

        // Label for the account name
        JLabel nameLabel = new JLabel("   " + email);
        nameLabel.setName("accountName");
        accountRowPanel.add(nameLabel);

        // Add the JComboBox components
        accountRowPanel.add(pluginDropdown);
        accountRowPanel.add(worldDropdown);
        accountRowPanel.add(proxyDropdown);
        accountRowPanel.add(profileDropdown);

        return accountRowPanel;
    }

    private static JComboBox<String> createDropdown(String[] items, Dimension dimension, String name) {
        JComboBox<String> dropdown = new JComboBox<>(items);
        dropdown.setName(name);
        dropdown.setPreferredSize(dimension);
        return dropdown;
    }

    private static JComboBox<String> createWorldDropdown(String initialItem, String[] freeWorlds, String[] membersWorlds, Dimension dimension, String name) {
        String[] items = {initialItem, "Free Worlds", "Members Worlds"};
        JComboBox<String> dropdown = createDropdown(items, dimension, name);

        dropdown.addActionListener(e -> {
            String selected = (String) dropdown.getSelectedItem();
            if ("Worlds".equals(selected) || "Go Back".equals(selected)) {
                dropdown.setModel(new DefaultComboBoxModel<>(new String[]{"Free Worlds", "Members Worlds"}));
            } else if ("Free Worlds".equals(selected)) {
                dropdown.setModel(new DefaultComboBoxModel<>(freeWorlds));
            } else if ("Members Worlds".equals(selected)) {
                dropdown.setModel(new DefaultComboBoxModel<>(membersWorlds));
            }
        });

        return dropdown;
    }



    public static void populateProxyList() {

        //System.out.println("[POPULATE PROXY LIST]");
        if (panelProxyList != null) {
            panelProxyList.removeAll();
        }

        // Show a JOptionPane with the message "We got here"
        //JOptionPane.showMessageDialog(mainPanel, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);

        try {
            // Check if the file exists
            if (Files.exists(pathProxyFile)) {
                List<String> accountLines = Files.readAllLines(pathProxyFile);

                for (String accountName : accountLines) {
                    if (!accountName.trim().isEmpty()) {
                        JPanel accountRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned row panel

                        // Checkbox for the proxy
                        JCheckBox checkbox = new JCheckBox();

                        // Label for the account name (read from the file)
                        JLabel nameLabel = new JLabel(accountName);

                        // Add components to the row panel
                        accountRowPanel.add(checkbox);
                        accountRowPanel.add(nameLabel);

                        //System.out.println("Adding - accountRowPanel: "+nameLabel.getText());
                        // Add the row to the scroll panel
                        panelProxyList.add(accountRowPanel);
                        //System.out.println("Panel Bounds: "+panelProxyList.getBounds()+"\n");
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

        //System.out.println("[POPULATE PROFILE LIST]");
        if (panelProfileList != null) {
            panelProfileList.removeAll();
        }

        // Show a JOptionPane with the message "We got here"
        //JOptionPane.showMessageDialog(mainPanel, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);

        try {

            // Check if the file exists
            if (Files.exists(pathProfileFile)) {
                List<String> accountLines = Files.readAllLines(pathProfileFile);

                for (String accountName : accountLines) {
                    if (!accountName.trim().isEmpty()) {
                        JPanel accountRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned row panel

                        // Checkbox for the proxy
                        JCheckBox checkbox = new JCheckBox();

                        // Label for the account name (read from the file)
                        JLabel nameLabel = new JLabel(accountName);

                        // Add components to the row panel
                        accountRowPanel.add(checkbox);
                        accountRowPanel.add(nameLabel);

                        //System.out.println("Adding - accountRowPanel: "+nameLabel.getText());
                        // Add the row to the scroll panel
                        panelProfileList.add(accountRowPanel);
                        //System.out.println("Panel Bounds: "+panelProfileList.getBounds()+"\n");
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

    // below is populate plugin panel
    // below is populate profile list
    public static void populatePluginList() {

        //System.out.println("[POPULATE PLUGIN LIST]");
        if (panelPluginList != null) {
            panelPluginList.removeAll();
        }

        // Show a JOptionPane with the message "We got here"
        //JOptionPane.showMessageDialog(mainPanel, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);

        try {

            // Check if the file exists
            if (Files.exists(pathPluginFile)) {
                List<String> accountLines = Files.readAllLines(pathPluginFile);

                for (String accountName : accountLines) {
                    if (!accountName.trim().isEmpty()) {
                        JPanel accountRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned row panel

                        // Checkbox for the proxy
                        JCheckBox checkbox = new JCheckBox();

                        // Label for the account name (read from the file)
                        JLabel nameLabel = new JLabel(accountName);

                        // Add components to the row panel
                        accountRowPanel.add(checkbox);
                        accountRowPanel.add(nameLabel);

                        //System.out.println("Adding - accountRowPanel: "+nameLabel.getText());
                        // Add the row to the scroll panel
                        panelPluginList.add(accountRowPanel);
                        //System.out.println("Panel Bounds: "+panelPluginList.getBounds()+"\n");
                    }
                }
            }

            // Refresh the layout of panelProxyList panel if needed
            panelPluginList.revalidate();
            panelPluginList.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initComponents() {
        new Run();
        new AccountList();
        new ProxyList();
        new ProfileList();
        new PluginList();
        new Scheduling();
        checkAndCreateFiles();
        populateAccountList();
        populateAccountListActual();
        populateProxyList();
        populateProfileList();
        populatePluginList();
    }

    public static void initScrollPane(JScrollPane scrollPane, JPanel panel) {

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(10, 100, managerWidth-20, 200);

        scrollPane.setViewportView(panel);

    }

}
