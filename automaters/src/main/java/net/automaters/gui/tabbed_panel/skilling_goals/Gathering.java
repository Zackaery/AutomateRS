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

    public static JSpinner spinnerGoalMining;
    public static JSpinner spinnerGoalFishing;
    public static JSpinner spinnerGoalWoodcutting;
    public static JSpinner spinnerGoalFarming;
    public static JSpinner spinnerGoalHunter;

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
            spinnerGoalMining = new JSpinner();
            spinnerGoalMining.setBounds(164, 17, 50, 20);
            spinnerGoalFishing = new JSpinner();
            spinnerGoalFishing.setBounds(164, 40, 50, 20);
            spinnerGoalWoodcutting = new JSpinner();
            spinnerGoalWoodcutting.setBounds(164, 63, 50, 20);
            spinnerGoalFarming = new JSpinner();
            spinnerGoalFarming.setBounds(164, 85, 50, 20);
            spinnerGoalHunter = new JSpinner();
            spinnerGoalHunter.setBounds(164, 108, 50, 20);

            setImage("skillIcons/Mining.png", labelGoalMining);
            setImage("skillIcons/Fishing.png", labelGoalFishing);
            setImage("skillIcons/Woodcutting.png", labelGoalWoodcutting);
            setImage("skillIcons/Farming.png", labelGoalFarming);
            setImage("skillIcons/Hunter.png", labelGoalHunter);

        }

        // ======== panelSkillingGoalsGathering ========
        {
            panelSkillingGoalsGathering.setBorder(new TitledBorder(null, "|     GATHERING     |",
                    TitledBorder.CENTER, TitledBorder.TOP, new Font(null, Font.BOLD, 12), Color.decode("#9f70d0")));

            // ---- labelGoalMining ----
            labelGoalMining.setText("Mining Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalMining.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalFishing ----
            labelGoalFishing.setText("Fishing Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalFishing.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalWoodcutting ----
            labelGoalWoodcutting.setText("Woodcutting Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalWoodcutting.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalFarming ----
            labelGoalFarming.setText("Farming Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalFarming.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalHunter ----
            labelGoalHunter.setText("Hunter Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalHunter.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        }

        // ======== vars ========
        {
            panelSkillingGoalsGathering.add(labelGoalMining);
            panelSkillingGoalsGathering.add(spinnerGoalMining);
            panelSkillingGoalsGathering.add(labelGoalFishing);
            panelSkillingGoalsGathering.add(spinnerGoalFishing);
            panelSkillingGoalsGathering.add(labelGoalWoodcutting);
            panelSkillingGoalsGathering.add(spinnerGoalWoodcutting);
            panelSkillingGoalsGathering.add(labelGoalFarming);
            panelSkillingGoalsGathering.add(spinnerGoalFarming);
            panelSkillingGoalsGathering.add(labelGoalHunter);
            panelSkillingGoalsGathering.add(spinnerGoalHunter);
        }
        TabSkillingGoals.gathering = true;
        TabSkillingGoals.create();
    }

    public static void setAccount_Gathering(Boolean b) {
        spinnerGoalMining.setEnabled(b);
        spinnerGoalMining.setValue(1);
        spinnerGoalFishing.setEnabled(b);
        spinnerGoalFishing.setValue(1);
        spinnerGoalWoodcutting.setEnabled(b);
        spinnerGoalWoodcutting.setValue(1);
        spinnerGoalFarming.setEnabled(b);
        spinnerGoalFarming.setValue(1);
        spinnerGoalHunter.setEnabled(b);
        spinnerGoalHunter.setValue(1);
    }
}
