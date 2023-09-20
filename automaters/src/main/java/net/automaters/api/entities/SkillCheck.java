package net.automaters.api.entities;

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.mixins.Inject;
import net.unethicalite.api.game.Skills;

@SuppressWarnings({"unused"})
public class SkillCheck {
    @Inject
    private static Client client;

    private static int getBoostedSkillLevel(Skill skill) {
        return Skills.getBoostedLevel(skill);
    }

    public static int getWoodcuttingLevel() {
        return Skills.getBoostedLevel(Skill.WOODCUTTING);
    }

    public static int getFishingLevel() {
        return Skills.getBoostedLevel(Skill.FISHING);
    }

    public static int getAttackLevel() {
        return Skills.getBoostedLevel(Skill.ATTACK);
    }

    public static int getStrengthLevel() {
        return Skills.getBoostedLevel(Skill.STRENGTH);
    }

    public static int getDefenceLevel() {
        return Skills.getBoostedLevel(Skill.DEFENCE);
    }

    public static int getRangedLevel() {
        return Skills.getBoostedLevel(Skill.RANGED);
    }

    public static int getPrayerLevel() {
        return Skills.getBoostedLevel(Skill.PRAYER);
    }

    public static int getMagicLevel() {
        return Skills.getBoostedLevel(Skill.MAGIC);
    }

    public static int getRunecraftingLevel() {
        return Skills.getBoostedLevel(Skill.RUNECRAFT);
    }

    public static int getHitpointsLevel() {
        return Skills.getBoostedLevel(Skill.HITPOINTS);
    }

    public static int getCraftingLevel() {
        return Skills.getBoostedLevel(Skill.CRAFTING);
    }

    public static int getMiningLevel() {
        return Skills.getBoostedLevel(Skill.MINING);
    }

    public static int getCookingLevel() {
        return Skills.getBoostedLevel(Skill.COOKING);
    }

    public static int getFiremakingLevel() {
        return Skills.getBoostedLevel(Skill.FIREMAKING);
    }

    public static int getAgilityLevel() {
        return Skills.getBoostedLevel(Skill.AGILITY);
    }

    public static int getHerbloreLevel() {
        return Skills.getBoostedLevel(Skill.HERBLORE);
    }

    public static int getThievingLevel() {
        return Skills.getBoostedLevel(Skill.THIEVING);
    }

    public static int getFletchingLevel() {
        return Skills.getBoostedLevel(Skill.FLETCHING);
    }

    public static int getSlayerLevel() {
        return Skills.getBoostedLevel(Skill.SLAYER);
    }

    public static int getFarmingLevel() {
        return Skills.getBoostedLevel(Skill.FARMING);
    }

    public static int getConstructionLevel() {
        return Skills.getBoostedLevel(Skill.CONSTRUCTION);
    }

    public static int getHunterLevel() {
        return Skills.getBoostedLevel(Skill.HUNTER);
    }

}