package net.automaters.activities.skills.fishing;

import lombok.Getter;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.walking.Area;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
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
        SMALLNET(Arrays.asList("Fishing spot"),  1, false, Locations.Smallnet.class),
        FLYFISH(Arrays.asList("Rod Fishing spot"), 21, false, Locations.FlyFish.class),
        LOBSTER(Arrays.asList("Cage"), 41, false, Locations.Lobsters.class),
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
            if (fishingLevel >= fishType.reqFishingLevel) {
                debug("fish type: "+fishType);
                return fishType;
            }
        }

        return null;
    }


    public static NPC getFurtherFish() {
        FishType chosenFishType = chooseFishType();

        if (chosenFishType != null) {
            List<String> fishNames = chosenFishType.getFishName();
            debug("Selected Fish Names: " + fishNames);

            NPC closestDistantFish = null;
            double closestDistance = Double.MAX_VALUE;
            double minDistance = 3; // Minimum distance to consider

            for (String fishName : fishNames) {

                for (TileObject fishObject : TileObjects.getSurrounding(Players.getLocal().getWorldLocation(), 12, fishName)) {
                    if (fishObject instanceof NPC) {
                        double distance = fishObject.distanceTo(localPlayer);

                        if (distance >= minDistance && distance < closestDistance) {
                            closestDistantFish = (NPC) fishObject;
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

    public static NPC getFish() {
        FishType chosenFishType = chooseFishType();
        if (chosenFishType != null) {
            List<String> fishNames = chosenFishType.getFishName();

            debug("fishNames: "+fishNames);
            NPC closestFish = null;
            double closestDistance = Double.MAX_VALUE;

            for (String fishName : fishNames) {
                debug("1 - "+fishName);
                TileObject fishObject = TileObjects.getNearest(LocalPlayer.getPosition(), fishName);
                if (fishObject instanceof NPC) {
                    debug("2");
                    double distance = fishObject.distanceTo(localPlayer);

                    if (distance < closestDistance) {
                        debug("3");
                        closestFish = (NPC) fishObject;
                        closestDistance = distance;
                        debug("closestFish: "+fishObject.getWorldLocation());
                    }
                    debug("4");
                }

                if (fishObject instanceof GameObject) {
                    debug("21");
                    double distance = fishObject.distanceTo(localPlayer);

                    if (distance < closestDistance) {
                        debug("31");
                        closestFish = (NPC) fishObject;
                        closestDistance = distance;
                        debug("closestFish: "+fishObject.getWorldLocation());
                    }
                    debug("41");
                }
                debug("5");
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
