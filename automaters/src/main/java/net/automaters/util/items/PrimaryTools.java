package net.automaters.util.items;

import lombok.Getter;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.automaters.activities.skills.woodcutting.WoodcuttingZach.primaryTool;
import static net.automaters.activities.skills.woodcutting.WoodcuttingZach.primaryToolID;
import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.ui.GrandExchange.automateBuy;
import static net.automaters.api.utils.GrandExchangePrices.canAfford;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.unethicalite.api.commons.Time.sleep;

public class PrimaryTools {

    public static void getPrimaryTool(Class<?> skillEnumClass) {
        if (Enum.class.isAssignableFrom(skillEnumClass)) {
            Enum<?>[] enumConstants = (Enum<?>[]) skillEnumClass.getEnumConstants();
            if (enumConstants != null) {
                for (Enum<?> tool : enumConstants) {

                    try {
                        Field idField = skillEnumClass.getDeclaredField("id");
                        Field skillLevelField = skillEnumClass.getDeclaredField("skillLevel");
                        Field attackLevelField = skillEnumClass.getDeclaredField("attackLevel");
                        Field buyableField = skillEnumClass.getDeclaredField("buyable");
                        Field skillField = skillEnumClass.getDeclaredField("skill");


                        idField.setAccessible(true);
                        skillLevelField.setAccessible(true);
                        attackLevelField.setAccessible(true);
                        buyableField.setAccessible(true);


                        int toolName = (int) idField.get(tool);
                        int skillLevel = (int) skillLevelField.get(tool);
                        int attackLevel = (int) attackLevelField.get(tool);
                        boolean buyable = (boolean) buyableField.get(tool);
                        Skill skill = (Skill) skillField.get(tool);
                        String name = (String) tool.name();

                        if (primaryTool == null) {
                            processTool(toolName, skillLevel, attackLevel, skill, buyable, name);
                        }

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void processTool(int toolID, int skillLevel, int attackLevel, Skill skill, Boolean buyable, String toolName) {
        while (scriptStarted && hasToolReq(skill, skillLevel)) {
            if (hasTool(toolID, toolName)) {
                if (canWield(attackLevel)) {
                    wieldTool(toolID, attackLevel, toolName);
                    break;
                }
                break;
            }
            if (primaryTool == null) {
                while (scriptStarted && !Bank.isOpen()) {
                    openBank();
                    sleep(600, 1200);
                }
                if (Bank.isOpen()) {
                    if (!Bank.contains(toolID)) {
                        if (buyable && canAfford(toolID)) {
                            automateBuy(toolID, 1, 5);
                            sleep(600, 1200);
                        } else {
                            break;
                        }
                    } else {
                        Bank.withdraw(toolID, 1, Bank.WithdrawMode.ITEM);
                        sleep(600, 1200);
                    }
                }
            }
        }
    }

    private static boolean hasToolReq(Skill skill, int skillLevel) {
        return (Skills.getLevel(skill) >= skillLevel);
    }

    private static boolean canWield(int attackLevel) {
        return (Skills.getLevel(Skill.ATTACK) >= attackLevel);
    }

    private static boolean hasTool(int id, String name) {
        if (Inventory.contains(id) || Equipment.contains(id)) {
            primaryTool = name;
            primaryToolID = id;
            return true;
        } else {
            return false;
        }
    }

    private static void wieldTool(int toolName, int attackLevel, String name) {
        if (canWield(attackLevel)) {
            Item item = Inventory.getFirst(toolName);
            item.interact("Wield");
            sleep(600);
        }
    }

    @Getter
    public enum WoodcuttingTools implements Equippable {

        CRYSTAL_AXE(ItemID.CRYSTAL_AXE, 71, 70, false),
        DRAGON_AXE(ItemID.DRAGON_AXE, 61, 60, true),
        RUNE_AXE(ItemID.RUNE_AXE, 41, 40, true),
        ADAMANT_AXE(ItemID.ADAMANT_AXE, 31, 30, true),
        MITHRIL_AXE(ItemID.MITHRIL_AXE, 21, 20, true),
        BLACK_AXE(ItemID.BLACK_AXE, 11, 10, true),
        STEEL_AXE(ItemID.STEEL_AXE, 6, 5, true),
        IRON_AXE(ItemID.IRON_AXE, 1, 1, true),
        BRONZE_AXE(ItemID.BRONZE_AXE, 1, 1, true)
        ;

        public final int id;
        public final int skillLevel;
        public final int attackLevel;
        public final boolean buyable;
        public final Skill skill;

        WoodcuttingTools(int id, int skillLevel, int attackLevel, boolean buyable) {
            this.id = id;
            this.skillLevel = skillLevel;
            this.attackLevel = attackLevel;
            this.buyable = buyable;
            this.skill = Skill.WOODCUTTING;
        }

    }

    public enum MiningTools implements Equippable {
        CRYSTAL_PICKAXE(ItemID.CRYSTAL_PICKAXE,71, 70, false),
        DRAGON_PICKAXE(ItemID.DRAGON_PICKAXE,61, 60, true),
        RUNE_PICKAXE(ItemID.RUNE_PICKAXE,41, 40, true),
        ADAMANT_PICKAXE(ItemID.ADAMANT_PICKAXE,31, 30, true),
        MITHRIL_PICKAXE(ItemID.MITHRIL_PICKAXE,21, 20, true),
        BLACK_PICKAXE(ItemID.BLACK_PICKAXE,11, 10, true),
        STEEL_PICKAXE(ItemID.STEEL_PICKAXE,6, 5, true),
        IRON_PICKAXE(ItemID.IRON_PICKAXE,1, 1, true),
        BRONZE_PICKAXE(ItemID.BRONZE_PICKAXE, 1, 1, true)
        ;

        public final int id;
        public final int skillLevel;
        public final int attackLevel;
        public final boolean buyable;
        public final Skill skill;

        MiningTools(int id, int skillLevel, int attackLevel, boolean buyable) {
            this.id = id;
            this.skillLevel = skillLevel;
            this.attackLevel = attackLevel;
            this.buyable = buyable;
            this.skill = Skill.MINING;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public int getSkillLevel() {
            return skillLevel;
        }

        @Override
        public int getAttackLevel() {
            return attackLevel;
        }
    }


}