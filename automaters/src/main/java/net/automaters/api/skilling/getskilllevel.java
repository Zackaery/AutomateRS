package net.automaters.api.skilling;

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.mixins.Inject;

import static net.automaters.script.AutomateRS.scriptStarted;



public class getskilllevel {
    @Inject
    private static Client client;

    public static int getwoodcuttingskill() {

        int getwoodcuttingskill = client.getBoostedSkillLevel(Skill.WOODCUTTING);
        return getwoodcuttingskill;
    }
    public static int getfishingskill() {

        int getfishingskill = client.getBoostedSkillLevel(Skill.FISHING);
        return getfishingskill;
    }


}