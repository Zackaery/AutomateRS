package net.automaters.activities.skills.woodcutting;

import com.openosrs.client.game.WorldLocation;
import net.automaters.api.entities.LocalPlayer;
import net.automaters.util.locations.woodcutting_rectangularareas;
import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.mixins.Inject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.coords.Area;
import net.unethicalite.api.coords.RectangularArea;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.pathfinder.Walker;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import net.unethicalite.api.plugins.LoopedPlugin;

import java.util.Comparator;
import java.util.Random;

import static net.automaters.api.entities.LocalPlayer.localInArea;
import static net.automaters.api.entities.SkillCheck.getAttackLevel;
import static net.automaters.api.entities.SkillCheck.getWoodcuttingLevel;
import static net.automaters.script.AutomateRS.debug;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.util.locations.woodcutting_rectangularareas.*;
import static net.automaters.util.locations.woodcutting_rectangularareas.Falador_Oak_TreeArea_V;
import static net.unethicalite.api.commons.Time.sleep;

import net.automaters.util.locations.woodcutting_rectangularareas.*;

public class WoodcuttingBored extends LoopedPlugin  {

    @Inject
    private Client client;

    // Just another woodcutting script...

    @Override
    public int loop()  {

        Player localPlayer = client.getLocalPlayer();
        var local = Players.getLocal();
        if ((local == null)|| !scriptStarted) {
            debug("Player does not exist.");
            return -1;
        }
        WorldPoint playerPosition = Players.getLocal().getWorldLocation();

        if (Inventory.isFull() || !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))) {
            // do something
            Movement.walkTo(BankLocation.getNearest());
            if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                Movement.toggleRun();
            }
            else {
                if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                    Movement.toggleRun();
                }
            }
        }

        NPC banker = NPCs.getNearest(npc -> npc.hasAction("Collect"));
        if (playerPosition.distanceTo(banker) < 4 && banker != null && !Bank.isOpen())
        {
            banker.interact("Bank");
            return -3;
        }

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("Logs"))
                || Bank.isOpen() && Inventory.isFull()
                || Bank.isOpen() && !Inventory.isFull() && Inventory.contains(Predicates.nameContains("Logs"))
                || !Inventory.isFull() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
                || Inventory.isFull() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
        ) {
            Bank.depositInventory();
        } else {

            if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("Logs"))
                    || Bank.isOpen() && Inventory.isFull()
                    || Bank.isOpen() && !Inventory.isFull() && Inventory.contains(Predicates.nameContains("Logs"))
                    || !Inventory.isFull() && Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
                    || Inventory.isFull() && Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))) {
                Bank.depositAllExcept(Predicates.nameContains("axe"));
            }
        }

        // handle gear upgrades if available

        String[] itemsToHandle = {
                "Lumberjack hat", "Lumberjack top", "Lumberjack legs", "Lumberjack boots",
                "Graceful hood", "Graceful top", "Graceful legs", "Graceful boots",
                "Graceful gloves", "Graceful cape", "ronman helm", "ron helm"
        };

        boolean lumberjackHatEquipped = Equipment.contains("Lumberjack hat");
        boolean lumberjackEquipped = Equipment.contains(Predicates.nameContains("Lumberjack"));
        boolean gracefulHoodEquipped = Equipment.contains("Graceful hood");

        for (String itemName : itemsToHandle) {
            if (itemName.startsWith("Lumberjack")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 44) {
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
            if (Bank.isOpen() && client.isMembersWorld() && !Equipment.contains(itemName)
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
                if (getWoodcuttingLevel() < 6 && getAttackLevel() < 5) {
                    continue; // Skip wearing Steel items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Black axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 11 && getAttackLevel() < 10) {
                    continue; // Skip wearing Black items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Mithril axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 21 && getAttackLevel() < 20) {
                    continue; // Skip wearing Mithril items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Adamant axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 31 && getAttackLevel() < 30) {
                    continue; // Skip wearing Adamant items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Rune axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 41 && getAttackLevel() < 40) {
                    continue; // Skip wearing Black items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Dragon axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 61 && getAttackLevel() < 60) {
                    continue; // Skip wearing Black items if Woodcutting level is less than 44
                }
            } else if (itemName.contains("Crystal axe")) {
                // Check Woodcutting level
                if (getWoodcuttingLevel() < 71 && getAttackLevel() < 70) {
                    continue; // Skip wearing Black items if Woodcutting level is less than 44
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
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(itemName), 3000);
                    }
                } else {
                    Bank.Inventory.getFirst(itemName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(itemName), 3000);
                }
            }

            // Withdraw item
            if (Bank.isOpen() && client.isMembersWorld() && !Equipment.contains(itemName)
                    && Bank.contains(itemName) && !Inventory.contains(itemName)) {
                Bank.withdraw(itemName, 1, Bank.WithdrawMode.ITEM);
            }
        }

        // Choosing tree location

        if (getWoodcuttingLevel() <= 15 && Equipment.contains(Predicates.nameContains("axe")) && !Inventory.isFull()) {
                Random random = new Random();
                int randomIndex = random.nextInt(5);

                // Check if randomIndex is within the valid range (0 to 4)
                if (randomIndex == 0 && playerPosition.distanceTo(Falador_Tree_TreeArea_I) > 2) {
                    // Handle the case when randomIndex is 0
                    // This corresponds to Falador_Tree_TreeArea_I
                    Movement.walkTo(Falador_Tree_TreeArea_I);
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                    else {
                        if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                            Movement.toggleRun();
                        }
                    }
                } else if (randomIndex == 1 && playerPosition.distanceTo(Falador_Tree_TreeArea_II) > 2) {
                    // Handle the case when randomIndex is 1
                    // This corresponds to Falador_Tree_TreeArea_II
                    Movement.walkTo(Falador_Tree_TreeArea_II);
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                    else {
                        if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                            Movement.toggleRun();
                        }
                    }
                } else if (randomIndex == 2 && playerPosition.distanceTo(Falador_Tree_TreeArea_III) > 2) {
                    // Handle the case when randomIndex is 2
                    // This corresponds to Falador_Tree_TreeArea_III
                    Movement.walkTo(Falador_Tree_TreeArea_III);
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                    else {
                        if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                            Movement.toggleRun();
                        }
                    }
                } else if (randomIndex == 3 && playerPosition.distanceTo(Falador_Tree_TreeArea_IV) > 2) {
                    // Handle the case when randomIndex is 3
                    // This corresponds to Falador_Tree_TreeArea_IV
                    Movement.walkTo(Falador_Tree_TreeArea_IV);
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                    else {
                        if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                            Movement.toggleRun();
                        }
                    }
                } else if (randomIndex == 4 && playerPosition.distanceTo(Falador_Tree_TreeArea_V) > 2) {
                    // Handle the case when randomIndex is 4
                    // This corresponds to Falador_Tree_TreeArea_V
                    Movement.walkTo(Falador_Tree_TreeArea_V);
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                    else {
                        if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                            Movement.toggleRun();
                        }
                    }
                }

        } else {
            if (getWoodcuttingLevel() > 15 && getWoodcuttingLevel() <= 30 && Equipment.contains(Predicates.nameContains("axe")) && !Inventory.isFull()) {
                Random random = new Random();
                int randomIndex = random.nextInt(5);

                // Check if randomIndex is within the valid range (0 to 4)
                if (randomIndex == 0 && playerPosition.distanceTo(Falador_Oak_TreeArea_I) > 2) {
                    // Handle the case when randomIndex is 0
                    // This corresponds to Falador_Tree_TreeArea_I
                    Movement.walkTo(Falador_Oak_TreeArea_I);
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                    else {
                        if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                            Movement.toggleRun();
                        }
                    }
                } else if (randomIndex == 1 && playerPosition.distanceTo(Falador_Oak_TreeArea_II) > 2) {
                    // Handle the case when randomIndex is 1
                    // This corresponds to Falador_Tree_TreeArea_II
                    Movement.walkTo(Falador_Oak_TreeArea_II);
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                    else {
                        if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                            Movement.toggleRun();
                        }
                    }
                } else if (randomIndex == 2 && playerPosition.distanceTo(Falador_Oak_TreeArea_III) > 2) {
                    // Handle the case when randomIndex is 2
                    // This corresponds to Falador_Tree_TreeArea_III
                    Movement.walkTo(Falador_Oak_TreeArea_III);
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                    else {
                        if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                            Movement.toggleRun();
                        }
                    }
                } else if (randomIndex == 3 && playerPosition.distanceTo(Falador_Oak_TreeArea_IV) > 2) {
                    // Handle the case when randomIndex is 3
                    // This corresponds to Falador_Tree_TreeArea_IV
                    Movement.walkTo(Falador_Oak_TreeArea_IV);
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                    else {
                        if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                            Movement.toggleRun();
                        }
                    }
                } else if (randomIndex == 4 && playerPosition.distanceTo(Falador_Oak_TreeArea_V) > 2) {
                    // Handle the case when randomIndex is 4
                    // This corresponds to Falador_Tree_TreeArea_V
                    Movement.walkTo(Falador_Oak_TreeArea_V);
                    if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                        Movement.toggleRun();
                    }
                    else {
                        if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                            Movement.toggleRun();
                        }
                    }
                }

            } else {
                if (getWoodcuttingLevel() > 30 && Equipment.contains(Predicates.nameContains("axe")) && !Inventory.isFull()) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(5);

                    // Check if randomIndex is within the valid range (0 to 4)
                    if (randomIndex == 0 && playerPosition.distanceTo(DraynorVillage_Willow_TreeArea_I) > 2) {
                        // Handle the case when randomIndex is 0
                        // This corresponds to Falador_Tree_TreeArea_I
                        Movement.walkTo(DraynorVillage_Willow_TreeArea_I);
                        if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                            Movement.toggleRun();
                        }
                        else {
                            if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                                Movement.toggleRun();
                            }
                        }
                    } else if (randomIndex == 1 && playerPosition.distanceTo(DraynorVillage_Willow_TreeArea_II) > 2) {
                        // Handle the case when randomIndex is 1
                        // This corresponds to Falador_Tree_TreeArea_II
                        Movement.walkTo(DraynorVillage_Willow_TreeArea_II);
                        if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                            Movement.toggleRun();
                        }
                        else {
                            if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                                Movement.toggleRun();
                            }
                        }
                    } else if (randomIndex == 2 && playerPosition.distanceTo(DraynorVillage_Willow_TreeArea_III) > 2) {
                        // Handle the case when randomIndex is 2
                        // This corresponds to Falador_Tree_TreeArea_III
                        Movement.walkTo(DraynorVillage_Willow_TreeArea_III);
                        if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                            Movement.toggleRun();
                        }
                        else {
                            if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                                Movement.toggleRun();
                            }
                        }
                    } else if (randomIndex == 3 && playerPosition.distanceTo(DraynorVillage_Willow_TreeArea_IV) > 2) {
                        // Handle the case when randomIndex is 3
                        // This corresponds to Falador_Tree_TreeArea_IV
                        Movement.walkTo(DraynorVillage_Willow_TreeArea_IV);
                        if (Movement.getRunEnergy() <= 10 && Movement.isRunEnabled()) {
                            Movement.toggleRun();
                        }
                        else {
                            if (Movement.getRunEnergy() >= 70 && !Movement.isRunEnabled()); {
                                Movement.toggleRun();
                            }
                        }
                    } else if (randomIndex == 4) {
                        while (playerPosition.distanceTo(DraynorVillage_Willow_TreeArea_V) > 2) {
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


        // Checking for tree and chopping

        if (playerPosition.distanceTo(Falador_Tree_TreeArea_I) < 2 && !Inventory.isFull() && Equipment.contains(Predicates.nameContains("axe"))
                || playerPosition.distanceTo(Falador_Tree_TreeArea_II) < 2 && !Inventory.isFull() && Equipment.contains(Predicates.nameContains("axe"))
                || playerPosition.distanceTo(Falador_Tree_TreeArea_III) < 2 && !Inventory.isFull() && Equipment.contains(Predicates.nameContains("axe"))
                || playerPosition.distanceTo(Falador_Tree_TreeArea_IV) < 2 && !Inventory.isFull() && Equipment.contains(Predicates.nameContains("axe"))
                || playerPosition.distanceTo(Falador_Tree_TreeArea_V) < 2 && !Inventory.isFull() && Equipment.contains(Predicates.nameContains("axe"))) {

            var tree = TileObjects
                    .getSurrounding(playerPosition, 8, "Tree")
                    .stream()
                    .min(Comparator.comparing(x -> x.distanceTo(local.getWorldLocation())))
                    .orElse(null);
            if (!localPlayer.isAnimating() && !localPlayer.isMoving()) {
                tree.interact("Chop down");
            }
        }






        // end of loop
       return 200;
    }

}
