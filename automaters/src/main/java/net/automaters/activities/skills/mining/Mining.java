package net.automaters.activities.skills.mining;

import net.automaters.activities.skills.firemaking.DynamicFiremaking;
import net.automaters.api.ui.InventoryUtils;
import net.automaters.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.Skill;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import java.util.List;
import java.util.Locale;

import static net.automaters.activities.skills.mining.Ores.getOre;
import static net.automaters.activities.skills.mining.Ores.getOreLocation;
import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.items.PrimaryTools.MiningTools;
import static net.automaters.api.items.PrimaryTools.getPrimaryTool;
import static net.automaters.api.ui.InventoryUtils.getAmountItemsNotInList;
import static net.automaters.api.utils.Calculator.random;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.unethicalite.api.commons.Time.sleep;

public class Mining extends Task {

    public Mining() {
        super();
    }

    @Override
    public void onStart() {
        if (primaryTool == null) {
            getPrimaryMiningTool();
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
        var tool = primaryTool;
        var ore = Inventory.getFirst(x -> x.getName().toLowerCase(Locale.ROOT).contains("ore"));
        var coal = Inventory.getFirst("Coal");

        if (tool != null) taskItems.add(tool);
        if (ore != null) taskItems.add(ore.getName());
        if (coal != null) taskItems.add(coal.getName());

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

        if (!Inventory.contains(tool) && !Equipment.contains(tool)) {
            debug("no tool found");
            getPrimaryMiningTool();
            sleep(600);
            return;
        }

        if (ore == null) {
            secondaryTaskActive = false;
        }

        if (ore != null || secondaryTaskActive) {
            if (secondaryTask.equals("Bank")) {
                ore.drop();
                sleep(333);
                return;
            } else {
                ore.drop();
                sleep(333);
                return;
            }
        } else {
            secondaryTaskActive = false;
        }
        if (!Inventory.isFull()) {
            interactableWalk(getOre(), "Mine", getOreLocation());
            sleep(600);
        }

    }

    @Override
    public boolean hasNonTaskItems() {
        var tool = primaryTool;
        var extraItems = Inventory.getAll("Pay-dirt");

        if (tool != null) taskItems.add(tool);
        if (extraItems != null) taskItems.add(extraItems.toString());

        List<Item> inventoryItems = Inventory.getAll(); // Get your inventory items
        List<String> itemsToCheck = taskItems; // Define the list of item names to check
        List<Item> itemsNotInList = InventoryUtils.getItemsNotInList(inventoryItems, itemsToCheck);

        if (!itemsNotInList.isEmpty()) {
            System.out.println("Items not in the list: ");
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
                debug("Inventory pickaxe: "+tool.getName());
                primaryTool = tool.getName();
            } else if (wornTool != null) {
                debug("Worn pickaxe: "+wornTool.getName());
                primaryTool = wornTool.getName();
            } else {
                debug("Pickaxe: null");
                primaryTool = null;
            }
        }
    }



}
