package me.sheephun.komiBattlePass.util;

import me.sheephun.komiBattlePass.data.MissionProgress;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class IconUtil {

    public static ItemStack battlePassIcon() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aBattlePass");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack missionsIcon() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eMissions");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack freeIcon() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aFree Tiers");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack premiumIcon() {
        ItemStack item = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Premium Tiers");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack barrier() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cNo Tier");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack nextPage() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aNext Page");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack previousPage() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aPrevious Page");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack backButton() {
        ItemStack item = new ItemStack(Material.OAK_DOOR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cBack");
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack barrierNoTier() {
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta meta = barrier.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§cNo Tier"); // Red “No Tier” label
            barrier.setItemMeta(meta);
        }
        return barrier;
    }



}
