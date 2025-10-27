package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class KillPlayerListener implements Listener {
    @EventHandler
    public void killPlayerListener(PlayerDeathEvent e){
        Player player = e.getEntity().getKiller();
        if (player == null) return;

        Entity entity = e.getEntity();

        missionTriggerEvent(player, entity.getType(), MissionType.KILL_PLAYER, 1, null);
    }
}
