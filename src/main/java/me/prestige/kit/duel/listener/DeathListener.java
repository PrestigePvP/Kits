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
        if ((duel = plugin.getDuelManager().getDuel(e.getPlayer())) == null) return;
        duel.end(Bukkit.getPlayer(duel.getOpponent(e.getPlayer().getUniqueId())), e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e){
        Duel duel;
        if ((duel = plugin.getDuelManager().getDuel(e.getEntity())) == null) return;
        e.setDeathMessage(null);
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
              duel.end(e.getEntity().getKiller(), e.getEntity().getUniqueId());
            }
        }, 1);
    }
}
