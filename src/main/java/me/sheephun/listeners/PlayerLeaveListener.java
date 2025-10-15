package me.sheephun.listeners;

import me.sheephun.komiBattlePass.data.PlayerData;
import me.sheephun.komiBattlePass.storage.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.getPlayerData;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    public void PlayerLeaveListener(PlayerQuitEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        PlayerData playerData = getPlayerData(uuid);
        Database.saveUser(playerData);
    }
}
