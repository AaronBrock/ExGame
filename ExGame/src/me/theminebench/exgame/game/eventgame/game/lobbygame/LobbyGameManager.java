package me.theminebench.exgame.game.eventgame.game.lobbygame;

import java.util.List;
import java.util.UUID;

import me.theminebench.exgame.game.eventgame.EventGameManager;
import me.theminebench.exgame.game.eventgame.game.EventGame;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameEndEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStartEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.utils.ServerUtil;


public class LobbyGameManager implements EventGame {
	
	private GameState gameState = GameState.RESTARTING;
	
	private LobbyGame currentLobbyGame;
	
	private LobbyGameFactory lobbyGameFactory;
	
	private EventGameManager eventGameManager;
	
	public LobbyGameManager(LobbyGameFactory lobbyGameFactory) {
		this.lobbyGameFactory = lobbyGameFactory;
	}
	
	@Override
	public void start() {
		startNewGame();
	}
	
	public void startNewGame() {
		if (!getGameState().equals(GameState.RESTARTING))
			return;
		
		this.currentLobbyGame = getLobbyGameFactory().getLobbyGame(this);
		
		if (getCurrentGame() == null) {
			ServerUtil.shutdown("Sorry, the game be NULL matey!", "The game created was NULL");
			return;
		}
		getCurrentGame().setLobbyGameManager(this);
		
		getCurrentGame().start();
		
		fireEvent(new GameStartEvent());
		
		setGameState(GameState.IN_LOBBY);
	}
	
	
	//Player stuff
	public List<UUID> getPlayers() {
		return getEventGameManager().getArena().getPlayers();
	}
	
	//Listener stuff
	
	public void registerListener(Object o) {
		getEventGameManager().getGameEventManager().registerListener(o);
	}
	
	public void unregisterListener(Object o) {
		getEventGameManager().getGameEventManager().unregisterListener(o);
	}
	
	public void fireEvent(Object o) {
		getEventGameManager().getGameEventManager().fireEvent(o);
	}
	
	//Setters
	
	public void setGameState(GameState newGameState) {
		GameState oldGameState = getGameState();
		this.gameState = newGameState;
		
		fireEvent(new GameStateChangeEvent(oldGameState, getGameState()));
		
		if (getGameState().equals(GameState.RESTARTING)) {
			fireEvent(new GameEndEvent());
			startNewGame();
		}
	}
	
	@Override
	public void setEventGame(EventGameManager eventGameManager) {
		this.eventGameManager = eventGameManager;
	}
	
	//Getters
	public GameState getGameState() {
		return this.gameState;
	}

	private LobbyGameFactory getLobbyGameFactory() {
		return lobbyGameFactory;
	}
	
	public LobbyGame getCurrentGame() {
		return currentLobbyGame;
	}
	
	public EventGameManager getEventGameManager() {
		return eventGameManager;
	}
	
	public String getName() {
		if (getCurrentGame() != null)
			return getCurrentGame().getName();
		else 
			return "Restarting";
	}
	
	public enum GameState {
		IN_LOBBY,
		PRE_GAME,
		IN_GAME,
		POST_GAME,
		RESTARTING;
		
		public boolean equals(GameState... gameStates) {
			for (GameState gs : gameStates) {
				if (this.equals(gs))
					return true;
			}
			return false;
		}
	}


}
