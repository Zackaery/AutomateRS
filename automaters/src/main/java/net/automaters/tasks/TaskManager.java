package net.automaters.tasks;

import net.automaters.activities.skills.woodcutting.Woodcutting;
//import net.automaters.activities.skills.mining.Mining;
import static net.automaters.script.AutomateRS.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();
    private final Random random = new Random();

    public static boolean taskStarted;
    public static boolean taskSelected;
    public static String currentTask;
    public static long startTime;
    public static final long taskDuration = 10000; // Specify the task duration in milliseconds (e.g., 10 seconds)

    public TaskManager(String task) {
        executeTask(task);
    }

    public void executeTask(String task) {
        currentTask = task;
        switch (task) {
            case "Woodcutting":
                debug("EXECUTING WOODCUTTING TASK");
                new Woodcutting().loop();
                break;
            case "Mining":
                debug("EXECUTING MINING TASK");
//                new Mining();
                break;
        }
    }

}