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

@SuppressWarnings({"ConstantConditions","unused"})
public class Woodcutting_I extends LoopedPlugin {

    // Booleans

    public static boolean HopWorlds = false;
    public static boolean Random_Skilling_Task = false;
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

    public static String Account_Type = "Unknown";
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

    boolean isWearingPickaxe = Equipment.contains(Predicates.nameContains("pickaxe"));

    @Override
    public int loop() {
        // start of script

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

        int getwoodcuttingskill = client.getBoostedSkillLevel(Skill.WOODCUTTING);
        int getfishingskill = client.getBoostedSkillLevel(Skill.FISHING);


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

        // method 5

        if (Bank.isOpen() && Inventory.getCount("Coins") >= 1 && !gecontainsplayer) {
            SHOP_GRAND_EXCHANGE = true;
            Bank.depositAll("Coins");
        }

        // method 6

        if (Bank.isOpen() && isWearingPickaxe){

            Bank.depositEquipment();

        }

        // method 7

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("pickaxe"))) {
            Bank.depositAllExcept("Lamp", "Knife", "Tinderbox");
        }

        // method 10

        if (Bank.isOpen() && !Bank.contains("Bronze axe") && !Inventory.contains("Bronze axe") && getwoodcuttingskill < 6 && Obtain_AXE.equals("null")) {
            BuyItems = true;
            Obtain_AXE = "Bronze axe";
        }

        // method 11

        if (Bank.isOpen() && !Bank.contains("Steel axe") && !Inventory.contains("Steel axe") && getwoodcuttingskill >= 6 && getwoodcuttingskill < 21 && Obtain_AXE.equals("null")) {
            BuyItems = true;
            Obtain_AXE = "Steel axe";
        }

        // method 12

        if (Bank.isOpen() && !Bank.contains("Mithril axe") && !Inventory.contains("Mithril axe") && getwoodcuttingskill >= 21 && getwoodcuttingskill < 31 && Obtain_AXE.equals("null")) {
            BuyItems = true;
            Obtain_AXE = "Mithril axe";
        }

        // method 13

        if (Bank.isOpen() && !Bank.contains("Adamant axe") && !Inventory.contains("Adamant axe") && getwoodcuttingskill >= 31 && getwoodcuttingskill < 41 && Obtain_AXE.equals("null")) {
            BuyItems = true;
            Obtain_AXE = "Adamant axe";
        }

        // method 14

        if (Bank.isOpen() && !Bank.contains("Rune axe") && !Inventory.contains("Rune axe") && getwoodcuttingskill >= 41 && getwoodcuttingskill < 51 && Obtain_AXE.equals("null")) {
            BuyItems = true;
            Obtain_AXE = "Rune axe";
        }

        // method 15

        if (Bank.isOpen() && !Bank.contains("Dragon axe") && !Inventory.contains("Dragon axe") && getwoodcuttingskill >= 61 && Obtain_AXE.equals("null")) {
            BuyItems = true;
            Obtain_AXE = "Dragon axe";
        }

        // method 16

        if (Bank.isOpen() && client.isMembersWorld() && !Bank.contains("Knife") && !Inventory.contains("Knife") && RandomTask >= 41 && RandomTask < 71 && Account_Type.equals("Normal")) {
            BuyItems = true;
            Obtain_Knife = true;
        }

        // method 17

        if (Bank.isOpen() && !Bank.contains("Tinderbox") && !Inventory.contains("Tinderbox") && RandomTask >= 71 && Account_Type.equals("Normal")) {
            BuyItems = true;
            Obtain_Tinderbox = true;
        }

        // method 18

        if (Bank.isOpen() && !Bank.contains("Knife") && !Bank.contains("Tinderbox") && !Inventory.contains("Knife") && !Inventory.contains("Tinderbox") && RandomTask >= 41 && RandomTask < 71 && !Account_Type.equals("Normal")
        || Bank.isOpen() && !Bank.contains("Knife") && !Bank.contains("Tinderbox") && !Inventory.contains("Knife") && !Inventory.contains("Tinderbox") && RandomTask >= 41 && RandomTask < 71 && !client.isMembersWorld()) {
            Obtain_Knife = false;
            RandomTask = 10;
        }

        // method 19

        if (Bank.isOpen() && !Bank.contains("Tinderbox") && !Inventory.contains("Tinderbox") && RandomTask >= 71 && Account_Type.equals("Normal")) {
            Obtain_Tinderbox = true;
            RandomTask = 10;
        }

        // method 20

        if (Bank.isOpen() && !Bank.contains("Knife") && Bank.contains("Tinderbox") && !Inventory.contains("Knife") && RandomTask >= 41 && RandomTask < 71 && !Account_Type.equals("Normal")
        || Bank.isOpen() && !Bank.contains("Knife") && Inventory.contains("Tinderbox") && !Inventory.contains("Knife") && RandomTask >= 41 && RandomTask < 71 && !Account_Type.equals("Normal")
        || Bank.isOpen() && !Bank.contains("Tinderbox") && Bank.contains("Knife") && !Inventory.contains("Tinderbox") && RandomTask >= 71 && !Account_Type.equals("Normal")
        || Bank.isOpen() && !Bank.contains("Tinderbox") && Inventory.contains("Knife") && !Inventory.contains("Tinderbox") && RandomTask >= 71 && !Account_Type.equals("Normal")) {

            Random random = new Random();
            RandomTask = random.nextInt(100) + 1;
            sleep(50, 100);
        }

        // method 21

        if (Obtain_AXE.equals("Mithril axe") && !Account_Type.equals("Normal") && !Inventory.contains("Mithril axe") && !Bank.contains("Mithril axe") && !Equipment.contains("Mithril axe")
                || Obtain_AXE.equals("Adamant axe") && !Account_Type.equals("Normal") && !Inventory.contains("Adamant axe") && !Bank.contains("Adamant axe") && !Equipment.contains("Adamant axe")
                || Obtain_AXE.equals("Rune axe") && !Account_Type.equals("Normal") && !Inventory.contains("Rune axe") && !Bank.contains("Rune axe") && !Equipment.contains("Rune axe")
                || Obtain_AXE.equals("Dragon axe") && !Account_Type.equals("Normal") && !Inventory.contains("Dragon axe") && !Bank.contains("Dragon axe") && !Equipment.contains("Dragon axe")) {

            Obtain_AXE = "Steel axe";
            sleep(100, 150);

        }

        // method 22

        if (Obtain_AXE.equals("Dragon axe") && Account_Type.equals("Normal") && !client.isMembersWorld()
                || Obtain_AXE.equals("Dragon axe") && Account_Type.equals("Normal") && Bank.isOpen() && Bank.getCount("Coins") < 70000 && Inventory.getCount("Coins") < 70000
                && !Inventory.contains("Dragon axe") && !Bank.contains("Dragon axe") && !Equipment.contains("Dragon axe")){

            Obtain_AXE = "Rune axe";
            sleep(100,150);

        }

        // method 23

        if (Obtain_AXE.equals("Rune axe") && Account_Type.equals("Normal") && Bank.isOpen() && Bank.getCount("Coins") < 8000 && Inventory.getCount("Coins") < 8000
                && !Inventory.contains("Rune axe") && !Bank.contains("Rune axe") && !Equipment.contains("Rune axe")) {

            Obtain_AXE = "Adamant axe";
            sleep(100,150);
        }

        // method 24

        if (Obtain_AXE.equals("Adamant axe") && Account_Type.equals("Normal") && Bank.isOpen() && Bank.getCount("Coins") < 500 && Inventory.getCount("Coins") < 500
        && !Inventory.contains("Adamant axe") && !Bank.contains("Adamant axe") && !Equipment.contains("Adamant axe")) {

            Obtain_AXE = "Mithril axe";
            sleep(100,150);
        }

        // method 25

        if (Obtain_AXE.equals("Mithril axe") && Account_Type.equals("Normal") && Bank.isOpen() && Bank.getCount("Coins") < 250 && Inventory.getCount("Coins") < 250
        && !Inventory.contains("Mithril axe") && !Bank.contains("Mithril axe") && !Equipment.contains("Mithril axe")) {

            Obtain_AXE = "Steel axe";
            sleep(100,150);
        }

        // method 26

        if (Obtain_AXE.equals("Steel axe") && Account_Type.equals("Normal") && Bank.isOpen() && Bank.getCount("Coins") < 100 && Inventory.getCount("Coins") < 100
                && !Inventory.contains("Steel axe") && !Bank.contains("Steel axe") && !Equipment.contains("Steel axe")
                || Obtain_AXE.equals("Steel axe") && !Account_Type.equals("Normal") && Bank.isOpen() && Bank.getCount("Coins") < 200 && Inventory.getCount("Coins") < 200
                && !Inventory.contains("Steel axe") && !Bank.contains("Steel axe") && !Equipment.contains("Steel axe")) {

            Obtain_AXE = "Bronze axe";
            sleep(100,150);
        }

        // method 27

        if (Obtain_AXE.equals("Dragon axe") && Inventory.contains("Dragon axe")
                || Obtain_AXE.equals("Dragon axe") && Bank.isOpen() && Bank.contains("Dragon axe")
                || Obtain_AXE.equals("Dragon axe") && Equipment.contains("Dragon axe")
                || Obtain_AXE.equals("Rune axe") && Inventory.contains("Rune axe")
                || Obtain_AXE.equals("Rune axe") && Bank.isOpen() && Bank.contains("Rune axe")
                || Obtain_AXE.equals("Rune axe") && Equipment.contains("Rune axe")
                || Obtain_AXE.equals("Adamant axe") && Inventory.contains("Adamant axe")
                || Obtain_AXE.equals("Adamant axe") && Bank.isOpen() && Bank.contains("Adamant axe")
                || Obtain_AXE.equals("Adamant axe") && Equipment.contains("Adamant axe")
                || Obtain_AXE.equals("Mithril axe") && Inventory.contains("Mithril axe")
                || Obtain_AXE.equals("Mithril axe") && Bank.isOpen() && Bank.contains("Mithril axe")
                || Obtain_AXE.equals("Mithril axe") && Equipment.contains("Mithril axe")
                || Obtain_AXE.equals("Steel axe") && Inventory.contains("Steel axe")
                || Obtain_AXE.equals("Steel axe") && Bank.isOpen() && Bank.contains("Steel axe")
                || Obtain_AXE.equals("Steel axe") && Equipment.contains("Steel axe")
                || Obtain_AXE.equals("Bronze axe") && Inventory.contains("Bronze axe")
                || Obtain_AXE.equals("Bronze axe") && Bank.isOpen() && Bank.contains("Bronze axe")
                || Obtain_AXE.equals("Bronze axe") && Equipment.contains("Bronze axe")) {

            Obtain_AXE = "false";
        }

        // method 36

        if (Bank.isOpen() && client.isMembersWorld() && !Equipment.contains("Lumberjack hat") && Bank.contains("Lumberjack hat") && !Inventory.contains("Lumberjack hat")
                && getwoodcuttingskill >= 44) {
            Bank.withdraw("Lumberjack hat",1,Bank.WithdrawMode.ITEM);
        }

        // method 37

        if (Bank.isOpen() && client.isMembersWorld() && !Equipment.contains("Lumberjack top") && Bank.contains("Lumberjack top") && !Inventory.contains("Lumberjack top")
                && getwoodcuttingskill >= 44) {
            Bank.withdraw("Lumberjack top",1,Bank.WithdrawMode.ITEM);
        }

        // method 38

        if (Bank.isOpen() && client.isMembersWorld() && !Equipment.contains("Lumberjack legs") && Bank.contains("Lumberjack legs") && !Inventory.contains("Lumberjack legs")
                && getwoodcuttingskill >= 44) {
            Bank.withdraw("Lumberjack legs",1,Bank.WithdrawMode.ITEM);
        }

        // method 39

        if (Bank.isOpen() && client.isMembersWorld() && !Equipment.contains("Lumberjack boots") && Bank.contains("Lumberjack boots") && !Inventory.contains("Lumberjack boots")
                && getwoodcuttingskill >= 44) {
            Bank.withdraw("Lumberjack boots",1,Bank.WithdrawMode.ITEM);
        }

        // method 40

        if (Bank.isOpen() && client.isMembersWorld() && !Equipment.contains("Lumberjack hat") && !Equipment.contains("Graceful hood") && !Bank.contains("Lumberjack hat") && !Inventory.contains("Lumberjack hat")
                && Bank.contains("Graceful hood") && !Inventory.contains("Graceful hood")) {

            Bank.withdraw("Graceful hood",1,Bank.WithdrawMode.ITEM);
        }

        // method 41

        if (Bank.isOpen() && client.isMembersWorld() && !Equipment.contains("Lumberjack top") && !Equipment.contains("Graceful top") && !Bank.contains("Lumberjack top") && !Inventory.contains("Lumberjack top")
                && Bank.contains("Graceful top") && !Inventory.contains("Graceful top")) {

            Bank.withdraw("Graceful top",1,Bank.WithdrawMode.ITEM);
        }

        // method 42

        if (Bank.isOpen() && client.isMembersWorld() && !Equipment.contains("Lumberjack legs") && !Equipment.contains("Graceful legs") && !Bank.contains("Lumberjack legs") && !Inventory.contains("Lumberjack legs")
                && Bank.contains("Graceful legs") && !Inventory.contains("Graceful legs")) {

            Bank.withdraw("Graceful legs",1,Bank.WithdrawMode.ITEM);
        }

        // method 43

        if (Bank.isOpen() && client.isMembersWorld() && !Equipment.contains("Lumberjack boots") && !Equipment.contains("Graceful boots") && !Bank.contains("Lumberjack boots") && !Inventory.contains("Lumberjack boots")
                && Bank.contains("Graceful boots") && !Inventory.contains("Graceful boots")) {

            Bank.withdraw("Graceful boots",1,Bank.WithdrawMode.ITEM);
        }

        // method 44

        if (Bank.isOpen() && client.isMembersWorld() && Bank.contains("Graceful gloves") && !Inventory.contains("Graceful gloves")) {
            Bank.withdraw("Graceful gloves",1,Bank.WithdrawMode.ITEM);
        }

        // method 45

        if (Bank.isOpen() && client.isMembersWorld() && Bank.contains("Graceful cape") && !Inventory.contains("Graceful cape")) {
            Bank.withdraw("Graceful cape",1,Bank.WithdrawMode.ITEM);
        }

        // method 46

        if (Bank.isOpen() && !Equipment.contains("Lumberjack hat") && !Equipment.contains("Graceful hood") && !Bank.contains("Lumberjack hat") && !Inventory.contains("Lumberjack hat") && !Inventory.contains("Graceful hood")
                && !Bank.contains("Graceful hood") && Bank.contains(Predicates.nameContains("ronman helm")) && getfishingskill < 34) {
            Bank.withdraw(Predicates.nameContains("ronman helm"),1,Bank.WithdrawMode.ITEM);
        }

        // method 47

        if (Bank.isOpen() && !Equipment.contains("Lumberjack hat") && !Equipment.contains("Graceful hood") && !Bank.contains("Lumberjack hat") && !Inventory.contains("Lumberjack hat") && !Inventory.contains("Graceful hood")
                && !Bank.contains("Graceful hood") && Bank.contains(Predicates.nameContains("ron helm")) && !Inventory.contains(Predicates.nameContains("ron helm")) && getfishingskill < 34
                || Bank.contains(Predicates.nameContains("ron helm")) && !Inventory.contains(Predicates.nameContains("ron helm")) && getfishingskill <34) {
            Bank.withdraw(Predicates.nameContains("ron helm"),1,Bank.WithdrawMode.ITEM);
        }

        // method 48

        if (!Bank.isOpen() && Inventory.contains("Lumberjack hat") && getwoodcuttingskill >= 44) {
           Inventory.getFirst("Lumberjack hat").interact("Wear");
        } else {
            if (Bank.isOpen() && Inventory.contains("Lumberjack hat") && getwoodcuttingskill >= 44) {
                Bank.Inventory.getFirst("Lumberjack hat").interact("Wear");
            }
        }

        // method 49

        if (!Bank.isOpen() && Inventory.contains("Lumberjack top") && getwoodcuttingskill >= 44) {
            Inventory.getFirst("Lumberjack top").interact("Wear");
        } else {
            if (Bank.isOpen() && Inventory.contains("Lumberjack top") && getwoodcuttingskill >= 44) {
                Bank.Inventory.getFirst("Lumberjack top").interact("Wear");
            }
        }

        // method 50

        if (!Bank.isOpen() && Inventory.contains("Lumberjack legs") && getwoodcuttingskill >= 44) {
            Inventory.getFirst("Lumberjack legs").interact("Wear");
        } else {
            if (Bank.isOpen() && Inventory.contains("Lumberjack legs") && getwoodcuttingskill >= 44) {
                Bank.Inventory.getFirst("Lumberjack legs").interact("Wear");
            }
        }

        // method 51

        if (!Bank.isOpen() && Inventory.contains("Lumberjack boots") && getwoodcuttingskill >= 44) {
            Inventory.getFirst("Lumberjack boots").interact("Wear");
        } else {
            if (Bank.isOpen() && Inventory.contains("Lumberjack boots") && getwoodcuttingskill >= 44) {
                Bank.Inventory.getFirst("Lumberjack boots").interact("Wear");
            }
        }

        // method 52

        if (!Bank.isOpen() && Inventory.contains("Graceful hood") && !Equipment.contains("Lumberjack hat")) {
            Inventory.getFirst("Graceful hood").interact("Wear");
        } else {
            if (Bank.isOpen() && Inventory.contains("Graceful hood") && !Equipment.contains("Lumberjack hat")) {
                Bank.Inventory.getFirst("Graceful hood").interact("Wear");
            }
        }

        // method 53

        if (!Bank.isOpen() && Inventory.contains("Graceful top") && !Equipment.contains("Lumberjack top")) {
            Inventory.getFirst("Graceful top").interact("Wear");
        } else {
            if (Bank.isOpen() && Inventory.contains("Graceful top") && !Equipment.contains("Lumberjack top")) {
                Bank.Inventory.getFirst("Graceful top").interact("Wear");
            }
        }

        // method 54

        if (!Bank.isOpen() && Inventory.contains("Graceful legs") && !Equipment.contains("Lumberjack legs")) {
            Inventory.getFirst("Graceful legs").interact("Wear");
        } else {
            if (Bank.isOpen() && Inventory.contains("Graceful legs") && !Equipment.contains("Lumberjack legs")) {
                Bank.Inventory.getFirst("Graceful legs").interact("Wear");
            }
        }

        // method 55

        if (!Bank.isOpen() && Inventory.contains("Graceful boots") && !Equipment.contains("Lumberjack boots")) {
            Inventory.getFirst("Graceful boots").interact("Wear");
        } else {
            if (Bank.isOpen() && Inventory.contains("Graceful boots") && !Equipment.contains("Lumberjack boots")) {
                Bank.Inventory.getFirst("Graceful boots").interact("Wear");
            }
        }

        // method 56

        if (!Bank.isOpen() && Inventory.contains("Graceful gloves")) {
            Inventory.getFirst("Graceful gloves").interact("Wear");
        } else {
            if (Bank.isOpen() && Inventory.contains("Graceful gloves")) {
                Bank.Inventory.getFirst("Graceful gloves").interact("Wear");
            }
        }

        // method 57

        if (!Bank.isOpen() && Inventory.contains("Graceful cape")) {
            Inventory.getFirst("Graceful cape").interact("Wear");
        } else {
            if (Bank.isOpen() && Inventory.contains("Graceful cape")) {
                Bank.Inventory.getFirst("Graceful cape").interact("Wear");
            }
        }

        // method 58

        if (Bank.isOpen() && !Equipment.contains("Lumberjack hat") && !Equipment.contains("Graceful hood") && Inventory.contains(Predicates.nameContains("ronman helm"))) {
            Bank.Inventory.getFirst(Predicates.nameContains("ronman helm")).interact("Wear");
        } else {
            if (!Bank.isOpen() && !Equipment.contains("Lumberjack hat") && !Equipment.contains("Graceful hood") && Inventory.contains(Predicates.nameContains("ronman helm"))) {
                Inventory.getFirst(Predicates.nameContains("ronman helm")).interact("Wear");
            }
        }

        // method 59

        if (Bank.isOpen() && !Equipment.contains("Lumberjack hat") && !Equipment.contains("Graceful hood") && Inventory.contains(Predicates.nameContains("ron helm"))) {
            Bank.Inventory.getFirst(Predicates.nameContains("ron helm")).interact("Wear");
        } else {
            if (!Bank.isOpen() && !Equipment.contains("Lumberjack hat") && !Equipment.contains("Graceful hood") && Inventory.contains(Predicates.nameContains("ron helm"))) {
                Inventory.getFirst(Predicates.nameContains("ron helm")).interact("Wear");
            }
        }

        // method 60

        if (!REGION_ASGARNIA_MISTHALIN && ramcontainsplayer
                || !REGION_ASGARNIA_MISTHALIN && ramicontainsplayer
                || !REGION_ASGARNIA_MISTHALIN && ramiicontainsplayer) {
            REGION_ASGARNIA_MISTHALIN = true;
        }

        // method 61

        if (!REGION_ASGARNIA_MISTHALIN && noramcontainsplayer && noramicontainsplayer && noramiicontainsplayer) {

            // may not need this method.

        }

        // method 62

        // end of script

        return 1000;
    }


}
