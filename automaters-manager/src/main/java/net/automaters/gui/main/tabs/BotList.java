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

public class BotList {

    public static JPanel panelBotListActual = new JPanel();
    public static JPanel panelImportBot = new JPanel();

    public static JTextField textFieldBotInput = new JTextField();

    public static JButton buttonAddBot = new JButton("Add");
    public static JButton buttonDeleteBot = new JButton("Delete");
    public static JButton buttonImportBot = new JButton("Bulk Import");

    public static final Path pathBotFile = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "botlist.txt");
    public static final Path pathScriptFile = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "scriptlist.txt");
    public static final Path pathProxyFile = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "proxylist.txt");
    public static final Path pathProfileFile = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "profilelist.txt");
    public static final Path pathWorldFile = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "worldlist.txt");


    public BotList() {
        initBotListComponents();
    }

    private void initBotListComponents() {
//        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.setViewportView(panelBotListActual);
//
//        panelBotListActual.setLayout(new BoxLayout(panelBotListActual, BoxLayout.Y_AXIS));

        JScrollPane botlistscrollPane = new JScrollPane();
        botlistscrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        botlistscrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        botlistscrollPane.setBounds(0, 0, 959, 193);
        tabBotList.add(botlistscrollPane);

        // Initialize your panelBotListActual panel by assigning it to the class-level field
//        panelBotListActual = new JPanel();
        botlistscrollPane.setViewportView(panelBotListActual);
        panelBotListActual.setLayout(new BoxLayout(panelBotListActual, BoxLayout.Y_AXIS));

        panelImportBot.setBounds(0, 200, managerWidth, 100);
        panelImportBot.setLayout(null);

        textFieldBotInput.setBounds(10, 32, 400, 23);
        textFieldBotInput.setColumns(10);

        buttonAddBot.setBounds(420, 32, 90, 23);
        buttonDeleteBot.setBounds(520, 32, 90, 23);
        buttonImportBot.setBounds(620, 32, 155, 23);

        buttonAddBot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the text from the textFieldBotInput JTextField
                String botName = textFieldBotInput.getText();

                // Check if the botName is not empty
                if (!botName.isEmpty()) {
                    try {
                        // Create the parent directories if they don't exist
                        Files.createDirectories(pathBotFile.getParent());

                        // Create the file if it doesn't exist
                        if (!Files.exists(pathBotFile)) {
                            Files.createFile(pathBotFile);
                        }

                        // Append the botName to the file
                        Files.write(pathBotFile, (botName + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                        // Clear the textFieldBotInput
                        textFieldBotInput.setText("");

                        // Repopulate the bot list
                        populateBotListActual();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        buttonDeleteBot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                for (Component component : panelBotListActual.getComponents()) {
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
                                    List<String> botLines = Files.readAllLines(pathBotFile);

                                    // Remove the botName from the list using a case-insensitive comparison
                                    botLines.removeIf(line -> line.trim().equalsIgnoreCase(botName));

                                    // Write the updated list back to the file
                                    Files.write(pathBotFile, botLines);

                                    // Remove the row from panelBotListActual
                                    panelBotListActual.remove(rowPanel);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }

                // Revalidate and repaint panelBotListActual after processing all checkboxes
                panelBotListActual.revalidate();
                panelBotListActual.repaint();
                populateBotList();
            }


        });

        buttonImportBot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                /// import bot button
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
                        Set<String> botlistLines = new HashSet<String>();
                        if (Files.exists(pathBotFile)) {
                            Files.lines(pathBotFile).forEach(botlistLines::add);
                        }

                        // Check each line from the selected file
                        for (String line : selectedFileLines) {
                            if (!botlistLines.contains(line)) {
                                // If the line is not in botlist.txt, add it
                                botlistLines.add(line);
                            }
                        }

                        // Write the updated botlist.txt
                        Files.write(pathBotFile, botlistLines);

                        // Optionally, display a message to inform the user
                        JOptionPane.showMessageDialog(null, "File added to botlist.txt");

                        populateBotListActual();
                        populateBotList();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error reading or writing files.");
                    }
                }
            }
        });

        tabBotList.add(scrollPane);
        tabBotList.add(panelImportBot);
        panelImportBot.add(textFieldBotInput);
        panelImportBot.add(buttonAddBot);
        panelImportBot.add(buttonImportBot);
        panelImportBot.add(buttonDeleteBot);
    }



}
