package net.automaters.api.automate_utils;

import net.runelite.api.Item;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.items.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static net.automaters.api.utils.Debug.debug;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.items.Inventory.contains;

public class AutomateInventory {

    public List<Item> getItem(String partialTextItem) {
        return Inventory.getAll(x -> x.getName().toLowerCase(Locale.ROOT).contains(partialTextItem));
    }

    public static void dropAll(ArrayList<String> dropList) {
        if (Inventory.contains(Predicates.nameContains(dropList))) {
            for (String itemName : dropList) {
                Inventory.getAll(Predicates.nameContains(itemName)).forEach(item -> {
                    if (Inventory.contains(item.getName())) {
                        item.drop();
                        sleep(333);
                    }
                });
            }
        }
    }


    public boolean contains(String item) {
        // Implementation
        return true;
    }

    public static List<Item> getItemsFromList(boolean inList, List<String> itemsToCheck) {
        List<Item> itemsNotInList = new ArrayList<>();
        List<Item> itemsInList = new ArrayList<>();
        List<Item> inventory = Inventory.getAll();
        if (!inList) {
            for (Item item : inventory) {
                String itemName = item.getName();
                if (!itemsToCheck.contains(itemName)) {
                    itemsNotInList.add(item);
                }
            }
            debug("Items not in list: "+itemsNotInList);
            return itemsNotInList;
        } else {
            for (Item item : inventory) {
                String itemName = item.getName();
                if (itemsToCheck.contains(itemName)) {
                    itemsInList.add(item);
                }
            }
            debug("Items in list: "+itemsInList);
            return itemsInList;
        }
    }

    private static int getAmountList(boolean b, List<String> items) {
        List<Item> inventory = Inventory.getAll();
        int countItems = 0;

        if (!b) {
            for (Item item : inventory) {
                String itemName = item.getName();

                if (!items.contains(itemName)) {
                    debug("Item not in list: "+itemName);
                    countItems++;
                }
            }
        } else {
            for (Item item : inventory) {
                String itemName = item.getName();

                if (items.contains(itemName)) {
                    debug("Item in list: "+itemName);
                    countItems++;
                }
            }
        }
        return countItems;
    }

    public static int getAmount(boolean inList, List<String> itemsToCheck) {
        if (inList) {
            System.out.println("Amount of items in list: "+getAmountList(true, itemsToCheck));
        } else {
            System.out.println("Amount of items not in list: "+getAmountList(false, itemsToCheck));
        }
        return getAmountList(inList, itemsToCheck);
    }

}
