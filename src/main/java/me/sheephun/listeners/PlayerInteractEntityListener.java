package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class PlayerInteractEntityListener implements Listener {
    @EventHandler
    public void playerInteractEntityListener(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        Material inHand = player.getInventory().getItemInMainHand().getType();

        if (entity instanceof Sheep && inHand == Material.SHEARS) {
            missionTriggerEvent(player, entity.getType(), MissionType.SHEAR_ENTITY, 1, null);
        }

        if (entity instanceof Cow && inHand == Material.BUCKET) {
            missionTriggerEvent(player, entity.getType(), MissionType.MILK_COW, 1, null);
        }

        if (entity instanceof Animals && (inHand == Material.WHEAT || inHand == Material.CARROT)) {
            missionTriggerEvent(player, entity.getType(), MissionType.FEED_ENTITY, 1, null);
        }
    }

}
