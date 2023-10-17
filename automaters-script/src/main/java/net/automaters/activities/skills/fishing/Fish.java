package net.automaters.activities.skills.fishing;

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

import static net.automaters.activities.skills.fishing.Locations.getClosestFishArea;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.utils.Debug.debug;

public class Fish {

    @Getter
    public enum FishType {
        SMALLNET(List.of("Fishing spot"),  1, false, Locations.Smallnet.class),
//        IRON(List.of("Iron rocks"), 15, false, Locations.Iron.class),
//        COAL(List.of("Coal rocks"), 99, false, Locations.Coal.class),
        ;

        private final List<String> fishName;
        private final int reqFishingLevel;
        private final boolean members;
        private final Class<? extends Enum> fishLocationClass;

        FishType(List<String> fishName, int reqFishingLevel, boolean members,  Class<? extends Enum> fishLocationClass) {
            this.fishName = fishName;
            this.reqFishingLevel = reqFishingLevel;
            this.members = members;
            this.fishLocationClass = fishLocationClass;
        }
    }

    public static FishType chooseFishType() {
        int fishingLevel = Skills.getLevel(Skill.FISHING);

        for (int i = FishType.values().length - 1; i >= 0; i--) {
            FishType fishType = FishType.values()[i];
            if (fishingLevel >= FishType.SMALLNET.getReqFishingLevel()) {
                return fishType;
            }
        }

        return null;
    }


    public static GameObject getFurtherFish() {
        FishType chosenFishType = chooseFishType();

        if (chosenFishType != null) {
            List<String> fishNames = chosenFishType.getFishName();
            debug("Selected Fish Names: " + fishNames);

            GameObject closestDistantFish = null;
            double closestDistance = Double.MAX_VALUE;
            double minDistance = 3; // Minimum distance to consider

            for (String fishName : fishNames) {

                for (TileObject fishObject : TileObjects.getSurrounding(Players.getLocal().getWorldLocation(), 12, fishName)) {
                    if (fishObject instanceof GameObject) {
                        double distance = fishObject.distanceTo(localPlayer);

                        if (distance >= minDistance && distance < closestDistance) {
                            closestDistantFish = (GameObject) fishObject;
                            closestDistance = distance;
                        }
                    }
                }
            }
            debug("Further Fish Location: " + closestDistantFish.getWorldLocation().toString());
            return closestDistantFish;
        } else {
            debug("No suitable Fish type found.");
            return null;
        }
    }

    public static GameObject getFish() {
        FishType chosenFishType = chooseFishType();
        if (chosenFishType != null) {
            List<String> fishNames = chosenFishType.getFishName();

            GameObject closestFish = null;
            double closestDistance = Double.MAX_VALUE;

            for (String fishName : fishNames) {
                TileObject fishObject = TileObjects.getNearest(LocalPlayer.getPosition(), fishName);
                if (fishObject instanceof GameObject) {
                    double distance = fishObject.distanceTo(localPlayer);

                    if (distance < closestDistance) {
                        closestFish = (GameObject) fishObject;
                        closestDistance = distance;
                    }
                }
            }



            return closestFish;
        } else {
            debug("No suitable Fish type found.");
            return null;
        }
    }

    public static Area getFishLocation() {
        FishType location = chooseFishType();
        if (location != null) {
            Class<? extends Enum> locationClass = location.getFishLocationClass();
            return getClosestFishArea(locationClass);
        } else {
            return null;
        }
    }
}
