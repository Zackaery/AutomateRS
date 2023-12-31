package net.automaters.unused_files;

import net.automaters.tasks.Task;
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

import java.util.Comparator;
import java.util.Random;

import static net.automaters.api.entities.LocalPlayer.*;
import static net.automaters.api.entities.SkillCheck.*;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.walking.Walking.automateWalk;
import static net.automaters.script.Variables.*;
import static net.automaters.util.locations.Mining.*;
import static net.unethicalite.api.commons.Time.sleep;

@SuppressWarnings({"ConstantConditions","unused"})
public abstract class MiningBored extends Task {

    @Inject
    private Client client;

    public MiningBored() {
        // Just another mining script...
        super();
    }

    @Override
    public void onStart() {
        setStarted(true);
    }

    @Override
    protected void onLoop() {
        debug("Holy shit this is in the loop");

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
//        boolean armorcheck = false;
        boolean dropore = false;

        WorldPoint playerPosition = Players.getLocal().getWorldLocation();

        if (!Bank.isOpen() && Inventory.isFull() || !Bank.isOpen() && !Inventory.contains(Predicates.nameContains("pickaxe")) && !Equipment.contains(Predicates.nameContains("pickaxe"))
                || !Bank.isOpen() && !Inventory.contains(Predicates.nameContains("pickaxe")) && !Equipment.contains(Predicates.nameContains("pickaxe"))) {
            // do something
            sleep(250);
            readytomine = false;
//            sleep(250);
//            armorcheck = true;
            sleep(250);
            pickaxecheck = true;
            openBank();
        }

        if (Bank.isOpen() && Inventory.contains(Predicates.nameContains("ore"))
                || Bank.isOpen() && Inventory.isFull()
                || dropore && Bank.isOpen() && !Inventory.isFull() && Inventory.contains(Predicates.nameContains("ore"))
                || Bank.isOpen() && Inventory.isFull() && !Inventory.contains(Predicates.nameContains("pickaxe")) && !Equipment.contains(Predicates.nameContains("pickaxe"))) {
            debug("trying to deposit now?");
            Bank.depositInventory();
            random.nextInt(5);
            debug("Location chosen after banking: " + randomIndex);
        }

        if (Bank.isOpen() && (
                (Inventory.contains(Predicates.nameContains("ore")) && !Inventory.isFull())
                        || (Inventory.isFull() && Inventory.contains(Predicates.nameContains("pickaxe")) && !Equipment.contains(Predicates.nameContains("pickaxe")))
        )){
            sleep(500);
            debug("trying to deposit");
            Bank.depositInventory();
            random.nextInt(5);
            debug("Location chosen after banking: " + randomIndex);
            sleep(1500);
            pickaxecheck = true;
//            armorcheck = true;
        }

        if (Bank.isOpen() && Inventory.isEmpty() /*&& !armorcheck*/ && !pickaxecheck) {
//            debug("checking armor now.");
//            GearUpgrade gearUpgrade = new GearUpgrade();
//            gearUpgrade.upgradeGear();
//            sleep(250,1500);
//            armorcheck = false;
//            debug("Armor check complete. Now checking axes");
            PickaxeUpgrade PickaxeUpgrade = new PickaxeUpgrade();
            PickaxeUpgrade.executePickaxeUpgrade();
            sleep(1500);
            pickaxecheck = false;
            debug("Pickaxe check complete!");
            sleep(500);
            if (/*armorcheck &&*/ pickaxecheck) {
                Bank.close();
                readytomine = true;
            }
            random.nextInt(5);
            debug("Location after gear checks: " + randomIndex);
        }
        // Copper rocks start

        if (!localPlayer.isMoving() && getWoodcuttingLevel() > 1 && Equipment.contains(Predicates.nameContains("pickaxe")) && !Inventory.isFull() && !localPlayer.isInteracting()) {

            // start of copper locations

            if (randomIndex == 0 && !isInArea(DraynorVillage_MiningArea_I)) {
                debug("Walking 1");
                automateWalk(DraynorVillage_MiningArea_I);
                sleep(2000);
                readytomine = true;
            }

            // end of copper

            if (randomIndex == 1 && !isInArea(DraynorVillage_MiningArea_II)) {
                debug("Walking 1");
                automateWalk(DraynorVillage_MiningArea_II);
                sleep(2000);
                readytomine = true;
            }

            // end of copper

            if (randomIndex == 2 && !isInArea(DraynorVillage_MiningArea_III)) {
                debug("Walking 1");
                automateWalk(DraynorVillage_MiningArea_III);
                sleep(2000);
                readytomine = true;
            }

            // end of copper

            if (randomIndex == 3 && !isInArea(DraynorVillage_MiningArea_IV)) {
                debug("Walking 1");
                automateWalk(DraynorVillage_MiningArea_IV);
                sleep(2000);
                readytomine = true;
            }

            // end of copper

            if (randomIndex == 4 && !isInArea(DraynorVillage_MiningArea_V)) {
                debug("Walking 1");
                automateWalk(DraynorVillage_MiningArea_V);
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
            while (scriptStarted && !Inventory.isFull() && Equipment.contains(Predicates.nameContains("pickaxe"))) {
                sleep(1000);
                var rock = TileObjects
                        .getSurrounding(localPlayer.getWorldLocation(), 8, "Copper rocks")
                        .stream()
                        .filter(Reachable::isInteractable)
                        .min(Comparator.comparing(x -> x.distanceTo(localPlayer.getWorldLocation())))
                        .orElse(null);
                sleep(1000);

                if (!localPlayer.isAnimating() && !localPlayer.isInteracting() && !Inventory.isFull() && !localPlayer.isMoving() && rock != null) {
                    debug("Swing batta batta!");
                    rock.interact("Mine");
                    sleep(600, 1500);
                }
                if (rock == null) {
                    debug("Cannot find rocks, need to reposition");
                    sleep(150, 500);
                    return;

                }
            }

        }

        // end of copper
    }

    @Override
    public boolean taskFinished() {
        return false;
    }

    @Override
    public void generateSecondaryTask() {
        secondaryTask = "none";
    }
}
