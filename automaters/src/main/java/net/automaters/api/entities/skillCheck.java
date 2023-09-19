package net.automaters.api.entities;

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.mixins.Inject;

@SuppressWarnings({"unused"})
public class skillCheck {
    @Inject
    private static Client client;

    private static int getBoostedSkillLevel(Skill skill) {
        return client.getBoostedSkillLevel(skill);
    }

    public static int WoodcuttingLevel() {
        return getBoostedSkillLevel(Skill.WOODCUTTING);
    }

    public static int FishingLevel() {
        return getBoostedSkillLevel(Skill.FISHING);
    }

    public static int AttackLevel() {
        return getBoostedSkillLevel(Skill.ATTACK);
    }

    public static int StrengthLevel() {
        return getBoostedSkillLevel(Skill.STRENGTH);
    }

    public static int DefenceLevel() {
        return getBoostedSkillLevel(Skill.DEFENCE);
    }

    public static int RangedLevel() {
        return getBoostedSkillLevel(Skill.RANGED);
    }

    public static int PrayerLevel() {
        return getBoostedSkillLevel(Skill.PRAYER);
    }

    public static int MagicLevel() {
        return getBoostedSkillLevel(Skill.MAGIC);
    }

    public static int RunecraftingLevel() {
        return getBoostedSkillLevel(Skill.RUNECRAFT);
    }

    public static int HitpointsLevel() {
        return getBoostedSkillLevel(Skill.HITPOINTS);
    }

    public static int CraftingLevel() {
        return getBoostedSkillLevel(Skill.CRAFTING);
    }

    public static int MiningLevel() {
        return getBoostedSkillLevel(Skill.MINING);
    }

    public static int CookingLevel() {
        return getBoostedSkillLevel(Skill.COOKING);
    }

    public static int FiremakingLevel() {
        return getBoostedSkillLevel(Skill.FIREMAKING);
    }

    public static int AgilityLevel() {
        return getBoostedSkillLevel(Skill.AGILITY);
    }

    public static int HerbloreLevel() {
        return getBoostedSkillLevel(Skill.HERBLORE);
    }

    public static int ThievingLevel() {
        return getBoostedSkillLevel(Skill.THIEVING);
    }

    public static int FletchingLevel() {
        return getBoostedSkillLevel(Skill.FLETCHING);
    }

    public static int SlayerLevel() {
        return getBoostedSkillLevel(Skill.SLAYER);
    }

    public static int FarmingLevel() {
        return getBoostedSkillLevel(Skill.FARMING);
    }

    public static int ConstructionLevel() {
        return getBoostedSkillLevel(Skill.CONSTRUCTION);
    }

    public static int HunterLevel() {
        return getBoostedSkillLevel(Skill.HUNTER);
    }

}