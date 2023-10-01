package net.automaters.tasks;

import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.walking.Area;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;

import javax.swing.*;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.tasks.TaskManager.*;
import static net.unethicalite.api.commons.Time.sleep;

public abstract class Task {

    private static boolean started;

    public static long startTime;
    public static long taskDuration;
    public static int skillTask;


    public static String task;
    public static String secondaryTask = "null";
    public static int primaryToolID;
    public static String primaryTool = null;
    public static int secondaryTool;
    public static String outfit = null;

    public boolean taskStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }

    public Task() {
        debug("Task - Status");
        if (!taskFinished()) {
            debug("Task - Not Finished");
            if (!taskStarted()) {
                debug("Task - Not Started");
                onStart();
            } else {
                debug("Task - Started");
                onLoop();
            }
        } else {
            debug("Task - Finished");
            onEnd();
        }
    }

    public abstract void onStart();
    protected abstract int onLoop();
    public abstract boolean taskFinished();
    public abstract void generateSecondaryTask();
    protected void onEnd() {
        debug("Completed Task: "+currentTask+"\n\n");
        currentTask = null;
        taskSelected = false;
        taskStarted = false;
        skillTask = 0;
        secondaryTask = "null";
    }

    public static int getGoal(JSpinner goalSkill) {
        if (goalSkill == null || goalSkill.getValue() == null) {
            return 1;
        }
        try {
            return (int) goalSkill.getValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 1;
        }
    }
    public static boolean skillBetween(Skill skill, int low, int high) {
        return Skills.getLevel(skill) >= low && Skills.getLevel(skill) < high;
    }

    protected int interactableWalk(TileObject tileObject, String action, Area worldArea) {
        if (Movement.isWalking() && tileObject != null && Reachable.isInteractable(tileObject)) {
            Movement.setDestination(0, 0);
            debug("Found a "+tileObject.getName());
            tileObject.interact(action);
            return -1;
        }

        if (Players.getLocal().isMoving() || Players.getLocal().isAnimating()) {
            return -1;
        }

        if (tileObject == null || tileObject.distanceTo(Players.getLocal()) > 20 || !Reachable.isInteractable(tileObject)) {
            debug("Walking to area.");
            Movement.walkTo(worldArea.toWorldArea());
            return -2;
        } else {
            if (LocalPlayer.canInteract()) {
                tileObject.interact(action);
                return -1;
            }
        }
        return -1;
    }

}