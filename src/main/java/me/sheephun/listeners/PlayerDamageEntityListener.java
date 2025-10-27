package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class PlayerDamageEntityListener implements Listener {
    @EventHandler
    public void playerDamageEntiyListener(EntityDamageByEntityEvent e){
        Entity entity = e.getDamager();
        if (entity instanceof Player player){
            Entity damagedEntity = e.getEntity();
            int roundedDamage = (int) Math.round(e.getDamage());
            missionTriggerEvent(player, damagedEntity.getType(), MissionType.DEAL_DAMAGE, roundedDamage, null);
        }
    }
}
