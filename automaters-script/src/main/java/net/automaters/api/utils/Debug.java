package net.automaters.api.utils;

import lombok.SneakyThrows;

import java.io.*;

import static net.automaters.overlay.panel.auto_login.ProfilePanel.profileName;
import static net.automaters.util.formats.DateFormat.*;
import static net.automaters.util.formats.TimeFormat.formatTime;

public class Debug {
    public static String displayMessage;
    private static String lastMessage = null;
    private static int consecutiveCount;
    @SneakyThrows
    public static void debug(String message) {
        String fileName = profileName + " - " + formatDateToday() + ".txt";
        String debugExportFolder = System.getProperty("user.home") + File.separator + ".openosrs" + File.separator + "data" + File.separator + "AutomateRS" + File.separator + "Debug Export";
        String logFilePath = debugExportFolder + File.separator + fileName;

        File debugExport = new File(debugExportFolder);
        if (!debugExport.exists()) {
            debugExport.mkdirs();
        }
        try (FileWriter fw = new FileWriter(logFilePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            String logMessage = formatTime() + ": " + message;

            if (message.equals(lastMessage)) {
                consecutiveCount++;
            } else {
                if (consecutiveCount > 0) {
                    out.println("[" + consecutiveCount + " times consecutively] " + lastMessage);
                    consecutiveCount = 0;
                }
                if (!message.contains("--- Initiating loop sequence ---")) {
                    displayMessage = message;
                    out.println(logMessage);
                }
                lastMessage = message;
                consecutiveCount = 0;
                System.out.println("[AutomateRS] - " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
