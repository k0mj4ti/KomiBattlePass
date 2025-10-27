package me.sheephun.listeners;

import me.sheephun.komiBattlePass.data.Mission;
import me.sheephun.komiBattlePass.data.MissionProgress;
import me.sheephun.komiBattlePass.data.PlayerData;
import me.sheephun.komiBattlePass.enums.MissionType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static me.sheephun.komiBattlePass.managers.MissionManager.getMissionsByType;
import static me.sheephun.komiBattlePass.managers.PlayerDataManager.*;

public class CraftItemListener implements Listener {
    @EventHandler
    public void craftItemListener(CraftItemEvent e) {
        Player player = (Player) e.getWhoClicked();
        Material craftedItemMat = e.getCurrentItem().getType();
        missionTriggerEvent(player, craftedItemMat, MissionType.CRAFT_ITEM, e.getCurrentItem().getAmount(), null);
    }

}
