package net.automaters.api.entities;

public class PlayerCrashInfo {
    private int crashCount;
    private long lastCrashTime;
    private String name;
    public PlayerCrashInfo() {
        this.crashCount = 0;
        this.lastCrashTime = 0;
        this.name = null;
    }

    public void updateName(String crasher) {
        name = crasher;
    }
    public String getName() { return name; }
    public int getCrashCount() {
        return crashCount;
    }

    public void incrementCrashCount() {
        crashCount++;
    }

    public long getLastCrashTimeSeconds() {
        long currentTimeMillis = System.currentTimeMillis();
        return (currentTimeMillis - lastCrashTime) / 1000;
    }

    public long getLastCrashTime() {
        return lastCrashTime;
    }

    public void updateLastCrashTime() {
        lastCrashTime = System.currentTimeMillis();
    }

    public void resetCrashCount() {
        crashCount = 0;
    }
}
