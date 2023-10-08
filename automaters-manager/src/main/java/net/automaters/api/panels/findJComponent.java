package net.automaters.api.panels;

import javax.swing.*;
import java.awt.*;

public class findJComponent {

    public static JComboBox<String> findDropdown(JPanel panel, String name) {
        Component[] components = panel.getComponents();

        for (Component component : components) {
            if (component instanceof JComboBox) {
                JComboBox<String> comboBox = (JComboBox<String>) component;
                if (comboBox.getName() != null && comboBox.getName().equals(name)) {
                    return comboBox;
                }
            }
        }

        return null; // Return null if the JComboBox is not found
    }

    // finding checkbox
    public static JCheckBox findCheckbox(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JCheckBox) {
                return (JCheckBox) component;
            }
        }
        return null; // Return null if no checkbox is found
    }

    // finding botname
    public static JLabel findLabel(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JLabel) {
                return (JLabel) component;
            }
        }
        return null; // Return null if no JLabel is found
    }

}
