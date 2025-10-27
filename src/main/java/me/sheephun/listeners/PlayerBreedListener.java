package me.sheephun.listeners;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class PlayerBreedListener implements Listener {
    @EventHandler
    public void playerBreedListener(EntityBreedEvent e){
        if (!(e.getBreeder() instanceof Player player)) return;
        EntityType type = e.getEntityType();
        missionTriggerEvent(player, type, MissionType.BREED_ANIMAL, 1, null);
    }
}
