package net.automaters.util.locations;

import net.runelite.api.Locatable;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.coords.RectangularArea;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;

import java.util.Arrays;
import java.util.Comparator;

public enum mining_rectangularareas implements Locatable {

    // new areas for mining

    // start of copper areas

    Copper_Lumbridge_MiningArea_I(new RectangularArea(3222, 3149, 3231, 3144, 0)),
    Copper_Lumbridge_MiningArea_II(new RectangularArea(3222, 3149, 3231, 3144, 0)),
    Copper_Lumbridge_MiningArea_III(new RectangularArea(3222, 3149, 3231, 3144, 0)),
    Copper_Lumbridge_MiningArea_IV(new RectangularArea(3281, 3366, 3290, 3361, 0)),
    Copper_Lumbridge_MiningArea_V(new RectangularArea(3281, 3366, 3290, 3361, 0)),

    Copper_Edgeville_MiningArea_I(new RectangularArea(2987, 3234, 2976, 3247, 0)),
    Copper_Edgeville_MiningArea_II(new RectangularArea(3033, 9826, 3032, 9825, 0)),
    Copper_Edgeville_MiningArea_III(new RectangularArea(3281, 3366, 3290, 3361, 0)),
    Copper_Edgeville_MiningArea_IV(new RectangularArea(3033, 9826, 3032, 9825, 0)),
    Copper_Edgeville_MiningArea_V(new RectangularArea(3281, 3366, 3290, 3361, 0)),

    Copper_Varrock_MiningArea_I(new RectangularArea(3033, 9826, 3032, 9825, 0)),
    Copper_Varrock_MiningArea_II(new RectangularArea(3281, 3366, 3290, 3361, 0)),
    Copper_Varrock_MiningArea_III(new RectangularArea(3281, 3366, 3290, 3361, 0)),
    Copper_Varrock_MiningArea_IV(new RectangularArea(3281, 3366, 3290, 3361, 0)),
    Copper_Varrock_MiningArea_V(new RectangularArea(3281, 3366, 3290, 3361, 0)),

    Copper_Falador_MiningArea_I(new RectangularArea(2905, 3364, 2911, 3358, 0)),
    Copper_Falador_MiningArea_II(new RectangularArea(3033, 9826, 3032, 9825, 0)),
    Copper_Falador_MiningArea_III(new RectangularArea(2987, 3234, 2976, 3247, 0)),
    Copper_Falador_MiningArea_IV(new RectangularArea(2987, 3234, 2976, 3247, 0)),
    Copper_Falador_MiningArea_V(new RectangularArea(3033, 9826, 3032, 9825, 0)),

    Copper_DraynorVillage_MiningArea_I(new RectangularArea(3294, 3309, 3295, 3311, 0)),
    Copper_DraynorVillage_MiningArea_II(new RectangularArea(3033, 9826, 3032, 9825, 0)),
    Copper_DraynorVillage_MiningArea_III(new RectangularArea(2987, 3234, 2976, 3247, 0)),
    Copper_DraynorVillage_MiningArea_IV(new RectangularArea(2987, 3234, 2976, 3247, 0)),
    Copper_DraynorVillage_MiningArea_V(new RectangularArea(3222, 3149, 3231, 3144, 0)),

    // end of copper areas

    // start of tin areas

    Tin_Hosidius_MiningArea_I(new RectangularArea(1751, 3718, 1754, 3715, 0)),
    Tin_Hosidius_MiningArea_II(new RectangularArea(1756, 3715, 1758, 3713, 0)),
    Tin_Hosidius_MiningArea_III(new RectangularArea(1755, 3713, 1759, 3711, 0)),
    Tin_Hosidius_MiningArea_IV(new RectangularArea(1772, 3487, 1774, 3485, 0)),
    Tin_Hosidius_MiningArea_V(new RectangularArea(1783, 3494, 1785, 3492, 0)),

