package net.automaters.api.ui;

import net.unethicalite.api.commons.Predicates;

import static net.unethicalite.api.items.Equipment.contains;


public class Equipment {

    public static boolean containsPartialName(String partialName) {
        return contains(Predicates.nameContains(partialName));
    }

}
