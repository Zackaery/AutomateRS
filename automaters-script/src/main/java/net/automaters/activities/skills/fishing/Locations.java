package net.automaters.activities.skills.fishing;

import lombok.Getter;
import net.automaters.api.walking.Area;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Worlds;

import static net.automaters.api.entities.LocalPlayer.getClosestArea;

public class Locations {

    public static <T extends Enum<T>> Area getClosestFishArea(Class<T> enumClass) {
        if (enumClass == null) {
            return null;
        }

        T[] allFish = enumClass.getEnumConstants();
        Area[] allAreas = new Area[allFish.length];
        boolean isInMembersWorld = Worlds.inMembersWorld();
        int playerCombatLevel = Players.getLocal().getCombatLevel();

        for (int i = 0; i < allFish.length; i++) {
            if (allFish[i] instanceof Smallnet) {
                Smallnet location = (Smallnet) allFish[i];
                if ((location.isMembersArea() && isInMembersWorld) || (!location.isMembersArea() && !isInMembersWorld)) {
                    if (location.getMinCombatLevel() <= playerCombatLevel) {
                        allAreas[i] = location.getSmallnet();
                    }
                }
            }
            else if (allFish[i] instanceof FlyFish) {
                FlyFish location = (FlyFish) allFish[i];
                if ((location.isMembersArea() && isInMembersWorld) || (!location.isMembersArea() && !isInMembersWorld)) {
                    if (location.getMinCombatLevel() <= playerCombatLevel) {
                        allAreas[i] = location.getFlyfish();
                    }
                }
            } else if (allFish[i] instanceof Lobsters) {
                Lobsters location = (Lobsters) allFish[i];
                if ((location.isMembersArea() && isInMembersWorld) || (!location.isMembersArea() && !isInMembersWorld)) {
                    if (location.getMinCombatLevel() <= playerCombatLevel) {
                        allAreas[i] = location.getLobster();
                    }
                }
            }
        }

        return getClosestArea(allAreas);
    }

    @Getter
    public enum Smallnet {
        // Small net areas
        Catherby_FishArea_I  (1, true, new Area(2834, 3434, 2846, 3428,0)),
        Catherby_FishArea_I_Walk  (1, true, new Area(2835, 3433, 2836, 3432,0)),
        Lumbridge_FishArea_I  (1, true, new Area(3242, 3161, 3247, 3149,0)),
        Lumbridge_FishArea_I_Walk  (1, true, new Area(3243, 3154, 3244, 3154,0)),
        AlKharid_FishArea_I  (1, true, new Area(3264, 3151, 3279, 3137,0)),
        AlKharid_FishArea_I_Walk  (1, true, new Area(3269, 3148, 3269, 3149,0)),
        PortPisc_FishArea_I  (1, true, new Area(1758, 3798, 1767, 3792,0)),
        PortPisc_FishArea_I_Walk  (1, true, new Area(1763, 3795, 1763, 3794,0)),
        Hosidius_FishArea_I  (1, true, new Area(1814, 3609, 1821, 3596,0)),
        Hosidius_FishArea_I_Walk  (1, true, new Area(1816, 3603, 1818, 3603,0)),

        ;

        private final int minCombatLevel;
        private final boolean membersArea;
        private final Area smallnet;

        Smallnet(int minCombatLevel, final boolean membersArea, final Area smallnet) {
            this.minCombatLevel = minCombatLevel;
            this.membersArea = membersArea;
            this.smallnet = smallnet;
        }

    }

    @Getter
    public enum Lobsters {

        // Hosidius Ore Areas
        MusaPoint_FishArea  (1, true, new Area(2922, 3182, 2927, 3178,0)),
        MusaPoint_FishArea_Walk  (1, true, new Area(2924, 3180, 2925, 3179,0)),
        LandsEnd_FishArea  (1, true, new Area(1483, 3434, 1489, 3429,0)),
        LandsEnd_FishArea_Walk  (1, true, new Area(1486, 3432, 1487, 3432,0)),
        PortPisc_FishArea  (1, true, new Area(1743, 3803, 1751, 3798,0)),
        PortPisc_FishArea_Walk  (1, true, new Area(1746, 3801, 1746, 3800,0)),
        Catherby_FishArea_I  (1, true, new Area(2843, 3432, 2848, 3428,0)),
        Catherby_FishArea_II  (1, true, new Area(2851, 3426, 2856, 3423,0)),
        Catherby_FishArea_I_Walk  (1, true, new Area(2845, 3431, 2845, 3430,0)),
        Catherby_FishArea_II_Walk  (1, true, new Area(2853, 3425, 2853, 3424,0)),

        ;

        private final int minCombatLevel;
        private final boolean membersArea;
        private final Area lobster;

        Lobsters(int minCombatLevel, final boolean membersArea, final Area lobster) {
            this.minCombatLevel = minCombatLevel;
            this.membersArea = membersArea;
            this.lobster = lobster;
        }

    }

    @Getter
    public enum FlyFish {
        // Hosidius Ore Areas
        Lumbridge_FishArea  (1, false, new Area(3236,3257,3244,3236,0)),
        Lumbridge_FishArea_Walk  (1, false, new Area(3240,3252,3241,3252,0)),
        BarbVillage_FishArea  (1, false, new Area(3100,3438,3111,3420,0)),
        BarbVillage_FishArea_Walk  (1, false, new Area(3104,3433,3105,3433,0)),
        SeersVillage_FishArea  (1, false, new Area(2712,3533,2729,3522,0)),
        SeersVillage_FishArea_Walk  (1, false, new Area(2725,3527,2727,3526,0)),
        Kourend_FishArea  (1, false, new Area(1719,3687,1722,3682,0)),
        Kourend_FishArea_Walk  (1, false, new Area(1720,3685,1721,3684,0)),

        ;

        private final int minCombatLevel;
        private final boolean membersArea;
        private final Area flyfish;

        FlyFish(int minCombatLevel, final boolean membersArea, final Area flyfish) {
            this.minCombatLevel = minCombatLevel;
            this.membersArea = membersArea;
            this.flyfish = flyfish;
        }
    }
}
