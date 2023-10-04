package net.automaters.api.entities;

import net.automaters.api.walking.Area;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.GrandExchange;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import net.unethicalite.api.widgets.Widgets;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.util.locations.Constants.GRAND_EXCHANGE;
import static net.unethicalite.api.commons.Time.sleep;

public class LocalPlayer {

    private LocalPlayer() {

    }

    public static Player localPlayer = Players.getLocal();

    public static WorldPoint getPosition() { return localPlayer.getWorldLocation(); }

    public static Area getClosestArea(Area... areas) {
        WorldPoint playerPosition = getPosition();

        if (playerPosition == null || areas == null || areas.length == 0) {
            return null;
        }

        double closestDistance = Double.MAX_VALUE;
        Area closestArea = null;

        for (Area area : areas) {
            if (area == null) {
                continue;
            }

            int centerX = (area.minX + area.maxX) / 2;
            int centerY = (area.minY + area.maxY) / 2;
            int plane = area.toWorldArea().getPlane();

            double distance = playerPosition.distanceTo(new WorldPoint(centerX, centerY, plane));

            if (distance < closestDistance) {
                closestDistance = distance;
                closestArea = area;
            }
        }

//        debug("Player Position: "+playerPosition);
//        debug("Closest Area: "+ closestArea.maxX +", "+closestArea.minY +", "+closestArea.minX +", "+closestArea.maxY +", "+closestArea.thisZ);
        return closestArea;
    }


    /**
     * Gets the local characters interaction status.
     *
     * @return if local can interact.
     */
    public static boolean canInteract() {
//        debug("Can Interact: "+(localPlayer != null && scriptStarted && !localPlayer.isAnimating() && !localPlayer.isInteracting()));
        return (localPlayer != null && scriptStarted && !localPlayer.isAnimating() && !localPlayer.isInteracting());
    }

    /**
     * Checks to see if local character is in area.
     *
     * @return if local is in area.
     */
    public static boolean isInArea(Area area) {
        WorldArea worldArea = area.toWorldArea();
        return worldArea.contains(localPlayer.getWorldLocation());
    }

    /**
     * Checks to see if local character is at nearest bank.
     *
     * @return if local is at nearest bank.
     */
    public static boolean isInBank() {
        return BankLocation.getNearest().getArea().contains(localPlayer.getWorldLocation());
    }

    /**
     * Walks your player to the closest bank.
     */
    public static void walkToNearestBank() {
        if (!Bank.isOpen() && !isInBank()) {
            Movement.walkTo(BankLocation.getNearest());
        }
    }

    /**
     * Opens the closest bank to your player.
     */
    public static void openBank() {
        debug("Walking to nearest bank!");
        sleep(1000);
        TileObject bank = TileObjects.getFirstSurrounding(localPlayer.getWorldLocation(), 20, obj -> obj.hasAction("Bank"));
        TileObject bankChest = TileObjects.getFirstSurrounding(localPlayer.getWorldLocation(), 20, obj -> obj.getName().startsWith("Bank chest"));
        while (scriptStarted && !Bank.isOpen() && !isInBank()) {
            walkToNearestBank();
            sleep(600, 2400);
        }
        if (bank != null && isInBank()) {
//            debug("i can see the bank");
            bank.interact("Bank");
            sleep(2200);
        } else if (bankChest != null && isInBank()) {
//            debug("i can see a bank booth");
            bankChest.interact("Use");
            sleep(2200);
        }


        while (scriptStarted && !Bank.isOpen() && localPlayer.isMoving() && isInBank()) {
//            debug("Moving around wildly");
            sleep(1250, 1800);
        }

        if (Bank.isOpen()) {
//            debug("The bank is open!");
            Widget widget = Widgets.get(664, 29, 0);
            if (widget != null) {
                widget.interact("Close");
//                debug("Closed bank tutorial widget.");
            }
        }
    }

    /**
     * Checks to see if local character is at the Grand Exchange.
     *
     * @return if local is at the Grand Exchange.
     */
    public static boolean isInGE() {
        return GRAND_EXCHANGE.toWorldArea().contains(localPlayer.getWorldLocation());
    }

    /**
     * Walks your player to the Grand Exchange.
     */
    public static void walkToGE() {
        if (!isInGE()) {
            automateWalk(GRAND_EXCHANGE);
        }
    }

    /**
     * Opens the Grand Exchange.
     */
    public static void openGE() {
        while (scriptStarted && !isInGE()) {
            debug("Walking to Grand Exchange!");
            walkToGE();
            sleep(600, 2400);
        }
        if (!GrandExchange.isOpen() && isInGE()) {
//            debug("I can see the Grand Exchange.");
            GrandExchange.open();
            sleep(2200);
        }
        while (scriptStarted && !GrandExchange.isOpen() && localPlayer.isMoving() && isInGE()) {
//            debug("Moving around wildly.");
            sleep(350, 600);
        }
        if (GrandExchange.isOpen()) {
//            debug("The Grand Exchange is open!");
        }
    }

}


