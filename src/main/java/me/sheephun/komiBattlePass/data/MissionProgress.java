package me.sheephun.komiBattlePass.data;

import me.sheephun.komiBattlePass.enums.MissionCategory;
import me.sheephun.komiBattlePass.enums.MissionType;

public class MissionProgress {
    private String id;
    private int progress;
    private boolean claimed;
    private long lastUpdated;
    private MissionType type;

    public MissionProgress() {
    }

    public MissionProgress(String id, int progress, boolean claimed, long lastUpdated, MissionType type){
        this.id = id;
        this.progress = progress;
        this.claimed = claimed;
        this.lastUpdated = lastUpdated;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
    public void addProgress(int progress) {
        this.progress = this.progress + progress;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public MissionType getType() {
        return type;
    }

    public void setType(MissionType type) {
        this.type = type;
    }
}
