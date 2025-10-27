package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class KillEntityListener implements Listener {
    @EventHandler
    public void killEntityListener(EntityDeathEvent e){
        Player player = e.getEntity().getKiller();
        if (player == null) return;

        Entity entity = e.getEntity();

        missionTriggerEvent(player, entity.getType(), MissionType.KILL_ENTITY, 1, null);
    }
}
