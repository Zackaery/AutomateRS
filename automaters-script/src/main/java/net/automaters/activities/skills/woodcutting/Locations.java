package net.automaters.activities.skills.woodcutting;

import lombok.Getter;
import net.automaters.api.walking.Area;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Worlds;

import static net.automaters.api.entities.LocalPlayer.getClosestArea;
import static net.automaters.api.utils.Debug.debug;

public class Locations {

    public static <T extends Enum<T>> Area getClosestTreeArea(Class<T> enumClass) {
        if (enumClass == null) {
            return null;
        }

        T[] allTrees = enumClass.getEnumConstants();
        Area[] allAreas = new Area[allTrees.length];
        boolean isInMembersWorld = Worlds.inMembersWorld();
        int playerCombatLevel = Players.getLocal().getCombatLevel();

        for (int i = 0; i < allTrees.length; i++) {
            if (allTrees[i] instanceof Tree) {
                Tree location = (Tree) allTrees[i];
                if ((location.isMembersArea() && isInMembersWorld) || (!location.isMembersArea() && !isInMembersWorld)) {
                    if (location.getMinCombatLevel() <= playerCombatLevel) {
                        allAreas[i] = location.getTree();
                    }
                }
            } else if (allTrees[i] instanceof Oak) {
                Oak location = (Oak) allTrees[i];
                if ((location.isMembersArea() && isInMembersWorld) || (!location.isMembersArea() && !isInMembersWorld)) {
                    if (location.getMinCombatLevel() <= playerCombatLevel) {
                        allAreas[i] = location.getOak();
                    }
                }
            } else if (allTrees[i] instanceof Willow) {
                Willow location = (Willow) allTrees[i];
                if ((location.isMembersArea() && isInMembersWorld) || (!location.isMembersArea() && !isInMembersWorld)) {
                    if (location.getMinCombatLevel() <= playerCombatLevel) {
                        allAreas[i] = location.getWillow();
                    }
                }
            } else if (allTrees[i] instanceof Teak) {
                Teak location = (Teak) allTrees[i];
                if ((location.isMembersArea() && isInMembersWorld) || (!location.isMembersArea() && !isInMembersWorld)) {
                    if (location.getMinCombatLevel() <= playerCombatLevel) {
                        allAreas[i] = location.getTeak();
                    }
                }
            } else if (allTrees[i] instanceof Maple) {
                Maple location = (Maple) allTrees[i];
                if ((location.isMembersArea() && isInMembersWorld) || (!location.isMembersArea() && !isInMembersWorld)) {
                    if (location.getMinCombatLevel() <= playerCombatLevel) {
                        allAreas[i] = location.getMaple();
                    }
                }
            } else if (allTrees[i] instanceof Yew) {
                Yew location = (Yew) allTrees[i];
                if ((location.isMembersArea() && isInMembersWorld) || (!location.isMembersArea() && !isInMembersWorld)) {
                    if (location.getMinCombatLevel() <= playerCombatLevel) {
                        allAreas[i] = location.getYew();
                    }
                }
            }
        }

        return getClosestArea(allAreas);
    }

    @Getter
    public enum Tree {
        // Hosidius Tree Areas
        HOSIDIUS_TREE_AREA_I  (3, true, new Area(1727, 3598, 1732, 3592, 0)),
        HOSIDIUS_TREE_AREA_II (3, true, new Area(1730, 3633, 1737, 3628, 0)),
        HOSIDIUS_TREE_AREA_III(3, true, new Area(1699, 3610, 1705, 3602, 0)),
        HOSIDIUS_TREE_AREA_IV (3, true, new Area(1718, 3493, 1725, 3483, 0)),
        HOSIDIUS_TREE_AREA_V  (3, true, new Area(1718, 3589, 1726, 3583, 0)),

        // Lumbridge Tree Areas
        LUMBRIDGE_TREE_AREA_I  (13, false, new Area(3189, 3221, 3191, 3217, 0)),
        LUMBRIDGE_TREE_AREA_II (3, false, new Area(3256, 3253, 3263, 3245, 0)),
        LUMBRIDGE_TREE_AREA_III(3, false, new Area(3176, 3260, 3183, 3256, 0)),
        LUMBRIDGE_TREE_AREA_IV (13, false, new Area(3163, 3216, 3165, 3213, 0)),
        LUMBRIDGE_TREE_AREA_V  (3, false, new Area(3242, 3261, 3249, 3251, 0)),

