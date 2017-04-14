package me.prestige.kit.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by TREHOME on 04/14/2017.
 */
public class SoupListener implements Listener {




    @EventHandler
    public void OnPlayerSoup(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if ((player).getHealth() != 20.0) {
            final int soup = 7;
            if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && player.getItemInHand().getType() == Material.MUSHROOM_SOUP) {
                player.setHealth(((player).getHealth() + soup > (player).getMaxHealth()) ? (player).getMaxHealth() : ((player).getHealth() + soup));
                event.getPlayer().getItemInHand().setType(Material.BOWL);
            }
        }else {
            player.setFoodLevel(player.getFoodLevel() + 7);
            event.getPlayer().getItemInHand().setType(Material.BOWL);
        }
    }


}
