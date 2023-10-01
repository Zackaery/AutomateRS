package net.automaters.activities.skills.woodcutting;

import net.automaters.activities.skills.firemaking.DynamicFiremaking;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.tasks.Task;
import net.runelite.api.*;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import java.util.Locale;

import static net.automaters.activities.skills.woodcutting.Trees.getTree;
import static net.automaters.activities.skills.woodcutting.Trees.getTreeLocation;
import static net.automaters.api.items.SecondaryTools.getSecondaryTool;
import static net.automaters.api.utils.Calculator.random;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.goalFiremaking;
import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.goalFletching;
import static net.automaters.api.items.PrimaryTools.*;
import static net.unethicalite.api.game.Skills.getLevel;

public class Woodcutting extends Task {

    public static boolean lightLogs = false;

    public Woodcutting() {
        super();
    }

    @Override
    public void onStart() {
        if (primaryTool == null) {
            if (!Inventory.contains(Predicates.nameContains(" axe"))
                    && !Equipment.contains(Predicates.nameContains(" axe"))) {
                getPrimaryTool(WoodcuttingTools.class);
            } else {
                Item axe = Inventory.getFirst(Predicates.nameContains(" axe"));
                Item wornAxe = Equipment.getFirst(Predicates.nameContains(" axe"));
                if (axe != null) {
                    primaryTool = axe.getName();
                } else if (wornAxe != null) {
                    primaryTool = wornAxe.getName();
                } else {
                    primaryTool = null;
                }
            }
        } else if (skillTask == 0) {
            generateSecondaryTask();
            if (!secondaryTask.equals("none")) {
                getSecondaryTool(secondaryTask);
            }
        } else {
            setStarted(true);
            debug("Primary Tool: "+primaryTool);
            debug("Secondary Tool: "+secondaryTool);
            debug("Secondary Task: "+secondaryTask);
        }
    }
    @Override
    protected int onLoop() {
        var logs = Inventory.getFirst(x -> x.getName().toLowerCase(Locale.ROOT).contains("logs"));
        var tinderbox = Inventory.getFirst("Tinderbox");
        var knife = Inventory.getFirst("Knife");
        var nest = TileItems.getNearest(x -> x.getName().toLowerCase(Locale.ROOT).contains(" nest"));

        if (nest != null)
        {
            nest.interact("Take");
            return -1;
        }

        if (logs != null && LocalPlayer.canInteract()) {
            if (secondaryTask.equals("Firemaking")) {
                if (tinderbox != null && Inventory.isFull() || lightLogs) {
                    lightLogs = true;
                    new DynamicFiremaking();
                    return 500;
                }
            } else if (secondaryTask.equals("Fletching")) {
                if (knife != null) {
                    //execute Fletching.java task
                    return 500;
                }
            } else if (secondaryTask.equals("Bank")) {
                logs.drop();
                return 500;
            } else {
                logs.drop();
                return 500;
            }
        }
        if (!Inventory.isFull()) {
            interactableWalk(getTree(), "Chop down", getTreeLocation());
            return -1;
        }
        return 1000;
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



}
