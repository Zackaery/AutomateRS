package net.automaters.gui.main.tabs;

import javax.swing.*;

import static net.automaters.AutomateManager.managerWidth;
import static net.automaters.api.panels.readContents.checkAndCreateFiles;
import static net.automaters.gui.main.MainComponents.*;

public class Scheduling {

    public static JTabbedPane tabbedPanelScheduling = new JTabbedPane(JTabbedPane.TOP);
    public static JPanel panelRunSchedule = new JPanel();
    public static JPanel panelCreateSchedule = new JPanel();

    public Scheduling() {
        initScheduling();
    }

    private void initScheduling() {
        tabbedPanelScheduling.setBounds(0, 0, managerWidth, 260);
        tabbedPanelScheduling.addTab("Start-Stop-Delete", null, panelRunSchedule, null);
        tabbedPanelScheduling.addTab("Create Schedule", null, panelCreateSchedule, null);

        tabSchedulePanel.add(tabbedPanelScheduling);
    }

}
