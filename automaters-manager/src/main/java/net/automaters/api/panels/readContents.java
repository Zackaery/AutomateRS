package net.automaters.api.panels;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class readContents {

    public static String readFileContents(Path filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(filePath);
        return new String(bytes);
    }

    // read script file
    public static String[] loadScriptList(Path scriptfilePath) {
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
    public static String[] loadProxyList(Path proxyfilePath) {
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
    public static String[] loadWorldList(Path worldfilePath) {
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
    public static String[] loadProfileList(Path profilefilePath) {
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

    public static void checkAndCreateFiles() {
        String baseDirectory = System.getProperty("user.home") + File.separator + ".openosrs" + File.separator + "data" + File.separator + "AutomateRS" + File.separator + "Manager";

        String[] fileNames = { "botlist.txt", "scriptlist.txt", "proxylist.txt", "profilelist.txt", "worldlist.txt" };

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

    public static void createFileIfNotExists(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent()); // Create parent directories if needed
            Files.createFile(filePath); // Create the file
        }
    }
}
