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

    public static JSpinner spinnerGoalAgility;
    public static JSpinner spinnerGoalThieving;
    public static JSpinner spinnerGoalSlayer;

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
            spinnerGoalAgility = new JSpinner();
            spinnerGoalAgility.setBounds(150, 18, 50, 20);
            spinnerGoalThieving = new JSpinner();
            spinnerGoalThieving.setBounds(150, 43, 50, 20);
            spinnerGoalSlayer = new JSpinner();
            spinnerGoalSlayer.setBounds(150, 68, 50, 20);

            setImage("images/skillIcons/Agility.png", labelGoalAgility);
            setImage("images/skillIcons/Thieving.png", labelGoalThieving);
            setImage("images/skillIcons/Slayer.png", labelGoalSlayer);
        }

        // ======== panelSkillingGoalsSupport ========
        {
            panelSkillingGoalsSupport.setBorder(new TitledBorder(null, "|     SUPPORT     |", TitledBorder.CENTER,
                    TitledBorder.TOP, new Font(null, Font.BOLD, 12), Color.decode("#9f70d0")));

            // ---- labelGoalAgility ----
            labelGoalAgility.setText("Agility Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalAgility.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalFishing ----
            labelGoalThieving.setText("Thieving Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalThieving.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            // ---- labelGoalWoodcutting ----
            labelGoalSlayer.setText("Slayer Level:");
            // ---- spinnerTaskStop ----
            spinnerGoalSlayer.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        }

        // ======== vars ========
        {
            panelSkillingGoalsSupport.add(labelGoalAgility);
            panelSkillingGoalsSupport.add(spinnerGoalAgility);
            panelSkillingGoalsSupport.add(labelGoalThieving);
            panelSkillingGoalsSupport.add(spinnerGoalThieving);
            panelSkillingGoalsSupport.add(labelGoalSlayer);
            panelSkillingGoalsSupport.add(spinnerGoalSlayer);
        }
        TabSkillingGoals.support = true;
        TabSkillingGoals.create();
    }

    public static void setAccount_Support(Boolean b) {
        spinnerGoalAgility.setEnabled(b);
        spinnerGoalAgility.setValue(1);
        spinnerGoalThieving.setEnabled(b);
        spinnerGoalThieving.setValue(1);
        spinnerGoalSlayer.setEnabled(b);
        spinnerGoalSlayer.setValue(1);
    }
}
