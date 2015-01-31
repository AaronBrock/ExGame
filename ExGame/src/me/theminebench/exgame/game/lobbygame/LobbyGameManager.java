package me.theminebench.exgame.game.lobbygame;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import me.theminebench.exgame.Arena;
import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.Game;
import me.theminebench.exgame.game.lobbygame.events.LobbyEvent;
import me.theminebench.exgame.game.lobbygame.events.LobbyEventHandler;
import me.theminebench.exgame.game.lobbygame.events.LobbyEventPriority;
import me.theminebench.exgame.game.lobbygame.events.LobbyListener;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.GameStateChangeEvent;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.PlayerCanJoinArenaEvent;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.PlayerJoinArenaEvent;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.PlayerQuitArenaEvent;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.utils.ServerUtil;
import me.theminebench.exgame.utils.WorldUtil;

import org.bukkit.World;
import org.bukkit.event.Cancellable;

public class LobbyGameManager implements Game {
	
	private Arena arena;
	private GameState gameState = GameState.RESTARTING;
	
	private LobbyGame currentLobbyGame;
	
	private LobbyGameFactory lobbyGameFactory;

	private LobbyGameTemplate[] lobbyGameTemplates;
	private File lobbyDataFile;
	
	private final String lobbyWorldName = "Lobby";
	
	public LobbyGameManager(LobbyGameFactory lobbyGameFactory) {
		this.lobbyGameFactory = lobbyGameFactory;
		this.lobbyDataFile = new File(ExGame.getPlugin().getDataFolder(), "LobbyGame");
		if (!getLobbyDataFile().exists()) {
			getLobbyDataFile().mkdirs();
			ServerUtil.shutdown("The server is not setup.", "Created files.");
			return;
		}
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
		
		setLobbyGameTemplates(getCurrentGame().getTemplates());
		
		setGameState(GameState.IN_LOBBY);
	}
	
	private ConcurrentHashMap<Class<? extends LobbyEvent>, ConcurrentHashMap<LobbyListener, HashSet<Method>>> methods = new ConcurrentHashMap<Class<? extends LobbyEvent>, ConcurrentHashMap<LobbyListener, HashSet<Method>>>();
	
	public void setLobbyGameTemplates(LobbyGameTemplate[] lobbyGameTemplates) {
		this.lobbyGameTemplates = lobbyGameTemplates;
	}
	
	@SuppressWarnings("unchecked")
	public void registerLobbyListener(LobbyListener listener) {
		for (Method m : listener.getClass().getMethods()) {
			if (m.isAnnotationPresent(LobbyEventHandler.class)) {				
				if (m.getParameterCount() == 1) {
					if (LobbyEvent.class.isAssignableFrom(m.getParameterTypes()[0])) {
						if (!methods.containsKey(m.getParameterTypes()[0])) {
							methods.put((Class<? extends LobbyEvent>) m.getParameterTypes()[0], new ConcurrentHashMap<LobbyListener, HashSet<Method>>());
						}
						if (methods.get(m.getParameterTypes()[0]).get(listener) == null) {
							methods.get(m.getParameterTypes()[0]).put(listener, new HashSet<Method>());
						}
						
						methods.get(m.getParameterTypes()[0]).get(listener).add(m);
					}
				}
			}
		}
	}
	
	public void unregisterLobbyListener(LobbyListener listener) {
		
		HashSet<Class<? extends LobbyEvent>> toRemove = new HashSet<Class<? extends LobbyEvent>>();
		
		for (Class<? extends LobbyEvent> eventClass : methods.keySet()) {
			
			methods.get(eventClass).remove(listener);
			
			//System.out.println("from unregisterLobbyListener: " + listener.getClass().getName());
			
			if (methods.get(eventClass).isEmpty()) {
				toRemove.add(eventClass);
			}
		}
		
		for (Class<? extends LobbyEvent> eventClass : toRemove) {
			methods.remove(eventClass);
		}
		
	}
	
	public void fireEvent(LobbyEvent lobbyEvent) {
		
		if (!methods.containsKey(lobbyEvent.getClass()))
			return;
		
		ConcurrentHashMap<LobbyListener, HashSet<Method>> hashMap = new ConcurrentHashMap<LobbyListener, HashSet<Method>>(methods.get(lobbyEvent.getClass()));
		//methods.get(lobbyEvent.getClass());
		
		for (LobbyListener listener : hashMap.keySet()) {
			
			//TODO Do this a better way!
			
			for (LobbyEventPriority lep : LobbyEventPriority.values()) {
				
				
				for (Method m : hashMap.get(listener)) {
					
					if (((lobbyEvent instanceof Cancellable)) && (((Cancellable) lobbyEvent).isCancelled()) && (m.getAnnotation(LobbyEventHandler.class).ignoreCancelled()))
						continue;

					if (m.getAnnotation(LobbyEventHandler.class).priority().equals(lep)) {
						
						try {
							m.invoke(listener, lobbyEvent);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
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
		fireEvent(playerCanJoinArenaEvent);
		return !playerCanJoinArenaEvent.isCancelled();
	}
	
	@Override
	public void playerJoin(UUID playersUUID) {
		fireEvent(new PlayerJoinArenaEvent(playersUUID));
	}

	@Override
	public void playerQuit(UUID playersUUID) {
		fireEvent(new PlayerQuitArenaEvent(playersUUID));
	}

	public LobbyGame getCurrentGame() {
		return currentLobbyGame;
	}
	
	public void setGameState(GameState newGameState) {
		GameState oldGameState = getGameState();
		this.gameState = newGameState;
		
		fireEvent(new GameStateChangeEvent(oldGameState, getGameState()));
		
		if (gameState.equals(GameState.RESTARTING)) {
			startNewGame();
		}
		
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	public File getLobbyDataFile() {
		return lobbyDataFile;
	}
	
	public LobbyGameTemplate[] getTemplates() {
		return this.lobbyGameTemplates;
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
	
	public LobbyGameTemplate[] getLobbyGameTemplates() {
		return lobbyGameTemplates;
	}
	
	public String getLobbyWorldName() {
		return lobbyWorldName;
	}
	
	public World getLobbyWorld() {
		return WorldUtil.getWorld(getLobbyWorldName());
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
