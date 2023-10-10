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

public class CombatGear {

    public static void getPiece(Class<?> skillEnumClass) {
        if (Enum.class.isAssignableFrom(skillEnumClass)) {
            Enum<?>[] enumConstants = (Enum<?>[]) skillEnumClass.getEnumConstants();
            if (enumConstants != null) {
                for (Enum<?> piece : enumConstants) {

                    try {
                        Field idField = skillEnumClass.getDeclaredField("id");
                        Field skillLevelField = skillEnumClass.getDeclaredField("skillLevel");
                        Field attackLevelField = skillEnumClass.getDeclaredField("attackLevel");
                        Field defenseLevelField = skillEnumClass.getDeclaredField("defenseLevel");
                        Field strengthLevelField = skillEnumClass.getDeclaredField("strengthLevel");
                        Field buyableField = skillEnumClass.getDeclaredField("buyable");
                        Field skillField = skillEnumClass.getDeclaredField("skill");

                        idField.setAccessible(true);
                        skillLevelField.setAccessible(true);
                        attackLevelField.setAccessible(true);
                        defenseLevelField.setAccessible(true);
                        strengthLevelField.setAccessible(true);
                        buyableField.setAccessible(true);

                        int pieceID = (int) idField.get(tool);
                        int skillLevel = (int) skillLevelField.get(piece);
                        int attackLevel = (int) attackLevelField.get(piece);
                        int defenseLevel = (int) defenseLevelField.get(piece);
                        int strengthLevel = (int) strengthLevelField.get(piece);
                        boolean buyable = (boolean) buyableField.get(piece);
                        Skill skill = (Skill) skillField.get(piece);
                        String pieceName = piece.name();

                        if (gearPiece == null) {
                            processPiece(pieceID, skillLevel, attackLevel, defenseLevel, strengthLevel, skill, buyable, pieceName);
                        }

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void processPiece(int pieceID, int skillLevel, int attackLevel int defenseLevel int strengthLevel, Skill skill, Boolean buyable, String pieceName) {
        debug("Processing Tool: "+pieceName);
        while (scriptStarted && hasToolReq(skill, skillLevel)) {
            debug("Has Tool Requirements: "+skillLevel+" "+skill.getName());
            if (hasTool(pieceID)) {
                Item piece = Inventory.getFirst(pieceID);
                Item wornTool = Equipment.getFirst(pieceID);
                if (piece != null) {
                    debug("Has Tool: "+piece.getName());
                } else {
                    debug("Has Tool: "+wornTool.getName());
                }
                if (canWield(attackLevel)) {
                    wieldTool(pieceID, attackLevel);
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
                    if (Inventory.isFull()) {
                        Bank.depositAllExcept(pieceID);
                        break;
                    }
                    if (!Bank.contains(pieceID)) {
                        if (buyable && canAfford(pieceID)) {
                            debug("Attempting to buy gear piece: " + pieceName);
                            automateBuy(pieceID, 1, 5);
                            sleep(600, 1200);
                        } else {
                            break;
                        }
                    } else {
                        if (!Inventory.isFull()) {
                            Bank.withdraw(pieceID, 1, Bank.WithdrawMode.ITEM);
                            sleep(600, 1200);
                        }
                    }
                }
            }
        }
    }

    private static boolean hasPieceReq(Skill skill, int skillLevel) {
        return (Skills.getLevel(skill) >= skillLevel);
    }

    private static boolean canWear(int attackLevel || int defenseLevel || int strenghtLevel) {
        return (Skills.getLevel(Skill.ATTACK) >= attackLevel || Skills.getLevel(Skill.DEFENSE) >= defenseLevel || Skills.getLevel(Skill.STRENGTH) >= strengthLevel);
    }

    private static boolean hasPiece(int id) {
        if (Inventory.contains(id) || Equipment.contains(id)) {
            Item piece = Inventory.getFirst(id);
            Item wornPiece = Equipment.getFirst(id);
            if (piece != null) {
                primaryPiece = piece.getName();
                primaryPieceID = id;
            } else {
                primaryPiece = wornPiece.getName();
                primaryPieceID = id;
            }
            return true;
        } else {
            return false;
        }
    }

    private static void wearPiece(int pieceID, int attackLevel, int defenseLevel, int strengthLevel) {
        if (canWear(attackLevel) || canWear(defenseLevel) || canWear(strengthLevel)) {
            Item item = Inventory.getFirst(pieceID);
            item.interact("Wear");
            sleep(600);
        }
    }

    @Getter
    public enum ChestPiece implements Equippable {

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

        ChestPiece(int id, int skillLevel, int attackLevel, boolean buyable) {
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