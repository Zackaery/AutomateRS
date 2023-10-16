package net.automaters.tasks.utils;

import net.runelite.api.Item;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static net.automaters.api.automate_utils.AutomateInventory.getAmount;
import static net.automaters.api.automate_utils.AutomateInventory.getItemsFromList;
import static net.automaters.api.automate_utils.AutomateUtils.addItemsToList;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.Variables.*;
import static net.automaters.tasks.Task.*;

public class Setup {

    private static final List<String> tasks = Arrays.asList("null", "none", "bank");

    public static boolean setupPrimaryTool() {
        return primaryTool != null;
    }

    public static boolean setupSecondaryTool() {
        return tasks.contains(secondaryTask) || !tasks.contains(secondaryTask) && secondaryTool != null;
    }

    public static boolean setupSecondaryTask() {
        return !secondaryTask.equals("null");
    }

    public static void startTask() {
        if (Bank.isOpen()) {
            Bank.close();
        }
        debug("Primary Tool: " + primaryTool);
        debug("Secondary Tool: " + secondaryTool);
        debug("Secondary Task: " + secondaryTask);
    }

    public static boolean hasNonTaskItems() {
        if (!taskItems.contains(primaryTool)) {
            addItemsToList(taskItems, primaryTool);
        }

        if (!tasks.contains(secondaryTask)) {
            addItemsToList(taskItems, secondaryTool);
        }
        debug("Task Items: "+taskItems);
        return getAmount(false, taskItems) > 0;
    }

}
