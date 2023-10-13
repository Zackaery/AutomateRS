package net.automaters.activities.skills.mining;

import net.automaters.api.automate_utils.AutomateBanking;
import net.automaters.api.automate_utils.AutomatePlayer;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.entities.PlayerCrashInfo;
import net.automaters.api.automate_utils.AutomateInventory;
import net.automaters.tasks.Task;
import net.runelite.api.*;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import net.runelite.api.Item;
import net.unethicalite.api.movement.Reachable;

import java.util.*;

import static net.automaters.activities.skills.mining.Ores.*;
import static net.automaters.api.automate_utils.AutomateInventory.*;
import static net.automaters.api.automate_utils.AutomatePlayerCrashHandler.playerCrashing;
import static net.automaters.api.automate_utils.AutomateUtils.addItemsToList;
import static net.automaters.api.entities.LocalPlayer.*;
import static net.automaters.api.utils.Calculator.random;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.api.items.PrimaryTools.*;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.tasks.utils.Setup.*;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.commons.Time.sleepUntil;

public class Mining extends Task {

    static ArrayList<String> resources = new ArrayList<>();
    static ArrayList<String> taskItems = new ArrayList<>();

    static {
        addItemsToList(resources, " ore");
        addItemsToList(resources, "Uncut ");
        taskItems.addAll(resources);
    }

    public Mining() {
        super();
    }

    @Override
    public void onStart() {
        if (setupPrimaryTool()) {
            if (setupSecondaryTask()) {
                if (!hasNonTaskItems()) {
                    startTask();
                    setStarted(true);
                } else {
                    handleNonTaskItems();
                }
            } else {
                generateSecondaryTask();
            }
        } else {
            getPrimaryMiningTool();
        }
    }


    @Override
    public void onLoop() {
        addItemsToList(true, taskItems, resources);
        debug("task items list: "+taskItems);
        debug("resource items list: "+resources);
        if (AutomateInventory.getAmount(false, taskItems) >= 5) {
            handleNonTaskItems();
        }

        if (!AutomatePlayer.hasItems(primaryTool)) {
            getPrimaryMiningTool();
        }

        if (!hasResources || resourceObject == null) {
            getResources();
        }

        if (Inventory.isFull() || secondaryTaskActive) {
            handleInventory();
        }

        if (playerCrashing()) {
            handlePlayerCrashing();
        }

        if (!Inventory.isFull()) {
            debug("interactWithResource");
            interactWithResource();
        }
    }

    /*
     *
     * # onStart() methods below
     *
     */
    private void handleNonTaskItems() {
        if (!taskStarted()) {
            debug("bank all except primary tool");
            AutomateBanking.bankAllExcept(primaryTool);
        } else {
            debug("bank all except taskitems");
            AutomateBanking.bankAll(false, taskItems);
        }
    }


    /*
    *
    * # onLoop() methods below
    *
     */
    private void handleInventory() {
        switch (secondaryTask) {
            case "Bank":
                handleNonTaskItems();
                return;
            default:
                AutomateInventory.dropAll(resources);
                secondaryTaskActive = false;
                break;
        }
    }

    private void handlePlayerCrashing() {
        debug("handling crash");
        resourceObject = getFurtherOre();
        resourceObject.interact("Mine");
        sleepUntil(() -> LocalPlayer.canInteract() || !Reachable.isInteractable(resourceObject) || playerCrashing(), 5500);
    }

    private void interactWithResource() {
        debug("interact with resource");
        if (resourceObject != null && localPlayer.distanceTo(resourceObject) <= 10 && Reachable.isInteractable(resourceObject)) {
            debug("passed normal checks");
            if (LocalPlayer.canInteract()) {
                debug("can interact");
                resourceObject.interact("Mine");
                debug("Mining: " + resourceObject.getName());
                sleepUntil(() -> LocalPlayer.canInteract() || !Reachable.isInteractable(resourceObject) || playerCrashing(), 5500);
            }
            debug("after interact");
        } else {
            debug("automate walk");
            automateWalk(resourceLocation);
        }
    }

    /*
     *
     * # onEnd() methods below
     *
     */



