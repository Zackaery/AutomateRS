package net.automaters.activities.skills.woodcutting;

import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.plugins.LoopedPlugin;

import java.util.Comparator;
import java.util.Locale;

import static net.automaters.script.AutomateRS.*;
import static net.unethicalite.api.items.Inventory.isFull;

public class Woodcutting extends LoopedPlugin {
    private WorldPoint startLocation = null;
    private boolean started;
    @Override
    public int loop() throws InterruptedException {

        var local = Players.getLocal();
        if ((local == null)|| !scriptStarted) {
            debug("Player does not exist.");
            return -1;
        }

        if (!started) {
            startLocation = local.getWorldLocation();
            debug("Starting location = "+startLocation);
            started = true;
        }

        var tree = TileObjects
                .getSurrounding(startLocation, 8, "Tree")
                .stream()
                .min(Comparator.comparing(x -> x.distanceTo(local.getWorldLocation())))
                .orElse(null);

        var logs = Inventory.getFirst(x -> x.getName().toLowerCase(Locale.ROOT).contains("logs"));

        if (logs != null && !local.isAnimating() && isFull())
        {
            logs.drop();

            debug("Dropping logs.");
            return 500;
        }

        if (local.isMoving() || local.isAnimating())
        {
            debug("Player is animating.");
            return 333;
        }

        if (tree == null)
        {
            debug("Could not find any trees");
            return 1000;
        }

        tree.interact("Chop down");
        debug("Chopping tree.");
        return 1000;
    }
}
