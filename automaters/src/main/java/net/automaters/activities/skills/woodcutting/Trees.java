package net.automaters.activities.skills.woodcutting;

import lombok.Getter;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.api.walking.Area;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Skills;

import java.util.Arrays;
import java.util.List;

import static net.automaters.activities.skills.woodcutting.Locations.getClosestTreeArea;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.tasks.Task.secondaryTask;

public class Trees {

    public static List<String> resourceNames;

    @Getter
    public enum TreeType {
        TREE(Arrays.asList("Tree", "Dead tree", "Evergreen tree"),  1, 15, false, Locations.Tree.class),
        OAK(Arrays.asList("Oak tree"), 15, 30, false, Locations.Oak.class),
        WILLOW(Arrays.asList("Willow tree"), 30, 60, false, Locations.Willow.class),
        TEAK(Arrays.asList("Teak tree"), 35, 99, true, Locations.Teak.class),
        MAPLE(Arrays.asList("Maple tree"), 45, 60, true, Locations.Maple.class),
        YEW(Arrays.asList("Yew tree"), 60, 99, false, Locations.Yew.class),
        ;

        private final List<String> treeName;
        private final int reqWoodcuttingLevel;
        private final int maxSkillLevel;
        private final boolean members;
        private final Class<? extends Enum> treeLocationClass;

        TreeType(List<String> treeName, int reqWoodcuttingLevel, int maxSkillLevel, boolean members,  Class<? extends Enum> treeLocationClass) {
            this.treeName = treeName;
            this.reqWoodcuttingLevel = reqWoodcuttingLevel;
            this.maxSkillLevel = maxSkillLevel;
            this.members = members;
            this.treeLocationClass = treeLocationClass;
        }
    }

    public static TreeType chooseTreeType() {
        int woodcutting = Skills.getLevel(Skill.WOODCUTTING);
        int firemaking = Skills.getLevel(Skill.FIREMAKING);
        int fletching = Skills.getLevel(Skill.FLETCHING);

        boolean taskFletching = secondaryTask.equals("Fletching");
        boolean taskFiremaking = secondaryTask.equals("Firemaking");

        TreeType[] treeTypes = TreeType.values();

        for (int i = 0; i < treeTypes.length; i++) {
            TreeType currentTree = treeTypes[i];
            int nextWoodcuttingLevel = (i < treeTypes.length - 1) ? treeTypes[i + 1].reqWoodcuttingLevel : Integer.MAX_VALUE;

            if (woodcutting >= currentTree.reqWoodcuttingLevel) {
                if (taskFletching) {
                    if (fletching >= currentTree.reqWoodcuttingLevel && fletching < currentTree.maxSkillLevel) {
                        return currentTree;
                    }
                } else if (taskFiremaking) {
                    if (firemaking >= currentTree.reqWoodcuttingLevel && firemaking < currentTree.maxSkillLevel) {
                        return currentTree;
                    }
                }

                if (woodcutting < nextWoodcuttingLevel) {
                    return currentTree;
                }
            }
        }
        return null;
    }

    public static TileObject getTree() {
        TileObject tree = null;
        TreeType chosenTreeType = chooseTreeType();
        if (chosenTreeType != null) {
            List<String> treeNames = chosenTreeType.getTreeName();
            resourceNames = treeNames;
            for (String treeName : treeNames) {
                TileObject treeObject = TileObjects.getNearest(LocalPlayer.getPosition(), treeName);
                if (treeObject != null) {
                    if (tree == null || treeObject.distanceTo(localPlayer) < tree.distanceTo(localPlayer)) {
                        tree = treeObject;
                    }
                }
            }

            return tree;
        } else {
            debug("No suitable tree type found.");
            return null;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Area getTreeLocation() {
        TreeType location = chooseTreeType();
        Class<? extends Enum> locationClass = location != null ? location.getTreeLocationClass() : null;
        return getClosestTreeArea(locationClass);
    }
}
