package net.automaters.gui;

import net.automaters.gui.tabbed_panel.TabSettings;
import net.automaters.gui.tabbed_panel.TabSkillingGoals;
import net.automaters.gui.tabbed_panel.settings.Account;
import net.automaters.gui.tabbed_panel.settings.Task;
import net.automaters.gui.tabbed_panel.settings.Webhook;
import net.automaters.gui.tabbed_panel.skilling_goals.Artisan;
import net.automaters.gui.tabbed_panel.skilling_goals.Combat;
import net.automaters.gui.tabbed_panel.skilling_goals.Gathering;
import net.automaters.gui.tabbed_panel.skilling_goals.Support;
import net.automaters.gui.utils.EventDispatchThreadRunner;
import net.automaters.script.AutomateRS;
import static net.automaters.api.utils.Debug.debug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static net.automaters.script.AutomateRS.*;
import static net.automaters.script.Variables.*;
import static net.automaters.util.file_managers.IconManager.AUTOMATERS_TITLE;
import static net.automaters.util.file_managers.IconManager.set;

public class GUI implements ActionListener {

    public JFrame frame = new JFrame();
    public static String versionNumber = "v0.28";
    public static JTabbedPane tabbedPanel;
    public static JLabel labelTitle;

    public static boolean F2P;
    public static boolean tabbedPanelInitialized = false;
    public static boolean tabSettingsInitialized = false;
    public static boolean tabSkillingGoalsInitialized = false;
    public static String selectedBuild;
    public static AutomateRS automateRS;


    private static JLabel labelSaveName;
    private static JLabel labelLoadName;
    private static JLabel labelDeleteName;

    private static JButton buttonStart;
    private static JButton buttonLoad;
    private static JButton buttonSave;
    private static JButton buttonDelete;
    private static JButton generateCLIArgs;

    private static JComboBox comboBoxLoadProfile;
    private static JComboBox comboBoxDeleteProfile;


    private static JTextField textFieldSaveName;

    public static int ATTACK_GOAL;
    public static int STRENGTH_GOAL;
    public static int DEFENCE_GOAL;
    public static int RANGED_GOAL;
    public static int MAGIC_GOAL;
    public static int PRAYER_GOAL;

    public static int HERBLORE_GOAL;
    public static int CRAFTING_GOAL;
    public static int FLETCHING_GOAL;
    public static int SMITHING_GOAL;
    public static int COOKING_GOAL;
    public static int FIREMAKING_GOAL;
    public static int RUNECRAFTING_GOAL;
    public static int CONSTRUCTION_GOAL;

    public static int MINING_GOAL;
    public static int FISHING_GOAL;
    public static int WOODCUTTING_GOAL;
    public static int FARMING_GOAL;
    public static int HUNTER_GOAL;

    public static int AGILITY_GOAL;
    public static int THIEVING_GOAL;
    public static int SLAYER_GOAL;


    public GUI() throws IOException {
        initGUI(frame);
    }

    public static void start() throws IOException {
        GUI gui = new GUI();
        gui.open();
        debug("Launching AutomateRS - GUI");
    }

