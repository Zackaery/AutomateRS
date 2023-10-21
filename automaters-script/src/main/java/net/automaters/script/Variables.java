package net.automaters.script;

import net.automaters.api.entities.PlayerCrashInfo;
import net.automaters.tasks.tasks.MiscellaneousTasks;
import net.automaters.tasks.tasks.MoneyMakingTasks;
import net.automaters.tasks.tasks.QuestingTasks;
import net.automaters.tasks.tasks.SkillingTasks;
import net.runelite.api.Item;
import net.runelite.api.TileObject;
import net.unethicalite.api.items.Inventory;

import java.util.*;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.overlay.AutomateRSOverlay.scriptRunTime;

public class Variables {

    //Task Info
    public static String resourceObject = null;
    public static String resourceLocation = null;
    public static String resourceAction = null;
    public static List<String> resourceItems = null;

    //Integers
    public static int skillTask;
    public static int totalCoins = -1;
    public static int primaryToolID;
    public static int secondaryToolID;

    //Strings
    public static String task;
    public static String outfit = null;
    public static String secondaryTask = "null";
    public static String primaryTool = null;
    public static String secondaryTool = null;
    public static String secondaryItem = null;
    public static String currentTask;

    //Booleans
    public static boolean delayWalk;
    public static boolean guiStarted;
    public static boolean scriptStarted;
    public static boolean secondaryTaskActive;
    public static boolean taskStarted;
    public static boolean taskSelected;

    //Timers
    public static long startTime;
    public static long scriptTimer;
    public static long elapsedTime;
    public static long taskDuration;
    public static long lastInteractionTime = 0;
    public static long interactionCooldown = 8000;

    //Lists
    public static List<Object> taskList = new ArrayList<>();
    public static List<SkillingTasks> skillingTasks = new ArrayList<>();
    public static List<QuestingTasks> questingTasks = new ArrayList<>();
    public static List<MoneyMakingTasks> moneyMakingTasks = new ArrayList<>();
    public static List<MiscellaneousTasks> miscellaneousTasks = new ArrayList<>();
    public static ArrayList<String> taskItems = new ArrayList<>();

    //Maps
    public static Map<String, PlayerCrashInfo> playerCrashInfo = new HashMap<>();

    //TileObjects
    public static TileObject objectToRender;

    //Players

    public Variables() { }

    public static void resetAll() {
        getAll();

        //Task Info
        resourceObject = null;
        resourceAction = null;
        resourceLocation = null;
        resourceItems = new ArrayList<>();

        //Integers
        skillTask = 0;
        totalCoins = -1;
        primaryToolID = 0;
        secondaryToolID = 0;

        //Strings
        task = null;
        outfit = null;
        secondaryTask = "null";
        primaryTool = null;
        secondaryTool = null;
        secondaryItem = null;
        currentTask = null;

        //Booleans
        delayWalk = false;
        guiStarted = false;
        scriptStarted = false;
        secondaryTaskActive = false;
        taskStarted = false;
        taskSelected = false;

        //Timers
        startTime = 0;
        scriptTimer = 0;
        elapsedTime = 0;
        taskDuration = 0;
        lastInteractionTime = 0;
        interactionCooldown = 8000;

        //Lists
        taskList = new ArrayList<>();
        skillingTasks = new ArrayList<>();
        questingTasks = new ArrayList<>();
        moneyMakingTasks = new ArrayList<>();
        miscellaneousTasks = new ArrayList<>();
        taskItems = new ArrayList<>();

        //Maps
        playerCrashInfo = new HashMap<>();

        //TileObjects
        objectToRender = null;
    }

    public static String getAll() {
        List<Item> inventoryItems = Inventory.getAll();
        List<String> export = new ArrayList<>();

        for (Item item : inventoryItems) {
            if (!export.contains(item)) {
                export.add(item.getName());
            }
        }

        String bugReport = (
                "\n\n**Task Information:**" +
                        "\n> **Total Runtime:** " + scriptRunTime() +
                        "\n> **Current Task:** " + currentTask +
                        "\n> **Task Started:** " + taskStarted +
                        "\n> **Resource Name:** " + resourceObject +
                        "\n> **Resource Action:** " + resourceAction +
                        "\n> **Resource Location:** " + resourceLocation +
                        "\n> **Resource Items - (droppable):** " + resourceItems +
                        "\n> **Inventory Items - (current):** " + export +

                        "\n```prolog" +
                        "\n[INTEGERS]" +
                        "\nskillTask : " + skillTask +
                        "\ntotalCoins : " + totalCoins +
                        "\nprimaryToolID : " + primaryToolID +
                        "\nsecondaryToolID : " + secondaryToolID +

                        "\n\n[STRINGS]" +
                        "\ntask : " + task +
                        "\noutfit : " + outfit +
                        "\nsecondaryTask : " + secondaryTask +
                        "\nprimaryTool : " + primaryTool +
                        "\nsecondaryTool : " + secondaryTool +
                        "\nsecondaryItem : " + secondaryItem +

                        "\n\n[BOOLEANS]" +
                        "\ndelayWalk : " + delayWalk +
                        "\nguiStarted : " + guiStarted +
                        "\nscriptStarted : " + scriptStarted +
                        "\nsecondaryTaskActive : " + secondaryTaskActive +

                        "\n\n[TIMERS]" +
                        "\nstartTime : " + startTime +
                        "\nscriptTimer : " + scriptTimer +
                        "\nelapsedTime : " + elapsedTime +
                        "\ntaskDuration : " + taskDuration +
                        "\nlastInteractionTime : " + lastInteractionTime +
                        "\ninteractionCooldown : " + interactionCooldown +

                        "\n\n[LISTS]" +
                        "\ntaskItems : " + taskItems +
                        "\ntaskList : " + taskList +
                        "\nskillingTasks : " + skillingTasks +
                        "\nquestingTasks : " + questingTasks +
                        "\nmoneyMakingTasks : " + moneyMakingTasks +
                        "\nmiscellaneousTasks : " + miscellaneousTasks +
                "\n```");

        debug("Thank-you for submitting a bug report!");
        return bugReport;
    }
}
