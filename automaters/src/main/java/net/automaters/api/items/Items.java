package net.automaters.api.items;

import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;

import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.utils.Debug.debug;

public class Items {

    public static int totalCoins;

    public static int getAmountBank(String name) {
        while (!Bank.isOpen()) {
            openBank();
        }
        return Bank.getCount(true, name);
    }
    public static int getAmountInventory(String name) {
        if (!Inventory.contains(name)) {
            return 0;
        }
        return Inventory.getCount(true, name);
    }

    public static int getAmountTotal(String name, Boolean checkBank){
        if (checkBank) {
            totalCoins = getAmountBank("Coins") + getAmountInventory("Coins");
            return getAmountBank(name) + getAmountInventory(name);
        } else {
            totalCoins = getAmountInventory("Coins");
            return getAmountInventory(name);
        }
    }
}
