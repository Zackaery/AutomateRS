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

    public static JSpinner goalAttack;
    public static JSpinner goalStrength;
    public static JSpinner goalDefence;
    public static JSpinner goalRanged;
    public static JSpinner goalMagic;
    public static JSpinner goalPrayer;

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
            goalAttack = new JSpinner();
            goalAttack.setBounds(145, 18, 50, 20);
            goalStrength = new JSpinner();
            goalStrength.setBounds(145, 41, 50, 20);
            goalDefence = new JSpinner();
            goalDefence.setBounds(145, 61, 50, 20);
            goalRanged = new JSpinner();
            goalRanged.setBounds(145, 82, 50, 20);
            goalMagic = new JSpinner();
            goalMagic.setBounds(145, 105, 50, 20);
            goalPrayer = new JSpinner();
            goalPrayer.setBounds(145, 128, 50, 20);

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
            goalAttack.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalStrength ----
            labelGoalStrength.setText("Strength Level:");
            // ---- spinnerTaskStop ----
            goalStrength.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalDefence ----
            labelGoalDefence.setText("Defence Level:");
            // ---- spinnerTaskStop ----
            goalDefence.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalRanged ----
            labelGoalRanged.setText("Ranged Level:");
            // ---- spinnerTaskStop ----
            goalRanged.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalMagic ----
            labelGoalMagic.setText("Magic Level:");
            // ---- spinnerTaskStop ----
            goalMagic.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalPrayer ----
            labelGoalPrayer.setText("Prayer Level:");
            // ---- spinnerTaskStop ----
            goalPrayer.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        }

        // ======== vars ========
        {
            panelSkillingGoalsCombat.add(labelGoalAttack);
//            panelSkillingGoalsCombat.add(goalAttack);
            panelSkillingGoalsCombat.add(labelGoalStrength);
//            panelSkillingGoalsCombat.add(goalStrength);
            panelSkillingGoalsCombat.add(labelGoalDefence);
//            panelSkillingGoalsCombat.add(goalDefence);
            panelSkillingGoalsCombat.add(labelGoalRanged);
//            panelSkillingGoalsCombat.add(goalRanged);
            panelSkillingGoalsCombat.add(labelGoalMagic);
//            panelSkillingGoalsCombat.add(goalMagic);
            panelSkillingGoalsCombat.add(labelGoalPrayer);
//            panelSkillingGoalsCombat.add(goalPrayer);
        }
        TabSkillingGoals.combat = true;
        TabSkillingGoals.create();
    }

    public static void setAccount_Combat(Boolean b) {
        goalAttack.setEnabled(b);
        goalAttack.setValue(1);
        goalStrength.setEnabled(b);
        goalStrength.setValue(1);
        goalDefence.setEnabled(b);
        goalDefence.setValue(1);
        goalRanged.setEnabled(b);
        goalRanged.setValue(1);
        goalMagic.setEnabled(b);
        goalMagic.setValue(1);
        goalPrayer.setEnabled(b);
        goalPrayer.setValue(1);
    }


}
