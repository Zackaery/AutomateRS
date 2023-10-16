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
                debug("adding item: "+item.getName());
                list.add(item.getName());
            }
        });
    }

    public static void addItemsToList(ArrayList<String> list, Integer id) {
        Inventory.getAll(Predicates.ids(id)).forEach(item -> {
            if (Inventory.contains(item.getName()) && !list.contains(item.getName())) {
                debug("adding item: "+item.getName());
                list.add(item.getName());
            }
        });
    }

    public static void addItemsToList(boolean inList, ArrayList<String> list, List<String> items) {

        for (Item item : Inventory.getAll()) {
            String itemName = item.getName();
            boolean isInItemList = items.contains(itemName);
            boolean isNotInList = !list.contains(itemName);

            if (!list.contains(itemName)) {
                if ((inList && isInItemList && isNotInList) || (!inList && !isInItemList && isNotInList)) {
                    addItemsToList(list, itemName);
                }
            }
        }
    }


    public static void addIdsToList(ArrayList<Integer> list, Integer id) {
        Inventory.getAll(Predicates.ids(id)).forEach(item -> {
            if (Inventory.contains(item.getId()) && !list.contains(item.getId())) {
                debug("adding item: "+item.getId());
                list.add(item.getId());
            }
        });
    }

    public static void addIdsToList(boolean inList, ArrayList<Integer> list, List<Integer> items) {
        for (Item item : Inventory.getAll()) {
            Integer itemId = item.getId();
            boolean isInItemList = items.contains(itemId);
            boolean isNotInList = !items.contains(itemId);

            if (!list.contains(itemId)) {
                if ((inList && isInItemList) || (!inList && isNotInList)) {
                    addIdsToList(list, itemId);
                }
            }
        }
    }
}
