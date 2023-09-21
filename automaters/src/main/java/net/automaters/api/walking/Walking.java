package net.automaters.api.walking;

import lombok.extern.slf4j.Slf4j;
import net.automaters.api.game.Game;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.callback.ClientThread;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.client.Static;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static net.automaters.api.entities.LocalPlayer.local;
import static net.automaters.script.AutomateRS.debug;
import static net.unethicalite.api.movement.Movement.toggleRun;

@Slf4j
public class Walking {

    static Client client;
    static ClientThread clientThread;

    private static final int MAX_MIN_ENERGY = 50;
    private static final int MIN_ENERGY = 15;

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
        debug("Walking to: "+ area.toString());
        var attempts = 0;
        Movement.walkTo(area);
        game.tick();
        while (!area.contains(Players.getLocal()) && attempts < 15 && !Thread.currentThread().isInterrupted()) {
            if (Players.getLocal().isMoving()) {
                attempts = 0;
            }

            if (Movement.isWalking()) {
                attempts = 0;
                if (!isRunning() && client.getEnergy() > minEnergy) {
                    minEnergy = new Random().nextInt(MAX_MIN_ENERGY - MIN_ENERGY + 1) + MIN_ENERGY;
                    debug("[Walking] Enabling run, next minimum run energy: " + minEnergy);
                    setRun(true);
                }
            } else {
                Movement.walkTo(area);
                attempts++;
                debug("Walking to: "+ area.toString()+", Total attempts: "+attempts);
            }
            game.tick();
        }

        if (attempts >= 15) {
            debug("Failed to walk to: "+ area.toString());
            return false;
        } else {
            debug("Arrived at: "+ area.toString());
            return true;
        }
    }

    public boolean automateWalk(WorldPoint target) {
        debug("Walking to: "+ target.toString());
        var attempts = 0;
        Movement.walkTo(target);
        game.tick();
        while (local.getWorldLocation().distanceTo(target) > 1 && attempts < 15 && !Thread.currentThread().isInterrupted()) {
            if (Players.getLocal().isMoving()) {
                attempts = 0;
            }

            if (Movement.isWalking()) {
                attempts = 0;
                if (!isRunning() && client.getEnergy() > minEnergy) {
                    minEnergy = new Random().nextInt(MAX_MIN_ENERGY - MIN_ENERGY + 1) + MIN_ENERGY;
                    debug("[Walking] Enabling run, next minimum run energy: " + minEnergy);
                    setRun(true);
                }
            } else if (Movement.getDestination() != null && Movement.getDestination().equals(target)) {
                debug("Last walk step: "+ Movement.getDestination().toString() + ", "+target.toString());
                game.waitUntil(() -> !Static.getClient().getLocalPlayer().isMoving(), 20);
            } else {
                Movement.walkTo(target);
                attempts++;
                debug("Attempting to walk, attempt: "+ attempts);
            }
            game.tick();
        }

        if (attempts >= 15) {
            debug("Failed to walk to: "+ target.toString());
            return false;
        } else {
            debug("Arrived at: "+ target.toString());
            return true;
        }
    }

    public static void setRun(boolean run) {
        if (isRunning() != run) {
            toggleRun();
            game.waitUntil(() -> isRunning() == run, 10);
        }
    }

    public static boolean isRunning() {
        return (getFromClientThread(() -> client.getVarpValue(173) == 1));

    }

    public static ClientThread clientThread() {
        return clientThread;
    }

    public static <T> T getFromClientThread(Supplier<T> supplier) {
        if (!client.isClientThread()) {
            CompletableFuture<T> future = new CompletableFuture<>();

            clientThread().invoke(() -> {
                future.complete(supplier.get());
            });
            return future.join();
        } else {
            return supplier.get();
        }
    }

}
