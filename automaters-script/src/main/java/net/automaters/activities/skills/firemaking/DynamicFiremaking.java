package net.automaters.activities.skills.firemaking;

import lombok.Getter;
import net.automaters.api.entities.LocalPlayer;
import net.runelite.api.GameObject;
import net.runelite.api.Tile;
import net.runelite.api.WallObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.scene.Tiles;

import java.util.*;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.tasks.Task.secondaryTaskActive;
import static net.unethicalite.api.commons.Time.sleep;

public class DynamicFiremaking {

    @Getter
    private static List<Tile> fireArea;
    private static Tile tileToLight;

    public static boolean firemaking;
    private static int verifyLogs = 0;

    public DynamicFiremaking() {
        firemaking = true;
        lightFire();
    }

    public static boolean isEmptyTile(Tile tile) {

        if (tile == null) {
            return false;
        }

        return TileObjects.getFirstAt(tile, v -> v instanceof GameObject) == null
                && TileObjects.getFirstAt(tile, v -> v instanceof WallObject) == null;
    }

    private void getTileToLight() {
        Optional<Tile> emptyTile = fireArea.stream()
                .max(Comparator.comparingInt(tile -> tile.getWorldLocation().getX()));

        if (emptyTile.isPresent()) {
            tileToLight = emptyTile.get();
        } else {
            fireArea = generateFireArea();
        }
    }

    protected void lightFire() {
        var logs = Inventory.getFirst(x -> x.getName().toLowerCase(Locale.ROOT).contains("logs"));
        var tinderbox = Inventory.getFirst("Tinderbox");
        var fireLogs = TileItems.getFirstAt(LocalPlayer.getPosition(), x -> x.getName().toLowerCase(Locale.ROOT).contains("logs"));

        if (!scriptStarted || logs == null && fireLogs == null || tinderbox == null && fireLogs == null) {
            endTask();
            return;
        }

        if (fireArea == null || fireArea.isEmpty()) {
            fireArea = generateFireArea();
            getTileToLight();
            sleep(1000);
            return;
        }

        while (scriptStarted && tileToLight != null) {
            if (!LocalPlayer.canInteract()) {
                sleep(333);
                return;
            }
            if (tileToLight.getWorldLocation().equals(LocalPlayer.getPosition())) {
                tileToLight = null;
            } else {
                Movement.walk(tileToLight);
                sleep(800);
                return;
            }
        }

        fireArea = generateFireArea();
        if (!LocalPlayer.canInteract()) {
            sleep(333);
            return;
        }

        if (LocalPlayer.canInteract() && fireLogs != null) {
            if (verifyLogs < 3) {
                verifyLogs++;
                sleep(600);
                return;
            } else {
                fireLogs.interact("Light");
                sleep(600);
                return;
            }
        }

        if (LocalPlayer.canInteract() && fireLogs == null && isPlayerInFireArea()) {
            debug("Lighting: "+logs.getName());
            tinderbox.useOn(logs);
            verifyLogs = 0;
            sleep(333);
            return;
        } else {
            if (!isPlayerInFireArea()) {
                if (tileToLight != null) {
                    Movement.walkTo(tileToLight);
                } else {
                    fireArea = generateFireArea();
                    getTileToLight();
                }
            }
        }
        lightFire();
    }

    public static List<Tile> generateFireArea() {
        WorldPoint localPosition = LocalPlayer.getPosition();
        List<Tile> fireArea = new ArrayList<>();

        int numDirections = 41; // The total number of directions (21 positive and 20 negative)

        int[][] directions = new int[numDirections][2];

        for (int i = 0; i < numDirections; i++) {
            if (i % 2 == 0) {
                directions[i] = new int[]{0, i / 2};       // Positive directions for even indices
            } else {
                directions[i] = new int[]{0, -i / 2};      // Negative directions for odd indices
            }
        }

        int maxTiles = 10;

        // Flag to track if we found a valid fire area
        boolean foundValidArea = false;

        // Iterate through each direction
        for (int[] direction : directions) {
            if (foundValidArea) {
                break; // Stop iterating if we found a valid area
            }

            int xOffset = 0;
            int yOffset = direction[1];

            // Generate in the current direction
            for (int i = 0; i < maxTiles; i++) {

                Tile tile = Tiles.getAt(localPosition.getWorldX() + xOffset, localPosition.getWorldY() + yOffset, localPosition.getPlane());

                // Check if the tile is valid (not null), empty, and walkable
                if (tile != null && isEmptyTile(tile) && Reachable.isWalkable(tile.getWorldLocation())) {
                    fireArea.add(tile); // Add the tile to the fireArea
                    xOffset++; // Move to the next tile in the positive x-direction
                    foundValidArea = true; // Set the flag to true if a valid area is found
                } else {
                    break; // Stop adding tiles if we hit an object tile or exceed the maxTiles
                }
            }
            // Reset myX to the player's X-coordinate
            xOffset = 0;

            // Generate in the negative x-direction
            for (int i = 0; i < maxTiles; i++) {
                xOffset--; // Move to the next tile in the negative x-direction
                Tile tile = Tiles.getAt(localPosition.getWorldX() + xOffset, localPosition.getWorldY(), localPosition.getPlane());

                // Check if the tile is valid (not null), empty, and walkable
                if (tile != null && isEmptyTile(tile) && Reachable.isWalkable(tile.getWorldLocation())) {
                    fireArea.add(tile); // Add the tile to the fireArea
                } else {
                    break; // Stop adding tiles if we hit an object tile or exceed the maxTiles
                }
            }
        }

        // If we found a valid area, return the fireArea
        if (foundValidArea) {
            return Collections.unmodifiableList(fireArea);
        }

        // If no valid area was found in any direction, return an empty list
        return Collections.emptyList();
    }

    private boolean isPlayerInFireArea() {
        WorldPoint playerPosition = LocalPlayer.getPosition();

        if (fireArea != null) {
            for (Tile tile : fireArea) {
                WorldPoint tileLocation = tile.getWorldLocation();
                if (playerPosition.equals(tileLocation)) {
                    //debug("Firemaking: player is in fire area");
                    return true;
                }
            }
        }
        //debug("Firemaking: player is NOT in fire area");
        return false;
    }

    private void endTask() {
        debug("endTask");
        firemaking = false;
        secondaryTaskActive = false;
    }
}