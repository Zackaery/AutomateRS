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

public class AccountList {

    public static JPanel panelAccountListInfo = new JPanel();
    private final InfoPanel infoPanel = new InfoPanel();

    public static JTextField textFieldAccountInput = new JTextField();

    public static JButton buttonAddAccount = new JButton("Add");
    public static JButton buttonDeleteAccount = new JButton("Delete");
    public static JButton buttonImportAccount = new JButton("Bulk Import");

    public AccountList() {
        initAccountListComponents();
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

    private void initAccountListComponents() {

        JScrollPane scrollPane = new JScrollPane();
        initScrollPane(scrollPane, panelAccountListActual);

        createInfoPanel(panelAccountListInfo, "Adding Accounts", "Format:<br><i>USERNAME:PASSWORD:PIN</i></html>");
        createTextField(textFieldAccountInput, "username@mail.com:password:1234");

        panelAccountListActual.setLayout(new BoxLayout(panelAccountListActual, BoxLayout.Y_AXIS));
        panelAccountListComponents.setLayout(null);
        textFieldAccountInput.setColumns(10);

        panelAccountListComponents.setBounds(0, 300, managerWidth-100, 45);
        buttonAddAccount.setBounds(320, 10, 90, 25);
        buttonDeleteAccount.setBounds(420, 10, 90, 25);
        buttonImportAccount.setBounds(520, 10, 155, 25);

        buttonAddAccount.addActionListener(e ->  {
           // Get the text from the textFieldAccountInput JTextField
           String accountName = textFieldAccountInput.getText();

           // Check if the accountName is not empty
           if (!accountName.isEmpty()) {
               try {
                   // Create the parent directories if they don't exist
                   Files.createDirectories(pathAccountFile.getParent());

                   // Create the file if it doesn't exist
                   if (!Files.exists(pathAccountFile)) {
                       Files.createFile(pathAccountFile);
                   }

                   // Append the accountName to the file
                   Files.write(pathAccountFile, (accountName + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                   // Clear the textFieldAccountInput
                   textFieldAccountInput.setText("");

                   // Repopulate the account list
                   populateAccountListActual();

               } catch (IOException ex) {
                   ex.printStackTrace();
               }
           }

        });
        buttonDeleteAccount.addActionListener(e ->  {

           for (Component component : panelAccountListActual.getComponents()) {
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
                               List<String> accountLines = Files.readAllLines(pathAccountFile);

                               // Remove the accountName from the list using a case-insensitive comparison
                               accountLines.removeIf(line -> line.trim().equalsIgnoreCase(accountName));

                               // Write the updated list back to the file
                               Files.write(pathAccountFile, accountLines);

                               // Remove the row from panelAccountListActual
                               panelAccountListActual.remove(rowPanel);
                           } catch (IOException ex) {
                               ex.printStackTrace();
                           }
                       }
                   }
               }
           }

           // Revalidate and repaint panelAccountListActual after processing all checkboxes
           panelAccountListActual.revalidate();
           panelAccountListActual.repaint();
           populateAccountList();



        });
        buttonImportAccount.addActionListener(e ->  {

           /// import account button
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

                   // Read the lines from accountlist.txt
                   Set<String> accountlistLines = new HashSet<String>();
                   if (Files.exists(pathAccountFile)) {
                       Files.lines(pathAccountFile).forEach(accountlistLines::add);
                   }

                   // Check each line from the selected file
                   for (String line : selectedFileLines) {
                       if (!accountlistLines.contains(line)) {
                           // If the line is not in accountlist.txt, add it
                           accountlistLines.add(line);
                       }
                   }

                   // Write the updated accountlist.txt
                   Files.write(pathAccountFile, accountlistLines);

                   // Optionally, display a message to inform the user
                   JOptionPane.showMessageDialog(null, "File added to accountlist.txt");

                   populateAccountListActual();
                   populateAccountList();

               } catch (IOException ex) {
                   ex.printStackTrace();
                   JOptionPane.showMessageDialog(null, "Error reading or writing files.");
               }
           }

        });

        panelAccountListComponents.add(textFieldAccountInput);
        panelAccountListComponents.add(buttonAddAccount);
        panelAccountListComponents.add(buttonImportAccount);
        panelAccountListComponents.add(buttonDeleteAccount);

        tabAccountList.add(scrollPane);
        tabAccountList.add(panelAccountListComponents);
        tabAccountList.add(panelAccountListInfo);
    }



}
