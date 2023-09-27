package net.automaters.util.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SkillingOutfits {

    private Map<String, ArrayList<String>> outfitCollections;
    private ArrayList<String> gracefulOutfit;
    private ArrayList<String> woodcuttingOutfit;
    private ArrayList<String> miningOutfit;
    public SkillingOutfits() {

        outfitCollections = new HashMap<>();

        gracefulOutfit = new ArrayList<>();
        gracefulOutfit.add("Graceful hood");
        gracefulOutfit.add("Graceful top");
        gracefulOutfit.add("Graceful legs");
        gracefulOutfit.add("Graceful boots");
        gracefulOutfit.add("Graceful gloves");
        gracefulOutfit.add("Graceful cape");
        outfitCollections.put("Graceful", gracefulOutfit);

        woodcuttingOutfit = new ArrayList<>();
        woodcuttingOutfit.add("Lumberjack hat");
        woodcuttingOutfit.add("Lumberjack top");
        woodcuttingOutfit.add("Lumberjack legs");
        woodcuttingOutfit.add("Lumberjack boots");
        outfitCollections.put("Woodcutting", woodcuttingOutfit);

        miningOutfit = new ArrayList<>();
        miningOutfit.add("Prospector helmet");
        miningOutfit.add("Prospector jacket");
        miningOutfit.add("Prospector legs");
        miningOutfit.add("Prospector boots");
        outfitCollections.put("Mining", miningOutfit);
    }

    public ArrayList<String> get(String... outfits) {
        ArrayList<String> combinedOutfits = new ArrayList<>();

        for (String outfit : outfits) {
            if (outfitCollections.containsKey(outfit)) {
                combinedOutfits.addAll(outfitCollections.get(outfit));
            }
        }

        return combinedOutfits;
    }
}