    Tin_Camelot_MiningArea_I(new RectangularArea(2654, 3169, 2657, 3167, 0)),
    Tin_Camelot_MiningArea_II(new RectangularArea(2628, 3150, 2629, 3148, 0)),
    Tin_Camelot_MiningArea_III(new RectangularArea(2629, 3148, 2631, 3146, 0)),
    Tin_Camelot_MiningArea_IV(new RectangularArea(2630, 3144, 2632, 3142, 0)),
    Tin_Camelot_MiningArea_V(new RectangularArea(2655, 3169, 2656, 3167, 0)),

    Tin_Ardougne_MiningArea_I(new RectangularArea(2654, 3169, 2657, 3167, 0)),
    Tin_Ardougne_MiningArea_II(new RectangularArea(2628, 3150, 2629, 3148, 0)),
    Tin_Ardougne_MiningArea_III(new RectangularArea(2629, 3148, 2631, 3146, 0)),
    Tin_Ardougne_MiningArea_IV(new RectangularArea(2630, 3144, 2632, 3142, 0)),
    Tin_Ardougne_MiningArea_V(new RectangularArea(2655, 3169, 2656, 3167, 0)),

    Tin_Yanille_MiningArea_I(new RectangularArea(2654, 3169, 2657, 3167, 0)),
    Tin_Yanille_MiningArea_II(new RectangularArea(2628, 3150, 2629, 3148, 0)),
    Tin_Yanille_MiningArea_III(new RectangularArea(2629, 3148, 2631, 3146, 0)),
    Tin_Yanille_MiningArea_IV(new RectangularArea(2630, 3144, 2632, 3142, 0)),
    Tin_Yanille_MiningArea_V(new RectangularArea(2655, 3169, 2656, 3167, 0)),

    Tin_BarbarianOutpost_MiningArea_I(new RectangularArea(2654, 3169, 2657, 3167, 0)),
    Tin_BarbarianOutpost_MiningArea_II(new RectangularArea(2628, 3150, 2629, 3148, 0)),
    Tin_BarbarianOutpost_MiningArea_III(new RectangularArea(2629, 3148, 2631, 3146, 0)),
    Tin_BarbarianOutpost_MiningArea_IV(new RectangularArea(2630, 3144, 2632, 3142, 0)),
    Tin_BarbarianOutpost_MiningArea_V(new RectangularArea(2655, 3169, 2656, 3167, 0)),

    Tin_GnomeStronghold_MiningArea_I(new RectangularArea(2654, 3169, 2657, 3167, 0)),
    Tin_GnomeStronghold_MiningArea_II(new RectangularArea(2628, 3150, 2629, 3148, 0)),
    Tin_GnomeStronghold_MiningArea_III(new RectangularArea(2629, 3148, 2631, 3146, 0)),
    Tin_GnomeStronghold_MiningArea_IV(new RectangularArea(2630, 3144, 2632, 3142, 0)),
    Tin_GnomeStronghold_MiningArea_V(new RectangularArea(2655, 3169, 2656, 3167, 0)),

    Tin_ALKHARID_MINE_I___29CBREQ(new RectangularArea(3302, 3284, 3303, 3285, 0)),
    Tin_ALKHARID_MINE_II__29CBREQ(new RectangularArea(3304, 3302, 3305, 3301, 0)),
    Tin_ALKHARID_MINE_III_29CBREQ(new RectangularArea(3294, 3309, 3295, 3311, 0)),

    Tin_SE_VARROCK_MINE(new RectangularArea(3281, 3366, 3290, 3361, 0)),
    Tin_SW_VARROCK_MINE_13CBREQ(new RectangularArea(3179, 3364, 3171, 3368, 0)),

    Tin_WEST_FALADOR_MINE_P2PREQ(new RectangularArea(2905, 3364, 2911, 3358, 0)),

    Tin_RIMMINGTON_MINE_I__DROPREQ(new RectangularArea(2987, 3234, 2976, 3247, 0)),
    Tin_RIMMINGTON_MINE_II_DROPREQ(new RectangularArea(2987, 3234, 2976, 3247, 0)),

