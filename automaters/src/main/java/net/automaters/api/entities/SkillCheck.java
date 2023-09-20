package net.automaters.api.entities;

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.mixins.Inject;

@SuppressWarnings({"unused"})
public class SkillCheck {
    @Inject
    private static Client client;

    private static int getBoostedSkillLevel(Skill skill) {
        return client.getBoostedSkillLevel(skill);
    }

    public static int getWoodcuttingLevel() {
        return getBoostedSkillLevel(Skill.WOODCUTTING);
    }

    public static int getFishingLevel() {
        return getBoostedSkillLevel(Skill.FISHING);
    }

    public static int getAttackLevel() {
        return getBoostedSkillLevel(Skill.ATTACK);
    }

    public static int getStrengthLevel() {
        return getBoostedSkillLevel(Skill.STRENGTH);
    }

    public static int getDefenceLevel() {
        return getBoostedSkillLevel(Skill.DEFENCE);
    }

    public static int getRangedLevel() {
        return getBoostedSkillLevel(Skill.RANGED);
    }

    public static int getPrayerLevel() {
        return getBoostedSkillLevel(Skill.PRAYER);
    }

    public static int getMagicLevel() {
        return getBoostedSkillLevel(Skill.MAGIC);
    }

    public static int getRunecraftingLevel() {
        return getBoostedSkillLevel(Skill.RUNECRAFT);
    }

    public static int getHitpointsLevel() {
        return getBoostedSkillLevel(Skill.HITPOINTS);
    }

    public static int getCraftingLevel() {
        return getBoostedSkillLevel(Skill.CRAFTING);
    }

    public static int getMiningLevel() {
        return getBoostedSkillLevel(Skill.MINING);
    }

    public static int getCookingLevel() {
        return getBoostedSkillLevel(Skill.COOKING);
    }

    public static int getFiremakingLevel() {
        return getBoostedSkillLevel(Skill.FIREMAKING);
    }

    public static int getAgilityLevel() {
        return getBoostedSkillLevel(Skill.AGILITY);
    }

    public static int getHerbloreLevel() {
        return getBoostedSkillLevel(Skill.HERBLORE);
    }

    public static int getThievingLevel() {
        return getBoostedSkillLevel(Skill.THIEVING);
    }

    public static int getFletchingLevel() {
        return getBoostedSkillLevel(Skill.FLETCHING);
    }

    public static int getSlayerLevel() {
        return getBoostedSkillLevel(Skill.SLAYER);
    }

    public static int getFarmingLevel() {
        return getBoostedSkillLevel(Skill.FARMING);
    }

    public static int getConstructionLevel() {
        return getBoostedSkillLevel(Skill.CONSTRUCTION);
    }

    public static int getHunterLevel() {
        return getBoostedSkillLevel(Skill.HUNTER);
    }

}