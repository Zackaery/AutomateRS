package net.automaters.account_builds.testing;

import net.automaters.tasks.GenerateTasks;
import net.automaters.tasks.SkillingTasks;
import net.automaters.tasks.TaskManager;
import net.unethicalite.api.game.Skills;

import java.util.ArrayList;
import java.util.List;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.tasks.TaskManager.currentTask;
import static net.automaters.tasks.TaskManager.taskSelected;

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