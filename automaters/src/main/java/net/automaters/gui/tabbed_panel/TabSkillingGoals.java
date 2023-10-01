package net.automaters.gui.tabbed_panel;

import net.automaters.gui.tabbed_panel.skilling_goals.Artisan;
import net.automaters.gui.tabbed_panel.skilling_goals.Combat;
import net.automaters.gui.tabbed_panel.skilling_goals.Gathering;
import net.automaters.gui.tabbed_panel.skilling_goals.Support;

import javax.swing.*;
import java.io.IOException;

import static net.automaters.gui.GUI.F2P;
import static net.automaters.gui.GUI.tabSkillingGoalsInitialized;
import static net.automaters.gui.tabbed_panel.skilling_goals.Artisan.*;
import static net.automaters.gui.tabbed_panel.skilling_goals.Combat.*;
import static net.automaters.gui.tabbed_panel.skilling_goals.Gathering.*;
import static net.automaters.gui.tabbed_panel.skilling_goals.Support.*;
import static net.automaters.api.utils.Debug.debug;

public class TabSkillingGoals
{

    public static JPanel panelTabSkillingGoals;

    public static JCheckBox checkBoxEnableRandomSkilling;
    public static JLabel labelRandomSkillingChance;
    public static JSpinner spinnerRandomSkillingChance;

    public static JButton buttonToggleSupport;
    public static JButton buttonToggleCombat;
    public static JButton buttonToggleArtisan;
    public static JButton buttonToggleGathering;
    public static JButton buttonMaxAccount;
    public static JButton buttonResetSkills;

    public static boolean combat;
    public static boolean artisan;
    public static boolean gathering;
    public static boolean support;


    public static void create() throws IOException
    {
        if (!combat)
        {
            debug("Combat.create()");
            Combat.create();
        }
        else if (!artisan)
        {
            debug("Artisan.create()");
            Artisan.create();
        }
        else if (!gathering)
        {
            debug("Gathering.create()");
            Gathering.create();
        }
        else if (!support)
        {
            debug("Support.create()");
            Support.create();
        }
        else
        {
            debug("randomSkilling()");
            randomSkilling();
            debug("buttonHandler()");
            buttonHandler();
            tabSkillingGoalsInitialized = true;
            debug("tabSkillingGoalsInitialized = TRUE");
        }
    }

    public static void randomSkilling() {
        debug("randomSkilling() INSIDE");
        checkBoxEnableRandomSkilling = new JCheckBox();
        checkBoxEnableRandomSkilling.setBounds(10, 20, 165, 23);

        spinnerRandomSkillingChance = new JSpinner();
        spinnerRandomSkillingChance.setBounds(10, 50, 50, 20);

        labelRandomSkillingChance = new JLabel();
        labelRandomSkillingChance.setBounds(64, 53, 156, 14);

        // ---- checkBoxEnableRandomSkilling ----
        checkBoxEnableRandomSkilling.setText(" Enable Random Skilling");
        checkBoxEnableRandomSkilling.addItemListener(e ->  {
                spinnerRandomSkillingChance.setEnabled(checkBoxEnableRandomSkilling.isSelected());
                labelRandomSkillingChance.setEnabled(checkBoxEnableRandomSkilling.isSelected());
                spinnerRandomSkillingChance.setValue(0);
            });

        // ---- spinnerRandomSkillingChance ----
        spinnerRandomSkillingChance.setModel(new SpinnerNumberModel(0, 0, 100, 5));
        spinnerRandomSkillingChance.setEnabled(false);

        // ---- labelRandomSkillingChance ----
        labelRandomSkillingChance.setText(" % Chance To Initiate");
        labelRandomSkillingChance.setEnabled(false);
    }

