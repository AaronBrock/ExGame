package me.theminebench.exgame.game.lobbygame.game.testgame;

import me.theminebench.exgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;

public class TestGame implements LobbyGame {
	
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
	public LobbyGameTemplate[] getTemplates() {
		return new LobbyGameTemplate[]{
			new TestGameTemplate(this)
		};
	}

	@Override
	public String getName() {
		return "test game";
	}

}
