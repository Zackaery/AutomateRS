package net.automaters.api.walking;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldArea;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;

import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.unethicalite.api.commons.Time.sleep;

@Slf4j
public class Walking {

    public static boolean automateWalk(Area area) {
        WorldArea worldArea = area.toWorldArea();
        debug("Walking to area...");
        var attempts = 0;
        while (scriptStarted && !worldArea.contains(localPlayer) && attempts < 15) {
            if (Players.getLocal().isMoving() || Movement.isWalking()) {
                attempts = 0;
            } else {
                Movement.walkTo(worldArea);
                attempts++;
                debug("Walking to area... Add an attempt: " + attempts);
            }
            Movement.walkTo(worldArea);
            sleep(600, 2400);
            debug("Walking to area... Total attempts: " + attempts);
        }
        if (attempts >= 15) {
            debug("Failed to walk to area.");
            return false;
        } else {
            debug("Arrived at area.");
            return true;
        }
    }

}
