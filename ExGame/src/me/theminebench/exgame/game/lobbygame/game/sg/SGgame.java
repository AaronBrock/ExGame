package me.theminebench.exgame.game.lobbygame.game.sg;

import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.gamestatelisteners.DefaultLobbyTemplate;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.gamestatelisteners.DefaultPostGameTemplate;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.gamestatelisteners.DefaultPreGameTemplate;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spawn.DefaultSpawnTemplate;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spectate.GameModeSpectateManager;
import me.theminebench.exgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.game.sg.templates.SGDataManager;
import me.theminebench.exgame.game.lobbygame.game.sg.templates.chests.ChestsTemplate;
import me.theminebench.exgame.game.lobbygame.game.sg.templates.chests.TestChestManager;
import me.theminebench.exgame.game.lobbygame.game.sg.templates.weight.WeightManager;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;

public class SGgame implements LobbyGame {
	
	private LobbyGameManager lobbyGameCreater;
	
	@Override
	public void setLobbyGameManager(LobbyGameManager lobbyGameCreater) {
		this.lobbyGameCreater = lobbyGameCreater;
	}

	@Override
	public LobbyGameManager getLobbyGameManager() {
		return this.lobbyGameCreater;
	}

	@Override
	public LobbyGameTemplate[] getTemplates() {
		GameModeSpectateManager sm = new GameModeSpectateManager(this);
		SGDataManager sdm = new SGDataManager(this, sm);
		WeightManager wm = new WeightManager(this, sdm, sdm);
		return new LobbyGameTemplate[]{sm, sdm, wm,
				new ChestsTemplate(this, new TestChestManager(sm)),
				new DefaultSpawnTemplate(this),
				new DefaultLobbyTemplate(this),
				new DefaultPreGameTemplate(this),
				new DefaultPostGameTemplate(this)
				};
	}

	@Override
	public String getName() {
		return "sg";
	}

	@Override
	public int getMaxPlayers() {
		return 10;
	}

	@Override
	public int getMinPlayers() {
		return 1;
	}
}
