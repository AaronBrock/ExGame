package me.theminebench.exgame.game.eventgame.game.lobbygame.game;

import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;

public interface LobbyGame {

	public void setLobbyGameManager(LobbyGameManager lobbyGameManager);
	
	public LobbyGameManager getLobbyGameManager();
	
	public String getName();
	
	public void start();
}