package net.automaters.activities.skills.mining;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.Skill;
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
import net.automaters.util.locations.mining_rectangularareas;

import java.util.Comparator;
import java.util.Random;

import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.entities.SkillCheck.*;
import static net.automaters.script.AutomateRS.debug;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.util.locations.woodcutting_rectangularareas.*;

@SuppressWarnings({"ConstantConditions","unused"})
public class MiningBored extends LoopedPlugin {

    @Inject
    private Client client;

    // Just another woodcutting script...

    @Override
    public int loop() {
        debug("Holy shit this is in the loop");

        var local = Players.getLocal();
        if ((local == null)|| !scriptStarted) {
            debug("Player does not exist.");
            return -1;
        }

        WorldPoint playerPosition = Players.getLocal().getWorldLocation();

        if (Inventory.isFull() || !Inventory.contains(Predicates.nameContains("pickaxe")) && !Equipment.contains(Predicates.nameContains("pickaxe"))
                || !Inventory.isFull() && !Equipment.contains(Predicates.nameContains("pickaxe")) && Inventory.contains(Predicates.nameContains("pickaxe"))
                || !Bank.isOpen() && !Inventory.contains(Predicates.nameContains("pickaxe")) && !Equipment.contains(Predicates.nameContains("pickaxe"))) {
            // do something
            openBank();
            return 100;
        }

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("pickaxe")) && Equipment.contains(Predicates.nameContains("pickaxe"))) {
            Bank.depositInventory();
            Bank.depositEquipment();
        }


        // handle pickaxe upgrades if available

        String[] axesToHandle = {
                "Bronze pickaxe", "Iron pickaxe", "Steel pickaxe", "Black pickaxe",
                "Mithril pickaxe", "Adamant pickaxe", "Rune pickaxe", "Dragon pickaxe"
        };

        for (String itemName : axesToHandle) {
            if (itemName.contains("Steel pickaxe")) {
                // Check Woodcutting level
                if (getMiningLevel() < 6 && getAttackLevel() < 5
                        || getMiningLevel() >= 11 && getAttackLevel() >= 10 && Bank.contains("Black pickaxe")) {
                    continue; // Skip wearing Steel items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Black pickaxe")) {
                // Check Woodcutting level
                if (getMiningLevel() < 11 && getAttackLevel() < 10
                        || getMiningLevel() >= 21 && getAttackLevel() >= 20 && Bank.contains("Mithril pickaxe")) {
                    continue; // Skip wearing Black items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Mithril pickaxe")) {
                // Check Woodcutting level
                if (getMiningLevel() < 21 && getAttackLevel() < 20
                        || getMiningLevel() >= 31 && getAttackLevel() >= 30 && Bank.contains("Adamant pickaxe")) {
                    continue; // Skip wearing Mithril items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Adamant pickaxe")) {
                // Check Woodcutting level
                if (getMiningLevel() < 31 && getAttackLevel() < 30
                        || getMiningLevel() >= 41 && getAttackLevel() >= 40 && Bank.contains("Rune pickaxe")) {
                    continue; // Skip wearing Adamant items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Rune pickaxe")) {
                // Check Woodcutting level
                if (getMiningLevel() < 41 && getAttackLevel() < 40
                        || getMiningLevel() >= 61 && getAttackLevel() >= 60 && Bank.contains("Dragon pickaxe")) {
                    continue; // Skip wearing Black items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Dragon pickaxe")) {
                // Check Woodcutting level
                if (getMiningLevel() < 61 && getAttackLevel() < 60
                        || getMiningLevel() >= 71 && getAttackLevel() >= 70 && Bank.contains("Crystal pickaxe")) {
                    continue; // Skip wearing Black items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Crystal pickaxe")) {
                // Check Woodcutting level
                if (getMiningLevel() < 71 && getAttackLevel() < 70) {
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

        // Choosing mine location

        if (!local.isMoving())

        if (getMiningLevel() <= 15 && Equipment.contains(Predicates.nameContains("pickaxe")) && !Inventory.isFull()) {
            Random random = new Random();
            int randomIndex = random.nextInt(5);

            if (randomIndex == 0 && Falador_Tree_TreeArea_I.isPlayerWithinTwoTiles()) {

                Movement.walkTo(Falador_Tree_TreeArea_I);
                while (!Falador_Tree_TreeArea_I.isPlayerWithinTwoTiles()) {
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                }
            } else if (randomIndex == 1 && Falador_Tree_TreeArea_II.isPlayerWithinTwoTiles()) {

                Movement.walkTo(Falador_Tree_TreeArea_II);
                while (!Falador_Tree_TreeArea_II.isPlayerWithinTwoTiles()) {
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                }
            } else if (randomIndex == 2 && Falador_Tree_TreeArea_III.isPlayerWithinTwoTiles()) {

                Movement.walkTo(Falador_Tree_TreeArea_III);
                while (!Falador_Tree_TreeArea_III.isPlayerWithinTwoTiles()) {
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                }
            } else if (randomIndex == 3 && Falador_Tree_TreeArea_IV.isPlayerWithinTwoTiles()) {

                Movement.walkTo(Falador_Tree_TreeArea_IV);
                while (!Falador_Tree_TreeArea_IV.isPlayerWithinTwoTiles()) {
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                }
            } else if (randomIndex == 4 && Falador_Tree_TreeArea_V.isPlayerWithinTwoTiles()) {

                Movement.walkTo(Falador_Tree_TreeArea_V);
                while (!Falador_Tree_TreeArea_V.isPlayerWithinTwoTiles()) {
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                }

            } else {
                if (getMiningLevel() > 15 && getMiningLevel() <= 30 && Equipment.contains(Predicates.nameContains("pickaxe")) && !Inventory.isFull()) {
                    if (randomIndex == 0 && !Falador_Oak_TreeArea_I.isPlayerWithinTwoTiles()) {

                        Movement.walkTo(Falador_Oak_TreeArea_I);
                        while (!Falador_Oak_TreeArea_I.isPlayerWithinTwoTiles()) {
                            if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                                Movement.toggleRun();
                            } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                                Movement.toggleRun();
                            }
                        }
                    } else if (randomIndex == 1 && !Falador_Oak_TreeArea_II.isPlayerWithinTwoTiles()) {

                        Movement.walkTo(Falador_Oak_TreeArea_II);
                        while (!Falador_Oak_TreeArea_II.isPlayerWithinTwoTiles()) {
                            if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                                Movement.toggleRun();
                            } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                                Movement.toggleRun();
                            }
                        }
                    } else if (randomIndex == 2 && !Falador_Oak_TreeArea_III.isPlayerWithinTwoTiles()) {

                        Movement.walkTo(Falador_Oak_TreeArea_III);
                        while (!Falador_Oak_TreeArea_III.isPlayerWithinTwoTiles()) {
                            if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                                Movement.toggleRun();
                            } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                                Movement.toggleRun();
                            }
                        }
                    } else if (randomIndex == 3 && !Falador_Oak_TreeArea_IV.isPlayerWithinTwoTiles()) {

                        Movement.walkTo(Falador_Oak_TreeArea_IV);
                        while (!Falador_Oak_TreeArea_IV.isPlayerWithinTwoTiles()) {
                            if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                                Movement.toggleRun();
                            } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                                Movement.toggleRun();
                            }
                        }
                    } else if (randomIndex == 4 && !Falador_Oak_TreeArea_V.isPlayerWithinTwoTiles()) {

                        Movement.walkTo(Falador_Oak_TreeArea_V);
                        while (!Falador_Oak_TreeArea_V.isPlayerWithinTwoTiles()) {
                            if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                                Movement.toggleRun();
                            } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                                Movement.toggleRun();
                            }
                        }

                    } else {
                        if (getMiningLevel() > 30 && Equipment.contains(Predicates.nameContains("pickaxe")) && !Inventory.isFull()) {
                            if (randomIndex == 0 && !DraynorVillage_Willow_TreeArea_I.isPlayerWithinTwoTiles()) {

                                Movement.walkTo(DraynorVillage_Willow_TreeArea_I);

                                while (!DraynorVillage_Willow_TreeArea_I.isPlayerWithinTwoTiles()) {
                                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                                        Movement.toggleRun();
                                    } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                                        Movement.toggleRun();
                                    }
                                }
                            } else if (randomIndex == 1 && !DraynorVillage_Willow_TreeArea_II.isPlayerWithinTwoTiles()) {

                                Movement.walkTo(DraynorVillage_Willow_TreeArea_II);
                                while (!DraynorVillage_Willow_TreeArea_II.isPlayerWithinTwoTiles()) {
                                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                                        Movement.toggleRun();
                                    } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                                        Movement.toggleRun();
                                    }
                                }
                            } else if (randomIndex == 2 && !DraynorVillage_Willow_TreeArea_III.isPlayerWithinTwoTiles()) {

                                Movement.walkTo(DraynorVillage_Willow_TreeArea_III);
                                while (!DraynorVillage_Willow_TreeArea_III.isPlayerWithinTwoTiles()) {
                                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                                        Movement.toggleRun();
                                    } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                                        Movement.toggleRun();
                                    }
                                }
                            } else if (randomIndex == 3 && !DraynorVillage_Willow_TreeArea_IV.isPlayerWithinTwoTiles()) {

                                Movement.walkTo(DraynorVillage_Willow_TreeArea_IV);
                                while (!DraynorVillage_Willow_TreeArea_IV.isPlayerWithinTwoTiles()) {
                                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                                        Movement.toggleRun();
                                    } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                                        Movement.toggleRun();
                                    }
                                }
                            } else if (randomIndex == 4 && !DraynorVillage_Willow_TreeArea_IV.isPlayerWithinTwoTiles()) {
                                while (!DraynorVillage_Willow_TreeArea_V.isPlayerWithinTwoTiles()) {
                                    Movement.walkTo(DraynorVillage_Willow_TreeArea_V);

                                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                                        Movement.toggleRun();
                                    } else if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()) {
                                        Movement.toggleRun();
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        // end of choosing area

                // Checking for tree and chopping

        // normal tree
                if (Falador_Tree_TreeArea_I.isPlayerWithinTwoTiles()
                        || Falador_Tree_TreeArea_II.isPlayerWithinTwoTiles()
                        || Falador_Tree_TreeArea_III.isPlayerWithinTwoTiles()
                        || Falador_Tree_TreeArea_IV.isPlayerWithinTwoTiles()
                        || Falador_Tree_TreeArea_V.isPlayerWithinTwoTiles()) {
                     if (!Inventory.isFull() && Equipment.contains(Predicates.nameContains("pickaxe"))) {

                        var tree = TileObjects
                                .getSurrounding(playerPosition, 8, "Tree")
                                .stream()
                                .filter(tileObject -> Reachable.isWalkable(tileObject.getWorldLocation()))
                                .min(Comparator.comparing(x -> x.distanceTo(local.getWorldLocation())))
                                .orElse(null);

                        if (!local.isAnimating() && !local.isMoving() && tree != null) {
                            tree.interact("Chop down");
                        }
                    }
                }

                // oak tree

        if (Falador_Oak_TreeArea_I.isPlayerWithinTwoTiles()
                || Falador_Oak_TreeArea_II.isPlayerWithinTwoTiles()
                || Falador_Oak_TreeArea_III.isPlayerWithinTwoTiles()
                || Falador_Oak_TreeArea_IV.isPlayerWithinTwoTiles()
                || Falador_Oak_TreeArea_V.isPlayerWithinTwoTiles()) {
            if (!Inventory.isFull() && Equipment.contains(Predicates.nameContains("pickaxe"))) {

                var tree = TileObjects
                        .getSurrounding(playerPosition, 8, "Oak Tree")
                        .stream()
                        .filter(tileObject -> Reachable.isWalkable(tileObject.getWorldLocation()))
                        .min(Comparator.comparing(x -> x.distanceTo(local.getWorldLocation())))
                        .orElse(null);

                if (!local.isAnimating() && !local.isMoving() && tree != null) {
                    tree.interact("Chop down");
                }
            }
        }

        // willow tree

        if (DraynorVillage_Willow_TreeArea_I.isPlayerWithinTwoTiles()
                || DraynorVillage_Willow_TreeArea_II.isPlayerWithinTwoTiles()
                || DraynorVillage_Willow_TreeArea_III.isPlayerWithinTwoTiles()
                || DraynorVillage_Willow_TreeArea_IV.isPlayerWithinTwoTiles()
                || DraynorVillage_Willow_TreeArea_V.isPlayerWithinTwoTiles()) {
            if (!Inventory.isFull() && Equipment.contains(Predicates.nameContains("pickaxe"))) {

                var tree = TileObjects
                        .getSurrounding(playerPosition, 8, "Willow Tree")
                        .stream()
                        .filter(tileObject -> Reachable.isWalkable(tileObject.getWorldLocation()))
                        .min(Comparator.comparing(x -> x.distanceTo(local.getWorldLocation())))
                        .orElse(null);

                if (!local.isAnimating() && !local.isMoving() && tree != null) {
                    tree.interact("Chop down");
                }
            }
        }




                // end of loop
                return 200;
            }
        }

