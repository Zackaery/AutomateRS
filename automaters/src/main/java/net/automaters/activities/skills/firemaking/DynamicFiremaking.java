package net.automaters.activities.skills.firemaking;

import lombok.Getter;
import net.automaters.api.entities.LocalPlayer;
import net.runelite.api.GameObject;
import net.runelite.api.Tile;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.movement.pathfinder.GlobalCollisionMap;
import net.unethicalite.api.scene.Tiles;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.automaters.activities.skills.woodcutting.Woodcutting.lightLogs;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.utils.Debug.debug;
import static net.unethicalite.api.commons.Time.sleep;

public class DynamicFiremaking {

    @Inject
    private static GlobalCollisionMap collisionMap;

    @Getter
    private static List<Tile> fireArea;

    public DynamicFiremaking() {
        debug("DynamicFiremaking");
        fireArea = generateFireArea(3);
        lightFire();
    }

    public static boolean isEmptyTile(Tile tile) {
        if (tile == null) {
            return false;
        }

        return TileObjects.getFirstAt(tile, a -> a instanceof GameObject) == null;
    }

    protected int lightFire() {
        var logs = Inventory.getFirst(x -> x.getName().toLowerCase(Locale.ROOT).contains("logs"));
        var tinderbox = Inventory.getFirst("Tinderbox");

        if (logs == null || tinderbox == null) {
            lightLogs = false;
            return 1000;
        }

        Optional<Tile> emptyTile = fireArea.stream()
                .filter(tile -> {
                    Tile worldTile = Tiles.getAt(tile.getWorldLocation());
                    return worldTile != null && isEmptyTile(worldTile);
                })
                .min(Comparator.comparingInt(wp -> wp.distanceTo(localPlayer)));

        if (emptyTile.isEmpty()) {
            fireArea = generateFireArea(3);
            return 1000;
        }

        Tile tileToLight = emptyTile.get();

        if (!tileToLight.getWorldLocation().equals(LocalPlayer.getPosition())) {
            if (!LocalPlayer.canInteract()) {
                return 333;
            }

            Movement.walk(tileToLight);
            return 1000;
        }

        if (!LocalPlayer.canInteract()) {
            return 333;
        }

        if (LocalPlayer.canInteract()) {
            tinderbox.useOn(logs);
            sleep(600, 800);
        }

        return 500;
    }

    private List<Tile> generateFireArea(int radius) {
        return Tiles.getSurrounding(Players.getLocal().getWorldLocation(), radius).stream()
                .filter(tile -> tile != null
                        && isEmptyTile(tile)
                        && Reachable.isWalkable(tile.getWorldLocation()))
                .collect(Collectors.toUnmodifiableList());
    }
}