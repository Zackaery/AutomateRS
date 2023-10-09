package net.automaters.tasks;

import net.runelite.api.Actor;
import net.runelite.api.AnimationID;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.plugins.LoopedPlugin;

import static net.automaters.api.entities.LocalPlayer.*;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.AutomateRS.scriptStarted;

public class Test extends LoopedPlugin {

    public Test() {
        loop();
    }

    @Override
    protected int loop() {

        if ((localPlayer == null) || !scriptStarted) {
            debug("Player does not exist.");
            return -1;
        }

        Player test = Players.getNearest(p -> p.isAnimating() && p.distanceTo(Players.getLocal()) <= 1);
        if (test != null) {
            debug("PLAYER = "+test.getName());
        }



       return 50;
    }

}
