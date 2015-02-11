package me.theminebench.exgame.game.eventgame;

import java.util.UUID;

import me.theminebench.exgame.Arena;
import me.theminebench.exgame.game.Game;
import me.theminebench.exgame.game.eventgame.events.PlayerCanJoinArenaEvent;
import me.theminebench.exgame.game.eventgame.events.PlayerJoinArenaEvent;
import me.theminebench.exgame.game.eventgame.events.PlayerQuitArenaEvent;
import me.theminebench.exgame.game.eventgame.game.EventGame;

public class EventGameManager implements Game {

	private Arena arena;

	private EventGame eventGame;

	private GameEventManager gameEventManager;

	public EventGameManager(EventGame eventGame) {
		this.eventGame = eventGame;
		getEventGame().setEventGame(this);
		this.gameEventManager = new GameEventManager();
	}

	@Override
	public void start() {
		getEventGame().start();
	}

	@Override
	public String getName() {
		return getEventGame().getName();
	}

	@Override
	public boolean canJoin(UUID playersUUID) {
		PlayerCanJoinArenaEvent playerCanJoinArenaEvent = new PlayerCanJoinArenaEvent(playersUUID);
		getGameEventManager().fireEvent(playerCanJoinArenaEvent);
		return !playerCanJoinArenaEvent.isCancelled();
	}

	@Override
	public void playerJoin(UUID playersUUID) {
		getGameEventManager().fireEvent(new PlayerJoinArenaEvent(playersUUID));
	}

	@Override
	public void playerQuit(UUID playersUUID) {
		getGameEventManager().fireEvent(new PlayerQuitArenaEvent(playersUUID));
	}

	public GameEventManager getGameEventManager() {
		return gameEventManager;
	}

	public EventGame getEventGame() {
		return eventGame;
	}

	public Arena getArena() {
		return arena;
	}

	@Override
	public void setArena(Arena arena) {
		this.arena = arena;
	}
}
