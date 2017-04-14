package me.prestige.kit.duel;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by TREHOME on 04/14/2017.
 */
@Getter
public class DuelInvite {

    private final static long EXPIRE_TIME;

    static {
        EXPIRE_TIME = TimeUnit.MINUTES.toMillis(1);
    }

    private final UUID sender;
    private final UUID receiver;
    private final long timeSent;

    public DuelInvite(Player sender, Player receiver) {
        Preconditions.checkNotNull(sender, "Sender was null in a duel, weird?");
        Preconditions.checkNotNull(receiver, "Reciever was null in a duel, this should not happen!");
        this.sender = sender.getUniqueId();
        this.receiver = receiver.getUniqueId();
        timeSent = System.currentTimeMillis();
    }

    public boolean hasExpired(){
        return System.currentTimeMillis() - timeSent >= EXPIRE_TIME;
    }
}
