package net.automaters.util.file_managers;

import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {
    public static final String PATH_AUTOMATE_RS = System.getProperty("user.home") + File.separator + ".openosrs" + File.separator + "data" + File.separator + "AutomateRS" + File.separator;
    public static final String PATH_GE_PRICES = PATH_AUTOMATE_RS + "Grand Exchange Prices" + File.separator + "prices.json";
    public static boolean getLastModified(File file, int difference) {
        long lastModifiedTime = file.lastModified();
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastModifiedTime;
        long timeDifferenceMinutes = timeDifference / (60 * 1000);

        return timeDifferenceMinutes > difference;
    }

    public static void formatJsonFile(String filePath) {
        try {
            JsonSerializer<Number> numberSerializer = (src, typeOfSrc, context) -> {
                if (src instanceof Double) {
                    Double value = (Double) src;
                    if (value == Math.floor(value)) {
                        return new JsonPrimitive(value.longValue());
                    }
                }
                return new JsonPrimitive(src);
            };

            String json = new String(Files.readAllBytes(Paths.get(filePath)));

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Number.class, numberSerializer)
                    .create();

            JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
            String formattedJson = gson.toJson(jsonElement);

            Files.write(Paths.get(filePath), formattedJson.getBytes(StandardCharsets.UTF_8));
            System.out.println("File formatted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readJsonFromFile(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
