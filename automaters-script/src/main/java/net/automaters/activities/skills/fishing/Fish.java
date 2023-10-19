package net.automaters.activities.skills.fishing;

import lombok.Getter;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.walking.Area;
import net.runelite.api.*;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Skills;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.automaters.activities.skills.fishing.Locations.getClosestFishArea;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.utils.Debug.debug;

public class Fish {

    @Getter
    public enum FishType {
        SMALL_NET(
                List.of("Fishing spot"),
                Arrays.asList("Net", "Small Net"),
                -1,
                1,
                false,
                Locations.SmallNet.class),
        ROD_FISH(
                Arrays.asList("Fishing spot", "Rod Fishing spot"),
                List.of("Bait"),
                ItemID.FISHING_BAIT,
                80,
                false,
                Locations.RodFish.class),
        FLY_FISH(
                List.of("Rod Fishing spot"),
                List.of("Lure"),
                ItemID.FEATHER,
                90,
                false,
                Locations.FlyFish.class),
        LOBSTER(
                List.of("Fishing spot"),
                List.of("Cage"),
                -1,
                99,
                false,
                Locations.Lobsters.class),
        ;

        private final List<String> fishName;
        private final List<String> action;
        private final int secondaryItem;
        private final int reqFishingLevel;
        private final boolean members;
        private final Class<? extends Enum> fishLocationClass;

        FishType(List<String> fishName, List<String> action, int secondaryItem, int reqFishingLevel, boolean members,  Class<? extends Enum> fishLocationClass) {
            this.fishName = fishName;
            this.action = action;
            this.secondaryItem = secondaryItem;
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
                return fishType;
            }
        }

        return null;
    }

    public static int getSecondaryItem() {
        FishType chosenFishType = chooseFishType();
        if (chosenFishType == null) {
            debug("No suitable secondary item found.");
            return -1;
        }
        return chosenFishType.getSecondaryItem();
    }

    public static String getAction() {
        FishType chosenFishType = chooseFishType();
        NPC fishingSpot = getFish();
        if (fishingSpot == null || chosenFishType == null) {
            debug("No suitable Fishing spots found.");
            return null;
        }

        List<String> interactions = chosenFishType.getAction();
        String[] actions = fishingSpot.getActions();

        for (String interaction : interactions) {
            for (String action : actions) {
                if (action != null && action.equals(interaction)) {
                    return action;
                }
            }
        }
        return null;
    }

    public static NPC getFish() {
        FishType chosenFishType = chooseFishType();
        if (chosenFishType == null) {
            debug("No suitable Fishing spots found.");
            return null;
        }

        List<String> fishNames = chosenFishType.getFishName();

        NPC closestFish = null;
        double closestDistance = Double.MAX_VALUE;
        for (String fishName : fishNames) {
            NPC fishObject = NPCs.getNearest(LocalPlayer.getPosition(), fishName);
            if (fishObject != null) {
                double distance = fishObject.distanceTo(localPlayer);
                if (distance < closestDistance) {
                    closestFish = fishObject;
                    closestDistance = distance;
                }
            }
        }

        if (closestFish != null) {
            return closestFish;
        } else {
            debug("No suitable Fishing spots found.");
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