    public static void buttonHandler() {
        debug("buttonHandler() INSIDE");
        buttonToggleSupport = new JButton();
        buttonToggleSupport.setBounds(52, 91, 120, 20);
        buttonToggleSupport.setText("Disable");
        buttonToggleSupport.addActionListener(e -> {
            if ((F2P == false) && goalAgility.isEnabled() && goalAgility != null ) {
                setAccount_Support(false);
                buttonToggleSupport.setText("Enable");
            } else {
                setAccount_Support(true);
                buttonToggleSupport.setText("Disable");
            }
            if (F2P == true) {
                buttonToggleSupport.setText("Enable");
                setAccount_F2P();
            }
        });

        buttonToggleCombat = new JButton();
        buttonToggleCombat.setBounds(52, 151, 120, 20);
        buttonToggleCombat.setText("Disable");
        buttonToggleCombat.addActionListener(e -> {
            if (goalAttack.isEnabled() && goalAttack != null) {
                setAccount_Combat(false);
                buttonToggleCombat.setText("Enable");
            } else {
                setAccount_Combat(true);
                buttonToggleCombat.setText("Disable");
            }
            if (F2P == true) {
                buttonToggleSupport.setText("Enable");
                setAccount_F2P();
            }
        });

        buttonToggleArtisan = new JButton();
        buttonToggleArtisan.setBounds(52, 198, 120, 20);
        buttonToggleArtisan.setText("Disable");
        buttonToggleArtisan.addActionListener(e -> {
            if (goalCrafting.isEnabled() && goalCrafting != null ) {
                setAccount_Artisan(false);
                buttonToggleArtisan.setText("Enable");
            } else {
                setAccount_Artisan(true);
                buttonToggleArtisan.setText("Disable");
            }
            if (F2P == true) {
                buttonToggleSupport.setText("Enable");
                setAccount_F2P();
            }
        });

        buttonToggleGathering = new JButton();
        buttonToggleGathering.setBounds(52, 131, 120, 20);
        buttonToggleGathering.setText("Disable");
        buttonToggleGathering.addActionListener(e -> {
            if (goalMining.isEnabled() && goalMining != null ) {
                setAccount_Gathering(false);
                buttonToggleGathering.setText("Enable");
            } else {
                setAccount_Gathering(true);
                buttonToggleGathering.setText("Disable");
            }
            if (F2P == true) {
                buttonToggleSupport.setText("Enable");
                setAccount_F2P();
            }
        });

        buttonMaxAccount = new JButton();
        buttonMaxAccount.setBounds(10, 77, 150, 30);
        buttonMaxAccount.setText("Max Skills");
        buttonMaxAccount.addActionListener(e -> {
                buttonToggleCombat.setText("Disable");
                buttonToggleArtisan.setText("Disable");
                buttonToggleGathering.setText("Disable");
                buttonToggleSupport.setText("Disable");
                setAccount_MAXED();
                if (F2P == true) {
                    buttonToggleSupport.setText("Enable");
                    setAccount_F2P();
                }
        });

        buttonResetSkills = new JButton();
        buttonResetSkills.setBounds(10, 114, 150, 30);
        buttonResetSkills.setText("Reset Skills");
        buttonResetSkills.addActionListener(e -> {
                buttonToggleCombat.setText("Disable");
                buttonToggleArtisan.setText("Disable");
                buttonToggleGathering.setText("Disable");
                buttonToggleSupport.setText("Disable");
                setAccount_ALL_UNLOCKED();
                if (F2P == true) {
                    buttonToggleSupport.setText("Enable");
                    setAccount_F2P();
                }
            });

        panelSkillingGoalsCombat.add(buttonToggleCombat);
        panelSkillingGoalsArtisan.add(buttonToggleArtisan);
        panelSkillingGoalsSupport.add(buttonToggleSupport);
        panelSkillingGoalsGathering.add(buttonToggleGathering);
    }

    public static boolean enableRandomSkilling() {
        return checkBoxEnableRandomSkilling.isSelected();
    }

