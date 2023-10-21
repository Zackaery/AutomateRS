package net.automaters.account_builds.testing;

import net.automaters.tasks.tasks.GenerateTasks;
import net.automaters.tasks.TaskManager;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.Variables.currentTask;
import static net.automaters.script.Variables.taskSelected;

public class AlphaTester {

    public AlphaTester() {
        if (!taskSelected) {
            new GenerateTasks();
        } else {
            runTask();
        }
    }
    private void generateTask() {
        debug("Generating a new AlphaTester task...");
        String[] possibleTasks = {"Woodcutting", "Mining"};
        String selectedTask = possibleTasks[(int) (Math.random() * possibleTasks.length)];
        selectedTask = "";
        debug("Selected Task: " + selectedTask);
        taskSelected = true;
        currentTask = selectedTask;
    }
    private void runTask() {
        new TaskManager(currentTask);
    }



}