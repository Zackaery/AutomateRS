package net.automaters.gui.tabbed_panel.settings;

import net.automaters.gui.tabbed_panel.TabSettings;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static net.automaters.gui.tabbed_panel.TabSettings.webhookSettings;

public class Webhook {

    public static JPanel panelWebhookSettings;
    public static JCheckBox checkBoxEnableDiscordWebhooks;

    public static JLabel labelWebhook;
    public static JTextField textFieldWebhook;

    public static void create() {
        panelWebhookSettings = new JPanel(null);
        panelWebhookSettings.setBounds(10, 250, 730, 110);
        checkBoxEnableDiscordWebhooks = new JCheckBox();
        checkBoxEnableDiscordWebhooks.setBounds(12, 23, 266, 23);
        labelWebhook = new JLabel();
        labelWebhook.setBounds(12, 53, 266, 14);
        textFieldWebhook = new JTextField();
        textFieldWebhook.setBounds(12, 71, 687, 25);

        // ======== panelWebhookSettings ========
        {
            panelWebhookSettings.setBorder(new TitledBorder(null, "Discord Webhook Settings", TitledBorder.CENTER,
                    TitledBorder.TOP, new Font(null, Font.BOLD, 12), Color.decode("#9f70d0")));

            // ---- checkBoxEnableDiscordWebhooks ----
            checkBoxEnableDiscordWebhooks.setText("Enable Discord Webhooks");
            checkBoxEnableDiscordWebhooks.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (checkBoxEnableDiscordWebhooks.isSelected()) {
                        labelWebhook.setEnabled(true);
                        textFieldWebhook.setEnabled(true);
                        getWebhook();
                    } else {
                        labelWebhook.setEnabled(false);
                        textFieldWebhook.setEnabled(false);
                        textFieldWebhook.setText("");
                    }
                }
            });
            // ---- labelWebhook ----
            labelWebhook.setText("Discord Webhook:");
            labelWebhook.setEnabled(false);
            textFieldWebhook.setEnabled(false);
        }

        // ======== vars ========
        {
            panelWebhookSettings.add(checkBoxEnableDiscordWebhooks);
            panelWebhookSettings.add(labelWebhook);
            panelWebhookSettings.add(textFieldWebhook);
        }

        webhookSettings = true;
        TabSettings.create();
    }

    public static void getWebhook () {
    }
}
