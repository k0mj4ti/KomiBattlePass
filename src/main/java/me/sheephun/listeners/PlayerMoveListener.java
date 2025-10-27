package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void playerMoveListener(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        if (from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ()) return; // only block movement

        Player player = e.getPlayer();

        if (player.isFlying()) return;

        if (player.isSprinting()) {
            missionTriggerEvent(player, "Sprint", MissionType.SPRINT_DISTANCE, 1, null);
        } else if (player.isSwimming()) {
            //Még mindig bussos
            missionTriggerEvent(player, "Swim", MissionType.SWIM_DISTANCE, 1, null);
        } else {
            missionTriggerEvent(player, "Walk", MissionType.WALK_DISTANCE, 1, null);
        }
    }


}
