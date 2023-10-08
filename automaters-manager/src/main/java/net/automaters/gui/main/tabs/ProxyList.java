package net.automaters.gui.main.tabs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.automaters.AutomateManager.managerWidth;
import static net.automaters.gui.main.MainComponents.*;
import static net.automaters.gui.main.tabs.BotList.pathProxyFile;

public class ProxyList {
    
    public static JPanel panelProxyList = new JPanel();
    public static JPanel panelImportProxy = new JPanel();
    public static JTextField textFieldProxyInput = new JTextField();
    public static JButton buttonAddProxy = new JButton("Add");
    public static JButton buttonDeleteProxy = new JButton("Delete");
    public static JButton buttonImportProxy = new JButton("Bulk Import");

    public ProxyList() {
        initProxyList();
    }
    
    private void initProxyList() {
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(panelProxyList);

        panelProxyList.setLayout(new BoxLayout(panelProxyList, BoxLayout.Y_AXIS));

        panelImportProxy.setBounds(0, 200, managerWidth, 100);
        panelImportProxy.setLayout(null);

        textFieldProxyInput.setBounds(10, 32, 400, 23);
        textFieldProxyInput.setColumns(10);

        buttonAddProxy.setBounds(420, 32, 90, 23);
        buttonDeleteProxy.setBounds(520, 32, 90, 23);
        buttonImportProxy.setBounds(620, 32, 155, 23);

        buttonAddProxy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the text from the textFieldProxyInput JTextField
                String proxy = textFieldProxyInput.getText();

                // Check if the proxy is not empty
                if (!proxy.isEmpty()) {
                    try {

                        // Create the parent directories if they don't exist
                        Files.createDirectories(pathProxyFile.getParent());

                        // Create the file if it doesn't exist
                        if (!Files.exists(pathProxyFile)) {
                            Files.createFile(pathProxyFile);
                        }

                        // Append the proxy to the file
                        Files.write(pathProxyFile, (proxy + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                        // Clear the textFieldProxyInput
                        textFieldProxyInput.setText("");
                        populateProxyList(); // Repopulate the proxy list
                        populateBotList();// Repopulate the bot list
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        buttonDeleteProxy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                for (Component component : panelProxyList.getComponents()) {
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
                                    List<String> botLines = Files.readAllLines(pathProxyFile);

                                    // Remove the botName from the list using a case-insensitive comparison
                                    botLines.removeIf(line -> line.trim().equalsIgnoreCase(botName));

                                    // Write the updated list back to the file
                                    Files.write(pathProxyFile, botLines);

                                    // Remove the row from panelBotListActual
                                    panelProxyList.remove(rowPanel);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }

                // Revalidate and repaint panelBotListActual after processing all checkboxes
                panelProxyList.revalidate();
                panelProxyList.repaint();
                populateBotList();
            }
        });

        buttonImportProxy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                /// import proxy button
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
                        Set<String> proxylistLines = new HashSet<String>();
                        if (Files.exists(pathProxyFile)) {
                            Files.lines(pathProxyFile).forEach(proxylistLines::add);
                        }

                        // Check each line from the selected file
                        for (String line : selectedFileLines) {
                            if (!proxylistLines.contains(line)) {
                                // If the line is not in botlist.txt, add it
                                proxylistLines.add(line);
                            }
                        }

                        // Write the updated botlist.txt
                        Files.write(pathProxyFile, proxylistLines);

                        // Optionally, display a message to inform the user
                        JOptionPane.showMessageDialog(null, "File added to proxylist.txt");

                        populateProxyList();
                        populateBotList();// Repopulate the bot list

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error reading or writing files.");
                    }
                }
            }
        });

        tabProxyList.add(scrollPane);
        tabProxyList.add(panelImportProxy);

        panelImportProxy.add(textFieldProxyInput);
        panelImportProxy.add(buttonAddProxy);
        panelImportProxy.add(buttonDeleteProxy);
        panelImportProxy.add(buttonImportProxy);

    }
}
