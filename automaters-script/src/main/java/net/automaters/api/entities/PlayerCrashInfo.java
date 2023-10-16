package net.automaters.api.entities;

import lombok.Getter;

public class PlayerCrashInfo {
    @Getter
    private int crashCount;
    private long lastCrashTime;
    @Getter
    private String name;

    public PlayerCrashInfo() {
        this.crashCount = 0;
        this.lastCrashTime = 0;
        this.name = null;
    }

    public void updateName(String crasher) {
        name = crasher;
    }

    public void incrementCrashCount() {
        crashCount++;
    }

    public long getLastCrashTime() {
        long currentTimeMillis = System.currentTimeMillis();
        if (lastCrashTime != 0) {
            return ((currentTimeMillis - lastCrashTime) / 1000);
        } else {
            return (lastCrashTime);
        }
    }

    public void updateLastCrashTime() {
        lastCrashTime = System.currentTimeMillis();
    }

    public void resetCrashCount() {
        crashCount = 0;
    }
}