    Tin_DWARF_MINE(new RectangularArea(3033, 9826, 3032, 9825, 0)),
    Tin_F2P_MININGGUILD_60MININGREQ(new RectangularArea(3034, 9737, 3032, 9739, 0)),

    Tin_P2P_MININGGUILD_60MININGREQ(new RectangularArea(3022, 9722, 3020, 9720, 0)),

    Tin_Lumbridge_MiningArea_I(new RectangularArea(3222, 3149, 3231, 3144, 0)),
    Tin_Lumbridge_MiningArea_II(new RectangularArea(3222, 3149, 3231, 3144, 0)),
    Tin_Lumbridge_MiningArea_III(new RectangularArea(3222, 3149, 3231, 3144, 0)),
    Tin_Lumbridge_MiningArea_IV(new RectangularArea(3281, 3366, 3290, 3361, 0)),
    Tin_Lumbridge_MiningArea_V(new RectangularArea(3281, 3366, 3290, 3361, 0)),

    Tin_Edgeville_MiningArea_I(new RectangularArea(2987, 3234, 2976, 3247, 0)),
    Tin_Edgeville_MiningArea_II(new RectangularArea(3033, 9826, 3032, 9825, 0)),
    Tin_Edgeville_MiningArea_III(new RectangularArea(3179, 3364, 3171, 3368, 0)),
    Tin_Edgeville_MiningArea_IV(new RectangularArea(3033, 9826, 3032, 9825, 0)),
    Tin_Edgeville_MiningArea_V(new RectangularArea(3281, 3366, 3290, 3361, 0))

    // end of tin

    ;