    public static void setAccount_F2P() {
        goalFarming.setEnabled(false);
        goalFarming.setValue(1);
        goalHunter.setEnabled(false);
        goalHunter.setValue(1);

        goalHerblore.setEnabled(false);
        goalHerblore.setValue(1);
        goalFletching.setEnabled(false);
        goalFletching.setValue(1);
        goalConstruction.setEnabled(false);
        goalConstruction.setValue(1);

        goalAgility.setEnabled(false);
        goalAgility.setValue(1);
        goalThieving.setEnabled(false);
        goalThieving.setValue(1);
        goalSlayer.setEnabled(false);
        goalSlayer.setValue(1);
    }

    public static void setAccount_MAXED() {
        goalAttack.setEnabled(true);
        goalAttack.setValue(99);
        goalStrength.setEnabled(true);
        goalStrength.setValue(99);
        goalDefence.setEnabled(true);
        goalDefence.setValue(99);
        goalRanged.setEnabled(true);
        goalRanged.setValue(99);
        goalMagic.setEnabled(true);
        goalMagic.setValue(99);
        goalPrayer.setEnabled(true);
        goalPrayer.setValue(99);

        goalMining.setEnabled(true);
        goalMining.setValue(99);
        goalFishing.setEnabled(true);
        goalFishing.setValue(99);
        goalWoodcutting.setEnabled(true);
        goalWoodcutting.setValue(99);
        goalFarming.setEnabled(true);
        goalFarming.setValue(99);
        goalHunter.setEnabled(true);
        goalHunter.setValue(99);

        goalHerblore.setEnabled(true);
        goalHerblore.setValue(99);
        goalCrafting.setEnabled(true);
        goalCrafting.setValue(99);
        goalFletching.setEnabled(true);
        goalFletching.setValue(99);
        goalSmithing.setEnabled(true);
        goalSmithing.setValue(99);
        goalCooking.setEnabled(true);
        goalCooking.setValue(99);
        goalFiremaking.setEnabled(true);
        goalFiremaking.setValue(99);
        goalRunecrafting.setEnabled(true);
        goalRunecrafting.setValue(99);
        goalConstruction.setEnabled(true);
        goalConstruction.setValue(99);

        goalAgility.setEnabled(true);
        goalAgility.setValue(99);
        goalThieving.setEnabled(true);
        goalThieving.setValue(99);
        goalSlayer.setEnabled(true);
        goalSlayer.setValue(99);
    }

    public static void setAccount_ALL_UNLOCKED() {
        goalAttack.setEnabled(true);
        goalAttack.setValue(1);
        goalStrength.setEnabled(true);
        goalStrength.setValue(1);
        goalDefence.setEnabled(true);
        goalDefence.setValue(1);
        goalRanged.setEnabled(true);
        goalRanged.setValue(1);
        goalMagic.setEnabled(true);
        goalMagic.setValue(1);
        goalPrayer.setEnabled(true);
        goalPrayer.setValue(1);

        goalMining.setEnabled(true);
        goalMining.setValue(1);
        goalFishing.setEnabled(true);
        goalFishing.setValue(1);
        goalWoodcutting.setEnabled(true);
        goalWoodcutting.setValue(1);
        goalFarming.setEnabled(true);
        goalFarming.setValue(1);
        goalHunter.setEnabled(true);
        goalHunter.setValue(1);

        goalHerblore.setEnabled(true);
        goalHerblore.setValue(1);
        goalCrafting.setEnabled(true);
        goalCrafting.setValue(1);
        goalFletching.setEnabled(true);
        goalFletching.setValue(1);
        goalSmithing.setEnabled(true);
        goalSmithing.setValue(1);
        goalCooking.setEnabled(true);
        goalCooking.setValue(1);
        goalFiremaking.setEnabled(true);
        goalFiremaking.setValue(1);
        goalRunecrafting.setEnabled(true);
        goalRunecrafting.setValue(1);
        goalConstruction.setEnabled(true);
        goalConstruction.setValue(1);

        goalAgility.setEnabled(true);
        goalAgility.setValue(1);
        goalThieving.setEnabled(true);
        goalThieving.setValue(1);
        goalSlayer.setEnabled(true);
        goalSlayer.setValue(1);
    }

}
