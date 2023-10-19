package net.automaters.activities.skills.fishing;

import net.automaters.api.automate_utils.AutomateBanking;
import net.automaters.api.automate_utils.AutomateInventory;
import net.automaters.api.automate_utils.AutomatePlayer;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.entities.SkillCheck;
import net.automaters.api.items.SecondaryItems;
import net.automaters.api.walking.Area;
import net.automaters.script.Variables;
import net.automaters.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.automaters.activities.skills.fishing.Fish.chooseFishType;
import static net.automaters.activities.skills.fishing.Fish.getSecondaryItem;
import static net.automaters.api.automate_utils.AutomateUtils.addItemsToList;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.items.PrimaryTools.FishingTools;
import static net.automaters.api.items.PrimaryTools.getPrimaryTool;
import static net.automaters.api.items.SecondaryItems.buySecondaryItem;
import static net.automaters.api.items.SecondaryItems.hasSecondaryItem;
import static net.automaters.api.utils.Calculator.random;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.script.Variables.*;
import static net.automaters.tasks.utils.Setup.*;
import static net.unethicalite.api.commons.Time.sleepUntil;

public class Fishing extends Task {

    private NPC object;
    private Area location;
    private String action;

    static ArrayList<String> resources;
    static ArrayList<String> taskItems = new ArrayList<>();

    static {
        resources = new ArrayList<>(Arrays.asList("Raw shrimps", "Raw anchovies", "Raw guppy", "Raw cavefish", "Raw tetra", "Raw sardine", "Raw herring", "Raw pike", "Raw trout", "Raw salmon", "Raw lobster", "Raw tuna", "Raw swordfish"));
    }

    public Fishing() {
        super();
    }

    @Override
    public void onStart() {
        if (setupPrimaryTool()) {
            if (hasSecondaryItem(Fish.getSecondaryItem(), 100, true)) {
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
                buySecondaryItem(Fish.getSecondaryItem(), 500, 3);
            }
        } else {
            getPrimaryFishingTool();
        }
    }


    @Override
    public void onLoop() {
        addItemsToList(true, taskItems, resources);
        addItemsToList(taskItems, primaryTool);
        addItemsToList(taskItems, secondaryItem);

        if (AutomateInventory.getAmount(false, taskItems) >= 5) {
            handleNonTaskItems();
        }

        if (!AutomatePlayer.hasItems(primaryTool)) {
            getPrimaryFishingTool();
        }

        if (Inventory.isFull() || secondaryTaskActive) {
            handleInventory();
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

    private void getPrimaryFishingTool() {
        primaryTool = null;
        primaryToolID = -1;
        Fish.FishType chosenFishType = chooseFishType();
        if (chosenFishType == null) {
            debug("Could not find a suitable fish type.");
            return;
        }

        int levelReq = chosenFishType.getReqFishingLevel();
        String tool;

        String[] toolNames = {"Lobster pot", "Fly fishing rod", "Fishing rod", "Small net"};
        if (levelReq == 40) {
            tool = toolNames[0];
        } else if (levelReq == 20) {
            tool = toolNames[1];
        } else if (levelReq == 10) {
            tool = toolNames[2];
        } else {
            tool = toolNames[3];
        }

        if (Inventory.contains(Predicates.nameContains(tool))) {
            Item toolItem = Inventory.getFirst(tool);
            debug("Inventory tool: " + toolItem.getName());
            primaryTool = toolItem.getName();
            return;
        }

        getPrimaryTool(false, FishingTools.class);
    }

    @Override
    public void generateSecondaryTask() {
        skillTask = random(1, 100);
        secondaryTask = "none";
    }

    private void handleNonTaskItems() {
        if (!isStarted()) {
            List<String> items = Arrays.asList(primaryTool, secondaryItem);
            AutomateBanking.bankAllExcept(false, items);
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

    private void getResources() {
        secondaryTaskActive = false;
        object = Fish.getFish();
        location = Fish.getFishLocation();
        action = Fish.getAction();
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
        if (secondaryItem != null) {
            if (!Inventory.contains(secondaryItem)) {
                if (!hasSecondaryItem(getSecondaryItem(), 100, true)) {
                    buySecondaryItem(getSecondaryItem(), 1000, 3);
                }
            }
        }

        if (object != null
                && localPlayer.distanceTo(object) <= 12
                && Reachable.isInteractable(object)) {

            object.interact(action);
            debug(action+ " Fishing: " + object.getName());
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
                sleepUntil(LocalPlayer::canInteract, 1200);
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
