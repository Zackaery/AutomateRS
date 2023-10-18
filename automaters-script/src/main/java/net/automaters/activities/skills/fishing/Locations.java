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
        CATHERBY_FISHAREA_I (1, true, new Area(2834, 3434, 2846, 3428,0)),
        LUMBRIDGE_FISHAREA_I (1, false, new Area(3242, 3161, 3247, 3149,0)),
        ALKHARID_FISHAREA_I (1, false, new Area(3264, 3151, 3279, 3137,0)),
        PORTPISC_FISHAREA_I (1, true, new Area(1758, 3798, 1767, 3792,0)),
        HOSIDIUS_FISHAREA_I (1, true, new Area(1814, 3609, 1821, 3596,0)),
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
        MUSAPOINT_FISHAREA (1, true, new Area(2922, 3182, 2927, 3178,0)),
        LANDSEND_FISHAREA (1, true, new Area(1483, 3434, 1489, 3429,0)),
        PORTPISC_FISHAREA (1, true, new Area(1743, 3803, 1751, 3798,0)),
        CATHERBY_FISHAREA_I (1, true, new Area(2843, 3432, 2848, 3428,0)),
        CATHERBY_FISHAREA_II (1, true, new Area(2851, 3426, 2856, 3423,0)),

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
        LUMBRIDGE_FISHAREA (1, false, new Area(3236,3257,3244,3236,0)),
        BARBVILLAGE_FISHAREA (1, false, new Area(3100,3438,3111,3420,0)),
        SEERSVILLAGE_FISHAREA (1, false, new Area(2712,3533,2729,3522,0)),
        KOUREND_FISHAREA (1, false, new Area(1719,3687,1722,3682,0)),
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
