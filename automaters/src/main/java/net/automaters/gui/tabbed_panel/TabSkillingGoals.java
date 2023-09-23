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
            if ((F2P == false) && spinnerGoalAgility.isEnabled() && spinnerGoalAgility != null ) {
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
            if (spinnerGoalAttack.isEnabled() && spinnerGoalAttack != null) {
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
            if (spinnerGoalCrafting.isEnabled() && spinnerGoalCrafting != null ) {
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
            if (spinnerGoalMining.isEnabled() && spinnerGoalMining != null ) {
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
        spinnerGoalFarming.setEnabled(false);
        spinnerGoalFarming.setValue(1);
        spinnerGoalHunter.setEnabled(false);
        spinnerGoalHunter.setValue(1);

        spinnerGoalHerblore.setEnabled(false);
        spinnerGoalHerblore.setValue(1);
        spinnerGoalFletching.setEnabled(false);
        spinnerGoalFletching.setValue(1);
        spinnerGoalConstruction.setEnabled(false);
        spinnerGoalConstruction.setValue(1);

        spinnerGoalAgility.setEnabled(false);
        spinnerGoalAgility.setValue(1);
        spinnerGoalThieving.setEnabled(false);
        spinnerGoalThieving.setValue(1);
        spinnerGoalSlayer.setEnabled(false);
        spinnerGoalSlayer.setValue(1);
    }

    public static void setAccount_MAXED() {
        spinnerGoalAttack.setEnabled(true);
        spinnerGoalAttack.setValue(99);
        spinnerGoalStrength.setEnabled(true);
        spinnerGoalStrength.setValue(99);
        spinnerGoalDefence.setEnabled(true);
        spinnerGoalDefence.setValue(99);
        spinnerGoalRanged.setEnabled(true);
        spinnerGoalRanged.setValue(99);
        spinnerGoalMagic.setEnabled(true);
        spinnerGoalMagic.setValue(99);
        spinnerGoalPrayer.setEnabled(true);
        spinnerGoalPrayer.setValue(99);

        spinnerGoalMining.setEnabled(true);
        spinnerGoalMining.setValue(99);
        spinnerGoalFishing.setEnabled(true);
        spinnerGoalFishing.setValue(99);
        spinnerGoalWoodcutting.setEnabled(true);
        spinnerGoalWoodcutting.setValue(99);
        spinnerGoalFarming.setEnabled(true);
        spinnerGoalFarming.setValue(99);
        spinnerGoalHunter.setEnabled(true);
        spinnerGoalHunter.setValue(99);

        spinnerGoalHerblore.setEnabled(true);
        spinnerGoalHerblore.setValue(99);
        spinnerGoalCrafting.setEnabled(true);
        spinnerGoalCrafting.setValue(99);
        spinnerGoalFletching.setEnabled(true);
        spinnerGoalFletching.setValue(99);
        spinnerGoalSmithing.setEnabled(true);
        spinnerGoalSmithing.setValue(99);
        spinnerGoalCooking.setEnabled(true);
        spinnerGoalCooking.setValue(99);
        spinnerGoalFiremaking.setEnabled(true);
        spinnerGoalFiremaking.setValue(99);
        spinnerGoalRunecrafting.setEnabled(true);
        spinnerGoalRunecrafting.setValue(99);
        spinnerGoalConstruction.setEnabled(true);
        spinnerGoalConstruction.setValue(99);

        spinnerGoalAgility.setEnabled(true);
        spinnerGoalAgility.setValue(99);
        spinnerGoalThieving.setEnabled(true);
        spinnerGoalThieving.setValue(99);
        spinnerGoalSlayer.setEnabled(true);
        spinnerGoalSlayer.setValue(99);
    }

    public static void setAccount_ALL_UNLOCKED() {
        spinnerGoalAttack.setEnabled(true);
        spinnerGoalAttack.setValue(1);
        spinnerGoalStrength.setEnabled(true);
        spinnerGoalStrength.setValue(1);
        spinnerGoalDefence.setEnabled(true);
        spinnerGoalDefence.setValue(1);
        spinnerGoalRanged.setEnabled(true);
        spinnerGoalRanged.setValue(1);
        spinnerGoalMagic.setEnabled(true);
        spinnerGoalMagic.setValue(1);
        spinnerGoalPrayer.setEnabled(true);
        spinnerGoalPrayer.setValue(1);

        spinnerGoalMining.setEnabled(true);
        spinnerGoalMining.setValue(1);
        spinnerGoalFishing.setEnabled(true);
        spinnerGoalFishing.setValue(1);
        spinnerGoalWoodcutting.setEnabled(true);
        spinnerGoalWoodcutting.setValue(1);
        spinnerGoalFarming.setEnabled(true);
        spinnerGoalFarming.setValue(1);
        spinnerGoalHunter.setEnabled(true);
        spinnerGoalHunter.setValue(1);

        spinnerGoalHerblore.setEnabled(true);
        spinnerGoalHerblore.setValue(1);
        spinnerGoalCrafting.setEnabled(true);
        spinnerGoalCrafting.setValue(1);
        spinnerGoalFletching.setEnabled(true);
        spinnerGoalFletching.setValue(1);
        spinnerGoalSmithing.setEnabled(true);
        spinnerGoalSmithing.setValue(1);
        spinnerGoalCooking.setEnabled(true);
        spinnerGoalCooking.setValue(1);
        spinnerGoalFiremaking.setEnabled(true);
        spinnerGoalFiremaking.setValue(1);
        spinnerGoalRunecrafting.setEnabled(true);
        spinnerGoalRunecrafting.setValue(1);
        spinnerGoalConstruction.setEnabled(true);
        spinnerGoalConstruction.setValue(1);

        spinnerGoalAgility.setEnabled(true);
        spinnerGoalAgility.setValue(1);
        spinnerGoalThieving.setEnabled(true);
        spinnerGoalThieving.setValue(1);
        spinnerGoalSlayer.setEnabled(true);
        spinnerGoalSlayer.setValue(1);
    }

}
