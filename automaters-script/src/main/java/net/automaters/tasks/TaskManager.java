package net.automaters.tasks;

//import net.automaters.activities.skills.mining.*;

import net.automaters.activities.skills.fishing.Fishing;
import net.automaters.activities.skills.mining.Mining;
import net.automaters.activities.skills.woodcutting.Woodcutting;
import net.automaters.tasks.tasks.GenerateTasks;
import net.automaters.tasks.tests.TestPluginCode;

import static net.automaters.script.Variables.currentTask;

public class TaskManager {


    public TaskManager(String task) {
        executeTask(task);
    }

    public void executeTask(String task) {
        boolean testing = false;
        if (testing) {
            new TestPluginCode();
        } else {
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
            case "FISHING":
                new Fishing();
                break;

            default:
                break;
        }
        }
    }

}