package net.automaters.gui.tabbed_panel.skilling_goals;

import net.automaters.gui.tabbed_panel.TabSkillingGoals;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;

import static net.automaters.gui.GUI.setImage;

public class Gathering {

    public static JPanel panelSkillingGoalsGathering;

    public static JButton buttonToggleGathering;

    public static JLabel labelGoalMining;
    public static JLabel labelGoalFishing;
    public static JLabel labelGoalWoodcutting;
    public static JLabel labelGoalFarming;
    public static JLabel labelGoalHunter;

    public static JSpinner goalMining;
    public static JSpinner goalFishing;
    public static JSpinner goalWoodcutting;
    public static JSpinner goalFarming;
    public static JSpinner goalHunter;

    public static void create() throws IOException {

        panelSkillingGoalsGathering = new JPanel(null);
        panelSkillingGoalsGathering.setBounds(180, 210, 225, 156);

        // ======== goalGathering ========
        {
            labelGoalMining = new JLabel();
            labelGoalMining.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalMining.setBounds(10, 16, 136, 23);
            labelGoalFishing = new JLabel();
            labelGoalFishing.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalFishing.setBounds(32, 39, 114, 23);
            labelGoalWoodcutting = new JLabel();
            labelGoalWoodcutting.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalWoodcutting.setBounds(10, 62, 136, 22);
            labelGoalFarming = new JLabel();
            labelGoalFarming.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalFarming.setBounds(26, 84, 120, 23);
            labelGoalHunter = new JLabel();
            labelGoalHunter.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalHunter.setBounds(34, 107, 112, 23);
            goalMining = new JSpinner();
            goalMining.setBounds(164, 17, 50, 20);
            goalFishing = new JSpinner();
            goalFishing.setBounds(164, 40, 50, 20);
            goalWoodcutting = new JSpinner();
            goalWoodcutting.setBounds(164, 63, 50, 20);
            goalFarming = new JSpinner();
            goalFarming.setBounds(164, 85, 50, 20);
            goalHunter = new JSpinner();
            goalHunter.setBounds(164, 108, 50, 20);

            setImage("gui\\skill_icons\\Mining.png", labelGoalMining);
            setImage("gui\\skill_icons\\Fishing.png", labelGoalFishing);
            setImage("gui\\skill_icons\\Woodcutting.png", labelGoalWoodcutting);
            setImage("gui\\skill_icons\\Farming.png", labelGoalFarming);
            setImage("gui\\skill_icons\\Hunter.png", labelGoalHunter);

        }

        // ======== panelSkillingGoalsGathering ========
        {
            panelSkillingGoalsGathering.setBorder(new TitledBorder(null, "|     GATHERING     |",
                    TitledBorder.CENTER, TitledBorder.TOP, new Font(null, Font.BOLD, 12), Color.decode("#9f70d0")));

            // ---- labelGoalMining ----
            labelGoalMining.setText("Mining Level:");
            // ---- spinnerTaskStop ----
            goalMining.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalFishing ----
            labelGoalFishing.setText("Fishing Level:");
            // ---- spinnerTaskStop ----
            goalFishing.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalWoodcutting ----
            labelGoalWoodcutting.setText("Woodcutting Level:");
            // ---- spinnerTaskStop ----
            goalWoodcutting.setModel(new SpinnerNumberModel(99, 1, 99, 1));
            // ---- labelGoalFarming ----
            labelGoalFarming.setText("Farming Level:");
            // ---- spinnerTaskStop ----
            goalFarming.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalHunter ----
            labelGoalHunter.setText("Hunter Level:");
            // ---- spinnerTaskStop ----
            goalHunter.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        }

        // ======== vars ========
        {
            panelSkillingGoalsGathering.add(labelGoalMining);
//            panelSkillingGoalsGathering.add(goalMining);
            panelSkillingGoalsGathering.add(labelGoalFishing);
//            panelSkillingGoalsGathering.add(goalFishing);
            panelSkillingGoalsGathering.add(labelGoalWoodcutting);
            panelSkillingGoalsGathering.add(goalWoodcutting);
            panelSkillingGoalsGathering.add(labelGoalFarming);
//            panelSkillingGoalsGathering.add(goalFarming);
            panelSkillingGoalsGathering.add(labelGoalHunter);
//            panelSkillingGoalsGathering.add(goalHunter);
        }
        TabSkillingGoals.gathering = true;
        TabSkillingGoals.create();
    }

    public static void setAccount_Gathering(Boolean b) {
        goalMining.setEnabled(b);
        goalMining.setValue(1);
        goalFishing.setEnabled(b);
        goalFishing.setValue(1);
        goalWoodcutting.setEnabled(b);
        goalWoodcutting.setValue(1);
        goalFarming.setEnabled(b);
        goalFarming.setValue(1);
        goalHunter.setEnabled(b);
        goalHunter.setValue(1);
    }
}
