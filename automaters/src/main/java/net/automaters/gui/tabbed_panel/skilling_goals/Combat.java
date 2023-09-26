package net.automaters.gui.tabbed_panel.skilling_goals;

import net.automaters.gui.tabbed_panel.TabSkillingGoals;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;

import static net.automaters.gui.GUI.setImage;

public class Combat {

    public static JPanel panelSkillingGoalsCombat;

    public static JButton buttonToggleCombat;

    public static JLabel labelGoalAttack;
    public static JLabel labelGoalStrength;
    public static JLabel labelGoalDefence;
    public static JLabel labelGoalRanged;
    public static JLabel labelGoalMagic;
    public static JLabel labelGoalPrayer;

    public static JSpinner spinnerGoalAttack;
    public static JSpinner spinnerGoalStrength;
    public static JSpinner spinnerGoalDefence;
    public static JSpinner spinnerGoalRanged;
    public static JSpinner spinnerGoalMagic;
    public static JSpinner spinnerGoalPrayer;

//    public static final Image attackImage = ImageManager.getInstance().loadImage("images/skillIcons/Attack.png");

    public static void create() throws IOException {

        panelSkillingGoalsCombat = new JPanel(null);
        panelSkillingGoalsCombat.setBounds(180, 11, 225, 176);

        // ======== goalCombat ========
        {
            labelGoalAttack = new JLabel();
            labelGoalAttack.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalAttack.setBounds(10, 16, 117, 25);
            labelGoalStrength = new JLabel();
            labelGoalStrength.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalStrength.setBounds(10, 41, 117, 20);
            labelGoalDefence = new JLabel();
            labelGoalDefence.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalDefence.setBounds(10, 62, 117, 19);
            labelGoalRanged = new JLabel();
            labelGoalRanged.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalRanged.setBounds(10, 81, 117, 23);
            labelGoalMagic = new JLabel();
            labelGoalMagic.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalMagic.setBounds(10, 104, 117, 23);
            labelGoalPrayer = new JLabel();
            labelGoalPrayer.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalPrayer.setBounds(10, 127, 117, 23);
            spinnerGoalAttack = new JSpinner();
            spinnerGoalAttack.setBounds(145, 18, 50, 20);
            spinnerGoalStrength = new JSpinner();
            spinnerGoalStrength.setBounds(145, 41, 50, 20);
            spinnerGoalDefence = new JSpinner();
            spinnerGoalDefence.setBounds(145, 61, 50, 20);
            spinnerGoalRanged = new JSpinner();
            spinnerGoalRanged.setBounds(145, 82, 50, 20);
            spinnerGoalMagic = new JSpinner();
            spinnerGoalMagic.setBounds(145, 105, 50, 20);
            spinnerGoalPrayer = new JSpinner();
            spinnerGoalPrayer.setBounds(145, 128, 50, 20);

            setImage("gui\\skill_icons\\Attack.png", labelGoalAttack);
            setImage("gui\\skill_icons\\Strength.png", labelGoalStrength);
            setImage("gui\\skill_icons\\Defence.png", labelGoalDefence);
            setImage("gui\\skill_icons\\Ranged.png", labelGoalRanged);
            setImage("gui\\skill_icons\\Magic.png", labelGoalMagic);
            setImage("gui\\skill_icons\\Prayer.png", labelGoalPrayer);
        }

        // ======== panelSkillingGoalsCombat ========
        {
            panelSkillingGoalsCombat.setBorder(new TitledBorder(null, "|     COMBAT     |", TitledBorder.CENTER,
                    TitledBorder.TOP, new Font(null, Font.BOLD, 12), Color.decode("#9f70d0")));
            // ---- labelGoalAttack ----
            labelGoalAttack.setText("Attack Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalAttack.setModel(new SpinnerNumberModel(10, 1, 99, 1));
            // ---- labelGoalStrength ----
            labelGoalStrength.setText("Strength Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalStrength.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalDefence ----
            labelGoalDefence.setText("Defence Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalDefence.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalRanged ----
            labelGoalRanged.setText("Ranged Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalRanged.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalMagic ----
            labelGoalMagic.setText("Magic Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalMagic.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalPrayer ----
            labelGoalPrayer.setText("Prayer Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalPrayer.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        }

        // ======== vars ========
        {
            panelSkillingGoalsCombat.add(labelGoalAttack);
            panelSkillingGoalsCombat.add(spinnerGoalAttack);
            panelSkillingGoalsCombat.add(labelGoalStrength);
            panelSkillingGoalsCombat.add(spinnerGoalStrength);
            panelSkillingGoalsCombat.add(labelGoalDefence);
            panelSkillingGoalsCombat.add(spinnerGoalDefence);
            panelSkillingGoalsCombat.add(labelGoalRanged);
            panelSkillingGoalsCombat.add(spinnerGoalRanged);
            panelSkillingGoalsCombat.add(labelGoalMagic);
            panelSkillingGoalsCombat.add(spinnerGoalMagic);
            panelSkillingGoalsCombat.add(labelGoalPrayer);
            panelSkillingGoalsCombat.add(spinnerGoalPrayer);
        }
        TabSkillingGoals.combat = true;
        TabSkillingGoals.create();
    }

    public static void setAccount_Combat(Boolean b) {
        spinnerGoalAttack.setEnabled(b);
        spinnerGoalAttack.setValue(1);
        spinnerGoalStrength.setEnabled(b);
        spinnerGoalStrength.setValue(1);
        spinnerGoalDefence.setEnabled(b);
        spinnerGoalDefence.setValue(1);
        spinnerGoalRanged.setEnabled(b);
        spinnerGoalRanged.setValue(1);
        spinnerGoalMagic.setEnabled(b);
        spinnerGoalMagic.setValue(1);
        spinnerGoalPrayer.setEnabled(b);
        spinnerGoalPrayer.setValue(1);
    }


}