        // Draynor Village Tree Areas
        DRAYNORVILLAGE_TREE_AREA_I  (3, false, new Area(3076, 3270, 3079, 3266, 0)),
        DRAYNORVILLAGE_TREE_AREA_II (53, false, new Area(3125, 3213, 3133, 3211, 0)),
        DRAYNORVILLAGE_TREE_AREA_III(3, false, new Area(3079, 3302, 3083, 3296, 0)),
        DRAYNORVILLAGE_TREE_AREA_IV (11, false, new Area(3036, 3268, 3041, 3262, 0)),
        DRAYNORVILLAGE_TREE_AREA_V  (3, false, new Area(2993, 3209, 3001, 3215, 0)),

        // Falador Tree Areas
        FALADOR_TREE_AREA_I  (3, false, new Area(2987, 3302, 2997, 3296, 0)),
        FALADOR_TREE_AREA_II (3, false, new Area(2948, 3409, 2953, 3403, 0)),
        FALADOR_TREE_AREA_III(3, false, new Area(2973, 3450, 2979, 3443, 0)),
        FALADOR_TREE_AREA_IV (3, false, new Area(3030, 3323, 3038, 3319, 0)),
        FALADOR_TREE_AREA_V  (3, false, new Area(2977, 3497, 2981, 3491, 0)),

        // Varrock Tree Areas
        VARROCK_TREE_AREA_I  (3, false, new Area(3165, 3410, 3169, 3404, 0)),
        VARROCK_TREE_AREA_II (3, false, new Area(3153, 3459, 3161, 3455, 0)),
        VARROCK_TREE_AREA_III(3, false, new Area(3161, 3397, 3169, 3385, 0)),
        VARROCK_TREE_AREA_IV (3, false, new Area(3276, 3453, 3282, 3446, 0)),
        VARROCK_TREE_AREA_V  (47, false, new Area(3198, 3369, 3194, 3372, 0)),

        // Edgeville Tree Areas
        EDGEVILLE_TREE_AREA_I  (3, false, new Area(3041, 3462, 3047, 3457, 0)),
        EDGEVILLE_TREE_AREA_II (3, false, new Area(3044, 3441, 3049, 3432, 0)),
        EDGEVILLE_TREE_AREA_III(3, false, new Area(3116, 3505, 3120, 3501, 0)),
        EDGEVILLE_TREE_AREA_IV (3, false, new Area(3090, 3447, 3095, 3442, 0)),
        EDGEVILLE_TREE_AREA_V  (3, false, new Area(3121, 3431, 3127, 3426, 0)),

        // Camelot Tree Areas
        CAMELOT_TREE_AREA_I  (3, true, new Area(2759, 3465, 2766, 3458, 0)),
        CAMELOT_TREE_AREA_II (3, true, new Area(2717, 3443, 2725, 3433, 0)),
        CAMELOT_TREE_AREA_III(3, true, new Area(2668, 3464, 2678, 3455, 0)),
        CAMELOT_TREE_AREA_IV (55, true, new Area(2723, 3405, 2733, 3396, 0)),
        CAMELOT_TREE_AREA_V  (3, true, new Area(2650, 3411, 2659, 3404, 0)),

        // Ardougne Tree Areas
        ARDOUGNE_TREE_AREA_I  (3, true, new Area(2691, 3312, 2699, 3308, 0)),
        ARDOUGNE_TREE_AREA_II (3, true, new Area(2617, 3252, 2629, 3247, 0)),
        ARDOUGNE_TREE_AREA_III(3, true, new Area(2603, 3322, 2607, 3314, 0)),
        ARDOUGNE_TREE_AREA_IV (3, true, new Area(2628, 3335, 2636, 3334, 0)),
        ARDOUGNE_TREE_AREA_V  (3, true, new Area(2658, 3330, 2651, 3327, 0)),

