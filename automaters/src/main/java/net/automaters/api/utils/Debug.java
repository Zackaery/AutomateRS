package net.automaters.api.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Debug {

    public static String displayMessage;
    private static String lastMessage = null;
    private static int consecutiveCount = 0;



    public static void debug(String message) {
        String debugExportFolder = System.getProperty("user.home") + File.separator + ".openosrs" + File.separator + "data" + File.separator + "AutomateRS" + File.separator + "Debug Export";
        File debugExport = new File(debugExportFolder);

        if (!debugExport.exists()) {
            debugExport.mkdirs();
        }

        String logFilePath = debugExportFolder + File.separator + "log.txt";

        try (FileWriter fw = new FileWriter(logFilePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            String logMessage = timestamp + ": " + message;

            if (message.equals(lastMessage)) {
                consecutiveCount++;
            } else {
                if (consecutiveCount > 0) {
                    out.println("[" + consecutiveCount + " times consecutively] " + lastMessage);
                    consecutiveCount = 0;
                }
                out.println(logMessage);
                displayMessage = message;
                lastMessage = message;
                consecutiveCount = 0;
                System.out.println("[AutomateRS] - " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
