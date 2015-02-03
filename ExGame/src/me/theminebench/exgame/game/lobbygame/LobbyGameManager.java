package me.theminebench.exgame.game.lobbygame;

import java.io.File;
import java.util.UUID;

import me.theminebench.exgame.Arena;
import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.Game;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.game.lobbygame.events.LobbyEventManager;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.PlayerCanJoinArenaEvent;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.PlayerJoinArenaEvent;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.PlayerQuitArenaEvent;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.utils.ServerUtil;
import me.theminebench.exgame.utils.WorldUtil;

import org.bukkit.World;

public class LobbyGameManager implements Game {
	
	private Arena arena;
	private GameState gameState = GameState.RESTARTING;
	
	private LobbyGame currentLobbyGame;
	
	private LobbyGameFactory lobbyGameFactory;

	private File lobbyDataFile;
	
	private LobbyEventManager lobbyEventManager;
	
	private final String lobbyWorldName = "Lobby";
	
	public LobbyGameManager(LobbyGameFactory lobbyGameFactory) {
		this.lobbyGameFactory = lobbyGameFactory;
		this.lobbyDataFile = new File(ExGame.getPlugin().getDataFolder(), "LobbyGame");
		if (!getLobbyDataFile().exists()) {
			getLobbyDataFile().mkdirs();
			System.out.println("Created file " + getLobbyDataFile().getPath());
			
		}
		this.lobbyEventManager = new LobbyEventManager();
		
	}
	
	@Override
	public void start() {
		startNewGame();
	}
	
	public void startNewGame() {
		if (!getGameState().equals(GameState.RESTARTING))
			return;
		
		if (getLobbyWorld() == null)
			WorldUtil.createWorld(new File(getLobbyDataFile(), "Lobby"), getLobbyWorldName());
		
		this.currentLobbyGame = getLobbyGameFactory().getLobbyGame();
		
		if (getCurrentGame() == null) {
			ServerUtil.shutdown("Sorry, the game be NULL matey!", "The game created was NULL");
			return;
		}
		getCurrentGame().setLobbyGameManager(this);
		
		setGameState(GameState.IN_LOBBY);
	}
	
	@Override
	public String getName() {
		if (getCurrentGame() != null)
			return getCurrentGame().getName();
		else 
			return "Restarting";
	}
	
	@Override
	public boolean canJoin(UUID playersUUID) {
		PlayerCanJoinArenaEvent playerCanJoinArenaEvent = new PlayerCanJoinArenaEvent(playersUUID);
		getLobbyEventManager().fireEvent(playerCanJoinArenaEvent);
		return !playerCanJoinArenaEvent.isCancelled();
	}
	
	@Override
	public void playerJoin(UUID playersUUID) {
		getLobbyEventManager().fireEvent(new PlayerJoinArenaEvent(playersUUID));
	}

	@Override
	public void playerQuit(UUID playersUUID) {
		getLobbyEventManager().fireEvent(new PlayerQuitArenaEvent(playersUUID));
	}

	public LobbyGame getCurrentGame() {
		return currentLobbyGame;
	}
	
	public void setGameState(GameState newGameState) {
		GameState oldGameState = getGameState();
		this.gameState = newGameState;
		
		getLobbyEventManager().fireEvent(new GameStateChangeEvent(oldGameState, getGameState()));
		
		if (getGameState().equals(GameState.RESTARTING)) {
			startNewGame();
		}
		
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	public File getLobbyDataFile() {
		return lobbyDataFile;
	}
	
	@Override
	public void setArena(Arena arena) {this.arena = arena;}
	
	public Arena getArena() {return arena;}
	
	private LobbyGameFactory getLobbyGameFactory() {
		return lobbyGameFactory;
	}
	
	public void setCurrentLobbyGame(LobbyGame currentLobbyGame) {
		this.currentLobbyGame = currentLobbyGame;
	}
	
	public String getLobbyWorldName() {
		return lobbyWorldName;
	}
	
	public World getLobbyWorld() {
		return WorldUtil.getWorld(getLobbyWorldName());
	}
	
	public LobbyEventManager getLobbyEventManager() {
		return lobbyEventManager;
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