        // Yanille Tree Areas
        YANILLE_TREE_AREA_I  (3, true, new Area(2617, 3127, 2623, 3121, 0)),
        YANILLE_TREE_AREA_II (3, true, new Area(2590, 3126, 2595, 3122, 0)),
        YANILLE_TREE_AREA_III(3, true, new Area(2593, 3067, 2600, 3060, 0)),
        YANILLE_TREE_AREA_IV (3, true, new Area(2525, 3118, 2529, 3111, 0)),
        YANILLE_TREE_AREA_V  (3, true, new Area(2483, 3110, 2488, 3097, 0)),

        // Barbarian Outpost Tree Areas
        BARBARIANOUTPOST_TREE_AREA_I  (3, false, new Area(2520, 3584, 2524, 3581, 0)),
        BARBARIANOUTPOST_TREE_AREA_II (3, false, new Area(2513, 3552, 2517, 3546, 0)),
        BARBARIANOUTPOST_TREE_AREA_III(3, false, new Area(2494, 3534, 2498, 3530, 0)),
        BARBARIANOUTPOST_TREE_AREA_IV (3, false, new Area(2545, 3580, 2548, 3575, 0)),
        BARBARIANOUTPOST_TREE_AREA_V  (3, false, new Area(2529, 3490, 2533, 3484, 0)),

        // Gnome Stronghold Tree Areas
        GNOMESTRONGHOLD_TREE_AREA_I  (3, true, new Area(2396, 3466, 2410, 3470, 0)),
        GNOMESTRONGHOLD_TREE_AREA_II (3, true, new Area(2452, 3439, 2456, 3426, 0)),
        GNOMESTRONGHOLD_TREE_AREA_III(3, true, new Area(2403, 3430, 2415, 3425, 0)),
        GNOMESTRONGHOLD_TREE_AREA_IV (3, true, new Area(2448, 3408, 2451, 3400, 0)),
        GNOMESTRONGHOLD_TREE_AREA_V  (3, true, new Area(2481, 3500, 2487, 3492, 0)),

        ;

        private final int minCombatLevel;
        private final boolean membersArea;
        private final Area tree;

        Tree(int minCombatLevel, final boolean membersArea, final Area tree) {
            this.minCombatLevel = minCombatLevel;
            this.membersArea = membersArea;
            this.tree = tree;
        }

    }

    @Getter
    public enum Oak {

        // Hosidius Tree Areas
        HOSIDIUS_TREE_AREA_I  (3, true, new Area(1724, 3625, 1735, 3606, 0)),
        HOSIDIUS_TREE_AREA_II (3, true, new Area(1718, 3603, 1730, 3594, 0)),
        HOSIDIUS_TREE_AREA_III(3, true, new Area(1717, 3591, 1736, 3584, 0)),
        HOSIDIUS_TREE_AREA_IV (3, true, new Area(1700, 3617, 1709, 3592, 0)),
        HOSIDIUS_TREE_AREA_V  (3, true, new Area(1708, 3490, 1732, 3478, 0)),

        // Lumbridge Tree Areas
        LUMBRIDGE_TREE_AREA_I  (3, false, new Area(3203, 3247, 3206, 3238, 0)),
        LUMBRIDGE_TREE_AREA_II (3, false, new Area(3246, 3273, 3250, 3269, 0)),
        LUMBRIDGE_TREE_AREA_IV (3, false, new Area(3258, 3226, 3263, 3222, 0)),
        LUMBRIDGE_TREE_AREA_V  (3, false, new Area(3258, 3226, 3263, 3222, 0)),

        // Draynor Village Tree Areas
        DRAYNORVILLAGE_TREE_AREA_I  (3, false, new Area(3095, 3289, 3103, 3284, 0)),
        DRAYNORVILLAGE_TREE_AREA_II (3, false, new Area(3056, 3261, 3059, 3270, 0)),
        DRAYNORVILLAGE_TREE_AREA_III(3, false, new Area(2996, 3256, 3002, 3260, 0)),
        DRAYNORVILLAGE_TREE_AREA_IV (3, false, new Area(2959, 3235, 2954, 3229, 0)),
        DRAYNORVILLAGE_TREE_AREA_V  (3, false, new Area(2982, 3203, 2987, 3208, 0)),

