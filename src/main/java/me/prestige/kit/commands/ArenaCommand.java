package me.prestige.kit.commands;

import com.google.common.primitives.Ints;
import lombok.RequiredArgsConstructor;
import me.prestige.kit.Kits;
import me.prestige.kit.duel.Duel;
import me.prestige.kit.duel.DuelInvite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by TREHOME on 04/14/2017.
 */
@RequiredArgsConstructor
public class ArenaCommand  implements CommandExecutor {

    private final Kits plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed in game!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /" + s + " <1|2>");
            return true;
        }
        Integer location = Ints.tryParse(args[0]);
        if(location == null){
            sender.sendMessage(ChatColor.RED + "Invalid input, please use either 1 or 2 for the location points!");
            return true;
        }
        if(location == 1){
            plugin.getDuelManager().setOne(player.getLocation());
        }else {
            if(location != 2){
                sender.sendMessage(ChatColor.RED + "Invalid input, please use either 1 or 2 for the location points!");
                return true;
            }
            plugin.getDuelManager().setTwo(player.getLocation());
        }
        sender.sendMessage(ChatColor.YELLOW + "You have successfully set point the spawn point for " + ChatColor.GREEN + location);
        return true;
    }
}