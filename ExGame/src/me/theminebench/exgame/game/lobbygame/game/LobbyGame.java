package me.theminebench.exgame.game.lobbygame.game;

import me.theminebench.exgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;


public interface LobbyGame {
	
	public void setLobbyGameManager(LobbyGameManager lobbyGameManager);
	public LobbyGameManager getLobbyGameManager();
	
	public LobbyGameTemplate[] getTemplates();
	public String getName();
}
