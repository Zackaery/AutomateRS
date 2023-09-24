package net.automaters.tasks;

//import net.automaters.activities.skills.mining.Mining;

import net.automaters.activities.skills.mining.MiningBored;
import net.automaters.activities.skills.woodcutting.WoodcuttingBored;

import static net.automaters.api.utils.Debug.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();
    private final Random random = new Random();

    public static boolean taskStarted;
    public static boolean taskSelected;
    public static String currentTask;

    public TaskManager(String task) {
        executeTask(task);
    }

    public void executeTask(String task) {
        currentTask = task;
        debug("Executing Task: "+task);
        switch (task) {
            case "Woodcutting":
                new WoodcuttingBored();
                break;
            case "Mining":
//                new Mining();
                break;
        }
    }

}