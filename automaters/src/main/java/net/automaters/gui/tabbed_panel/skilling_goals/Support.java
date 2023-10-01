package net.automaters.gui.tabbed_panel.skilling_goals;

import net.automaters.gui.tabbed_panel.TabSkillingGoals;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;

import static net.automaters.gui.GUI.setImage;

public class Support {
    public static JPanel panelSkillingGoalsSupport;

    public static JButton buttonToggleSupport;

    public static JLabel labelGoalAgility;
    public static JLabel labelGoalThieving;
    public static JLabel labelGoalSlayer;

    public static JSpinner goalAgility;
    public static JSpinner goalThieving;
    public static JSpinner goalSlayer;

    public static void create() throws IOException {

        panelSkillingGoalsSupport = new JPanel(null);
        panelSkillingGoalsSupport.setBounds(465, 250, 225, 116);


        // ======== goalSupport ========
        {
            labelGoalAgility = new JLabel();
            labelGoalAgility.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalAgility.setBounds(10, 16, 122, 25);
            labelGoalThieving = new JLabel();
            labelGoalThieving.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalThieving.setBounds(10, 41, 122, 25);
            labelGoalSlayer = new JLabel();
            labelGoalSlayer.setHorizontalAlignment(SwingConstants.TRAILING);
            labelGoalSlayer.setBounds(10, 66, 122, 24);
            goalAgility = new JSpinner();
            goalAgility.setBounds(150, 18, 50, 20);
            goalThieving = new JSpinner();
            goalThieving.setBounds(150, 43, 50, 20);
            goalSlayer = new JSpinner();
            goalSlayer.setBounds(150, 68, 50, 20);

            setImage("gui\\skill_icons\\Agility.png", labelGoalAgility);
            setImage("gui\\skill_icons\\Thieving.png", labelGoalThieving);
            setImage("gui\\skill_icons\\Slayer.png", labelGoalSlayer);
        }

        // ======== panelSkillingGoalsSupport ========
        {
            panelSkillingGoalsSupport.setBorder(new TitledBorder(null, "|     SUPPORT     |", TitledBorder.CENTER,
                    TitledBorder.TOP, new Font(null, Font.BOLD, 12), Color.decode("#9f70d0")));

            // ---- labelGoalAgility ----
            labelGoalAgility.setText("Agility Level:");
            // ---- spinnerTaskStop ----
            goalAgility.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalFishing ----
            labelGoalThieving.setText("Thieving Level:");
            // ---- spinnerTaskStop ----
            goalThieving.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalWoodcutting ----
            labelGoalSlayer.setText("Slayer Level:");
            // ---- spinnerTaskStop ----
            goalSlayer.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        }

        // ======== vars ========
        {
            panelSkillingGoalsSupport.add(labelGoalAgility);
//            panelSkillingGoalsSupport.add(goalAgility);
            panelSkillingGoalsSupport.add(labelGoalThieving);
//            panelSkillingGoalsSupport.add(goalThieving);
            panelSkillingGoalsSupport.add(labelGoalSlayer);
//            panelSkillingGoalsSupport.add(goalSlayer);
        }
        TabSkillingGoals.support = true;
        TabSkillingGoals.create();
    }

    public static void setAccount_Support(Boolean b) {
        goalAgility.setEnabled(b);
        goalAgility.setValue(1);
        goalThieving.setEnabled(b);
        goalThieving.setValue(1);
        goalSlayer.setEnabled(b);
        goalSlayer.setValue(1);
    }
}
