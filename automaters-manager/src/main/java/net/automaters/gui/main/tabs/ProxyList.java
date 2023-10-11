package net.automaters.gui.main.tabs;

import net.automaters.api.panels.InfoPanel;
import net.automaters.api.ui.DynamicGridLayout;

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

public class ProxyList {



    public static JPanel panelProxyInfo = new JPanel();
    private final InfoPanel infoPanel = new InfoPanel();

    public static JTextField textFieldProxyInput = new JTextField();

    public static JButton buttonAddProxy = new JButton("Add");
    public static JButton buttonDeleteProxy = new JButton("Delete");
    public static JButton buttonImportProxy = new JButton("Bulk Import");

    public ProxyList() {
        initProxyList();
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
        textField.setForeground(new Color(165, 165, 165));
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
    
    private void initProxyList() {

        JScrollPane scrollPane = new JScrollPane();
        initScrollPane(scrollPane, panelProxyList);

        createInfoPanel(panelProxyInfo, "Adding Proxys", "Format:<br><i>IP:PORT:USERNAME:PASSWORD</i></html>");
        createTextField(textFieldProxyInput, "192.168.1.1:1234:username:password");

        panelProxyList.setLayout(new BoxLayout(panelProxyList, BoxLayout.Y_AXIS));


        panelProxyComponents.setLayout(null);
        panelProxyComponents.setBounds(0, 300, managerWidth, 45);

        buttonAddProxy.setBounds(320, 10, 90, 25);
        buttonDeleteProxy.setBounds(420, 10, 90, 25);
        buttonImportProxy.setBounds(520, 10, 155, 25);

        buttonAddProxy.addActionListener(e ->  {
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
                   populateAccountList();// Repopulate the account list
               } catch (IOException ex) {
                   ex.printStackTrace();
               }
           }

        });
        buttonDeleteProxy.addActionListener(e ->  {

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
                           String accountName = nameLabel.getText().trim(); // Trim whitespace

                           try {
                               // Read the file
                               List<String> accountLines = Files.readAllLines(pathProxyFile);

                               // Remove the accountName from the list using a case-insensitive comparison
                               accountLines.removeIf(line -> line.trim().equalsIgnoreCase(accountName));

                               // Write the updated list back to the file
                               Files.write(pathProxyFile, accountLines);

                               // Remove the row from panelAccountListActual
                               panelProxyList.remove(rowPanel);
                           } catch (IOException ex) {
                               ex.printStackTrace();
                           }
                       }
                   }
               }
           }

           // Revalidate and repaint panelAccountListActual after processing all checkboxes
           panelProxyList.revalidate();
           panelProxyList.repaint();
           populateAccountList();

        });
        buttonImportProxy.addActionListener(e -> {
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


                    // Read the lines from proxylist.txt
                    Set<String> proxylistLines = new HashSet<String>();
                    if (Files.exists(pathProxyFile)) {
                        Files.lines(pathProxyFile).forEach(proxylistLines::add);
                    }

                    // Check each line from the selected file
                    for (String line : selectedFileLines) {
                        if (!proxylistLines.contains(line)) {
                            // If the line is not in proxylist.txt, add it
                            proxylistLines.add(line);
                        }
                    }

                    // Write the updated proxylist.txt
                    Files.write(pathProxyFile, proxylistLines);

                    // Optionally, display a message to inform the user
                    JOptionPane.showMessageDialog(null, "File added to proxylist.txt");

                    populateProxyList();
                    populateAccountList();// Repopulate the account list

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error reading or writing files.");
                }
            }
        });

        panelProxyComponents.add(textFieldProxyInput);
        panelProxyComponents.add(buttonAddProxy);
        panelProxyComponents.add(buttonDeleteProxy);
        panelProxyComponents.add(buttonImportProxy);

        tabProxyList.add(scrollPane);
        tabProxyList.add(panelProxyComponents);
        tabProxyList.add(panelProxyInfo);

    }
}
