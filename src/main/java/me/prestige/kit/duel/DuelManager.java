package me.prestige.kit.duel;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by TREHOME on 04/14/2017.
 */
public class DuelManager {

    @Getter
    private List<DuelInvite> inviteList = new ArrayList<>();
    @Getter
    private List<Duel> activeDuels = new ArrayList<>();


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

}
