package net.automaters.tasks;

//import net.automaters.activities.skills.mining.*;

import net.automaters.activities.skills.mining.Mining;
import net.automaters.activities.skills.woodcutting.Woodcutting;

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
        switch (task) {
            case "Woodcutting":
                new Woodcutting();
                break;
            case "Mining":
                new Mining();
                break;
        }
    }

}