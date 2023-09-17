package net.automaters.activities.skills.woodcutting;
import net.automaters.util.locations.woodcutting_rectangularareas;
import net.automaters.util.timers.globaltimers;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.coords.Area;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import net.unethicalite.api.plugins.LoopedPlugin;

import javax.swing.*;
import java.util.Random;


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
    public static String Task_Goal = "None";
    public static String Current_Task = "None";
    public static String Obtain_BronzeAxe = "None";
    public static String Obtain_AXE = "false";

    // timers

    Timer timer1 = globaltimers.getTimer1();
    Timer timer2 = globaltimers.getTimer2();

    // start of script

    @Override
    public int loop() {
        // start of script

        // player location
        var local = Players.getLocal();
        WorldPoint playerLocation = local != null ? local.getWorldLocation() : null;


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
                && !Bank.isOpen() && BuyItems && Obtain_BronzeAxe != null && !Inventory.contains(Predicates.nameContains("axe"))
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





        // end of script

        return 1000;
    }


}
