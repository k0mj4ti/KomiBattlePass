package me.sheephun.komiBattlePass.data;

import me.sheephun.komiBattlePass.managers.BattlePassManager;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private UUID uuid;
    private String username;
    private int season;
    private int xp;
    private boolean isPremium;
    private HashSet<String> tiersClaimed;
    private Map<String, MissionProgress> missions = new HashMap<>();

    public PlayerData(){}

    public PlayerData(UUID uuid){
        this.uuid = uuid;
        this.username = Bukkit.getPlayer(uuid).getName();
        this.season = BattlePassManager.getBattlePassInstance().getSeason();
        this.xp = 0;
        this.isPremium = false;
        this.tiersClaimed = new HashSet<>();
        this.missions = new HashMap<>();
    }

    public PlayerData(UUID uuid, int season, int xp, boolean isPremium,
                      HashSet<String> tiersClaimed, Map<String, MissionProgress> missions){
        this.uuid = uuid;
        this.username = Bukkit.getPlayer(uuid).getName();
        this.season = season;
        this.xp = xp;
        this.isPremium = isPremium;
        this.tiersClaimed = tiersClaimed;
        this.missions = missions;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getSeason() {
        return season;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public HashSet<String> getTiersClaimed() {
        return tiersClaimed;
    }

    public void setTiersClaimed(HashSet<String> tiersClaimed) {
        this.tiersClaimed = tiersClaimed;
    }

    public Map<String, MissionProgress> getMissions() {
        return missions;
    }

    public void setMissions(Map<String, MissionProgress> missions) {
        this.missions = missions;
    }

    public String getUsername() {
        return username;
    }
}
