package net.automaters.tasks;

import net.automaters.api.ui.GrandExchange;
import net.automaters.api.walking.Area;
import net.runelite.api.ItemID;
import net.unethicalite.api.plugins.LoopedPlugin;

import java.util.ArrayList;

import static net.automaters.api.entities.LocalPlayer.*;
import static net.automaters.api.ui.GrandExchange.automateBuy;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.AutomateRS.scriptStarted;


public class Test extends LoopedPlugin{

    private static final Area TEST_AREA = new Area(3208, 3243, 3222, 3246, 0);

    static boolean buyAxe = true;

    @Override
    protected int loop() {


        if ((localPlayer == null) || !scriptStarted) {
            debug("Player does not exist.");
            return -1;
        }


        ArrayList<GrandExchange.PurchaseItem> itemsToBuy = new ArrayList<>();
        itemsToBuy.add(new GrandExchange.PurchaseItem(ItemID.STEEL_AXE, 1, 20));
        itemsToBuy.add(new GrandExchange.PurchaseItem(ItemID.MITHRIL_AXE, 1, 20));
        itemsToBuy.add(new GrandExchange.PurchaseItem(ItemID.ADAMANT_AXE, 1, 20));
        itemsToBuy.add(new GrandExchange.PurchaseItem(ItemID.RUNE_AXE, 1, 20));
        automateBuy(itemsToBuy);




        automateBuy(ItemID.STEEL_AXE, 1, 20);


       return 50;
    }


}
