package me.sheephun.komiBattlePass.managers;

import me.sheephun.komiBattlePass.data.BattlePassTier;
import me.sheephun.komiBattlePass.data.Reward;
import me.sheephun.komiBattlePass.enums.RewardType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class BattlePassManager {
    private static BattlePassManager instance;

    private final int season;
    private final int maxLevel;
    private final int xpPerLevel;
    private final Map<Integer, BattlePassTier> freeTiers = new HashMap<>();
    private final Map<Integer, BattlePassTier> premiumTiers = new HashMap<>();

    public BattlePassManager(FileConfiguration config) {
        instance = this;

        this.season = config.getInt("season", 1);
        this.maxLevel = config.getInt("max-level", 50);
        this.xpPerLevel = config.getInt("xp-per-level", 1000);

        System.out.println("[KomiBattlePass] Loading BattlePass season " + season + ", max level: " + maxLevel);

        ConfigurationSection tiersSection = config.getConfigurationSection("tiers");
        if (tiersSection == null) {
            System.out.println("[KomiBattlePass] No 'tiers' section found!");
            return;
        }

        for (String key : tiersSection.getKeys(false)) {
            try {
                int level = Integer.parseInt(key);
                ConfigurationSection levelSection = tiersSection.getConfigurationSection(key);
                if (levelSection == null) continue;

                // --- FREE TIER ---
                if (levelSection.isConfigurationSection("free")) {
                    ConfigurationSection freeSec = levelSection.getConfigurationSection("free");
                    BattlePassTier tier = buildTier(level, false, freeSec);
                    freeTiers.put(level, tier);
                    System.out.println("[KomiBattlePass] Loaded FREE tier " + level + ": " + tier.getName());
                }

                // --- PREMIUM TIER ---
                if (levelSection.isConfigurationSection("premium")) {
                    ConfigurationSection premSec = levelSection.getConfigurationSection("premium");
                    BattlePassTier tier = buildTier(level, true, premSec);
                    premiumTiers.put(level, tier);
                    System.out.println("[KomiBattlePass] Loaded PREMIUM tier " + level + ": " + tier.getName());
                }

            } catch (Exception e) {
                System.out.println("[KomiBattlePass] Failed to load tier " + key + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("[KomiBattlePass] Loaded " + freeTiers.size() + " free tiers and " + premiumTiers.size() + " premium tiers.");
    }

    private BattlePassTier buildTier(int level, boolean premium, ConfigurationSection section) {
        if (section == null) return null;

        String name = section.getString("name", "Unnamed Tier");
        String desc = section.getString("description", "No description");
        Reward reward = loadReward(section.getConfigurationSection("reward"));

        return new BattlePassTier(level, premium, name, desc, reward);
    }

    private Reward loadReward(ConfigurationSection section) {
        if (section == null) return null;

        String type = section.getString("type", "").toLowerCase();
        RewardType rewardType;
        Object data = null;

        switch (type) {
            case "command" -> {
                rewardType = RewardType.COMMAND;
                data = section.getString("data");
            }
            case "cosmetic" -> {
                rewardType = RewardType.COSMETIC;
                data = section.getString("data");
            }
            case "gold" -> {
                rewardType = RewardType.GOLD;
                data = section.getInt("amount");
            }
            case "xp" -> {
                rewardType = RewardType.XP;
                data = section.getInt("amount");
            }
            case "multiple" -> {
                rewardType = RewardType.MULTIPLE;
                List<Reward> subRewards = new ArrayList<>();
                List<Map<?, ?>> items = section.getMapList("items");
                for (Map<?, ?> map : items) {
                    String t = ((String) map.get("type")).toUpperCase();
                    Object d = map.get("data") != null ? map.get("data") : map.get("amount");
                    subRewards.add(new Reward(RewardType.valueOf(t), d));
                }
                data = subRewards;
            }
            default -> rewardType = RewardType.GOLD;
        }

        return new Reward(rewardType, data);
    }

    public int getSeason() {
        return season;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getXpPerLevel() {
        return xpPerLevel;
    }

    public Map<Integer, BattlePassTier> getFreeTiers() {
        return freeTiers;
    }

    public Map<Integer, BattlePassTier> getPremiumTiers() {
        return premiumTiers;
    }

    public static BattlePassManager getBattlePassInstance() {
        return instance;
    }
}
