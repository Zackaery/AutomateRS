package net.automaters.tasks.tasks;

import net.unethicalite.api.game.Skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.tasks.TaskManager.currentTask;
import static net.automaters.tasks.TaskManager.taskSelected;

public class GenerateTasks {

    private static List<Object> taskList = new ArrayList<>();
    private static List<SkillingTasks> skillingTasks = new ArrayList<>();
    private static List<QuestingTasks> questingTasks = new ArrayList<>();
    private static List<MoneyMakingTasks> moneyMakingTasks = new ArrayList<>();
    private static List<MiscellaneousTasks> miscellaneousTasks = new ArrayList<>();

    public GenerateTasks() {
        resetLists();
        getTask();
    }

    private void getTask() {
        debug("getting task");

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
