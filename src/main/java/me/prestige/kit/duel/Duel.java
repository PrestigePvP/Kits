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
        // I have it, but don't use it. If you wanted you could display game length.
        this.startTime = System.currentTimeMillis();
    }

    public void start() {
        Player one = Bukkit.getPlayer(attacker);
        Player two = Bukkit.getPlayer(other);
        if (one == null || two == null) {
            return;
        }
        // Make sure the duel is registered.
        Kits.getPlugin(Kits.class).getDuelManager().getActiveDuels().add(this);
        // Call this for hiding folks
        Bukkit.getPluginManager().callEvent(new DuelStartEvent(this));
        // Teleport them
        one.teleport(Kits.getPlugin(Kits.class).getDuelManager().getOne());
        two.teleport(Kits.getPlugin(Kits.class).getDuelManager().getTwo());

        // Give them gear
        fillInventory(one);
        fillInventory(two);

        // Reset health and food and such
        one.setHealth(one.getMaxHealth());
        two.setHealth(two.getMaxHealth());


    }

    public void end(Player winner, Player dead) {
        // Call this to unhide folks
        Bukkit.getPluginManager().callEvent(new DuelEndEvent(this));
        // respawn the player so they can see our pretty message
        if (dead.isDead()) {
            dead.spigot().respawn();
        }
        // send our pretty message
        dead.sendMessage(ChatColor.GOLD + "You have lost the " + ChatColor.GREEN + "Buffed w/ Speed II " + ChatColor.GOLD + "match to " + ChatColor.WHITE + winner.getName() + ChatColor.GOLD + "." + ChatColor.WHITE + winner.getName() + ChatColor.GOLD + " had " + ChatColor.RED + countSoups(winner) + ChatColor.GOLD + " soups and " + ChatColor.RED + Math.round(winner.getHealth() / 2) + " hearts " + ChatColor.GOLD + "left.");
        // In case something weird happened we need to clear their stuff
        dead.getInventory().clear();
        dead.getInventory().setArmorContents(null);
        // Teleport them to spawn
        dead.teleport(Bukkit.getWorld("world").getSpawnLocation());

        // Handle winner
        winner.teleport(Bukkit.getWorld("world").getSpawnLocation());
        winner.sendMessage(ChatColor.YELLOW + "You have won the fight against " + dead.getName());
        // Clear their inventory and such
        winner.removePotionEffect(PotionEffectType.SPEED);
        winner.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        winner.getInventory().clear();
        winner.getInventory().setArmorContents(null);
        // Make them full health, so they don't die somehow in spawn.
        winner.setHealth(winner.getMaxHealth());
        winner.setFoodLevel(20);
        winner.setSaturation(20);

        // Make sure we're actually ending this duel
        Kits.getPlugin(Kits.class).getDuelManager().getActiveDuels().remove(this);
    }

    private void fillInventory(Player player) {
        // Add all the things for a duel.
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
            // Make sure the item isn't null, and a soup if so then add it.
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
