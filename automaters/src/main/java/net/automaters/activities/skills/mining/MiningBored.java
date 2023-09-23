package net.automaters.activities.skills.mining;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.mixins.Inject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.plugins.LoopedPlugin;
import net.automaters.util.locations.mining_rectangularareas;

import java.util.Comparator;
import java.util.Random;

import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.entities.SkillCheck.*;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.util.locations.woodcutting_rectangularareas.*;

@SuppressWarnings({"ConstantConditions","unused"})
public class MiningBored extends LoopedPlugin {

    @Inject
    private Client client;

    // Just another woodcutting script...

    @Override
    public int loop() {
        debug("Holy shit this is in the loop");

        return 0;
    }
}

