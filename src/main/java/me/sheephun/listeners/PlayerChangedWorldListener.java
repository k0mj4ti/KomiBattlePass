package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class PlayerChangedWorldListener implements Listener {
    @EventHandler
    public void onEnterDimension(PlayerChangedWorldEvent e){
        Player player = e.getPlayer();
        World from = e.getFrom();
        World to = player.getWorld();

        missionTriggerEvent(player, to.getEnvironment(), MissionType.ENTER_DIMENSION, 1 ,null);
    }

}
