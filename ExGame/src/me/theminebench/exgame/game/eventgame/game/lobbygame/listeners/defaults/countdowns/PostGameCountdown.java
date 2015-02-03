package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.countdowns;

import me.theminebench.exgame.UpdateListener;
import me.theminebench.exgame.Updater;
import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameEndEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;

public class PostGameCountdown implements UpdateListener {
	
	private LobbyGameManager lobbyGameManager;
	
	// TODO get default time from config
	public final int DEFAULTTIME = 5;
	
	private int timeUntilStart;
	
	public PostGameCountdown(LobbyGameManager lobbyGameManager) {
		this.lobbyGameManager = lobbyGameManager;
		getLobbyGameManager().registerListener(this);
	}
	
	
	@GameEventHandler
	public void gameStateChange(GameStateChangeEvent e) {
		if (e.getCurrentGameState().equals(GameState.POST_GAME)) {
			start();
		} else {
			stop();
		}
			
	}
	
	@GameEventHandler
	public void onEnd(GameEndEvent e) {
		getLobbyGameManager().unregisterListener(this);
	}
	
	@Override
	public void update(long tick) {
		if(getLobbyGameManager().getGameState() != GameState.POST_GAME) {
			System.out.print("ERROR YOU HAVE THE POSTGAME COUNTDOWN, AND THE GAME IS NOT POST_GAME!");
			stop();
			return;
		}
		
		if(getTimeUntilStart() <= 0) {
			getLobbyGameManager().setGameState(GameState.RESTARTING);
			stop();
			return;
		}
		
		if ((getTimeUntilStart() % 10 == 0) || getTimeUntilStart() <= 5) {
			broadcast("Ending in " + getTimeUntilStart() + "!");
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
	
	public void broadcast(String message) {
		getLobbyGameManager().getEventGameManager().getArena().formattedBroadcast("CountDown", message);
	}
}
