package net.automaters.util.file_managers;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.util.file_managers.FileManager.URL_GITHUB;

public class ResourceManager {

    private static final String DIRECTORY = Paths.get(
            System.getProperty("user.home"),
            ".openosrs",
            "data",
            "AutomateRS"
    ).toString();

    public static InputStream loadFile(final String relativeFilePath) {

        if (fileExistsInDataDir(relativeFilePath)) {
            return loadFileFromDataDir(relativeFilePath);
        }

        InputStream jarInputStream = loadFileFromJar(relativeFilePath);

        if (jarInputStream != null) {
            saveFileToDataDirectory(jarInputStream, relativeFilePath);
            return loadFileFromDataDir(relativeFilePath);
        }


        downloadResourcesFromGitHubToDataDirectory();
        return loadFileFromDataDir(relativeFilePath);
    }

    private static InputStream loadFileFromJar(final String relativeFilePath) {
        String jarFilePath = "/resources/" + relativeFilePath;
        debug(jarFilePath);
        return ResourceManager.class.getResourceAsStream(jarFilePath);
    }

    private static boolean fileExistsInDataDir(final String relativeFilePath) {
        File file = Paths.get(DIRECTORY, relativeFilePath).toFile();
        return file.exists() && file.length() > 0;
    }

    private static InputStream loadFileFromDataDir(final String relativeFilePath) {
        File file = Paths.get(DIRECTORY, relativeFilePath).toFile();

        if (!file.exists()) {
            debug(
                    String.format("'%s' does not exist", file.toString())
            );
            return null;
        }

        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            debug(
                    String.format("Failed to load file '%s' from '%s'", relativeFilePath, DIRECTORY)
            );
        }

        return null;
    }

    private static void downloadResourcesFromGitHubToDataDirectory() {

        debug("Downloading Resources From Github");
        try {
            URL url = new URL(URL_GITHUB + "/raw/main/resources.zip");
            debug("Downloading file from url: " + url.toString() + ", to: " + DIRECTORY);

            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());

            File outputFile = Paths.get(DIRECTORY, "resources.zip").toFile();

            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            debug(String.format("Extracting file: %s, to: %s", outputFile.getAbsolutePath(), DIRECTORY));

            unzipArchive(outputFile, new File(DIRECTORY));

            outputFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static synchronized boolean saveFileToDataDirectory(final InputStream inputStream, final String relativeFilePath) {
        if (inputStream == null) {
            return false;
        }

        Path outputFilePath = Paths.get(DIRECTORY, relativeFilePath);
        File parentDir = outputFilePath.getParent().toFile();

        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                debug("Failed to make directory: " + parentDir.toString());
                return false;
            }
        }

        try {
            Files.copy(
                    inputStream,
                    Paths.get(DIRECTORY, relativeFilePath),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static synchronized void unzipArchive(final File archive, final File destinationDir) {
        try (ZipFile zipFile = new ZipFile(archive)) {
            destinationDir.mkdirs();
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                debug("ZipEntry = " + entry.toString());

                Path filePath = Paths.get(destinationDir.getAbsolutePath(), entry.getName());
                debug("Unzipping file to: " + filePath.toString());

                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    try (InputStream is = zipFile.getInputStream(entry);
                         BufferedInputStream bis = new BufferedInputStream(is)) {
                        Files.createFile(filePath);
                        try (FileOutputStream fileOutput = new FileOutputStream(filePath.toFile());
                             BufferedOutputStream bos = new BufferedOutputStream(fileOutput)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = bis.read(buffer)) != -1) {
                                bos.write(buffer, 0, bytesRead);
                            }
                        }
                        debug("Extracted file: " + filePath.toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}