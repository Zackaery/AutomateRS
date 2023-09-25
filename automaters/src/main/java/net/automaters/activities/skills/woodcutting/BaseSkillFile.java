package net.automaters.activities.skills.woodcutting;

import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.mixins.Inject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.plugins.LoopedPlugin;

import java.util.Comparator;
import java.util.Random;

import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.entities.SkillCheck.getWoodcuttingLevel;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.util.locations.Woodcutting.*;
import static net.unethicalite.api.commons.Time.sleep;


@SuppressWarnings({"ConstantConditions","unused"})
public class BaseSkillFile extends LoopedPlugin {

    @Inject
    private Client client;

    // Base file

    @Override
    public int loop() {
        debug("Holy shit this is in the loop");

        var local = Players.getLocal();
        if ((local == null) || !scriptStarted) {
            debug("Player does not exist.");
            return -1;
        }

        // Choosing tree location
        debug("Setting location to initial thought...");
        Random random = new Random();
        int randomIndex = random.nextInt(5);

        debug("I'm thinking... " + randomIndex);
        sleep(1000, 2500);

        boolean readytochop = false;
        boolean gearcheck = false;
        boolean axecheck = false;
        boolean armorcheck = false;
        boolean droplogs = false;

        WorldPoint playerPosition = Players.getLocal().getWorldLocation();

        if (!Bank.isOpen() && Inventory.isFull() || !Bank.isOpen() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
                || !Bank.isOpen() && !Inventory.isFull() && !Equipment.contains(Predicates.nameContains("axe")) && Inventory.contains(Predicates.nameContains("axe"))
                || !Bank.isOpen() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))) {
            // do something
            sleep(250);
            readytochop = false;
            sleep(250);
            armorcheck = true;
            sleep(250);
            axecheck = true;
            openBank();
        }

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("axe")) && Equipment.contains(Predicates.nameContains("axe"))) {
            Bank.depositInventory();
            Bank.depositEquipment();
            random.nextInt(5);
            debug("Location chosen after banking: " + randomIndex);
        }

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("Logs"))
                || Bank.isOpen() && Inventory.isFull()
                || droplogs && Bank.isOpen() && !Inventory.isFull() && Inventory.contains(Predicates.nameContains("Logs"))
                || Bank.isOpen() && Inventory.isFull() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))
                || Bank.isOpen() && Inventory.isFull() && !Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe"))) {
            debug("trying to deposit now?");
            Bank.depositInventory();
            random.nextInt(5);
            debug("Location chosen after banking: " + randomIndex);
        }

        if (Bank.isOpen() && (
                (Inventory.contains(Predicates.nameContains("Logs")) && !Inventory.isFull())
                        || (!Inventory.isFull() && Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe")))
                        || (Inventory.isFull() && Inventory.contains(Predicates.nameContains("axe")) && !Equipment.contains(Predicates.nameContains("axe")))
        )){
            sleep(500);
            debug("trying to deposit");
            Bank.depositInventory();
            random.nextInt(5);
            debug("Location chosen after banking: " + randomIndex);
            sleep(1500);
            axecheck = true;
            armorcheck = true;
        }

        if (Bank.isOpen() && Inventory.isEmpty() && !armorcheck && !axecheck) {
            debug("checking armor now.");
            GearUpgrade gearUpgrade = new GearUpgrade();
            gearUpgrade.upgradeGear();
            sleep(250,1500);
            armorcheck = false;
            debug("Armor check complete. Now checking axes");
            AxeUpgrade axeUpgrade = new AxeUpgrade();
            axeUpgrade.executeAxeUpgrade();
            sleep(1500);
            axecheck = false;
            debug("Axe check complete!");
            sleep(500);
            if (armorcheck && axecheck) {
                Bank.close();
                readytochop = true;
            }
            random.nextInt(5);
            debug("Locatoin after gear checks: " + randomIndex);
        }
        // woodcutting regular tree start

        if (!local.isMoving() && getWoodcuttingLevel() <= 15 && Equipment.contains(Predicates.nameContains("axe")) && !Inventory.isFull() && !local.isInteracting()) {

            // start of tree locations

            if (randomIndex == 0 && !playerPosition.isInArea(Falador_Tree_TreeArea_I_Area.toWorldArea())) {
                debug("Walking 1");
                automateWalk(Falador_Tree_TreeArea_I_Area.toWorldArea());
                sleep(2000);
                readytochop = true;
            }
            // end of tree locations
        }

        // normal tree

        if (randomIndex == 0 && readytochop
                || randomIndex == 1 && readytochop
                || randomIndex == 2 && readytochop
                || randomIndex == 3 && readytochop
                || randomIndex == 4 && readytochop) {
            debug("inside index");
            sleep(1000);
            while (scriptStarted && !Inventory.isFull() && Equipment.contains(Predicates.nameContains("axe"))) {
                sleep(1000);
                var tree = TileObjects
                        .getSurrounding(local.getWorldLocation(), 8, 1276, 1278, 2091, 2092)
                        .stream()
                        .filter(Reachable::isInteractable)
                        .min(Comparator.comparing(x -> x.distanceTo(local.getWorldLocation())))
                        .orElse(null);
                sleep(1000);

                if (!local.isAnimating() && !local.isInteracting() && !Inventory.isFull() && !local.isMoving() && tree != null) {
                    debug("Chop suey!");
                    tree.interact("Chop down");
                    sleep(600, 1500);
                }
                if (tree == null) {
                    debug("Cannot find tree, need to reposition");
                    sleep(150, 500);
                    return -1;

                }
            }
        }

        // end willow trees
        return 0;
    }

}

