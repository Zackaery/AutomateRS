package net.automaters.api.panels;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static net.automaters.api.utils.file_managers.FileManager.PATH_AUTOMATE_RS;
import static net.automaters.gui.main.MainComponents.PATH_AUTOMATE_RS_MANAGER;

public class ReadContents {

    public static String readFileContents(Path filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(filePath);
        return new String(bytes);
    }

    public static String[] loadFileList(Path filePath) {
        try {
            if (Files.exists(filePath)) {
                List<String> fileLines = Files.readAllLines(filePath);
                return fileLines.toArray(new String[0]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Return an empty array or handle the error as needed
        return new String[0];
    }

    public static void checkAndCreateFiles() {
        String[] fileNames = { "accountlist.txt", "pluginlist.txt", "proxylist.txt", "profilelist.txt"};

        for (String fileName : fileNames) {
            Path filePath = Paths.get(PATH_AUTOMATE_RS_MANAGER, fileName);
            try {
                createFileIfNotExists(filePath);
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
