package me.sheephun.komiBattlePass.api;

import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.entity.Player;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.missionTriggerEvent;

public class BattlePassAPI {
    public void addProgress(Player player, String missionId, int amount){
        missionTriggerEvent(player, null, MissionType.EXTERNAL, amount, missionId);
    }
}
