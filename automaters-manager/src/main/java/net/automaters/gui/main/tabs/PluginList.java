package net.automaters.gui.main.tabs;

import net.automaters.api.ui.DynamicGridLayout;
import net.automaters.api.panels.InfoPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.awt.BorderLayout.CENTER;
import static net.automaters.AutomateManager.managerWidth;
import static net.automaters.gui.main.MainComponents.*;

public class PluginList {

    public static JPanel panelScriptInfo = new JPanel();
    private final InfoPanel infoPanel = new InfoPanel();
    
    public static JTextField textFieldScriptInput = new JTextField();
    
    public static JButton buttonAddScript = new JButton("Add");
    public static JButton buttonDeleteScript = new JButton("Delete");
    public static JButton buttonImportScript = new JButton("Bulk Import");
    
    public PluginList() {
        initPluginList();
    }

    private void createInfoPanel(JPanel panel, String title, String description) {
        panel.setLayout(new DynamicGridLayout(2, 1, 0, 0));
        panel.setBounds(0, 0, managerWidth-100, 100);
        infoPanel.setBounds(0, 0, managerWidth, 100);
        infoPanel.setContent(title, description);
        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(new JSeparator(), CENTER);
    }

    private void createTextField(JTextField textField, String placeholder) {
        Font italicFont = new Font(textField.getFont().getName(), Font.ITALIC, textField.getFont().getSize());
        Font normalFont = new Font(textField.getFont().getName(), Font.PLAIN, textField.getFont().getSize());

        textField.setColumns(10);
        textField.setText(placeholder);
        textField.setBounds(10, 10, 300, 25);
        textField.setForeground(Color.GRAY);
        textField.setFont(italicFont);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setFont(normalFont);
                    textField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setFont(italicFont);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }
    
    private void initPluginList() {

        JScrollPane scrollPane = new JScrollPane();
        initScrollPane(scrollPane, panelPluginList);

        createInfoPanel(panelScriptInfo, "Adding Plugins", "Format:<br><i>Exact Plugin Name</i></html>");
        createTextField(textFieldScriptInput, "AutomateRS");

        panelPluginList.setLayout(new BoxLayout(panelPluginList, BoxLayout.Y_AXIS));
        panelPluginComponents.setLayout(null);
        textFieldScriptInput.setColumns(10);

        panelPluginComponents.setBounds(0, 300, managerWidth, 45);
        buttonAddScript.setBounds(320, 10, 90, 25);
        buttonDeleteScript.setBounds(420, 10, 90, 25);
        buttonImportScript.setBounds(520, 10, 155, 25);

        buttonDeleteScript.addActionListener(e -> {
            for (Component component : panelPluginList.getComponents()) {
                if (component instanceof JPanel) {
                    JPanel rowPanel = (JPanel) component;

                    // Check if the rowPanel contains a checkbox
                    JCheckBox checkbox = null;
                    for (Component innerComponent : rowPanel.getComponents()) {
                        if (innerComponent instanceof JCheckBox) {
                            checkbox = (JCheckBox) innerComponent;
                            break;
                        }
                    }

                    // If a checkbox is found and it's selected, proceed with deletion
                    if (checkbox != null && checkbox.isSelected()) {
                        // Get the label component from the row panel
                        JLabel nameLabel = null;
                        for (Component innerComponent : rowPanel.getComponents()) {
                            if (innerComponent instanceof JLabel) {
                                nameLabel = (JLabel) innerComponent;
                                break;
                            }
                        }

                        // If a label was found, get its trimmed text and delete it from the file
                        if (nameLabel != null) {
                            String accountName = nameLabel.getText().trim(); // Trim whitespace

                            try {
                                // Read the file
                                List<String> accountLines = Files.readAllLines(pathPluginFile);

                                // Remove the accountName from the list using a case-insensitive comparison
                                accountLines.removeIf(line -> line.trim().equalsIgnoreCase(accountName));

                                // Write the updated list back to the file
                                Files.write(pathPluginFile, accountLines);

                                // Remove the row from panelAccountListActual
                                panelPluginList.remove(rowPanel);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
            // Revalidate and repaint panelAccountListActual after processing all checkboxes
            panelPluginList.revalidate();
            panelPluginList.repaint();
            populateAccountList();

        });
        buttonAddScript.addActionListener(e -> {
            // Get the text from the ScriptInputField JTextField
            String proxy = textFieldScriptInput.getText();

            // Check if the script is not empty
            if (!proxy.isEmpty()) {
                try {

                    // Create the parent directories if they don't exist
                    Files.createDirectories(pathPluginFile.getParent());

                    // Create the file if it doesn't exist
                    if (!Files.exists(pathPluginFile)) {
                        Files.createFile(pathPluginFile);
                    }

                    // Append the script to the file
                    Files.write(pathPluginFile, (proxy + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                    // Clear the textFieldProfileInput
                    textFieldScriptInput.setText("");
                    populatePluginList(); // Repopulate the proxy list
                    populateAccountList();// Repopulate the account list
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonImportScript.addActionListener(e -> {
            /// import script button
            JFileChooser fileChooser = new JFileChooser();

            // Show the file dialog and get the user's choice
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                Path selectedFilePath = selectedFile.toPath();

                try {
                    // Read the lines from the selected file
                    Set<String> selectedFileLines = new HashSet<String>();
                    Files.lines(selectedFilePath).forEach(selectedFileLines::add);

                    // Read the lines from pluginlist.txt
                    Set<String> pluginlistLines = new HashSet<String>();
                    if (Files.exists(pathPluginFile)) {
                        Files.lines(pathPluginFile).forEach(pluginlistLines::add);
                    }

                    // Check each line from the selected file
                    for (String line : selectedFileLines) {
                        if (!pluginlistLines.contains(line)) {
                            // If the line is not in pluginlist.txt, add it
                            pluginlistLines.add(line);
                        }
                    }

                    // Write the updated pluginlist.txt
                    Files.write(pathPluginFile, pluginlistLines);

                    // Optionally, display a message to inform the user
                    JOptionPane.showMessageDialog(null, "File added to pluginlist.txt");

                    populatePluginList();
                    populateAccountList();// Repopulate the account list

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error reading or writing files.");
                }
            }
        });

        panelPluginComponents.add(textFieldScriptInput);
        panelPluginComponents.add(buttonAddScript);
        panelPluginComponents.add(buttonDeleteScript);
        panelPluginComponents.add(buttonImportScript);

        tabPluginList.add(scrollPane);
        tabPluginList.add(panelPluginComponents);
        tabPluginList.add(panelScriptInfo);

    }
}
