package me.sheephun.komiBattlePass.data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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


    public boolean isPremium() {
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

    public ItemStack getIcon(boolean claimed, PlayerData playerData) {
        Material material = Material.PAPER;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        boolean hasPremium = playerData.isPremium();
        boolean hasEnoughXp = (playerData.getXp() / 1000) >= getLevel();


        // Set display name
        String displayName = (isPremium ? "§6" : "§a") + name + " (Level " + level + ")";
        if (isPremium) displayName += " §e⚡"; // Premium indicator
        meta.setDisplayName(displayName);

        // Build lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + description);
        lore.add("§7Reward: " + rewardSummary());
        lore.add(isPremium ? "§6Premium Reward" : "§aFree Reward");

        // Determine CustomModelData and additional lore for locked/unlocked
        int modelData;
        if (isPremium) {
            if (!hasPremium) {
                modelData = 1116; // Locked because player doesn't have premium
                lore.add("§cLocked: Premium required");
            } else if (!hasEnoughXp) {
                modelData = 1116; // Locked due to insufficient XP
                lore.add("§cLocked: Not enough XP");
            } else if (claimed) {
                modelData = 1115; // Premium claimed
                lore.add("§6✓ Claimed");
            } else {
                modelData = 1114; // Premium unlocked, not claimed
                lore.add("§eClick to Claim");
            }
        } else {
            if (!hasEnoughXp) {
                modelData = 1113; // Free locked due to insufficient XP
                lore.add("§cLocked: Not enough XP");
            } else if (claimed) {
                modelData = 1112; // Free claimed
                lore.add("§6✓ Claimed");
            } else {
                modelData = 1111; // Free unlocked, not claimed
                lore.add("§eClick to Claim");
            }
        }
        meta.setCustomModelData(modelData);
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