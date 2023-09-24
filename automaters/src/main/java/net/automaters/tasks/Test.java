package net.automaters.tasks;

import net.automaters.api.walking.Area;
import net.unethicalite.api.plugins.LoopedPlugin;

import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.items.Items.getAmountTotal;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.AutomateRS.scriptStarted;


public class Test extends LoopedPlugin{

    private static final Area TEST_AREA = new Area(3208, 3243, 3222, 3246, 0);

    @Override
    protected int loop() {

        if ((localPlayer == null) || !scriptStarted) {
            debug("Player does not exist.");
            return -1;
        }

        debug(String.valueOf(getAmountTotal("Coins", true)));

       return 50;
    }
}
