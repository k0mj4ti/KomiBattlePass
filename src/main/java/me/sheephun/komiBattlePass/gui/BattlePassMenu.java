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
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.stream.IntStream;

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
                .title("<shift:-7><glyph:rewardsmenu>")
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

        int tiersPerPage = 9;
        int startLevel = (page - 1) * tiersPerPage + 1;
        int endLevel = startLevel + tiersPerPage - 1;

        int freeRow = 1;
        int premiumRow = 3;

        for (int level = startLevel; level <= endLevel; level++) {
            int col = (level - startLevel);

            // --- FREE TIER ---
            BattlePassTier freeTier = freeTiers.get(level);
            if (freeTier != null) {
                boolean claimed = PlayerDataManager.hasClaimedTier(player.getUniqueId(), level, false);
                final int currentLevel = level;

                if (claimed) {
                    // Unclickable if already claimed
                    contents.set(freeRow, col, ClickableItem.empty(freeTier.getIcon(true, data)));
                } else {
                    // Clickable if not claimed
                    contents.set(freeRow, col, ClickableItem.of(
                            freeTier.getIcon(false, data),
                            e -> {
                                PlayerDataManager.claimBattlePassTier(player.getUniqueId(), currentLevel, false);
                                open(player, page);
                            }
                    ));
                }
            } else {
                contents.set(freeRow, col, ClickableItem.empty(IconUtil.noTierIcon()));
            }

            // --- PREMIUM TIER ---
            BattlePassTier premiumTier = premiumTiers.get(level);
            if (premiumTier != null) {
                boolean claimed = PlayerDataManager.hasClaimedTier(player.getUniqueId(), level, true);
                final int currentLevel = level;

                if (claimed) {
                    contents.set(premiumRow, col, ClickableItem.empty(premiumTier.getIcon(true, data)));
                } else {
                    contents.set(premiumRow, col, ClickableItem.of(
                            premiumTier.getIcon(false, data),
                            e -> {
                                PlayerDataManager.claimBattlePassTier(player.getUniqueId(), currentLevel, true);
                                open(player, page);
                            }
                    ));
                }
            } else {
                contents.set(premiumRow, col, ClickableItem.empty(IconUtil.noTierIcon()));
            }
        }

        // --- NAVIGATION ---
        contents.set(5, 2, ClickableItem.of(IconUtil.previousPageIcon(), e -> {
            if (page > 1) open(player, page - 1);
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
        }));

        // Only show next page if there are more tiers
        int nextStartLevel = page * tiersPerPage + 1;
        boolean hasNextPage = IntStream.range(nextStartLevel, nextStartLevel + tiersPerPage).anyMatch(level -> freeTiers.containsKey(level) || premiumTiers.containsKey(level));

        contents.set(5, 6, ClickableItem.of(IconUtil.nextPageIcon(), e -> {
            if (hasNextPage) open(player, page + 1);
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
        }));


        contents.set(5, 4, ClickableItem.of(IconUtil.backButtonIcon(), e -> {
            MainMenu.open(player);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }));

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Optionally, you can refresh periodically if you want live updates
    }
}
