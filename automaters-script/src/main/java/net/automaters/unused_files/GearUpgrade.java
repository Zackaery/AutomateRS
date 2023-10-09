package net.automaters.unused_files;

import net.runelite.api.Item;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.automaters.api.entities.SkillCheck.getWoodcuttingLevel;
import static net.automaters.api.utils.Debug.debug;
import static net.unethicalite.api.commons.Time.sleep;

public class GearUpgrade {

    public void upgradeGear() {
        debug("Checking and upgrading gear...");

        String[] itemsToHandle = {
                "Lumberjack hat", "Lumberjack top", "Lumberjack legs", "Lumberjack boots",
                "Graceful hood", "Graceful top", "Graceful legs", "Graceful boots",
                "Graceful gloves", "Graceful cape", "ronman helm", "ron helm"
        };

        // Create a list to store items to withdraw
        List<String> itemsToWithdraw = new ArrayList<>();

        for (String itemName : itemsToHandle) {
            if (shouldSkipItem(itemName)) {
                continue;
            }

            // Check if the item is in the bank and not in the inventory
            if (Bank.contains(itemName) && !Inventory.contains(itemName)) {
                itemsToWithdraw.add(itemName);
            }
        }

        // Withdraw all items in the list
        if (!itemsToWithdraw.isEmpty()) {
            String[] itemsArray = itemsToWithdraw.toArray(new String[0]);
            for (String itemName : itemsToWithdraw) {
                Bank.withdraw(itemName, 1, Bank.WithdrawMode.ITEM);
                debug("Withdrew item: " + itemName);
                sleep(1500);
            }
            debug("Withdrew items: " + Arrays.toString(itemsArray));
            sleep(1500);
        }

        // Continue with the rest of the code for equipping items
        for (String itemName : itemsToHandle) {
            if (shouldSkipItem(itemName)) {
                continue;
            }

            // Wear item
            if (Inventory.contains(itemName) && !Equipment.contains(itemName) && !Bank.isOpen()) {
                Item item = Inventory.getFirst(itemName);
                if (item != null) {
                    item.interact("Wear");
                    debug("Equipped item: " + itemName);
                    Time.sleepUntil(() -> Equipment.contains(itemName), 3000);
                }
            }

            // Wear item if bank is open
            // Wear item
            if (Inventory.contains(itemName) && !Equipment.contains(itemName) && Bank.isOpen()) {
                Item item = Bank.Inventory.getFirst(itemName);
                if (item != null) {
                    item.interact("Wear");
                    debug("Equipped item: " + itemName);
                    Time.sleepUntil(() -> Equipment.contains(itemName), 3000);
                }
            }
        }
    }




    private boolean areItemsEquipped(String[] itemNames) {
        return Arrays.stream(itemNames).allMatch(Equipment::contains);
    }

    private boolean shouldSkipItem(String itemName) {
        if (getWoodcuttingLevel() < 44 && itemName.startsWith("Lumberjack")) {
            return true;
        }
        // Add other skip conditions here if needed
        return false;
    }
}
