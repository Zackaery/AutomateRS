package net.automaters.tasks;

import net.automaters.api.walking.Area;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.plugins.LoopedPlugin;

import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.script.AutomateRS.debug;
import static net.automaters.script.AutomateRS.scriptStarted;


public class Test extends LoopedPlugin {

    private static final Area TEST_AREA = new Area(3208, 3243, 3222, 3246, 0);

    @Override
    protected int loop() {

        var local = Players.getLocal();
        if ((local == null) || !scriptStarted) {
            debug("Player does not exist.");
            return -1;
        }

        WorldPoint playerPosition = Players.getLocal().getWorldLocation();
        WorldArea area = TEST_AREA.toWorldArea();

        if (!playerPosition.isInArea(area)) {
            debug("test.java walking to area: " + area);
            automateWalk(area);
            return 600;
        } else {
            debug("Well i'm here now what?");
        }
        return 0;
    }
}
