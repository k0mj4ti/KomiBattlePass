package me.sheephun.komiBattlePass.data;

import me.sheephun.komiBattlePass.enums.MissionType;
import me.sheephun.komiBattlePass.enums.MissionCategory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class Mission {
    private final String id;
    private MissionType missionType;
    private String name;
    private String description;
    private int xp;
    private int target;
    private List<Object> data;
    private final String item;
    private MissionCategory missionCategory;

    public Mission(String id, MissionType missionType, String name, String description,
                   int xp, int target, List<Object> data, String item, MissionCategory missionCategory) {
        this.id = id;
        this.missionType = missionType;
        this.name = name;
        this.description = description;
        this.xp = xp;
        this.target = target;
        this.data = data;
        this.item = item;
        this.missionCategory = missionCategory;
    }

    public String getId() {
        return id;
    }

    public MissionType getMissionType() {
        return missionType;
    }

    public void setMissionType(MissionType missionType) {
        this.missionType = missionType;
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

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public MissionCategory getMissionCategory() {
        return missionCategory;
    }

    public void setMissionCategory(MissionCategory missionCategory) {
        this.missionCategory = missionCategory;
    }

    public ItemStack getIcon(MissionProgress progress) {
        Material material = Material.getMaterial(item.toUpperCase());
        if (material == null) material = Material.BOOK;

        ItemStack item;
        if (progress == null) {
            item = new ItemStack(material);
        } else {
            item = new ItemStack(progress.isClaimed() ? Material.WRITABLE_BOOK : material);
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + name);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + description);
        lore.add(ChatColor.GRAY + "XP: " + ChatColor.YELLOW + xp);

        int totalBars = 10;
        int currentProgress = progress != null ? progress.getProgress() : 0;
        float progressPercent = Math.min((float) currentProgress / target, 1f);

        int filledBars = Math.round(totalBars * progressPercent);
        int emptyBars = totalBars - filledBars;

        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < filledBars; i++) bar.append("§6█"); // orange filled
        for (int i = 0; i < emptyBars; i++) bar.append("§7█"); // gray empty

        lore.add(ChatColor.GRAY + "Progress: " + bar + ChatColor.YELLOW + String.format(" (%d/%d)", currentProgress, target));

        // Claimed or not claimed
        if (progress != null && progress.getProgress() >= target) {
            lore.add(progress.isClaimed() ? ChatColor.YELLOW + "" + ChatColor.BOLD + "Reward Claimed"
                    : ChatColor.YELLOW + "" + ChatColor.BOLD + "Reward Available");
        } else if (progress != null) {
            lore.add(ChatColor.GRAY + "Reward: Not Yet Available");
        } else {
            lore.add(ChatColor.GRAY + "Reward: Not Yet Available");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }


    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}

