package net.automaters.api.items;

import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.ui.GrandExchange.automateBuy;
import static net.automaters.api.unobfuscated.GrandExchangePrices.canAfford;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.Variables.*;
import static net.unethicalite.api.commons.Time.sleep;

public class SecondaryTools {

    public static void getSecondaryTool(String secondaryTask) {
        switch (secondaryTask) {
            case "fletching":
                secondaryToolID = ItemID.KNIFE;
                secondaryTool = "Knife";
                processTool(secondaryToolID);
                break;
            case "firemaking":
                secondaryToolID = ItemID.TINDERBOX;
                secondaryTool = "Tinderbox";
                processTool(secondaryToolID);
                break;
        }
    }

    private static void processTool(int toolName) {
        debug("in processTool(int toolName)");
        while (scriptStarted && !hasTool(toolName)) {
            while (scriptStarted && !Bank.isOpen()) {
                openBank();
                sleep(600, 1200);
            }
            if (Bank.isOpen()) {
                if (Inventory.isFull()) {
                    Bank.depositAllExcept(Predicates.ids(primaryToolID, toolName));
                    sleep(333);
                    break;
                }
                if (!Bank.contains(toolName)) {
                    if (canAfford(toolName)) {
                        automateBuy(toolName, 1, 5);
                        sleep(600, 1200);
                    } else {
                        break;
                    }
                } else {
                    if (!Inventory.isFull()) {
                        Bank.withdraw(toolName, 1, Bank.WithdrawMode.ITEM);
                        sleep(600, 1200);
                    }
                }
            }
        }
    }

    private static boolean hasTool(int name) {
        return (Inventory.contains(name) || Equipment.contains(name));
    }
}
