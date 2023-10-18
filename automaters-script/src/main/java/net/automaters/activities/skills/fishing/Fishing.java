package net.automaters.activities.skills.fishing;

import net.automaters.api.automate_utils.AutomateBanking;
import net.automaters.api.automate_utils.AutomateInventory;
import net.automaters.api.automate_utils.AutomatePlayer;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.entities.SkillCheck;
import net.automaters.api.walking.Area;
import net.automaters.tasks.Task;
import net.runelite.api.GameObject;
import net.runelite.api.Item;
import net.runelite.api.Skill;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;

import java.util.ArrayList;
import java.util.Arrays;

import static net.automaters.activities.skills.fishing.Fish.*;
import static net.automaters.api.automate_utils.AutomatePlayerCrashHandler.playerCrashing;
import static net.automaters.api.automate_utils.AutomateUtils.addItemsToList;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.items.PrimaryTools.FishingTools;
import static net.automaters.api.items.PrimaryTools.getPrimaryTool;
import static net.automaters.api.utils.Calculator.random;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.script.Variables.*;
import static net.automaters.tasks.utils.Setup.*;
import static net.unethicalite.api.commons.Time.sleepUntil;

public class Fishing extends Task {

    private GameObject resourceObject;
    private Area resourceLocation;

    static ArrayList<String> resources;
    static ArrayList<String> taskItems = new ArrayList<>();

    static {
        resources = new ArrayList<>(Arrays.asList("Raw shrimp", "Raw anchovies", "Raw Lobster", "Raw guppy", "Raw cavefish", "Raw tetra", "Raw sardine", "Raw herring", "Raw pike", "Raw trout", "Raw salmon", "Raw tuna", "Raw swordfish", "Raw lobster"));
    }

    public Fishing() {
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
            getPrimaryFishingTool();
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
            getPrimaryFishingTool();
        }

        if (Inventory.isFull() || secondaryTaskActive) {
            handleInventory();
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

    private void getPrimaryFishingTool() {
        // Lobster
        if (SkillCheck.getFishingLevel() >= 40 ) {
            if (!Inventory.contains(Predicates.nameContains("Lobster"))) {
                debug("getPrimaryTool");
                primaryTool = null;
                primaryToolID = -1;
                getPrimaryTool(FishingTools.class);
            } else {
                Item tool = Inventory.getFirst(Predicates.nameContains("Lobster"));
                if (tool != null) {
                    debug("Inventory tool: " + tool.getName());
                    primaryTool = tool.getName();
                } else {
                    primaryTool = null;
                }
            }
        }
        // Fly Fish
        if (SkillCheck.getFishingLevel() >= 20 && SkillCheck.getFishingLevel() < 40 ) {
            if (!Inventory.contains(Predicates.nameContains("Fly"))) {
                debug("getPrimaryTool");
                primaryTool = null;
                primaryToolID = -1;
                getPrimaryTool(FishingTools.class);
            } else {
                Item tool = Inventory.getFirst(Predicates.nameContains("Fly"));
                if (tool != null) {
                    debug("Inventory tool: " + tool.getName());
                    primaryTool = tool.getName();
                } else {
                    primaryTool = null;
                }
            }
        }
        // Fishing rod
        if (SkillCheck.getFishingLevel() >= 10 && SkillCheck.getFishingLevel() < 20 ) {
            if (!Inventory.contains("Fishing rod")) {
                debug("getPrimaryTool");
                primaryTool = null;
                primaryToolID = -1;
                getPrimaryTool(FishingTools.class);
            } else {
                Item tool = Inventory.getFirst("Fishing rod");
                if (tool != null) {
                    debug("Inventory tool: " + tool.getName());
                    primaryTool = tool.getName();
                } else {
                    primaryTool = null;
                }
            }
        }
        // Small net
        if (SkillCheck.getFishingLevel() >= 1 && SkillCheck.getFishingLevel() < 10 ) {
            if (!Inventory.contains("Small net")) {
                debug("getPrimaryTool");
                primaryTool = null;
                primaryToolID = -1;
                getPrimaryTool(FishingTools.class);
            } else {
                Item tool = Inventory.getFirst(Predicates.nameContains("Small net"));
                if (tool != null) {
                    debug("Inventory tool: " + tool.getName());
                    primaryTool = tool.getName();
                } else {
                    primaryTool = null;
                }
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

    private void getResources() {
        secondaryTaskActive = false;
        resourceObject = Fish.getFish();
        resourceLocation = Fish.getFishLocation();
        if (resourceObject != null
                && resourceLocation != null) {
            String l = String.format("%d, %d, %d, %d, %d",
                    resourceLocation.minX,
                    resourceLocation.minY,
                    resourceLocation.maxX,
                    resourceLocation.maxY,
                    resourceLocation.thisZ);
            debug("Resource Name: " + resourceObject.getName());
            debug("Resource Location: " + l);
        }
    }

    private void interactWithResource() {
        // small net
        if (resourceObject.getName() == "Fishing spot") {
            if (resourceObject != null
                    && localPlayer.distanceTo(resourceObject) <= 12
                    && Reachable.isInteractable(resourceObject)) {

                debug("Fishing: " + resourceObject.getName());
                resourceObject.interact("Small Net");
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
        // Fly Fishing
        if (resourceObject.getName() == "Rod Fishing spot" && Inventory.contains("Fly fishing rod")) {
            if (resourceObject != null
                    && localPlayer.distanceTo(resourceObject) <= 12
                    && Reachable.isInteractable(resourceObject)) {

                debug("Fishing: " + resourceObject.getName());
                resourceObject.interact("Lure");
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
        // Lobster
        if (resourceObject.getName() == "Fishing spot" && Inventory.contains("Lobster pot")){
            if (resourceObject != null
                    && localPlayer.distanceTo(resourceObject) <= 12
                    && Reachable.isInteractable(resourceObject)) {

                debug("Fishing: " + resourceObject.getName());
                resourceObject.interact("Cage");
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
