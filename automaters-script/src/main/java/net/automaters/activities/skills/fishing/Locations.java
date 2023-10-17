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
                    if (location.getMinFishingLevel() <= playerCombatLevel) {
                        allAreas[i] = location.getSmallnet();
                    }
                }
            }
//            else if (allOres[i] instanceof Iron) {
//                Iron location = (Iron) allOres[i];
//                if ((location.isMembersArea() && isInMembersWorld) || (!location.isMembersArea() && !isInMembersWorld)) {
//                    if (location.getMinCombatLevel() <= playerCombatLevel) {
//                        allAreas[i] = location.getIron();
//                    }
//                }
//            } else if (allOres[i] instanceof Coal) {
//                Coal location = (Coal) allOres[i];
//                if ((location.isMembersArea() && isInMembersWorld) || (!location.isMembersArea() && !isInMembersWorld)) {
//                    if (location.getMinCombatLevel() <= playerCombatLevel) {
//                        allAreas[i] = location.getCoal();
//                    }
//                }
//            }
        }

        return getClosestArea(allAreas);
    }

    @Getter
    public enum Smallnet {
        // Hosidius Ore Areas
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

        private final int minFishingLevel;
        private final boolean membersArea;
        private final Area smallnet;

        Smallnet(int minFishingLevel, final boolean membersArea, final Area smallnet) {
            this.minFishingLevel = minFishingLevel;
            this.membersArea = membersArea;
            this.smallnet = smallnet;
        }

    }
