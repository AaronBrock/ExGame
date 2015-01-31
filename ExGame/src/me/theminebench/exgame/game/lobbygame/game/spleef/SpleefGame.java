package me.theminebench.exgame.game.lobbygame.game.spleef;

import java.util.UUID;

import me.theminebench.exgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.lobbygame.events.LobbyEventHandler;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.countdowns.LobbyCountdown;
import me.theminebench.exgame.game.lobbygame.templates.countdowns.PostGameCountdown;
import me.theminebench.exgame.game.lobbygame.templates.countdowns.PreGameCountdown;
import me.theminebench.exgame.game.lobbygame.templates.gamedefaults.DefaultLobbyTemplate;
import me.theminebench.exgame.game.lobbygame.templates.gamedefaults.DefaultPostGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.gamedefaults.DefaultPreGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.spawn.DefaultSpawnTemplate;
import me.theminebench.exgame.game.lobbygame.templates.spectate.PlayerEnableSpectateEvent;
import me.theminebench.exgame.game.lobbygame.templates.spectate.SpectateManager;
import me.theminebench.exgame.game.lobbygame.templates.worldcreation.WorldCreationTemplate;

public class SpleefGame implements LobbyGame, LobbyGameTemplate {
	
	private SpectateManager spectateManager;
	
	private SpleefTemplate spleefTemplate;
	
	private LobbyGameManager lobbyGameCreater;
	
	private DefaultSpawnTemplate defaultSpawnTemplate;
	
	@Override
	public void setLobbyGameManager(LobbyGameManager lobbyGameCreater) {
		this.lobbyGameCreater = lobbyGameCreater;
	}

	@Override
	public LobbyGameManager getLobbyGameManager() {
		return lobbyGameCreater;
	}

	@Override
	public LobbyGameTemplate[] getTemplates() {
		getLobbyGameManager().registerLobbyListener(this);
		spectateManager = new SpectateManager(this);
		
		spleefTemplate = new SpleefTemplate(this, spectateManager);
		
		defaultSpawnTemplate = new DefaultSpawnTemplate(this);
		
		return new LobbyGameTemplate[] {
				this,
				new WorldCreationTemplate(this),
				new LobbyCountdown(getLobbyGameManager(), 1, 15),
				new PreGameCountdown(getLobbyGameManager()),
				new PostGameCountdown(getLobbyGameManager()),
				spectateManager,
				spleefTemplate,
				defaultSpawnTemplate,
				new DefaultLobbyTemplate(this),
				new DefaultPreGameTemplate(this),
				new DefaultPostGameTemplate(this)};
	}

	@Override
	public String getName() {
		
		return "spleef";
	}
	
	@LobbyEventHandler
	public void enabledSpectate(PlayerEnableSpectateEvent e) {
		checkGameEnd();
		
	}
	
	public void checkGameEnd() {
		if (spectateManager.getPlayers().size() <= 1) {
			getLobbyGameManager().setGameState(GameState.POST_GAME);
		}
	}

	public DefaultSpawnTemplate getDefaultSpawnTemplate() {
		return defaultSpawnTemplate;
	}

}
