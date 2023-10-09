package net.automaters.api.items;

import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;

import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.ui.GrandExchange.automateBuy;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.unethicalite.api.commons.Time.sleep;

public class Items {

    private static PrimaryTools primaryTools = new PrimaryTools();

    public static int totalCoins = -1;

    public static int getAmountBank(String name) {
        while (scriptStarted && !Bank.isOpen()) {
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
