package me.theminebench.exgame.game.eventgame.game.lobbygame.game.spleef;

import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.DefaultsUtil;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.gameenders.LMSGameEnder;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spawn.DefaultSpawnTemplate;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spectate.GameModeSpectateManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.worldcreation.GameWorldCreater;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.worldcreation.LobbyWorldCreater;


public class SpleefGame implements LobbyGame {
	
	private LobbyGameManager lobbyGameManager;
	
	
	
	@Override
	public void setLobbyGameManager(LobbyGameManager lobbyGameManager) {
		this.lobbyGameManager = lobbyGameManager;
	}
	
	@Override
	public LobbyGameManager getLobbyGameManager() {
		return lobbyGameManager;
	}
	
	@Override
	public String getName() {
		return "spleef";
	}
	
	@Override
	public void start() {
		DefaultsUtil.loadAllDefaults(getLobbyGameManager(), 1, 10);
		new DefaultSpawnTemplate(getLobbyGameManager());
		new GameModeSpectateManager(getLobbyGameManager());
		new GameWorldCreater(getLobbyGameManager());
		new LobbyWorldCreater(getLobbyGameManager());
		new LMSGameEnder(lobbyGameManager);
		new SpleefListener(this);
	}

}
