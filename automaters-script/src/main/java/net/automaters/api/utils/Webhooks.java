package net.automaters.api.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static net.automaters.overlay.panel.auto_login.ProfilePanel.profileName;

public class Webhooks {

    private static final String BUG_REPORT_HOOK = "https://discord.com/api/webhooks/1164499790399275049/ibCPlfp2mYS8U06YFKkR2xRhQhkyLDvpXKVKZcw4RH0rRCPwW5EfibwGEd8uXT4hPyKd";
    private static final String PROGRESS_REPORT_HOOK = "https://discord.com/api/webhooks/1164503950523895940/HFOhVchV1fLtDkcbaALPJyXQxQ-yhIF6Hostw5mbS8WSmIwqzSPB6LyxVnVXEkaeJrzT";


    public static void sendWebhookMessage(String message) {
        try {
            URL url = new URL(BUG_REPORT_HOOK);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String bugReport =
                    ("## - "+profileName+
                            "\n"+message+"\n");

            // Send the message as plain text
            byte[] input = ("content=" + URLEncoder.encode(bugReport, "UTF-8")).getBytes("utf-8");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 204) {
                System.out.println("Message sent successfully to the Discord webhook.");
            } else {
                System.err.println("Failed to send message to the Discord webhook. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
