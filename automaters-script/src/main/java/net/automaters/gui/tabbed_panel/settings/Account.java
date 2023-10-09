package net.automaters.gui.tabbed_panel.settings;

import net.automaters.gui.data.Constants;
import net.automaters.gui.tabbed_panel.TabSettings;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static net.automaters.gui.data.Constants.accountType.*;
import static net.automaters.gui.tabbed_panel.TabSettings.accountSettings;

public class Account {

    public static JPanel panelAccountSettings;
    public static JLabel labelAccountCategory;
    public static JLabel labelAccountType;
    public static JComboBox<String> comboBoxAccountCategory;
    public static JComboBox<String> comboBoxAccountType;

    public static void create() {
        panelAccountSettings = new JPanel(null);
        panelAccountSettings.setBounds(10, 10, 730, 85);

        labelAccountCategory = new JLabel();
        labelAccountCategory.setHorizontalAlignment(SwingConstants.TRAILING);
        labelAccountCategory.setBounds(156, 25, 150, 14);

        labelAccountType = new JLabel();
        labelAccountType.setHorizontalAlignment(SwingConstants.TRAILING);
        labelAccountType.setBounds(156, 50, 150, 14);

        comboBoxAccountCategory = new JComboBox<>();
        comboBoxAccountType = new JComboBox<>();

        // ======== panelAccountSettings ========
        {
            panelAccountSettings.setBorder(new TitledBorder(null, "Account Settings", TitledBorder.CENTER,
                    TitledBorder.TOP, new Font(null, Font.BOLD, 12), Color.decode("#9f70d0")));

            // ---- labelAccountCategory ----
            labelAccountCategory.setText("Account Category:");
            // ---- labelAccountType ----
            labelAccountType.setText("Account Type:");

        }

        // ======== vars ========
        {
            panelAccountSettings.add(labelAccountCategory);
            panelAccountSettings.add(labelAccountType);
            // ---- comboBoxAccountType ----
            comboBoxAccountType.setBounds(324, 50, 150, 20);
            comboBoxAccountType.setModel(new DefaultComboBoxModel<>(TESTING));
            panelAccountSettings.add(comboBoxAccountType);
            // ---- comboBoxAccountCategory ----
            comboBoxAccountCategory.setBounds(324, 22, 150, 20);
            comboBoxAccountCategory.addActionListener(e -> {
                if (!comboBoxAccountCategory.getSelectedItem().toString().equals("LOADED PROFILE")) {
                    {
                        if ("PVM".equals(comboBoxAccountCategory.getSelectedItem().toString())) {
                            comboBoxAccountType.setEnabled(true);
                            comboBoxAccountType.removeAllItems();
                            comboBoxAccountCategory.removeItem("LOADED PROFILE");
                            for (String element : PVM) {
                                comboBoxAccountType.addItem(element);
                            }
                        } else if ("PVP".equals(comboBoxAccountCategory.getSelectedItem().toString())) {
                            comboBoxAccountType.setEnabled(true);
                            comboBoxAccountType.removeAllItems();
                            comboBoxAccountCategory.removeItem("LOADED PROFILE");
                            for (String element : PVP) {
                                comboBoxAccountType.addItem(element);
                            }
                        } else if ("Testing".equals(comboBoxAccountCategory.getSelectedItem().toString())) {
                            comboBoxAccountType.setEnabled(true);
                            comboBoxAccountType.removeAllItems();
                            comboBoxAccountCategory.removeItem("LOADED PROFILE");
                            for (String element : TESTING) {
                                comboBoxAccountType.addItem(element);
                            }
                        } else if ("Money Making".equals(comboBoxAccountCategory.getSelectedItem().toString())) {
                            comboBoxAccountType.setEnabled(true);
                            comboBoxAccountType.removeAllItems();
                            comboBoxAccountCategory.removeItem("LOADED PROFILE");
                            for (String element : MONEY_MAKING) {
                                comboBoxAccountType.addItem(element);
                            }
                        } else if ("Ironman".equals(comboBoxAccountCategory.getSelectedItem().toString())) {
                            comboBoxAccountType.setEnabled(true);
                            comboBoxAccountType.removeAllItems();
                            comboBoxAccountCategory.removeItem("LOADED PROFILE");
                            for (String element : IRONMAN) {
                                comboBoxAccountType.addItem(element);
                            }
                        } else if ("Free Builds".equals(comboBoxAccountCategory.getSelectedItem().toString())) {
                            comboBoxAccountType.setEnabled(true);
                            comboBoxAccountType.removeAllItems();
                            comboBoxAccountCategory.removeItem("LOADED PROFILE");
                            for (String element : FREE_BUILDS) {
                                comboBoxAccountType.addItem(element);
                            }
                        } else if ("Full Access".equals(comboBoxAccountCategory.getSelectedItem().toString())) {
                            comboBoxAccountType.setEnabled(true);
                            comboBoxAccountType.removeAllItems();
                            comboBoxAccountCategory.removeItem("LOADED PROFILE");
                            for (String element : FULL_ACCESS) {
                                comboBoxAccountType.addItem(element);
                            }
                        } else if ("Mules".equals(comboBoxAccountCategory.getSelectedItem().toString())) {
                            comboBoxAccountType.setEnabled(true);
                            comboBoxAccountType.removeAllItems();
                            comboBoxAccountCategory.removeItem("LOADED PROFILE");
                            for (String element : MULES) {
                                comboBoxAccountType.addItem(element);
                            }
                        } else {
                            comboBoxAccountType.setEnabled(false);
                            comboBoxAccountType.removeAllItems();
                            comboBoxAccountCategory.removeItem("LOADED PROFILE");
                            comboBoxAccountType.addItem("NOT OWNED");
                        }
                    }
                }
            });
            comboBoxAccountCategory
                    .setModel(new DefaultComboBoxModel<>(Constants.accountCategory.ALL_CATEGORIES));
            panelAccountSettings.add(comboBoxAccountCategory);
        }
        accountSettings = true;
        TabSettings.create();
    }
}
