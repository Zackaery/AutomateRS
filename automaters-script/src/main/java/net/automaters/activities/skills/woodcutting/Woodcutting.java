package net.automaters.activities.skills.woodcutting;

import net.automaters.activities.skills.firemaking.DynamicFiremaking;
import net.automaters.api.automate_utils.AutomateBanking;
import net.automaters.api.automate_utils.AutomateInventory;
import net.automaters.api.automate_utils.AutomatePlayer;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.walking.Area;
import net.automaters.tasks.Task;
import net.runelite.api.*;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import net.runelite.api.Item;
import net.unethicalite.api.movement.Reachable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Locale;

import static net.automaters.activities.skills.firemaking.DynamicFiremaking.firemaking;
import static net.automaters.api.automate_utils.AutomateUtils.addItemsToList;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.items.SecondaryTools.getSecondaryTool;
import static net.automaters.api.utils.Calculator.random;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.goalFiremaking;
import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.goalFletching;
import static net.automaters.api.items.PrimaryTools.*;
import static net.automaters.tasks.utils.Setup.*;
import static net.automaters.tasks.utils.Setup.startTask;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.commons.Time.sleepUntil;
import static net.unethicalite.api.game.Skills.getLevel;

public class Woodcutting extends Task {

    private GameObject resourceObject;
    private Area resourceLocation;

    static ArrayList<String> resources = new ArrayList<>();
    static ArrayList<String> taskItems = new ArrayList<>();

    static {
        addItemsToList(resources, " logs");
        addItemsToList(resources, "Tinderbox");
        addItemsToList(resources, "Knife");
        taskItems.addAll(resources);
    }

    public Woodcutting() {
        super();
    }

    @Override
    public void onStart() {
        if (setupPrimaryTool()) {
            if (setupSecondaryTask()) {
                if (setupSecondaryTool()) {
                    if (!hasNonTaskItems()) {
                        startTask();
                        setStarted(true);
                    } else {
                        handleNonTaskItems();
                    }
                } else {
                    getSecondaryTool(secondaryTask);
                }
            } else {
                generateSecondaryTask();
            }
        } else {
            getPrimaryWoodcuttingTool();
        }
    }

    @Override
    public void onLoop() {
        addItemsToList(taskItems, primaryTool);
        addItemsToList(true, taskItems, resources);

        if (AutomateInventory.getAmount(false, taskItems) >= 5) {
            handleNonTaskItems();
        }

        if (!AutomatePlayer.hasItems(primaryTool)) {
            getPrimaryWoodcuttingTool();
        }

        if (Inventory.isFull() || secondaryTaskActive) {
            handleInventory();
            return;
        }

        if (resourceObject == null && LocalPlayer.canInteract()) {
            getResources();
        }

        if (!Inventory.isFull()) {
            interactWithResource();
        }
    }

    @Override
    protected void onEnd() {
        // Handle end methods
        setEnded(true);
    }

    /**
     * # onStart() methods below
     */

    private void getPrimaryWoodcuttingTool() {
        if (!Inventory.contains(Predicates.nameContains(" axe"))
                && !Equipment.contains(Predicates.nameContains(" axe"))) {
            primaryTool = null;
            primaryToolID = -1;
            getPrimaryTool(WoodcuttingTools.class);
        } else {
            Item tool = Inventory.getFirst(Predicates.nameContains(" axe"));
            Item wornTool = Equipment.getFirst(Predicates.nameContains(" axe"));
            if (tool != null) {
                debug("Inventory tool: "+tool.getName());
                primaryTool = tool.getName();
            } else if (wornTool != null) {
                debug("Worn tool: "+wornTool.getName());
                primaryTool = wornTool.getName();
            } else {
                primaryTool = null;
            }
        }
    }

    @Override
    public void generateSecondaryTask() {
        skillTask = random(1, 100);
        if (Worlds.inMembersWorld()) {
            if (getLevel(Skill.FLETCHING) < getGoal(goalFletching) && getLevel(Skill.FIREMAKING) < getGoal(goalFiremaking)) {
                secondaryTask = (random(1, 100) <= 50) ? "fletching" : "firemaking";
            } else if (getLevel(Skill.FLETCHING) < getGoal(goalFletching)) {
                secondaryTask = "fletching";
            } else if (getLevel(Skill.FIREMAKING) < getGoal(goalFiremaking)) {
                secondaryTask = "firemaking";
            } else {
                secondaryTask = "none";
            }
        } else if (getLevel(Skill.FIREMAKING) < getGoal(goalFiremaking)) {
            secondaryTask = "firemaking";
        } else {
            secondaryTask = "none";
        }
    }

    private void handleNonTaskItems() {
        if (!isStarted()) {
            List<String> tools = Arrays.asList(primaryTool, secondaryTool);
            AutomateBanking.bankAllExcept(false, tools);
        } else {
            AutomateBanking.bankAll(false, taskItems);
        }
    }

    /**
     * # onLoop() methods below
     */

    private void handleInventory() {
        secondaryTaskActive = true;
        switch (secondaryTask) {
            case "firemaking":
                new DynamicFiremaking();
                sleep(333);
                return;
            case "fletching":
                //execute Fletching.java task
                return;
            case "bank":
                handleNonTaskItems();
                return;
            default:
                AutomateInventory.dropAll(resources);
                secondaryTaskActive = false;
                break;
        }
    }


    private void getResources() {
        firemaking = false;
//        fletching = false;
        secondaryTaskActive = false;
        resourceObject = Trees.getTree();
        resourceLocation = Trees.getTreeLocation();
        String l = String.format("%d, %d, %d, %d, %d",
                resourceLocation.minX,
                resourceLocation.minY,
                resourceLocation.maxX,
                resourceLocation.maxY,
                resourceLocation.thisZ);
        debug("Resource Name: " + resourceObject.getName());
        debug("Resource Location: "+l);
    }

    private void interactWithResource() {

        var nest = TileItems.getNearest(x -> x.getName().toLowerCase(Locale.ROOT).contains(" nest"));
        if (nest != null) {
            debug("Looting: "+nest.getName());
            nest.interact("Take");
            sleep(333);
        }

        NPC attacking = NPCs.getNearest(x -> x.getName() != null);
        if (attacking != null && attacking.getInteracting() != null && attacking.getInteracting() == localPlayer && localPlayer.getHealthScale() != -1) {
            debug("NPC Attacking player: "+attacking);
            automateWalk(resourceLocation);
        }

        if (resourceObject != null && localPlayer.distanceTo(resourceObject) <= 12
                && Reachable.isInteractable(resourceObject)) {
            debug("Chopping down: " + resourceObject.getName());
            resourceObject.interact("Chop down");
            sleepUntil(() -> !LocalPlayer.canInteract(), 1800);
            sleepUntil(() -> LocalPlayer.canInteract() || !Reachable.isInteractable(resourceObject), 5500);
        } else {
            if (LocalPlayer.canInteract()) {
                if (resourceLocation != null) {
                    automateWalk(resourceLocation);
                } else {
                    getResources();
                }
            } else {
                sleepUntil(() -> LocalPlayer.canInteract(), 1200);
            }
        }
    }

    /**
     * # onEnd() methods below
     *
     */

    @Override
    public boolean taskFinished() {
        return false;
    }

}
