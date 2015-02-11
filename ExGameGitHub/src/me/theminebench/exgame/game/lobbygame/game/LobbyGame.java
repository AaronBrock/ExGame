package me.theminebench.exgame.game.lobbygame.game;

import me.theminebench.exgame.game.lobbygame.LobbyGameManager;

public interface LobbyGame {

	public void setLobbyGameManager(LobbyGameManager lobbyGameManager);

	public LobbyGameManager getLobbyGameManager();

	public String getName();
}
