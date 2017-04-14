package me.prestige.kit.commands;

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
public class AcceptCommand implements CommandExecutor {

    private final Kits plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed in game!");
            return true;
        }
        Player player = (Player) sender;
        Player other;
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /" + s + " <playerName>");
            return true;
        }

        // We can simply check if the Bukkit player is null, they can't accept offline offers.
        if ((other = Bukkit.getPlayer(args[0])) == null) {
            player.sendMessage(ChatColor.RED + "Player " + ChatColor.GRAY + args[0] + ChatColor.RED + " not found!");
            return true;
        }
        // Make sure they're not inviting someone already in a duel
        if (plugin.getDuelManager().getDuel(other) != null) {
            player.sendMessage(ChatColor.RED + "That player is currently in a duel");
            return true;
        }
        // If they bypassed the duel thing somehow.
        if(other.equals(player)) {
            player.sendMessage(ChatColor.RED + "You cannot do this.");
            return true;
        }
        // Make sure the player inviting isn't in a duel
        if (plugin.getDuelManager().getDuel(player) != null){
            player.sendMessage(ChatColor.RED + "You cannot do this in a duel!");
            return true;
        }
        if(plugin.getDuelManager().getOne() == null || plugin.getDuelManager().getTwo() == null){
            player.sendMessage(ChatColor.RED + "Please contact an admin, the arena must be setup!");
            return true;
        }
        // If they had a duel, it will be here, otherwise it'll be null
        DuelInvite duelInvite = plugin.getDuelManager().accept(other, player);
        // Check if they're trying to accept an expired or non invited duel
        if(duelInvite == null){
            player.sendMessage(ChatColor.RED + "You were not dueled by this player or it has expired!");
            return true;
        }
        new Duel(duelInvite).start();
        plugin.getDuelManager().getInviteList().remove(duelInvite);
        return true;
    }
}
