package me.theminebench.exgame.game.eventgame.events;

import java.util.UUID;

import org.bukkit.event.Cancellable;

public class PlayerCanJoinArenaEvent implements Cancellable {
	
	private boolean cancellable;
	
	private UUID playersUUID;
	
	public PlayerCanJoinArenaEvent(UUID playersUUID) {
		this.playersUUID = playersUUID;
		this.cancellable = false;
	}
	
	public UUID getPlayersUUID() {
		return playersUUID;
	}
	
	@Override
	public boolean isCancelled() {
		return cancellable;
	}

	@Override
	public void setCancelled(boolean cancellable) {
		this.cancellable = cancellable;
	}

}
