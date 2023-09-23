package net.automaters.account_builds.testing;

import net.automaters.tasks.TaskManager;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.tasks.TaskManager.currentTask;
import static net.automaters.tasks.TaskManager.taskSelected;

public class AlphaTester {

    public AlphaTester() {
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
    private void runTask() {
        new TaskManager(currentTask);
    }

}