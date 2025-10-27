package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class PlayerEnchantItem implements Listener {
    @EventHandler
    public void playerEnchantItem(EnchantItemEvent e){
        Player player = e.getEnchanter();
        ItemStack item = e.getItem();
        missionTriggerEvent(player, item.getType(), MissionType.ENCHANT_ITEM, 1, null);
    }
}
