package net.automaters.tasks.utils;

import net.unethicalite.api.items.Bank;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.tasks.Task.*;

public class Setup {

    public static boolean setupPrimaryTool() {
        return primaryTool != null;
    }

    public static boolean setupSecondaryTask() {
        return secondaryTask != null;
    }

    public static void startTask() {
        if (Bank.isOpen()) {
            Bank.close();
        }
        debug("Primary Tool: " + primaryTool);
        debug("Secondary Tool: " + secondaryTool);
        debug("Secondary Task: " + secondaryTask);
    }

}
