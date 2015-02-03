package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.countdowns;

import java.util.List;
import java.util.UUID;

import me.theminebench.exgame.UpdateListener;
import me.theminebench.exgame.Updater;
import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.events.PlayerCanJoinArenaEvent;
import me.theminebench.exgame.game.eventgame.events.PlayerJoinArenaEvent;
import me.theminebench.exgame.game.eventgame.events.PlayerQuitArenaEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.PlayerManager;



public class LobbyCountdown implements UpdateListener {
	
	private int minPlayers;
	private int maxPlayers;
	
	private PlayerManager playerManager;
	
	private LobbyGameManager lobbyGameManager;
	
	// TODO get default time from config
	public final int DEFAULTTIME = 20;
	
	private int timeUntilStart;
	
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
	public void onEnd() {
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
	
	@Override
	public void update(long tick) {
		if(getLobbyGameManager().getGameState() != GameState.IN_LOBBY) {
			System.out.print("ERROR YOU ARE TRYING TO START THE LOBBY COUNTDOWN, AND THE GAME IS NOT IN_LOBBY!");
			stop();
			return;
		}
		
		if(getTimeUntilStart() <= 0) {
			getLobbyGameManager().setGameState(GameState.PRE_GAME);
			stop();
			return;
		}
		
		if ((getTimeUntilStart() % 10 == 0) || getTimeUntilStart() <= 5) {
			broadcast("Starting in " + getTimeUntilStart() + "!");
		}
		
		setTimeUntilStart(getTimeUntilStart() - 1);
	}
	
	private void stop() {
		Updater.instance().unregisterListener(this);
	}
	
	private void start() {
		if (!Updater.instance().isRegistered(this)) {
			Updater.instance().registerListener(this, 20);
			resetTimer();
		}
	}
	
	public void resetTimer() {
		setTimeUntilStart(DEFAULTTIME);
	}
	
	public int getTimeUntilStart() {
		return timeUntilStart;
	}
	
	public void setTimeUntilStart(int timeUntilStart) {
		this.timeUntilStart = timeUntilStart;
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
	
	
	public void broadcast(String message) {
		getLobbyGameManager().getEventGameManager().getArena().formattedBroadcast("CountDown", message);
	}
}
