package net.automaters.activities.skills.mining;

import net.runelite.api.Item;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import static net.automaters.api.entities.SkillCheck.getAttackLevel;
import static net.automaters.api.entities.SkillCheck.getWoodcuttingLevel;
import static net.automaters.api.utils.Debug.debug;
import static net.unethicalite.api.commons.Time.sleep;
@SuppressWarnings({"ConstantConditions","unused"})
public class PickaxeUpgrade {
    public PickaxeUpgrade() {
        // Constructor
    }

    public void executeAxeUpgrade() {
        String[] axesToHandle = {
                "Bronze pickaxe", "Iron pickaxe", "Steel pickaxe", "Black pickaxe",
                "Mithril pickaxe", "Adamant pickaxe", "Rune pickaxe", "Dragon pickaxe", "Crystal pickaxe"
        };

        if (Bank.isOpen()) {
            String bestAxe = findBestAxe();

            if (bestAxe != null) {
                debug("Swapping for the best available pickaxe: " + bestAxe);
                debug("Attack level: " + getAttackLevel() + " Woodcutting level: " + getWoodcuttingLevel());
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
                "Bronze pickaxe", "Iron pickaxe", "Steel pickaxe", "Black pickaxe",
                "Mithril pickaxe", "Adamant pickaxe", "Rune pickaxe", "Dragon pickaxe", "Crystal pickaxe"
        };

        String bestAxe = null; // Initialize with no best axe

        // Iterate through the axes in reverse order (highest-tier first)
        for (int i = axesToHandle.length - 1; i >= 0; i--) {
            String itemName = axesToHandle[i];

            // Check if the axe should be considered based on Woodcutting level
            boolean shouldConsiderAxe = true;

            switch (itemName) {
                case "Steel pickaxe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 6 && getAttackLevel() >= 5;
                    debug("Considering Steel pickaxe.");
                    break;
                case "Black pickaxe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 11 && getAttackLevel() >= 10;
                    debug("Considering Black pickaxe.");
                    break;
                case "Mithril pickaxe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 21 && getAttackLevel() >= 20;
                    debug("Considering Mithril pickaxe.");
                    break;
                case "Adamant pickaxe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 31 && getAttackLevel() >= 30;
                    debug("Considering Adamant pickaxe.");
                    break;
                case "Rune pickaxe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 41 && getAttackLevel() >= 40;
                    debug("Considering Rune pickaxe.");
                    break;
                case "Dragon pickaxe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 61 && getAttackLevel() >= 60;
                    debug("Considering Dragon pickaxe.");
                    break;
                case "Crystal pickaxe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 71 && getAttackLevel() >= 70;
                    debug("Considering Crystal pickaxe.");
                    break;
            }

            // If the axe should be considered and it's in the bank, return it as the best axe
            if (shouldConsiderAxe && Bank.isOpen() && !Equipment.contains(itemName) && Bank.contains(itemName) && !Inventory.contains(itemName)) {
                return itemName; // Return the first suitable axe found
            }
        }

        return bestAxe; // Return null if no suitable axe is found
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
                    debug("Equipping pickaxe!");
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
                "Bronze pickaxe", "Iron pickaxe", "Steel pickaxe", "Black pickaxe",
                "Mithril pickaxe", "Adamant pickaxe", "Rune pickaxe", "Dragon pickaxe"
        };

        for (String axeName : lowerTierAxes) {
            if (Inventory.contains(axeName) && !axeName.equals(bestAxeName)) {
                debug("Depositing lower-tier pickaxe: " + axeName);
                Bank.deposit(axeName, Inventory.getCount(axeName));
                sleep(250,500);
            }
        }
    }
}