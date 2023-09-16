package net.automaters.account_builds.testing;

import net.automaters.tasks.TaskManager;

import static net.automaters.script.AutomateRS.debug;
import static net.automaters.tasks.TaskManager.currentTask;
import static net.automaters.tasks.TaskManager.taskSelected;

public class AlphaTester {

    public AlphaTester() throws InterruptedException {
        if (!taskSelected) {
            generateTask();
        } else {
            runTask();
        }
    }
    private void generateTask() {
        debug("Generating a new AlphaTester task...");
        String[] possibleTasks = {"Woodcutting", "Mining"};
        String selectedTask = possibleTasks[(int) (Math.random() * possibleTasks.length)];
        selectedTask = "Woodcutting";
        debug("Selected Task: " + selectedTask);
        taskSelected = true;
        currentTask = selectedTask;
//        TaskManager.executeTask(selectedTask, bot, api);
    }
    private void runTask() throws InterruptedException {
        new TaskManager(currentTask);
    }

}