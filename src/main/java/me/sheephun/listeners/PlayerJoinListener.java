package me.sheephun.listeners;

import me.sheephun.komiBattlePass.data.PlayerData;
import me.sheephun.komiBattlePass.data.ResetTimer;
import me.sheephun.komiBattlePass.storage.Database;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.allPlayers;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void playerJoinListener(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        PlayerData data = Database.getUser(uuid);

        if (data == null) {
            data = new PlayerData(uuid);
            Database.saveUser(data);
            System.out.println("[KomiBattlePass] Created new PlayerData for " + e.getPlayer().getName());
        }

        // --- ResetTimer debug section ---
        ResetTimer timer = Database.getResetTimer();

        if (timer == null) {
            System.out.println("[KomiBattlePass][DEBUG] ResetTimer is NULL for player " + e.getPlayer().getName());
            return;
        }

        System.out.println("[KomiBattlePass][DEBUG] ResetTimer fetched for " + e.getPlayer().getName());
        System.out.println("[KomiBattlePass][DEBUG] LastDaily: " + timer.getLastDaily() +
                " (" + (System.currentTimeMillis() - timer.getLastDaily()) + "ms ago)");
        System.out.println("[KomiBattlePass][DEBUG] LastWeekly: " + timer.getLastWeekly() +
                " (" + (System.currentTimeMillis() - timer.getLastWeekly()) + "ms ago)");

        // --- Apply reset logic once ---
        resetPlayerMissionsIfNeeded(data, timer);

        allPlayers.putIfAbsent(uuid, data);
    }

    public static void resetPlayerMissionsIfNeeded(PlayerData data, ResetTimer timer) {
        long lastDailyReset = timer.getLastDaily();
        long lastWeeklyReset = timer.getLastWeekly();

        if (data.getMissions() == null || data.getMissions().isEmpty()) {
            System.out.println("[KomiBattlePass][DEBUG] No missions found for " + data.getUuid());
            return;
        }

        data.getMissions().values().forEach(m -> {
            if (m.getType() == null) {
                System.out.println("[KomiBattlePass][DEBUG] Skipping null mission type for " + data.getUuid());
                return;
            }

            boolean reset = false;

            if (m.getType().name().startsWith("DAILY") && m.getLastUpdated() < lastDailyReset) {
                reset = true;
            } else if (m.getType().name().startsWith("WEEKLY") && m.getLastUpdated() < lastWeeklyReset) {
                reset = true;
            }

            if (reset) {
                System.out.println("[KomiBattlePass][DEBUG] Resetting mission " + m.getType() +
                        " for player " + data.getUuid());
                m.setProgress(0);
                m.setClaimed(false);
                m.setLastUpdated(System.currentTimeMillis());
            }
        });
    }
}
