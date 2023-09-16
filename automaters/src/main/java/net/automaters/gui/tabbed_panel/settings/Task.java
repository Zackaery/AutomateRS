package net.automaters.gui.tabbed_panel.settings;

import net.automaters.gui.tabbed_panel.TabSettings;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.concurrent.TimeUnit;

import static net.automaters.gui.tabbed_panel.TabSettings.taskSettings;

public class Task {

    public static JPanel panelTask;
    public static JLabel labelTaskTimer;
    public static JLabel labelTimerH;
    public static JLabel labelTimerM;
    public static JLabel labelTimerS;
    public static JSpinner spinnerTaskTimerH;
    public static JSpinner spinnerTaskTimerM;
    public static JSpinner spinnerTaskTimerS;
    public static JSlider sliderTaskTimerDeviation;

    public static String getDvTaskTimer() {

        int D = sliderTaskTimerDeviation.getValue();
        int H = (int) spinnerTaskTimerH.getValue();
        int M = (int) spinnerTaskTimerM.getValue();
        int S = (int) spinnerTaskTimerS.getValue();
        int Tms = (H * 3600000) + (M * 60000) + (S * 1000);
        int Dms = (Tms / 100) * D;
        int Lms = Tms - Dms;
        int Hms = Tms + Dms;
        int TaskTimerDms = Dms;
        int TaskTimerTms = Tms;
        final long LowS = TimeUnit.MILLISECONDS.toSeconds(Lms);
        final long LowM = TimeUnit.MILLISECONDS.toMinutes(Lms);
        final long LowH = TimeUnit.MILLISECONDS.toHours(Lms);
        final long HighS = TimeUnit.MILLISECONDS.toSeconds(Hms);
        final long HighM = TimeUnit.MILLISECONDS.toMinutes(Hms);
        final long HighH = TimeUnit.MILLISECONDS.toHours(Hms);
        return String.format("%02d", LowH % 24) + ":" + String.format("%02d", LowM % 60) + ":"
                + String.format("%02d", LowS % 60) + " - " + String.format("%02d", HighH % 24) + ":"
                + String.format("%02d", HighM % 60) + ":" + String.format("%02d", HighS % 60);
    }

    public static void create() {

        panelTask = new JPanel(null);
        panelTask.setBounds(10, 95, 730, 155);

        labelTaskTimer = new JLabel();
        labelTaskTimer.setHorizontalAlignment(SwingConstants.CENTER);
        labelTaskTimer.setBounds(280, 21, 160, 30);

        labelTimerH = new JLabel();
        labelTimerH.setHorizontalAlignment(SwingConstants.TRAILING);
        labelTimerH.setBounds(168, 54, 60, 20);
        labelTimerM = new JLabel();
        labelTimerM.setHorizontalAlignment(SwingConstants.TRAILING);
        labelTimerM.setBounds(298, 54, 60, 20);
        labelTimerS = new JLabel();
        labelTimerS.setHorizontalAlignment(SwingConstants.TRAILING);
        labelTimerS.setBounds(428, 54, 60, 20);

        spinnerTaskTimerH = new JSpinner();
        spinnerTaskTimerH.setBounds(238, 54, 50, 20);
        spinnerTaskTimerM = new JSpinner();
        spinnerTaskTimerM.setBounds(368, 54, 50, 20);
        spinnerTaskTimerS = new JSpinner();
        spinnerTaskTimerS.setBounds(498, 54, 50, 20);
        sliderTaskTimerDeviation = new JSlider(0, 100, 30);
        sliderTaskTimerDeviation.setBounds(178, 80, 337, 45);


        // ======== panelTask ========
        {
            panelTask.setBorder(new TitledBorder(null, "Task Settings", TitledBorder.CENTER,
                    TitledBorder.TOP, new Font(null, Font.BOLD, 12), Color.decode("#9f70d0")));

            // ---- labelTaskTime ----
            labelTaskTimer.setEnabled(true);
            labelTimerH.setText("Hours:");
            labelTimerH.setEnabled(true);
            spinnerTaskTimerH.setModel(new SpinnerNumberModel(0, 0, 23, 1));
            spinnerTaskTimerH.setEnabled(true);
            labelTimerM.setText("Minutes:");
            labelTimerM.setEnabled(true);
            spinnerTaskTimerM.setModel(new SpinnerNumberModel(30, 0, 59, 1));
            spinnerTaskTimerM.setEnabled(true);
            labelTimerS.setText("Seconds:");
            labelTimerS.setEnabled(true);
            spinnerTaskTimerS.setModel(new SpinnerNumberModel(0, 0, 59, 1));
            spinnerTaskTimerS.setEnabled(true);

            sliderTaskTimerDeviation.setMajorTickSpacing(25);
            sliderTaskTimerDeviation.setMinorTickSpacing(5);
            sliderTaskTimerDeviation.setPaintLabels(true);
            sliderTaskTimerDeviation.setPaintTicks(true);
            sliderTaskTimerDeviation.setPaintTrack(true);
            sliderTaskTimerDeviation.setAutoscrolls(true);
            sliderTaskTimerDeviation.setEnabled(true);

            labelTaskTimer.setToolTipText("Set your randomized task time, which controls how long you will do a task before switching to a new task.");
            labelTaskTimer.setText("Task Time: " + "\n" + getDvTaskTimer());
            spinnerTaskTimerH.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    labelTaskTimer
                            .setText("Task Time: " + "\n" + getDvTaskTimer());
                }
            });

            spinnerTaskTimerM.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    labelTaskTimer
                            .setText("Task Time: " + "\n" + getDvTaskTimer());
                }
            });

            spinnerTaskTimerS.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    labelTaskTimer
                            .setText("Task Time: " + "\n" + getDvTaskTimer());
                }
            });
        }

        // ======== vars ========
        {
            panelTask.add(labelTaskTimer);
            panelTask.add(labelTimerH);
            panelTask.add(spinnerTaskTimerH);
            panelTask.add(labelTimerM);
            panelTask.add(spinnerTaskTimerM);
            panelTask.add(labelTimerS);
            panelTask.add(spinnerTaskTimerS);
            panelTask.add(sliderTaskTimerDeviation);
        }
        taskSettings = true;
        TabSettings.create();
    }
}
