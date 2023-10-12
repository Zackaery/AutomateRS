package net.automaters.activities.skills.woodcutting;

import net.automaters.activities.skills.firemaking.DynamicFiremaking;
import net.automaters.tasks.Task;
import net.runelite.api.*;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import net.runelite.api.Item;

import java.util.List;

import java.util.Locale;

import static net.automaters.activities.skills.firemaking.DynamicFiremaking.firemaking;
import static net.automaters.activities.skills.woodcutting.Trees.*;
import static net.automaters.api.automate_utils.AutomateInventory.getAmount;
import static net.automaters.api.automate_utils.AutomateInventory.getItemsFromList;
import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.items.SecondaryTools.getSecondaryTool;
import static net.automaters.api.utils.Calculator.random;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.goalFiremaking;
import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.goalFletching;
import static net.automaters.api.items.PrimaryTools.*;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.game.Skills.getLevel;

public class Woodcutting extends Task {

    public Woodcutting() {
        super();
    }

    @Override
    public void onStart() {
        if (primaryTool == null) {
            getPrimaryWoodcuttingTool();
            sleep(333);
        } else if (skillTask == 0) {
            generateSecondaryTask();
            if (!secondaryTask.equals("none")) {
                getSecondaryTool(secondaryTask);
                sleep(333);
            }
        } else if (hasNonTaskItems()) {
            openBank();
            while (scriptStarted && Bank.isOpen()) {
                if (hasNonTaskItems()) {
                    Bank.depositAllExcept(Predicates.nameContains(taskItems));
                    sleep(333);
                } else {
                    break;
                }
            }
        } else {
            if (!Inventory.contains(primaryTool) && !Equipment.contains(primaryTool)) {
                getPrimaryWoodcuttingTool();
            } else if (secondaryTask.equals("Fletching") && !Inventory.contains("Knife")
                    || secondaryTask.equals("Firemaking") && !Inventory.contains("Tinderbox")) {
                getSecondaryTool(secondaryTask);
            } else {
                if (Bank.isOpen()) {
                    Bank.close();
                }
                setStarted(true);
                debug("Primary Tool: " + primaryTool);
                debug("Secondary Tool: " + secondaryTool);
                debug("Secondary Task: " + secondaryTask);
            }
        }
    }
    @Override
    protected void onLoop() {
        var tool = primaryTool;
        var resource = Inventory.getFirst(x -> x.getName().toLowerCase(Locale.ROOT).contains("logs"));
        var tinderbox = Inventory.getFirst("Tinderbox");
        var knife = Inventory.getFirst("Knife");
        var nest = TileItems.getNearest(x -> x.getName().toLowerCase(Locale.ROOT).contains(" nest"));

        if (tool != null) taskItems.add(tool);
        if (resource != null) taskItems.add(resource.getName());
        if (tinderbox != null) taskItems.add(tinderbox.getName());
        if (knife != null) taskItems.add(knife.getName());

        List<Item> inventoryItems = Inventory.getAll(); // Get your inventory items
        List<String> itemsToCheck = taskItems; // Define the list of item names to check

        if (getAmount(false, itemsToCheck) >= 5) {
            debug("Banking non-task items.");
            openBank();
            while (scriptStarted && Bank.isOpen() && Inventory.contains(Predicates.nameContains(taskItems))) {
                Bank.depositAllExcept(Predicates.nameContains(taskItems));
                sleep(333);
            }
        }

        if (!Inventory.contains(tool) && !Equipment.contains(tool)) {
            debug("no tool found");
            getPrimaryWoodcuttingTool();
            sleep(333);
            return;
        }

        if (nest != null) {
            nest.interact("Take");
            sleep(333);
            return;
        }

        if (!secondaryTaskActive) {
            if (resourceObject == null || resourceLocation == null || resource == null && (!hasResources)) {
                getResources();
            }
        }

        if (Inventory.isFull() && resource != null || secondaryTaskActive) {
            hasResources = false;
            secondaryTaskActive = true;
            switch (secondaryTask) {
                case "Firemaking":
                    if (tinderbox != null && Inventory.isFull() || firemaking) {
                        new DynamicFiremaking();
                        sleep(333);
                        return;
                    }
                    break;
                case "Fletching":
                    if (knife != null) {
                        //execute Fletching.java task
                        return;
                    }
                    break;
                case "Bank":
                    if (Inventory.isFull() && !Bank.isOpen()) {
                        openBank();
                        if (Bank.isOpen()) {
                            Bank.depositAllExcept(Predicates.nameContains(taskItems));
                        }
                        sleep(333);
                    }
                    return;
                default:
                    resource.drop();
                    sleep(333);
                    secondaryTaskActive = false;
                    return;
            }
        }

        if (!Inventory.isFull()) {
            if (resourceObject == null) {
                resourceObject = getTree();
            }
            interactableWalk(getTree(), "Chop down", resourceLocation);
            sleep(600);
        }

    }

