package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.countdowns;

import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameEndEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.updater.TickListener;
import me.theminebench.exgame.updater.Ticker;

public class PreGameCountdown implements TickListener {
	
	private LobbyGameManager lobbyGameManager;
	
	// TODO get default time from config
	public final int DEFAULTTIME = 10;
	
	public PreGameCountdown(LobbyGameManager lobbyGameManager) {
		this.lobbyGameManager = lobbyGameManager;
		getLobbyGameManager().registerListener(this);
	}
	
	
	@GameEventHandler
	public void gameStateChange(GameStateChangeEvent e) {
		if (e.getCurrentGameState().equals(GameState.PRE_GAME)) {
			start();
		} else {
			stop();
		}	
	}
	
	@GameEventHandler
	public void onEnd(GameEndEvent e) {
		getLobbyGameManager().unregisterListener(this);
		stop();
	}

	
	private int tick;
	
	@Override
	public void tick() {
		if (tick % 20 == 0) {
			int count = tick / 20;
			
			if (getLobbyGameManager().getGameState() != GameState.PRE_GAME) {
				System.out.print("Stoping " + GameState.PRE_GAME + " countdown abruptly!");
				stop();
				return;
			}
			
			if (count <= 0) {
				getLobbyGameManager().setGameState(GameState.IN_GAME);
				stop();
				return;
			}
			
			if ((count % 10 == 0) || count <= 5) {
				
			}
			broadcast("Starting in " + count + "!");
		}

		tick--;
	}
	
	private void stop() {
		Ticker.unregisterListener(this);
	}
	
	private void start() {
		Ticker.registerListener(this);
		resetTimer();
	}
	
	public void resetTimer() {
		tick = DEFAULTTIME * 20;
	}
	
	public LobbyGameManager getLobbyGameManager() {
		return lobbyGameManager;
	}
	
	
	public void broadcast(String message) {
		getLobbyGameManager().getEventGameManager().getArena().formattedBroadcast("CountDown", message);
	}
}
