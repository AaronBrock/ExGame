package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults;

import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.countdowns.LobbyCountdown;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.countdowns.PostGameCountdown;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.countdowns.PreGameCountdown;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.gamestatelisteners.DefaultLobbyTemplate;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.gamestatelisteners.DefaultPostGameTemplate;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.gamestatelisteners.DefaultPreGameTemplate;

public class DefaultsUtil {
	private DefaultsUtil() {
		
	}
	
	public static void loadAllDefaults(LobbyGameManager lobbyGameManager, int minPlayers, int maxPlayers) {
		loadCountdownDefaults(lobbyGameManager, minPlayers, maxPlayers);
		loadGameStateDefaults(lobbyGameManager);
	}
	
	public static void loadCountdownDefaults(LobbyGameManager lobbyGameManager, int minPlayers, int maxPlayers) {
		new LobbyCountdown(lobbyGameManager, minPlayers, maxPlayers);
		new PreGameCountdown(lobbyGameManager);
		new PostGameCountdown(lobbyGameManager);
	}
	
	public static void loadGameStateDefaults(LobbyGameManager lobbyGameManager) {
		new DefaultLobbyTemplate(lobbyGameManager);
		new DefaultPreGameTemplate(lobbyGameManager);
		new DefaultPostGameTemplate(lobbyGameManager);
	}
}
