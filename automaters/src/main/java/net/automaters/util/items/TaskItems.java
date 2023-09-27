package net.automaters.util.items;

import lombok.Getter;

import javax.tools.Tool;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TaskItems {
    private List<String> primaryTool;
    private List<String> skillingOutfits;
    private List<String> secondaryTools;

    public TaskItems(List<String> primaryTool, List<String> skillingOutfits, List<String> secondaryTools) {
        this.primaryTool = primaryTool;
        this.skillingOutfits = skillingOutfits;
        this.secondaryTools = secondaryTools;
    }

}