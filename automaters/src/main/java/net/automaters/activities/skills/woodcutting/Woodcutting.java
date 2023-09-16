package net.automaters.activities.skills.woodcutting;

import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.plugins.LoopedPlugin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Locale;

import static net.automaters.script.AutomateRS.*;
import static net.unethicalite.api.commons.Time.sleep;
import static net.unethicalite.api.items.Inventory.isFull;

public class Woodcutting extends LoopedPlugin {

    public WorldPoint startLocation = null;
    public boolean started = false;

    public static boolean moveto = false; // Remember these need to be static to save value.
    public static boolean dropalllogs = false;


    @Override
    public int loop() {

        var local = Players.getLocal();
        if ((local == null)|| !scriptStarted) {
            debug("Player does not exist.");
            return -1;
        }

        // Define the coordinates of the rectangular area
        int minX = 3176;
        int minY = 3207;
        int maxX = 3199;
        int maxY = 3240;

// Get the player's current position
        WorldPoint playerPosition = Players.getLocal().getWorldLocation();

// Check if the player is within the rectangular area
        boolean isPlayerWithinArea = playerPosition.getX() >= minX && playerPosition.getX() <= maxX &&
                playerPosition.getY() >= minY && playerPosition.getY() <= maxY;

// Set moveto to false if the player is not within the area
        if (!isPlayerWithinArea) {
            moveto = false;
        }


        if (!started) {
            startLocation = local.getWorldLocation();
            debug("Starting location = "+startLocation);
            started = true;
        }

        if (!moveto) {
            String filePath = "C:\\Users\\corey\\.openosrs\\data\\AutomateRS\\testlocation.txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line = reader.readLine(); // Read the first line
                if (line != null) {
                    debug("File read successfully");
                    debug("Need to move! Heading out!");
                    debug(line); // Log the first line

                    String[] numberStrings = line.split(",");
                    if (numberStrings.length == 3) {
                        try {
                            int x = Integer.parseInt(numberStrings[0].trim());
                            int y = Integer.parseInt(numberStrings[1].trim());
                            int z = Integer.parseInt(numberStrings[2].trim());

                            debug("X: " + String.valueOf(x));
                            debug("y: " + String.valueOf(y));
                            debug("z: " + String.valueOf(z));

                            // Loop until the player reaches the destination
                            while (local.getWorldLocation().getX() != x || local.getWorldLocation().getY() != y) {
                                // Call the Movement.walkTo() method repeatedly until the destination is reached
                                Movement.walkTo(x, y, z);
                                // delay added so not spam clicking to move.
                                // Movements are needing to be looped because it takes 1 step per cycle of the loop.
                                // So if we don't loop the movement internally the whole script will cycle over and over,
                                // to get to it's location before ever reaching it. Thus, we loop internally for movement.
                                // ** Note **
                                // When doing this we need to implement failsafes, health safespotting, etc etc.
                                sleep(1000,3000);// sleeps for 1 sec min to max of 3 sec
                            }

                        } catch (NumberFormatException e) {
                            // Handle the case where the string cannot be parsed as an integer
                            debug("Error parsing coordinates: " + e.getMessage());
                        }
                    } else {
                        // Handle the case where the first line does not have 3 comma-separated values
                        debug("Invalid first line format.");
                    }
                } else {
                    // Handle the case where the file is empty
                    debug("File is empty.");
                }
            } catch (IOException e) {
                debug("Error reading the file: " + e.getMessage());
            }
            debug("Arrived!");
            moveto = true;
        }

        var tree = TileObjects
                .getSurrounding(startLocation, 8, "Tree")
                .stream()
                .min(Comparator.comparing(x -> x.distanceTo(local.getWorldLocation())))
                .orElse(null);

        var logs = Inventory.getFirst(x -> x.getName().toLowerCase(Locale.ROOT).contains("logs"));

        if (!dropalllogs && moveto && logs != null && !local.isAnimating() && isFull()) {
            dropalllogs = true;
            debug("Need to drop all logs!");
            return 500;
        }

        if (dropalllogs) {
            debug("reading here");
            while (Inventory.contains("Logs")) {
                Inventory.getFirst("Logs").interact("Drop");
                debug("Dropping log");
                sleep(300);
            }
            dropalllogs = false;
        }

        if (moveto && local.isMoving() || local.isAnimating())
        {
            debug("Player is animating.");
            return 333;
        }

        if (moveto && tree == null)
        {
            debug("Could not find any trees");
            return 1000;
        }

        if (moveto)
            tree.interact("Chop down");
            debug("Chopping tree.");
            return 1000;
        }
}
