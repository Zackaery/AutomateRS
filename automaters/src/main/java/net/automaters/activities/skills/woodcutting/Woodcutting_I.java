package net.automaters.activities.skills.woodcutting;
import net.automaters.util.locations.woodcutting_rectangularareas;
import net.automaters.util.timers.globaltimers;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.mixins.Inject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import net.unethicalite.api.plugins.LoopedPlugin;

import javax.swing.*;
import java.util.Random;

import static net.unethicalite.api.commons.Time.sleep;


public class Woodcutting_I extends LoopedPlugin {

    // Booleans

    public static boolean HopWorlds = false;
    public static boolean Random_Skilling_Task = true;
    public static boolean REGION_ASGARNIA_MISTHALIN = false;
    public static boolean REGION_KANDARIN = false;
    public static boolean REGION_ZEAH = false;
    public static boolean SELECTED_SHOP = false;
    public static boolean SHOP_GRAND_EXCHANGE = false;
    public static boolean SHOP_BOBS_AXES = false;
    public static boolean SWITCH_SHOP = false;
    public static boolean COMPLETED_GOAL_WOODCUTTING = false;
    public static boolean COMPLETED_GOAL_FIREMAKING = false;
    public static boolean COMPLETED_GOAL_FLETCHING = false;
    public static boolean BuyItems = false;
    public static boolean Obtain_Knife = false;
    public static boolean Obtain_Tinderbox = false;

    // integers

    public static int RandomizedSelector = 0;
    public static int RandomizedSelectorX = 0;
    public static int Randomizer = 0;
    public static int RandomTask = 0;
    public static int Random_Increase = 0;
    public static int CURRENT_PRIORITIZATION = 1;

    // strings

    public static String Accoount_Type = "Unknown";
    public static String Task_Goal = null;
    public static String Current_Task = null;
    public static String Obtain_BronzeAxe = null;
    public static String Obtain_AXE = null;

    // timers

    Timer timer1 = globaltimers.getTimer1();
    Timer timer2 = globaltimers.getTimer2();

    // start of script
    @Inject
    private Client client;
    boolean isItemEquipped(ItemID itemName) {
        final ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
        return equipment != null && equipment.count() == 1;
    }

    boolean isWearingPickaxe = Equipment.contains(Predicates.nameContains("pickaxe"));

