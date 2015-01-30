package me.theminebench.exgame.game.lobbygame.templates.countdowns;

import java.util.UUID;

import me.theminebench.exgame.UpdateListener;
import me.theminebench.exgame.Updater;
import me.theminebench.exgame.game.lobbygame.LobbyGameCreater;
import me.theminebench.exgame.game.lobbygame.LobbyGameCreater.GameState;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;

public class LobbyCountdown implements LobbyGameTemplate, UpdateListener {
	
	private int minPlayers;
	private int maxPlayers;
	
	private LobbyGameCreater lobbyGameCreater;
	
	// TODO get default time from config
	public final int DEFAULTTIME = 20;
	
	private int timeUntilStart;
	
	public LobbyCountdown(LobbyGameCreater lobbyGameCreater, int minPlayers, int maxPlayers) {
		this.lobbyGameCreater = lobbyGameCreater;
		
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
	}
	
	@Override
	public void gameStateChange(GameState oldGameState, GameState newGameState) {
		if (newGameState.equals(GameState.IN_LOBBY)) {
			if (getLobbyGameCreater().getArena().getPlayers().size() >= getMinPlayers())
				start();
			else
				stop();
		} else
			stop();
	}

	@Override
	public boolean canJoin(UUID playersUUID) {
		return !(getLobbyGameCreater().getArena().getPlayers().size() + 1 >= getMaxPlayers());
	}

	@Override
	public void playerJoin(UUID playersUUID) {
		checkCountdown();
	}
	
	@Override
	public void playerQuit(UUID playersUUID) {
		checkCountdown();
	}
	
	private void checkCountdown() {
		if (!getLobbyGameCreater().getGameState().equals(GameState.IN_LOBBY))
			return;
		if (getLobbyGameCreater().getArena().getPlayers().size() >= getMinPlayers())
			start();
		else
			stop();
	}
	
	@Override
	public void update(long tick) {
		if(getLobbyGameCreater().getGameState() != GameState.IN_LOBBY) {
			System.out.print("ERROR YOU ARE TRYING TO START THE LOBBY COUNTDOWN, AND THE GAME IS NOT IN_LOBBY!");
			stop();
			return;
		}
		
		if(getTimeUntilStart() <= 0) {
			getLobbyGameCreater().setGameState(GameState.PRE_GAME);
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
	public int getMinPlayers() {
		return minPlayers;
	}
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public void broadcast(String message) {
		getLobbyGameCreater().getArena().formattedBroadcast("CountDown", message);
	}
}
