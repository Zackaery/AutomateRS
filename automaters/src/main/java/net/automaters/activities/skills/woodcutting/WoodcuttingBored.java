package net.automaters.activities.skills.woodcutting;

import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.mixins.Inject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.plugins.LoopedPlugin;

import java.util.Comparator;
import java.util.Random;

import static net.automaters.api.entities.LocalPlayer.*;
import static net.automaters.api.entities.SkillCheck.getWoodcuttingLevel;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.util.locations.woodcutting_rectangularareas.*;
import static net.unethicalite.api.commons.Time.sleep;


@SuppressWarnings({"ConstantConditions","unused"})
public class WoodcuttingBored extends LoopedPlugin {

    @Inject
    private Client client;

    // Just another woodcutting script...

    @Override
    public int loop() {
        debug("Holy shit this is in the loop");

        var local = Players.getLocal();
        if ((local == null) || !scriptStarted) {
            debug("Player does not exist.");
            return -1;
        }

        // Choosing tree location
        debug("Setting location to initial thought...");
        Random random = new Random();
        int randomIndex = random.nextInt(5);

        debug("I'm thinking... " + randomIndex);
        sleep(1000, 2500);

        boolean readytochop = false;
        boolean gearcheck = false;
        boolean axecheck = false;
        boolean armorcheck = false;

        WorldPoint playerPosition = Players.getLocal().getWorldLocation();

        if (!Bank.isOpen() && Inventory.isFull() || !Bank.isOpen() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
                || !Bank.isOpen() && !Inventory.isFull() && !Equipment.contains(Predicates.nameContains("axe")) && Inventory.contains(Predicates.nameContains("axe"))
                || !Bank.isOpen() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))) {
            // do something
            openBank();
            sleep(250);
            readytochop = false;
            sleep(250);
            armorcheck = true;
            sleep(250);
            axecheck = true;
            return 100;
        }

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("axe")) && Equipment.contains(Predicates.nameContains("axe"))) {
            Bank.depositInventory();
            Bank.depositEquipment();
            random.nextInt(5);
            debug("Location chosen after banking: " + randomIndex);
        }

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("Logs"))
                || Bank.isOpen() && Inventory.isFull()
                || Bank.isOpen() && !Inventory.isFull() && Inventory.contains(Predicates.nameContains("Logs"))
                || Inventory.isFull() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
                || Inventory.isFull() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))) {
            debug("trying to deposit now?");
            Bank.depositInventory();
            random.nextInt(5);
            debug("Location chosen after banking: " + randomIndex);
            return 100;
        }

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("Logs"))
                || Bank.isOpen() && Inventory.isFull()
                || Bank.isOpen() && !Inventory.isFull() && Inventory.contains(Predicates.nameContains("Logs"))
                || !Inventory.isFull() && Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
                || Inventory.isFull() && Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))) {
            debug("trying to deposit");
            Bank.depositInventory();
            debug("Location chosen after banking: " + randomIndex);
            return 100;
        }

//        String[] itemsToHandle = {
//                "Lumberjack hat", "Lumberjack top", "Lumberjack legs", "Lumberjack boots",
//                "Graceful hood", "Graceful top", "Graceful legs", "Graceful boots",
//                "Graceful gloves", "Graceful cape", "ronman helm", "ron helm"
//        };
//
//        if (!armorcheck && Bank.isOpen()) {
//        boolean lumberjackHatEquipped = Equipment.contains("Lumberjack hat");
//        boolean gracefulHoodEquipped = Equipment.contains("Graceful hood");
//
//        for (String itemName : itemsToHandle) {
//            // Check if the item should be skipped
//            boolean shouldSkipItem = false;
//
//            if (itemName.startsWith("Lumberjack")) {
//                // Check Woodcutting level
//                if (Skills.getBoostedLevel(Skill.WOODCUTTING) < 44) {
//                    shouldSkipItem = true;
//                }
//            } else if (itemName.startsWith("Graceful")) {
//                // Wear Graceful items only if Lumberjack isn't equipped and Graceful hood isn't equipped
//                if (lumberjackHatEquipped || gracefulHoodEquipped) {
//                    shouldSkipItem = true;
//                }
//            } else if (itemName.startsWith("ronman") || itemName.startsWith("ron")) {
//                // Wear Ronman items only if Lumberjack hat and Graceful hood aren't equipped
//                if (lumberjackHatEquipped || gracefulHoodEquipped) {
//                    shouldSkipItem = true;
//                }
//            }
//
//            // Skip item if needed
//            if (shouldSkipItem) {
//                continue;
//            }
//
//            // Withdraw item
//            if (Bank.isOpen() && !Equipment.contains(itemName)
//                    && Bank.contains(itemName) && !Inventory.contains(itemName)
//                    && Skills.getBoostedLevel(Skill.WOODCUTTING) >= 44) {
//                Bank.withdraw(itemName, 1, Bank.WithdrawMode.ITEM);
//                sleep(1500);
//                //  Bank.close();
//                sleep(1000);
//            }
//
//            // Wear item
//            if (Inventory.contains(itemName) && !Equipment.contains(itemName)) {
//                if (!Bank.isOpen()) {
//                    Item item = Inventory.getFirst(itemName);
//                    if (item != null) {
//                        debug("Equipping item: " + itemName);
//                        item.interact("Wear");
//                        Time.sleepUntil(() -> Equipment.contains(itemName), 3000);
//                    }
//                } else {
//                    Bank.Inventory.getFirst(itemName).interact("Wear");
//                    Time.sleepUntil(() -> Equipment.contains(itemName), 3000);
//                }
//            }
//        }
//        armorcheck = false;
//    }


        // start of new upgrade axe