        // Falador Tree Areas
        FALADOR_TREE_AREA_I  (3, false, new Area(3086, 3295, 3079, 3290, 0)),
        FALADOR_TREE_AREA_II (3, false, new Area(3005, 3309, 2999, 3316, 0)),
        FALADOR_TREE_AREA_III(3, false, new Area(2957, 3397, 2950, 3400, 0)),
        FALADOR_TREE_AREA_IV (3, false, new Area(3012, 3408, 3008, 3412, 0)),
        FALADOR_TREE_AREA_V  (3, false, new Area(2930, 3365, 2926, 3368, 0)),

        // Varrock Tree Areas
        VARROCK_TREE_AREA_I  (3, false, new Area(3172, 3424, 3163, 3415, 0)),
        VARROCK_TREE_AREA_II (47, false, new Area(3192, 3463, 3196, 3458, 0)),
        VARROCK_TREE_AREA_III(3, false, new Area(3278, 3432, 3283, 3424, 0)),
        VARROCK_TREE_AREA_IV (3, false, new Area(3199, 3372, 3209, 3364, 0)),
        VARROCK_TREE_AREA_V  (3, false, new Area(3307, 3462, 3311, 3458, 0)),

        // Camelot Tree Areas
        CAMELOT_TREE_AREA_I  (3, true, new Area(2767, 3472, 2766, 3464, 0)),
        CAMELOT_TREE_AREA_II (3, true, new Area(2785, 3443, 2790, 3434, 0)),
        CAMELOT_TREE_AREA_III(3, true, new Area(2728, 3449, 2723, 3453, 0)),
        CAMELOT_TREE_AREA_IV (3, true, new Area(2703, 3483, 2698, 3480, 0)),
        CAMELOT_TREE_AREA_V  (3, true, new Area(2658, 3391, 2652, 3402, 0)),

        // Ardougne Tree Areas
        ARDOUGNE_TREE_AREA_I  (3, true, new Area(2657, 3324, 2660, 3328, 0)),
        ARDOUGNE_TREE_AREA_II (3, true, new Area(2684, 3289, 2681, 3294, 0)),
        ARDOUGNE_TREE_AREA_III(3, true, new Area(2635, 3255, 2629, 3248, 0)),
        ARDOUGNE_TREE_AREA_IV (3, true, new Area(2611, 3252, 2604, 3241, 0)),
        ARDOUGNE_TREE_AREA_V  (3, true, new Area(2585, 3281, 2581, 3273, 0)),

        // Yanille Tree Areas
        YANILLE_TREE_AREA_I  (3, true, new Area(2628, 3198, 2623, 3201, 0)),
        YANILLE_TREE_AREA_II (3, true, new Area(2625, 3110, 2628, 3104, 0)),
        YANILLE_TREE_AREA_III(3, true, new Area(2597, 3068, 2590, 3062, 0)),
        YANILLE_TREE_AREA_IV (3, true, new Area(2531, 3113, 2525, 3118, 0)),
        YANILLE_TREE_AREA_V  (3, true, new Area(2481, 3120, 2477, 3116, 0)),

        // Barbarian Outpost Tree Areas
        BARBARIANOUTPOST_TREE_AREA_I  (3, true, new Area(2521, 3560, 2516, 3557, 0)),

        // Gnome Stronghold Tree Areas
        GNOMESTRONGHOLD_TREE_AREA_I  (3, true, new Area(2412, 3502, 2415, 3508, 0)),
        GNOMESTRONGHOLD_TREE_AREA_II (3, true, new Area(2422, 3458, 2417, 3454, 0)),
        GNOMESTRONGHOLD_TREE_AREA_III(3, true, new Area(2419, 3426, 2416, 3422, 0)),
        GNOMESTRONGHOLD_TREE_AREA_IV (3, true, new Area(2452, 3414, 2448, 3408, 0)),
        GNOMESTRONGHOLD_TREE_AREA_V  (3, true, new Area(2471, 3405, 2475, 3402, 0)),

        ;

        private final int minCombatLevel;
        private final boolean membersArea;
        private final Area oak;

        Oak(int minCombatLevel, final boolean membersArea, final Area oak) {
            this.minCombatLevel = minCombatLevel;
            this.membersArea = membersArea;
            this.oak = oak;
        }

    }

