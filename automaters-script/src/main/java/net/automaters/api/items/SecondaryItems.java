package net.automaters.api.items;

import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;

import java.util.List;

import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.ui.GrandExchange.automateBuy;
import static net.automaters.api.unobfuscated.GrandExchangePrices.canAfford;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.Variables.scriptStarted;
import static net.automaters.script.Variables.secondaryItem;
import static net.unethicalite.api.commons.Time.sleep;

public class SecondaryItems {

    public static Boolean hasSecondaryItem(int itemID, Integer minAmount, Boolean withdrawAll) {
        while (scriptStarted) {
            if (itemID == -1) {
                secondaryItem = "";
                return true;
            }
            if (Inventory.getCount(true, itemID) >= minAmount) {
                Item item = Inventory.getFirst(itemID);
                secondaryItem = item.getName();
                return true;
            }

            if (!Bank.isOpen()) {
                openBank();
            } else {
                if (Bank.getCount(true, itemID) < minAmount) {
                    return false;
                } else {
                    if (withdrawAll) {
                        Bank.withdrawAll(itemID, Bank.WithdrawMode.ITEM);
                    } else {
                        Bank.withdraw(itemID, minAmount, Bank.WithdrawMode.ITEM);
                    }
                }
            }
        }
        return true;
    }

    public static void buySecondaryItem(int itemID, Integer maxAmount, int multipliedValue) {
        debug("buySecondaryItem");
        if (canAfford(itemID) && !Inventory.contains(itemID)) {
            debug("Attempting to buy tool: " + itemID);
            automateBuy(itemID, maxAmount, multipliedValue);
            sleep(600, 1200);
        }
    }


}
