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
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.plugins.LoopedPlugin;

import java.util.Comparator;
import java.util.Random;

import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.entities.SkillCheck.getAttackLevel;
import static net.automaters.api.entities.SkillCheck.getWoodcuttingLevel;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.script.AutomateRS.debug;
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

        WorldPoint playerPosition = Players.getLocal().getWorldLocation();

        if (Inventory.isFull() || !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
                || !Inventory.isFull() && !Equipment.contains(Predicates.nameContains("axe")) && Inventory.contains(Predicates.nameContains("axe"))
                || !Bank.isOpen() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))) {
            // do something
            openBank();
            return 100;
        }

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("axe")) && Equipment.contains(Predicates.nameContains("axe"))) {
            Bank.depositInventory();
            Bank.depositEquipment();
        }

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("Logs"))
                || Bank.isOpen() && Inventory.isFull()
                || Bank.isOpen() && !Inventory.isFull() && Inventory.contains(Predicates.nameContains("Logs"))
                || !Inventory.isFull() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
                || Inventory.isFull() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
        ) {
            Bank.depositInventory();
            return 100;
        } else {

            if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("Logs"))
                    || Bank.isOpen() && Inventory.isFull()
                    || Bank.isOpen() && !Inventory.isFull() && Inventory.contains(Predicates.nameContains("Logs"))
                    || !Inventory.isFull() && Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
                    || Inventory.isFull() && Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))) {
                Bank.depositInventory();
                return 100;
            }
        }

        // handle gear upgrades if available

        String[] itemsToHandle = {
                "Lumberjack hat", "Lumberjack top", "Lumberjack legs", "Lumberjack boots",
                "Graceful hood", "Graceful top", "Graceful legs", "Graceful boots",
                "Graceful gloves", "Graceful cape", "ronman helm", "ron helm"
        };

        boolean lumberjackHatEquipped = Equipment.contains("Lumberjack hat");
        boolean gracefulHoodEquipped = Equipment.contains("Graceful hood");

        for (String itemName : itemsToHandle) {
            if (itemName.startsWith("Lumberjack")) {
                // Check Woodcutting level
                if (Skills.getBoostedLevel(Skill.WOODCUTTING) < 44) {
                    continue; // Skip wearing Lumberjack items if Woodcutting level is less than 44
                }
            } else if (itemName.startsWith("Graceful")) {
                // Wear Graceful items only if Lumberjack isn't equipped and Graceful hood isn't equipped
                if (!lumberjackHatEquipped) {
                    continue; // Skip wearing Graceful items if Lumberjack is equipped
                }
            } else if (itemName.startsWith("ronman") || itemName.startsWith("ron")) {
                // Wear Graceful items only if Lumberjack isn't equipped and Graceful hood isn't equipped
                if (!lumberjackHatEquipped || !gracefulHoodEquipped) {
                    continue; // Skip wearing Graceful items if Lumberjack hat or Graceful hood is equipped
                }
            }

            // Wear item
            if (Inventory.contains(itemName) && !Equipment.contains(itemName)) {
                if (!Bank.isOpen()) {
                    Item item = Inventory.getFirst(itemName);
                    if (item != null) {
                        item.interact("Wear");
                        Time.sleepUntil(() -> Equipment.contains(itemName), 3000);
                    }
                } else {
                    Bank.Inventory.getFirst(itemName).interact("Wear");
                    Time.sleepUntil(() -> Equipment.contains(itemName), 3000);
                }
            }

            // Withdraw item
            if (Bank.isOpen() && !Equipment.contains(itemName)
                    && Bank.contains(itemName) && !Inventory.contains(itemName)
                    && getWoodcuttingLevel() >= 44) {
                Bank.withdraw(itemName, 1, Bank.WithdrawMode.ITEM);
            }
        }

        // handle axe upgrades if available

        String[] axesToHandle = {
                "Bronze axe", "Iron axe", "Steel axe", "Black axe",
                "Mithril axe", "Adamant axe", "Rune axe", "Dragon axe"
        };

        for (String itemName : axesToHandle) {
            if (itemName.contains("Steel axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 6 && getAttackLevel() < 5
                        || getWoodcuttingLevel() >= 11 && getAttackLevel() >= 10 && Bank.contains("Black axe")) {
                    continue; // Skip wearing Steel items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Black axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 11 && getAttackLevel() < 10
                        || getWoodcuttingLevel() >= 21 && getAttackLevel() >= 20 && Bank.contains("Mithril axe")) {
                    continue; // Skip wearing Black items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Mithril axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 21 && getAttackLevel() < 20
                        || getWoodcuttingLevel() >= 31 && getAttackLevel() >= 30 && Bank.contains("Adamant axe")) {
                    continue; // Skip wearing Mithril items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Adamant axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 31 && getAttackLevel() < 30
                        || getWoodcuttingLevel() >= 41 && getAttackLevel() >= 40 && Bank.contains("Rune axe")) {
                    continue; // Skip wearing Adamant items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Rune axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 41 && getAttackLevel() < 40
                        || getWoodcuttingLevel() >= 61 && getAttackLevel() >= 60 && Bank.contains("Dragon axe")) {
                    continue; // Skip wearing Black items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Dragon axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 61 && getAttackLevel() < 60
                        || getWoodcuttingLevel() >= 71 && getAttackLevel() >= 70 && Bank.contains("Crystal axe")) {
                    continue; // Skip wearing Black items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Crystal axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 71 && getAttackLevel() < 70) {
                    continue; // Skip wearing Black items if Woodcutting level is less than 44
                }
            }


            // Wear item
            if (Inventory.contains(itemName) && !Equipment.contains(itemName)) {
                if (!Bank.isOpen()) {
                    Item item = Inventory.getFirst(itemName);
                    if (item != null) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(itemName), 3000);
                    }
                } else {
                    Bank.Inventory.getFirst(itemName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(itemName), 3000);
                }
            }

            // Withdraw item
            if (Bank.isOpen() && !Equipment.contains(itemName)
                    && Bank.contains(itemName) && !Inventory.contains(itemName)) {
                Bank.withdraw(itemName, 1, Bank.WithdrawMode.ITEM);
            }
        }

        // Choosing tree location

        if (getWoodcuttingLevel() <= 15 && Equipment.contains(Predicates.nameContains("axe")) && !Inventory.isFull()) {
            debug("Choosing location");
            Random random = new Random();
            int randomIndex = random.nextInt(5);

            debug("Location chosen: " + randomIndex);

            if (randomIndex == 0) {

                debug("Walking");

                automateWalk(Falador_Tree_TreeArea_I_Area.toWorldArea());
                while (!playerPosition.isInArea(Falador_Tree_TreeArea_I_Area.toWorldArea())) {
                    sleep(1500, 3000);
                    debug("walking 2");
                    automateWalk(Falador_Tree_TreeArea_I_Area.toWorldArea());
                }
                if (!Inventory.isFull() && !playerPosition.isInArea(Falador_Tree_TreeArea_I_Area.toWorldArea())) {
                    while (!playerPosition.isInArea(Falador_Tree_TreeArea_I_Area.toWorldArea())) {
                        sleep(1500, 3000);
                        debug("walking 2");
                        automateWalk(Falador_Tree_TreeArea_I_Area.toWorldArea());
                    }

                    if (randomIndex == 1) {

                        automateWalk(Falador_Tree_TreeArea_II_Area.toWorldArea());
                        while (!playerPosition.isInArea(Falador_Tree_TreeArea_II_Area.toWorldArea())) {
                            sleep(1500, 3000);
                            debug("walking 2");
                            automateWalk(Falador_Tree_TreeArea_II_Area.toWorldArea());
                        }
                        if (!Inventory.isFull() && !playerPosition.isInArea(Falador_Tree_TreeArea_II_Area.toWorldArea())) {
                            while (!playerPosition.isInArea(Falador_Tree_TreeArea_II_Area.toWorldArea())) {
                                sleep(1500, 3000);
                                debug("walking 2");
                                automateWalk(Falador_Tree_TreeArea_II_Area.toWorldArea());
                            }
                        }
                        if (randomIndex == 2) {

                            automateWalk(Falador_Tree_TreeArea_III_Area.toWorldArea());
                            while (!playerPosition.isInArea(Falador_Tree_TreeArea_III_Area.toWorldArea())) {
                                sleep(1500, 3000);
                                debug("walking 2");
                                automateWalk(Falador_Tree_TreeArea_III_Area.toWorldArea());
                            }
                            if (!Inventory.isFull() && !playerPosition.isInArea(Falador_Tree_TreeArea_III_Area.toWorldArea())) {
                                while (!playerPosition.isInArea(Falador_Tree_TreeArea_III_Area.toWorldArea())) {
                                    sleep(1500, 3000);
                                    debug("walking 2");
                                    automateWalk(Falador_Tree_TreeArea_III_Area.toWorldArea());
                                }
                            }
                            if (randomIndex == 3) {

                                automateWalk(Falador_Tree_TreeArea_IV_Area.toWorldArea());
                                while (!playerPosition.isInArea(Falador_Tree_TreeArea_IV_Area.toWorldArea())) {
                                    sleep(1500, 3000);
                                    debug("walking 2");
                                    automateWalk(Falador_Tree_TreeArea_IV_Area.toWorldArea());
                                }
                                if (!Inventory.isFull() && !playerPosition.isInArea(Falador_Tree_TreeArea_IV_Area.toWorldArea())) {
                                    while (!playerPosition.isInArea(Falador_Tree_TreeArea_IV_Area.toWorldArea())) {
                                        sleep(1500, 3000);
                                        debug("walking 2");
                                        automateWalk(Falador_Tree_TreeArea_IV_Area.toWorldArea());
                                    }
                                }
                                if (randomIndex == 4) {

                                    automateWalk(Falador_Tree_TreeArea_V_Area.toWorldArea());
                                    while (!playerPosition.isInArea(Falador_Tree_TreeArea_V_Area.toWorldArea())) {
                                        sleep(1500, 3000);
                                        debug("walking 2");
                                        automateWalk(Falador_Tree_TreeArea_V_Area.toWorldArea());
                                    }
                                    if (!Inventory.isFull() && !playerPosition.isInArea(Falador_Tree_TreeArea_V_Area.toWorldArea())) {
                                        while (!playerPosition.isInArea(Falador_Tree_TreeArea_V_Area.toWorldArea())) {
                                            sleep(1500, 3000);
                                            debug("walking 2");
                                            automateWalk(Falador_Tree_TreeArea_V_Area.toWorldArea());
                                        }
                                        debug("1");
                                    }
                                    debug("2");
                                }
                                debug("3");
                                // end of choosing area

                            }
                            debug("4");
                        }
                        debug("5");
                    }
                    debug("6");
                }
                debug("7");
            }
            debug("8");
            // normal tree
            if (playerPosition.isInArea(Falador_Tree_TreeArea_I_Area.toWorldArea())
                    || playerPosition.isInArea(Falador_Tree_TreeArea_II_Area.toWorldArea())
                    || playerPosition.isInArea(Falador_Tree_TreeArea_III_Area.toWorldArea())
                    || playerPosition.isInArea(Falador_Tree_TreeArea_IV_Area.toWorldArea())
                    || playerPosition.isInArea(Falador_Tree_TreeArea_V_Area.toWorldArea())) {
                debug("I should chop a tree down...");
                while (!Inventory.isFull() && Equipment.contains(Predicates.nameContains("axe"))) {
                    debug("Chop suey!");

                    var tree = TileObjects
                            .getSurrounding(playerPosition, 8, 1276,1278)
                            .stream()
                            .filter(tileObject -> Reachable.isWalkable(tileObject.getWorldLocation()))
                            .min(Comparator.comparing(x -> x.distanceTo(playerPosition)))
                            .orElse(null);

                    if (!local.isAnimating() && !local.isMoving() && tree != null) {
                        tree.interact("Chop down");
                    }
                }
            }
        }
        debug("9");
        return 0;
    }

}