    @Getter
    public enum Willow {
        // Hosidius Tree Areas
        HOSIDIUS_TREE_AREA_I  (3, true, new Area(1796, 3531, 1799, 3523, 0)),
        HOSIDIUS_TREE_AREA_II (3, true, new Area(1745, 3663, 1749, 3659, 0)),
        HOSIDIUS_TREE_AREA_III(3, true, new Area(1735, 3660, 1737, 3655, 0)),
        HOSIDIUS_TREE_AREA_IV (3, true, new Area(1711, 3689, 1715, 3679, 0)),
        HOSIDIUS_TREE_AREA_V  (3, true, new Area(1818, 3530, 1821, 3524, 0)),

        // Lumbridge Tree Areas
        LUMBRIDGE_TREE_AREA_I  (3, false, new Area(3161, 3272, 3164, 3266, 0)),
        LUMBRIDGE_TREE_AREA_II (3, false, new Area(3232, 3246, 3235, 3243, 0)),
        LUMBRIDGE_TREE_AREA_III(3, false, new Area(3219, 3310, 3223, 3300, 0)),
        LUMBRIDGE_TREE_AREA_IV (3, false, new Area(3176, 3276, 3180, 3268, 0)),
        LUMBRIDGE_TREE_AREA_V  (3, false, new Area(3163, 3275, 3170, 3271, 0)),

        // Draynor Village Tree Areas
        DRAYNORVILLAGE_TREE_AREA_I  (3, false, new Area(2960, 3198, 2966, 3193, 0)),
        DRAYNORVILLAGE_TREE_AREA_II (3, false, new Area(2985, 3191, 2993, 3182, 0)),
        DRAYNORVILLAGE_TREE_AREA_III(3, false, new Area(2994, 3171, 3000, 3163, 0)),
        DRAYNORVILLAGE_TREE_AREA_IV (3, false, new Area(3026, 3178, 3031, 3166, 0)),
        DRAYNORVILLAGE_TREE_AREA_V  (3, false, new Area(3056, 3256, 3064, 3251, 0)),

        // Falador Tree Areas
        FALADOR_TREE_AREA_I  (3, false, new Area(2914, 3302, 2916, 3301, 0)),
        FALADOR_TREE_AREA_II (3, false, new Area(2918, 3298, 2919, 3296, 0)),
        FALADOR_TREE_AREA_III(3, true, new Area(2892, 3366, 2895, 3365, 0)),
        FALADOR_TREE_AREA_IV (3, true, new Area(2899, 3402, 2902, 3401, 0)),
        FALADOR_TREE_AREA_V  (3, true, new Area(2923, 3405, 2924, 3403, 0)),

        // Varrock Tree Areas
        VARROCK_TREE_AREA_I  (3, false, new Area(3202, 3358, 3213, 3355, 0)),
        VARROCK_TREE_AREA_II (3, false, new Area(3222, 3308, 3223, 3301, 0)),
        VARROCK_TREE_AREA_III(3, true, new Area(2923, 3407, 2927, 3407, 0)),
        VARROCK_TREE_AREA_IV (3, true, new Area(2901, 3402, 2901, 3399, 0)),
        VARROCK_TREE_AREA_V  (3, false, new Area(3164, 3273, 3162, 3272, 0)),

        // Camelot Tree Areas
        CAMELOT_TREE_AREA_I  (3, true, new Area(2767, 3472, 2766, 3464, 0)),
        CAMELOT_TREE_AREA_II (3, true, new Area(2785, 3443, 2790, 3434, 0)),
        CAMELOT_TREE_AREA_III(3, true, new Area(2728, 3449, 2723, 3453, 0)),
        CAMELOT_TREE_AREA_IV (3, true, new Area(2703, 3483, 2698, 3480, 0)),
        CAMELOT_TREE_AREA_V  (3, true, new Area(2658, 3391, 2652, 3402, 0)),

        // Ardougne Tree Areas
        ARDOUGNE_TREE_AREA_I  (3, true, new Area(2657, 3324, 2660, 3328, 0)),
        ARDOUGNE_TREE_AREA_II (3, true, new Area(2684, 3289, 2681, 3294, 0)),
        ARDOUGNE_TREE_AREA_III(3, true, new Area(2635, 3255, 2629, 3248, 0)),
        ARDOUGNE_TREE_AREA_IV (3, true, new Area(2611, 3252, 2604, 3241, 0)),
        ARDOUGNE_TREE_AREA_V  (3, true, new Area(2585, 3281, 2581, 3273, 0)),

