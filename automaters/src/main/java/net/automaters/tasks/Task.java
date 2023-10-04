package net.automaters.tasks;

import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.walking.Area;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;

import javax.swing.*;

import java.util.ArrayList;

import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.tasks.TaskManager.*;
import static net.unethicalite.api.commons.Time.sleep;

public abstract class Task {


    public static ArrayList<String> taskItems = new ArrayList<>();

    private static boolean started;
    public static boolean secondaryTaskActive;

    public static long startTime;
    public static long taskDuration;
    public static int skillTask;

    public static boolean renderObjects;
    public static TileObject objectToRender;


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
        if (!taskFinished()) {
            if (!taskStarted()) {
                debug(currentTask+" - Not Started");
                onStart();
                sleep(600);
            } else {
                onLoop();
                sleep(600);
            }
        } else {
            debug(currentTask+" - Finished");
            onEnd();
            sleep(600);
        }
    }

    public abstract void onStart();
    protected abstract void onLoop();
    public abstract boolean hasNonTaskItems();
    public abstract boolean taskFinished();
    public abstract void generateSecondaryTask();
    protected void onEnd() {
        debug("Completed Task: "+currentTask+"\n\n");
        started = false;
        secondaryTaskActive = false;
        currentTask = null;

        startTime = 0;
        taskDuration = 0;
        skillTask = 0;

        task = null;
        secondaryTask = "null";

        primaryToolID = -1;
        primaryTool = null;
        secondaryTool = -1;
        outfit = null;

        taskSelected = false;
        taskStarted = false;
        taskItems.clear();
    }

    public static int getGoal(JSpinner goalSkill) {
        if (goalSkill == null || goalSkill.getValue() == null) {
            return -1;
        }
        try {
            return (int) goalSkill.getValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static boolean skillBetween(Skill skill, int low, int high) {
        return Skills.getLevel(skill) >= low && Skills.getLevel(skill) < high;
    }

    protected void interactableWalk(TileObject tileObject, String action, Area worldArea) {
        if (worldArea == null) {
            return;
        }

        NPC attacking = NPCs.getNearest(x -> x.getName() != null);
        if (attacking != null && attacking.getInteracting() != null && attacking.getInteracting() == localPlayer && localPlayer.getHealthScale() != -1) {
            debug("NPC Attacking player: "+attacking);
            automateWalk(worldArea);
        }

        if (!LocalPlayer.canInteract()) {
            return;
        }

        if (Movement.isWalking() && tileObject != null && Reachable.isInteractable(tileObject) && tileObject.distanceTo(Players.getLocal()) < 8) {
            renderObjects = true;
            objectToRender = tileObject;
            String location = String.format("%d, %d, %d, %d",
                    tileObject.getX(),
                    tileObject.getY(),
                    tileObject.getZ(),
                    tileObject.getPlane());
            Movement.setDestination(0, 0);
            debug("Found a " + tileObject.getName());
            debug(tileObject.getName() + " Location = "+location);
            debug("Interacting with: "+tileObject.getName()+" - "+action);
            tileObject.interact(action);
            while (scriptStarted && localPlayer.isMoving()) {
                sleep(333);
            }
            return;
        }

        if (tileObject == null || tileObject.distanceTo(Players.getLocal()) > 8 || !Reachable.isInteractable(tileObject)) {
            String location = String.format("%d, %d, %d, %d, %d",
                    worldArea.minX,
                    worldArea.minY,
                    worldArea.maxX,
                    worldArea.maxY,
                    worldArea.thisZ);
            debug("Walking to area: " +location);
            Movement.walkTo(worldArea.toWorldArea());
            return;
        } else {
            renderObjects = true;
            objectToRender = tileObject;
            if (LocalPlayer.canInteract()) {
                debug("Interacting with: "+tileObject.getName()+" - "+action);
                tileObject.interact(action);
                return;
            }
        }
        debug("renderObjects = false");
        renderObjects = false;
    }

}