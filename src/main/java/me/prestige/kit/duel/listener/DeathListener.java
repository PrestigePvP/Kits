package me.prestige.kit.duel.listener;

import lombok.RequiredArgsConstructor;
import me.prestige.kit.Kits;
import me.prestige.kit.duel.Duel;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by TREHOME on 04/14/2017.
 */
@RequiredArgsConstructor
public class DeathListener implements Listener {

    private final Kits plugin;

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Duel duel;
        // I don't care if they DC not in a duel
        if ((duel = plugin.getDuelManager().getDuel(e.getPlayer())) == null) return;
        // End the duel if they leave.
        duel.end(Bukkit.getPlayer(duel.getOpponent(e.getPlayer().getUniqueId())), e.getPlayer());
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e){
        Duel duel;
        if ((duel = plugin.getDuelManager().getDuel(e.getEntity())) == null) return;
        // Since its  a duel, no one but the 2 care about messages
        e.setDeathMessage(null);
        // In case they had xp to drop
        e.setDroppedExp(0);
        // No need to drop items
        e.getDrops().clear();
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                // Run this a tick later so that they respawn on time and such.
              duel.end(e.getEntity().getKiller(), e.getEntity());
            }
        }, 1);
    }
}
