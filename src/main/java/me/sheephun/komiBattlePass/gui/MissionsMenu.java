package me.sheephun.komiBattlePass.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.sheephun.komiBattlePass.KomiBattlePass;
import me.sheephun.komiBattlePass.data.Mission;
import me.sheephun.komiBattlePass.data.MissionProgress;
import me.sheephun.komiBattlePass.managers.MissionManager;
import me.sheephun.komiBattlePass.managers.PlayerDataManager;
import me.sheephun.komiBattlePass.util.IconUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MissionsMenu implements InventoryProvider {

    private final int page;
    private static final int MISSIONS_PER_ROW = 9;

    public MissionsMenu(int page) {
        this.page = Math.max(page, 0); // safety: never below 0
    }

    public static void open(Player player, int page) {
        SmartInventory.builder()
                .id("missions-menu")
                .provider(new MissionsMenu(Math.max(page, 0))) // force page >= 0
                .size(6, 9)
                .title("Missions - Page " + (page + 1))
                .manager(KomiBattlePass.getInvManager())
                .build()
                .open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        List<Mission> daily = new ArrayList<>(MissionManager.getDailyMissions().values());
        List<Mission> weekly = new ArrayList<>(MissionManager.getWeeklyMissions().values());
        List<Mission> oneTime = new ArrayList<>(MissionManager.getOneTimeMissions().values());

        System.out.println("=== Loading Missions Page " + page + " ===");
        System.out.println("Daily: " + daily.size());
        System.out.println("Weekly: " + weekly.size());
        System.out.println("One-Time: " + oneTime.size());

        int startIndex = page * MISSIONS_PER_ROW;

        fillMissionRow(player, contents, 0, daily, startIndex);
        fillMissionRow(player, contents, 2, weekly, startIndex);
        fillMissionRow(player, contents, 4, oneTime, startIndex);

        // Navigation buttons
        contents.set(5, 0, ClickableItem.of(IconUtil.previousPage(), e -> {
            if (page > 0) open(player, page - 1);
        }));

        contents.set(5, 8, ClickableItem.of(IconUtil.nextPage(), e -> {
            if (hasMorePages(daily, weekly, oneTime)) open(player, page + 1);
        }));

        contents.set(5, 4, ClickableItem.of(IconUtil.backButton(), e -> MainMenu.open(player)));
    }

    private void fillMissionRow(Player player, InventoryContents contents, int row, List<Mission> missions, int startIndex) {
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta meta = barrier.getItemMeta();
        meta.setDisplayName("§cNo Quest");  // invisible name
        barrier.setItemMeta(meta);

        for (int i = 0; i < MISSIONS_PER_ROW; i++) {
            int index = startIndex + i;
            if (index < missions.size()) {
                Mission m = missions.get(index);
                MissionProgress progress = PlayerDataManager.getMissionProgress(player.getUniqueId(), m.getId());
                contents.set(row, i, ClickableItem.of(m.getIcon(progress), e -> claimMission(player, m)));
            } else {
                contents.set(row, i, ClickableItem.empty(barrier));
            }
        }
    }

    private boolean hasMorePages(List<Mission> daily, List<Mission> weekly, List<Mission> oneTime) {
        int startIndex = (page + 1) * MISSIONS_PER_ROW;
        return daily.size() > startIndex || weekly.size() > startIndex || oneTime.size() > startIndex;
    }

    private void claimMission(Player player, Mission mission) {
        PlayerDataManager.claimMission(player.getUniqueId(), mission.getId());
        player.sendMessage("§aYou claimed rewards for mission: §e" + mission.getName());
        open(player, page); // refresh
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Optional live updates for progress
    }
}
