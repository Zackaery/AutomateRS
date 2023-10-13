package net.automaters.api.automate_utils;

import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import java.util.Arrays;

import static net.automaters.api.utils.Debug.debug;

public class AutomatePlayer {

    public static boolean hasItems(String... items) {
        return Inventory.contains(items) || Equipment.contains(items);
    }

    public static boolean hasItems(int... ids) {
        return (!Inventory.contains(ids) && !Equipment.contains(ids));
    }

}
