package net.automaters.activities.skills.woodcutting;

import net.runelite.api.EquipmentInventorySlot;
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
                "Bronze axe", "Iron axe", "Steel axe", "Black axe",
                "Mithril axe", "Adamant axe", "Rune axe", "Dragon axe", "Crystal axe"
        };

        String bestAxe = null; // Initialize with no best axe

        // Initialize the currently equipped axe to null
        String currentlyEquippedAxe = Equipment.fromSlot(EquipmentInventorySlot.WEAPON).getName();

        // Iterate through the axes in reverse order (highest-tier first)
        for (int i = axesToHandle.length - 1; i >= 0; i--) {
            String itemName = axesToHandle[i];

            // Check if the axe should be considered based on Woodcutting level
            boolean shouldConsiderAxe = true;

            switch (itemName) {
                case "Steel axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 6 && getAttackLevel() >= 5;
                    debug("Considering Steel axe.");
                    break;
                case "Black axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 11 && getAttackLevel() >= 10;
                    debug("Considering Black axe.");
                    break;
                case "Mithril axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 21 && getAttackLevel() >= 20;
                    debug("Considering Mithril axe.");
                    break;
                case "Adamant axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 31 && getAttackLevel() >= 30;
                    debug("Considering Adamant axe.");
                    break;
                case "Rune axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 41 && getAttackLevel() >= 40;
                    debug("Considering Rune axe.");
                    break;
                case "Dragon axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 61 && getAttackLevel() >= 60;
                    debug("Considering Dragon axe.");
                    break;
                case "Crystal axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 71 && getAttackLevel() >= 70;
                    debug("Considering Crystal axe.");
                    break;
            }

            // If the axe should be considered and it's in the bank, return it as the best axe
            if (shouldConsiderAxe && Bank.isOpen() && !Inventory.contains(itemName) && Bank.contains(itemName)) {
                // Check if it's better than the currently equipped axe
                if (currentlyEquippedAxe == null || itemName.compareTo(currentlyEquippedAxe) > 0) {
                    bestAxe = itemName;
                    currentlyEquippedAxe = itemName;
                }
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