package net.automaters.activities.skills.woodcutting;

import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.Rune;

import static net.automaters.api.entities.LocalPlayer.openGE;
import static net.automaters.api.entities.SkillCheck.getAttackLevel;
import static net.automaters.api.entities.SkillCheck.getWoodcuttingLevel;
import static net.automaters.api.utils.Debug.debug;
import static net.unethicalite.api.commons.Time.sleep;
import static net.automaters.api.ui.GrandExchange.*;

@SuppressWarnings({"ConstantConditions","unused"})
public class AxeUpgrade {

    String Bronzeaxe = "Bronze axe";
    String Ironaxe = "Iron axe";
    String Steelaxe = "Steel axe";
    String Blackaxe = "Black axe";
    String Mithrilaxe = "Mithril axe";
    String Adamantaxe = "Adamant axe";
    String Runeaxe = "Rune axe";
    String Dragonaxe = "Dragon axe";
    String Crystalaxe = "Crystal axe";

    public AxeUpgrade() {
        // Constructor
    }
    public void executeAxeUpgradeBuy() {

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
        String currentlyEquippedAxe = null; // Initialize the currently equipped axe to null

        // Check if a weapon is equipped before attempting to get its name
        if (Equipment.fromSlot(EquipmentInventorySlot.WEAPON) != null) {
            currentlyEquippedAxe = Equipment.fromSlot(EquipmentInventorySlot.WEAPON).getName();
        }


        // Iterate through the axes in reverse order (highest-tier first)
        for (int i = axesToHandle.length - 1; i >= 0; i--) {
            String itemName = axesToHandle[i];

            // Check if the axe should be considered based on Woodcutting level
            boolean shouldConsiderAxe = true;
            int buyaxe = 0;

            switch (itemName) {
                case "Bronze axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 1 && getWoodcuttingLevel() < 6;
                    debug("Considering Bronze axe.");
                    buyaxe = ItemID.BRONZE_AXE;

                    break;
                case "Iron axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 1 && getWoodcuttingLevel() < 6;
                    debug("Considering Iron axe.");
                    buyaxe = ItemID.IRON_AXE;

                    break;
                case "Steel axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 6 && getWoodcuttingLevel() < 11;
                    debug("Considering Steel axe.");
                    buyaxe = ItemID.STEEL_AXE;

                    break;
                case "Black axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 11 && getWoodcuttingLevel() < 21;
                    debug("Considering Black axe.");
                    buyaxe = ItemID.BLACK_AXE;
                    break;
                case "Mithril axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 21 && getWoodcuttingLevel() < 31;
                    debug("Considering Mithril axe.");
                    buyaxe = ItemID.MITHRIL_AXE;
                    break;
                case "Adamant axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 31 && getWoodcuttingLevel() < 41;
                    debug("Considering Adamant axe.");
                    buyaxe = ItemID.ADAMANT_AXE;
                    break;
                case "Rune axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 41 && getWoodcuttingLevel() < 61;
                    debug("Considering Rune axe.");
                    buyaxe = ItemID.RUNE_AXE;
                    break;
                case "Dragon axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 61 && getWoodcuttingLevel() < 71;
                    debug("Considering Dragon axe.");
                    buyaxe = ItemID.DRAGON_AXE;
                    break;
                case "Crystal axe":
                    shouldConsiderAxe = getWoodcuttingLevel() >= 71 && getAttackLevel() >= 70;
                    debug("Considering Crystal axe.");
                    buyaxe = ItemID.CRYSTAL_AXE;
                    break;
            }


            if (shouldConsiderAxe && Bank.isOpen() && !Inventory.contains(itemName) && !Bank.contains(itemName) && !Equipment.contains(itemName)) {
                debug("Need to buy axe, attempting to purchase: " + buyaxe + " Which is: " + itemName);
                sleep(1500);
                automateBuy(buyaxe,1,2);
            } else
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
                    if (axeName.equals(Bronzeaxe)) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(axeName), 3000);
                    } else if (axeName.equals(Ironaxe)) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                    } else if (axeName.equals(Steelaxe) && getAttackLevel() >= 5) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                    } else if (axeName.equals(Blackaxe) && getAttackLevel() >= 10) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                    } else if (axeName.equals(Mithrilaxe) && getAttackLevel() >= 20) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                    } else if (axeName.equals(Adamantaxe) && getAttackLevel() >= 30) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                    } else if (axeName.equals(Runeaxe) && getAttackLevel() >= 40) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                    } else if (axeName.equals(Dragonaxe) && getAttackLevel() >= 60) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                    } else if (axeName.equals(Crystalaxe) && getAttackLevel() >= 70) {
                        item.interact("Wield");
                        Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                    }
                }
            } else {
                if (axeName.equals(Bronzeaxe)) {
                    Bank.Inventory.getFirst(axeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(axeName), 3000);
                } else if (axeName.equals(Ironaxe)) {
                    Bank.Inventory.getFirst(axeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                } else if (axeName.equals(Steelaxe) && getAttackLevel() >= 5) {
                    Bank.Inventory.getFirst(axeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                } else if (axeName.equals(Blackaxe) && getAttackLevel() >= 10) {
                    Bank.Inventory.getFirst(axeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                } else if (axeName.equals(Mithrilaxe) && getAttackLevel() >= 20) {
                    Bank.Inventory.getFirst(axeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                } else if (axeName.equals(Adamantaxe) && getAttackLevel() >= 30) {
                    Bank.Inventory.getFirst(axeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                } else if (axeName.equals(Runeaxe) && getAttackLevel() >= 40) {
                    Bank.Inventory.getFirst(axeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                } else if (axeName.equals(Dragonaxe) && getAttackLevel() >= 60) {
                    Bank.Inventory.getFirst(axeName).interact("Wield");
                    Time.sleepUntil(() -> Equipment.contains(axeName), 3000);

                } else if (axeName.equals(Crystalaxe) && getAttackLevel() >= 70) {
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