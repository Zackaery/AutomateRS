package net.automaters.activities.skills.mining;

import net.automaters.activities.skills.firemaking.DynamicFiremaking;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.walking.Area;
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
import static net.automaters.activities.skills.mining.Ores.*;
import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.items.SecondaryTools.getSecondaryTool;
import static net.automaters.api.ui.InventoryUtils.getAmountItemsNotInList;
import static net.automaters.api.ui.InventoryUtils.getItemsNotInList;
import static net.automaters.api.utils.Calculator.random;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.goalFiremaking;
import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.goalFletching;
import static net.automaters.api.items.PrimaryTools.*;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.game.Skills.getLevel;

public class Mining extends Task {

    private static TileObject resourceObject;
    private static Area resourceLocation;
    private static boolean hasResources;
    public Mining() {
        super();
    }

    @Override
    public void onStart() {
        if (primaryTool == null) {
            getPrimaryMiningTool();
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
                    Bank.close();
                    return;
                }
            }
        } else {
            setStarted(true);
            debug("Primary Tool: "+primaryTool);
            debug("Secondary Tool: "+secondaryTool);
            debug("Secondary Task: "+secondaryTask);
        }
    }
    @Override
    protected void onLoop() {
        var tool = primaryTool;
        var resource = Inventory.getFirst(x -> x.getName().toLowerCase(Locale.ROOT).contains("ore"));
        if (tool != null) taskItems.add(tool);
        if (resource != null) taskItems.add(resource.getName());

        List<Item> inventoryItems = Inventory.getAll();
        List<String> itemsToCheck = taskItems;

        if (getAmountItemsNotInList(inventoryItems, itemsToCheck) >= 5) {
            debug("Banking non-task items.");
            openBank();
            while (scriptStarted && Bank.isOpen() && Inventory.contains(Predicates.nameContains(taskItems))) {
                Bank.depositAllExcept(Predicates.nameContains(taskItems));
                sleep(333);
            }
        }

        if (!Inventory.contains(tool) && !Equipment.contains(tool)) {
            debug("no tool found");
            getPrimaryMiningTool();
            sleep(333);
            return;
        }

        if (resourceObject == null || resourceLocation == null || resource == null && (!hasResources)) {
            getResources();
        }

        if (Inventory.isFull() && resource != null || secondaryTaskActive) {
            hasResources = false;
            switch (secondaryTask) {
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

        if (!Inventory.isFull() && LocalPlayer.canInteract()) {
            if (resourceObject == null) {
                resourceObject = getOre();
            }
            interactableWalk(getOre(), "Mine", resourceLocation);
            sleep(600);
        }

    }

    @Override
    public boolean hasNonTaskItems() {
        if (!taskItems.contains(primaryTool)) {
            var tool = primaryTool;
            taskItems.add(tool);
        }

        debug("Task Items: "+taskItems);

        List<Item> inventory = Inventory.getAll();
        List<String> itemsToCheck = taskItems;
        List<Item> itemsNotInList = getItemsNotInList(inventory, itemsToCheck);

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
        secondaryTask = "None";
    }

    private void getPrimaryMiningTool() {
        if (!Inventory.contains(Predicates.nameContains("pickaxe"))
                && !Equipment.contains(Predicates.nameContains("pickaxe"))) {
            debug("getPrimaryTool");
            primaryTool = null;
            primaryToolID = -1;
            getPrimaryTool(MiningTools.class);
        } else {
            Item tool = Inventory.getFirst(Predicates.nameContains("pickaxe"));
            Item wornTool = Equipment.getFirst(Predicates.nameContains("pickaxe"));
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
        secondaryTaskActive = false;
        hasResources = true;
        resourceObject = Ores.getOre();
        resourceLocation = Ores.getOreLocation();
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
