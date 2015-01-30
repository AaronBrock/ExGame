package me.theminebench.exgame.game.lobbygame.game;

import me.theminebench.exgame.game.lobbygame.LobbyGameCreater;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;


public interface LobbyGame {
	
	public void setLobbyGameCreater(LobbyGameCreater lobbyGameCreater);
	public LobbyGameCreater getLobbyGameCreater();
	
	public LobbyGameTemplate[] getTemplates();
	public String getName();
}
