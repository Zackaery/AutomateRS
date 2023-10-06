package net.automaters.tasks;

import lombok.Getter;
import net.runelite.api.Skill;

import static net.automaters.gui.tabbed_panel.skilling_goals.Combat.*;
import static net.automaters.gui.tabbed_panel.skilling_goals.Gathering.*;
import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.*;
import static net.automaters.gui.tabbed_panel.skilling_goals.Support.*;
import static net.automaters.tasks.Task.getGoal;

@Getter
public enum SkillingTasks {

    // COMBAT SKILLS
    ATTACK(Skill.ATTACK, getGoal(goalAttack)),
    STRENGTH(Skill.STRENGTH, getGoal(goalStrength)),
    DEFENCE(Skill.DEFENCE, getGoal(goalDefence)),
    RANGED(Skill.RANGED, getGoal(goalRanged)),
    MAGIC(Skill.MAGIC, getGoal(goalMagic)),
    PRAYER(Skill.PRAYER, getGoal(goalPrayer)),

    // GATHERING SKILLS
    MINING(Skill.MINING, getGoal(goalMining)),
    FISHING(Skill.FISHING, getGoal(goalFishing)),
    WOODCUTTING(Skill.WOODCUTTING, getGoal(goalWoodcutting)),
    FARMING(Skill.FARMING, getGoal(goalFarming)),
    HUNTER(Skill.HUNTER, getGoal(goalHunter)),

    // ARTISAN SKILLS
    HERBLORE(Skill.HERBLORE, getGoal(goalHerblore)),
    CRAFTING(Skill.CRAFTING, getGoal(goalCrafting)),
    FLETCHING(Skill.FLETCHING, getGoal(goalFletching)),
    SMITHING(Skill.SMITHING, getGoal(goalSmithing)),
    COOKING(Skill.COOKING, getGoal(goalCooking)),
    FIREMAKING(Skill.FIREMAKING, getGoal(goalFiremaking)),
    RUNECRAFT(Skill.RUNECRAFT, getGoal(goalRunecrafting)),
    CONSTRUCTION(Skill.CONSTRUCTION, getGoal(goalConstruction)),

    // SUPPORT SKILLS
    AGILITY(Skill.AGILITY, getGoal(goalAgility)),
    THIEVING(Skill.THIEVING, getGoal(goalThieving)),
    SLAYER(Skill.SLAYER, getGoal(goalSlayer));
    ;

    private final Skill skill;
    private final int skillGoal;

    SkillingTasks(Skill skill, int skillGoal) {
        this.skill = skill;
        this.skillGoal = skillGoal;
    }
}


