package net.automaters.activities.skills.woodcutting;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.mixins.Inject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import net.unethicalite.api.plugins.LoopedPlugin;
import net.automaters.util.locations.woodcutting_rectangularareas;

import java.util.Random;

import static net.unethicalite.api.commons.Time.sleep;
@SuppressWarnings({"ConstantConditions","unused"})
public class Woodcutting_II extends LoopedPlugin {

    @Inject
    private Client client;

    @Override

    public int loop() {


        // strings

        String Account_Type = "Unknown";
        String Task_Goal = "null";
        String Current_Task = "null";
        String Obtain_BronzeAxe = "null";
        String Obtain_AXE = "null";

        // player location
        var local = Players.getLocal();
        WorldPoint playerLocation = local != null ? local.getWorldLocation() : null;

        boolean notatclosestbank = BankLocation.getNearest() != null && playerLocation != null && playerLocation.distanceTo(BankLocation.getNearest().getArea()) > 4;

        boolean atclosestbank = BankLocation.getNearest() != null && playerLocation != null && playerLocation.distanceTo(BankLocation.getNearest().getArea()) <= 4;
        boolean gecontainsplayer = woodcutting_rectangularareas.SHOP_GRAND_EXCHANGE_FS.getArea().contains(Players.getLocal().getWorldLocation());
        boolean ramcontainsplayer = woodcutting_rectangularareas.Region_AsgarniaMisthalin.getArea().contains(Players.getLocal().getWorldLocation());
        boolean ramicontainsplayer = woodcutting_rectangularareas.Region_AsgarniaMisthalin_I.getArea().contains(Players.getLocal().getWorldLocation());
        boolean ramiicontainsplayer = woodcutting_rectangularareas.Region_AsgarniaMisthalin_II.getArea().contains(Players.getLocal().getWorldLocation());
        boolean noramcontainsplayer = !woodcutting_rectangularareas.Region_AsgarniaMisthalin.getArea().contains(Players.getLocal().getWorldLocation());
        boolean noramicontainsplayer = !woodcutting_rectangularareas.Region_AsgarniaMisthalin_I.getArea().contains(Players.getLocal().getWorldLocation());
        boolean noramiicontainsplayer = !woodcutting_rectangularareas.Region_AsgarniaMisthalin_II.getArea().contains(Players.getLocal().getWorldLocation());
        boolean HopWorlds = false;
        boolean Random_Skilling_Task = false;
        boolean REGION_ASGARNIA_MISTHALIN = false;
        boolean REGION_KANDARIN = false;
        boolean REGION_ZEAH = false;
        boolean SELECTED_SHOP = false;
        boolean SHOP_GRAND_EXCHANGE = false;
        boolean SHOP_BOBS_AXES = false;
        boolean SWITCH_SHOP = false;
        boolean COMPLETED_GOAL_WOODCUTTING = false;
        boolean COMPLETED_GOAL_FIREMAKING = false;
        boolean COMPLETED_GOAL_FLETCHING = false;
        boolean BuyItems = false;
        boolean Obtain_Knife = false;
        boolean Obtain_Tinderbox = false;
        boolean isWearingPickaxe = Equipment.contains(Predicates.nameContains("pickaxe"));

        // integers

        int RandomizedSelector = 0;
        int RandomizedSelectorX = 0;
        int Randomizer = 0;
        int RandomTask = 0;
        int Random_Increase = 0;
        int CURRENT_PRIORITIZATION = 1;
        int getwoodcuttingskill = client.getBoostedSkillLevel(Skill.WOODCUTTING);
        int getfishingskill = client.getBoostedSkillLevel(Skill.FISHING);

        // start of script

        // method 1

        if ((RandomTask >= 41 && RandomTask < 71) ||
                (Bank.isOpen() && !Inventory.contains("Knife") && !Bank.contains("Knife") && RandomTask >= 41 && RandomTask < 71) ||
                (Bank.isOpen() && !Inventory.contains("Tinderbox") && !Bank.contains("Tinderbox") && RandomTask >= 71) ||
                (!COMPLETED_GOAL_FIREMAKING && RandomTask >= 71) ||
                (!COMPLETED_GOAL_FLETCHING && RandomTask >= 41 && RandomTask < 71)) {

            // Generate a new random number between 1 and 100 (inclusive) and assign it to RandomTask
            Random random = new Random();
            RandomTask = random.nextInt(100) + 1;
        }

        // method 2

        if (notatclosestbank && !Bank.isOpen() && BuyItems && Obtain_BronzeAxe.equals("null") && !Inventory.contains(Predicates.nameContains("axe"))
                && !Equipment.contains(Predicates.nameContains("axe")) || RandomTask >= 41 && RandomTask < 71 && notatclosestbank && !Bank.isOpen()
                && BuyItems && !Inventory.contains("Knife") || RandomTask >= 71 && notatclosestbank && !Bank.isOpen()
                && BuyItems && !Inventory.contains("Tinderbox") || !Bank.isOpen() && notatclosestbank && !BuyItems && SELECTED_SHOP
                || !Bank.isOpen() && notatclosestbank && !Inventory.contains("Coins") && !BuyItems && !SHOP_BOBS_AXES || !Bank.isOpen() && notatclosestbank && !Inventory.contains("Coins") && !BuyItems
                && !SHOP_GRAND_EXCHANGE && woodcutting_rectangularareas.SHOP_GRAND_EXCHANGE_FS.getArea().contains(Players.getLocal().getWorldLocation())
                || !Bank.isOpen() && notatclosestbank && Inventory.contains("Coins") && !BuyItems && !SHOP_GRAND_EXCHANGE
                && !woodcutting_rectangularareas.SHOP_GRAND_EXCHANGE_FS.getArea().contains(Players.getLocal().getWorldLocation())
                || !Bank.isOpen() && notatclosestbank && Equipment.contains(Predicates.nameContains("pickaxe")) || !Bank.isOpen() && notatclosestbank
                && Inventory.contains(Predicates.nameContains("pickaxe"))) {
            // walk to closest bank
            WorldPoint nearestBankLocation = BankLocation.getNearest().getArea().getCenter();
            Movement.walkTo(nearestBankLocation.getX(), nearestBankLocation.getY(), nearestBankLocation.getPlane());

        }

        // method 3

        if (!Bank.isOpen() && notatclosestbank && BuyItems && Obtain_BronzeAxe.equals("null") && Inventory.contains(Predicates.nameContains("axe"))
                && Equipment.contains(Predicates.nameContains("axe")) || RandomTask >= 41 && RandomTask < 71 && notatclosestbank && !Bank.isOpen()
                && BuyItems && !Inventory.contains("Knife") || RandomTask >= 71 && notatclosestbank && !Bank.isOpen() && BuyItems && !Inventory.contains("Tinderbox")
                || !Bank.isOpen() && notatclosestbank && !BuyItems && SELECTED_SHOP || !Bank.isOpen() && notatclosestbank && !Inventory.contains("Coins") && !BuyItems && !SHOP_BOBS_AXES
                || !Bank.isOpen() && notatclosestbank && !Inventory.contains("Coins") && !BuyItems && !SHOP_GRAND_EXCHANGE && gecontainsplayer || !Bank.isOpen() && notatclosestbank
                && Inventory.contains("Coins") && !BuyItems && !SHOP_GRAND_EXCHANGE && !gecontainsplayer || !Bank.isOpen() && notatclosestbank && Equipment.contains(Predicates.nameContains("pickaxe"))
                || !Bank.isOpen() && notatclosestbank && Inventory.contains(Predicates.nameContains("pickaxe")))
        {
            // walk to closest bank
            WorldPoint nearestBankLocation = BankLocation.getNearest().getArea().getCenter();
            Movement.walkTo(nearestBankLocation.getX(), nearestBankLocation.getY(), nearestBankLocation.getPlane());

        }

        // method 4

        if (BuyItems && atclosestbank && !Bank.isOpen() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
                || BuyItems && atclosestbank && !Bank.isOpen() && RandomTask >= 41 && RandomTask < 71 && !Inventory.contains("Knife") || BuyItems && atclosestbank && !Bank.isOpen()
                && RandomTask >= 71 && !Inventory.contains("Tinderbox") || !Bank.isOpen() && atclosestbank && !BuyItems && SELECTED_SHOP || !Bank.isOpen() && atclosestbank && !Inventory.contains("Coins")
                && !BuyItems && !SHOP_BOBS_AXES || !Bank.isOpen() && atclosestbank && !Inventory.contains("Coins") && !BuyItems && !SHOP_GRAND_EXCHANGE && gecontainsplayer || !Bank.isOpen()
                && atclosestbank && Inventory.contains("Coins") && !BuyItems && !SHOP_GRAND_EXCHANGE && !gecontainsplayer || !Bank.isOpen() && atclosestbank && Equipment.contains(Predicates.nameContains("pickaxe"))
                || !Bank.isOpen() && atclosestbank && Inventory.contains(Predicates.nameContains("pickaxe"))) {

            NPC banker = NPCs.getNearest(npc -> npc.hasAction("Collect"));
            if (banker != null)
            {
                banker.interact("Bank");
                return -3;
            }
            sleep(10000);

        }

        // method 5 - 27

        if (Bank.isOpen()) {
            if (Inventory.getCount("Coins") >= 1 && !gecontainsplayer) {
                SHOP_GRAND_EXCHANGE = true;
                Bank.depositAll("Coins");
            } else if (isWearingPickaxe) {
                Bank.depositEquipment();
            } else if (Inventory.contains(Predicates.nameContains("pickaxe"))) {
                Bank.depositAllExcept("Lamp", "Knife", "Tinderbox");
            } else if (!Bank.contains("Bronze axe") && !Inventory.contains("Bronze axe") && getwoodcuttingskill < 6 && Obtain_AXE.equals("null")) {
                BuyItems = true;
                Obtain_AXE = "Bronze axe";
            } else if (!Bank.contains("Steel axe") && !Inventory.contains("Steel axe") && getwoodcuttingskill >= 6 && getwoodcuttingskill < 21 && Obtain_AXE.equals("null")) {
                BuyItems = true;
                Obtain_AXE = "Steel axe";
            } else if (!Bank.contains("Mithril axe") && !Inventory.contains("Mithril axe") && getwoodcuttingskill >= 21 && getwoodcuttingskill < 31 && Obtain_AXE.equals("null")) {
                BuyItems = true;
                Obtain_AXE = "Mithril axe";
            } else if (!Bank.contains("Adamant axe") && !Inventory.contains("Adamant axe") && getwoodcuttingskill >= 31 && getwoodcuttingskill < 41 && Obtain_AXE.equals("null")) {
                BuyItems = true;
                Obtain_AXE = "Adamant axe";
            } else if (!Bank.contains("Rune axe") && !Inventory.contains("Rune axe") && getwoodcuttingskill >= 41 && getwoodcuttingskill < 51 && Obtain_AXE.equals("null")) {
                BuyItems = true;
                Obtain_AXE = "Rune axe";
            } else if (!Bank.contains("Dragon axe") && !Inventory.contains("Dragon axe") && getwoodcuttingskill >= 61 && Obtain_AXE.equals("null")) {
                BuyItems = true;
                Obtain_AXE = "Dragon axe";
            }
        }


        if (client.isMembersWorld()) {
            if (!Bank.contains("Knife") && !Inventory.contains("Knife") && RandomTask >= 41 && RandomTask < 71 && Account_Type.equals("Normal")) {
                BuyItems = true;
                Obtain_Knife = true;
            }
        } else if (!Bank.contains("Knife") && !Bank.contains("Tinderbox") && !Inventory.contains("Knife") && !Inventory.contains("Tinderbox") && RandomTask >= 41 && RandomTask < 71) {
            Obtain_Knife = false;
            RandomTask = 10;
        }

        if (Account_Type.equals("Normal")) {
            if (!Bank.contains("Tinderbox") && !Inventory.contains("Tinderbox") && RandomTask >= 71) {
                Obtain_Tinderbox = true;
                RandomTask = 10;
            }
        }

        if (Obtain_AXE.equals("Mithril axe") || Obtain_AXE.equals("Adamant axe") || Obtain_AXE.equals("Rune axe") || Obtain_AXE.equals("Dragon axe")) {
            if (!Account_Type.equals("Normal") && !Inventory.contains(Obtain_AXE) && !Bank.contains(Obtain_AXE) && !Equipment.contains(Obtain_AXE)) {
                Obtain_AXE = "Steel axe";
                sleep(100, 150);
            }
        }

        if (Obtain_AXE.equals("Dragon axe") && Account_Type.equals("Normal") && (!client.isMembersWorld() || (Bank.isOpen() && Bank.getCount("Coins") < 70000 && Inventory.getCount("Coins") < 70000 && !Inventory.contains("Dragon axe") && !Bank.contains("Dragon axe") && !Equipment.contains("Dragon axe")))) {
            Obtain_AXE = "Rune axe";
            sleep(100, 150);
        }

        if (Obtain_AXE.equals("Rune axe") && Account_Type.equals("Normal") && Bank.isOpen() && Bank.getCount("Coins") < 8000 && Inventory.getCount("Coins") < 8000 && !Inventory.contains("Rune axe") && !Bank.contains("Rune axe") && !Equipment.contains("Rune axe")) {
            Obtain_AXE = "Adamant axe";
            sleep(100, 150);
        }

        if (Obtain_AXE.equals("Adamant axe") && Account_Type.equals("Normal") && Bank.isOpen() && Bank.getCount("Coins") < 500 && Inventory.getCount("Coins") < 500 && !Inventory.contains("Adamant axe") && !Bank.contains("Adamant axe") && !Equipment.contains("Adamant axe")) {
            Obtain_AXE = "Mithril axe";
            sleep(100, 150);
        }

        if (Obtain_AXE.equals("Mithril axe") && Account_Type.equals("Normal") && Bank.isOpen() && Bank.getCount("Coins") < 250 && Inventory.getCount("Coins") < 250 && !Inventory.contains("Mithril axe") && !Bank.contains("Mithril axe") && !Equipment.contains("Mithril axe")) {
            Obtain_AXE = "Steel axe";
            sleep(100, 150);
        }

        if (Obtain_AXE.equals("Steel axe") && Account_Type.equals("Normal") && (Bank.isOpen() && Bank.getCount("Coins") < 100 && Inventory.getCount("Coins") < 100 && !Inventory.contains("Steel axe") && !Bank.contains("Steel axe") && !Equipment.contains("Steel axe") || (!Account_Type.equals("Normal") && Bank.isOpen() && Bank.getCount("Coins") < 200 && Inventory.getCount("Coins") < 200 && !Inventory.contains("Steel axe") && !Bank.contains("Steel axe") && !Equipment.contains("Steel axe")))) {
            Obtain_AXE = "Bronze axe";
            sleep(100, 150);
        }

        if ((Obtain_AXE.equals("Dragon axe") && (Inventory.contains("Dragon axe") || (Bank.isOpen() && Bank.contains("Dragon axe")) || Equipment.contains("Dragon axe"))) || (Obtain_AXE.equals("Rune axe") && (Inventory.contains("Rune axe") || (Bank.isOpen() && Bank.contains("Rune axe")) || Equipment.contains("Rune axe"))) || (Obtain_AXE.equals("Adamant axe") && (Inventory.contains("Adamant axe") || (Bank.isOpen() && Bank.contains("Adamant axe")) || Equipment.contains("Adamant axe"))) || (Obtain_AXE.equals("Mithril axe") && (Inventory.contains("Mithril axe") || (Bank.isOpen() && Bank.contains("Mithril axe")) || Equipment.contains("Mithril axe"))) || (Obtain_AXE.equals("Steel axe") && (Inventory.contains("Steel axe") || (Bank.isOpen() && Bank.contains("Steel axe")) || Equipment.contains("Steel axe"))) || (Obtain_AXE.equals("Bronze axe") && (Inventory.contains("Bronze axe") || (Bank.isOpen() && Bank.contains("Bronze axe")) || Equipment.contains("Bronze axe")))) {
            Obtain_AXE = "false";
        }

        // methods 36 - 59

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
                if (getwoodcuttingskill < 44) {
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
                    && getwoodcuttingskill >= 44) {
                Bank.withdraw(itemName, 1, Bank.WithdrawMode.ITEM);
            }
        }



        return 0;
    }
}
