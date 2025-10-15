package me.sheephun.komiBattlePass.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BattlePassTier {
    private final int level;
    private final boolean isPremium;
    private String name;
    private String description;
    private Reward reward;

    public BattlePassTier(int level, boolean isPremium, String name, String description, Reward reward){
        this.level = level;
        this.isPremium = isPremium;
        this.name = name;
        this.description = description;
        this.reward = reward;
    }

    public int getLevel() {
        return level;
    }

    // Keep for backward compatibility
    public int getId() {
        return level;
    }

    public boolean isPremium() {
        return isPremium;
    }

    // Keep for backward compatibility
    public boolean isPremiumOnly() {
        return isPremium;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public ItemStack getIcon(boolean claimed) {
        Material material = isPremium ? Material.DIAMOND : Material.EMERALD;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        String displayName = (isPremium ? "§6" : "§a") + name + " (Level " + level + ")";
        if (isPremium) {
            displayName += " §e⚡"; // Premium indicator
        }

        meta.setDisplayName(displayName);
        List<String> lore = new ArrayList<>();
        lore.add(description);
        lore.add("§7Reward: " + rewardSummary());
        lore.add(isPremium ? "§6Premium Reward" : "§aFree Reward");
        lore.add(claimed ? "§6✓ Claimed" : "§eClick to Claim");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private String rewardSummary() {
        if (reward == null) return "None";
        switch (reward.getRewardType()) {
            case COMMAND -> {
                return "Command: " + reward.getData();
            }
            case GOLD, XP -> {
                return reward.getData() + " " + reward.getRewardType().name();
            }
            case COSMETIC -> {
                return "Cosmetic: " + reward.getData();
            }
            case MULTIPLE -> {
                StringBuilder sb = new StringBuilder();
                for (Object obj : (List<Reward>) reward.getData()) {
                    Reward r = (Reward) obj;
                    sb.append(r.getRewardType().name()).append(" ");
                }
                return sb.toString().trim();
            }
        }
        return "Unknown";
    }
}