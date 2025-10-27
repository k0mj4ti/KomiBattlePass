package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class PlaceBlockListener implements Listener {
    @EventHandler
    public void placeBlockEvent(BlockPlaceEvent e){
        Player player = e.getPlayer();
        Material placedMaterial = e.getBlock().getType();
        missionTriggerEvent(player, placedMaterial, MissionType.PLACE_BLOCK, 1, null);
    }
}