        // Yanille Tree Areas
        YANILLE_TREE_AREA_I  (3, true, new Area(2628, 3198, 2623, 3201, 0)),
        YANILLE_TREE_AREA_II (3, true, new Area(2625, 3110, 2628, 3104, 0)),
        YANILLE_TREE_AREA_III(3, true, new Area(2597, 3068, 2590, 3062, 0)),
        YANILLE_TREE_AREA_IV (3, true, new Area(2531, 3113, 2525, 3118, 0)),
        YANILLE_TREE_AREA_V  (3, true, new Area(2481, 3120, 2477, 3116, 0)),

        // GnomeStronghold Tree Areas
        GNOMESTRONGHOLD_TREE_AREA_I  (3, true, new Area(2412, 3502, 2415, 3508, 0)),
        GNOMESTRONGHOLD_TREE_AREA_II (3, true, new Area(2422, 3458, 2417, 3454, 0)),
        GNOMESTRONGHOLD_TREE_AREA_III(3, true, new Area(2419, 3426, 2416, 3422, 0)),
        GNOMESTRONGHOLD_TREE_AREA_IV (3, true, new Area(2452, 3414, 2448, 3408, 0)),
        GNOMESTRONGHOLD_TREE_AREA_V  (3, true, new Area(2471, 3405, 2475, 3402, 0)),

        ;

        private final int minCombatLevel;
        private final boolean membersArea;
        private final Area willow;

        Willow(int minCombatLevel, final boolean membersArea, final Area willow) {
            this.minCombatLevel = minCombatLevel;
            this.membersArea = membersArea;
            this.willow = willow;
        }
    }

    @Getter
    public enum Teak {

        ;

        private final int minCombatLevel;
        private final boolean membersArea;
        private final Area teak;

        Teak(int minCombatLevel, final boolean membersArea, final Area teak) {
            this.minCombatLevel = minCombatLevel;
            this.membersArea = membersArea;
            this.teak = teak;
        }
    }

    @Getter
    public enum Maple {

        // Hosidius Tree Areas
        HOSIDIUS_TREE_AREA_I  (3, true, new Area(1686, 3553, 1691, 3547, 0)),
        HOSIDIUS_TREE_AREA_II (3, true, new Area(1636, 3597, 1642, 3591, 0)),
        HOSIDIUS_TREE_AREA_III(3, true, new Area(1648, 3597, 1657, 3592, 0)),
        HOSIDIUS_TREE_AREA_IV (3, true, new Area(1657, 3591, 1648, 3586, 0)),
        HOSIDIUS_TREE_AREA_V  (3, true, new Area(1634, 3590, 1642, 3586, 0)),

        // Camelot Tree Areas
        CAMELOT_TREE_AREA_I  (3, true, new Area(2719, 3504, 2733, 3499, 0)),
        CAMELOT_TREE_AREA_II (3, true, new Area(2673, 3540, 2680, 3533, 0)),
        CAMELOT_TREE_AREA_III(3, true, new Area(2718, 3479, 2724, 3473, 0)),
        CAMELOT_TREE_AREA_IV (3, true, new Area(2706, 3371, 2717, 3365, 0)),
        CAMELOT_TREE_AREA_V  (3, true, new Area(2721, 3567, 2725, 3555, 0)),

        ;

        private final int minCombatLevel;
        private final boolean membersArea;
        private final Area maple;

        Maple(int minCombatLevel, final boolean membersArea, final Area maple) {
            this.minCombatLevel = minCombatLevel;
            this.membersArea = membersArea;
            this.maple = maple;
        }
    }

    @Getter
    public enum Yew {

        ;

        private final int minCombatLevel;
        private final boolean membersArea;
        private final Area yew;

        Yew(int minCombatLevel, final boolean membersArea, final Area yew) {
            this.minCombatLevel = minCombatLevel;
            this.membersArea = membersArea;
            this.yew = yew;
        }
    }



}
