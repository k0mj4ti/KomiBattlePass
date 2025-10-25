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
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AllTimeMissionsMenu implements InventoryProvider {

    private final int page;
    private static final int MISSIONS_PER_PAGE = 9;

    public AllTimeMissionsMenu(int page) {
        this.page = Math.max(page, 0);
    }

    public static void open(Player player, int page) {
        SmartInventory.builder()
                .id("alltime-missions")
                .provider(new AllTimeMissionsMenu(page))
                .size(4, 9)
                .title("<shift:-8><glyph:alltimequestsmenu>")
                .manager(KomiBattlePass.getInvManager())
                .build()
                .open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        List<Mission> missions = new ArrayList<>(MissionManager.getOneTimeMissions().values());
        int start = page * MISSIONS_PER_PAGE;

        fillMissionRow(player, contents, 1, missions, start);

        contents.set(3, 2, ClickableItem.of(IconUtil.previousPageIcon(), e -> {
            if (page > 0) open(player, page - 1);
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
        }));

        contents.set(3, 4, ClickableItem.of(IconUtil.backButtonIcon(), e -> {MissionsMainMenu.open(player); player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);}));

        contents.set(3, 6, ClickableItem.of(IconUtil.nextPageIcon(), e -> {
            if (missions.size() > (page + 1) * MISSIONS_PER_PAGE) open(player, page + 1);
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
        }));
    }

    private void fillMissionRow(Player player, InventoryContents contents, int row, List<Mission> missions, int start) {
        ItemStack empty = new ItemStack(Material.BARRIER);
        ItemMeta meta = empty.getItemMeta();
        meta.setDisplayName("§cNo Mission");
        empty.setItemMeta(meta);

        for (int i = 0; i < MISSIONS_PER_PAGE; i++) {
            int index = start + i;
            if (index < missions.size()) {
                Mission mission = missions.get(index);
                MissionProgress progress = PlayerDataManager.getMissionProgress(player.getUniqueId(), mission.getId());
                contents.set(row, i, ClickableItem.of(mission.getIcon(progress), e -> {claimMission(player, mission); player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);}));
            } else {
                contents.set(row, i, ClickableItem.empty(empty));
            }
        }
    }

    private void claimMission(Player player, Mission mission) {
        PlayerDataManager.claimMission(player.getUniqueId(), mission.getId());
        player.sendMessage("§aClaimed all-time mission: §e" + mission.getName());
        open(player, page);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}
