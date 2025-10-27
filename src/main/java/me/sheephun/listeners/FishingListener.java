package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class FishingListener implements Listener {
    @EventHandler
    public void fishingListener(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;

        Player player = e.getPlayer();
        Entity caught = e.getCaught();
        if (caught == null) return;

        missionTriggerEvent(player, caught.getType(), MissionType.FISH, 1, null);
    }

}
