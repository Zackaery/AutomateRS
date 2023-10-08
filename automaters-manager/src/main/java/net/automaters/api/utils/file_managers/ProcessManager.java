package net.automaters.api.utils.file_managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessManager {

    public static List<String> getProcessPIDs() throws IOException {
        Process pidProcess = Runtime.getRuntime().exec("tasklist /fi \"IMAGENAME eq java.exe\"");
        BufferedReader reader = new BufferedReader(new InputStreamReader(pidProcess.getInputStream()));
        String line;
        List<String> pids = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            if (line.contains("java.exe")) {
                String[] parts = line.split("\\s+");
                pids.add(parts[1]);
                System.out.println("PID of Java process: " + parts[1]);
            }
        }

        return pids;
    }
}
