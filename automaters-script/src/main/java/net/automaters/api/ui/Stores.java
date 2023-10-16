package net.automaters.api.ui;

import net.runelite.api.NPC;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.widgets.Widgets;

import java.util.function.Supplier;

public class Stores {

    private static final Supplier<Widget> SHOP = () -> Widgets.get(300, 0);
    private static final Supplier<Widget> SHOP_ITEMS = () -> Widgets.get(300, 16);
    private static final Supplier<Widget> INVENTORY = () -> Widgets.get(301, 0);

    public static class PurchaseItem {
        private int itemID;
        private int quantity;

        public PurchaseItem(int itemID, int quantity) {
            this.itemID = itemID;
            this.quantity = quantity;
        }
        public int getItemID() {
            return itemID;
        }
        public int getQuantity() {
            return quantity;
        }
    }

    public static boolean isOpen() { return Widgets.isVisible(SHOP.get()); }

    public static void open() {
        NPC npc = NPCs.getNearest(x -> x.getName() != null && x.hasAction("Trade"));
        if (!isOpen()) {
            if (npc != null) {
                npc.interact("Trade");
            }
        }
    }

    public static void buy(int itemID, int quantity) {

    }


}
