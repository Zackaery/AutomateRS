package net.automaters.api.automate_utils;

import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.entities.PlayerCrashInfo;
import net.runelite.api.Player;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Reachable;

import static net.automaters.activities.skills.mining.Ores.getFurtherOre;
import static net.automaters.api.entities.LocalPlayer.hopWorld;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.tasks.Task.playerCrashCounts;
import static net.automaters.tasks.Task.playerCrashInfo;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.commons.Time.sleepUntil;

public class AutomatePlayerCrashHandler {


    public static boolean playerCrashing() {
        int playerCount = 0;
        Player playerCrashing = Players.getNearest(x -> x.isAnimating()
                && x.distanceTo(Players.getLocal()) <= 1
                && x != Players.getLocal());

        for (Player player : Players.getAll()) {
            if (player != localPlayer && player.distanceTo(localPlayer) <= 1) {
                playerCount++;
                // Check if this player has crashed you before
                PlayerCrashInfo crashInfo = playerCrashInfo.get(player.getName());
                if (crashInfo != null) {
                    if (playerCrashing != null && player.getName().equals(playerCrashing.getName())) {
                        // Increment the crash count for this player in the PlayerCrashInfo object
                        crashInfo.incrementCrashCount();

                        // Check the last crash time for this player
                        long currentTime = System.currentTimeMillis();
                        long lastCrashTime = crashInfo.getLastCrashTime();

                        if (currentTime - lastCrashTime <= 60000) { // Within 60 seconds
                            if (crashInfo.getCrashCount() >= 3) {
                                debug("You've been crashed 3+ times in 60 seconds, hopping to new world.");
                                sleep(300, 600);
                                hopWorld(true);
                                sleep(600);
                            }
                        }
                        debug("Players near you: " + playerCount);
                        debug(player.getName() + " crashed you " + crashInfo.getCrashCount() + " times.");
                        debug(player.getName() + " last crashed you " + crashInfo.getLastCrashTimeSeconds() + " seconds ago.");
                    } else {
                        // Reset the crash count and update the last crash time
                        crashInfo.resetCrashCount();
                    }
                    crashInfo.updateLastCrashTime();
                } else {
                    // Create a new PlayerCrashInfo object for this player
                    crashInfo = new PlayerCrashInfo();
                    crashInfo.incrementCrashCount();
                    crashInfo.updateLastCrashTime();
                    playerCrashInfo.put(player.getName(), crashInfo);
                }
            }
            if (playerCrashing != null) {
                debug("You've been crashed " + playerCrashCounts.getOrDefault(playerCrashing.getName(), 0) + " times.");
                debug("Player: " + playerCrashing.getName() + " is crashing you.");
                return true;
            }
        }

        if (playerCount >= 3) {
            debug("Hopping to a new world... Players near you: " + playerCount);
            hopWorld(true);
        }

        return false;
    }

}
