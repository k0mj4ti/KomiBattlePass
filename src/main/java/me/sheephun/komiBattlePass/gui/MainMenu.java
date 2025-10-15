package me.sheephun.komiBattlePass.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.sheephun.komiBattlePass.KomiBattlePass;
import me.sheephun.komiBattlePass.data.PlayerData;
import me.sheephun.komiBattlePass.managers.PlayerDataManager;
import me.sheephun.komiBattlePass.util.IconUtil;
import org.bukkit.entity.Player;
import org.bukkit.Material;

public class MainMenu implements InventoryProvider {

    public static void open(Player player) {
        SmartInventory.builder()
                .id("main-menu")
                .provider(new MainMenu())
                .size(3, 9)
                .title("BattlePass Main Menu")
                .manager(KomiBattlePass.getInvManager())
                .build()
                .open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        // BattlePass button
        contents.set(1, 3, ClickableItem.of(IconUtil.battlePassIcon(),
                e -> BattlePassMenu.open(player, 1)));

        // Missions button
        contents.set(1, 5, ClickableItem.of(IconUtil.missionsIcon(),
                e -> MissionsMenu.open(player, 0)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {}
}
