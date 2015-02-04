package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spectate;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerEnableSpectateEvent {
	
	private Player player;
	
	private Location playerTeleportLocation;
	
	private GameModeSpectateManager spectateManager;
	
	public PlayerEnableSpectateEvent(Player player, Location playerTeleportLocation, GameModeSpectateManager spectateManager) {
		this.player = player;
		this.playerTeleportLocation = playerTeleportLocation;
		this.spectateManager = spectateManager;
	}
	
	public void setPlayerTeleportLocation(Location playerTeleportLocation) {
		this.playerTeleportLocation = playerTeleportLocation;
	}
	
	public Location getPlayerTeleportLocation() {
		return playerTeleportLocation;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public GameModeSpectateManager getSpectateManager() {
		return spectateManager;
	}
	
}