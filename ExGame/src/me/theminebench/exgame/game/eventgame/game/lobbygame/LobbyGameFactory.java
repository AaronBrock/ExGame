package me.theminebench.exgame.game.eventgame.game.lobbygame;

import me.theminebench.exgame.game.eventgame.game.lobbygame.game.LobbyGame;

public interface LobbyGameFactory {
	public LobbyGame getLobbyGame(LobbyGameManager lobbyGameManager);
}
