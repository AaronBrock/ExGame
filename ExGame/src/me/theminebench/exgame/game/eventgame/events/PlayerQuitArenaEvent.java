package me.theminebench.exgame.game.eventgame.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerQuitArenaEvent {
	
	private UUID playersUUID;
	
	public PlayerQuitArenaEvent(UUID playersUUID) {
		this.playersUUID = playersUUID;
	}
	
	public UUID getPlayersUUID() {
		return playersUUID;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(getPlayersUUID());
	}
}
