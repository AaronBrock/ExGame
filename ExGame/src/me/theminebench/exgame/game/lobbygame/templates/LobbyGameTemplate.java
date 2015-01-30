package me.theminebench.exgame.game.lobbygame.templates;

import java.util.UUID;

import me.theminebench.exgame.game.lobbygame.LobbyGameCreater.GameState;

import org.bukkit.event.Listener;

public interface LobbyGameTemplate extends Listener {
	
	public void gameStateChange(GameState oldGameState, GameState newGameState);
	
	public boolean canJoin(UUID playersUUID);
	
	public void playerJoin(UUID playersUUID);
	
	public void playerQuit(UUID playersUUID);
}
