package net.automaters.api.walking;

import net.automaters.api.utils.Calculator;
import net.runelite.api.Locatable;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

import static net.automaters.api.utils.Debug.debug;

public class Area {

    boolean debugEnabled = false;

    public final int minX;
    public final int minY;
    public final int maxX;
    public final int maxY;
    public final int thisZ;

    public Area(int x1, int y1, int x2, int y2) {
        this(x1, y1, x2, y2, 0);
    }

    public Area(int x1, int y1, int x2, int y2, int z) {
        minX = Math.min(x1, x2);
        minY = Math.min(y1, y2);
        maxX = Math.max(x1, x2);
        maxY = Math.max(y1, y2);
        thisZ = z;
    }

    public WorldArea toWorldArea() {
        var area = new WorldArea(minX, minY, maxX - minX, maxY - minY, thisZ);
        if (debugEnabled) {
            debugArea(area);
        }

        return area;
    }

    public void debugArea(WorldArea area) {
        debug("WorldArea: " + area.getX() + ", " + area.getY() + ", " +
                area.getWidth() + ", " + area.getHeight() + ", " + area.getPlane());
    }

    public Position randomPosition() {
        var x = Calculator.random(minX, maxX);
        var y = Calculator.random(minY, maxY);
        var z = thisZ;

        var position = new Position(x, y, z);
        if (debugEnabled) {
            debug("Random Position: " + position + " found from Rectangular Area: " + this);
        }

        return position;
    }

    public boolean contains(WorldPoint worldPoint) {
        return false;
    }

    public boolean contains(Locatable locatable) {
        return contains(locatable.getWorldLocation());
    }

}
