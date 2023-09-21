package net.automaters.api.entities;

import net.automaters.api.walking.Position;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.unethicalite.api.coords.RectangularArea;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;

import static net.automaters.script.AutomateRS.debug;
import static net.automaters.script.AutomateRS.scriptStarted;

public class LocalPlayer {

    private LocalPlayer() {

    }

    public static Player local = Players.getLocal();


    /**
     * Gets the local characters interaction status.
     *
     * @return if local can interact.
     */
    public static boolean localCanInteract() {
        return ((local == null) || !scriptStarted || local.isAnimating() || local.isInteracting());
    }

    /**
     * Checks to see if local character is in area.
     *
     * @return if local is in area.
     */
    public static boolean localInArea(RectangularArea area) {
        return area.contains(local.getWorldLocation());
    }

    /**
     * Checks to see if local character is at nearest bank.
     *
     * @return if local is at nearest bank.
     */
    public static boolean isInBank() {
        return BankLocation.getNearest().getArea().contains(local.getWorldLocation());
    }

    /**
     * Walks your player to the closest bank.
     */
    public static void walkToNearestBank() {
        if (!Bank.isOpen() && !LocalPlayer.isInBank()) {
            Movement.walkTo(BankLocation.getNearest());
        }
    }

    /**
     * Opens the closest bank to your player.
     */
    public static void openBank() {
        TileObject bank = TileObjects.getFirstSurrounding(local.getWorldLocation(), 10, obj -> obj.hasAction("Bank"));
        TileObject bankChest = TileObjects.getFirstSurrounding(local.getWorldLocation(), 10, obj -> obj.getName().startsWith("Bank chest"));
        if (!Bank.isOpen() && !LocalPlayer.isInBank()) {
            walkToNearestBank();
        } else if (!Bank.isOpen() && LocalPlayer.isInBank()) {
            if (bank != null) {
                bank.interact("Bank");
            } else if (bankChest != null) {
                bankChest.interact("Use");
            }
        } else {
            debug("The bank is open!");
        }
    }

}
