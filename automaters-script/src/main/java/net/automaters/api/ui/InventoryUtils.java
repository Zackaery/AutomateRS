package net.automaters.api.ui;

import net.runelite.api.Item;

import java.util.ArrayList;
import java.util.List;

import static net.automaters.api.utils.Debug.debug;

public class InventoryUtils {

    public static List<Item> getItemsNotInList(List<Item> items, List<String> itemsToCheck) {
        List<Item> itemsNotInList = new ArrayList<>();

        for (Item item : items) {
            String itemName = item.getName();

            if (!itemsToCheck.contains(itemName)) {
                itemsNotInList.add(item);
            }
        }

        return itemsNotInList;
    }

    public static int getAmountItemsNotInList(List<Item> items, List<String> itemsToCheck) {
        int countItemsNotInList = 0;

        for (Item item : items) {
            String itemName = item.getName();

            if (!itemsToCheck.contains(itemName)) {
                countItemsNotInList++;
            }
        }

        return countItemsNotInList;
    }


}