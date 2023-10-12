package net.automaters.tasks;

//import net.automaters.activities.skills.mining.*;

import net.automaters.activities.skills.mining.Mining;
import net.automaters.activities.skills.woodcutting.Woodcutting;
import net.automaters.tasks.tasks.GenerateTasks;

public class TaskManager {

    public static boolean taskStarted;
    public static boolean taskSelected;
    public static String currentTask;

    public TaskManager(String task) {
        executeTask(task);
    }

    public void executeTask(String task) {

        // ENABLE TESTING
//        new Test();

        switch (currentTask) {
            case "WOODCUTTING":
                new Woodcutting();
                break;
            case "MINING":
                new Mining();
                break;
            case "FLETCHING":
//                new Fletching();
                new GenerateTasks();
                break;
            case "FIREMAKING":
//                new Firemaking();
                new GenerateTasks();
                break;

            default:
                break;
        }
    }

}