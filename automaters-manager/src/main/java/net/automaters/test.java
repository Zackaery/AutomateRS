package net.automaters;

import javax.swing.*;
import java.awt.*;

public class test {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Centered Tabs Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Create empty filler tabs to center-align the actual tabs
        JPanel leftFiller = new JPanel();
        leftFiller.setOpaque(true); // Make it transparent
        JPanel rightFiller = new JPanel();
        rightFiller.setOpaque(true);

        // Create actual tabs
        JPanel tab1 = new JPanel();
        tab1.add(new JLabel("Tab 1 Content"));

        JPanel tab2 = new JPanel();
        tab2.add(new JLabel("Tab 2 Content"));

        // Add tabs and fillers to the tabbed pane
        tabbedPane.addTab("                    ", leftFiller); // Left filler
        tabbedPane.addTab("Tab 1", tab1);
        tabbedPane.addTab("Tab 2", tab2);
        tabbedPane.addTab("                    ", rightFiller); // Right filler

        // Set the layout policy to wrap tabs if necessary
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}