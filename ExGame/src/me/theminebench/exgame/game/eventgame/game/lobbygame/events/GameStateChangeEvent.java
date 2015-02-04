package me.theminebench.exgame.game.eventgame.game.lobbygame.events;

import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager.GameState;

public class GameStateChangeEvent {
	
	private GameState oldGameState;
	
	private GameState currentGameState;
	
	public GameStateChangeEvent(GameState oldGameState, GameState currentGameState) {
		this.oldGameState = oldGameState;
		this.currentGameState = currentGameState;
	}
	public GameState getCurrentGameState() {
		return currentGameState;
	}
	public GameState getOldGameState() {
		return oldGameState;
	}
}