    @Override
    public boolean hasNonTaskItems() {
        if (!taskItems.contains(primaryTool)) {
            var tool = primaryTool;
            taskItems.add(tool);
        }
        if (!taskItems.contains("Tinderbox")) {
            taskItems.add("Tinderbox");
        }
        if (!taskItems.contains("Knife")) {
            taskItems.add("Knife");
        }
        var resource = Inventory.getAll(x -> x.getName().toLowerCase(Locale.ROOT).contains("logs"));
        for (Item item : resource) {
            if (!taskItems.contains(item.getName())) {
                taskItems.add(item.getName());
            }
        }
        debug("Task Items: "+taskItems);

        List<String> itemsToCheck = taskItems;
        List<Item> itemsNotInList = getItemsFromList(false, itemsToCheck);

        int itemCount = 0;
        for (Item item : itemsNotInList) {
            if (Inventory.contains(item.getName())) {
                itemCount++;
                System.out.println("Has non task item: " + item.getName());
            }
        }
        if (itemCount != 0) {
            return true;
        }
        System.out.println("All items in the inventory are in the list.");
        return false;
    }

    @Override
    public boolean taskFinished() {
            return false;
    }

    @Override
    public void generateSecondaryTask() {
        skillTask = random(1, 100);
        if (Worlds.inMembersWorld()) {
            if (getLevel(Skill.FLETCHING) < getGoal(goalFletching) && getLevel(Skill.FIREMAKING) < getGoal(goalFiremaking)) {
                secondaryTask = (random(1, 100) <= 50) ? "Fletching" : "Firemaking";
            } else if (getLevel(Skill.FLETCHING) < getGoal(goalFletching)) {
                secondaryTask = "Fletching";
            } else if (getLevel(Skill.FIREMAKING) < getGoal(goalFiremaking)) {
                secondaryTask = "Firemaking";
            } else {
                secondaryTask = "None";
            }
        } else if (getLevel(Skill.FIREMAKING) < getGoal(goalFiremaking)) {
            secondaryTask = "Firemaking";
        } else {
            secondaryTask = "None";
        }
    }

    private void getPrimaryWoodcuttingTool() {
        if (!Inventory.contains(Predicates.nameContains(" axe"))
                && !Equipment.contains(Predicates.nameContains(" axe"))) {
            debug("getPrimaryTool");
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

    private void getResources() {
        firemaking = false;
//        fletching = false;
        secondaryTaskActive = false;
        hasResources = true;
        resourceObject = Trees.getTree();
        resourceLocation = Trees.getTreeLocation();
        if (resourceObject != null && resourceLocation != null) {
            String location = String.format("Location: %d, %d, %d, %d, %d",
                    resourceLocation.minX,
                    resourceLocation.minY,
                    resourceLocation.maxX,
                    resourceLocation.maxY,
                    resourceLocation.thisZ);
            debug("Resource: " + resourceNames);
            debug(location);
        }
    }



}
