package me.sheephun.komiBattlePass.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.sheephun.komiBattlePass.KomiBattlePass;
import me.sheephun.komiBattlePass.util.IconUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MissionsMainMenu implements InventoryProvider {

    public static void open(Player player) {
        SmartInventory.builder()
                .id("missions-main")
                .provider(new MissionsMainMenu())
                .size(6, 9)
                .title("<shift:-8><glyph:missionsmenu>")
                .manager(KomiBattlePass.getInvManager())
                .build()
                .open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        // Rows 0–1: Daily Quests
        for (int row = 0; row <= 1; row++) {
            for (int col = 0; col < 9; col++) {
                contents.set(row, col, ClickableItem.of(IconUtil.getDailyQuestsIcon(),
                        e -> {DailyMissionsMenu.open(player, 0); player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);}));
            }
        }

        // Rows 2–3: Weekly Quests
        for (int row = 2; row <= 3; row++) {
            for (int col = 0; col < 9; col++) {
                contents.set(row, col, ClickableItem.of(IconUtil.getWeeklyQuestsIcon(),
                        e -> {WeeklyMissionsMenu.open(player, 0); player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);}));
            }
        }

        // Rows 4–5: All-Time Quests with Back/Close buttons
        for (int row = 4; row <= 5; row++) {
            for (int col = 0; col < 9; col++) {
                if (col == 0) {
                    contents.set(row, col, ClickableItem.of(IconUtil.backButtonIcon(),
                            e -> {MainMenu.open(player); player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);}));
                } else if (col == 8) {
                    contents.set(row, col, ClickableItem.of(IconUtil.closeButtonIcon(),
                            e -> {player.closeInventory(); player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);}));
                } else {
                    contents.set(row, col, ClickableItem.of(IconUtil.getAllTimeQuestsIcon(),
                            e -> {AllTimeMissionsMenu.open(player, 0); player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);}));
                }
            }
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}
