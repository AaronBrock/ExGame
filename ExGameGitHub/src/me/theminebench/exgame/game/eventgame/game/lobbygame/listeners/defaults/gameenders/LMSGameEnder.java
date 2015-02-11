package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.gameenders;

import java.util.List;
import java.util.UUID;

import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.events.PlayerQuitArenaEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameEndEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.PlayerManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spectate.PlayerEnableSpectateEvent;
import me.theminebench.exgame.utils.ServerUtil;

public class LMSGameEnder {
	
	private LobbyGameManager lobbyGameManager;
	private PlayerManager playerManager;
	
	private int endPlayersAmount;
	
	public LMSGameEnder(LobbyGameManager lobbyGameManager) {
		this(lobbyGameManager, 1);
	}
	
	public LMSGameEnder(LobbyGameManager lobbyGameManager, int endPlayersAmount) {
		this.lobbyGameManager = lobbyGameManager;
		this.endPlayersAmount = endPlayersAmount;
		getLobbyGameManager().registerListener(this);
	}
	
	@GameEventHandler
	public void onEnd(GameEndEvent e) {
		getLobbyGameManager().unregisterListener(this);
	}
	
	@GameEventHandler
	public void onPlayerManager(PlayerManager e) {
		this.playerManager = e;
	}
	
	@GameEventHandler
	public void enableSpectate(PlayerEnableSpectateEvent e) {
		checkEndGame();
	}
	
	@GameEventHandler
	public void playerLeaveEvent(PlayerQuitArenaEvent e) {
		checkEndGame();
	}
	
	public void checkEndGame() {
		if (getGamePlayers().size() <= endPlayersAmount) {
			getLobbyGameManager().setGameState(GameState.POST_GAME);
		}
	}
	
	private List<UUID> getGamePlayers() {
		if (playerManager == null) {
			ServerUtil.shutdown("Its worse then that, there aint no PlayerManager Jim!", "There was no PlayerManager sent to LMSGameEnder!");
			return null;
		} else
			return playerManager.getGamePlayers();
	}

	
	public LobbyGameManager getLobbyGameManager() {
		return lobbyGameManager;
	}
}
