package net.automaters.gui.tabbed_panel;


import net.automaters.gui.tabbed_panel.settings.Account;
import net.automaters.gui.tabbed_panel.settings.Task;
import net.automaters.gui.tabbed_panel.settings.Webhook;

import javax.swing.*;

import static net.automaters.gui.GUI.tabSettingsInitialized;
import static net.automaters.script.AutomateRS.debug;

public class TabSettings {

    public static JPanel panelTabSettings;

    public static boolean accountSettings;
    public static boolean taskSettings;
    public static boolean webhookSettings;

    public static void create() {
        if (!accountSettings) {
            debug("Account.create()");
            Account.create();
        } else if (!taskSettings) {
            debug("Task.create()");
            Task.create();
        } else if (!webhookSettings) {
            debug("Webhook.create()");
            Webhook.create();
        } else {
            tabSettingsInitialized = true;
            debug("tabSettingsInitialized = TRUE");
        }

    }

}