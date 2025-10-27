package me.sheephun.komiBattlePass.managers;

import me.sheephun.ghostRoyale.GhostRoyale;
import me.sheephun.komiBattlePass.data.BattlePassTier;
import me.sheephun.komiBattlePass.data.Mission;
import me.sheephun.komiBattlePass.data.MissionProgress;
import me.sheephun.komiBattlePass.data.PlayerData;
import me.sheephun.komiBattlePass.data.Reward;
import me.sheephun.komiBattlePass.enums.MissionType;
import me.sheephun.komiBattlePass.enums.RewardType;
import me.sheephun.komiBattlePass.storage.Database;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.*;

import static me.sheephun.komiBattlePass.managers.MissionManager.getMissionById;
import static me.sheephun.komiBattlePass.managers.MissionManager.getMissionsByType;

public class PlayerDataManager {

    public static final HashMap<UUID, PlayerData> allPlayers = new HashMap<>();
    private static MissionManager missionManager;
    private static BattlePassManager battlePassManager;

    public PlayerDataManager(MissionManager missionManager, BattlePassManager battlePassManager) {
        PlayerDataManager.missionManager = missionManager;
        PlayerDataManager.battlePassManager = battlePassManager;

        // Load all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayer(player.getUniqueId());
        }
    }

    // Load player data from DB
    public static void loadPlayer(UUID uuid) {
        PlayerData playerData = Database.getUser(uuid);

        if (playerData == null) {
            Component kickMessage = Component.text("Invalid PlayerData, try again or contact administrator", NamedTextColor.RED);
            Bukkit.getPlayer(uuid).kick(kickMessage, PlayerKickEvent.Cause.PLUGIN);
            return;
        }

        allPlayers.put(uuid, playerData);
    }

    public static void savePlayer(UUID uuid) {
        PlayerData data = allPlayers.get(uuid);
        if (data != null) Database.saveUser(data);
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return allPlayers.get(uuid);
    }

    // ---------------- Missions ----------------

    public static MissionProgress getMissionProgress(UUID uuid, String missionId) {
        PlayerData data = allPlayers.get(uuid);
        if (data == null) return null;
        return data.getMissions().get(missionId);
    }

    public static void addProgress(Mission mission, MissionProgress missionProgress, Player player, Map<String, MissionProgress> missionProgressMap, PlayerData playerData, int amount){
        if (missionProgress.getProgress() >= mission.getTarget()){
            return;
        }
        int target = mission.getTarget();
        missionProgress.addProgress(amount);
        int progress = missionProgress.getProgress();
        missionProgress.setLastUpdated(System.currentTimeMillis());
        missionProgressMap.replace(mission.getId(), missionProgress);
        playerData.setMissions(missionProgressMap);
        if (progress >= target){
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            String message = ChatColor.GOLD + "" + ChatColor.BOLD + "MISSION COMPLETE "
                    + ChatColor.GRAY + "» "
                    + ChatColor.YELLOW + "You've successfully completed the "
                    + ChatColor.GRAY  + "["+ mission.getMissionCategory().name().replace("_", " ") + "]"
                    + ChatColor.YELLOW + " " + ChatColor.GOLD + "\"" + mission.getName() +  "\"" +  ChatColor.YELLOW + " quest\n"
                    + ChatColor.GRAY + "Collect your XP via the "
                    + ChatColor.YELLOW + "/battlepass "
                    + ChatColor.GRAY + "menu!";


            player.sendMessage(message);
        }
    }

    public static void claimMission(UUID uuid, String missionId) {
        PlayerData data = allPlayers.get(uuid);
        if (data == null) return;

        MissionProgress progress = data.getMissions().get(missionId);
        Mission mission = getMissionById(missionId);
        if (progress != null) {
            Player player = Bukkit.getPlayer(uuid);
            if (progress.getProgress() >= mission.getTarget()){
                if (progress.isClaimed()) return;
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1 ,1);
                progress.setClaimed(true);
                progress.setLastUpdated(System.currentTimeMillis());
                data.addXp(mission.getXp());
                player.sendMessage("§aClaimed mission: §e" + mission.getName() + " [+" + mission.getXp() + "XP]");
            }else{
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1 ,1);
            }
        }
    }

    public static void resetDailyMissions() {
        for (PlayerData data : allPlayers.values()) {
            data.getMissions().values().forEach(m -> {
                if (m.getType() != null && m.getType().name().startsWith("DAILY")) {
                    m.setProgress(0);
                    m.setClaimed(false);
                    m.setLastUpdated(System.currentTimeMillis());
                }
            });
        }
    }

    public static void resetWeeklyMissions() {
        for (PlayerData data : allPlayers.values()) {
            data.getMissions().values().forEach(m -> {
                if (m.getType() != null && m.getType().name().startsWith("WEEKLY")) {
                    m.setProgress(0);
                    m.setClaimed(false);
                    m.setLastUpdated(System.currentTimeMillis());
                }
            });
        }
    }

    // ---------------- BattlePass ----------------

    public static int getBattlePassLevel(UUID uuid) {
        PlayerData data = allPlayers.get(uuid);
        if (data == null) return 0;
        return data.getXp() / battlePassManager.getXpPerLevel();
    }

    /**
     * Check if a tier has been claimed
     * @param uuid Player UUID
     * @param tierLevel The level of the tier
     * @param isPremium Whether this is a premium tier
     * @return true if claimed, false otherwise
     */
    public static boolean hasClaimedTier(UUID uuid, int tierLevel, boolean isPremium) {
        PlayerData data = allPlayers.get(uuid);
        if (data == null) return false;

        // Create unique identifier: "level:type" (e.g., "1:free" or "1:premium")
        String tierKey = tierLevel + ":" + (isPremium ? "premium" : "free");
        return data.getTiersClaimed().contains(tierKey);
    }

    /**
     * Claim a battle pass tier
     * @param uuid Player UUID
     * @param tierLevel The level of the tier
     * @param isPremium Whether this is a premium tier
     */
    public static void claimBattlePassTier(UUID uuid, int tierLevel, boolean isPremium) {
        PlayerData data = allPlayers.get(uuid);
        if (data == null) return;

        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        // Get the appropriate tier
        BattlePassTier tier = isPremium
                ? battlePassManager.getPremiumTiers().get(tierLevel)
                : battlePassManager.getFreeTiers().get(tierLevel);

        if (tier == null) {
            player.sendMessage("§cThis tier does not exist!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Check if already claimed
        if (hasClaimedTier(uuid, tierLevel, isPremium)) {
            player.sendMessage("§cYou have already claimed this tier!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Check if player has reached this level
        int playerLevel = getBattlePassLevel(uuid);
        if (playerLevel < tierLevel) {
            player.sendMessage("§cYou must reach level " + tierLevel + " to claim this reward!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Check if premium tier requires premium pass
        if (isPremium && !data.isPremium()) {
            player.sendMessage("§cYou need the Premium Battle Pass to claim this reward!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Claim the tier
        String tierKey = tierLevel + ":" + (isPremium ? "premium" : "free");
        data.getTiersClaimed().add(tierKey);
        giveRewards(uuid, tier.getReward());

        player.sendMessage("§aYou have claimed: " + tier.getName());
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    public static void missionTriggerEvent(Player player, Object trigger, MissionType missionType, int amount, String missionId) {
        UUID uuid = player.getUniqueId();
        PlayerData playerData = allPlayers.get(uuid);
        if (playerData == null) return;

        Map<String, MissionProgress> missionProgresses = playerData.getMissions();

        if (missionId != null) {
            // Directly increment the specific mission without checking triggers
            Mission mission = getMissionById(missionId);
            if (mission == null) return;

            MissionProgress progress = missionProgresses.computeIfAbsent(
                    mission.getId(),
                    id -> new MissionProgress(id, 0, false, System.currentTimeMillis(), missionType)
            );

            addProgress(mission, progress, player, missionProgresses, playerData, amount);

        } else {
            // Normal trigger-based behavior
            List<Mission> missions = getMissionsByType(missionType);

            for (Mission mission : missions) {
                MissionProgress progress = missionProgresses.computeIfAbsent(
                        mission.getId(),
                        id -> new MissionProgress(id, 0, false, System.currentTimeMillis(), missionType)
                );

                List<Object> dataList = mission.getData();
                boolean match = false;

                if (dataList != null && !dataList.isEmpty()) {
                    for (Object obj : dataList) {
                        if (trigger instanceof Material mat) {
                            Material missionMat = Material.matchMaterial(obj.toString());
                            if (missionMat != null && missionMat == mat) {
                                match = true;
                                break;
                            }
                        } else if (trigger instanceof EntityType ent) {
                            try {
                                EntityType missionEnt = EntityType.valueOf(obj.toString().toUpperCase());
                                if (missionEnt == ent) {
                                    match = true;
                                    break;
                                }
                            } catch (IllegalArgumentException ignored) {}
                        } else if ("Walk".equals(trigger) || "Swim".equals(trigger) || "Sprint".equals(trigger)) {
                            match = true;
                        }
                    }
                } else {
                    match = true;
                }

                if (match) {
                    addProgress(mission, progress, player, missionProgresses, playerData, amount);
                }
            }
        }
    }



    // ---------------- Reward helper ----------------

    private static void giveRewards(UUID uuid, Reward reward) {
        if (reward == null) return;

        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        if (reward.getRewardType() == RewardType.MULTIPLE) {
            List<Reward> list = (List<Reward>) reward.getData();
            for (Reward r : list) giveRewards(uuid, r);
            return;
        }

        switch (reward.getRewardType()) {
            case COMMAND -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    ((String) reward.getData()).replace("%player%", player.getName()));
            case GOLD -> GhostRoyale.getApi().addGold(player, (int) reward.getData());
            case COSMETIC -> { /* give cosmetic logic */ }
            case XP -> getPlayerData(uuid).setXp(getPlayerData(uuid).getXp() + (int) reward.getData());
        }
    }

}