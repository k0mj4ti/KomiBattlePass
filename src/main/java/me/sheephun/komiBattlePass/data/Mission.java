package me.sheephun.komiBattlePass.data;

import me.sheephun.komiBattlePass.enums.MissionType;
import me.sheephun.komiBattlePass.enums.MissionCategory;
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
    private MissionCategory missionCategory;
    private Reward reward;

    public Mission(String id, MissionType missionType, String name, String description,
                   int xp, int target, MissionCategory missionCategory, Reward reward) {
        this.id = id;
        this.missionType = missionType;
        this.name = name;
        this.description = description;
        this.xp = xp;
        this.target = target;
        this.missionCategory = missionCategory;
        this.reward = reward;
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

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public ItemStack getIcon(MissionProgress progress) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> lore = new ArrayList<>();
        lore.add("§e" + description);
        lore.add("§7XP: " + xp);
        if(progress != null) {
            lore.add("§aProgress: " + progress.getProgress() + "/" + target);
            lore.add(progress.isClaimed() ? "§6Claimed" : "§eNot Claimed");
        } else {
            lore.add("§eProgress: 0/" + target);
            lore.add("§cNot Claimed");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}

