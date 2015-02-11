package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spectate;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerDisableSpectateEvent {
	
	private Player player;
	
	private Location playerTeleportLocation;
	
	private GameMode gameMode;
	
	private GameModeSpectateManager spectateManager;
	
	public PlayerDisableSpectateEvent(Player player, Location playerTeleportLocation, GameMode gameMode, GameModeSpectateManager spectateManager) {
		this.player = player;
		this.playerTeleportLocation = playerTeleportLocation;
		this.gameMode = gameMode;
		this.spectateManager = spectateManager;
	}
	
	public void setPlayerTeleportLocation(Location playerTeleportLocation) {
		this.playerTeleportLocation = playerTeleportLocation;
	}
	
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}
	
	public GameMode getGameMode() {
		return gameMode;
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