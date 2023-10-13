package net.automaters.api.automate_utils;

import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;

import java.util.*;

import static net.automaters.api.automate_utils.AutomateUtils.addItemsToList;
import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.unethicalite.api.commons.Time.sleep;

public class AutomateBanking {

//    public static void bankAllExcept(String... items) {
//        if (Bank.isOpen()) {
//            if (Inventory.contains)
//        }
//    }
//


    private static boolean isOpen() {
        return Bank.isOpen();
    }

    public static void bankAllExcept(String item) {
        if (isOpen()) {
            Inventory.getAll().forEach(itemName -> {
                while (scriptStarted && Inventory.contains(itemName.getName()) && !itemName.getName().equals(item)) {
                    sleep(333);
                    Bank.depositAllExcept(item);
                    sleep(333, 600);
                }
            });
        } else {
            openBank();
        }
    }

    public static void bankAllExcept(boolean inList, List<String> listItems) {
        ArrayList<String> nonListItems = new ArrayList<>();
        addItemsToList(!inList, nonListItems, listItems);
        if (!nonListItems.isEmpty()) {
            if (isOpen()) {
                Bank.depositAllExcept(Predicates.nameContains(nonListItems));
            } else {
                openBank();
            }
        }
    }

    public static void bankAll(String item) {
        ArrayList<String> nonListItems = new ArrayList<>();
        addItemsToList(nonListItems, item);
        if (!nonListItems.isEmpty()) {
            if (isOpen()) {
                Bank.depositAll(Predicates.nameContains(nonListItems));
            } else {
                openBank();
            }
        }
    }

    public static void bankAll(boolean inList, List<String> listItems) {
        ArrayList<String> nonListItems = new ArrayList<>();
        addItemsToList(inList, nonListItems, listItems);
        if (!nonListItems.isEmpty()) {
            if (isOpen()) {
                Bank.depositAll(Predicates.nameContains(nonListItems));
            } else {
                openBank();
            }
        }
    }


//        List<Item> itemsNotInList = new ArrayList<>();
//        for (Item item : items) {
//            String itemName = item.getName();
//
//            if (!itemsToCheck.contains(itemName)) {
//                itemsNotInList.add(item);
//            }
//        }
//        if (Bank.isOpen()) {
//            if (Inventory.contains)
//        }

//
//    public static List<Item> bankItems(String... items) {
//        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains(List.of(items)))) {
//            for (Item item : items) {
//
//            }
//        }
//    }
//
//
//    private static boolean isOpen() {
//
//    }
//
//
////    public static void bankItems(Item... items) {
////
////    }
//    if (getAmountItemsNotInList(inventoryItems, itemsToCheck) >= 5) {
//        debug("Banking non-task items.");
//        openBank();
//        while (scriptStarted && Bank.isOpen() && Inventory.contains(Predicates.nameContains(taskItems))) {
//            Bank.depositAllExcept(Predicates.nameContains(taskItems));
//            sleep(333);
//            debug("non task items still in inventory = " + taskItems);
//        }
//    }
}
