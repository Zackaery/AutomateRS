package net.automaters.activities.skills.mining;

import net.automaters.activities.skills.woodcutting.AxeUpgrade;
import net.automaters.activities.skills.woodcutting.GearUpgrade;
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
import net.automaters.util.locations.mining_rectangularareas;

import java.util.Comparator;
import java.util.Random;

import static net.automaters.api.entities.LocalPlayer.openBank;
import static net.automaters.api.entities.SkillCheck.*;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.script.AutomateRS.scriptStarted;
import static net.automaters.util.locations.woodcutting_rectangularareas.*;
import static net.unethicalite.api.commons.Time.sleep;

@SuppressWarnings({"ConstantConditions","unused"})
public class MiningBored extends LoopedPlugin {

    @Inject
    private Client client;

    // Just another mining script...

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

        // copper
        Random random = new Random();
        int randomIndex = random.nextInt(5);

        // copper = 0, tin = 1
        Random coppertin = new Random();
        int coppertinIndex = coppertin.nextInt(2);

        debug("I'm thinking... " + randomIndex);
        sleep(1000, 2500);

        boolean readytomine = false;
        boolean gearcheck = false;
        boolean pickaxecheck = false;
        boolean armorcheck = false;
        boolean dropore = false;

        WorldPoint playerPosition = Players.getLocal().getWorldLocation();

        if (!Bank.isOpen() && Inventory.isFull() || !Bank.isOpen() && !Inventory.contains(Predicates.nameContains("pickaxe")) && !Equipment.contains(Predicates.nameContains("pickaxe"))
                || !Bank.isOpen() && !Inventory.isFull() && !Equipment.contains(Predicates.nameContains("pickaxe")) && Inventory.contains(Predicates.nameContains("pickaxe"))
                || !Bank.isOpen() && !Inventory.contains(Predicates.nameContains("pickaxe")) && !Equipment.contains(Predicates.nameContains("pickaxe"))) {
            // do something
            sleep(250);
            readytomine = false;
            sleep(250);
            armorcheck = true;
            sleep(250);
            pickaxecheck = true;
            openBank();
        }

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("pickaxe")) && Equipment.contains(Predicates.nameContains("pickaxe"))) {
            Bank.depositInventory();
            Bank.depositEquipment();
            random.nextInt(5);
            debug("Location chosen after banking: " + randomIndex);
        }

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("ore"))
                || Bank.isOpen() && Inventory.isFull()
                || dropore && Bank.isOpen() && !Inventory.isFull() && Inventory.contains(Predicates.nameContains("Logs"))
                || Bank.isOpen() && Inventory.isFull() && !Inventory.contains(Predicates.nameContains("pickaxe")) && !Equipment.contains(Predicates.nameContains("pickaxe"))
                || Bank.isOpen() && Inventory.isFull() && !Inventory.contains(Predicates.nameContains("pickaxe")) && !Equipment.contains(Predicates.nameContains("pickaxe"))) {
            debug("trying to deposit now?");
            Bank.depositInventory();
            random.nextInt(5);
            debug("Location chosen after banking: " + randomIndex);
        }

        if (Bank.isOpen() && (
                (Inventory.contains(Predicates.nameContains("ore")) && !Inventory.isFull())
                        || (!Inventory.isFull() && Inventory.contains(Predicates.nameContains("pickaxe")) && !Equipment.contains(Predicates.nameContains("pickaxe")))
                        || (Inventory.isFull() && Inventory.contains(Predicates.nameContains("pickaxe")) && !Equipment.contains(Predicates.nameContains("pickaxe")))
        )){
            sleep(500);
            debug("trying to deposit");
            Bank.depositInventory();
            random.nextInt(5);
            debug("Location chosen after banking: " + randomIndex);
            sleep(1500);
            pickaxecheck = true;
            armorcheck = true;
        }

        if (Bank.isOpen() && Inventory.isEmpty() && !armorcheck && !pickaxecheck) {
            debug("checking armor now.");
            GearUpgrade gearUpgrade = new GearUpgrade();
            gearUpgrade.upgradeGear();
            sleep(250,1500);
            armorcheck = false;
            debug("Armor check complete. Now checking axes");
            AxeUpgrade axeUpgrade = new AxeUpgrade();
            axeUpgrade.executeAxeUpgrade();
            sleep(1500);
            pickaxecheck = false;
            debug("Pickaxe check complete!");
            sleep(500);
            if (armorcheck && pickaxecheck) {
                Bank.close();
                readytomine = true;
            }
            random.nextInt(5);
            debug("Location after gear checks: " + randomIndex);
        }
        // woodcutting regular tree start

        if (!local.isMoving() && getWoodcuttingLevel() <= 15 && Equipment.contains(Predicates.nameContains("pickaxe")) && !Inventory.isFull() && !local.isInteracting()) {

            // start of copper locations

            if (randomIndex == 0 && !playerPosition.isInArea(Falador_Tree_TreeArea_I_Area.toWorldArea())) {
                debug("Walking 1");
                automateWalk(Falador_Tree_TreeArea_I_Area.toWorldArea());
                sleep(2000);
                readytomine = true;
            }

            // end of copper

            if (randomIndex == 1 && !playerPosition.isInArea(Falador_Tree_TreeArea_II_Area.toWorldArea())) {
                debug("Walking 1");
                automateWalk(Falador_Tree_TreeArea_II_Area.toWorldArea());
                sleep(2000);
                readytomine = true;
            }

            // end of copper

            if (randomIndex == 2 && !playerPosition.isInArea(Falador_Tree_TreeArea_III_Area.toWorldArea())) {
                debug("Walking 1");
                automateWalk(Falador_Tree_TreeArea_III_Area.toWorldArea());
                sleep(2000);
                readytomine = true;
            }

            // end of copper

            if (randomIndex == 3 && !playerPosition.isInArea(Falador_Tree_TreeArea_IV_Area.toWorldArea())) {
                debug("Walking 1");
                automateWalk(Falador_Tree_TreeArea_IV_Area.toWorldArea());
                sleep(2000);
                readytomine = true;
            }

            // end of copper

            if (randomIndex == 4 && !playerPosition.isInArea(Falador_Tree_TreeArea_V_Area.toWorldArea())) {
                debug("Walking 1");
                automateWalk(Falador_Tree_TreeArea_V_Area.toWorldArea());
                sleep(2000);
                readytomine = true;
            }
            // end of copper locations
        }

        // copper mine

        if (randomIndex == 0 && readytomine
                || randomIndex == 1 && readytomine
                || randomIndex == 2 && readytomine
                || randomIndex == 3 && readytomine
                || randomIndex == 4 && readytomine) {
            debug("inside index");
            sleep(1000);
            while (!Inventory.isFull() && Equipment.contains(Predicates.nameContains("pickaxe"))) {
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

        // end of regular tree

        // end willow trees
        return 0;
    }

}
