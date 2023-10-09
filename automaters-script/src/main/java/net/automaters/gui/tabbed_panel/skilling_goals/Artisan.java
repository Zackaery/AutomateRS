package net.automaters.gui.tabbed_panel.skilling_goals;

import net.automaters.gui.tabbed_panel.TabSkillingGoals;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;

import static net.automaters.gui.GUI.setImage;

public class Artisan {
    public static JPanel panelSkillingGoalsArtisan;

    public static JButton buttonToggleArtisan;

    public static JLabel labelGoalHerblore;
    public static JLabel labelGoalCrafting;
    public static JLabel labelGoalFletching;
    public static JLabel labelGoalSmithing;
    public static JLabel labelGoalCooking;
    public static JLabel labelGoalFiremaking;
    public static JLabel labelGoalRunecrafting;
    public static JLabel labelGoalConstruction;

    public static JSpinner goalHerblore;
    public static JSpinner goalCrafting;
    public static JSpinner goalFletching;
    public static JSpinner goalSmithing;
    public static JSpinner goalCooking;
    public static JSpinner goalFiremaking;
    public static JSpinner goalRunecrafting;
    public static JSpinner goalConstruction;

    public static void create() throws IOException {

        panelSkillingGoalsArtisan = new JPanel(null);
        panelSkillingGoalsArtisan.setBounds(465, 11, 225, 223);

        // ======== goalArtisan ========
        {
            labelGoalHerblore = new JLabel();
            labelGoalHerblore.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalHerblore.setBounds(10, 18, 144, 17);
            labelGoalCrafting = new JLabel();
            labelGoalCrafting.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalCrafting.setBounds(34, 36, 120, 22);
            labelGoalFletching = new JLabel();
            labelGoalFletching.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalFletching.setBounds(29, 58, 125, 25);
            labelGoalSmithing = new JLabel();
            labelGoalSmithing.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalSmithing.setBounds(38, 84, 116, 19);
            labelGoalCooking = new JLabel();
            labelGoalCooking.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalCooking.setBounds(40, 103, 114, 21);
            labelGoalFiremaking = new JLabel();
            labelGoalFiremaking.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalFiremaking.setBounds(23, 124, 131, 23);
            labelGoalRunecrafting = new JLabel();
            labelGoalRunecrafting.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalRunecrafting.setBounds(10, 147, 144, 25);
            labelGoalConstruction = new JLabel();
            labelGoalConstruction.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalConstruction.setBounds(11, 172, 143, 25);
            goalHerblore = new JSpinner();
            goalHerblore.setBounds(165, 16, 50, 20);
            goalCrafting = new JSpinner();
            goalCrafting.setBounds(165, 37, 50, 20);
            goalFletching = new JSpinner();
            goalFletching.setBounds(165, 60, 50, 20);
            goalSmithing = new JSpinner();
            goalSmithing.setBounds(165, 83, 50, 20);
            goalCooking = new JSpinner();
            goalCooking.setBounds(165, 103, 50, 20);
            goalFiremaking = new JSpinner();
            goalFiremaking.setBounds(165, 125, 50, 20);
            goalRunecrafting = new JSpinner();
            goalRunecrafting.setBounds(165, 149, 50, 20);
            goalConstruction = new JSpinner();
            goalConstruction.setBounds(165, 174, 50, 20);

            setImage("gui\\skill_icons\\Herblore.png", labelGoalHerblore);
            setImage("gui\\skill_icons\\Crafting.png", labelGoalCrafting);
            setImage("gui\\skill_icons\\Fletching.png", labelGoalFletching);
            setImage("gui\\skill_icons\\Smithing.png", labelGoalSmithing);
            setImage("gui\\skill_icons\\Cooking.png", labelGoalCooking);
            setImage("gui\\skill_icons\\Firemaking.png", labelGoalFiremaking);
            setImage("gui\\skill_icons\\Runecrafting.png", labelGoalRunecrafting);
            setImage("gui\\skill_icons\\Construction.png", labelGoalConstruction);

        }

        // ======== panelSkillingGoalsArtisan ========
        {
            panelSkillingGoalsArtisan.setBorder(new TitledBorder(null, "|     ARTISAN     |", TitledBorder.CENTER,
                    TitledBorder.TOP, new Font(null, Font.BOLD, 12), Color.decode("#9f70d0")));

            // ---- labelGoalHerblore ----
            labelGoalHerblore.setText("Herblore Level:");
            // ---- spinnerTaskStop ----
            goalHerblore.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalCrafting ----
            labelGoalCrafting.setText("Crafting Level:");
            // ---- spinnerTaskStop ----
            goalCrafting.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalFletching ----
            labelGoalFletching.setText("Fletching Level:");
            // ---- spinnerTaskStop ----
            goalFletching.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalSmithing ----
            labelGoalSmithing.setText("Smithing Level:");
            // ---- spinnerTaskStop ----
            goalSmithing.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalCooking ----
            labelGoalCooking.setText("Cooking Level:");
            // ---- spinnerTaskStop ----
            goalCooking.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalFiremaking ----
            labelGoalFiremaking.setText("Firemaking Level:");
            // ---- spinnerTaskStop ----
            goalFiremaking.setModel(new SpinnerNumberModel(99, 1, 99, 1));
            // ---- labelGoalRunecrafting ----
            labelGoalRunecrafting.setText("Runecrafting Level:");
            // ---- spinnerTaskStop ----
            goalRunecrafting.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalConstruction ----
            labelGoalConstruction.setText("Construction Level:");
            // ---- spinnerTaskStop ----
            goalConstruction.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        }

        // ======== vars ========
        {
            panelSkillingGoalsArtisan.add(labelGoalHerblore);
//            panelSkillingGoalsArtisan.add(goalHerblore);
            panelSkillingGoalsArtisan.add(labelGoalCrafting);
//            panelSkillingGoalsArtisan.add(goalCrafting);
            panelSkillingGoalsArtisan.add(labelGoalFletching);
//            panelSkillingGoalsArtisan.add(goalFletching);
            panelSkillingGoalsArtisan.add(labelGoalSmithing);
//            panelSkillingGoalsArtisan.add(goalSmithing);
            panelSkillingGoalsArtisan.add(labelGoalCooking);
//            panelSkillingGoalsArtisan.add(goalCooking);
            panelSkillingGoalsArtisan.add(labelGoalFiremaking);
            panelSkillingGoalsArtisan.add(goalFiremaking);
            panelSkillingGoalsArtisan.add(labelGoalRunecrafting);
//            panelSkillingGoalsArtisan.add(goalRunecrafting);
            panelSkillingGoalsArtisan.add(labelGoalConstruction);
//            panelSkillingGoalsArtisan.add(goalConstruction);
        }
        TabSkillingGoals.artisan = true;
        TabSkillingGoals.create();
    }

    public static void setAccount_Artisan(Boolean b) {
        goalHerblore.setEnabled(b);
        goalHerblore.setValue(1);
        goalCrafting.setEnabled(b);
        goalCrafting.setValue(1);
        goalFletching.setEnabled(b);
        goalFletching.setValue(1);
        goalSmithing.setEnabled(b);
        goalSmithing.setValue(1);
        goalCooking.setEnabled(b);
        goalCooking.setValue(1);
        goalFiremaking.setEnabled(b);
        goalFiremaking.setValue(1);
        goalRunecrafting.setEnabled(b);
        goalRunecrafting.setValue(1);
        goalConstruction.setEnabled(b);
        goalConstruction.setValue(1);
    }
}
