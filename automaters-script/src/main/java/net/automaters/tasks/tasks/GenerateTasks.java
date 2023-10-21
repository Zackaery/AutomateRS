package net.automaters.tasks.tasks;

import net.unethicalite.api.game.Skills;

import java.util.Random;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.Variables.*;

public class GenerateTasks {

    public GenerateTasks() {
        resetLists();
        getTask();
    }

    private void getTask() {
        // SKILLING TASKS
        for (SkillingTasks task : SkillingTasks.values()) {
            if (task.getSkillGoal() > Skills.getLevel(task.getSkill())) {
                skillingTasks.add(task);
                debug(task.name() + ": Level: " + Skills.getLevel(task.getSkill()) + ", Goal: " + task.getSkillGoal());
            }
        }

        // Add tasks to the common task list
        taskList.addAll(skillingTasks);
        taskList.addAll(questingTasks);
        taskList.addAll(moneyMakingTasks);
        taskList.addAll(miscellaneousTasks);

        if (!taskList.isEmpty()) {
            debug("Task List: " + taskList);
            Random random = new Random();
            int randomList = random.nextInt(taskList.size());
            Object taskSelection = taskList.get(randomList);
            taskSelected = true;

            if (taskSelection instanceof SkillingTasks) {
                int randomSkill = random.nextInt(skillingTasks.size());
                currentTask = skillingTasks.get(randomSkill).name();
                debug("Current Task Selected: " + currentTask);
            }
        } else {
            scriptStarted = false;
        }
    }

    private void resetLists() {
        taskList.clear();
        skillingTasks.clear();
        questingTasks.clear();
        moneyMakingTasks.clear();
        miscellaneousTasks.clear();
    }

}
