package net.automaters.activities.skills.woodcutting;

import net.automaters.activities.skills.firemaking.DynamicFiremaking;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.items.TaskItems;
import net.automaters.api.ui.InventoryUtils;
import net.automaters.api.walking.Area;
import net.automaters.tasks.Task;
import net.runelite.api.*;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import net.runelite.api.Item;

import java.util.List;

import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static net.automaters.activities.skills.firemaking.DynamicFiremaking.firemaking;
import static net.automaters.activities.skills.woodcutting.Trees.*;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.items.SecondaryTools.getSecondaryTool;
import static net.automaters.api.ui.InventoryUtils.getAmountItemsNotInList;
import static net.automaters.api.utils.Calculator.random;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.goalFiremaking;
import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.goalFletching;
import static net.automaters.api.items.PrimaryTools.*;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.game.Skills.getLevel;

public class Woodcutting extends Task {

    private static TileObject resource;
    private static Area resourceLocation;
    private static boolean hasResources;
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
            while (scriptStarted && Bank.isOpen() && Inventory.contains(Predicates.nameContains(taskItems))) {
                Bank.depositAllExcept(Predicates.nameContains(taskItems));
                sleep(333);
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
        var axe = primaryTool;
        var logs = Inventory.getFirst(x -> x.getName().toLowerCase(Locale.ROOT).contains("logs"));
        var tinderbox = Inventory.getFirst("Tinderbox");
        var knife = Inventory.getFirst("Knife");
        var nest = TileItems.getNearest(x -> x.getName().toLowerCase(Locale.ROOT).contains(" nest"));

        if (axe != null) taskItems.add(axe);
        if (logs != null) taskItems.add(logs.getName());
        if (tinderbox != null) taskItems.add(tinderbox.getName());
        if (knife != null) taskItems.add(knife.getName());

        List<Item> inventoryItems = Inventory.getAll(); // Get your inventory items
        List<String> itemsToCheck = taskItems; // Define the list of item names to check

        if (getAmountItemsNotInList(inventoryItems, itemsToCheck) >= 5) {
            debug("Banking non-task items.");
            openBank();
            while (scriptStarted && Bank.isOpen() && Inventory.contains(Predicates.nameContains(taskItems))) {
                Bank.depositAllExcept(Predicates.nameContains(taskItems));
                sleep(333);
            }
        }

        if (!Inventory.contains(axe) && !Equipment.contains(axe)) {
            debug("no axe found");
            getPrimaryWoodcuttingTool();
            sleep(333);
            return;
        }

        if (nest != null) {
            nest.interact("Take");
            sleep(333);
            return;
        }

        if (resource == null || resourceLocation == null || logs == null && (!hasResources)) {
            getResources();
        }

        if (logs != null || secondaryTaskActive) {
            hasResources = false;
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
                    logs.drop();
                    sleep(333);
                    return;
                default:
                    logs.drop();
                    sleep(333);
                    secondaryTaskActive = false;
                    return;
            }
        }

        if (!Inventory.isFull()) {
            if (resource == null) {
                debug("Resource is null");
                resource = getTree();
            }
            interactableWalk(getTree(), "Chop down", resourceLocation);
            sleep(600);
        }

    }

    @Override
    public boolean hasNonTaskItems() {
        var axe = primaryTool;
        var logs = Inventory.getFirst(x -> x.getName().toLowerCase(Locale.ROOT).contains("logs"));
        var tinderbox = Inventory.getFirst("Tinderbox");
        var knife = Inventory.getFirst("Knife");

        if (axe != null) taskItems.add(axe);
        if (logs != null) taskItems.add(logs.getName());
        if (tinderbox != null) taskItems.add(tinderbox.getName());
        if (knife != null) taskItems.add(knife.getName());

        List<Item> inventoryItems = Inventory.getAll(); // Get your inventory items
        List<String> itemsToCheck = taskItems; // Define the list of item names to check
        List<Item> itemsNotInList = InventoryUtils.getItemsNotInList(inventoryItems, itemsToCheck);

        if (!itemsNotInList.isEmpty()) {
            return true;
        } else {
            System.out.println("All items in the inventory are in the list.");
            return false;
        }
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
            Item axe = Inventory.getFirst(Predicates.nameContains(" axe"));
            Item wornAxe = Equipment.getFirst(Predicates.nameContains(" axe"));
            if (axe != null) {
                debug("Inventory axe: "+axe.getName());
                primaryTool = axe.getName();
            } else if (wornAxe != null) {
                debug("Worn axe: "+wornAxe.getName());
                primaryTool = wornAxe.getName();
            } else {
                debug("Axe: null");
                primaryTool = null;
            }
        }
    }

    private void getResources() {
        firemaking = false;
//        fletching = false;
        secondaryTaskActive = false;
        hasResources = true;
        resource = Trees.getTree();
        resourceLocation = Trees.getTreeLocation();
        if (resource != null && resourceLocation != null) {
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
