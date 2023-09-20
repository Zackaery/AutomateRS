package net.automaters.activities.money_making.low_value;

import net.automaters.api.entities.LocalPlayer;
import net.automaters.tasks.Task;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.movement.pathfinder.model.BankLocation;
import static net.automaters.script.AutomateRS.debug;


public class PotOfFlour extends Task {

    public PotOfFlour() {
        super();
    }

    @Override
    public void onStart() {
        LocalPlayer.openBank();
        if (Bank.isOpen()) {

        }




//        if (Bank.isOpen()) {
//        taskDuration = 10000;
//            setStarted(true);
//        }
    }

    @Override
    public void onLoop() {
        debug("Task Duration: "+ taskDuration);

    }

    @Override
    public boolean taskFinished() {
        return false;
    }

}
