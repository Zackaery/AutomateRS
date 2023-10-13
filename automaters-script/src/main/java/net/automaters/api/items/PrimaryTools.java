package net.automaters.api.items;

import lombok.Getter;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import java.lang.reflect.Field;

import static net.automaters.activities.skills.woodcutting.Woodcutting.primaryTool;
import static net.automaters.activities.skills.woodcutting.Woodcutting.primaryToolID;
import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.ui.GrandExchange.automateBuy;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.unobfuscated.GrandExchangePrices.canAfford;
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

                        int toolID = (int) idField.get(tool);
                        int skillLevel = (int) skillLevelField.get(tool);
                        int attackLevel = (int) attackLevelField.get(tool);
                        boolean buyable = (boolean) buyableField.get(tool);
                        Skill skill = (Skill) skillField.get(tool);
                        String toolName = tool.name();

                        if (primaryTool == null) {
                            processTool(toolID, skillLevel, attackLevel, skill, buyable, toolName);
                        }

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void processTool(int toolID, int skillLevel, int attackLevel, Skill skill, Boolean buyable, String toolName) {
        debug("Processing Tool: "+toolName);
        while (scriptStarted && hasToolReq(skill, skillLevel)) {
            debug("Has Tool Requirements: "+skillLevel+" "+skill.getName());
            if (hasTool(toolID)) {
                Item tool = Inventory.getFirst(toolID);
                Item wornTool = Equipment.getFirst(toolID);
                if (tool != null) {
                    debug("Has Tool: "+tool.getName());
                } else {
                    debug("Has Tool: "+wornTool.getName());
                }
                if (canWield(attackLevel)) {
                    wieldTool(toolID, attackLevel);
                    break;
                }
                break;
            }
            if (primaryTool == null) {
                debug("in primaryTool == null");
                while (scriptStarted && !Bank.isOpen()) {
                    openBank();
                    sleep(600, 1200);
                }
                if (Bank.isOpen()) {
                    if (Inventory.isFull()) {
                        Bank.depositAllExcept(toolID);
                        break;
                    }
                    if (!Bank.contains(toolID)) {
                        if (buyable && canAfford(toolID)) {
                            debug("Attempting to buy tool: " + toolName);
                            automateBuy(toolID, 1, 5);
                            sleep(600, 1200);
                        } else {
                            break;
                        }
                    } else {
                        if (!Inventory.isFull()) {
                            Bank.withdraw(toolID, 1, Bank.WithdrawMode.ITEM);
                            sleep(600, 1200);
                        }
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

    private static boolean hasTool(int id) {
        if (Inventory.contains(id) || Equipment.contains(id)) {
            Item tool = Inventory.getFirst(id);
            Item wornTool = Equipment.getFirst(id);
            if (tool != null) {
                primaryTool = tool.getName();
                primaryToolID = id;
            } else {
                primaryTool = wornTool.getName();
                primaryToolID = id;
            }
            return true;
        } else {
            return false;
        }
    }

    private static void wieldTool(int toolID, int attackLevel) {
        if (canWield(attackLevel)) {
            Item item = Inventory.getFirst(toolID);
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

    public enum FishingTools {
        LOBSTER_POT(ItemID.LOBSTER_POT,41, 40, true),
        FLY_FISHING_ROD(ItemID.FLY_FISHING_ROD,21, 20, true),
        FISHING_ROD(ItemID.FISHING_ROD,11, 10, true),
        SMALL_FISHING_NET(ItemID.SMALL_FISHING_NET,1, 1, true),
        ;

        public final int id;
        public final int skillLevel;
        public final int fishingLevel;
        public final boolean buyable;
        public final Skill skill;

        FishingTools(int id, int skillLevel, int fishingLevel, boolean buyable) {
            this.id = id;
            this.skillLevel = skillLevel;
            this.fishingLevel = fishingLevel;
            this.buyable = buyable;
            this.skill = Skill.FISHING;
        }

        public int getId() {
            return id;
        }

        public int getSkillLevel() {
            return skillLevel;
        }

        public int getFishingLevel() {
            return fishingLevel;
        }
    }


}