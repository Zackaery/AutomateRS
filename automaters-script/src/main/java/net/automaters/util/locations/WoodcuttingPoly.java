package net.automaters.util.locations;

import net.unethicalite.api.commons.Rand;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.coords.Area;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import static net.automaters.script.Variables.scriptStarted;

public class WoodcuttingPoly
{
    private static final Map<String, Area> areas = new HashMap<>();

    static
    {
        // Define your polygonal areas and label them here
        int[][] shopBobAxesFS = {
                {3228,3206},{3228,3205},{3227,3205},{3227,3202},{3228,3202},{3228,3201},{3234,3201},{3234,3206},{3228,3206}
        };

        int[][] anotherArea = {
                // Define the coordinates for another area here
        };

        areas.put("SHOP_BOB_AXES_FS", new CustomPolygonalArea(0, shopBobAxesFS));
        areas.put("ANOTHER_AREA", new CustomPolygonalArea(0, anotherArea));
        // Add more areas as needed
    }

    public static Area getArea(String areaName)
    {
        return areas.get(areaName);
    }

    private static class CustomPolygonalArea implements Area
    {
        private final Polygon polygon;
        private final int plane;

        public CustomPolygonalArea(int plane, int[][] points)
        {
            this.plane = plane;
            this.polygon = new Polygon();

            for (int[] point : points)
            {
                polygon.addPoint(point[0], point[1]);
            }
        }

        @Override
        public boolean contains(WorldPoint worldPoint)
        {
            if (worldPoint.getPlane() == -1 || worldPoint.getPlane() != plane)
            {
                return false;
            }

            return polygon.contains(worldPoint.getX(), worldPoint.getY());
        }

        @Override
        public WorldPoint getRandomTile()
        {
            int x, y;
            Rectangle r = polygon.getBounds();
            do
            {
                x = Rand.nextInt(r.x, r.x + r.width);
                y = Rand.nextInt(r.y, r.y + r.height);
            } while (scriptStarted && !polygon.contains(x, y));
            return new WorldPoint(x, y, plane);
        }
    }
}
