package net.automaters.api.entities;

import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
@SuppressWarnings({"unused"})
public class itemCheck {

    public static boolean noItem(String itemName) {
        return !Bank.contains(itemName) && !Inventory.contains(itemName);
    }

    public static boolean inBank(String itemName) {
        return Bank.contains(itemName) && !Inventory.contains(itemName);
    }

    public static boolean inInv(String itemName) {
        return !Bank.contains(itemName) && Inventory.contains(itemName);
    }

    public static boolean notEquipped(String itemName) {
        return Inventory.contains(itemName) && !Equipment.contains(itemName);
    }

    public static boolean isEquipped(String itemName) {
        return !Inventory.contains(itemName) && Equipment.contains(itemName);
    }

}
