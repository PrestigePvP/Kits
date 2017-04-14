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

    private static final int HEALTH_PER_SOUP;

    static {
        HEALTH_PER_SOUP = 7;
    }



    @EventHandler
    public void OnPlayerSoup(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if(player.getHealth() == 20 && player.getFoodLevel() == 20) return;
        if (player.getHealth() != 20.0) {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && player.getItemInHand().getType() == Material.MUSHROOM_SOUP) {
                player.setHealth(((player).getHealth() + HEALTH_PER_SOUP > (player).getMaxHealth()) ? (player).getMaxHealth() : ((player).getHealth() + HEALTH_PER_SOUP));
                event.getPlayer().getItemInHand().setType(Material.BOWL);
            }
        }else {
            player.setFoodLevel(player.getFoodLevel() + 7);
            event.getPlayer().getItemInHand().setType(Material.BOWL);
        }
    }


}
