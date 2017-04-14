package me.prestige.kit.duel.event;

import lombok.Getter;
import me.prestige.kit.duel.Duel;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.Collection;

/**
 * Created by TREHOME on 04/14/2017.
 */
@Getter
public class DuelEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    private final Duel duel;

    public DuelEvent(Duel duel) {
        this.duel = duel;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
