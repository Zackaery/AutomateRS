package net.automaters.tasks;

import net.automaters.api.walking.Area;
import net.runelite.api.coords.WorldArea;
import net.unethicalite.api.plugins.LoopedPlugin;

import static net.automaters.api.walking.Walking.automateWalk;


public class Test extends LoopedPlugin {

    private static final Area TEST_AREA = new Area(3208, 3243, 3222, 3246, 0);
    @Override
    protected int loop() {
        WorldArea area = TEST_AREA.toWorldArea();
        automateWalk(area);
       return 600;
    }
}
