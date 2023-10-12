package net.automaters.activities.skills.mining;

import lombok.Getter;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.walking.Area;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Skills;

import java.util.Arrays;
import java.util.List;

import static net.automaters.activities.skills.mining.Locations.getClosestOreArea;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.utils.Debug.debug;

public class Ores {

    @Getter
    public enum OreType {
        BRONZE(Arrays.asList("Copper rocks", "Tin rocks"),  1, false, Locations.Bronze.class),
        IRON(Arrays.asList("Iron rocks"), 15, false, Locations.Iron.class),
        COAL(Arrays.asList("Coal rocks"), 99, false, Locations.Coal.class),
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


    public static TileObject getFurtherOre() {
        OreType chosenOreType = chooseOreType();
        TileObject ore = null;
        if (chosenOreType != null) {
            List<String> oreNames = chosenOreType.getOreName();
            debug("Selected Ore Names: " + oreNames);

            for (String oreName : oreNames) {
                TileObject oreObject = TileObjects.getNearest(LocalPlayer.getPosition(), oreName);

                if (oreObject != null) {
                    if (ore == null || oreObject.distanceTo(localPlayer) > 3) {
                        ore = oreObject;
                    }
                }
            }
            debug("further ore location: "+ore.getWorldLocation().toString());

            return ore;
        } else {
            debug("No suitable ore type found.");
            return null;
        }
    }

    public static TileObject getOre() {
        OreType chosenOreType = chooseOreType();
        TileObject ore = null;
        if (chosenOreType != null) {
            List<String> oreNames = chosenOreType.getOreName();
            debug("Selected Ore Names: " + oreNames);

            for (String oreName : oreNames) {
                TileObject oreObject = TileObjects.getNearest(LocalPlayer.getPosition(), oreName);

                if (oreObject != null) {
                    if (ore == null || oreObject.distanceTo(localPlayer) < ore.distanceTo(localPlayer)) {
                        ore = oreObject;
                        debug("ore location: "+ore.getWorldLocation().toString());
                    }
                }
            }

            return ore;
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
