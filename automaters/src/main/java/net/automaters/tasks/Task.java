package net.automaters.tasks;

import net.automaters.script.AutomateRS;

import static net.automaters.tasks.TaskManager.*;

public abstract class Task {

    private boolean hasContext;

    public Task() {
        hasContext = true;
    }

    public boolean hasContext() {
        return hasContext;
    }

    public abstract void run() throws InterruptedException;
    public abstract void onStart() throws InterruptedException;
    public abstract void onLoop() throws InterruptedException;
    public abstract boolean taskFinished();
    public void onEnd() {
        AutomateRS.debug("Completed Task: "+currentTask+"\n\n");
        currentTask = null;
        taskSelected = false;
        taskStarted = false;
    }

}