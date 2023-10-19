package net.automaters.tasks.tests;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TestGuiCode {

    public static void main(String[] args) {
            // Replace with your Discord webhook URL
            String webhookUrl = "https://discord.com/api/webhooks/1163711564549996544/248IXgWKJwQuF3-xlZo8bUwudDGIP1yhj6ne6et40XaILt7fghsCOnK4wq_bxbaUmZCx";

        // Replace with your message
            String message = "";

            try {
                URL url = new URL(webhookUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String jsonMessage = "{\"content\":\"" + message + "\"}";
                byte[] postData = jsonMessage.getBytes(StandardCharsets.UTF_8);

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(postData);
                }

                int responseCode = connection.getResponseCode();
                System.out.println("Webhook Response Code: " + responseCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}