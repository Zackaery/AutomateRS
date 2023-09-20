package net.automaters.activities.money_making.low_value;

import net.automaters.tasks.Task;
import net.unethicalite.api.plugins.LoopedPlugin;

import static net.automaters.script.AutomateRS.debug;


public class PotOfFlour extends Task {

    public PotOfFlour() {
        super();
    }

    @Override
    public void onStart() {
        taskDuration = 10000;
        setStarted(true);
    }

    @Override
    public void onLoop() {

    }

    @Override
    public boolean taskFinished() {
        return false;
    }

}
