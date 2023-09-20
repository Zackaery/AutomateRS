package net.automaters.api.entities;

import net.runelite.api.Player;
import net.unethicalite.api.coords.RectangularArea;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;

import static net.automaters.script.AutomateRS.scriptStarted;

public class LocalPlayer {

    static Player local = Players.getLocal();

    /**
     * Gets the local characters interaction status.
     *
     * @return if local can interact.
     */
    public static boolean localCanInteract() {
        if ((local == null) || !scriptStarted || local.isAnimating() || local.isInteracting() ) {
            return false;
        }
        return true;
    }

    /**
     * Checks to see if local character is in area.
     *
     * @return if local is in area.
     */
    public static boolean localInArea(RectangularArea area) {
        if (area.contains(local)) {
            return true;
        }
        return false;
    }

}
