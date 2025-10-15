package me.sheephun.listeners;

import me.sheephun.komiBattlePass.data.PlayerData;
import me.sheephun.komiBattlePass.data.ResetTimer;
import me.sheephun.komiBattlePass.storage.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.allPlayers;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void playerJoinListener(PlayerJoinEvent e){
        UUID uuid = e.getPlayer().getUniqueId();

        PlayerData data = Database.getUser(uuid);
        if (data == null) {
            data = new PlayerData(uuid);
            Database.saveUser(data);
            System.out.println("[KomiBattlePass] Created new PlayerData for " + e.getPlayer().getName());
        }

        ResetTimer timer = Database.getResetTimer();
        if (timer != null) {
            resetPlayerMissionsIfNeeded(data, timer);
        } else {
            System.out.println("[KomiBattlePass] Warning: ResetTimer was null during join for " + e.getPlayer().getName());
        }
        resetPlayerMissionsIfNeeded(data, timer);
        allPlayers.putIfAbsent(uuid, data);
    }

    public static void resetPlayerMissionsIfNeeded(PlayerData data, ResetTimer timer) {
        long lastDailyReset = timer.getLastDaily();
        long lastWeeklyReset = timer.getLastWeekly();

        data.getMissions().values().forEach(m -> {
            if(m.getType() != null) {
                if (m.getType().name().startsWith("DAILY") && m.getLastUpdated() < lastDailyReset) {
                    m.setProgress(0);
                    m.setClaimed(false);
                    m.setLastUpdated(System.currentTimeMillis());
                }
                if (m.getType().name().startsWith("WEEKLY") && m.getLastUpdated() < lastWeeklyReset) {
                    m.setProgress(0);
                    m.setClaimed(false);
                    m.setLastUpdated(System.currentTimeMillis());
                }
            }
        });
    }

}
