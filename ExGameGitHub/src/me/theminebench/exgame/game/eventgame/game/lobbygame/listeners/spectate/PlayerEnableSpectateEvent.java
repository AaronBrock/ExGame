package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spectate;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerEnableSpectateEvent {
	
	private UUID playersUUID;
	
	private Location playerTeleportLocation;
	
	private GameModeSpectateManager spectateManager;
	
	public PlayerEnableSpectateEvent(UUID playersUUID, Location playerTeleportLocation, GameModeSpectateManager spectateManager) {
		this.playersUUID = playersUUID;
		this.playerTeleportLocation = playerTeleportLocation;
		this.spectateManager = spectateManager;
	}
	
	public void setPlayerTeleportLocation(Location playerTeleportLocation) {
		this.playerTeleportLocation = playerTeleportLocation;
	}
	
	public Location getPlayerTeleportLocation() {
		return playerTeleportLocation;
	}
	
	public UUID getPlayersUUID() {
		return playersUUID;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(getPlayersUUID());
	}
	
	public GameModeSpectateManager getSpectateManager() {
		return spectateManager;
	}
	
}