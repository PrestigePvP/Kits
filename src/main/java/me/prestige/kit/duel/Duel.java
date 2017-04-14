package me.prestige.kit.duel;

import lombok.Getter;
import me.prestige.kit.Kits;
import me.prestige.kit.duel.event.DuelEndEvent;
import me.prestige.kit.duel.event.DuelStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * Created by TREHOME on 04/14/2017.
 */
@Getter
public class Duel {

    private final UUID attacker;
    private final UUID other;
    private final long startTime;

    public Duel(DuelInvite duelInvite) {
        this.attacker = duelInvite.getSender();
        this.other = duelInvite.getReceiver();
        this.startTime = System.currentTimeMillis();
    }

    public void start() {
        Player one = Bukkit.getPlayer(attacker);
        Player two = Bukkit.getPlayer(other);
        if (one == null || two == null) {
            return;
        }
        Kits.getPlugin(Kits.class).getDuelManager().getActiveDuels().add(this);
        Bukkit.getPluginManager().callEvent(new DuelStartEvent(this));
        // Teleport them
        one.teleport(Bukkit.getWorld("world").getSpawnLocation().add(0, 0, 10));
        two.teleport(Bukkit.getWorld("world").getSpawnLocation().add(10, 0, 0));

        // Give them gear
        fillInventory(one);
        fillInventory(two);

        // Reset health and food and such
        one.setHealth(one.getMaxHealth());
        two.setHealth(two.getMaxHealth());


    }

    public void end(Player winner, Player dead) {

        Bukkit.getPluginManager().callEvent(new DuelEndEvent(this));
        if (dead.isDead()) {
            dead.spigot().respawn();
        }
        dead.sendMessage(ChatColor.GOLD + "You have lost the " + ChatColor.GREEN + "Buffed w/ Speed II " + ChatColor.GOLD + "match to " + ChatColor.WHITE + winner.getName() + ChatColor.GOLD + "." + ChatColor.WHITE + winner.getName() + ChatColor.GOLD + " had " + ChatColor.RED + countSoups(winner) + ChatColor.GOLD + " soups and " + ChatColor.RED + Math.round(winner.getHealth() / 2) + " hearts " + ChatColor.GOLD + "left.");
        dead.getInventory().clear();
        dead.getInventory().setArmorContents(null);
        dead.teleport(Bukkit.getWorld("world").getSpawnLocation());

        // Handle winner
        winner.removePotionEffect(PotionEffectType.SPEED);
        winner.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        winner.teleport(Bukkit.getWorld("world").getSpawnLocation());
        winner.sendMessage(ChatColor.YELLOW + "You have won the fight against " + dead.getName());
        winner.getInventory().clear();
        winner.getInventory().setArmorContents(null);
        winner.setHealth(winner.getMaxHealth());
        winner.setFoodLevel(20);
        winner.setSaturation(20);

        // Make sure we're actually ending this duel
        Kits.getPlugin(Kits.class).getDuelManager().getActiveDuels().remove(this);
    }

    private void fillInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().setItem(0, sword);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
        for (int i = 1; i < 9; i++) {
            player.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
        }
    }

    private int countSoups(Player player) {
        int amount = 0;
        for (ItemStack soup : player.getInventory().getContents()) {
            if(soup == null) continue;
            if(!soup.getType().equals(Material.MUSHROOM_SOUP)) continue;
            amount++;
        }
        return amount;
    }

    public UUID getOpponent(UUID uuid) {
        return attacker.equals(uuid) ? other : attacker;
    }


}
