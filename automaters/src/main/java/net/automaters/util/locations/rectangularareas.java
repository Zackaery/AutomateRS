package net.automaters.util.locations;

import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.coords.RectangularArea;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;

import java.util.Arrays;
import java.util.Comparator;

public enum rectangularareas {

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

    rectangularareas(RectangularArea area) {
        this.area = area;
    }

    public RectangularArea getArea() {
        return area;
    }

    public static rectangularareas getNearest() {
        WorldPoint playerLocation = Players.getLocal().getWorldLocation();
        return Arrays.stream(values())
                .min(Comparator.comparingInt(x -> x.getArea().getNearest().distanceTo2D(playerLocation)))
                .orElse(null);
    }

    public static rectangularareas getNearestPath() {
        WorldPoint playerLocation = Players.getLocal().getWorldLocation();
        return Arrays.stream(values())
                .min(Comparator.comparingInt(x -> Movement.calculateDistance(x.getArea().getNearest())))
                .orElse(null);
    }
}