    protected void onLop() {
        var tool = primaryTool;
        var resources = Inventory.getAll(x -> x.getName().toLowerCase(Locale.ROOT).contains("ore"));

        if (tool != null) {
            taskItems.add(tool);
        }

        if (resources != null) {
            for (Item resource : resources) {
                taskItems.add(resource.getName());
            }
        }

        List<Item> inventoryItems = Inventory.getAll();
        List<String> itemsToCheck = taskItems;

        if (getAmount(false, itemsToCheck) >= 5) {
            debug("Banking non-task items.");
            openBank();
            while (scriptStarted && Bank.isOpen() && Inventory.contains(Predicates.nameContains(taskItems))) {
                Bank.depositAllExcept(Predicates.nameContains(taskItems));
                sleep(333);
                debug("non task items still in inventory = " + taskItems);
            }
        }

        if (!Inventory.contains(tool) && !Equipment.contains(tool)) {
            debug("no tool found");
            getPrimaryMiningTool();
            sleep(333);
            return;
        }

        if (resourceObject == null || resourceLocation == null || resources == null && (!hasResources)) {
            getResources();
        }

        if (Inventory.isFull() && resources != null || secondaryTaskActive) {
            hasResources = false;
            switch (secondaryTask) {
                case "Bank":
                    if (Inventory.isFull() && !Bank.isOpen()) {
                        debug("in Inventory.isFull() && !Bank.isOpen()");
                        openBank();
                        if (Bank.isOpen()) {
                            Bank.depositAllExcept(Predicates.nameContains(taskItems));
                        }
                        sleep(333);
                    }
                    return;
                default:
                    List<String> itemsToDropNames = new ArrayList<>();
                    itemsToDropNames.add(" ore");
                    itemsToDropNames.add("Uncut ");

                    for (String itemName : itemsToDropNames) {
                        Inventory.getAll(Predicates.nameContains(itemName)).forEach(item -> {
                            if (Inventory.contains(item.getName())) {
                                item.drop();
                                sleep(333);
                            }
                        });
                    }

                    secondaryTaskActive = false;
                    break;
            }
        }


        if (!Inventory.isFull()) {
            int playerCount = 0;

            Player playerCrashing = Players.getNearest(x -> x.isAnimating()
                    && x.distanceTo(localPlayer) <= 1
                    && x != localPlayer);

            for (Player player : Players.getAll()) {
                if (player != localPlayer && player.distanceTo(localPlayer) <= 1) {
                    playerCount++;

                    // Check if this player has crashed you before
                    PlayerCrashInfo crashInfo = playerCrashInfo.get(player.getName());
                    if (crashInfo != null) {
                        if (playerCrashing != null && player.getName().equals(playerCrashing.getName())) {
                            // Increment the crash count for this player in the PlayerCrashInfo object
                            crashInfo.incrementCrashCount();

                            // Check the last crash time for this player
                            long currentTime = System.currentTimeMillis();
                            long lastCrashTime = crashInfo.getLastCrashTime();

                            if (currentTime - lastCrashTime <= 60000) { // Within 60 seconds
                                if (crashInfo.getCrashCount() >= 3) {
                                    System.out.println("Hopping to a new world... Players near you: " + playerCount);
                                    hopWorld(true);
                                }
                            } else {
                                // Reset the crash count and update the last crash time
                                crashInfo.resetCrashCount();
                            }

                            crashInfo.updateLastCrashTime();
                        }
                        System.out.println("Players near you: " + playerCount);
                        System.out.println(player.getName()+" crashed you "+ crashInfo.getCrashCount()+" times.");
                        System.out.println(player.getName()+" last crashed you "+ crashInfo.getLastCrashTimeSeconds()+" seconds ago.");
                    } else {
                        // Create a new PlayerCrashInfo object for this player
                        crashInfo = new PlayerCrashInfo();
                        crashInfo.incrementCrashCount();
                        crashInfo.updateLastCrashTime();
                        playerCrashInfo.put(player.getName(), crashInfo);
                    }
                }
            }





        }
    }

    @Override
    public boolean hasNonTaskItems() {
        if (!taskItems.contains(primaryTool)) {
            addItemsToList(taskItems, primaryTool);
        }
        debug("Task Items: "+taskItems);
        return getAmount(false, taskItems) > 0;
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
        debug("GETTTING RESOURCES ================");
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
        } else {
            debug("resourceObject == null");
//            debug("resource object: "+resourceObject.getName());
            debug("Ore: "+Ores.getOre());
            debug("Ore location: "+Ores.getOreLocation().toWorldArea());

        }
    }



}
