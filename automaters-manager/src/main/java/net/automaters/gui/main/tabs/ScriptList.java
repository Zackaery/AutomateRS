package net.automaters.gui.main.tabs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.automaters.AutomateManager.managerWidth;
import static net.automaters.gui.main.MainComponents.*;
import static net.automaters.gui.main.tabs.BotList.pathScriptFile;

public class ScriptList {

    public static JPanel buttonScriptPanel = new JPanel();
    public static JPanel panelScriptList = new JPanel();
    
    public static JTextField textFieldScriptInput = new JTextField();
    
    public static JButton buttonAddScript = new JButton("Add");
    public static JButton buttonDeleteScript = new JButton("Delete");
    public static JButton buttonImportScript = new JButton("Bulk Import");
    
    public ScriptList() {
        initScriptList();
    }
    
    private void initScriptList() {
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(panelScriptList);

        panelScriptList.setLayout(new BoxLayout(panelScriptList, BoxLayout.Y_AXIS));
        buttonScriptPanel.setLayout(null);
        textFieldScriptInput.setColumns(10);

        buttonScriptPanel.setBounds(0, 200, managerWidth, 100);
        textFieldScriptInput.setBounds(10, 32, 400, 23);
        buttonAddScript.setBounds(420, 32, 90, 23);
        buttonDeleteScript.setBounds(520, 32, 90, 23);
        buttonImportScript.setBounds(620, 32, 155, 23);

        buttonScriptPanel.add(textFieldScriptInput);
        buttonScriptPanel.add(buttonAddScript);
        buttonScriptPanel.add(buttonDeleteScript);
        buttonScriptPanel.add(buttonImportScript);
        
        tabScriptList.add(scrollPane);
        tabScriptList.add(buttonScriptPanel);


        buttonDeleteScript.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                for (Component component : panelScriptList.getComponents()) {
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
                                String botName = nameLabel.getText().trim(); // Trim whitespace

                                try {
                                    // Read the file
                                    List<String> botLines = Files.readAllLines(pathScriptFile);

                                    // Remove the botName from the list using a case-insensitive comparison
                                    botLines.removeIf(line -> line.trim().equalsIgnoreCase(botName));

                                    // Write the updated list back to the file
                                    Files.write(pathScriptFile, botLines);

                                    // Remove the row from panelBotListActual
                                    panelScriptList.remove(rowPanel);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }

                // Revalidate and repaint panelBotListActual after processing all checkboxes
                panelScriptList.revalidate();
                panelScriptList.repaint();
                populateBotList();
            }
        });

        buttonAddScript.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the text from the ScriptInputField JTextField
                String proxy = textFieldScriptInput.getText();

                // Check if the script is not empty
                if (!proxy.isEmpty()) {
                    try {

                        // Create the parent directories if they don't exist
                        Files.createDirectories(pathScriptFile.getParent());

                        // Create the file if it doesn't exist
                        if (!Files.exists(pathScriptFile)) {
                            Files.createFile(pathScriptFile);
                        }

                        // Append the script to the file
                        Files.write(pathScriptFile, (proxy + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                        // Clear the textFieldProfileInput
                        textFieldScriptInput.setText("");
                        populateScriptList(); // Repopulate the proxy list
                        populateBotList();// Repopulate the bot list
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        buttonImportScript.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

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

                        // Read the lines from scriptlist.txt
                        Set<String> scriptlistLines = new HashSet<String>();
                        if (Files.exists(pathScriptFile)) {
                            Files.lines(pathScriptFile).forEach(scriptlistLines::add);
                        }

                        // Check each line from the selected file
                        for (String line : selectedFileLines) {
                            if (!scriptlistLines.contains(line)) {
                                // If the line is not in botlist.txt, add it
                                scriptlistLines.add(line);
                            }
                        }

                        // Write the updated botlist.txt
                        Files.write(pathScriptFile, scriptlistLines);

                        // Optionally, display a message to inform the user
                        JOptionPane.showMessageDialog(null, "File added to scriptlist.txt");

                        populateScriptList();
                        populateBotList();// Repopulate the bot list

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error reading or writing files.");
                    }
                }

            }
        });

    }
}
