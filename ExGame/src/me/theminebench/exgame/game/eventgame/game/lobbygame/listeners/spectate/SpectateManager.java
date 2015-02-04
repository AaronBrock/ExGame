package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spectate;

import java.util.UUID;

import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.PlayerManager;

public interface SpectateManager extends PlayerManager {
	
	public void enableSpectate(UUID playersUUID);
	
	public void disableSpectate(UUID playersUUID);
	
	
}