    // checks if player is in any mining area
    public boolean isInAnyMiningArea() {
        // Get the player's current position or area here (You need to implement this part)
        // For example, if you have a method getPlayerPosition() that returns the player's position:
        WorldPoint playerPosition = Players.getLocal().getWorldLocation();

        // Define an array of all mining areas
        RectangularArea[] miningAreas = {
                Copper_Lumbridge_MiningArea_I.getArea(),
                Copper_Lumbridge_MiningArea_II.getArea(),
                Copper_Lumbridge_MiningArea_III.getArea(),
                Copper_Lumbridge_MiningArea_IV.getArea(),
                Copper_Lumbridge_MiningArea_V.getArea(),
                Copper_Edgeville_MiningArea_I.getArea(),
                Copper_Edgeville_MiningArea_II.getArea(),
                Copper_Edgeville_MiningArea_III.getArea(),
                Copper_Edgeville_MiningArea_IV.getArea(),
                Copper_Edgeville_MiningArea_V.getArea(),
                Copper_Varrock_MiningArea_I.getArea(),
                Copper_Varrock_MiningArea_II.getArea(),
                Copper_Varrock_MiningArea_III.getArea(),
                Copper_Varrock_MiningArea_IV.getArea(),
                Copper_Varrock_MiningArea_V.getArea(),
                Copper_Falador_MiningArea_I.getArea(),
                Copper_Falador_MiningArea_II.getArea(),
                Copper_Falador_MiningArea_III.getArea(),
                Copper_Falador_MiningArea_IV.getArea(),
                Copper_Falador_MiningArea_V.getArea(),
                Copper_DraynorVillage_MiningArea_I.getArea(),
                Copper_DraynorVillage_MiningArea_II.getArea(),
                Copper_DraynorVillage_MiningArea_III.getArea(),
                Copper_DraynorVillage_MiningArea_IV.getArea(),
                Copper_DraynorVillage_MiningArea_V.getArea(),

                Tin_Hosidius_MiningArea_I.getArea(),
                Tin_Hosidius_MiningArea_II.getArea(),
                Tin_Hosidius_MiningArea_III.getArea(),
                Tin_Hosidius_MiningArea_IV.getArea(),
                Tin_Hosidius_MiningArea_V.getArea(),
                Tin_Camelot_MiningArea_I.getArea(),
                Tin_Camelot_MiningArea_II.getArea(),
                Tin_Camelot_MiningArea_III.getArea(),
                Tin_Camelot_MiningArea_IV.getArea(),
                Tin_Camelot_MiningArea_V.getArea(),
                Tin_Ardougne_MiningArea_I.getArea(),
                Tin_Ardougne_MiningArea_II.getArea(),
                Tin_Ardougne_MiningArea_III.getArea(),
                Tin_Ardougne_MiningArea_IV.getArea(),
                Tin_Ardougne_MiningArea_V.getArea(),
                Tin_Yanille_MiningArea_I.getArea(),
                Tin_Yanille_MiningArea_II.getArea(),
                Tin_Yanille_MiningArea_III.getArea(),
                Tin_Yanille_MiningArea_IV.getArea(),
                Tin_Yanille_MiningArea_V.getArea(),
                Tin_BarbarianOutpost_MiningArea_I.getArea(),
                Tin_BarbarianOutpost_MiningArea_II.getArea(),
                Tin_BarbarianOutpost_MiningArea_III.getArea(),
                Tin_BarbarianOutpost_MiningArea_IV.getArea(),
                Tin_BarbarianOutpost_MiningArea_V.getArea(),
                Tin_GnomeStronghold_MiningArea_I.getArea(),
                Tin_GnomeStronghold_MiningArea_II.getArea(),
                Tin_GnomeStronghold_MiningArea_III.getArea(),
                Tin_GnomeStronghold_MiningArea_IV.getArea(),
                Tin_GnomeStronghold_MiningArea_V.getArea(),
                Tin_ALKHARID_MINE_I___29CBREQ.getArea(),
                Tin_ALKHARID_MINE_II__29CBREQ.getArea(),
                Tin_ALKHARID_MINE_III_29CBREQ.getArea(),
                Tin_SE_VARROCK_MINE.getArea(),
                Tin_SW_VARROCK_MINE_13CBREQ.getArea(),
                Tin_WEST_FALADOR_MINE_P2PREQ.getArea(),
                Tin_RIMMINGTON_MINE_I__DROPREQ.getArea(),
                Tin_RIMMINGTON_MINE_II_DROPREQ.getArea(),
                Tin_DWARF_MINE.getArea(),
                Tin_F2P_MININGGUILD_60MININGREQ.getArea(),
                Tin_P2P_MININGGUILD_60MININGREQ.getArea(),
                Tin_Lumbridge_MiningArea_I.getArea(),
                Tin_Lumbridge_MiningArea_II.getArea(),
                Tin_Lumbridge_MiningArea_III.getArea(),
                Tin_Lumbridge_MiningArea_IV.getArea(),
                Tin_Lumbridge_MiningArea_V.getArea(),
                Tin_Edgeville_MiningArea_I.getArea(),
                Tin_Edgeville_MiningArea_II.getArea(),
                Tin_Edgeville_MiningArea_III.getArea(),
                Tin_Edgeville_MiningArea_IV.getArea(),
                Tin_Edgeville_MiningArea_V.getArea(),

        };

        // Check if the player's position matches any of the mining areas
        for (RectangularArea miningArea : miningAreas) {
            if (miningArea.contains(playerPosition)) {
                return true; // Player is in a mining area
            }
        }

        return false; // Player is not in any mining area
    }


    private final RectangularArea area;

    mining_rectangularareas(RectangularArea area) {
        this.area = area;
    }

    public RectangularArea getArea() {
        return area;
    }

    // New method to check if player is within 2 tiles of the area
    public boolean isPlayerWithinTwoTiles() {
        WorldPoint playerLocation = Players.getLocal().getWorldLocation();
        return area.contains(playerLocation);
    }

    public static mining_rectangularareas getNearest() {
        WorldPoint playerLocation = Players.getLocal().getWorldLocation();
        return Arrays.stream(values())
                .min(Comparator.comparingInt(x -> x.getArea().getNearest().distanceTo2D(playerLocation)))
                .orElse(null);
    }

    public static mining_rectangularareas getNearestPath() {
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
