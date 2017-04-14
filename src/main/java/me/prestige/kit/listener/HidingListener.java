package me.prestige.kit.listener;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import me.prestige.kit.Kits;
import me.prestige.kit.duel.event.DuelEndEvent;
import me.prestige.kit.duel.event.DuelStartEvent;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by TREHOME on 04/14/2017.
 */
@RequiredArgsConstructor
public class HidingListener implements Listener {

    private final Kits plugin;



    @EventHandler
    public void onDuelStart(DuelStartEvent e) {
        Player attacker = Bukkit.getPlayer(e.getDuel().getAttacker());
        Player attacked = Bukkit.getPlayer(e.getDuel().getOther());
        if (attacked == null || attacker == null) return;
        for (Player on : Bukkit.getOnlinePlayers()) {
            if (on.getUniqueId().equals(e.getDuel().getAttacker()) || on.getUniqueId().equals(e.getDuel().getOther()))
                continue;
            on.hidePlayer(attacker);
            attacker.hidePlayer(on);

            on.hidePlayer(attacked);
            attacked.hidePlayer(on);
        }
    }

    @EventHandler
    public void onDuelEnd(DuelEndEvent e) {
        Player attacker = Bukkit.getPlayer(e.getDuel().getAttacker());
        Player attacked = Bukkit.getPlayer(e.getDuel().getOther());
        for (Player on : Bukkit.getOnlinePlayers()) {
            if (attacked != null /*TODO check if they're vanished */) {
                attacked.showPlayer(on);
            }
            if (attacker != null/*TODO check if they're vanished */) {
                attacker.showPlayer(on);
            }
        }
    }

    @EventHandler
    public void onPicupItem(PlayerPickupItemEvent e) {
        if (!e.getPlayer().canSee(e.getItem())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntitySpawn(PlayerDropItemEvent e) {
        for (Player on : Bukkit.getOnlinePlayers()) {
            if(on.canSee(e.getPlayer())) continue;
            on.hide(e.getItemDrop());
        }
    }



    void hidePlayer(Player hiding, Player from) {
        from.hidePlayer(hiding);
        EntityPlayer nmsFrom = ((CraftPlayer) from).getHandle();
        EntityPlayer nmsHiding = ((CraftPlayer) hiding).getHandle();
        nmsFrom.playerConnection.sendPacket(PacketPlayOutPlayerInfo.addPlayer(nmsHiding));
    }
}
