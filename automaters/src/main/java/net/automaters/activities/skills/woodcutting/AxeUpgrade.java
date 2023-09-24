package net.automaters.activities.skills.woodcutting;

import net.runelite.api.Item;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import static net.automaters.api.entities.SkillCheck.getAttackLevel;
import static net.automaters.api.entities.SkillCheck.getWoodcuttingLevel;
import static net.automaters.api.utils.Debug.debug;
import static net.unethicalite.api.commons.Time.sleep;

public class AxeUpgrade {
    public AxeUpgrade() {
        // Constructor
    }

    public void executeAxeUpgrade() {
        String[] axesToHandle = {
                "Bronze axe", "Iron axe", "Steel axe", "Black axe",
                "Mithril axe", "Adamant axe", "Rune axe", "Dragon axe", "Crystal axe"
        };

        if (Bank.isOpen()) {
            String bestAxe = findBestAxe();

            if (bestAxe != null) {
                debug("Swapping for the best available axe: " + bestAxe);
                withdrawAndEquipAxe(bestAxe);
                sleep(500);
                depositLowerTierAxes(bestAxe);
                sleep(500);
                Bank.close();
                sleep(250);
            }
        }
    }

    private String findBestAxe() {
        String[] axesToHandle = {
                "Bronze axe", "Iron axe", "Steel axe", "Black axe",
                "Mithril axe", "Adamant axe", "Rune axe", "Dragon axe", "Crystal axe"
        };

        String bestAxe = null; // Initialize with no best axe

        // Iterate through the axes in reverse order (highest-tier first)
        for (int i = axesToHandle.length - 1; i >= 0; i--) {
            String itemName = axesToHandle[i];

            // Check if the axe should be skipped based on Woodcutting level
            boolean shouldSkipAxe = false;

            switch (itemName) {
                case "Steel axe":
                    shouldSkipAxe = getWoodcuttingLevel() < 6 && getAttackLevel() < 5;
                    debug("Skipping Steel axe.");
                    break;
                case "Black axe":
                    shouldSkipAxe = getWoodcuttingLevel() < 11 && getAttackLevel() < 10;
                    debug("Skipping Black axe.");
                    break;
                case "Mithril axe":
                    shouldSkipAxe = getWoodcuttingLevel() < 21 && getAttackLevel() < 20;
                    debug("Skipping Mithril axe.");
                    break;
                case "Adamant axe":
                    shouldSkipAxe = getWoodcuttingLevel() < 31 && getAttackLevel() < 30;
                    debug("Skipping Adamant axe.");
                    break;
                case "Rune axe":
                    shouldSkipAxe = getWoodcuttingLevel() < 41 && getAttackLevel() < 40;
                    debug("Skipping Rune axe.");
                    break;
                case "Dragon axe":
                    shouldSkipAxe = getWoodcuttingLevel() < 61 && getAttackLevel() < 60;
                    debug("Skipping Dragon axe.");
                    break;
                case "Crystal axe":
                    shouldSkipAxe = getWoodcuttingLevel() < 71 && getAttackLevel() < 70;
                    debug("Skipping Crystal axe.");
                    break;
            }

            // If the axe should be skipped, continue to the next axe
            if (shouldSkipAxe) {
                continue;
            }

            // Check if the axe is in the bank and the attack level is sufficient
            if (Bank.isOpen() && !Equipment.contains(itemName) && Bank.contains(itemName) && !Inventory.contains(itemName)) {
                bestAxe = itemName; // Update the best available axe
            }
        }

        return bestAxe; // Return the best available axe or null if none found
    }


    private void withdrawAndEquipAxe(String axeName) {
        // Withdraw item
        if (Bank.isOpen() && !Equipment.contains(axeName) && Bank.contains(axeName) && !Inventory.contains(axeName)) {
            Bank.withdraw(axeName, 1, Bank.WithdrawMode.ITEM);
            sleep(800,1500);
        }

        // Wear item
        if (Inventory.contains(axeName) && !Equipment.contains(axeName)) {
            if (!Bank.isOpen()) {
                Item item = Inventory.getFirst(axeName);
                if (item != null) {
                    debug("Equipping axe!");
                    item.interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(axeName), 3000);
                }
            } else {
                if (Bank.isOpen()) {
                    Bank.Inventory.getFirst(axeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(axeName), 3000);
                }
            }
        }
    }
    private void depositLowerTierAxes(String bestAxeName) {
        String[] lowerTierAxes = {
                "Bronze axe", "Iron axe", "Steel axe", "Black axe",
                "Mithril axe", "Adamant axe", "Rune axe", "Dragon axe"
        };

        for (String axeName : lowerTierAxes) {
            if (Inventory.contains(axeName) && !axeName.equals(bestAxeName)) {
                debug("Depositing lower-tier axe: " + axeName);
                Bank.deposit(axeName, Inventory.getCount(axeName));
                sleep(250,500);
            }
        }
    }
}