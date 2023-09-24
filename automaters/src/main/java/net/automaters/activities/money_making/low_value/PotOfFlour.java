package net.automaters.activities.money_making.low_value;

import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.NPCs;
import net.runelite.api.NPC;
import net.automaters.tasks.Task;
import net.runelite.api.World;
import net.runelite.api.coords.WorldArea;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.items.Shop;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.packets.DialogPackets;

import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.utils.Debug.debug;


public class PotOfFlour extends Task {

    private static String task;
    private World randomWorld;
    private boolean hopping;
    public PotOfFlour() {
        super();
    }

    private static final WorldArea LUMBRIDGE_STORE = new WorldArea(3208, 3243, 7, 8, 0);

    @Override
    public void onStart() {
        openBank();
        if (Bank.isOpen()) {
            if (getPots() < 14) {
                task = "BUY_POTS";
            } else {
                task = "MAKE_FLOUR";
            }
            taskDuration = 10000;
            setStarted(true);
        }
    }

    @Override
    public int onLoop() {
        debug("Task Duration: "+ taskDuration);
        var local = Players.getLocal();

        switch (task) {
            case "BUY_POTS":
                if (!Bank.isOpen() && !Inventory.contains("Coins")) {
                    openBank();
                } else if (Bank.isOpen()) {
                    if (getPots() > 14) {
                        task = "MAKE_FLOUR";
                    }
                    if (!Inventory.contains("Coins")) {
                        Bank.withdrawAll("Coins", Bank.WithdrawMode.ITEM);
                    } else {
                        Bank.close();
                    }
                    if (!LUMBRIDGE_STORE.contains(local.getWorldLocation())) {
                        final WorldPoint store = LUMBRIDGE_STORE.getRandom();
                        Movement.walk(store);
                    } else if (LUMBRIDGE_STORE.contains(local.getWorldLocation())) {
                        if (!Inventory.isFull()) {
                            if (Shop.isOpen()) {
                                if (Shop.getStock(1931) > 0) {
                                    Shop.buyFifty(1931);
                                }
                                if (Shop.getStock(1931) == 0) {
                                    DialogPackets.closeInterface();
                                    randomWorld = Worlds.getRandom(x -> x.isNormal() && x.getPlayerCount() < 1800);
                                    hopping = true;
                                }
                            } else {
                                final NPC shop = NPCs.getNearest(new String[]{"Shop keeper", "Shop assistant"});
                                shop.interact("Trade");
                            }
                        } else {
                            openBank();
                        }
                    }
                }
                break;
            case "MAKE_FLOUR":
                debug("Making flour");
                break;
        }
        return -1;
    }

    @Override
    public boolean taskFinished() {
        return false;
    }

    private int getPots() {
        debug("Pots owned: "+Bank.getCount("Pot") + Bank.Inventory.getCount("Pot"));
        return Bank.getCount("Pot") + Bank.Inventory.getCount("Pot");
    }

}
