package net.automaters.api.automate_utils;

import net.automaters.api.entities.PlayerCrashInfo;
import net.runelite.api.Player;
import net.unethicalite.api.entities.Players;
import static net.automaters.api.entities.LocalPlayer.hopWorld;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.tasks.Task.playerCrashCounts;
import static net.automaters.tasks.Task.playerCrashInfo;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.commons.Time.sleepUntil;

public class AutomatePlayerCrashHandler {

    public static boolean playerCrashing() {
        int playerCount = countPlayersNearby();

        if (playerCount >= 3) {
            debug("Hopping to a new world... Players near you: " + playerCount);
            hopWorld(true);
        }

        Player playerCrashing = findPlayerCrashing();
        if (playerCrashing != null) {
            handlePlayerCrash(playerCrashing);
            return true;
        }

        return false;
    }

    private static int countPlayersNearby() {
        int playerCount = 0;
        for (Player player : Players.getAll()) {
            if (player != localPlayer && player.distanceTo(localPlayer) <= 1) {
                playerCount++;
                debug("- [CRASH HANDLER] - Current Player Count: " + playerCount);
            }
        }
        return playerCount;
    }

    private static Player findPlayerCrashing() {
        return Players.getNearest(AutomatePlayerCrashHandler::isCrashingPlayer);
    }

    private static boolean isCrashingPlayer(Player player) {
        return player.isAnimating() && player.distanceTo(localPlayer) <= 1 && player != localPlayer;
    }

    private static void handlePlayerCrash(Player playerCrashing) {
        PlayerCrashInfo crashInfo = playerCrashInfo.get(playerCrashing.getName());

        if (crashInfo != null) {
            handleExistingCrasher(playerCrashing, crashInfo);
        } else {
            handleNewCrasher(playerCrashing);
        }
    }

    private static void handleExistingCrasher(Player p, PlayerCrashInfo c) {
        c.incrementCrashCount();
        // Check the last crash time for this player
        long currentTime = System.currentTimeMillis();
        long lastCrashTime = c.getLastCrashTime();
        long crashTimeSeconds = ((currentTime - lastCrashTime)/1000);

        if (crashTimeSeconds <= 60) { // Within 60 seconds
            if (c.getCrashCount() >= 3) {
                debug("[CRASH HANDLER] - " + p.getName() + " has crashed you " + c.getCrashCount() + " times in " + crashTimeSeconds + " seconds.");
                debug("[CRASH HANDLER] - Hopping Worlds.");
                hopWorld(true);
                sleep(600);
            }
        }
        debug("[CRASH HANDLER] - " + p.getName() + " has crashed you " + c.getCrashCount() + " times.");
        playerCrashInfo.put(p.getName(), c);
    }

    private static void handleNewCrasher(Player p) {
        debug("- [CRASH HANDLER] - New Player has crashed you: " + p.getName());
        // Create a new PlayerCrashInfo object for this player
        PlayerCrashInfo c = new PlayerCrashInfo();
        c.updateName(p.getName());
        debug("- [CRASH HANDLER] new - Creating new hashmap for: " + p.getName());
        c.incrementCrashCount();
        debug("- [CRASH HANDLER] new - " + p.getName() + " has crashed you " + c.getCrashCount() + " times.");
        debug("- [CRASH HANDLER] new - Last crash happened: " + c.getLastCrashTimeSeconds() + " seconds ago.");
        c.updateLastCrashTime();
        debug("- [CRASH HANDLER] new - Resetting crash time to: " + c.getLastCrashTimeSeconds() + " seconds ago.");

        // Rest of the new crash handling logic
        // ...

        playerCrashInfo.put(p.getName(), c);
        debug("- [CRASH HANDLER] new - Added " + p.getName() + " to your crash list");
    }
}
