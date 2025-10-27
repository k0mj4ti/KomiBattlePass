package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class PlayerConsumeListener implements Listener {
    @EventHandler
    public void playerConsomeListener(PlayerItemConsumeEvent e){
        Player player = e.getPlayer();
        Material item = e.getItem().getType();
        missionTriggerEvent(player, item, MissionType.CONSUME_ITEM, 1, null);
    }
}
