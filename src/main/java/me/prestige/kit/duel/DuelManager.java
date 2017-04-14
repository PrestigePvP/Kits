package me.prestige.kit.duel;

import lombok.Getter;
import lombok.Setter;
import me.prestige.kit.Kits;
import me.prestige.kit.util.GenericUtils;
import me.prestige.kit.util.PersistableLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by TREHOME on 04/14/2017.
 */
@Getter
public class DuelManager {

    private List<DuelInvite> inviteList = new ArrayList<>();
    private List<Duel> activeDuels = new ArrayList<>();
    @Setter
    private Location one;
    @Setter
    private Location two;
    private Kits plugin;

    public DuelManager(Kits plugin){
        this.plugin = plugin;
        Object object = plugin.getConfig().get("locations");
        if(object instanceof List) {
            List<PersistableLocation> locations = GenericUtils.createList(object, PersistableLocation.class);
            for(PersistableLocation location : locations) {
                if(one == null){
                    one = location.getLocation();
                }else {
                    two = location.getLocation();
                }
            }
        }

    }


    public boolean hasInvitedRecently(Player player) {
        for (DuelInvite invite : inviteList) {
            if (invite.getSender().equals(player.getUniqueId()) && !invite.hasExpired()) {
                return true;
            }
        }
        return false;
    }

    public void invite(DuelInvite invite) {
        inviteList.add(invite);
    }

    public DuelInvite accept(Player sender, Player receiver) {
        for (DuelInvite invite : inviteList) {
            if (invite.getReceiver().equals(receiver.getUniqueId()) && invite.getSender().equals(sender.getUniqueId())) {
                if (invite.hasExpired()) {
                    return null;
                }
                return invite;
            }
        }
        return null;
    }

    public Duel getDuel(Player player) {
        for (Duel duel : activeDuels) {
            if (duel.getAttacker().equals(player.getUniqueId()) || duel.getOther().equals(player.getUniqueId())) {
                return duel;
            }
        }
        return null;
    }

    public void save(){
        plugin.getConfig().set("locations", Arrays.asList(one, two));
        plugin.saveConfig();
    }

}
