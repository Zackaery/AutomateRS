package net.automaters.activities.skills.mining;

import net.automaters.api.automate_utils.AutomateBanking;
import net.automaters.api.automate_utils.AutomatePlayer;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.automate_utils.AutomateInventory;
import net.automaters.api.walking.Area;
import net.automaters.script.Variables;
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
import static net.automaters.script.Variables.*;
import static net.automaters.tasks.utils.Setup.*;
import static net.unethicalite.api.commons.Time.sleepUntil;

public class Mining extends Task {

    private GameObject object;
    private Area location;
    private String action = "Mine";

    static ArrayList<String> resources;
    static ArrayList<String> taskItems = new ArrayList<>();

    static {
        resources = new ArrayList<>(Arrays.asList("Tin ore", "Copper ore", "Iron ore", "Coal ore", "Uncut sapphire", "Uncut ruby", "Uncut emerald"));
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
        addItemsToList(taskItems, primaryTool);

        if (AutomateInventory.getAmount(false, taskItems) >= 5) {
            handleNonTaskItems();
        }

        if (!AutomatePlayer.hasItems(primaryTool)) {
            getPrimaryMiningTool();
        }

        if (Inventory.isFull() || secondaryTaskActive) {
            handleInventory();
        }

        if (object == null && LocalPlayer.canInteract()) {
            getResources();
        }

        if (object != null && localPlayer.distanceTo(object) <= 12) {
            if (playerCrashing()) {
                handlePlayerCrashing();
            }
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
            getPrimaryTool(true, MiningTools.class);
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
        object = getFurtherOre();
        object.interact("Mine");
        sleepUntil(() -> localPlayer.distanceTo(object) <= 1, 5500);
    }

    private void getResources() {
        secondaryTaskActive = false;
        object = Ores.getOre();
        location = Ores.getOreLocation();
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
        if (object != null
                && localPlayer.distanceTo(object) <= 12
                && Reachable.isInteractable(object)) {

            debug("Mining: " + object.getName());
            object.interact(action);
            sleepUntil(() -> !LocalPlayer.canInteract(), 1800);
            sleepUntil(() -> LocalPlayer.canInteract() || !Reachable.isInteractable(object) || playerCrashing(), 5500);

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
