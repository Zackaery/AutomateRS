package net.automaters.api.entities;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.mixins.Inject;
import net.unethicalite.api.items.Inventory;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class CombatAPI {

    // get closest npc designated by "distance" in the following double after this one calculating

    @Inject
    private static Client client;
    public static double calculateDistanceToNPC(Player localPlayer, String npcName) {
        double minDistance = Double.MAX_VALUE; // Initialize with a high value
        NPC closestNPC = null;

        for (NPC npc : client.getNpcs()) {
            if (npc.getName() != null && npc.getName().equalsIgnoreCase(npcName)) {
                // Get the local point of the local player and the NPC
                LocalPoint playerLocalPoint = localPlayer.getLocalLocation();
                LocalPoint npcLocalPoint = npc.getLocalLocation();

                // Calculate the distance using Euclidean distance formula
                int deltaX = playerLocalPoint.getX() - npcLocalPoint.getX();
                int deltaY = playerLocalPoint.getY() - npcLocalPoint.getY();
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                // Check if this NPC is closer than the previous closest
                if (distance < minDistance) {
                    minDistance = distance;
                    closestNPC = npc;
                }
            }
        }

        return minDistance;
    }

    // example if (distance("Chicken") < 10) { fight } this will grab the closest chicken within 10 tiles.
    public static double distance(String npcName) {

        return calculateDistanceToNPC(client.getLocalPlayer(), npcName);
    }

    // eats food if hp threshold is <= to what is set
    // example
    //
    // if (needtoHeal(35)) { eatFood(SHARK) }
    //
    // Player is at or belo 35% health, eats a shark.

    public boolean needtoHeal(int percentageThreshold) {
        int maxHP = client.getRealSkillLevel(Skill.HITPOINTS);
        int currentHP = client.getBoostedSkillLevel(Skill.HITPOINTS);

        // Calculate the current HP percentage
        int hpPercentage = (currentHP * 100) / maxHP;

        return hpPercentage <= percentageThreshold;
    }

    public void eatFood(String foodName) {
        if (Inventory.contains(foodName)) {
            Inventory.getFirst(foodName).interact("Eat");
        }
    }

}
