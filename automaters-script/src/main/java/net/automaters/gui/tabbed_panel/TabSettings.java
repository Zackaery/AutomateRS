package net.automaters.gui.tabbed_panel;


import net.automaters.gui.tabbed_panel.settings.Account;
import net.automaters.gui.tabbed_panel.settings.Task;
import net.automaters.gui.tabbed_panel.settings.Webhook;

import javax.swing.*;

import static net.automaters.gui.GUI.tabSettingsInitialized;
import static net.automaters.api.utils.Debug.debug;

public class TabSettings {

    public static JPanel panelTabSettings;

    public static boolean accountSettings;
    public static boolean taskSettings;
    public static boolean webhookSettings;

    public static void create() {
        if (!accountSettings) {
            Account.create();
        } else if (!taskSettings) {
            Task.create();
        } else if (!webhookSettings) {
            Webhook.create();
        } else {
            tabSettingsInitialized = true;
        }

    }

}
