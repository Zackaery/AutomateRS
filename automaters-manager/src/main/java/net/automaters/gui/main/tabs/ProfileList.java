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
import static net.automaters.gui.main.tabs.BotList.pathProfileFile;

public class ProfileList {

    public static JPanel panelProfileList = new JPanel();
    public static JPanel panelImportProfile = new JPanel();
    public static JTextField textFieldProfileInput = new JTextField();
    public static JButton buttonAddProfile = new JButton("Add");
    public static JButton buttonDeleteProfile = new JButton("Delete");
    public static JButton buttonImportProfile = new JButton("Bulk Import");

    public ProfileList() {
        initProfileList();
    }

    private void initProfileList() {

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(panelProfileList);

        panelProfileList.setLayout(new BoxLayout(panelProfileList, BoxLayout.Y_AXIS));

        panelImportProfile.setBounds(0, 200, managerWidth, 100);
        panelImportProfile.setLayout(null);

        textFieldProfileInput.setBounds(10, 32, 400, 23);
        textFieldProfileInput.setColumns(10);

        buttonAddProfile.setBounds(420, 32, 90, 23);
        buttonDeleteProfile.setBounds(520, 32, 90, 23);
        buttonImportProfile.setBounds(620, 32, 155, 23);


        buttonImportProfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                /// import profile button
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

                        // Read the lines from botlist.txt
                        Set<String> profilelistLines = new HashSet<String>();
                        if (Files.exists(pathProfileFile)) {
                            Files.lines(pathProfileFile).forEach(profilelistLines::add);
                        }

                        // Check each line from the selected file
                        for (String line : selectedFileLines) {
                            if (!profilelistLines.contains(line)) {
                                // If the line is not in botlist.txt, add it
                                profilelistLines.add(line);
                            }
                        }

                        // Write the updated botlist.txt
                        Files.write(pathProfileFile, profilelistLines);

                        // Optionally, display a message to inform the user
                        JOptionPane.showMessageDialog(null, "File added to profilelist.txt");

                        populateProfileList();
                        populateBotList();// Repopulate the bot list

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error reading or writing files.");
                    }
                }

            }
        });

        buttonAddProfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the text from the textFieldProfileInput JTextField
                String proxy = textFieldProfileInput.getText();

                // Check if the profile is not empty
                if (!proxy.isEmpty()) {
                    try {

                        // Create the parent directories if they don't exist
                        Files.createDirectories(pathProfileFile.getParent());

                        // Create the file if it doesn't exist
                        if (!Files.exists(pathProfileFile)) {
                            Files.createFile(pathProfileFile);
                        }

                        // Append the profile to the file
                        Files.write(pathProfileFile, (proxy + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                        // Clear the textFieldProfileInput
                        textFieldProfileInput.setText("");
                        populateProfileList(); // Repopulate the proxy list
                        populateBotList();// Repopulate the bot list
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

        buttonDeleteProfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                for (Component component : panelProfileList.getComponents()) {
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
                                    List<String> botLines = Files.readAllLines(pathProfileFile);

                                    // Remove the botName from the list using a case-insensitive comparison
                                    botLines.removeIf(line -> line.trim().equalsIgnoreCase(botName));

                                    // Write the updated list back to the file
                                    Files.write(pathProfileFile, botLines);

                                    // Remove the row from panelBotListActual
                                    panelProfileList.remove(rowPanel);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }

                // Revalidate and repaint panelBotListActual after processing all checkboxes
                panelProfileList.revalidate();
                panelProfileList.repaint();
                populateBotList();
            }
        });


        panelImportProfile.add(textFieldProfileInput);
        panelImportProfile.add(buttonAddProfile);
        panelImportProfile.add(buttonDeleteProfile);
        panelImportProfile.add(buttonImportProfile);

        tabProfileList.add(scrollPane);
        tabProfileList.add(panelImportProfile);
    }

}
