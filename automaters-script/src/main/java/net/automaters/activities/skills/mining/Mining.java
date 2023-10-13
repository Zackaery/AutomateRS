package net.automaters.activities.skills.mining;

import net.automaters.api.automate_utils.AutomateBanking;
import net.automaters.api.automate_utils.AutomatePlayer;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.automate_utils.AutomateInventory;
import net.automaters.api.walking.Area;
import net.automaters.tasks.Task;
import net.runelite.api.*;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import net.runelite.api.Item;
import net.unethicalite.api.movement.Reachable;

import java.util.*;

import static net.automaters.activities.skills.mining.Ores.*;
import static net.automaters.api.automate_utils.AutomatePlayerCrashHandler.playerCrashing;
import static net.automaters.api.automate_utils.AutomateUtils.addItemsToList;
import static net.automaters.api.entities.LocalPlayer.*;
import static net.automaters.api.utils.Calculator.random;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.api.items.PrimaryTools.*;
import static net.automaters.tasks.utils.Setup.*;
import static net.unethicalite.api.commons.Time.sleepUntil;

public class Mining extends Task {

    private GameObject resourceObject;
    private Area resourceLocation;

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
        addItemsToList(taskItems, primaryTool);
        addItemsToList(true, taskItems, resources);

        if (AutomateInventory.getAmount(false, taskItems) >= 5) {
            handleNonTaskItems();
        }

        if (!AutomatePlayer.hasItems(primaryTool)) {
            getPrimaryMiningTool();
        }

        if (Inventory.isFull() || secondaryTaskActive) {
            handleInventory();
        }

        if (resourceObject == null && LocalPlayer.canInteract()) {
            getResources();
        }

        if (playerCrashing()) {
            handlePlayerCrashing();
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

    @Override
    public void generateSecondaryTask() {
        skillTask = random(1, 100);
        secondaryTask = "none";
    }

    private void handleNonTaskItems() {
        if (!isStarted()) {
            AutomateBanking.bankAllExcept(primaryTool);
        } else {
            AutomateBanking.bankAll(false, taskItems);
        }
    }



    /**
     * # onLoop() methods below
     */

    private void handleInventory() {
        switch (secondaryTask) {
            case "bank":
                handleNonTaskItems();
                return;
            default:
                AutomateInventory.dropAll(resources);
                secondaryTaskActive = false;
                break;
        }
    }

    private void handlePlayerCrashing() {
        debug("[HANDLE PLAYER CRASHING] - Handling crash");
        resourceObject = getFurtherOre();
        resourceObject.interact("Mine");
        sleepUntil(() -> LocalPlayer.canInteract() || !Reachable.isInteractable(resourceObject) || playerCrashing(), 5500);
    }

    private void getResources() {
        secondaryTaskActive = false;
        resourceObject = Ores.getOre();
        resourceLocation = Ores.getOreLocation();
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
        if (resourceObject != null
                && localPlayer.distanceTo(resourceObject) <= 12
                && Reachable.isInteractable(resourceObject)) {

            debug("Mining: " + resourceObject.getName());
            resourceObject.interact("Mine");
            sleepUntil(() -> !LocalPlayer.canInteract(), 1800);
            sleepUntil(() -> LocalPlayer.canInteract() || !Reachable.isInteractable(resourceObject) || playerCrashing(), 5500);

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
