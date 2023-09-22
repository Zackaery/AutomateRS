package net.automaters.api.walking;

import lombok.extern.slf4j.Slf4j;
import net.automaters.api.game.Game;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.client.Static;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.game.Game.getFromClientThread;
import static net.automaters.script.AutomateRS.debug;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.movement.Movement.toggleRun;

@Slf4j
public class Walking {

    @Inject
    static Client client = Static.getClient();
    @Inject
    static ClientThread clientThread = Static.getClientThread();

    private static final int MAX_MIN_ENERGY = 50;
    private static final int MIN_ENERGY = 15;
    @Inject
    private static Game game;

    private static int minEnergy = new Random().nextInt(MAX_MIN_ENERGY - MIN_ENERGY + 1) + MIN_ENERGY;

    @Inject
    public Walking(Game game) {
        this.game = game;
    }

    private WorldPoint getWpFromArea(Area area) {
        if (area instanceof Area) {
            return ((Area) area).randomPosition().toWorldPoint();
        }
        return null;
    }

    public static boolean automateWalk(WorldArea area) {
        debug("Walking to area...");
        var attempts = 0;
        while (!area.contains(localPlayer) && attempts < 15) {
            if (Players.getLocal().isMoving() || Movement.isWalking()) {
                attempts = 0;
               /* if (!isRunning() && client.getEnergy() > minEnergy) {
                    minEnergy = new Random().nextInt(MAX_MIN_ENERGY - MIN_ENERGY + 1) + MIN_ENERGY;
                    debug("[Walking] Enabling run, next minimum run energy: " + minEnergy);
                    setRun(true);
                }*/
            } else {
                Movement.walkTo(area);
                attempts++;
                debug("Walking to area... Add an attempt: " + attempts);
            }
            Movement.walkTo(area);
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

    public static boolean boredWalk(WorldArea area) {
        debug("Walking to: "+ area.toString());
        Movement.walkTo(area);

        while (!area.contains(Players.getLocal())) {
            debug("Walking and sleeping is sleep walking!");
            sleep(1000,5000);
            Movement.walkTo(area);
        }
        debug("Arrived at: "+ area.toString());
        return true;
    }

    public static void setRun(boolean run) {
     /*   if (minEnergy == client.getEnergy()) {
            toggleRun();
            setRun(false);
        }*/
    }

    public static boolean isRunning() {
        return (getFromClientThread(() -> client.getVarpValue(173) == 1));

    }



}
