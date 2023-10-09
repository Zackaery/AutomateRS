package net.automaters.themes;

import com.formdev.flatlaf.FlatDarkLaf;

public class AutomateLaf extends FlatDarkLaf {
    public static boolean setup() {
        return setup( new net.automaters.gui.themes.AutomateLaf() );
    }

    @Override
    public String getName() {
        return "AutomateLaf";
    }
}