package me.sheephun.komiBattlePass.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.sheephun.komiBattlePass.KomiBattlePass;
import me.sheephun.komiBattlePass.data.BattlePassTier;
import me.sheephun.komiBattlePass.data.PlayerData;
import me.sheephun.komiBattlePass.managers.BattlePassManager;
import me.sheephun.komiBattlePass.managers.PlayerDataManager;
import me.sheephun.komiBattlePass.util.IconUtil;
import org.bukkit.entity.Player;

import java.util.Map;

public class BattlePassMenu implements InventoryProvider {

    private final int page;

    public BattlePassMenu(int page) {
        this.page = page;
    }

    public static void open(Player player, int page) {
        SmartInventory.builder()
                .id("battlepass-menu")
                .provider(new BattlePassMenu(page))
                .size(6, 9)
                .title("BattlePass")
                .manager(KomiBattlePass.getInvManager())
                .build()
                .open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        PlayerData data = PlayerDataManager.getPlayerData(player.getUniqueId());
        BattlePassManager manager = BattlePassManager.getBattlePassInstance();
        Map<Integer, BattlePassTier> freeTiers = manager.getFreeTiers();
        Map<Integer, BattlePassTier> premiumTiers = manager.getPremiumTiers();

        int tiersPerPage = 8; // 8 levels per page
        int startLevel = (page - 1) * tiersPerPage + 1;
        int endLevel = startLevel + tiersPerPage - 1;

        int freeRow = 1;      // Row for free tiers
        int premiumRow = 3;   // Row for premium tiers


        // Place row labels FIRST (in column 0)
        contents.set(freeRow, 0, ClickableItem.empty(IconUtil.freeIcon()));
        contents.set(premiumRow, 0, ClickableItem.empty(IconUtil.premiumIcon()));

        // Place tiers for each level
        for (int level = startLevel; level <= endLevel; level++) {
            int col = (level - startLevel) + 1; // Column 1-8 for levels

            // Place free tier if exists
            BattlePassTier freeTier = freeTiers.get(level);
            if (freeTier != null) {
                boolean claimed = PlayerDataManager.hasClaimedTier(player.getUniqueId(), level, false);
                final int currentLevel = level;
                contents.set(freeRow, col, ClickableItem.of(
                        freeTier.getIcon(claimed),
                        e -> PlayerDataManager.claimBattlePassTier(player.getUniqueId(), currentLevel, false)
                ));
            } else {
                contents.set(freeRow, col, ClickableItem.empty(IconUtil.barrierNoTier()));
            }

            // Place premium tier if exists
            BattlePassTier premiumTier = premiumTiers.get(level);
            if (premiumTier != null) {
                boolean claimed = PlayerDataManager.hasClaimedTier(player.getUniqueId(), level, true);
                final int currentLevel = level;
                contents.set(premiumRow, col, ClickableItem.of(
                        premiumTier.getIcon(claimed),
                        e -> PlayerDataManager.claimBattlePassTier(player.getUniqueId(), currentLevel, true)
                ));
            } else {
                contents.set(premiumRow, col, ClickableItem.empty(IconUtil.barrierNoTier()));
            }
        }

        // Page navigation
        contents.set(5, 0, ClickableItem.of(IconUtil.previousPage(), e -> {
            if (page > 1) open(player, page - 1);
        }));
        contents.set(5, 8, ClickableItem.of(IconUtil.nextPage(), e -> open(player, page + 1)));
        contents.set(5, 4, ClickableItem.of(IconUtil.backButton(), e -> MainMenu.open(player)));
    }




    @Override
    public void update(Player player, InventoryContents contents) {
        // Here you could dynamically update claimed tiers without reopening
    }
}
