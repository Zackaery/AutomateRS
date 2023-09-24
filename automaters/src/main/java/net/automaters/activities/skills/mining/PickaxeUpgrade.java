package net.automaters.activities.skills.mining;

import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import static net.automaters.api.entities.SkillCheck.*;
import static net.automaters.api.utils.Debug.debug;
import static net.unethicalite.api.commons.Time.sleep;
@SuppressWarnings({"ConstantConditions","unused"})
public class PickaxeUpgrade {
    public PickaxeUpgrade() {
        // Constructor
    }

    public void executePickaxeUpgrade() {
        String[] PickaxesToHandle = {
                "Bronze pickaxe", "Iron pickaxe", "Steel pickaxe", "Black pickaxe",
                "Mithril pickaxe", "Adamant pickaxe", "Rune pickaxe", "Dragon pickaxe", "Crystal pickaxe"
        };

        if (Bank.isOpen()) {
            String bestPickaxe = findBestPickaxe();

            if (bestPickaxe != null) {
                debug("Swapping for the best available pickaxe: " + bestPickaxe);
                debug("Attack level: " + getAttackLevel() + " Mining level: " + getMiningLevel());
                withdrawAndEquipPickaxe(bestPickaxe);
                sleep(500);
                depositLowerTierPickaxes(bestPickaxe);
                sleep(500);
                Bank.close();
                sleep(250);
            }
        }
    }

    private String findBestPickaxe() {
        String[] pickaxesToHandle = {
                "Bronze pickaxe", "Iron pickaxe", "Steel pickaxe", "Black pickaxe",
                "Mithril pickaxe", "Adamant pickaxe", "Rune pickaxe", "Dragon pickaxe", "Crystal pickaxe"
        };

        String bestPickaxe = null; // Initialize with no best pickaxe
        String currentlyEquippedPickaxe = null; // Initialize the currently equipped pickaxe to null

        // Check if a weapon is equipped before attempting to get its name
        if (Equipment.fromSlot(EquipmentInventorySlot.WEAPON) != null) {
            currentlyEquippedPickaxe = Equipment.fromSlot(EquipmentInventorySlot.WEAPON).getName();
        }

        // Iterate through the pickaxes in reverse order (highest-tier first)
        for (int i = pickaxesToHandle.length - 1; i >= 0; i--) {
            String itemName = pickaxesToHandle[i];

            // Check if the pickaxe should be considered based on Mining level
            boolean shouldConsiderPickaxe = true;

            switch (itemName) {
                case "Steel pickaxe":
                    shouldConsiderPickaxe = getMiningLevel() >= 6 && getAttackLevel() >= 5;
                    debug("Considering Steel pickaxe.");
                    break;
                case "Black pickaxe":
                    shouldConsiderPickaxe = getMiningLevel() >= 11 && getAttackLevel() >= 10;
                    debug("Considering Black pickaxe.");
                    break;
                case "Mithril pickaxe":
                    shouldConsiderPickaxe = getMiningLevel() >= 21 && getAttackLevel() >= 20;
                    debug("Considering Mithril pickaxe.");
                    break;
                case "Adamant pickaxe":
                    shouldConsiderPickaxe = getMiningLevel() >= 31 && getAttackLevel() >= 30;
                    debug("Considering Adamant pickaxe.");
                    break;
                case "Rune pickaxe":
                    shouldConsiderPickaxe = getMiningLevel() >= 41 && getAttackLevel() >= 40;
                    debug("Considering Rune pickaxe.");
                    break;
                case "Dragon pickaxe":
                    shouldConsiderPickaxe = getMiningLevel() >= 61 && getAttackLevel() >= 60;
                    debug("Considering Dragon pickaxe.");
                    break;
                case "Crystal pickaxe":
                    shouldConsiderPickaxe = getMiningLevel() >= 71 && getAttackLevel() >= 70;
                    debug("Considering Crystal pickaxe.");
                    break;
            }

            // If the pickaxe should be considered and it's in the bank, return it as the best pickaxe
            if (shouldConsiderPickaxe && Bank.isOpen() && !Inventory.contains(itemName) && Bank.contains(itemName)) {
                // Check if it's better than the currently equipped pickaxe
                if (currentlyEquippedPickaxe == null || itemName.compareTo(currentlyEquippedPickaxe) > 0) {
                    bestPickaxe = itemName;
                    currentlyEquippedPickaxe = itemName;
                }
            }
        }

        return bestPickaxe; // Return the best available pickaxe or null if none found
    }




    private void withdrawAndEquipPickaxe(String pickaxeName) {
        // Withdraw item
        if (Bank.isOpen() && !Equipment.contains(pickaxeName) && Bank.contains(pickaxeName) && !Inventory.contains(pickaxeName)) {
            Bank.withdraw(pickaxeName, 1, Bank.WithdrawMode.ITEM);
            sleep(800,1500);
        }

        // Wear item
        if (Inventory.contains(pickaxeName) && !Equipment.contains(pickaxeName)) {
            if (!Bank.isOpen()) {
                Item item = Inventory.getFirst(pickaxeName);
                if (item != null) {
                    debug("Equipping pickaxe!");
                    item.interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);
                }
            } else {
                if (Bank.isOpen()) {
                    Bank.Inventory.getFirst(pickaxeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);
                }
            }
        }
    }
    private void depositLowerTierPickaxes(String bestPickaxeName) {
        String[] lowerTierPickaxes = {
                "Bronze pickaxe", "Iron pickaxe", "Steel pickaxe", "Black pickaxe",
                "Mithril pickaxe", "Adamant pickaxe", "Rune pickaxe", "Dragon pickaxe"
        };

        for (String pickaxeName : lowerTierPickaxes) {
            if (Inventory.contains(pickaxeName) && !pickaxeName.equals(bestPickaxeName)) {
                debug("Depositing lower-tier pickaxe: " + pickaxeName);
                Bank.deposit(pickaxeName, Inventory.getCount(pickaxeName));
                sleep(250,500);
            }
        }
    }
}