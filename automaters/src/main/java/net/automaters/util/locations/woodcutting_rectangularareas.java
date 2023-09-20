package net.automaters.util.locations;

import net.runelite.api.Locatable;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.coords.RectangularArea;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;

import java.util.Arrays;
import java.util.Comparator;

public enum woodcutting_rectangularareas implements Locatable {

    // new areas for woodcuttingbored

    Falador_Tree_TreeArea_I(new RectangularArea(2987, 3302, 2997, 3296,0)),
    Falador_Tree_TreeArea_II(new RectangularArea(2948, 3409, 2953, 3403,0)),
    Falador_Tree_TreeArea_III(new RectangularArea(2973, 3450, 2979, 3443,0)),
    Falador_Tree_TreeArea_IV(new RectangularArea(3030, 3323, 3038, 3319,0)),
    Falador_Tree_TreeArea_V(new RectangularArea(2977, 3497, 2981, 3491,0)),


    // oak trees

    Falador_Oak_TreeArea_I(new RectangularArea(3086, 3295, 3079, 3290,0)),
    Falador_Oak_TreeArea_II(new RectangularArea(3005, 3309, 2999, 3316,0)),
    Falador_Oak_TreeArea_III(new RectangularArea(2957, 3397, 2950, 3400,0)),
    Falador_Oak_TreeArea_IV(new RectangularArea(3012, 3408, 3008, 3412,0)),
    Falador_Oak_TreeArea_V(new RectangularArea(2930, 3365, 2926, 3368,0)),

    // Willow trees

    DraynorVillage_Willow_TreeArea_I(new RectangularArea(2960, 3198, 2966, 3193,0)),
    DraynorVillage_Willow_TreeArea_II(new RectangularArea(2985, 3191, 2993, 3182,0)),
    DraynorVillage_Willow_TreeArea_III(new RectangularArea(2994, 3171, 3000, 3163,0)),
    DraynorVillage_Willow_TreeArea_IV(new RectangularArea(3026, 3178, 3031, 3166,0)),
    DraynorVillage_Willow_TreeArea_V(new RectangularArea(3056, 3256, 3064, 3251,0)),

    // end tree locations


    // zAccountBuilder areas

    SHOP_GRAND_EXCHANGE_FS(new RectangularArea(3141, 3513, 3186, 3464,0)),
    SHOP_GRAND_EXCHANGE_WALK(new RectangularArea(3167, 3489, 3167, 3488,0)),

    Region_AsgarniaMisthalin(new RectangularArea(3899,2569,2863,4133,0)),
    Region_AsgarniaMisthalin_I(new RectangularArea(3899,2569,2863,4133,1)),
    Region_AsgarniaMisthalin_II(new RectangularArea(3899,2569,2863,4133,2)),

    Region_Kandarin(new RectangularArea(2859,2565,2003,4133,0)),
    Region_Kandarin_I(new RectangularArea(2859,2565,2003,4133,1)),
    Region_Kandarin_II(new RectangularArea(2859,2565,2003,4133,2)),

    Region_Zeah(new RectangularArea(1936,3371,1164,4101,0)),
    Region_Zeah_I(new RectangularArea(1936,3371,1164,4101,1)),
    Region_Zeah_II(new RectangularArea(1936,3371,1164,4101,2)),

    SETHS_AXE(new RectangularArea(3234, 3296, 3231, 3293,0)),
    SETHS_AXE_FS(new RectangularArea(3225, 3301, 3236, 3287,0)),

    FREDS_AXE(new RectangularArea(3186, 3278, 3190, 3276,0)),
    FREDS_AXE_FS(new RectangularArea(3184, 3279, 3192, 3276,0)),

    WOODCUTTING_TUTOR(new RectangularArea(3223, 3246, 3224, 3242,0)),

    Varrock(new RectangularArea(3062,3520,3291,3374,0)),

    FALADOR_BANK(new RectangularArea(3010, 3355, 3015, 3355,0)),

    KingdomOfMisthalin(new RectangularArea(2832,3518,3433,2988,0)),
    KingdomOfMisthalin_I(new RectangularArea(2832,3518,3433,2988,1)),
    KingdomOfMisthalin_II(new RectangularArea(2832,3518,3433,2988,2)),

    KingdomOfKandarin(new RectangularArea(2831,2988,2124,3734,0)),
    KingdomOfKandarin_I(new RectangularArea(2831,2988,2124,3734,1)),
    KingdomOfKandarin_II(new RectangularArea(2831,2988,2124,3734,2)),

    ;

    private final RectangularArea area;

    woodcutting_rectangularareas(RectangularArea area) {
        this.area = area;
    }

    public RectangularArea getArea() {
        return area;
    }

    public static woodcutting_rectangularareas getNearest() {
        WorldPoint playerLocation = Players.getLocal().getWorldLocation();
        return Arrays.stream(values())
                .min(Comparator.comparingInt(x -> x.getArea().getNearest().distanceTo2D(playerLocation)))
                .orElse(null);
    }

    public static woodcutting_rectangularareas getNearestPath() {
        WorldPoint playerLocation = Players.getLocal().getWorldLocation();
        return Arrays.stream(values())
                .min(Comparator.comparingInt(x -> Movement.calculateDistance(x.getArea().getNearest())))
                .orElse(null);
    }

    @Override
    public WorldPoint getWorldLocation() {
        return null;
    }

    @Override
    public LocalPoint getLocalLocation() {
        return null;
    }
}
