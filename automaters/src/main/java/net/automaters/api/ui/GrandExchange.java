package net.automaters.api.ui;

import net.runelite.api.ItemID;
import net.unethicalite.api.items.Inventory;

import java.io.File;
import java.util.ArrayList;

import static net.automaters.api.entities.LocalPlayer.openGE;
import static net.automaters.api.items.Items.getAmountTotal;
import static net.automaters.api.items.Items.totalCoins;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.utils.GrandExchangePrices.getPrice;
import static net.automaters.api.utils.GrandExchangePrices.updatePrices;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.util.file_managers.FileManager.FILE_GE_PRICES;
import static net.automaters.util.file_managers.FileManager.getLastModified;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.items.GrandExchange.exchange;
import static net.unethicalite.api.items.GrandExchange.isOpen;

public class GrandExchange {

    public static class PurchaseItem {
        private int itemID;
        private int quantity;
        private int multipliedValue;

        public PurchaseItem(int itemID, int quantity, int multipliedValue) {
            this.itemID = itemID;
            this.quantity = quantity;
            this.multipliedValue = (multipliedValue == 0) ? 1 : multipliedValue;
        }
        public int getItemID() {
            return itemID;
        }
        public int getQuantity() {
            return quantity;
        }
        public int getMultipliedValue() {
            return multipliedValue;
        }
    }

    public static boolean failedPurchase;
    public static void automateBuy(ArrayList<PurchaseItem> itemsToBuy) {
        boolean boughtItem = false;
        if (getLastModified(new File(FILE_GE_PRICES), 30)) {
            updatePrices();
            debug("Updating GE Prices.");
        }
        for (PurchaseItem item : itemsToBuy) {
            int itemID = item.getItemID();
            int quantity = item.getQuantity();
            int multipliedValue = item.getMultipliedValue();

            while (scriptStarted && !failedPurchase && !boughtItem) {
                if (totalCoins == -1) {
                    debug("Checking total coins amount");
                    getAmountTotal("Coins", true);
                } else if (getPrice(itemID).high * multipliedValue > totalCoins) {
                    debug("Price to buy at is > total coins.");
                    if (totalCoins >= getPrice(itemID).high) {
                        debug("New price to buy is: " + (totalCoins / getPrice(itemID).high));
                        multipliedValue = totalCoins / getPrice(itemID).high;
                    } else {
                        debug("Cannot buy the item: " + itemID);
                        failedPurchase = true;
                    }
                } else if (!isOpen()) {
                    openGE();
                    debug("Opened GE");
                } else {
                    debug("Buying " + quantity + "x " + itemID + " at " + getPrice(itemID).high * multipliedValue);
                    exchange(true, itemID, quantity, getPrice(itemID).high * multipliedValue);
                }
                if (Inventory.contains(itemID)) {
                    boughtItem = true;
                }
            }
        }
    }

    public static void automateBuy(int itemID, int quantity, int multipliedValue) {
        boolean boughtItem = false;
        if (getLastModified(new File(FILE_GE_PRICES), 30)) {
            updatePrices();
            debug("Updating GE Prices.");
        }
        while (scriptStarted && !failedPurchase && !boughtItem) {
            if (totalCoins == -1) {
                debug("Checking total coins amount");
                getAmountTotal("Coins", true);
            } else if (getPrice(itemID).high * multipliedValue > totalCoins) {
                debug("Price to buy at is > total coins.");
                if (totalCoins >= getPrice(itemID).high) {
                    debug("New price to buy is: "+ (totalCoins / getPrice(itemID).high));
                    multipliedValue = totalCoins / getPrice(itemID).high;
                } else {
                    debug("Cannot buy the item: "+itemID);
                    failedPurchase = true;
                }
            } else if (!isOpen()) {
                openGE();
                debug("Opened GE");
            } else {
                while (scriptStarted && !Inventory.contains(itemID)) {
                    debug("Buying " + quantity + "x " + itemID + " at " + getPrice(itemID).high * multipliedValue);
                    sleep(1800);
                    exchange(true, itemID, quantity, getPrice(itemID).high * multipliedValue, true, false);
                    sleep(1200);
                }
            }
            if (Inventory.contains(itemID)) {
                net.unethicalite.api.items.GrandExchange.close();
                boughtItem = true;
            }
        }
    }

}
