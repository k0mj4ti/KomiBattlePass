package me.sheephun.komiBattlePass.util;

import me.sheephun.komiBattlePass.data.MissionProgress;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class IconUtil {


    public static ItemStack backButtonIcon(){
        return customItem(ChatColor.RED + "Back", new ArrayList<>());
    }
    public static ItemStack closeButtonIcon(){
        return customItem(ChatColor.RED + "Close", new ArrayList<>());
    }
    public static ItemStack nextPageIcon(){
        return customItem(ChatColor.YELLOW + "Next Page", new ArrayList<>());
    }
    public static ItemStack previousPageIcon(){
        return customItem(ChatColor.YELLOW + "Previous Page", new ArrayList<>());
    }
    public static ItemStack noTierIcon(){
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "No Tier");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPremiumIcon() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Unlock more rewards and bonuses!");
        lore.add(ChatColor.GRAY + "Get more gold, exclusive cosmetics and more.");
        lore.add("");
        lore.add(ChatColor.DARK_GREEN + "⚡ " + ChatColor.GREEN + "Upgrade your Battle Pass today!");
        lore.add(ChatColor.GRAY + "Visit: " + ChatColor.GREEN + ChatColor.UNDERLINE + "store.ghostempire.gg");
        lore.add("");
        lore.add(ChatColor.DARK_GREEN + "» " + ChatColor.WHITE + "Click to open the store!");

        return customItem(ChatColor.GREEN + "" + ChatColor.BOLD + "Get Premium", lore);
    }

    public static ItemStack getRewardsIcon() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "View and claim your unlocked rewards!");
        lore.add("");
        lore.add(ChatColor.GOLD + "🎁 " + ChatColor.YELLOW + "Collect tiers, cosmetics, gold and more!");
        lore.add("");
        lore.add(ChatColor.GOLD + "» " + ChatColor.WHITE + "Click to open Rewards Menu!");

        return customItem(ChatColor.YELLOW + "" + ChatColor.BOLD + "Rewards", lore);
    }

    public static ItemStack getMissionsIcon() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Complete missions to earn Battle Pass XP!");
        lore.add("");
        lore.add(ChatColor.YELLOW + "✅ " + ChatColor.GOLD + "Daily, Weekly and Never Ending tasks await!");
        lore.add("");
        lore.add(ChatColor.YELLOW + "» " + ChatColor.WHITE + "Click to open Missions Menu!");

        return customItem(ChatColor.GOLD + "" + ChatColor.BOLD + "Missions", lore);
    }

    public static ItemStack getPlayerStatsIcon(String playerName, boolean isPremium, float xp) {
        int level = (int) (xp / 1000);
        float progress = (xp % 1000) / 1000f;

        int totalBars = 10;
        int filledBars = Math.round(totalBars * progress);
        int emptyBars = totalBars - filledBars;

        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < filledBars; i++) bar.append("§a█");
        for (int i = 0; i < emptyBars; i++) bar.append("§7█");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Player: " + ChatColor.WHITE + playerName);
        lore.add(ChatColor.GRAY + "Premium: " + (isPremium ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
        lore.add(ChatColor.GRAY + "Level: " + ChatColor.YELLOW + level);
        lore.add(ChatColor.GRAY + "Progress: " + bar + ChatColor.YELLOW + String.format(" (%.0f%%)", progress * 100));
        lore.add("");
        lore.add(ChatColor.YELLOW + "🌟 Keep progressing to unlock more rewards!");

        return customItem(ChatColor.YELLOW + "" + ChatColor.BOLD + "Your Stats", lore);
    }


    public static ItemStack getDailyQuestsIcon() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Daily repeating quests to keep");
        lore.add(ChatColor.GRAY + "your progress steady and rewarding.");
        lore.add("");
        lore.add(ChatColor.DARK_GREEN + "☀ " + ChatColor.GREEN + "Log in daily to keep your streak alive!");
        lore.add("");
        lore.add(ChatColor.DARK_GREEN + "» " + ChatColor.WHITE + "Click to view Daily Quests!");

        return customItem(ChatColor.GREEN + "" + ChatColor.BOLD + "Daily Quests", lore);
    }

    public static ItemStack getWeeklyQuestsIcon() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Weekly recurring quests that refresh");
        lore.add(ChatColor.GRAY + "every Monday for consistent XP gains.");
        lore.add("");
        lore.add(ChatColor.GOLD + "🗓 " + ChatColor.YELLOW + "Resets every Monday!");
        lore.add("");
        lore.add(ChatColor.GOLD + "» " + ChatColor.WHITE + "Click to view Weekly Quests!");

        return customItem(ChatColor.YELLOW + "" + ChatColor.BOLD + "Weekly Quests", lore);
    }

    public static ItemStack getAllTimeQuestsIcon() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Permanent missions that can be");
        lore.add(ChatColor.GRAY + "completed once to mark your legacy.");
        lore.add("");
        lore.add(ChatColor.YELLOW + "🏆 " + ChatColor.GOLD + "Prove your dedication and earn exclusive rewards!");
        lore.add("");
        lore.add(ChatColor.YELLOW + "» " + ChatColor.WHITE + "Click to view All-Time Quests!");

        return customItem(ChatColor.GOLD + "" + ChatColor.BOLD + "All-Time Quests", lore);
    }



    public static ItemStack customItem(String displayName, List<String> lore){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1117);
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }



}
