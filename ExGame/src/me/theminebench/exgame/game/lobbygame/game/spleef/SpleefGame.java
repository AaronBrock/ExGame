package me.theminebench.exgame.game.lobbygame.game.spleef;

import java.util.UUID;

import me.theminebench.exgame.game.lobbygame.LobbyGameCreater;
import me.theminebench.exgame.game.lobbygame.LobbyGameCreater.GameState;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.countdowns.LobbyCountdown;
import me.theminebench.exgame.game.lobbygame.templates.countdowns.PostGameCountdown;
import me.theminebench.exgame.game.lobbygame.templates.countdowns.PreGameCountdown;
import me.theminebench.exgame.game.lobbygame.templates.gamedefaults.DefaultLobbyTemplate;
import me.theminebench.exgame.game.lobbygame.templates.gamedefaults.DefaultPostGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.gamedefaults.DefaultPreGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.spawn.DefaultSpawnTemplate;
import me.theminebench.exgame.game.lobbygame.templates.spectate.SpectateListener;
import me.theminebench.exgame.game.lobbygame.templates.spectate.SpectateManager;
import me.theminebench.exgame.game.lobbygame.templates.worldcreation.WorldCreationTemplate;

public class SpleefGame implements LobbyGame, SpectateListener {
	
	private SpectateManager spectateManager;
	
	private SpleefTemplate spleefTemplate;
	
	private LobbyGameCreater lobbyGameCreater;
	
	private DefaultSpawnTemplate defaultSpawnTemplate;
	
	private WorldCreationTemplate worldCreationTemplate;
	
	@Override
	public void setLobbyGameCreater(LobbyGameCreater lobbyGameCreater) {
		this.lobbyGameCreater = lobbyGameCreater;
	}

	@Override
	public LobbyGameCreater getLobbyGameCreater() {
		return lobbyGameCreater;
	}

	@Override
	public LobbyGameTemplate[] getTemplates() {
		
		worldCreationTemplate = new WorldCreationTemplate(this);
		
		spectateManager = new SpectateManager(this, worldCreationTemplate, this);
		
		spleefTemplate = new SpleefTemplate(this, spectateManager, worldCreationTemplate);
		
		defaultSpawnTemplate = new DefaultSpawnTemplate(this);
		
		return new LobbyGameTemplate[] {
			new LobbyCountdown(getLobbyGameCreater(), 1, 15),
			new PreGameCountdown(getLobbyGameCreater()),
			new PostGameCountdown(getLobbyGameCreater()),
			worldCreationTemplate,
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

	@Override
	public void enabledSpectate(UUID playersUUID) {
		checkGameEnd();
		
	}
	
	public void checkGameEnd() {
		if (spectateManager.getPlayers().size() <= 1) {
			getLobbyGameCreater().setGameState(GameState.POST_GAME);
		}
	}
	
	@Override
	public void disabledSpectate(UUID playersUUID) {
	}

	public DefaultSpawnTemplate getDefaultSpawnTemplate() {
		return defaultSpawnTemplate;
	}

}
