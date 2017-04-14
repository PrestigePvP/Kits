package me.prestige.kit.listener;

import lombok.RequiredArgsConstructor;
import me.prestige.kit.Kits;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by TREHOME on 04/14/2017.
 */
@RequiredArgsConstructor
public class SimplyLobbyListener  implements Listener{

    private final Kits plugin;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && !event.getPlayer().isOp()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && !event.getPlayer().isOp()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCancelHit(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            if(plugin.getDuelManager().getDuel((Player) e.getDamager()) == null || plugin.getDuelManager().getDuel((Player) e.getEntity()) == null){
                e.setCancelled(true);
            }
        }else {
            // Just cancel it if someones not a player
            e.setCancelled(true);
        }
    }
}
