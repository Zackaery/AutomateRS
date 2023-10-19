package net.automaters.activities.skills.woodcutting;

import net.automaters.activities.skills.firemaking.DynamicFiremaking;
import net.automaters.api.automate_utils.AutomateBanking;
import net.automaters.api.automate_utils.AutomateInventory;
import net.automaters.api.automate_utils.AutomatePlayer;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.walking.Area;
import net.automaters.script.Variables;
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
import static net.automaters.script.Variables.*;
import static net.automaters.tasks.utils.Setup.*;
import static net.automaters.tasks.utils.Setup.startTask;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.commons.Time.sleepUntil;
import static net.unethicalite.api.game.Skills.getLevel;

public class Woodcutting extends Task {

    private GameObject object;
    private Area location;
    private String action = "Chop down";

    static ArrayList<String> resources;
    static ArrayList<String> taskItems = new ArrayList<>();

    static {
        resources = new ArrayList<>(Arrays.asList("Logs", "Oak logs", "Willow logs", "Maple logs", "Teak logs", "Yew logs", "Magic logs"));
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
        addItemsToList(true, taskItems, resources);
        addItemsToList(taskItems, primaryTool);
        addItemsToList(taskItems, "Tinderbox");
        addItemsToList(taskItems, "Knife");

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

        if (object == null && LocalPlayer.canInteract()) {
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
            getPrimaryTool(true, WoodcuttingTools.class);
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
        object = Trees.getTree();
        location = Trees.getTreeLocation();
        if (object != null
                && location != null) {
            String area = String.format("%d, %d, %d, %d, %d",
                    location.minX,
                    location.minY,
                    location.maxX,
                    location.maxY,
                    location.thisZ);
            debug("Resource Name: " + object.getName());
            debug("Resource Location: " + area);
            Variables.resourceObject = object.getName();
            Variables.resourceLocation = area;
            Variables.resourceAction = action;
            Variables.resourceItems = resources;
        }
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
            automateWalk(location);
        }

        if (object != null && localPlayer.distanceTo(object) <= 12
                && Reachable.isInteractable(object)) {
            debug("Chopping down: " + object.getName());
            object.interact(action);
            sleepUntil(() -> !LocalPlayer.canInteract(), 1800);
            sleepUntil(() -> LocalPlayer.canInteract() || !Reachable.isInteractable(object), 5500);
        } else {
            if (LocalPlayer.canInteract()) {
                if (location != null) {
                    automateWalk(location);
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
