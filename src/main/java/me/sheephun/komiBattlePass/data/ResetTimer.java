package me.sheephun.komiBattlePass.data;

import org.bson.codecs.pojo.annotations.BsonId;

public class ResetTimer {
    @BsonId
    private String id;

    private long lastDaily;
    private long lastWeekly;

    public ResetTimer() {} // MongoDB needs this

    public ResetTimer(String id, long lastDaily, long lastWeekly) {
        this.id = id;
        this.lastDaily = lastDaily;
        this.lastWeekly = lastWeekly;
    }

    public String getId() {
        return id;
    }

    public long getLastDaily() {
        return lastDaily;
    }

    public void setLastDaily(long lastDaily) {
        this.lastDaily = lastDaily;
    }

    public long getLastWeekly() {
        return lastWeekly;
    }

    public void setLastWeekly(long lastWeekly) {
        this.lastWeekly = lastWeekly;
    }
}
