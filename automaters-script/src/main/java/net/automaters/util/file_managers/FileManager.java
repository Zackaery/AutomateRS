package net.automaters.util.file_managers;


import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static net.automaters.api.utils.Debug.debug;

public class FileManager {
    public static final String PATH_OPENOSRS = System.getProperty("user.home") + File.separator + ".openosrs" + File.separator;
    public static final String PATH_PLUGINS_FOLDER = PATH_OPENOSRS + "plugins" + File.separator;
    public static final String PATH_AUTOMATE_RS = PATH_OPENOSRS + "data" + File.separator + "AutomateRS" + File.separator;
    public static final String PATH_RESOURCES = "resources" + File.separator + "net.automaters" + File.separator;
    public static final String PATH_AUTOMATE_RS_RESOURCES = PATH_AUTOMATE_RS + PATH_RESOURCES;
    public static final String FILE_GE_PRICES = PATH_AUTOMATE_RS + "Grand Exchange Prices" + File.separator + "prices.json";
    public static final String FILE_AUTOMATERS = PATH_PLUGINS_FOLDER + "automaters-early-access.jar";
    public static final String URL_GITHUB = "https://github.com/Zackaery/Automate-RS";
    public static final String URL_GITHUB_DOWNLOAD = "https://github.com/Zackaery/Automate-RS/raw/main/automaters-early-access.jar";
    public static final String URL_GITHUB_CHECK = "https://api.github.com/repos/Zackaery/Automate-RS/commits?path=automaters-early-access.jar";
    public static final String URL_DISCORD_DOWNLOAD = "https://discord.com/channels/1150244727904546816/1152959895784660992";

    public static boolean getLastModified(File file, int difference) {
        long lastModifiedTime = file.lastModified();
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastModifiedTime;
        long timeDifferenceMinutes = timeDifference / (60 * 1000);

        return timeDifferenceMinutes > difference;
    }


    public static boolean shouldUpdate() throws IOException, ParseException
    {
        File automaters = new File(FILE_AUTOMATERS);
        if (!automaters.exists())
        {
            return true;
        }

        Date automatersLastUpdated = new Date(automaters.lastModified());
        // Send an HTTP GET request to the URL and open a connection
        HttpURLConnection connection = (HttpURLConnection) new URL(URL_GITHUB_CHECK).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        if (connection.getResponseCode() == 403)
        {

            debug("You're being instance limited by Github.");
            Object[] options = {"OK"};
            JOptionPane.showOptionDialog(null,
                    "Your IP is limited from auto updating for 1 hour...",
                    "Rate limit exceeded...", JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options,
                    options[0]);
        }

        // Read the JSON response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            response.append(line);
        }

        // Close the connection
        connection.disconnect();


        // Parse the JSON response manually
        String jsonResponse = response.toString();
        int dateIndex = jsonResponse.indexOf("\"date\":");
        if (dateIndex != -1)
        {
            // Extract the date value
            int startIndex = dateIndex + "\"date\":\"".length();
            int endIndex = jsonResponse.indexOf("\"", startIndex);
            if (endIndex != -1)
            {
                String dateValue = jsonResponse.substring(startIndex, endIndex);
                System.out.println("Date: " + dateValue);
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
                inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                try
                {
                    // Parse the input date
                    Date githubLastUpdated = inputFormat.parse(dateValue);

                    // Format the date in the desired output format
                    String formattedDate = outputFormat.format(githubLastUpdated);
                    debug("Github last updated: " + githubLastUpdated);
                    debug("AutomateRS last updated: " + automatersLastUpdated);
                    return githubLastUpdated.after(automatersLastUpdated);
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            System.out.println("Date not found in JSON response.");
        }
        return false;
    }

    public static void downloadUpdate() {
        try {
            URL url = new URL(URL_GITHUB_DOWNLOAD);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (httpURLConnection.getResponseCode() == 200) {
                try (InputStream inputStream = httpURLConnection.getInputStream()) {
                    // Save the file to the specified file path
                    Path destinationPath = Path.of(FILE_AUTOMATERS);
                    Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Downloaded and saved the file to: " + destinationPath);
                }
            } else {
                Object[] options = {"OK"};
                JOptionPane.showOptionDialog(null,
                        "Your IP is limited from auto updating for 1 hour...",
                        "Rate limit exceeded...", JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options,
                        options[0]);
            }
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
