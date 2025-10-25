package me.sheephun.komiBattlePass.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.sheephun.komiBattlePass.KomiBattlePass;
import me.sheephun.komiBattlePass.data.PlayerData;
import me.sheephun.komiBattlePass.util.IconUtil;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.eclipse.sisu.launch.Main;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.allPlayers;

public class MainMenu implements InventoryProvider {

    public static void open(Player player) {
        SmartInventory.builder()
                .id("main-menu")
                .provider(new MainMenu())
                .size(6, 9)
                .title("<shift:-8><glyph:battlepassmenu>")
                .manager(KomiBattlePass.getInvManager())
                .build()
                .open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        PlayerData playerData = allPlayers.get(player.getUniqueId());
        if (playerData == null) return;

        // Cached variables
        ItemStack playerStats = IconUtil.getPlayerStatsIcon(
                player.getName(),
                playerData.isPremium(),
                playerData.getXp()
        );
        ItemStack premiumIcon = IconUtil.getPremiumIcon();

        // === Rows 0 & 1 ===
        for (int row = 0; row <= 1; row++) {
            // First 2 columns = player stats
            for (int col = 0; col <= 1; col++) {
                contents.set(row, col, ClickableItem.empty(playerStats));
            }

            // Rest of row = Get Premium buttons
            for (int col = 2; col <= 8; col++) {
                contents.set(row, col, ClickableItem.of(premiumIcon, e -> {
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "⚡ Upgrade your Battle Pass!");

                    net.md_5.bungee.api.chat.TextComponent link = new net.md_5.bungee.api.chat.TextComponent(
                            ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Click here to open the store!"
                    );
                    link.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                            net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL,
                            "https://store.ghostempire.gg"
                    ));
                    link.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(
                            net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                            new net.md_5.bungee.api.chat.ComponentBuilder(
                                    ChatColor.YELLOW + "Click to open store.ghostempire.gg"
                            ).create()
                    ));

                    player.spigot().sendMessage(link);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    player.closeInventory();
                }));
            }
        }

        // === Rows 2 & 3 = Rewards ===
        ItemStack rewardsIcon = IconUtil.getRewardsIcon();
        for (int row = 2; row <= 3; row++) {
            for (int col = 0; col <= 8; col++) {
                contents.set(row, col, ClickableItem.of(rewardsIcon, e -> {BattlePassMenu.open(player, 1); player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);}));
            }
        }

        // === Rows 4 & 5 = Missions ===
        ItemStack missionsIcon = IconUtil.getMissionsIcon();
        for (int row = 4; row <= 5; row++) {
            for (int col = 0; col <= 8; col++) {
                contents.set(row, col, ClickableItem.of(missionsIcon, e -> {MissionsMainMenu.open(player); player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);}));
            }
        }
    }


    @Override
    public void update(Player player, InventoryContents contents) {}
}
