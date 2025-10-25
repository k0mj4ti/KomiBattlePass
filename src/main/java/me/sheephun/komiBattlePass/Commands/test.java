package me.sheephun.komiBattlePass.Commands;

import me.sheephun.komiBattlePass.data.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.allPlayers;

public class test implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player player){
            PlayerData playerData = allPlayers.get(player.getUniqueId());
            playerData.setXp(playerData.getXp() + 300);
            playerData.setPremium(true);
        }
        return true;
    }
}
