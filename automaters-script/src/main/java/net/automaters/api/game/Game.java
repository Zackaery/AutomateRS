package net.automaters.api.game;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.unethicalite.client.Static;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static net.automaters.script.Variables.scriptStarted;

@Slf4j
@Singleton
public class Game {

    @Inject
    static Client client = Static.getClient();
    @Inject
    static ClientThread clientThread = Static.getClientThread();

    @Inject
    public static ExecutorService executorService;

    public static Client client() {
        return client;
    }

    public static boolean waiting = false;
    public static long gameTickDelay = 0;

    public static ItemContainer container(InventoryID inventoryID) {
        return client.getItemContainer(inventoryID);
    }

    public ItemContainer container(int containerId) {
        InventoryID inventoryID = InventoryID.getValue(containerId);
        return client.getItemContainer(inventoryID);
    }


    public int tick(int tickMin, int tickMax) {
        Random r = new Random();
        int result = r.nextInt((tickMax + 1) - tickMin) + tickMin;

        for (int i = 0; i < result; i++) {
            tick();
        }
        return result;
    }

    public void tick(int ticks) {
        if (client.isClientThread()) {
            waiting = true;
            gameTickDelay = ticks() + ticks;
            return;
        }

        for (int i = 0; i < ticks; i++) {
            tick();
        }
    }

    public static void tick() {
        if (client.getGameState() == GameState.LOGIN_SCREEN || client.getGameState() == GameState.LOGIN_SCREEN_AUTHENTICATOR) {
            return;
        }

        waiting = true;
        gameTickDelay = ticks() + 1;

        if (client.isClientThread()) {
            return;
        }

        long start = client().getTickCount();

        while (scriptStarted && client.getTickCount() == start) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                waiting = false;
                throw new RuntimeException(e);
            }
        }
    }

    public static long ticks() {
        return client.getTickCount();
    }

    public static void waitUntil(BooleanSupplier condition) {
        waiting = true;

        if (client.isClientThread()) {
            log.info("Submitting waitUntil on Executor");
            executorService.submit(() -> waitUntil(condition));
            return;
        }

        if (!waitUntil(condition, 100)) {
            waiting = false;
            throw new IllegalStateException("timed out");
        }
        waiting = false;
    }

    public static boolean waitUntil(BooleanSupplier condition, int ticks) {
        waiting = true;
        if (client.isClientThread()) {
            log.info("Submitting waitUntil on Executor");
            boolean result = getFromExecutorThread(() -> waitUntil(condition, ticks));
            waiting = false;
            return result;
        } else {
            for (var i = 0; i < ticks; i++) {
                if (condition.getAsBoolean()) {
                    waiting = false;
                    return true;
                }
                tick();
            }
            waiting = false;
            return false;
        }
    }
    public static <T> T getFromExecutorThread(Supplier<T> supplier) {
        if (client.isClientThread()) {
            CompletableFuture<T> future = new CompletableFuture<>();

            executorService.submit(() -> {
                future.complete(supplier.get());
            });
            return future.join();
        } else {
            return supplier.get();
        }
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
