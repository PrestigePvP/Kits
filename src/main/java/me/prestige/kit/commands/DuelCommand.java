package me.prestige.kit.commands;

import lombok.RequiredArgsConstructor;
import me.prestige.kit.Kits;
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
public class DuelCommand implements CommandExecutor {

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

        // We can simply check if the Bukkit player is null, they can't duel offline players.
        if ((other = Bukkit.getPlayer(args[0])) == null) {
            player.sendMessage(ChatColor.RED + "Player " + ChatColor.GRAY + args[0] + ChatColor.RED + " not found!");
            return true;
        }
        // Make sure they're not inviting someone already in a duel
        if (plugin.getDuelManager().getDuel(other) != null) {
            player.sendMessage(ChatColor.RED + "That player is already in a duel!");
            return true;
        }
        // Make sure the player inviting isn't in a duel
        if (plugin.getDuelManager().getDuel(player) != null){
            player.sendMessage(ChatColor.RED + "You cannot do this in a duel!");
            return true;
        }
        // So a player doesn't spam invites to random people.
        if (plugin.getDuelManager().hasInvitedRecently(player)) {
            player.sendMessage(ChatColor.RED + "You must wait for your past invite to time out before sending another one!");
            return true;
        }
        plugin.getDuelManager().invite(new DuelInvite(player, other));
        player.sendMessage(ChatColor.GOLD + "You have invited " + ChatColor.WHITE +  other.getName() + ChatColor.GOLD + " to a duel!");
        other.sendMessage(ChatColor.GOLD + "You have been invited to a duel by " + ChatColor.WHITE + player.getName() + '\n' + ChatColor.GOLD + "To accept do " + ChatColor.GREEN + "/accept " + player.getName() + ChatColor.GOLD + ".");
        return true;
    }
}
