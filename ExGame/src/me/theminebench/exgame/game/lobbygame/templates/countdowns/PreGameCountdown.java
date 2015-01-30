package me.theminebench.exgame.game.lobbygame.templates.countdowns;

import java.util.UUID;

import me.theminebench.exgame.UpdateListener;
import me.theminebench.exgame.Updater;
import me.theminebench.exgame.game.lobbygame.LobbyGameCreater;
import me.theminebench.exgame.game.lobbygame.LobbyGameCreater.GameState;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;

public class PreGameCountdown implements LobbyGameTemplate, UpdateListener {
	
	private LobbyGameCreater lobbyGameCreater;
	// TODO get default time from config
	public final int DEFAULTTIME = 10;
	
	private int timeUntilStart;
	
	public PreGameCountdown(LobbyGameCreater lobbyGameCreater) {
		this.lobbyGameCreater = lobbyGameCreater;
	}
	
	@Override
	public void gameStateChange(GameState oldGameState, GameState newGameState) {
		if (newGameState.equals(GameState.PRE_GAME)) {
			start();
		} else
			stop();
	}

	@Override
	public boolean canJoin(UUID playersUUID) {
		return true;
	}

	@Override
	public void playerJoin(UUID playersUUID) {
	}
	
	@Override
	public void playerQuit(UUID playersUUID) {
	}
	
	
	@Override
	public void update(long tick) {
		if(getLobbyGameCreater().getGameState() != GameState.PRE_GAME) {
			System.out.print("ERROR YOU ARE TRYING TO START THE LOBBY COUNTDOWN, AND THE GAME IS NOT IN_LOBBY!");
			stop();
			return;
		}
		
		if(getTimeUntilStart() <= 0) {
			getLobbyGameCreater().setGameState(GameState.IN_GAME);
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
	
	public LobbyGameCreater getLobbyGameCreater() {
		return lobbyGameCreater;
	}
	
	public void broadcast(String message) {
		getLobbyGameCreater().getArena().formattedBroadcast("CountDown", message);
	}
}
