package net.automaters.api.entities;

public class PlayerCrashInfo {
    private int crashCount;
    private long lastCrashTime;

    public PlayerCrashInfo() {
        this.crashCount = 0;
        this.lastCrashTime = 0;
    }

    public int getCrashCount() {
        return crashCount;
    }

    public void incrementCrashCount() {
        crashCount++;
    }

    public long getLastCrashTimeSeconds() {
        long currentTimeMillis = System.currentTimeMillis();
        long lastCrashTimeSeconds = (currentTimeMillis - lastCrashTime) / 1000; // Convert to seconds
        return lastCrashTimeSeconds;
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
