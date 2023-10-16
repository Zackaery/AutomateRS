package net.automaters.activities.skills.mining;

import lombok.Getter;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.walking.Area;
import net.runelite.api.GameObject;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Skills;

import java.util.Arrays;
import java.util.List;

import static net.automaters.activities.skills.mining.Locations.getClosestOreArea;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.entities.LocalPlayer.localPlayer;

public class Ores {

    @Getter
    public enum OreType {
        BRONZE(Arrays.asList("Copper rocks", "Tin rocks"),  1, false, Locations.Bronze.class),
        IRON(List.of("Iron rocks"), 15, false, Locations.Iron.class),
        COAL(List.of("Coal rocks"), 99, false, Locations.Coal.class),
        ;

        private final List<String> oreName;
        private final int reqMiningLevel;
        private final boolean members;
        private final Class<? extends Enum> oreLocationClass;

        OreType(List<String> oreName, int reqMiningLevel, boolean members,  Class<? extends Enum> oreLocationClass) {
            this.oreName = oreName;
            this.reqMiningLevel = reqMiningLevel;
            this.members = members;
            this.oreLocationClass = oreLocationClass;
        }
    }

    public static OreType chooseOreType() {
        int miningLevel = Skills.getLevel(Skill.MINING);

        for (int i = OreType.values().length - 1; i >= 0; i--) {
            OreType oreType = OreType.values()[i];
            if (miningLevel >= oreType.reqMiningLevel) {
                return oreType;
            }
        }

        return null;
    }


    public static GameObject getFurtherOre() {
        OreType chosenOreType = chooseOreType();

        if (chosenOreType != null) {
            List<String> oreNames = chosenOreType.getOreName();
            debug("Selected Ore Names: " + oreNames);

            GameObject closestDistantOre = null;
            double closestDistance = Double.MAX_VALUE;
            double minDistance = 3; // Minimum distance to consider

            for (String oreName : oreNames) {

                for (TileObject oreObject : TileObjects.getSurrounding(Players.getLocal().getWorldLocation(), 12, oreName)) {
                    if (oreObject instanceof GameObject) {
                        double distance = oreObject.distanceTo(localPlayer);

                        if (distance >= minDistance && distance < closestDistance) {
                            closestDistantOre = (GameObject) oreObject;
                            closestDistance = distance;
                        }
                    }
                }
            }
            debug("Further Ore Location: " + closestDistantOre.getWorldLocation().toString());
            return closestDistantOre;
        } else {
            debug("No suitable ore type found.");
            return null;
        }
    }

    public static GameObject getOre() {
        OreType chosenOreType = chooseOreType();
        if (chosenOreType != null) {
            List<String> oreNames = chosenOreType.getOreName();

            GameObject closestOre = null;
            double closestDistance = Double.MAX_VALUE;

            for (String oreName : oreNames) {
                TileObject oreObject = TileObjects.getNearest(LocalPlayer.getPosition(), oreName);
                if (oreObject instanceof GameObject) {
                    double distance = oreObject.distanceTo(localPlayer);

                    if (distance < closestDistance) {
                        closestOre = (GameObject) oreObject;
                        closestDistance = distance;
                    }
                }
            }



            return closestOre;
        } else {
            debug("No suitable ore type found.");
            return null;
        }
    }

    public static Area getOreLocation() {
        OreType location = chooseOreType();
        if (location != null) {
            Class<? extends Enum> locationClass = location.getOreLocationClass();
            return getClosestOreArea(locationClass);
        } else {
            return null;
        }
    }
}
