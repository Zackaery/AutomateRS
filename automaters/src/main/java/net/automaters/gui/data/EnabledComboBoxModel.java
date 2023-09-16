package net.automaters.gui.data;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EnabledComboBoxModel<E> extends DefaultComboBoxModel<E> {
    private final Map<E, Boolean> itemEnabled = new HashMap<>();

    public EnabledComboBoxModel(E[] items) {
        super(items);
        for (E item : items) {
            itemEnabled.put(item, true);
        }
    }

    @Override
    public E getElementAt(int index) {
        E item = super.getElementAt(index);
        if (!itemEnabled.get(item)) {
            return null;
        }
        return item;
    }

    public void setEnabled(E item, boolean enabled) {
        itemEnabled.put(item, enabled);
    }

    public void updateComboBoxEnabledStates(JComboBox<String> comboBox, String directory) {
        ComboBoxModel<String> model = comboBox.getModel();

        // Loop through the items and set their enabled state based on whether the file exists
        for (int i = 0; i < model.getSize(); i++) {
            String item = model.getElementAt(i);
            File file = new File(directory + "\\" + item + ".txt");
            if (model instanceof EnabledComboBoxModel) {
                ((EnabledComboBoxModel<String>) model).setEnabled(item, file.exists());
            }
        }
    }
}