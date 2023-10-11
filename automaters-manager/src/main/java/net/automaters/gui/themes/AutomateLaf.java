package net.automaters.gui.themes;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class AutomateLaf extends FlatDarkLaf {
    public static boolean setup() {
        return setup( new AutomateLaf() );
    }
    

    @Override
    public String getName() {
        return "AutomateLaf";
    }
}