    public void initGUI(JFrame frame) throws IOException {
        labelSaveName = new JLabel();
        labelSaveName.setText("Save profile as:");
        labelSaveName.setHorizontalAlignment(SwingConstants.TRAILING);

        labelLoadName = new JLabel();
        labelLoadName.setText("Load profile:");
        labelLoadName.setHorizontalAlignment(SwingConstants.TRAILING);

        labelDeleteName = new JLabel();
        labelDeleteName.setText("Delete profile:");
        labelDeleteName.setHorizontalAlignment(SwingConstants.TRAILING);

        textFieldSaveName = new JTextField();
        textFieldSaveName.setText("AutomateRS");
        textFieldSaveName.setMargin(new Insets(3, 5, 3, 5));

        buttonStart = new JButton();
        buttonStart.setText("START SCRIPT");
        buttonStart.setSize(50, 15);
        buttonStart.addActionListener(e -> {
            guiStarted = true;
            scriptStarted = true;
            selectedBuild = getSelectedBuild();
            configStats();
            frame.dispose();
            scriptTimer = (System.currentTimeMillis() - elapsedTime);
        });
        buttonLoad = new JButton();
        buttonLoad.setText("LOAD");
        buttonLoad.setSize(150, 15);
        buttonLoad.addActionListener(e -> {
        });
        buttonSave = new JButton();
        buttonSave.setText("SAVE");
        buttonSave.setSize(50, 15);
        buttonSave.addActionListener(e -> {
        });
        buttonDelete = new JButton();
        buttonDelete.setText("DELETE");
        buttonDelete.setSize(50, 15);
        buttonDelete.addActionListener(e -> {
        });
        generateCLIArgs = new JButton();
        generateCLIArgs.setText("GENERATE CLI ARGUMENTS");
        generateCLIArgs.setSize(150, 15);
        generateCLIArgs.addActionListener(e -> {
        });

        comboBoxLoadProfile = new JComboBox<>();
        comboBoxDeleteProfile = new JComboBox<>();

        // ======== this ========
        frame.setTitle("AutomateRS - Configurations " + versionNumber);

        // --- labelTitle ---
        labelTitle = new JLabel(AUTOMATERS_TITLE);
        labelTitle.setBounds(449, 10, 957, 160);
        labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitle.setFont(labelTitle.getFont().deriveFont(labelTitle.getFont().getSize() + 4f));

        // --- tabbedPanel ---
        tabbedPanel = new JTabbedPane();

        if (!tabSettingsInitialized) {
            TabSettings.create();
        } else if (!tabSkillingGoalsInitialized) {
            try {
                TabSkillingGoals.create();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (!tabbedPanelInitialized) {
            createGUI();
            tabbedPanel.setTabPlacement(SwingConstants.TOP);
            tabbedPanel.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
            tabbedPanel.setBorder(null);
            tabbedPanel.addChangeListener(e -> {
                Component selected = tabbedPanel.getSelectedComponent();
                if (selected != null) {
                    selected.requestFocusInWindow();
                }
            });
            tabbedPanel.addTab("General Settings", TabSettings.panelTabSettings);
            tabbedPanel.setForegroundAt(0, new Color(255, 255, 255));
            tabbedPanel.addTab("Skilling Goals", TabSkillingGoals.panelTabSkillingGoals);
            tabbedPanel.setForegroundAt(1, new Color(255, 255, 255));

            tabbedPanelInitialized = true;
        }
    }

    public void createGUI() {
        Component selected = tabbedPanel.getSelectedComponent();
        if (selected != null) {
            selected.requestFocusInWindow();
        }
        TabSettings.panelTabSettings = new JPanel(null);
        TabSettings.panelTabSettings.add(Account.panelAccountSettings);
        TabSettings.panelTabSettings.add(Task.panelTask);
        TabSettings.panelTabSettings.add(Webhook.panelWebhookSettings);

        TabSkillingGoals.panelTabSkillingGoals = new JPanel(null);
        TabSkillingGoals.panelTabSkillingGoals.add(TabSkillingGoals.checkBoxEnableRandomSkilling);
        TabSkillingGoals.panelTabSkillingGoals.add(TabSkillingGoals.spinnerRandomSkillingChance);
        TabSkillingGoals.panelTabSkillingGoals.add(TabSkillingGoals.labelRandomSkillingChance);
        TabSkillingGoals.panelTabSkillingGoals.add(TabSkillingGoals.buttonMaxAccount);
        TabSkillingGoals.panelTabSkillingGoals.add(TabSkillingGoals.buttonResetSkills);
        TabSkillingGoals.panelTabSkillingGoals.add(Combat.panelSkillingGoalsCombat);
        TabSkillingGoals.panelTabSkillingGoals.add(Gathering.panelSkillingGoalsGathering);
        TabSkillingGoals.panelTabSkillingGoals.add(Artisan.panelSkillingGoalsArtisan);
        TabSkillingGoals.panelTabSkillingGoals.add(Support.panelSkillingGoalsSupport);

        Container contentPane = frame.getContentPane();

        frame.getContentPane().setLayout(null);

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPaneLayout.setHorizontalGroup(contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(tabbedPanel, GroupLayout.PREFERRED_SIZE, 750, Short.MAX_VALUE)
                                .addComponent(labelTitle)
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                        .addComponent(labelSaveName, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textFieldSaveName, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonSave, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                        .addComponent(labelLoadName, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(comboBoxLoadProfile, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonLoad, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                        .addComponent(labelDeleteName, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(comboBoxDeleteProfile, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonDelete, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                        .addComponent(buttonStart, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(generateCLIArgs, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE))
                        )));
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(labelTitle)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelSaveName, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldSaveName, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                                        .addComponent(buttonSave, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelLoadName, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBoxLoadProfile, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                                        .addComponent(buttonLoad, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelDeleteName, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBoxDeleteProfile, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                                        .addComponent(buttonDelete, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGap(20)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonStart, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                                        .addComponent(generateCLIArgs, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tabbedPanel, GroupLayout.PREFERRED_SIZE, 405, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        contentPane.setLayout(contentPaneLayout);

        frame.pack();
//        contentPane.setForeground(new Color(17, 17, 17));
//        contentPane.setBackground(new Color(17, 17, 17));
        frame.setVisible(true);
        frame.setLocationRelativeTo(frame.getOwner());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static void configStats() {
        ATTACK_GOAL = Integer.parseInt(String.valueOf(Combat.goalAttack.getValue()));
        STRENGTH_GOAL = Integer.parseInt(String.valueOf(Combat.goalStrength.getValue()));
        DEFENCE_GOAL = Integer.parseInt(String.valueOf(Combat.goalDefence.getValue()));
        RANGED_GOAL = Integer.parseInt(String.valueOf(Combat.goalRanged.getValue()));
        MAGIC_GOAL = Integer.parseInt(String.valueOf(Combat.goalMagic.getValue()));
        PRAYER_GOAL = Integer.parseInt(String.valueOf(Combat.goalPrayer.getValue()));

        HERBLORE_GOAL = Integer.parseInt(String.valueOf(Artisan.goalHerblore.getValue()));
        CRAFTING_GOAL = Integer.parseInt(String.valueOf(Artisan.goalCrafting.getValue()));
        FLETCHING_GOAL = Integer.parseInt(String.valueOf(Artisan.goalFletching.getValue()));
        SMITHING_GOAL = Integer.parseInt(String.valueOf(Artisan.goalSmithing.getValue()));
        COOKING_GOAL = Integer.parseInt(String.valueOf(Artisan.goalCooking.getValue()));
        FIREMAKING_GOAL = Integer.parseInt(String.valueOf(Artisan.goalFiremaking.getValue()));
        RUNECRAFTING_GOAL = Integer.parseInt(String.valueOf(Artisan.goalRunecrafting.getValue()));
        CONSTRUCTION_GOAL = Integer.parseInt(String.valueOf(Artisan.goalConstruction.getValue()));

        MINING_GOAL = Integer.parseInt(String.valueOf(Gathering.goalMining.getValue()));
        FISHING_GOAL = Integer.parseInt(String.valueOf(Gathering.goalFishing.getValue()));
        WOODCUTTING_GOAL = Integer.parseInt(String.valueOf(Gathering.goalWoodcutting.getValue()));
        FARMING_GOAL = Integer.parseInt(String.valueOf(Gathering.goalFarming.getValue()));
        HUNTER_GOAL = Integer.parseInt(String.valueOf(Gathering.goalHunter.getValue()));

        AGILITY_GOAL = Integer.parseInt(String.valueOf(Support.goalAgility.getValue()));
        THIEVING_GOAL = Integer.parseInt(String.valueOf(Support.goalThieving.getValue()));
        SLAYER_GOAL = Integer.parseInt(String.valueOf(Support.goalSlayer.getValue()));

    }

    public static String getSelectedBuild() {
        if (Account.comboBoxAccountType != null) {
            return Account.comboBoxAccountType.getSelectedItem().toString().toUpperCase().replace(" ", "_");
        }
        return "";
    }

    public static void setImage(String loadImage, JLabel label) {
        ImageIcon icon = set(loadImage);
        label.setIcon(icon);
    }

    public void setImage(String pathToImage, JButton button) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(pathToImage));
        button.setIcon(icon);
    }

    public static void main(String[] args) throws IOException {
        try {
            EventDispatchThreadRunner.runOnDispatchThread(() -> {
                try {
                    new GUI();
                    debug("Launching AutomateRS - GUI");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, true);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void open() throws IOException {
        initGUI(frame);
        frame.setVisible(true);
    }

    public boolean isOpen() {
        return frame.isVisible();
    }

    public void close() {
        debug("Closing GUI");
        frame.setVisible(false);
        frame.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
