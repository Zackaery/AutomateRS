package net.automaters.activities.skills.mining;

import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import static net.automaters.api.entities.SkillCheck.*;
import static net.automaters.api.ui.GrandExchange.automateBuy;
import static net.automaters.api.utils.Debug.debug;
import static net.unethicalite.api.commons.Time.sleep;
@SuppressWarnings({"ConstantConditions","unused"})
public class PickaxeUpgrade {

    String Bronzepickaxe = "Bronze pickaxe";
    String Ironpickaxe = "Iron pickaxe";
    String Steelpickaxe = "Steel pickaxe";
    String Blackpickaxe = "Black pickaxe";
    String Mithrilpickaxe = "Mithril pickaxe";
    String Adamantpickaxe = "Adamant pickaxe";
    String Runepickaxe = "Rune pickaxe";
    String Dragonpickaxe = "Dragon pickaxe";
    String Crystalpickaxe = "Crystal pickaxe";

    public PickaxeUpgrade() {
        // Constructor
    }
    public void executePickaxeUpgradeBuy() {

    }

    public void executePickaxeUpgrade() {
        String[] pickaxesToHandle = {
                "Bronze pickaxe", "Iron pickaxe", "Steel pickaxe", "Black pickaxe",
                "Mithril pickaxe", "Adamant pickaxe", "Rune pickaxe", "Dragon pickaxe", "Crystal pickaxe"
        };

        if (Bank.isOpen()) {
            String bestPickaxe = findBestPickaxe();

            if (bestPickaxe != null) {
                debug("Swapping for the best available axe: " + bestPickaxe);
                debug("Attack level: " + getAttackLevel() + " Woodcutting level: " + getWoodcuttingLevel());
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

        String bestPickaxe = null; // Initialize with no best axe
        String currentlyEquippedPickaxe = null; // Initialize the currently equipped axe to null

        // Check if a weapon is equipped before attempting to get its name
        if (Equipment.fromSlot(EquipmentInventorySlot.WEAPON) != null) {
            currentlyEquippedPickaxe = Equipment.fromSlot(EquipmentInventorySlot.WEAPON).getName();
        }


        // Iterate through the axes in reverse order (highest-tier first)
        for (int i = pickaxesToHandle.length - 1; i >= 0; i--) {
            String itemName = pickaxesToHandle[i];

            // Check if the axe should be considered based on Woodcutting level
            boolean shouldConsiderPickaxe = true;
            int buypickaxe = 0;

            switch (itemName) {
                case "Bronze axe":
                    shouldConsiderPickaxe = getWoodcuttingLevel() >= 1 && getWoodcuttingLevel() < 6;
                    debug("Considering Bronze axe.");
                    buypickaxe = ItemID.BRONZE_AXE;

                    break;
                case "Iron axe":
                    shouldConsiderPickaxe = getWoodcuttingLevel() >= 1 && getWoodcuttingLevel() < 6;
                    debug("Considering Iron axe.");
                    buypickaxe = ItemID.IRON_AXE;

                    break;
                case "Steel axe":
                    shouldConsiderPickaxe = getWoodcuttingLevel() >= 6 && getWoodcuttingLevel() < 11;
                    debug("Considering Steel axe.");
                    buypickaxe = ItemID.STEEL_AXE;

                    break;
                case "Black axe":
                    shouldConsiderPickaxe = getWoodcuttingLevel() >= 11 && getWoodcuttingLevel() < 21;
                    debug("Considering Black axe.");
                    buypickaxe = ItemID.BLACK_AXE;
                    break;
                case "Mithril axe":
                    shouldConsiderPickaxe = getWoodcuttingLevel() >= 21 && getWoodcuttingLevel() < 31;
                    debug("Considering Mithril axe.");
                    buypickaxe = ItemID.MITHRIL_AXE;
                    break;
                case "Adamant axe":
                    shouldConsiderPickaxe = getWoodcuttingLevel() >= 31 && getWoodcuttingLevel() < 41;
                    debug("Considering Adamant axe.");
                    buypickaxe = ItemID.ADAMANT_AXE;
                    break;
                case "Rune axe":
                    shouldConsiderPickaxe = getWoodcuttingLevel() >= 41 && getWoodcuttingLevel() < 61;
                    debug("Considering Rune axe.");
                    buypickaxe = ItemID.RUNE_AXE;
                    break;
                case "Dragon axe":
                    shouldConsiderPickaxe = getWoodcuttingLevel() >= 61 && getWoodcuttingLevel() < 71;
                    debug("Considering Dragon axe.");
                    buypickaxe = ItemID.DRAGON_AXE;
                    break;
                case "Crystal axe":
                    shouldConsiderPickaxe = getWoodcuttingLevel() >= 71 && getAttackLevel() >= 70;
                    debug("Considering Crystal axe.");
                    buypickaxe = ItemID.CRYSTAL_AXE;
                    break;
            }


            if (shouldConsiderPickaxe && Bank.isOpen() && !Inventory.contains(itemName) && !Bank.contains(itemName) && !Equipment.contains(itemName)) {
                debug("Need to buy axe, attempting to purchase: " + buypickaxe + " Which is: " + itemName);
                sleep(1500);
                automateBuy(buypickaxe,1,2);
            } else
                // If the axe should be considered and it's in the bank, return it as the best axe
                if (shouldConsiderPickaxe && Bank.isOpen() && !Inventory.contains(itemName) && Bank.contains(itemName)) {
                    // Check if it's better than the currently equipped axe
                    if (currentlyEquippedPickaxe == null || itemName.compareTo(currentlyEquippedPickaxe) > 0) {
                        bestPickaxe = itemName;
                        currentlyEquippedPickaxe = itemName;
                    }
                }
        }

        return bestPickaxe; // Return the best available axe or null if none found
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
                    debug("Equipping axe!");
                    if (pickaxeName.equals(Bronzepickaxe)) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);
                    } else if (pickaxeName.equals(Ironpickaxe)) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                    } else if (pickaxeName.equals(Steelpickaxe) && getAttackLevel() >= 5) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                    } else if (pickaxeName.equals(Blackpickaxe) && getAttackLevel() >= 10) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                    } else if (pickaxeName.equals(Mithrilpickaxe) && getAttackLevel() >= 20) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                    } else if (pickaxeName.equals(Adamantpickaxe) && getAttackLevel() >= 30) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                    } else if (pickaxeName.equals(Runepickaxe) && getAttackLevel() >= 40) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                    } else if (pickaxeName.equals(Dragonpickaxe) && getAttackLevel() >= 60) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                    } else if (pickaxeName.equals(Crystalpickaxe) && getAttackLevel() >= 70) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                    }
                }
            } else {
                if (pickaxeName.equals(Bronzepickaxe)) {
                    Bank.Inventory.getFirst(pickaxeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);
                } else if (pickaxeName.equals(Ironpickaxe)) {
                    Bank.Inventory.getFirst(pickaxeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                } else if (pickaxeName.equals(Steelpickaxe) && getAttackLevel() >= 5) {
                    Bank.Inventory.getFirst(pickaxeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                } else if (pickaxeName.equals(Blackpickaxe) && getAttackLevel() >= 10) {
                    Bank.Inventory.getFirst(pickaxeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                } else if (pickaxeName.equals(Mithrilpickaxe) && getAttackLevel() >= 20) {
                    Bank.Inventory.getFirst(pickaxeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                } else if (pickaxeName.equals(Adamantpickaxe) && getAttackLevel() >= 30) {
                    Bank.Inventory.getFirst(pickaxeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                } else if (pickaxeName.equals(Runepickaxe) && getAttackLevel() >= 40) {
                    Bank.Inventory.getFirst(pickaxeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                } else if (pickaxeName.equals(Dragonpickaxe) && getAttackLevel() >= 60) {
                    Bank.Inventory.getFirst(pickaxeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                } else if (pickaxeName.equals(Crystalpickaxe) && getAttackLevel() >= 70) {
                    Bank.Inventory.getFirst(pickaxeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(pickaxeName), 3000);

                }

            }
        }
    }
    private void depositLowerTierPickaxes(String bestPickaxeName) {
        String[] lowerTierPickaxes = {
                "Bronze axe", "Iron axe", "Steel axe", "Black axe",
                "Mithril axe", "Adamant axe", "Rune axe", "Dragon axe"
        };

        for (String pickaxeName : lowerTierPickaxes) {
            if (Inventory.contains(pickaxeName) && !pickaxeName.equals(bestPickaxeName)) {
                debug("Depositing lower-tier axe: " + pickaxeName);
                Bank.deposit(pickaxeName, Inventory.getCount(pickaxeName));
                sleep(250,500);
            }
        }
    }
}