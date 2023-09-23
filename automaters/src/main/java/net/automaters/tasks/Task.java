package net.automaters.tasks;

import static net.automaters.api.utils.Debug.debug;
import static net.automaters.tasks.TaskManager.*;

public abstract class Task {

    private static boolean started;

    public static long startTime;
    public static long taskDuration;

    public boolean taskStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }

    public Task() {
        if (!taskFinished()) {
            if (!taskStarted()) {
                onStart();
            } else {
                onLoop();
            }
        } else {
            onEnd();
        }
    }

    public abstract void onStart();
    public abstract void onLoop();
    public abstract boolean taskFinished();
    public void onEnd() {
        debug("Completed Task: "+currentTask+"\n\n");
        currentTask = null;
        taskSelected = false;
        taskStarted = false;
    }

}