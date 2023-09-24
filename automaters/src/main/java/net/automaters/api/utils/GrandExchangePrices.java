package net.automaters.api.utils;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class GrandExchangePrices {
    public static void updatePrices() {
        try {
            // Define the URL to download
            String urlToDownload = "https://prices.runescape.wiki/api/v1/osrs/latest";

            // Define the directory to save the file
            String userHome = System.getProperty("user.home");
            String directoryPath = userHome + File.separator + ".openosrs" + File.separator + "data" + File.separator + "AutomateRS" + File.separator + "Grand Exchange Prices";
            File directory = new File(directoryPath);

            // Create the directory if it doesn't exist
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Define the file path
            String filePath = directoryPath + File.separator + "latest.json";

            // Create a URL object
            URL url = new URL(urlToDownload);

            // Open a connection to the URL
            try (InputStream in = url.openStream();
                 ReadableByteChannel rbc = Channels.newChannel(in);
                 FileOutputStream fos = new FileOutputStream(filePath)) {

                // Create a buffer to read from the URL and write to the file
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                // Read from the URL and write to the file
                while (rbc.read(buffer) != -1) {
                    buffer.flip();
                    fos.getChannel().write(buffer);
                    buffer.clear();
                }

                System.out.println("File downloaded successfully to: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Format the JSON file with proper indentation
            formatJsonFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void formatJsonFile(String filePath) {
        try {
            // Read the JSON file
            String json = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the JSON and format it with proper indentation
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String formattedJson = gson.toJson(gson.fromJson(json, Object.class));

            // Write the formatted JSON back to the file
            Files.write(Paths.get(filePath), formattedJson.getBytes(StandardCharsets.UTF_8));
            System.out.println("File formatted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}