package net.automaters.api.items;

import lombok.Getter;

import javax.tools.Tool;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TaskItems {
    private final List<String> primaryTool;
    private final List<String> skillingOutfits;
    private final List<String> secondaryTools;
    private final List<String> secondaryItems;

    public TaskItems(List<String> primaryTool, List<String> skillingOutfits, List<String> secondaryTools, List<String> secondaryItems) {
        this.primaryTool = primaryTool;
        this.skillingOutfits = skillingOutfits;
        this.secondaryTools = secondaryTools;
        this.secondaryItems = secondaryItems;
    }

}