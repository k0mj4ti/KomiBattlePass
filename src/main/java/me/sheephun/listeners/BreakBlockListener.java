package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class BreakBlockListener implements Listener {
    @EventHandler
    public void breakBlockListener(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Material brokenMaterial = e.getBlock().getType();
        missionTriggerEvent(player, brokenMaterial, MissionType.BREAK_BLOCK, 1, null);
    }


}
