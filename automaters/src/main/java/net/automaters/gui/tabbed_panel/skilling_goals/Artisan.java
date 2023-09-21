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

    public static JSpinner spinnerGoalHerblore;
    public static JSpinner spinnerGoalCrafting;
    public static JSpinner spinnerGoalFletching;
    public static JSpinner spinnerGoalSmithing;
    public static JSpinner spinnerGoalCooking;
    public static JSpinner spinnerGoalFiremaking;
    public static JSpinner spinnerGoalRunecrafting;
    public static JSpinner spinnerGoalConstruction;

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
            spinnerGoalHerblore = new JSpinner();
            spinnerGoalHerblore.setBounds(165, 16, 50, 20);
            spinnerGoalCrafting = new JSpinner();
            spinnerGoalCrafting.setBounds(165, 37, 50, 20);
            spinnerGoalFletching = new JSpinner();
            spinnerGoalFletching.setBounds(165, 60, 50, 20);
            spinnerGoalSmithing = new JSpinner();
            spinnerGoalSmithing.setBounds(165, 83, 50, 20);
            spinnerGoalCooking = new JSpinner();
            spinnerGoalCooking.setBounds(165, 103, 50, 20);
            spinnerGoalFiremaking = new JSpinner();
            spinnerGoalFiremaking.setBounds(165, 125, 50, 20);
            spinnerGoalRunecrafting = new JSpinner();
            spinnerGoalRunecrafting.setBounds(165, 149, 50, 20);
            spinnerGoalConstruction = new JSpinner();
            spinnerGoalConstruction.setBounds(165, 174, 50, 20);

            setImage("skillIcons/Herblore.png", labelGoalHerblore);
            setImage("skillIcons/Crafting.png", labelGoalCrafting);
            setImage("skillIcons/Fletching.png", labelGoalFletching);
            setImage("skillIcons/Smithing.png", labelGoalSmithing);
            setImage("skillIcons/Cooking.png", labelGoalCooking);
            setImage("skillIcons/Firemaking.png", labelGoalFiremaking);
            setImage("skillIcons/Runecrafting.png", labelGoalRunecrafting);
            setImage("skillIcons/Construction.png", labelGoalConstruction);

        }

        // ======== panelSkillingGoalsArtisan ========
        {
            panelSkillingGoalsArtisan.setBorder(new TitledBorder(null, "|     ARTISAN     |", TitledBorder.CENTER,
                    TitledBorder.TOP, new Font(null, Font.BOLD, 12), Color.decode("#9f70d0")));

            // ---- labelGoalHerblore ----
            labelGoalHerblore.setText("Herblore Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalHerblore.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalCrafting ----
            labelGoalCrafting.setText("Crafting Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalCrafting.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalFletching ----
            labelGoalFletching.setText("Fletching Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalFletching.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalSmithing ----
            labelGoalSmithing.setText("Smithing Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalSmithing.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalCooking ----
            labelGoalCooking.setText("Cooking Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalCooking.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalFiremaking ----
            labelGoalFiremaking.setText("Firemaking Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalFiremaking.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalRunecrafting ----
            labelGoalRunecrafting.setText("Runecrafting Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalRunecrafting.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalConstruction ----
            labelGoalConstruction.setText("Construction Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalConstruction.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        }

        // ======== vars ========
        {
            panelSkillingGoalsArtisan.add(labelGoalHerblore);
            panelSkillingGoalsArtisan.add(spinnerGoalHerblore);
            panelSkillingGoalsArtisan.add(labelGoalCrafting);
            panelSkillingGoalsArtisan.add(spinnerGoalCrafting);
            panelSkillingGoalsArtisan.add(labelGoalFletching);
            panelSkillingGoalsArtisan.add(spinnerGoalFletching);
            panelSkillingGoalsArtisan.add(labelGoalSmithing);
            panelSkillingGoalsArtisan.add(spinnerGoalSmithing);
            panelSkillingGoalsArtisan.add(labelGoalCooking);
            panelSkillingGoalsArtisan.add(spinnerGoalCooking);
            panelSkillingGoalsArtisan.add(labelGoalFiremaking);
            panelSkillingGoalsArtisan.add(spinnerGoalFiremaking);
            panelSkillingGoalsArtisan.add(labelGoalRunecrafting);
            panelSkillingGoalsArtisan.add(spinnerGoalRunecrafting);
            panelSkillingGoalsArtisan.add(labelGoalConstruction);
            panelSkillingGoalsArtisan.add(spinnerGoalConstruction);
        }
        TabSkillingGoals.artisan = true;
        TabSkillingGoals.create();
    }

    public static void setAccount_Artisan(Boolean b) {
        spinnerGoalHerblore.setEnabled(b);
        spinnerGoalHerblore.setValue(1);
        spinnerGoalCrafting.setEnabled(b);
        spinnerGoalCrafting.setValue(1);
        spinnerGoalFletching.setEnabled(b);
        spinnerGoalFletching.setValue(1);
        spinnerGoalSmithing.setEnabled(b);
        spinnerGoalSmithing.setValue(1);
        spinnerGoalCooking.setEnabled(b);
        spinnerGoalCooking.setValue(1);
        spinnerGoalFiremaking.setEnabled(b);
        spinnerGoalFiremaking.setValue(1);
        spinnerGoalRunecrafting.setEnabled(b);
        spinnerGoalRunecrafting.setValue(1);
        spinnerGoalConstruction.setEnabled(b);
        spinnerGoalConstruction.setValue(1);
    }
}
