package net.automaters.api.ui;

import net.runelite.api.Item;
import net.unethicalite.api.commons.Predicates;

import static net.unethicalite.api.items.Inventory.contains;

public class Inventory {
    public static boolean containsPartialName(String partialName) {
        return contains(Predicates.nameContains(partialName));
    }
}
