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
            missionList = config.getMapList(key);
        } else if (config.isConfigurationSection(key)) {
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
                String item = (String) o.get("item");

                MissionType type = MissionType.valueOf(((String) o.get("type")).toUpperCase());

                // Parse data
                List<?> dataRaw = (List<?>) o.get("data");
                List<Object> data = new ArrayList<>();
                if (dataRaw != null) {
                    data.addAll(dataRaw);
                } else {
                    data = null;
                }

                Mission mission = new Mission(
                        id,
                        type,
                        name,
                        desc,
                        xp,
                        targetCount,
                        data,
                        item,
                        category
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


    public static Mission getMissionById(String id){
        if(dailyMissions.containsKey(id)) return dailyMissions.get(id);
        if(weeklyMissions.containsKey(id)) return weeklyMissions.get(id);
        return oneTimeMissions.get(id);
    }
    public static List<Mission> getMissionsByType(MissionType missionType){
        List<Mission> missions = new ArrayList<>();
        for(Mission mission : dailyMissions.values()){
            if (mission.getMissionType().equals(missionType)){
                missions.add(mission);
            }
        }
        for(Mission mission : weeklyMissions.values()){
            if (mission.getMissionType().equals(missionType)){
                missions.add(mission);
            }
        }
        for(Mission mission : weeklyMissions.values()){
            if (mission.getMissionType().equals(missionType)){
                missions.add(mission);
            }
        }
        return missions;
    }
    public static MissionManager getMissionManagerInstance(){
        return missionManagerInstance;
    }
}
