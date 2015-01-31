package me.theminebench.exgame.game.lobbygame.templates.scoreboard;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

import me.theminebench.exgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;

public class ScoreboardManager implements LobbyGameTemplate {

	private HashMap<UUID, Scoreboard> playerScoreboards = new HashMap<UUID, Scoreboard>();
	
	private Scoreboard globalScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	
	
	@Override
	public void gameStateChange(GameState oldGameState, GameState newGameState) {
		
		
		
	}
	
	
	
	
	
	@Override
	public boolean canJoin(UUID playersUUID) {
		
		return true;
	}

	@Override
	public void playerJoin(UUID playersUUID) {}

	@Override
	public void playerQuit(UUID playersUUID) {}

}
