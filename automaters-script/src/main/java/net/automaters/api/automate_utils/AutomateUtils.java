package net.automaters.api.automate_utils;

import net.runelite.api.Item;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.items.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static net.automaters.api.utils.Debug.debug;

public class AutomateUtils {

    public static void addItemsToList(ArrayList<String> list, String nameContains) {
        Inventory.getAll(Predicates.nameContains(nameContains)).forEach(item -> {
            if (Inventory.contains(item.getName()) && !list.contains(item.getName())) {
                list.add(item.getName());
            }
        });
    }

    public static void addItemsToList(boolean inList, ArrayList<String> list, List<String> items) {
        for (Item item : Inventory.getAll()) {
            String itemName = item.getName();
            boolean isInItemList = items.contains(itemName);
            boolean isNotInList = !items.contains(itemName);

            if (!list.contains(itemName)) {
                if ((inList && isInItemList) || (!inList && isNotInList)) {
                    list.add(itemName);
                }
            }
        }
    }
}
