package net.automaters.api.ui;

import net.runelite.api.ItemID;

import java.io.File;
import java.util.ArrayList;

import static net.automaters.api.entities.LocalPlayer.openGE;
import static net.automaters.api.items.Items.getAmountTotal;
import static net.automaters.api.items.Items.totalCoins;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.utils.GrandExchangePrices.getPrice;
import static net.automaters.api.utils.GrandExchangePrices.updatePrices;
import static net.automaters.util.file_managers.FileManager.PATH_GE_PRICES;
import static net.automaters.util.file_managers.FileManager.getLastModified;
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
    public static void buy(ArrayList<PurchaseItem> itemsToBuy) {
        if (getLastModified(new File(PATH_GE_PRICES), 30)) {
            updatePrices();
            debug("Updating GE Prices.");
        }
        for (PurchaseItem item : itemsToBuy) {
            int itemID = item.getItemID();
            int quantity = item.getQuantity();
            int multipliedValue = item.getMultipliedValue();

            if (totalCoins == -1) {
                getAmountTotal("Coins", true);
            } else if (getPrice(itemID).high * multipliedValue > totalCoins) {
                if (totalCoins >= getPrice(itemID).high) {
                    multipliedValue = totalCoins / getPrice(itemID).high;
                } else {
                    debug("Cannot buy the item: "+itemID);
                    failedPurchase = true;
                }
            } else if (!isOpen()) {
                openGE();
            }
        exchange(true, itemID, quantity, getPrice(itemID).high * multipliedValue);
        }
    }

    public static void buy(int itemID, int quantity, int multipliedValue) {
        if (getLastModified(new File(PATH_GE_PRICES), 30)) {
            updatePrices();
            debug("Updating GE Prices.");
        }
        if (totalCoins == -1) {
            getAmountTotal("Coins", true);
        } else if (getPrice(itemID).high * multipliedValue > totalCoins) {
            if (totalCoins >= getPrice(itemID).high) {
                multipliedValue = totalCoins / getPrice(itemID).high;
            } else {
                debug("Cannot buy the item: "+itemID);
                failedPurchase = true;
            }
        } else if (!isOpen()) {
            openGE();
        }
        exchange(true, itemID, quantity, getPrice(itemID).high * multipliedValue);
    }

}
