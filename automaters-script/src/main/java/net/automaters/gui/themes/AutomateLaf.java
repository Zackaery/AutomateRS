package net.automaters.gui.themes;

import com.formdev.flatlaf.FlatDarkLaf;

public class AutomateLaf extends FlatDarkLaf {
    public static boolean setup() {
        return setup( new AutomateLaf() );
    }

    @Override
    public String getName() {
        return "AutomateLaf";
    }
}