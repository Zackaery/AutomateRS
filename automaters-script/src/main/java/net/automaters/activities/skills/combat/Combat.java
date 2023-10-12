//package net.automaters.activities.skills.combat;
//
//import net.automaters.activities.skills.firemaking.DynamicFiremaking;
//import net.automaters.api.walking.Area;
//import net.automaters.tasks.Task;
//import net.runelite.api.*;
//import net.unethicalite.api.commons.Predicates;
//import net.unethicalite.api.entities.TileItems;
//import net.unethicalite.api.game.Worlds;
//import net.unethicalite.api.items.Bank;
//import net.unethicalite.api.items.Equipment;
//import net.unethicalite.api.items.Inventory;
//
//import net.runelite.api.Item;
//
//import java.util.List;
//
//import java.util.Locale;
//
//import static net.automaters.activities.skills.firemaking.DynamicFiremaking.firemaking;
//import static net.automaters.activities.skills.woodcutting.Trees.*;
//import static net.automaters.api.entities.LocalPlayer.openBank;
//import static net.automaters.api.items.SecondaryTools.getSecondaryTool;
//import static net.automaters.api.ui.InventoryUtils.getAmountItemsNotInList;
//import static net.automaters.api.ui.InventoryUtils.getItemsNotInList;
//import static net.automaters.api.utils.Calculator.random;
//import static net.automaters.api.utils.Debug.debug;
//import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.goalFiremaking;
//import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.goalFletching;
//import static net.automaters.api.items.PrimaryTools.*;
//import static net.automaters.script.AutomateRS.scriptStarted;
//import static net.unethicalite.api.commons.Time.sleep;
//import static net.unethicalite.api.game.Skills.getLevel;
//
//public class Combat extends Task {
//
//    private static TileObject resourceObject;
//    private static Area resourceLocation;
//    private static boolean hasResources;
//    public Combat() {
//        super();
//    }
//
//    @Override
//    public void onStart() {
//
//    }
//    @Override
//    protected void onLoop() {
//
//    }
//
//    @Override
//    public boolean hasNonTaskItems() {
//
//    }
//
//    @Override
//    public boolean taskFinished() {
//            return false;
//    }
//
//    @Override
//    public void generateSecondaryTask() {
//
//    }
//
//    private void getResources() {
////        cooking = false;
//        secondaryTaskActive = false;
//        hasResources = true;
//        resourceObject = Trees.getTree();
//        resourceLocation = Trees.getTreeLocation();
//        if (resourceObject != null && resourceLocation != null) {
//            String location = String.format("Location: %d, %d, %d, %d, %d",
//                    resourceLocation.minX,
//                    resourceLocation.minY,
//                    resourceLocation.maxX,
//                    resourceLocation.maxY,
//                    resourceLocation.thisZ);
//            debug("Resource: " + resourceNames);
//            debug(location);
//        }
//    }
//
//
//
//}