//
//    @Getter
//    public enum Iron {
//
//        // Hosidius Ore Areas
//        HOSIDIUS_ORE_AREA_I  (3, true, new Area(1780, 3495, 1783, 3493, 0)),
//        HOSIDIUS_ORE_AREA_II (3, true, new Area(1769, 3491, 1771, 3489, 0)),
//        HOSIDIUS_ORE_AREA_III(3, true, new Area(1599, 3642, 1602, 3639, 0)),
//        HOSIDIUS_ORE_AREA_IV (3, true, new Area(1760, 3719, 1762, 3715, 0)),
//        HOSIDIUS_ORE_AREA_V  (3, true, new Area(1764, 3718, 1766, 3712, 0)),
//
//        // Lumbridge Ore Areas
//        LUMBRIDGE_ORE_AREA_I  (29, false, new Area(3302, 3284, 3303, 3285, 0)),
//        LUMBRIDGE_ORE_AREA_II (29, false, new Area(3304, 3302, 3305, 3301, 0)),
//        LUMBRIDGE_ORE_AREA_III(3, false, new Area(3033, 9826, 3032, 9825, 0)),
//        LUMBRIDGE_ORE_AREA_IV (13, false, new Area(3175, 3366, 3175, 3368, 0)),
//        LUMBRIDGE_ORE_AREA_V  (13, false, new Area(3284, 3370, 3288, 3368, 0)),
//
//        // Draynor Village Ore Areas
//        DRAYNORVILLAGE_ORE_AREA_I  (3, false, new Area(3034, 9737, 3032, 9739, 0)),
//        DRAYNORVILLAGE_ORE_AREA_II (29, false, new Area(3294, 3309, 3295, 3311, 0)),
//        DRAYNORVILLAGE_ORE_AREA_III(3, false, new Area(2967, 3238, 2970, 3243, 0)),
//        DRAYNORVILLAGE_ORE_AREA_IV (3, false, new Area(2979, 3236, 2984, 3231, 0)),
//
//        // Falador Ore Areas
//        FALADOR_ORE_AREA_I  (3, true, new Area(3022, 9722, 3020, 9720, 0)),
//        FALADOR_ORE_AREA_III(3, true, new Area(2905, 3364, 2911, 3358, 0)),
//
//        // Camelot Ore Areas
//        CAMELOT_ORE_AREA_I  (3, true, new Area(2601, 3238, 2605, 3230, 0)),
//        CAMELOT_ORE_AREA_II (3, true, new Area(2623, 3152, 2627, 3148, 0)),
//        CAMELOT_ORE_AREA_III(3, true, new Area(2625, 3143, 2629, 3139, 0)),
//        CAMELOT_ORE_AREA_IV (43, true, new Area(2691, 3330, 2694, 3328, 0)),
//        CAMELOT_ORE_AREA_V  (43, true, new Area(2711, 3331, 2713, 3329, 0)),
//        ;
//
//        private final int minCombatLevel;
//        private final boolean membersArea;
//        private final Area iron;
//
//        Iron(int minCombatLevel, final boolean membersArea, final Area iron) {
//            this.minCombatLevel = minCombatLevel;
//            this.membersArea = membersArea;
//            this.iron = iron;
//        }
//
//    }
//
//    @Getter
//    public enum Coal {
//        // Hosidius Ore Areas
//        HOSIDIUS_ORE_AREA_I  (3, false, new Area(1796, 3531, 1799, 3523, 0)),
//        HOSIDIUS_ORE_AREA_II (3, false, new Area(1745, 3663, 1749, 3659, 0)),
//        HOSIDIUS_ORE_AREA_III(3, false, new Area(1735, 3660, 1737, 3655, 0)),
//        HOSIDIUS_ORE_AREA_IV (3, false, new Area(1711, 3689, 1715, 3679, 0)),
//        HOSIDIUS_ORE_AREA_V  (3, false, new Area(1818, 3530, 1821, 3524, 0)),
//
//        // Lumbridge Ore Areas
//        LUMBRIDGE_ORE_AREA_I  (3, false, new Area(3161, 3272, 3164, 3266, 0)),
//        LUMBRIDGE_ORE_AREA_II (3, false, new Area(3232, 3246, 3235, 3243, 0)),
//        LUMBRIDGE_ORE_AREA_III(3, false, new Area(3219, 3310, 3223, 3300, 0)),
//        LUMBRIDGE_ORE_AREA_IV (3, false, new Area(3176, 3276, 3180, 3268, 0)),
//        LUMBRIDGE_ORE_AREA_V  (3, false, new Area(3163, 3275, 3170, 3271, 0)),
//
//        // Draynor Village Ore Areas
//        DRAYNORVILLAGE_ORE_AREA_I  (3, false, new Area(2960, 3198, 2966, 3193, 0)),
//        DRAYNORVILLAGE_ORE_AREA_II (3, false, new Area(2985, 3191, 2993, 3182, 0)),
//        DRAYNORVILLAGE_ORE_AREA_III(3, false, new Area(2994, 3171, 3000, 3163, 0)),
//        DRAYNORVILLAGE_ORE_AREA_IV (3, false, new Area(3026, 3178, 3031, 3166, 0)),
//        DRAYNORVILLAGE_ORE_AREA_V  (3, false, new Area(3056, 3256, 3064, 3251, 0)),
//
//        // Falador Ore Areas
//        FALADOR_ORE_AREA_I  (3, false, new Area(2914, 3302, 2916, 3301, 0)),
//        FALADOR_ORE_AREA_II (3, false, new Area(2918, 3298, 2919, 3296, 0)),
//        FALADOR_ORE_AREA_III(3, false, new Area(2892, 3366, 2895, 3365, 0)),
//        FALADOR_ORE_AREA_IV (3, false, new Area(2899, 3402, 2902, 3401, 0)),
//        FALADOR_ORE_AREA_V  (3, false, new Area(2923, 3405, 2924, 3403, 0)),
//
//        // Varrock Ore Areas
//        VARROCK_ORE_AREA_I  (3, false, new Area(3168, 3422, 3170, 3420, 0)),
//        VARROCK_ORE_AREA_II (3, false, new Area(3192, 3463, 3196, 3458, 0)),
//        VARROCK_ORE_AREA_III(3, false, new Area(3278, 3432, 3283, 3424, 0)),
//        VARROCK_ORE_AREA_IV (3, false, new Area(3199, 3372, 3209, 3364, 0)),
//        VARROCK_ORE_AREA_V  (3, false, new Area(3307, 3462, 3311, 3458, 0)),
//
//        // Edgeville Ore Areas
//        EDGEVILLE_ORE_AREA_I  (3, false, new Area(3168, 3422, 3170, 3420, 0)),
//        EDGEVILLE_ORE_AREA_II (3, false, new Area(3192, 3463, 3196, 3458, 0)),
//        EDGEVILLE_ORE_AREA_III(3, false, new Area(3278, 3432, 3283, 3424, 0)),
//        EDGEVILLE_ORE_AREA_IV (3, false, new Area(3199, 3372, 3209, 3364, 0)),
//        EDGEVILLE_ORE_AREA_V  (3, false, new Area(3307, 3462, 3311, 3458, 0)),
//
//        // Camelot Ore Areas
//        CAMELOT_ORE_AREA_I  (3, false, new Area(2767, 3472, 2766, 3464, 0)),
//        CAMELOT_ORE_AREA_II (3, false, new Area(2785, 3443, 2790, 3434, 0)),
//        CAMELOT_ORE_AREA_III(3, false, new Area(2728, 3449, 2723, 3453, 0)),
//        CAMELOT_ORE_AREA_IV (3, false, new Area(2703, 3483, 2698, 3480, 0)),
//        CAMELOT_ORE_AREA_V  (3, false, new Area(2658, 3391, 2652, 3402, 0)),
//
//        // Ardougne Ore Areas
//        ARDOUGNE_ORE_AREA_I  (3, false, new Area(2657, 3324, 2660, 3328, 0)),
//        ARDOUGNE_ORE_AREA_II (3, false, new Area(2684, 3289, 2681, 3294, 0)),
//        ARDOUGNE_ORE_AREA_III(3, false, new Area(2635, 3255, 2629, 3248, 0)),
//        ARDOUGNE_ORE_AREA_IV (3, false, new Area(2611, 3252, 2604, 3241, 0)),
//        ARDOUGNE_ORE_AREA_V  (3, false, new Area(2585, 3281, 2581, 3273, 0)),
//
//        // Yanille Ore Areas
//        YANILLE_ORE_AREA_I  (3, false, new Area(2628, 3198, 2623, 3201, 0)),
//        YANILLE_ORE_AREA_II (3, false, new Area(2625, 3110, 2628, 3104, 0)),
//        YANILLE_ORE_AREA_III(3, false, new Area(2597, 3068, 2590, 3062, 0)),
//        YANILLE_ORE_AREA_IV (3, false, new Area(2531, 3113, 2525, 3118, 0)),
//        YANILLE_ORE_AREA_V  (3, false, new Area(2481, 3120, 2477, 3116, 0)),
//
//        // BarbarianOutpost Ore Areas
//        BARBARIANOUTPOST_ORE_AREA_I  (3, false, new Area(2521, 3560, 2516, 3557, 0)),
//        BARBARIANOUTPOST_ORE_AREA_II (3, false, new Area(2521, 3560, 2516, 3557, 0)),
//        BARBARIANOUTPOST_ORE_AREA_III(3, false, new Area(2521, 3560, 2516, 3557, 0)),
//        BARBARIANOUTPOST_ORE_AREA_IV (3, false, new Area(2521, 3560, 2516, 3557, 0)),
//        BARBARIANOUTPOST_ORE_AREA_V  (3, false, new Area(2521, 3560, 2516, 3557, 0)),
//
//        // GnomeStronghold Ore Areas
//        GNOMESTRONGHOLD_ORE_AREA_I  (3, false, new Area(2412, 3502, 2415, 3508, 0)),
//        GNOMESTRONGHOLD_ORE_AREA_II (3, false, new Area(2422, 3458, 2417, 3454, 0)),
//        GNOMESTRONGHOLD_ORE_AREA_III(3, false, new Area(2419, 3426, 2416, 3422, 0)),
//        GNOMESTRONGHOLD_ORE_AREA_IV (3, false, new Area(2452, 3414, 2448, 3408, 0)),
//        GNOMESTRONGHOLD_ORE_AREA_V  (3, false, new Area(2471, 3405, 2475, 3402, 0));
//        ;
//
//        private final int minCombatLevel;
//        private final boolean membersArea;
//        private final Area coal;
//
//        Coal(int minCombatLevel, final boolean membersArea, final Area coal) {
//            this.minCombatLevel = minCombatLevel;
//            this.membersArea = membersArea;
//            this.coal = coal;
//        }
//    }
}
