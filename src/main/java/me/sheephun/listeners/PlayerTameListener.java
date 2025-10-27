package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class PlayerTameListener implements Listener {
    @EventHandler
    public void playerTameListener(EntityTameEvent e){
        if (!(e.getOwner() instanceof Player player)) return;
        EntityType type = e.getEntityType();
        missionTriggerEvent(player, type, MissionType.TAME_ENTITY, 1, null);
    }
}