//
//        GearUpgrade gearUpgrade = new GearUpgrade();
//        gearUpgrade.upgradeGear();
//        sleep(250,1500);


        if (Bank.isOpen() && Inventory.isEmpty() && !armorcheck && !axecheck) {
            debug("checking armor now.");
            GearUpgrade gearUpgrade = new GearUpgrade();
            gearUpgrade.upgradeGear();
            sleep(250,1500);
            armorcheck = false;
            debug("Armor check complete. Now checking axes");
            AxeUpgrade axeUpgrade = new AxeUpgrade();
            axeUpgrade.executeAxeUpgrade();
            sleep(1500);
            axecheck = false;
            debug("Axe check complete!");
            sleep(500);
            Bank.close();
        }



//        // handle axe upgrades if available
//
//        String[] axesToHandle = {
//                "Bronze axe", "Iron axe", "Steel axe", "Black axe",
//                "Mithril axe", "Adamant axe", "Rune axe", "Dragon axe", "Crystal axe"
//        };
//        if (Bank.isOpen() && !axecheck) {
//            debug("Axe check complete!");
//            axecheck = false;
//        }


        if (!local.isMoving() && getWoodcuttingLevel() <= 95 && Equipment.contains(Predicates.nameContains("axe")) && !Inventory.isFull() && !playerPosition.isInArea(Falador_Tree_TreeArea_I_Area.toWorldArea())
                ||!local.isMoving() && getWoodcuttingLevel() <= 95 && Equipment.contains(Predicates.nameContains("axe")) && !Inventory.isFull() && !playerPosition.isInArea(Falador_Tree_TreeArea_II_Area.toWorldArea())
                ||!local.isMoving() && getWoodcuttingLevel() <= 95 && Equipment.contains(Predicates.nameContains("axe")) && !Inventory.isFull() && !playerPosition.isInArea(Falador_Tree_TreeArea_III_Area.toWorldArea())
                ||!local.isMoving() && getWoodcuttingLevel() <= 95 && Equipment.contains(Predicates.nameContains("axe")) && !Inventory.isFull() && !playerPosition.isInArea(Falador_Tree_TreeArea_IV_Area.toWorldArea())
                ||!local.isMoving() && getWoodcuttingLevel() <= 95 && Equipment.contains(Predicates.nameContains("axe")) && !Inventory.isFull() && !playerPosition.isInArea(Falador_Tree_TreeArea_V_Area.toWorldArea())) {

            // start of tree locations

            if (randomIndex == 0 && !playerPosition.isInArea(Falador_Tree_TreeArea_I_Area.toWorldArea())) {
                debug("Walking 1");
                automateWalk(Falador_Tree_TreeArea_I_Area.toWorldArea());
                sleep(2000);
                readytochop = true;
            }

            // end of tree

            if (randomIndex == 1 && !playerPosition.isInArea(Falador_Tree_TreeArea_II_Area.toWorldArea())) {
                debug("Walking 1");
                automateWalk(Falador_Tree_TreeArea_II_Area.toWorldArea());
                sleep(2000);
                readytochop = true;
            }

            // end of tree

            if (randomIndex == 2 && !playerPosition.isInArea(Falador_Tree_TreeArea_III_Area.toWorldArea())) {
                debug("Walking 1");
                automateWalk(Falador_Tree_TreeArea_III_Area.toWorldArea());
                sleep(2000);
                readytochop = true;
            }

            // end of tree

            if (randomIndex == 3 && !playerPosition.isInArea(Falador_Tree_TreeArea_IV_Area.toWorldArea())) {
                debug("Walking 1");
                automateWalk(Falador_Tree_TreeArea_IV_Area.toWorldArea());
                sleep(2000);
                readytochop = true;
            }

            // end of tree

            if (randomIndex == 4 && !playerPosition.isInArea(Falador_Tree_TreeArea_V_Area.toWorldArea())) {
                debug("Walking 1");
                automateWalk(Falador_Tree_TreeArea_V_Area.toWorldArea());
                sleep(2000);
                readytochop = true;
            }
            // end of tree
        }

        // normal tree

        if (randomIndex == 0 && readytochop) {
            debug("inside index");
            sleep(1000);
            while (!Inventory.isFull() && Equipment.contains(Predicates.nameContains("axe"))) {
                sleep(1000);
                var tree = TileObjects
                        .getSurrounding(local.getWorldLocation(), 8, 1276, 1278, 2091, 2092)
                        .stream()
                        .filter(Reachable::isInteractable)
                        .min(Comparator.comparing(x -> x.distanceTo(local.getWorldLocation())))
                        .orElse(null);
                sleep(1000);

                if (!local.isAnimating() && !local.isInteracting() && !Inventory.isFull() && !local.isMoving() && tree != null) {
                    debug("Chop suey!");
                    tree.interact("Chop down");
                    sleep(600, 1500);
                }
                    if (tree == null) {
                        debug("Cannot find tree, need to reposition");
                        sleep(150, 500);
                        return -1;

                }
            }

        }

        if (randomIndex == 1 && readytochop) {
            debug("inside index");
            sleep(1000);
            while (!Inventory.isFull() && Equipment.contains(Predicates.nameContains("axe"))) {
                sleep(1000);
                var tree = TileObjects
                        .getSurrounding(local.getWorldLocation(), 8, 1276,1278,2091,2092)
                        .stream()
                        .filter(Reachable::isInteractable)
                        .min(Comparator.comparing(x -> x.distanceTo(local.getWorldLocation())))
                        .orElse(null);
                sleep(1000);

                if (!local.isAnimating() && !local.isInteracting() && !Inventory.isFull() && !local.isMoving() && tree != null) {
                    debug("Chop suey!");
                    tree.interact("Chop down");
                    sleep(600,1500);
                }
                if (tree == null) {
                    debug("Cannot find tree, need to reposition");
                    sleep(150, 500);
                    return -1;
                }

            }

        }

        if (randomIndex == 2 && readytochop) {
            debug("inside index");
            sleep(1000);
            while (!Inventory.isFull() && Equipment.contains(Predicates.nameContains("axe"))) {
                sleep(1000);
                var tree = TileObjects
                        .getSurrounding(local.getWorldLocation(), 8, 1276,1278,2091,2092)
                        .stream()
                        .filter(Reachable::isInteractable)
                        .min(Comparator.comparing(x -> x.distanceTo(local.getWorldLocation())))
                        .orElse(null);
                sleep(1000);

                if (!local.isAnimating() && !local.isInteracting() && !Inventory.isFull() && !local.isMoving() && tree != null) {
                    debug("Chop suey!");
                    tree.interact("Chop down");
                    sleep(600,1500);
                }
                if (tree == null) {
                        debug("Cannot find tree, need to reposition");
                        sleep(150, 500);
                        return -1;

                }
            }

        }

        if (randomIndex == 3 && readytochop) {
            debug("inside index");
            sleep(1000);
            while (!Inventory.isFull() && Equipment.contains(Predicates.nameContains("axe"))) {
                sleep(1000);
                var tree = TileObjects
                        .getSurrounding(local.getWorldLocation(), 8, 1276,1278,2091,2092)
                        .stream()
                        .filter(Reachable::isInteractable)
                        .min(Comparator.comparing(x -> x.distanceTo(local.getWorldLocation())))
                        .orElse(null);
                sleep(1000);

                if (!local.isAnimating() && !local.isInteracting() && !Inventory.isFull() && !local.isMoving() && tree != null) {
                    debug("Chop suey!");
                    tree.interact("Chop down");
                    sleep(600,1500);
                }
                if (tree == null) {
                        debug("Cannot find tree, need to reposition");
                        sleep(150, 500);
                        return -1;

                }
            }

        }

        if (randomIndex == 4 && readytochop) {
            debug("inside index");
            sleep(1000);
            while (!Inventory.isFull() && Equipment.contains(Predicates.nameContains("axe"))) {
                sleep(1000);


                var tree = TileObjects
                        .getSurrounding(local.getWorldLocation(), 8, 1276,1278,2091,2092)
                        .stream()
                        .filter(Reachable::isInteractable)
                        .min(Comparator.comparing(x -> x.distanceTo(local.getWorldLocation())))
                        .orElse(null);
                sleep(1000);

                if (!local.isAnimating() && !local.isInteracting() && !Inventory.isFull() && !local.isMoving() && tree != null) {
                    debug("Chop suey!");
                    tree.interact("Chop down");
                    sleep(600,1500);
                }
                if (tree == null) {
                        debug("Cannot find tree, need to reposition");
                        sleep(150, 500);
                        return -1;
                }
            }
        }
        return 0;
    }

}

