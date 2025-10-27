package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class SmeltItemListener implements Listener {
    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent e) {
        Player player = e.getPlayer();
        Material smeltedItem = e.getItemType();
        int amount = e.getItemAmount();

        missionTriggerEvent(player, smeltedItem, MissionType.SMELT_ITEM, amount, null);
    }

}
