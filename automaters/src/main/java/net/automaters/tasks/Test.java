package net.automaters.tasks;

import net.automaters.api.ui.GrandExchange;
import net.automaters.api.walking.Area;
import net.runelite.api.ItemID;
import net.unethicalite.api.plugins.LoopedPlugin;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import static net.automaters.api.entities.LocalPlayer.*;
import static net.automaters.api.ui.GrandExchange.automateBuy;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.util.locations.Constants.GRAND_EXCHANGE;


public class Test extends LoopedPlugin{

    private static final Area TEST_AREA = new Area(3208, 3243, 3222, 3246, 0);

    static boolean buyAxe = true;

    @Override
    protected int loop() {


        if ((localPlayer == null) || !scriptStarted) {
            debug("Player does not exist.");
            return -1;
        }

        Area LUMB = new Area(3200, 3200, 3220, 3220);
        if (isInArea(LUMB)) {
            debug("true");
        } else {
            debug("false");
        }

       return 50;
    }


}
