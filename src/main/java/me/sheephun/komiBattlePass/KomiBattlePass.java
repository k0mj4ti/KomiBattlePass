package me.sheephun.komiBattlePass;

import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInvsPlugin;
import me.sheephun.komiBattlePass.Commands.Battlepass;
import me.sheephun.komiBattlePass.data.ResetTimer;
import me.sheephun.komiBattlePass.managers.BattlePassManager;
import me.sheephun.komiBattlePass.managers.MissionManager;
import me.sheephun.komiBattlePass.managers.PlayerDataManager;
import me.sheephun.komiBattlePass.storage.Database;
import me.sheephun.listeners.PlayerJoinListener;
import me.sheephun.listeners.PlayerLeaveListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;


public final class KomiBattlePass extends JavaPlugin {

    private static KomiBattlePass instance;
    private MissionManager missionManager;
    private BattlePassManager battlePassManager;
    private PlayerDataManager playerDataManager;
    private static InventoryManager inventoryManager;
    @Override
    public void onEnable() {
        new Database();

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        File battlepassFile = new File(getDataFolder(), "battlepass.yml");
        FileConfiguration battlepassConfig = YamlConfiguration.loadConfiguration(battlepassFile);
        battlePassManager = new BattlePassManager(battlepassConfig);

        File missionsFile = new File(getDataFolder(), "missions.yml");
        FileConfiguration missionsConfig = YamlConfiguration.loadConfiguration(missionsFile);
        missionManager = new MissionManager(missionsConfig);

        playerDataManager = new PlayerDataManager(missionManager, battlePassManager);

        saveResource("battlepass.yml", false);
        saveResource("missions.yml", false);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);

        getCommand("battlepass").setExecutor(new Battlepass());

        saveTimer();
        getServer().getConsoleSender().sendMessage("KomiBattlePass is now running.");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("KomiBattlePass is now stopped.");
    }
    public static KomiBattlePass getInstance(){
        return instance;
    }
    public static InventoryManager getInvManager() {
        return inventoryManager;
    }
    public void saveTimer() {
        long ticks = 5 * 60 * 20;

        new BukkitRunnable() {
            @Override
            public void run() {
                Database.saveAllUser();

                ResetTimer timer = Database.getResetTimer();
                long now = System.currentTimeMillis();

                boolean changed = false;

                if (now - timer.getLastDaily() >= 24 * 60 * 60 * 1000L) {
                    PlayerDataManager.resetDailyMissions();
                    timer.setLastDaily(now);
                    changed = true;
                }
                if (now - timer.getLastWeekly() >= 7L * 24 * 60 * 60 * 1000L) {
                    PlayerDataManager.resetWeeklyMissions();
                    timer.setLastWeekly(now);
                    changed = true;
                }

                if (changed) Database.saveResetTimer(timer);
            }
        }.runTaskTimer(this, 0, ticks);
    }

}
