package net.automaters.util.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecondaryTools {

    private Map<String, ArrayList<String>> toolCollections;
    private ArrayList<String> woodcuttingSecondarys;
    public SecondaryTools() {

        toolCollections = new HashMap<>();
        woodcuttingSecondarys = new ArrayList<>();
        woodcuttingSecondarys.add("Tinderbox");
        woodcuttingSecondarys.add("Knife");
        toolCollections.put("Woodcutting", woodcuttingSecondarys);
    }

    public ArrayList<String> getSecondayTool(String secondaryTool) {
        return toolCollections.get(secondaryTool);
    }
}
