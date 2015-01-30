package me.theminebench.exgame.game.lobbygame.game.sg;

import me.theminebench.exgame.game.lobbygame.LobbyGameCreater;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.game.sg.templates.SGDataManager;
import me.theminebench.exgame.game.lobbygame.game.sg.templates.chests.ChestsTemplate;
import me.theminebench.exgame.game.lobbygame.game.sg.templates.chests.TestChestManager;
import me.theminebench.exgame.game.lobbygame.game.sg.templates.weight.WeightManager;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.gamedefaults.DefaultLobbyTemplate;
import me.theminebench.exgame.game.lobbygame.templates.gamedefaults.DefaultPostGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.gamedefaults.DefaultPreGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.spawn.DefaultSpawnTemplate;
import me.theminebench.exgame.game.lobbygame.templates.spectate.SpectateManager;

public class SGgame implements LobbyGame {
	
	private LobbyGameCreater lobbyGameCreater;
	
	@Override
	public void setLobbyGameCreater(LobbyGameCreater lobbyGameCreater) {
		this.lobbyGameCreater = lobbyGameCreater;
	}

	@Override
	public LobbyGameCreater getLobbyGameCreater() {
		return this.lobbyGameCreater;
	}

	@Override
	public LobbyGameTemplate[] getTemplates() {
		SpectateManager sm = new SpectateManager(this);
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
