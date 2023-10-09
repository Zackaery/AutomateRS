package net.automaters.api.walking;

import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;

public class Position {
    @Inject
    private Client client;
    public final int x;
    public final int y;
    public final int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(WorldPoint worldPoint) {
        this.x = worldPoint.getX();
        this.y = worldPoint.getY();
        this.z = worldPoint.getPlane();
    }

    public Position north() {
        return new Position(x, y + 1, z);
    }

    public Position south() {
        return new Position(x, y - 1, z);
    }

    public Position east() {
        return new Position(x + 1, y, z);
    }

    public Position west() {
        return new Position(x - 1, y, z);
    }

    public Position add(int x, int y, int z) {
        return new Position(this.x + x, this.y + y, this.z + z);
    }

    public WorldPoint toWorldPoint() {
        return new WorldPoint(x, y, z);
    }

    public double distanceTo(Position other) {
        if (z != other.z) {
            return Double.MAX_VALUE;
        }

        return Math.hypot((other.x - x), (other.y - y));
    }

}
