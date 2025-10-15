package me.sheephun.komiBattlePass.managers;

import me.sheephun.komiBattlePass.data.Mission;
import me.sheephun.komiBattlePass.data.Reward;
import me.sheephun.komiBattlePass.enums.MissionType;
import me.sheephun.komiBattlePass.enums.MissionCategory;
import me.sheephun.komiBattlePass.enums.RewardType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class MissionManager {
    private static MissionManager missionManagerInstance;

    private final static Map<String, Mission> dailyMissions = new HashMap<>();
    private final static Map<String, Mission> weeklyMissions = new HashMap<>();
    private final static Map<String, Mission> oneTimeMissions = new HashMap<>();

    public MissionManager(FileConfiguration missionsConfig) {
        loadMissions(missionsConfig);
        missionManagerInstance = this;
    }

    private void loadMissions(FileConfiguration config) {
        loadCategory(config, "daily", dailyMissions);
        loadCategory(config, "weekly", weeklyMissions);
        loadCategory(config, "onetime", oneTimeMissions);
    }

    private void loadCategory(FileConfiguration config, String key, Map<String, Mission> target) {
        if (!config.contains(key)) {
            System.out.println("[KomiBattlePass] No configuration section found for: " + key);
            return;
        }

        List<Map<?, ?>> missionList = new ArrayList<>();

        // Support both list-based and section-based formats
        if (config.isList(key)) {
            // Example: daily: - id: ... (list)
            missionList = config.getMapList(key);
        } else if (config.isConfigurationSection(key)) {
            // Example: daily: mission1: {id: ..., name: ...} (nested section)
            for (String id : config.getConfigurationSection(key).getKeys(false)) {
                Map<String, Object> sectionMap = new HashMap<>();
                for (String subKey : config.getConfigurationSection(key + "." + id).getKeys(false)) {
                    sectionMap.put(subKey, config.get(key + "." + id + "." + subKey));
                }
                missionList.add(sectionMap);
            }
        }

        if (missionList.isEmpty()) {
            System.out.println("[KomiBattlePass] No missions found in: " + key);
            return;
        }

        System.out.println("[KomiBattlePass] Loading " + key + " missions: " + missionList.size() + " found");

        MissionCategory category;
        try {
            category = MissionCategory.valueOf(key.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("[KomiBattlePass] Invalid mission category key: " + key);
            return;
        }

        for (int i = 0; i < missionList.size(); i++) {
            Map<?, ?> o = missionList.get(i);
            try {
                String id = (String) o.get("id");
                String name = (String) o.get("name");
                String desc = (String) o.get("description");
                int xp = (int) o.get("xp");
                int targetCount = (int) o.get("target");

                MissionType type = MissionType.valueOf(((String) o.get("type")).toUpperCase());

                // Parse rewards
                List<Map<?, ?>> rewardsRaw = (List<Map<?, ?>>) o.get("rewards");
                List<Reward> rewards = new ArrayList<>();
                if (rewardsRaw != null) {
                    for (Map<?, ?> r : rewardsRaw) {
                        String rType = ((String) r.get("type")).toUpperCase();
                        Object data = r.get("amount") != null ? r.get("amount") : r.get("data");
                        rewards.add(new Reward(RewardType.valueOf(rType), data));
                    }
                }

                Mission mission = new Mission(
                        id,
                        type,
                        name,
                        desc,
                        xp,
                        targetCount,
                        category,
                        new Reward(RewardType.MULTIPLE, rewards)
                );

                target.put(id, mission);
                System.out.println("[KomiBattlePass] Loaded mission: " + id);

            } catch (Exception e) {
                System.out.println("[KomiBattlePass] Failed to load mission at index " + i + " in " + key + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }



    public static Map<String, Mission> getDailyMissions() { return dailyMissions; }
    public static Map<String, Mission> getWeeklyMissions() { return weeklyMissions; }
    public static Map<String, Mission> getOneTimeMissions() { return oneTimeMissions; }


    public Mission getMissionById(String id){
        if(dailyMissions.containsKey(id)) return dailyMissions.get(id);
        if(weeklyMissions.containsKey(id)) return weeklyMissions.get(id);
        return oneTimeMissions.get(id);
    }
    public static MissionManager getMissionManagerInstance(){
        return missionManagerInstance;
    }
}
