package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.countdowns;

import java.util.List;
import java.util.UUID;

import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.events.PlayerCanJoinArenaEvent;
import me.theminebench.exgame.game.eventgame.events.PlayerJoinArenaEvent;
import me.theminebench.exgame.game.eventgame.events.PlayerQuitArenaEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameEndEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.PlayerManager;
import me.theminebench.exgame.updater.TickListener;
import me.theminebench.exgame.updater.Ticker;

public class LobbyCountdown implements TickListener {
	
	private int minPlayers;
	private int maxPlayers;
	
	private PlayerManager playerManager;
	private LobbyGameManager lobbyGameManager;
	
	// TODO get default time from config
	public final int DEFAULTTIME = 20;
	
	public LobbyCountdown(LobbyGameManager lobbyGameManager, int minPlayers, int maxPlayers) {
		this.lobbyGameManager = lobbyGameManager;
		
		this.minPlayers = minPlayers;
		
		this.maxPlayers = maxPlayers;
		
		getLobbyGameManager().registerListener(this);
	}
	
	
	@GameEventHandler
	public void gameStateChange(GameStateChangeEvent e) {
		if (e.getCurrentGameState().equals(GameState.IN_LOBBY)) {		
			checkCountdown();
		} else {
			stop();
		}
	}
	
	@GameEventHandler
	public void onEnd(GameEndEvent e) {
		getLobbyGameManager().unregisterListener(this);
		stop();
	}
	
	@GameEventHandler
	public void onPlayerManager(PlayerManager e) {
		this.playerManager = e;
	}
	
	@GameEventHandler
	public boolean canJoin(PlayerCanJoinArenaEvent e) {
		return !(getPlayers().size() + 1 >= getMaxPlayers());
	}

	@GameEventHandler
	public void playerJoin(PlayerJoinArenaEvent e) {
		checkCountdown();
	}
	
	@GameEventHandler
	public void playerQuit(PlayerQuitArenaEvent e) {
		checkCountdown();
	}
	
	private void checkCountdown() {
		if (!getLobbyGameManager().getGameState().equals(GameState.IN_LOBBY))
			return;
		if (getPlayers().size() >= getMinPlayers())
			start();
		else
			stop();
	}
	
	private int tick;
	
	@Override
	public void tick() {
		if (tick % 20 == 0) {
			int count = tick / 20;
			
			if (getLobbyGameManager().getGameState() != GameState.IN_LOBBY) {
				System.out.print("ERROR YOU ARE TRYING TO START THE LOBBY COUNTDOWN, AND THE GAME IS NOT IN_LOBBY!");
				stop();
				return;
			}
			
			if (count <= 0) {
				getLobbyGameManager().setGameState(GameState.PRE_GAME);
				stop();
				return;
			}
			
			if ((count % 10 == 0) || count <= 5) {
				
			}
			broadcast(count);
		}

		tick--;
	}

	private void stop() {
		Ticker.unregisterListener(this);
	}
	
	private void start() {
		if (!Ticker.isRegistered(this)) {
			Ticker.registerListener(this);
			resetTimer();
		}
	}
	
	public void resetTimer() {
		tick = DEFAULTTIME * 20;
	}
	
	public LobbyGameManager getLobbyGameManager() {
		return lobbyGameManager;
	}
	public int getMinPlayers() {
		return minPlayers;
	}
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	private List<UUID> getPlayers() {
		if (playerManager == null)
			return getLobbyGameManager().getPlayers();
		else
			return playerManager.getPlayers();
	}
	
	public void broadcast(int i) {
		getLobbyGameManager().getEventGameManager().getArena().formattedBroadcast("CountDown", "Starting in " + i + "!");
	}
	
	
	
	
	
	
	
	
	
}