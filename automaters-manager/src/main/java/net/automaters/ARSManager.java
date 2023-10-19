package net.automaters;

import com.formdev.flatlaf.FlatLaf;
import net.automaters.gui.themes.AutomateLaf;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ARSManager extends JFrame {

    private JPanel panelRunBotList; // Declare the class-level field here
    private JPanel panelBotListActual;
    private JPanel panelProxyList;
    private JPanel panelProfileList;
    private JPanel panelPluginList;

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JTextField textFieldBotInput;
    private JTextField textFieldProxyInput;
    private JTextField textFieldProfileInput;
    private JTextField ScriptInputField;

	int managerWidth = 800;
	int managerHeight = 575;

    public ARSManager() {
		setResizable(false);
		setTitle("AutomateRS - Manager");

		JPanel mainFrame = new JPanel();
		getContentPane().add(mainFrame, BorderLayout.CENTER);
		mainFrame.setLayout(null);

		JPanel TopPanel = new JPanel();
		TopPanel.setBounds(10, 5, 964, 65);
		mainFrame.add(TopPanel);
		TopPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		// Set the preferred size of TopPanel to 150 pixels in height
		TopPanel.setPreferredSize(new Dimension(TopPanel.getPreferredSize().height, 150));

		JTabbedPane tabbedPanel = new JTabbedPane(JTabbedPane.TOP);
		System.out.println("main panel: " + mainFrame.getBounds());
		tabbedPanel.setBounds(10, 100, managerWidth - 20, managerHeight - 200);
		System.out.println("tabbedPanel panel: " + tabbedPanel.getBounds());
		mainFrame.add(tabbedPanel);

		//run tab
		JPanel tabRun = new JPanel();
		tabbedPanel.addTab("Start/Stop", null, tabRun, null);
		tabRun.setLayout(null);

		{
			JScrollPane scrollPaneBot = new JScrollPane();
			scrollPaneBot.setBounds(0, 0, 959, 214);
			scrollPaneBot.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			tabRun.add(scrollPaneBot);

			panelRunBotList = new JPanel(); // Use the class-level field
			scrollPaneBot.setViewportView(panelRunBotList);
			panelRunBotList.setLayout(new BoxLayout(panelRunBotList, BoxLayout.Y_AXIS));

			JPanel panelRunButton = new JPanel();
			panelRunButton.setBounds(0, 214, 959, 44);
			tabRun.add(panelRunButton);
			panelRunButton.setLayout(null);


			JButton buttonStart = new JButton("Start bot(s)");
			buttonStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						// Get the PIDs of the current running java processes
						Process pidprocess = Runtime.getRuntime().exec("tasklist /fi \"IMAGENAME eq java.exe\"");
						BufferedReader reader = new BufferedReader(new InputStreamReader(pidprocess.getInputStream()));
						String line;
						ArrayList<String> pids = new ArrayList<String>();
						while ((line = reader.readLine()) != null) {
							if (line.contains("java.exe")) {
								String[] parts = line.split("\\s+");
								pids.add(parts[1]);
								System.out.println("PID of Java process: " + parts[1]);
							}
						}

						// Loop through the components in the panelRunBotList panel
						for (Component component : panelRunBotList.getComponents()) {
							if (component instanceof JPanel) {
								JPanel botRowPanel = (JPanel) component;

								// Check if the rowPanel contains a checkbox
								JCheckBox checkbox = findCheckbox(botRowPanel);
								if (checkbox != null && checkbox.isSelected()) {

									// Find the JLabel named "botName" in the botRowPanel
									JLabel botNameLabel = findLabel(botRowPanel);
									String botName = botNameLabel.getText().trim();
									String botNameSub = botNameLabel.getText().trim();
									// Extract the part of botName before the "@" symbol
									int atIndex = botNameSub.indexOf('@');
									if (atIndex != -1) {
										botNameSub = botNameSub.substring(0, atIndex);
									}

									// Check if botName is empty or null
									if (botName.isEmpty()) {
										JOptionPane.showMessageDialog(null, "Bot name is empty!", "Error", JOptionPane.ERROR_MESSAGE);
										continue;
									}

									// Check if the bot is already running
									boolean contains = false;
									Path pidFilePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", botNameSub, "pidlist.txt");
									if (Files.exists(pidFilePath)) {
										String fileContent = readFileContents(pidFilePath);
										for (String pid : pids) {
											if (pid.equals(fileContent.trim())) {
												contains = true;
												JOptionPane.showMessageDialog(null, botName + " already running!");
												break;
											}
										}
									}

									// If not already running, start the program
									if (!contains) {
										// Get the selected values from the dropdowns
										JComboBox<String> scriptDropdown = findDropdown(botRowPanel, "scriptDropdown");
										JComboBox<String> profileDropdown = findDropdown(botRowPanel, "profileDropdown");
										JComboBox<String> proxyDropdown = findDropdown(botRowPanel, "proxyDropdown");
										JComboBox<String> worldDropdown = findDropdown(botRowPanel, "worldDropdown");

										String scriptValue = scriptDropdown.getSelectedItem().toString();
										String profileValue = profileDropdown.getSelectedItem().toString();
										String proxyValue = proxyDropdown.getSelectedItem().toString();
										String worldValue = worldDropdown.getSelectedItem().toString();

										// Check if the script is the default value
										if ("Script".equals(scriptValue)) {
											JOptionPane.showMessageDialog(null, "No script selected", "Error", JOptionPane.ERROR_MESSAGE);
										} else {
											// Specify the path to the JAR file you want to launch
											String jarFilePath = "C:\\Users\\corey\\IdeaProjects\\devious-client\\runelite-client\\build\\libs\\runelite-client-1.0.20-EXPERIMENTAL-shaded.jar";

											// Create the command as a string
											String command = "java -jar " + jarFilePath +
													" --minimal" +
													" --account " + botName +
													" --script \"" + scriptValue + "\"";

											// Add non-default values to the command
											if (!"World".equals(worldValue)) {
												try {
													int intValue = Integer.parseInt(worldValue);
													command += " --world " + intValue;
												} catch (NumberFormatException e1) {
													JOptionPane.showMessageDialog(null, "World selected is not a number!", "Error", JOptionPane.ERROR_MESSAGE);
													break;
												}
											}
											if (!"Profile".equals(profileValue)) {
												command += " --scriptargs " + profileValue;
											}
											if (!"Proxy".equals(proxyValue)) {
												command += " --proxy " + proxyValue;
											}

											// Start the program
											Process process = Runtime.getRuntime().exec(command);

											// Handle process output and errors if needed
											// ...


											// Wait for a short period to allow the process to start
											Thread.sleep(2000); // You can adjust the sleep duration as needed

											// Check if the process is still running
											if (process.isAlive()) {
												// The process has started

												// Create the directory if it doesn't exist
												Files.createDirectories(pidFilePath.getParent());

												// Write the PID to the pidlist.txt file
												try (BufferedWriter writer = Files.newBufferedWriter(pidFilePath)) {
													writer.write(String.valueOf(process.pid()));
												} catch (IOException ex) {
													ex.printStackTrace();
												}
											} else {
												// The process failed to start
											}
										}
									}
								}
							}
						}
					} catch (IOException | InterruptedException ex) {
						ex.printStackTrace();
					}
				}


			});
			buttonStart.setBounds(91, 11, 120, 23);
			panelRunButton.add(buttonStart);

			JButton buttonStop = new JButton("Stop Bot(s)");
			buttonStop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						// Get the PIDs of the current running java processes
						Process pidprocess = Runtime.getRuntime().exec("tasklist /fi \"IMAGENAME eq java.exe\"");
						BufferedReader reader = new BufferedReader(new InputStreamReader(pidprocess.getInputStream()));
						String line;
						ArrayList<String> pids = new ArrayList<String>();
						while ((line = reader.readLine()) != null) {
							if (line.contains("java.exe")) {
								String[] parts = line.split("\\s+");
								pids.add(parts[1]);
								System.out.println("PID of Java process: " + parts[1]);
							}
						}

						// Loop through the components in the panelRunBotList panel
						for (Component component : panelRunBotList.getComponents()) {
							if (component instanceof JPanel) {
								JPanel botRowPanel = (JPanel) component;

								// Check if the rowPanel contains a checkbox
								JCheckBox checkbox = findCheckbox(botRowPanel);
								if (checkbox != null && checkbox.isSelected()) {

									// Find the JLabel named "botName" in the botRowPanel
									JLabel botNameLabel = findLabel(botRowPanel);
									String botName = botNameLabel.getText().trim();
									String botNameSub = botNameLabel.getText().trim();
									// Extract the part of botName before the "@" symbol
									int atIndex = botNameSub.indexOf('@');
									if (atIndex != -1) {
										botNameSub = botNameSub.substring(0, atIndex);
									}

									// Check if botName is empty or null
									if (botName.isEmpty()) {
										JOptionPane.showMessageDialog(null, "Bot name is empty!", "Error", JOptionPane.ERROR_MESSAGE);
										continue;
									}
									Path pidFilePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", botNameSub, "pidlist.txt");
									if (Files.exists(pidFilePath)) {
										String fileContent = readFileContents(pidFilePath);
										for (String pid : pids) {
											if (pid.equals(fileContent.trim())) {
												// Stop the process with the matching PID
												try {
													ProcessBuilder processBuilder = new ProcessBuilder("taskkill", "/F", "/PID", pid);
													Process stopProcess = processBuilder.start();
													stopProcess.waitFor();
												} catch (IOException | InterruptedException ex) {
													ex.printStackTrace();
													JOptionPane.showMessageDialog(null, "Failed to stop " + botName + ".", "Error", JOptionPane.ERROR_MESSAGE);
												}
											}
										}
									}
								}
							}
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "All selected bots stopped successfully!");
				}
			});
			buttonStop.setBounds(221, 11, 120, 23);
			panelRunButton.add(buttonStop);

		}

		//bot list

		JPanel tabBotList = new JPanel();
		tabbedPanel.addTab("Bot List", null, tabBotList, null);
		tabBotList.setLayout(null);
		{
			JScrollPane botlistscrollPane = new JScrollPane();
			botlistscrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			botlistscrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			botlistscrollPane.setBounds(0, 0, 959, 193);
			tabBotList.add(botlistscrollPane);

			// Initialize your panelBotListActual panel by assigning it to the class-level field
			panelBotListActual = new JPanel();
			botlistscrollPane.setViewportView(panelBotListActual);
			panelBotListActual.setLayout(new BoxLayout(panelBotListActual, BoxLayout.Y_AXIS));

			JPanel panelImportBot = new JPanel();
			panelImportBot.setBounds(0, 193, 959, 66);
			tabBotList.add(panelImportBot);
			panelImportBot.setLayout(null);

			textFieldBotInput = new JTextField();
			textFieldBotInput.setBounds(10, 32, 543, 23);
			panelImportBot.add(textFieldBotInput);
			textFieldBotInput.setColumns(10);

			JButton buttonAddBot = new JButton("Add");
			buttonAddBot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Get the text from the textFieldBotInput JTextField
					String botName = textFieldBotInput.getText();

					// Check if the botName is not empty
					if (!botName.isEmpty()) {
						try {
							// Create a path to the botlist.txt file in user.home
							Path filePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "botlist.txt");

							// Create the parent directories if they don't exist
							Files.createDirectories(filePath.getParent());

							// Create the file if it doesn't exist
							if (!Files.exists(filePath)) {
								Files.createFile(filePath);
							}

							// Append the botName to the file
							Files.write(filePath, (botName + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

							// Clear the textFieldBotInput
							textFieldBotInput.setText("");
							populateBotListActual();
							populateBotList();// Repopulate the bot list
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}
			});
			buttonAddBot.setBounds(563, 32, 89, 23);
			panelImportBot.add(buttonAddBot);

			JButton buttonImportBot = new JButton("Bulk Import");
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

							// Define the path to botlist.txt
							Path botlistPath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "botlist.txt");

							// Read the lines from botlist.txt
							Set<String> botlistLines = new HashSet<String>();
							if (Files.exists(botlistPath)) {
								Files.lines(botlistPath).forEach(botlistLines::add);
							}

							// Check each line from the selected file
							for (String line : selectedFileLines) {
								if (!botlistLines.contains(line)) {
									// If the line is not in botlist.txt, add it
									botlistLines.add(line);
								}
							}

							// Write the updated botlist.txt
							Files.write(botlistPath, botlistLines);

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
			buttonImportBot.setBounds(761, 32, 188, 23);
			panelImportBot.add(buttonImportBot);

			JButton buttonDeleteBot = new JButton("Delete");
			buttonDeleteBot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Define the path to botlist.txt
					Path botlistPath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "botlist.txt");

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
										List<String> botLines = Files.readAllLines(botlistPath);

										// Remove the botName from the list using a case-insensitive comparison
										botLines.removeIf(line -> line.trim().equalsIgnoreCase(botName));

										// Write the updated list back to the file
										Files.write(botlistPath, botLines);

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
			buttonDeleteBot.setBounds(662, 32, 89, 23);
			panelImportBot.add(buttonDeleteBot);
		}

		//proxy list
		JPanel tabProxyList = new JPanel();
		tabbedPanel.addTab("Proxy List", null, tabProxyList, null);
		tabProxyList.setLayout(null);

		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setBounds(0, 0, 959, 189);
			tabProxyList.add(scrollPane);

			panelProxyList = new JPanel();
			scrollPane.setViewportView(panelProxyList);
			panelProxyList.setLayout(new BoxLayout(panelProxyList, BoxLayout.Y_AXIS));

			JPanel panelImportProxy = new JPanel();
			panelImportProxy.setBounds(0, 189, 959, 70);
			tabProxyList.add(panelImportProxy);
			panelImportProxy.setLayout(null);

			textFieldProxyInput = new JTextField();
			textFieldProxyInput.setBounds(10, 10, 543, 23);
			panelImportProxy.add(textFieldProxyInput);
			textFieldProxyInput.setColumns(10);

			JButton buttonImportProxy = new JButton("Bulk Import");
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

							// Define the path to botlist.txt
							Path proxylistPath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "proxylist.txt");

							// Read the lines from botlist.txt
							Set<String> proxylistLines = new HashSet<String>();
							if (Files.exists(proxylistPath)) {
								Files.lines(proxylistPath).forEach(proxylistLines::add);
							}

							// Check each line from the selected file
							for (String line : selectedFileLines) {
								if (!proxylistLines.contains(line)) {
									// If the line is not in botlist.txt, add it
									proxylistLines.add(line);
								}
							}

							// Write the updated botlist.txt
							Files.write(proxylistPath, proxylistLines);

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
			buttonImportProxy.setBounds(761, 10, 188, 23);
			panelImportProxy.add(buttonImportProxy);

			JButton buttonAddProxy = new JButton("Add");
			buttonAddProxy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Get the text from the textFieldProxyInput JTextField
					String proxy = textFieldProxyInput.getText();

					// Check if the proxy is not empty
					if (!proxy.isEmpty()) {
						try {
							// Create a path to the proxylist.txt file in user.home
							Path filePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "proxylist.txt");

							// Create the parent directories if they don't exist
							Files.createDirectories(filePath.getParent());

							// Create the file if it doesn't exist
							if (!Files.exists(filePath)) {
								Files.createFile(filePath);
							}

							// Append the proxy to the file
							Files.write(filePath, (proxy + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

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
			buttonAddProxy.setBounds(563, 10, 89, 23);
			panelImportProxy.add(buttonAddProxy);

			JButton buttonDeleteProxy = new JButton("Delete");
			buttonDeleteProxy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Define the path to proxylist.txt
					Path botlistPath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "proxylist.txt");

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
										List<String> botLines = Files.readAllLines(botlistPath);

										// Remove the botName from the list using a case-insensitive comparison
										botLines.removeIf(line -> line.trim().equalsIgnoreCase(botName));

										// Write the updated list back to the file
										Files.write(botlistPath, botLines);

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
			buttonDeleteProxy.setBounds(662, 10, 89, 23);
			panelImportProxy.add(buttonDeleteProxy);
		}

		//profile tab
		JPanel tabProfileList = new JPanel();
		tabbedPanel.addTab("Profile List", null, tabProfileList, null);
		tabProfileList.setLayout(null);

		{
			JScrollPane scrollPane_1 = new JScrollPane();
			scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane_1.setBounds(0, 0, 960, 186);
			tabProfileList.add(scrollPane_1);

			panelProfileList = new JPanel();
			scrollPane_1.setViewportView(panelProfileList);
			panelProfileList.setLayout(new BoxLayout(panelProfileList, BoxLayout.Y_AXIS));

			JPanel panelImportProfile = new JPanel();
			panelImportProfile.setBounds(0, 185, 960, 74);
			tabProfileList.add(panelImportProfile);
			panelImportProfile.setLayout(null);

			textFieldProfileInput = new JTextField();
			textFieldProfileInput.setBounds(10, 10, 548, 23);
			panelImportProfile.add(textFieldProfileInput);
			textFieldProfileInput.setColumns(10);

			JButton buttonImportProfile = new JButton("Bulk Import");
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

							// Define the path to botlist.txt
							Path profilelistPath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "profilelist.txt");

							// Read the lines from botlist.txt
							Set<String> profilelistLines = new HashSet<String>();
							if (Files.exists(profilelistPath)) {
								Files.lines(profilelistPath).forEach(profilelistLines::add);
							}

							// Check each line from the selected file
							for (String line : selectedFileLines) {
								if (!profilelistLines.contains(line)) {
									// If the line is not in botlist.txt, add it
									profilelistLines.add(line);
								}
							}

							// Write the updated botlist.txt
							Files.write(profilelistPath, profilelistLines);

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
			buttonImportProfile.setBounds(762, 10, 188, 23);
			panelImportProfile.add(buttonImportProfile);

			JButton buttonAddProfile = new JButton("Add");
			buttonAddProfile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Get the text from the textFieldProfileInput JTextField
					String proxy = textFieldProfileInput.getText();

					// Check if the profile is not empty
					if (!proxy.isEmpty()) {
						try {
							// Create a path to the proxylist.txt file in user.home
							Path filePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "profilelist.txt");

							// Create the parent directories if they don't exist
							Files.createDirectories(filePath.getParent());

							// Create the file if it doesn't exist
							if (!Files.exists(filePath)) {
								Files.createFile(filePath);
							}

							// Append the profile to the file
							Files.write(filePath, (proxy + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

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
			buttonAddProfile.setBounds(568, 10, 89, 23);
			panelImportProfile.add(buttonAddProfile);

			JButton buttonDeleteProfile = new JButton("Delete");
			buttonDeleteProfile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Define the path to profilelist.txt
					Path botlistPath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "profilelist.txt");

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
										List<String> botLines = Files.readAllLines(botlistPath);

										// Remove the botName from the list using a case-insensitive comparison
										botLines.removeIf(line -> line.trim().equalsIgnoreCase(botName));

										// Write the updated list back to the file
										Files.write(botlistPath, botLines);

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
			buttonDeleteProfile.setBounds(667, 10, 89, 23);
			panelImportProfile.add(buttonDeleteProfile);
		}

		//script tab
		JPanel tabPluginList = new JPanel();
		tabbedPanel.addTab("Script List", null, tabPluginList, null);
		tabPluginList.setLayout(null);

		{
			JScrollPane scrollPane_2 = new JScrollPane();
			scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane_2.setBounds(0, 0, 959, 183);
			tabPluginList.add(scrollPane_2);

			panelPluginList = new JPanel();
			scrollPane_2.setViewportView(panelPluginList);
			panelPluginList.setLayout(new BoxLayout(panelPluginList, BoxLayout.Y_AXIS));

			JPanel scriptbuttonpanel = new JPanel();
			scriptbuttonpanel.setBounds(0, 183, 959, 76);
			tabPluginList.add(scriptbuttonpanel);
			scriptbuttonpanel.setLayout(null);

			JButton scriptdeletebutton = new JButton("Delete");
			scriptdeletebutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Define the path to pluginlist.txt
					Path botlistPath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "pluginlist.txt");

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
									String botName = nameLabel.getText().trim(); // Trim whitespace

									try {
										// Read the file
										List<String> botLines = Files.readAllLines(botlistPath);

										// Remove the botName from the list using a case-insensitive comparison
										botLines.removeIf(line -> line.trim().equalsIgnoreCase(botName));

										// Write the updated list back to the file
										Files.write(botlistPath, botLines);

										// Remove the row from panelBotListActual
										panelPluginList.remove(rowPanel);
									} catch (IOException ex) {
										ex.printStackTrace();
									}
								}
							}
						}
					}

					// Revalidate and repaint panelBotListActual after processing all checkboxes
					panelPluginList.revalidate();
					panelPluginList.repaint();
					populateBotList();
				}
			});
			scriptdeletebutton.setBounds(662, 11, 89, 23);
			scriptbuttonpanel.add(scriptdeletebutton);

			JButton scriptaddbutton = new JButton("Add");
			scriptaddbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Get the text from the ScriptInputField JTextField
					String proxy = ScriptInputField.getText();

					// Check if the script is not empty
					if (!proxy.isEmpty()) {
						try {
							// Create a path to the pluginlist.txt file in user.home
							Path filePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "pluginlist.txt");

							// Create the parent directories if they don't exist
							Files.createDirectories(filePath.getParent());

							// Create the file if it doesn't exist
							if (!Files.exists(filePath)) {
								Files.createFile(filePath);
							}

							// Append the script to the file
							Files.write(filePath, (proxy + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

							// Clear the textFieldProfileInput
							ScriptInputField.setText("");
							populatePluginList(); // Repopulate the proxy list
							populateBotList();// Repopulate the bot list
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}
			});
			scriptaddbutton.setBounds(563, 11, 89, 23);
			scriptbuttonpanel.add(scriptaddbutton);

			JButton scriptbulkimportbutton = new JButton("Bulk Import");
			scriptbulkimportbutton.addActionListener(new ActionListener() {
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

							// Define the path to pluginlist.txt
							Path pluginlistPath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "pluginlist.txt");

							// Read the lines from pluginlist.txt
							Set<String> pluginlistLines = new HashSet<String>();
							if (Files.exists(pluginlistPath)) {
								Files.lines(pluginlistPath).forEach(pluginlistLines::add);
							}

							// Check each line from the selected file
							for (String line : selectedFileLines) {
								if (!pluginlistLines.contains(line)) {
									// If the line is not in botlist.txt, add it
									pluginlistLines.add(line);
								}
							}

							// Write the updated botlist.txt
							Files.write(pluginlistPath, pluginlistLines);

							// Optionally, display a message to inform the user
							JOptionPane.showMessageDialog(null, "File added to pluginlist.txt");

							populatePluginList();
							populateBotList();// Repopulate the bot list

						} catch (IOException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Error reading or writing files.");
						}
					}

				}
			});
			scriptbulkimportbutton.setBounds(761, 11, 188, 23);
			scriptbuttonpanel.add(scriptbulkimportbutton);

			ScriptInputField = new JTextField();
			ScriptInputField.setBounds(10, 11, 543, 23);
			scriptbuttonpanel.add(ScriptInputField);
			ScriptInputField.setColumns(10);
		}


		//schedule panel
		JPanel schedulepanel = new JPanel();
		tabbedPanel.addTab("Scheduling", null, schedulepanel, null);
		schedulepanel.setLayout(null);

		{
			JTabbedPane tabbedPanel_1 = new JTabbedPane(JTabbedPane.TOP);
			tabbedPanel_1.setBounds(0, 0, 959, 259);
			schedulepanel.add(tabbedPanel_1);

			JPanel schedulestartstoppanel = new JPanel();
			tabbedPanel_1.addTab("Start-Stop-Delete", null, schedulestartstoppanel, null);

			JPanel schedulecreatepanel = new JPanel();
			tabbedPanel_1.addTab("Create Schedule", null, schedulecreatepanel, null);

			// Initialize your GUI components here
			initComponents();
		}
    }
    
    // check for files
    private void checkAndCreateFiles() {
        String baseDirectory = System.getProperty("user.home") + File.separator + ".openosrs" + File.separator + "data" + File.separator + "AutomateRS" + File.separator + "Manager";

        String[] fileNames = { "botlist.txt", "pluginlist.txt", "proxylist.txt", "profilelist.txt", "worldlist.txt" };

        for (String fileName : fileNames) {
            Path filePath = Paths.get(baseDirectory, fileName);
            try {
                createFileIfNotExists(filePath);

                // If it's the "worldlist.txt" file, write the contents
                if (fileName.equals("worldlist.txt")) {
                    if (!Files.readString(filePath).trim().equals("")) {
                        continue; // File already has contents, don't overwrite
                    }
                    List<String> worldList = new ArrayList<>();
                    worldList.add("Free Worlds");
                    worldList.addAll(Arrays.asList(
                            "301", "308", "316", "326", "335", "371", "379", "380", "382", "383", "384", "394", "397", "398",
                            "399", "417", "418", "430", "431", "433", "434", "435", "436", "437", "451", "452", "453",
                            "454", "455", "456", "460", "462", "463", "464", "465", "469", "470", "471", "475", "476",
                            "483", "497", "498", "499", "562", "563", "570"
                    ));
                    worldList.add("Members Worlds");
                    worldList.addAll(Arrays.asList(
                            "302", "303", "304", "305", "306", "307", "309", "310", "311", "312", "313", "314", "315",
                            "317", "320", "321", "323", "324", "325", "327", "328", "329", "330", "331", "332", "334",
                            "336", "337", "338", "339", "340", "341", "342", "343", "344", "346", "347", "348", "350",
                            "351", "352", "354", "355", "356", "357", "358", "359", "360", "362", "367", "368", "370",
                            "374", "375", "376", "377", "386", "387", "388", "390", "395", "441", "443", "444", "445",
                            "459", "463", "464", "465", "466", "477", "478", "480", "481", "482", "484", "485", "486",
                            "487", "488", "489", "490", "491", "492", "493", "494", "495", "496", "505", "506", "508",
                            "509", "510", "511", "512", "513", "514", "515", "516", "517", "518", "519", "520", "521",
                            "522", "523", "524", "525", "531", "532", "534", "535", "580"
                    ));

                    Files.write(filePath, worldList, StandardCharsets.UTF_8);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createFileIfNotExists(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent()); // Create parent directories if needed
            Files.createFile(filePath); // Create the file
        }
    }

    private void initComponents() {
    	// initComps!
        checkAndCreateFiles();
    	populateBotList();
    	populateBotListActual();
    	populateProxyList();
    	populateProfileList();
    	populatePluginList();
    	// initComps!
    }

    public static void main(String[] args) {
		FlatLaf.registerCustomDefaultsSource("net.automaters.themes.AutomateLaf");
		AutomateLaf.setup();
//         Create an instance of ARSManager (your JFrame)
        ARSManager frame = new ARSManager();
//        frame.setTitle("AutomateRS - Manager ");
//        frame.setSize(800, 575);
//		frame.setLocationRelativeTo(null);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
    }

    // read script file
    private String[] loadPluginList(Path scriptfilePath) {
        try {
            if (Files.exists(scriptfilePath)) {
                List<String> scriptLines = Files.readAllLines(scriptfilePath);
                return scriptLines.toArray(new String[0]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Return an empty array or handle the error as needed
        return new String[0];
    }

    // read proxy file
    private String[] loadProxyList(Path proxyfilePath) {
        try {
            if (Files.exists(proxyfilePath)) {
                List<String> proxyLines = Files.readAllLines(proxyfilePath);
                return proxyLines.toArray(new String[0]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Return an empty array or handle the error as needed
        return new String[0];
    }
    
    // read world file
    private String[] loadWorldList(Path worldfilePath) {
        try {
            if (Files.exists(worldfilePath)) {
                List<String> worldLines = Files.readAllLines(worldfilePath);
                return worldLines.toArray(new String[0]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Return an empty array or handle the error as needed
        return new String[0];
    }
    
    // read profile file
    private String[] loadProfileList(Path profilefilePath) {
        try {
            if (Files.exists(profilefilePath)) {
                List<String> profileLines = Files.readAllLines(profilefilePath);
                return profileLines.toArray(new String[0]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Return an empty array or handle the error as needed
        return new String[0];
    }

    public String readFileContents(Path filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(filePath);
        return new String(bytes);
    }
    
    
    // Method to populate the bot list
    private void populateBotList() {

		System.out.println("populateBotList");
   	 if (panelRunBotList != null) {
   		panelRunBotList.removeAll();
   		}
        // Show a JOptionPane with the message "We got here"
   	 	// Inside your populateBotList method or any other suitable location
   	 	JOptionPane.showMessageDialog(this, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);
        try {
            // Read the file we saved with the previous code
            Path botfilePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "botlist.txt");
            Path scriptfilePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "pluginlist.txt");
            Path proxyfilePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "proxylist.txt");
            Path profilefilePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "profilelist.txt");
            Path worldfilePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "worldlist.txt");

            // Check if the file exists
            if (Files.exists(botfilePath)) {
                List<String> botLines = Files.readAllLines(botfilePath);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL; // Allow components to expand horizontally

                for (String botName : botLines) {
                    if (!botName.trim().isEmpty()) {
                        JPanel botRowPanel = new JPanel(new GridBagLayout());

                        // Checkbox for the bot
                        JCheckBox checkbox = new JCheckBox();
                        gbc.gridx = 0;
                        gbc.weightx = 0; // Don't let the checkbox expand
                        botRowPanel.add(checkbox, gbc);

                        // Text field for "offline"
                        JTextField statusTextField = new JTextField("Offline");
                        statusTextField.setEditable(false);
                        gbc.gridx = 1;
                        gbc.weightx = 0; // Don't let the field expand
                        botRowPanel.add(statusTextField, gbc);

                        // Label for the bot name (read from the file)
                        JLabel nameLabel = new JLabel("   " + botName);
                        gbc.gridx = 2;
                        gbc.weightx = 1; // Expand this field
                        nameLabel.setName("botName");
                        botRowPanel.add(nameLabel, gbc);

                     // Dropdown for "script"
                        String[] scriptItems = loadPluginList(scriptfilePath);

                        // Always add "Script" as the first item in the list
                        String[] scriptitemsWithDefault = new String[scriptItems.length + 1];
                        scriptitemsWithDefault[0] = "Script";
                        System.arraycopy(scriptItems, 0, scriptitemsWithDefault, 1, scriptItems.length);

                        JComboBox<String> scriptDropdown = new JComboBox<>(scriptitemsWithDefault);
                        gbc.gridx = 3;
                        gbc.weightx = 0; // Don't let the dropdown expand
                        scriptDropdown.setName("scriptDropdown");
                        botRowPanel.add(scriptDropdown, gbc);
                        
                     // Dropdown for "world"
                        String[] worldItems = loadWorldList(worldfilePath);

                        // Always add "Script" as the first item in the list
                        String[] worlditemsWithDefault = new String[worldItems.length + 1];
                        worlditemsWithDefault[0] = "World";
                        System.arraycopy(worldItems, 0, worlditemsWithDefault, 1, worldItems.length);

                        JComboBox<String> worldDropdown = new JComboBox<>(worlditemsWithDefault);
                        gbc.gridx = 4;
                        gbc.weightx = 0; // Don't let the world expand
                        worldDropdown.setName("worldDropdown");
                        botRowPanel.add(worldDropdown, gbc);

                     // Dropdown for "proxy"
                        String[] proxyItems = loadProxyList(proxyfilePath);

                        // Always add "Proxy" as the first item in the list
                        String[] proxyitemsWithDefault = new String[proxyItems.length + 1];
                        proxyitemsWithDefault[0] = "Proxy";
                        System.arraycopy(proxyItems, 0, proxyitemsWithDefault, 1, proxyItems.length);

                        JComboBox<String> proxyDropdown = new JComboBox<>(proxyitemsWithDefault);
                        gbc.gridx = 5;
                        gbc.weightx = 0; // Don't let the dropdown expand
                        proxyDropdown.setName("proxyDropdown");
                        botRowPanel.add(proxyDropdown, gbc);

                     // Dropdown for "profile"
                        String[] profileItems = loadProfileList(profilefilePath);

                        // Always add "Profile" as the first item in the list
                        String[] profileitemsWithDefault = new String[profileItems.length + 1];
                        profileitemsWithDefault[0] = "Profile";
                        System.arraycopy(profileItems, 0, profileitemsWithDefault, 1, profileItems.length);

                        JComboBox<String> profileDropdown = new JComboBox<>(profileitemsWithDefault);
                        gbc.gridx = 6;
                        gbc.weightx = 0; // Don't let the dropdown expand
                        profileDropdown.setName("profileDropdown");
                        botRowPanel.add(profileDropdown, gbc);


						System.out.println("panelRunBotList.add");
                        // Add the row to the scroll panel
                        panelRunBotList.add(botRowPanel, gbc);

						System.out.println("panelRunBotList BOUNDS = "+panelRunBotList.getBounds());
                    }
                }
            }
            
            // Refresh the layout of botlistpanel if needed
            panelRunBotList.revalidate();
            panelRunBotList.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // below is end of start/stop loading bots
    
    // below is populating bot list panel
    
    private void populateBotListActual() {
		System.out.println("populateBotListActual");
      	 if (panelBotListActual != null) {
      		panelBotListActual.removeAll();
      		}
           // Show a JOptionPane with the message "We made it here"
      	 	// Inside your populateBotList method or any other suitable location
      	 	JOptionPane.showMessageDialog(this, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);
      	    try {
      	        // Read the file we saved with the previous code
      	        Path botfilePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "botlist.txt");

      	        // Check if the file exists
      	        if (Files.exists(botfilePath)) {
      	            List<String> botLines = Files.readAllLines(botfilePath);

      	            for (String botName : botLines) {
      	                if (!botName.trim().isEmpty()) {
      	                    JPanel botRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned row panel

      	                    // Checkbox for the bot
      	                    JCheckBox checkbox = new JCheckBox();

      	                    // Text field for "offline"
      	                    JTextField statusTextField = new JTextField("Offline");
      	                    statusTextField.setEditable(false);

      	                    // Label for the bot name (read from the file)
      	                    JLabel nameLabel = new JLabel("   " + botName);

      	                    // Add components to the row panel
      	                    botRowPanel.add(checkbox);
      	                    botRowPanel.add(statusTextField);
      	                    botRowPanel.add(nameLabel);

							System.out.println("panelBotListActual.add");
      	                    // Add the row to the scroll panel
      	                    panelBotListActual.add(botRowPanel);
							System.out.println("panelBotListActual BOUNDS = "+panelBotListActual.getBounds());
      	                }
      	            }
      	        }

      	        // Refresh the layout of panelBotListActual panel if needed
      	        panelBotListActual.revalidate();
      	        panelBotListActual.repaint();
      	    } catch (IOException ex) {
      	        ex.printStackTrace();
      	    }
      	}
    // below is populating proxy list
    
    private void populateProxyList() {
        if (panelProxyList != null) {
            panelProxyList.removeAll();
        }

        // Show a JOptionPane with the message "We got here"
        JOptionPane.showMessageDialog(this, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);

        try {
            // Read the file we saved with the previous code
            Path botfilePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "proxylist.txt");

            // Check if the file exists
            if (Files.exists(botfilePath)) {
                List<String> botLines = Files.readAllLines(botfilePath);

                for (String botName : botLines) {
                    if (!botName.trim().isEmpty()) {
                        JPanel botRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned row panel

                        // Checkbox for the proxy
                        JCheckBox checkbox = new JCheckBox();

                        // Label for the bot name (read from the file)
                        JLabel nameLabel = new JLabel(botName);

                        // Add components to the row panel
                        botRowPanel.add(checkbox);
                        botRowPanel.add(nameLabel);

                        // Add the row to the scroll panel
                        panelProxyList.add(botRowPanel);
                    }
                }
            }

            // Refresh the layout of panelProxyList panel if needed
            panelProxyList.revalidate();
            panelProxyList.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // below is populate profile list
    private void populateProfileList() {
        if (panelProfileList != null) {
        	panelProfileList.removeAll();
        }

        // Show a JOptionPane with the message "We got here"
        JOptionPane.showMessageDialog(this, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);

        try {
            // Read the file we saved with the previous code
            Path botfilePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "profilelist.txt");

            // Check if the file exists
            if (Files.exists(botfilePath)) {
                List<String> botLines = Files.readAllLines(botfilePath);

                for (String botName : botLines) {
                    if (!botName.trim().isEmpty()) {
                        JPanel botRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned row panel

                        // Checkbox for the proxy
                        JCheckBox checkbox = new JCheckBox();

                        // Label for the bot name (read from the file)
                        JLabel nameLabel = new JLabel(botName);

                        // Add components to the row panel
                        botRowPanel.add(checkbox);
                        botRowPanel.add(nameLabel);

                        // Add the row to the scroll panel
                        panelProfileList.add(botRowPanel);
                    }
                }
            }

            // Refresh the layout of panelProxyList panel if needed
            panelProfileList.revalidate();
            panelProfileList.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // below is populate script panel
    // below is populate profile list
    private void populatePluginList() {
        if (panelPluginList != null) {
        	panelPluginList.removeAll();
        }

        // Show a JOptionPane with the message "We got here"
        JOptionPane.showMessageDialog(this, "We got here!", "Message", JOptionPane.INFORMATION_MESSAGE);

        try {
            // Read the file we saved with the previous code
            Path botfilePath = Paths.get(System.getProperty("user.home"), ".openosrs", "data", "AutomateRS", "Manager", "pluginlist.txt");

            // Check if the file exists
            if (Files.exists(botfilePath)) {
                List<String> botLines = Files.readAllLines(botfilePath);

                for (String botName : botLines) {
                    if (!botName.trim().isEmpty()) {
                        JPanel botRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned row panel

                        // Checkbox for the proxy
                        JCheckBox checkbox = new JCheckBox();

                        // Label for the bot name (read from the file)
                        JLabel nameLabel = new JLabel(botName);

                        // Add components to the row panel
                        botRowPanel.add(checkbox);
                        botRowPanel.add(nameLabel);

                        // Add the row to the scroll panel
                        panelPluginList.add(botRowPanel);
                    }
                }
            }

            // Refresh the layout of panelProxyList panel if needed
            panelPluginList.revalidate();
            panelPluginList.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    // finding dropdown
    private JComboBox<String> findDropdown(JPanel panel, String name) {
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
    private JCheckBox findCheckbox(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JCheckBox) {
                return (JCheckBox) component;
            }
        }
        return null; // Return null if no checkbox is found
    }
    // finding botname
    private JLabel findLabel(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JLabel) {
                return (JLabel) component;
            }
        }
        return null; // Return null if no JLabel is found
    }
}
