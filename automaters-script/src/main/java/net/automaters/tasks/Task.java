package net.automaters.tasks;

import lombok.Getter;
import lombok.Setter;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.walking.Area;
import net.runelite.api.NPC;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;

import javax.swing.*;

import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.script.Variables.*;
import static net.automaters.tasks.TaskManager.*;
import static net.unethicalite.api.commons.Time.sleep;

public abstract class Task {

    @Getter
    @Setter
    protected static boolean started;

    @Getter
    @Setter
    protected static boolean ended;

    public Task() {
        if (isStarted()) {
            if (taskFinished()) {
                if (isEnded()) {
                    debug(currentTask + " - Finished");
                    sleep(600);
                    reset();
                } else {
                    sleep(600);
                    onEnd();
                }
            } else {
                sleep(600);
                onLoop();
            }
        } else {
            debug(currentTask + " - Not Started");
            sleep(600);
            onStart();
        }
    }

    protected abstract void onStart();
    protected abstract void onLoop();
    protected abstract void onEnd();
    protected abstract boolean taskFinished();
    protected abstract void generateSecondaryTask();
    private void reset() {
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
        secondaryToolID = -1;
        primaryTool = null;
        secondaryTool = null;
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

        // Check if the timer has elapsed
        long currentTime = System.currentTimeMillis();

        if (!LocalPlayer.canInteract()) {
            lastInteractionTime = System.currentTimeMillis();
            return;
        }


        if (currentTime - lastInteractionTime < interactionCooldown) {
            delayWalk = true;
        } else {
            delayWalk = false;
        }

        if (Movement.isWalking() && tileObject != null && Reachable.isInteractable(tileObject) && tileObject.distanceTo(Players.getLocal()) < 15) {
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

        if (tileObject == null || tileObject.distanceTo(Players.getLocal()) > 16 || !Reachable.isInteractable(tileObject)) {
            if (!delayWalk) {
                String location = String.format("%d, %d, %d, %d, %d",
                        worldArea.minX,
                        worldArea.minY,
                        worldArea.maxX,
                        worldArea.maxY,
                        worldArea.thisZ);
                debug("Walking to area: " + location);
                Movement.walkTo(worldArea.toWorldArea());
            }
        } else {
            objectToRender = tileObject;
            if (LocalPlayer.canInteract() && !localPlayer.isMoving()) {
                debug("Interacting with: "+tileObject.getName()+" - "+action);
                tileObject.interact(action);
                lastInteractionTime = System.currentTimeMillis();
            }
        }
    }

}