package net.automaters.api.entities;

import net.automaters.api.walking.Area;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.World;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.GrandExchange;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import net.unethicalite.api.widgets.Widgets;

import java.util.Map;

import static net.automaters.activities.skills.mining.Mining.resourceObject;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.tasks.Task.*;
import static net.automaters.util.locations.Constants.GRAND_EXCHANGE;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.commons.Time.sleepUntil;

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

    public static TileObject getClosestBank() {
        TileObject bank = TileObjects.getNearest(getPosition(), x -> x.hasAction("Bank"));
        if (bank == null || bank.distanceTo(localPlayer) > 10 || !Reachable.isInteractable(bank)) {
            return null;
        } else {
            objectToRender = bank;
            return bank;
        }
    }

    /**
     * Walks your player to the closest bank.
     */
    public static void walkToNearestBank() {
        BankLocation closestBank = BankLocation.getNearest();
        while (scriptStarted && !Bank.isOpen() && !closestBank.getArea().contains(localPlayer)) {
            Movement.walkTo(closestBank);
            sleep(600, 1800);
        }
    }

    /**
     * Opens the closest bank to your player.
     */
    public static void openBank() {
        TileObject bank = getClosestBank();
        while (scriptStarted && !Bank.isOpen()) {
            if (bank == null || !Reachable.isInteractable(bank)) {
                debug("Walking to closest bank!");
                walkToNearestBank();
                return;
            } else if (Reachable.isInteractable(bank)) {
                if (bank.distanceTo(Players.getLocal()) >= 6) {
                    debug("Walking to "+bank.getName());
                    Movement.walkNextTo(bank);
                    return;
                } else {
                    debug("Opening "+bank.getName());
                    bank.interact("Bank");
                    Time.sleepUntil(Bank::isOpen, 2400);
                }
            }
        }

        if (Bank.isOpen()) {
            Widget bankTutWidget = Widgets.get(664, 29, 0);
            Widget bankSpaceWidget = Widgets.get(289, 7);
            if (bankTutWidget != null) {
                bankTutWidget.interact("Close");
            }
            if (bankSpaceWidget != null) {
                bankSpaceWidget.interact("Not now");
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

    public static void hopWorld(boolean resetVariables) {
        World currentWorld = Worlds.getCurrentWorld();
        World newWorld;

        if (currentWorld.isMembers()) {
            newWorld = Worlds.getRandom(world -> world.isMembers() && world.isNormal());
        } else {
            newWorld = Worlds.getRandom(world -> !world.isMembers() && world.isNormal());
        }

        if (resetVariables) {
            debug("player crash counts: \n"+playerCrashCounts);
            for (Map.Entry<String, Long> entry : lastCrashTimes.entrySet()) {
                String playerName = entry.getKey();
                long lastCrashTimeMs = entry.getValue();

                // Convert milliseconds to seconds
                long lastCrashTimeSec = lastCrashTimeMs / 1000;

                debug("Player: " + playerName + ", Last Crash Time (seconds): " + lastCrashTimeSec);
            }
            playerCrashCounts.clear();
            lastCrashTimes.clear();
            resourceObject = null;
            resourceLocation = null;
            hasResources = false;
        }

        if (currentWorld == Worlds.getCurrentWorld()) {
            debug("Current World: "+currentWorld.getId()+", New World: "+newWorld.getId());
            Worlds.hopTo(newWorld);
            sleepUntil(() -> Worlds.getCurrentWorld() == newWorld, 10000);
        }
    }
}


