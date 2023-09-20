package net.automaters.util.locations;

import net.runelite.api.Locatable;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.coords.RectangularArea;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public enum woodcutting_rectangularareas implements Locatable {

    // new areas for woodcuttingbored

    Falador_Tree_TreeArea_I(new RectangularArea(2987, 3302, 2997, 3296, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(2997 - 2987 + 1) + 2987;
            int randomY = random.nextInt(3302 - 3296 + 1) + 3296;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    Falador_Tree_TreeArea_II(new RectangularArea(2948, 3409, 2953, 3403, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(2953 - 2948 + 1) + 2948;
            int randomY = random.nextInt(3409 - 3403 + 1) + 3403;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    Falador_Tree_TreeArea_III(new RectangularArea(2973, 3450, 2979, 3443, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(2979 - 2973 + 1) + 2973;
            int randomY = random.nextInt(3450 - 3443 + 1) + 3443;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    Falador_Tree_TreeArea_IV(new RectangularArea(3030, 3323, 3038, 3319, 0)) {
       // @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3038 - 3030 + 1) + 3030;
            int randomY = random.nextInt(3323 - 3319 + 1) + 3319;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    Falador_Tree_TreeArea_V(new RectangularArea(2977, 3497, 2981, 3491, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(2981 - 2977 + 1) + 2977;
            int randomY = random.nextInt(3497 - 3491 + 1) + 3491;
            return new WorldPoint(randomX, randomY, 0);
        }
    },


    // oak trees

    Falador_Oak_TreeArea_I(new RectangularArea(3086, 3295, 3079, 3290, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3086 - 3079 + 1) + 3079;
            int randomY = random.nextInt(3295 - 3290 + 1) + 3290;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    Falador_Oak_TreeArea_II(new RectangularArea(3005, 3309, 2999, 3316, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3005 - 2999 + 1) + 2999;
            int randomY = random.nextInt(3309 - 3316 + 1) + 3316;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    Falador_Oak_TreeArea_III(new RectangularArea(2957, 3397, 2950, 3400, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(2957 - 2950 + 1) + 2950;
            int randomY = random.nextInt(3397 - 3400 + 1) + 3400;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    Falador_Oak_TreeArea_IV(new RectangularArea(3012, 3408, 3008, 3412, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3012 - 3008 + 1) + 3008;
            int randomY = random.nextInt(3408 - 3412 + 1) + 3412;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    Falador_Oak_TreeArea_V(new RectangularArea(2930, 3365, 2926, 3368, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(2930 - 2926 + 1) + 2926;
            int randomY = random.nextInt(3365 - 3368 + 1) + 3368;
            return new WorldPoint(randomX, randomY, 0);
        }
    },

    // Willow trees

    DraynorVillage_Willow_TreeArea_I(new RectangularArea(2960, 3198, 2966, 3193, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(2966 - 2960 + 1) + 2960;
            int randomY = random.nextInt(3198 - 3193 + 1) + 3193;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    DraynorVillage_Willow_TreeArea_II(new RectangularArea(2985, 3191, 2993, 3182, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(2993 - 2985 + 1) + 2985;
            int randomY = random.nextInt(3191 - 3182 + 1) + 3182;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    DraynorVillage_Willow_TreeArea_III(new RectangularArea(2994, 3171, 3000, 3163, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3000 - 2994 + 1) + 2994;
            int randomY = random.nextInt(3171 - 3163 + 1) + 3163;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    DraynorVillage_Willow_TreeArea_IV(new RectangularArea(3026, 3178, 3031, 3166, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3031 - 3026 + 1) + 3026;
            int randomY = random.nextInt(3178 - 3166 + 1) + 3166;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    DraynorVillage_Willow_TreeArea_V(new RectangularArea(3056, 3256, 3064, 3251, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3064 - 3056 + 1) + 3056;
            int randomY = random.nextInt(3256 - 3251 + 1) + 3251;
            return new WorldPoint(randomX, randomY, 0);
        }
    },

    // end tree locations


    // zAccountBuilder areas

    SHOP_GRAND_EXCHANGE_FS(new RectangularArea(3141, 3513, 3186, 3464, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3186 - 3141 + 1) + 3141;
            int randomY = random.nextInt(3513 - 3464 + 1) + 3464;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    SHOP_GRAND_EXCHANGE_WALK(new RectangularArea(3167, 3489, 3167, 3488, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            // Assuming you want the same coordinates for both SHOP_GRAND_EXCHANGE_WALK areas
            return new WorldPoint(3167, 3488, 0);
        }
    },
    Region_AsgarniaMisthalin(new RectangularArea(3899, 2569, 2863, 4133, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3899 - 2863 + 1) + 2863;
            int randomY = random.nextInt(4133 - 2569 + 1) + 2569;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    Region_AsgarniaMisthalin_I(new RectangularArea(3899, 2569, 2863, 4133, 1)) {
        @Override
        public WorldPoint getRandomPointInside() {
            // You can implement a similar getRandomPointInside method for this area if needed
            return null;
        }
    },
    Region_AsgarniaMisthalin_II(new RectangularArea(3899, 2569, 2863, 4133, 2)) {
        @Override
        public WorldPoint getRandomPointInside() {
            // You can implement a similar getRandomPointInside method for this area if needed
            return null;
        }
    },
    Region_Kandarin(new RectangularArea(2859, 2565, 2003, 4133, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(2859 - 2003 + 1) + 2003;
            int randomY = random.nextInt(4133 - 2565 + 1) + 2565;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    Region_Kandarin_I(new RectangularArea(2859, 2565, 2003, 4133, 1)) {
        @Override
        public WorldPoint getRandomPointInside() {
            // You can implement a similar getRandomPointInside method for this area if needed
            return null;
        }
    },
    Region_Kandarin_II(new RectangularArea(2859, 2565, 2003, 4133, 2)) {
        @Override
        public WorldPoint getRandomPointInside() {
            // You can implement a similar getRandomPointInside method for this area if needed
            return null;
        }
    },

    Region_Zeah(new RectangularArea(1936, 3371, 1164, 4101, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(1936 - 1164 + 1) + 1164;
            int randomY = random.nextInt(4101 - 3371 + 1) + 3371;
            return new WorldPoint(randomX, randomY, 0);
        }
    },

    SETHS_AXE(new RectangularArea(3234, 3296, 3231, 3293, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3234 - 3231 + 1) + 3231;
            int randomY = random.nextInt(3296 - 3293 + 1) + 3293;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    SETHS_AXE_FS(new RectangularArea(3225, 3301, 3236, 3287, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3236 - 3225 + 1) + 3225;
            int randomY = random.nextInt(3301 - 3287 + 1) + 3287;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    FREDS_AXE(new RectangularArea(3186, 3278, 3190, 3276, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3190 - 3186 + 1) + 3186;
            int randomY = random.nextInt(3278 - 3276 + 1) + 3276;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    FREDS_AXE_FS(new RectangularArea(3184, 3279, 3192, 3276, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3192 - 3184 + 1) + 3184;
            int randomY = random.nextInt(3279 - 3276 + 1) + 3276;
            return new WorldPoint(randomX, randomY, 0);
        }
    },

    WOODCUTTING_TUTOR(new RectangularArea(3223, 3246, 3224, 3242, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3224 - 3223 + 1) + 3223;
            int randomY = random.nextInt(3246 - 3242 + 1) + 3242;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    Varrock(new RectangularArea(3062, 3520, 3291, 3374, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3291 - 3062 + 1) + 3062;
            int randomY = random.nextInt(3374 - 3520 + 1) + 3520;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    FALADOR_BANK(new RectangularArea(3010, 3355, 3015, 3355, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3015 - 3010 + 1) + 3010;
            int randomY = 3355; // Since it's a straight line, Y remains the same
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    KingdomOfMisthalin(new RectangularArea(2832, 3518, 3433, 2988, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(3433 - 2832 + 1) + 2832;
            int randomY = random.nextInt(3518 - 2988 + 1) + 2988;
            return new WorldPoint(randomX, randomY, 0);
        }
    },
    KingdomOfKandarin(new RectangularArea(2831, 2988, 2124, 3734, 0)) {
        @Override
        public WorldPoint getRandomPointInside() {
            Random random = new Random();
            int randomX = random.nextInt(2124 - 2831 + 1) + 2831;
            int randomY = random.nextInt(3734 - 2988 + 1) + 2988;
            return new WorldPoint(randomX, randomY, 0);
        }
    },

    ;

    public abstract WorldPoint getRandomPointInside();

    private final RectangularArea area;

    woodcutting_rectangularareas(RectangularArea area) {
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

    public void walkToTreeArea() {
        if (!isPlayerWithinTwoTiles()) {
            Movement.walkTo(area.getCenter());
        }
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