    @Override
    public int loop() {
        // start of script

        // player location
        var local = Players.getLocal();
        WorldPoint playerLocation = local != null ? local.getWorldLocation() : null;

        boolean notatclosestbank = BankLocation.getNearest() != null && playerLocation.distanceTo(BankLocation.getNearest().getArea()) > 4;
        boolean atclosestbank = BankLocation.getNearest() != null && playerLocation.distanceTo(BankLocation.getNearest().getArea()) <= 4;
        boolean gecontainsplayer = woodcutting_rectangularareas.SHOP_GRAND_EXCHANGE_FS.getArea().contains(Players.getLocal().getWorldLocation());
        int getwoodcuttingskill = client.getBoostedSkillLevel(Skill.WOODCUTTING);


        // method 1

        if ((RandomTask >= 41 && RandomTask < 71) ||
                (Bank.isOpen() && !Inventory.contains("Knife") && !Bank.contains("Knife") && RandomTask >= 41 && RandomTask < 71) ||
                (Bank.isOpen() && !Inventory.contains("Tinderbox") && !Bank.contains("Tinderbox") && RandomTask >= 71) ||
                (COMPLETED_GOAL_FIREMAKING && RandomTask >= 71) ||
                (COMPLETED_GOAL_FLETCHING && RandomTask >= 41 && RandomTask < 71)) {

            // Generate a new random number between 1 and 100 (inclusive) and assign it to RandomTask
            Random random = new Random();
            RandomTask = random.nextInt(100) + 1;
        } else {
            // Handle the case where none of the conditions are met
        }

        // method 2

        if (BankLocation.getNearest() != null && playerLocation.distanceTo(BankLocation.getNearest().getArea()) > 4
                && !Bank.isOpen() && BuyItems && Obtain_BronzeAxe == null && !Inventory.contains(Predicates.nameContains("axe"))
                && !Equipment.contains(Predicates.nameContains("axe")) || RandomTask >= 41 && RandomTask < 71 && BankLocation.getNearest() != null
                && playerLocation.distanceTo(BankLocation.getNearest().getArea()) > 4 && !Bank.isOpen()
                && !BuyItems && !Inventory.contains("Knife") || RandomTask >= 71 && BankLocation.getNearest() != null
                && playerLocation.distanceTo(BankLocation.getNearest().getArea()) > 4 && !Bank.isOpen()
                && !BuyItems && !Inventory.contains("Tinderbox") || !Bank.isOpen() && BankLocation.getNearest() != null
                && playerLocation.distanceTo(BankLocation.getNearest().getArea()) > 4 && BuyItems && !SELECTED_SHOP
                || !Bank.isOpen() && BankLocation.getNearest() != null && playerLocation.distanceTo(BankLocation.getNearest().getArea()) > 4
                && !Inventory.contains("Coins") && BuyItems && SHOP_BOBS_AXES || !Bank.isOpen() && BankLocation.getNearest() != null
                && playerLocation.distanceTo(BankLocation.getNearest().getArea()) > 4 && !Inventory.contains("Coins") && BuyItems
                && SHOP_GRAND_EXCHANGE && woodcutting_rectangularareas.SHOP_GRAND_EXCHANGE_FS.getArea().contains(Players.getLocal().getWorldLocation())
                || !Bank.isOpen() && BankLocation.getNearest() != null && playerLocation.distanceTo(BankLocation.getNearest().getArea()) > 4
                && Inventory.contains("Coins") && BuyItems && SHOP_GRAND_EXCHANGE
                && !woodcutting_rectangularareas.SHOP_GRAND_EXCHANGE_FS.getArea().contains(Players.getLocal().getWorldLocation())
                || !Bank.isOpen() && BankLocation.getNearest() != null && playerLocation.distanceTo(BankLocation.getNearest().getArea()) > 4
                && Equipment.contains(Predicates.nameContains("pickaxe")) || !Bank.isOpen() && BankLocation.getNearest() != null && playerLocation.distanceTo(BankLocation.getNearest().getArea()) > 4
                && Inventory.contains(Predicates.nameContains("pickaxe"))) {
            // walk to closest bank
            WorldPoint nearestBankLocation = BankLocation.getNearest().getArea().getCenter();
            Movement.walkTo(nearestBankLocation.getX(), nearestBankLocation.getY(), nearestBankLocation.getPlane());

        } else
        {

        }

        // method 3

        if (!Bank.isOpen() && notatclosestbank && !BuyItems && Obtain_BronzeAxe == null && Inventory.contains(Predicates.nameContains("axe"))
                && Equipment.contains(Predicates.nameContains("axe")) || RandomTask >= 41 && RandomTask < 71 && notatclosestbank && !Bank.isOpen()
                && !BuyItems && !Inventory.contains("Knife") || RandomTask >= 71 && notatclosestbank && !Bank.isOpen() && !BuyItems && !Inventory.contains("Tinderbox")
                || !Bank.isOpen() && notatclosestbank && BuyItems && !SELECTED_SHOP || !Bank.isOpen() && notatclosestbank && !Inventory.contains("Coins") && BuyItems && SHOP_BOBS_AXES
                || !Bank.isOpen() && notatclosestbank && !Inventory.contains("Coins") && BuyItems && SHOP_GRAND_EXCHANGE && gecontainsplayer || !Bank.isOpen() && notatclosestbank
                && Inventory.contains("Coins") && BuyItems && SHOP_GRAND_EXCHANGE && !gecontainsplayer || !Bank.isOpen() && notatclosestbank && Equipment.contains(Predicates.nameContains("pickaxe"))
                || !Bank.isOpen() && notatclosestbank && Inventory.contains(Predicates.nameContains("pickaxe")))
        {
            // walk to closest bank
            WorldPoint nearestBankLocation = BankLocation.getNearest().getArea().getCenter();
            Movement.walkTo(nearestBankLocation.getX(), nearestBankLocation.getY(), nearestBankLocation.getPlane());

        } else {

        }

        // method 4

        if (!BuyItems && atclosestbank && !Bank.isOpen() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
                || !BuyItems && atclosestbank && !Bank.isOpen() && RandomTask >= 41 && RandomTask < 71 && !Inventory.contains("Knife") || !BuyItems && atclosestbank && !Bank.isOpen()
                && RandomTask >= 71 && !Inventory.contains("Tinderbox") || !Bank.isOpen() && atclosestbank && BuyItems && !SELECTED_SHOP || !Bank.isOpen() && atclosestbank && !Inventory.contains("Coins")
                && BuyItems && SHOP_BOBS_AXES || !Bank.isOpen() && atclosestbank && !Inventory.contains("Coins") && BuyItems && SHOP_GRAND_EXCHANGE && gecontainsplayer || !Bank.isOpen()
                && atclosestbank && Inventory.contains("Coins") && BuyItems && SHOP_GRAND_EXCHANGE && !gecontainsplayer || !Bank.isOpen() && atclosestbank && Equipment.contains(Predicates.nameContains("pickaxe"))
                || !Bank.isOpen() && atclosestbank && Inventory.contains(Predicates.nameContains("pickaxe"))) {

            NPC banker = NPCs.getNearest(npc -> npc.hasAction("Collect"));
            if (banker != null)
            {
                banker.interact("Bank");
                return -3;
            }
            sleep(10000);

        } else {

        }

        // method 5

        if (Bank.isOpen() && Inventory.getCount("Coins") >= 1 && !gecontainsplayer) {
            SHOP_GRAND_EXCHANGE = true;
            Bank.depositAll("Coins");
        } else {

        }

        // method 6

        if (Bank.isOpen() && isWearingPickaxe){

            Bank.depositEquipment();

        } else {

        }

        // method 7

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("pickaxe"))) {
            Bank.depositAllExcept("Lamp", "Knife", "Tinderbox");
        } else {

        }

        // method 10

        if (Bank.isOpen() && !Bank.contains("Bronze axe") && !Inventory.contains("Bronze axe") && getwoodcuttingskill < 6 && Obtain_AXE == null) {
            BuyItems = true;
            Obtain_AXE = "Bronze axe";
        }

        // method 11

        if (Bank.isOpen() && !Bank.contains("Steel axe") && !Inventory.contains("Steel axe") && getwoodcuttingskill >= 6 && getwoodcuttingskill < 21 && Obtain_AXE == null) {
            BuyItems = true;
            Obtain_AXE = "Steel axe";
        } else {

        }

        // method 12

        if (Bank.isOpen() && !Bank.contains("Mithril axe") && !Inventory.contains("Mithril axe") && getwoodcuttingskill >= 21 && getwoodcuttingskill < 31 && Obtain_AXE == null) {
            BuyItems = true;
            Obtain_AXE = "Mithril axe";
        } else {

        }

        // method 13

        if (Bank.isOpen() && !Bank.contains("Adamant axe") && !Inventory.contains("Adamant axe") && getwoodcuttingskill >= 31 && getwoodcuttingskill < 41 && Obtain_AXE == null) {
            BuyItems = true;
            Obtain_AXE = "Adamant axe";
        } else {

        }

        // method 14

        if (Bank.isOpen() && !Bank.contains("Rune axe") && !Inventory.contains("Rune axe") && getwoodcuttingskill >= 41 && getwoodcuttingskill < 51 && Obtain_AXE == null) {
            BuyItems = true;
            Obtain_AXE = "Rune axe";
        } else {

        }

        // method 15

        if (Bank.isOpen() && !Bank.contains("Dragon axe") && !Inventory.contains("Dragon axe") && getwoodcuttingskill >= 61 && Obtain_AXE == null) {
            BuyItems = true;
            Obtain_AXE = "Dragon axe";
        } else {

        }

        // method 16

        if (Bank.isOpen() && client.isMembersWorld() && !Bank.contains("Knife") && !Inventory.contains("Knife") && RandomTask >= 41 && RandomTask < 71 && Accoount_Type == "Normal") {
            BuyItems = true;
            Obtain_Knife = true;
        } else {

        }

        // method 17

        if (Bank.isOpen() && !Bank.contains("Tinderbox") && !Inventory.contains("Tinderbox") && RandomTask >= 71 && Accoount_Type == "Normal") {
            BuyItems = true;
            Obtain_Tinderbox = true;
        } else {

        }

        // method 18

        if (Bank.isOpen() && !Bank.contains("Knife") && !Bank.contains("Tinderbox") && !Inventory.contains("Knife") && !Inventory.contains("Tinderbox") && RandomTask >= 41 && RandomTask < 71 && Accoount_Type != "Normal"
        || Bank.isOpen() && !Bank.contains("Knife") && !Bank.contains("Tinderbox") && !Inventory.contains("Knife") && !Inventory.contains("Tinderbox") && RandomTask >= 41 && RandomTask < 71 && !client.isMembersWorld()) {
            Obtain_Knife = false;
            RandomTask = 10;
        }

        // end of script

        return 1000;
    }


}
