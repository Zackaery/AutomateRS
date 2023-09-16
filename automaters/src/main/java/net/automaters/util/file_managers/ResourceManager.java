package net.automaters.util.file_managers;


import net.automaters.script.AutomateRS;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ResourceManager {

    private static final String DIRECTORY = Paths.get(
            System.getProperty("user.home"),
            ".openosrs",
            "data",
            "AutomateRS",
            "resources"
    ).toString();

    private static final String GITHUB_URL = "https://github.com/Zackaery/Account-Builder";

    public static InputStream loadFile(final String relativeFilePath) {
        File directory = Paths.get(DIRECTORY, relativeFilePath).toFile();
//        if (!directory.exists()) {
//
//        }

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
        AutomateRS.debug(jarFilePath);
        return ResourceManager.class.getResourceAsStream(jarFilePath);
    }

    private static boolean fileExistsInDataDir(final String relativeFilePath) {
        File file = Paths.get(DIRECTORY, relativeFilePath).toFile();
        return file.exists() && file.length() > 0;
    }

    private static InputStream loadFileFromDataDir(final String relativeFilePath) {
        File file = Paths.get(DIRECTORY, relativeFilePath).toFile();

        if (!file.exists()) {
            AutomateRS.debug(
                    String.format("'%s' does not exist", file.toString())
            );
            return null;
        }

        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            AutomateRS.debug(
                    String.format("Failed to load file '%s' from '%s'", relativeFilePath, DIRECTORY)
            );
        }

        return null;
    }

    private static void downloadResourcesFromGitHubToDataDirectory() {
        try {
            URL url = new URL(GITHUB_URL + "/raw/main/images.zip");
            AutomateRS.debug("Downloading file from url: " + url.toString() + ", to: " + DIRECTORY);

            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());

            File outputFile = Paths.get(DIRECTORY, "images.zip").toFile();

            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            AutomateRS.debug(String.format("Extracting file: %s, to: %s", outputFile.getAbsolutePath(), DIRECTORY));

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
                AutomateRS.debug("Failed to make directory: " + parentDir.toString());
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
        try(ZipFile zipFile = new ZipFile(archive))
        {
            FileSystem fileSystem = FileSystems.getDefault();

            destinationDir.mkdirs();

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements())
            {
                ZipEntry entry = entries.nextElement();

                Path filePath = fileSystem.getPath(destinationDir.getAbsolutePath(), entry.getName());

                AutomateRS.debug("Unzipping file to: " + filePath.toString());

                if (entry.isDirectory())
                {
                    Files.createDirectories(filePath);
                }
                else
                {
                    InputStream is = zipFile.getInputStream(entry);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    Files.createFile(filePath);
                    FileOutputStream fileOutput = new FileOutputStream(filePath.toFile());
                    while (bis.available() > 0)
                    {
                        fileOutput.write(bis.read());
                    }
                    fileOutput.close();
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}