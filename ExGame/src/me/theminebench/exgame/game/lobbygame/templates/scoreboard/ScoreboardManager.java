package me.theminebench.exgame.game.lobbygame.templates.scoreboard;

import java.util.HashMap;
import java.util.UUID;

import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreboardManager implements LobbyGameTemplate {

	private HashMap<UUID, ScoreboardData> playerScoreboards = new HashMap<UUID, ScoreboardData>();
	
	private ScoreboardData globalScoreboard = new ScoreboardData();
	
	public ScoreboardManager(LobbyGame lobbyGame) {
		
	}
	
	public ScoreboardData checkScoreboard(UUID playersUUID) {
		if (playerScoreboards.get(playersUUID) == null) {
			playerScoreboards.put(playersUUID, new ScoreboardData());
		}
		return playerScoreboards.get(playersUUID);
	}
	
	public void setScore(UUID playersUUID, String id, String scoreName, int scoreNumber) {
		ScoreboardData sd = checkScoreboard(playersUUID);
		sd.setScore(id, scoreName, scoreNumber);
	}
	
	public void removePlayerScoreboard(UUID playersUUID) {
		
		if (playerScoreboards.get(playersUUID) == null)
			return;
		Player p = Bukkit.getPlayer(playersUUID);
		
		if (p.getScoreboard() == playerScoreboards.get(playersUUID).getScoreboard()) {
			setScoreboard(playersUUID, ScoreboardType.NO_SCOREBOARD);
		}
		
		playerScoreboards.remove(playersUUID);
	}
	
	
	public void setScoreboard(UUID playersUUID, ScoreboardType st) {
		Player p = Bukkit.getPlayer(playersUUID);
		
		switch (st) {
		case ARENA_SCOREBOARD:
			p.setScoreboard(globalScoreboard.getScoreboard());
			return;
		case NO_SCOREBOARD:
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			return;
		case PLAYER_SCOREBOARD:
			p.setScoreboard(checkScoreboard(playersUUID).getScoreboard());
			return;
		}
	}
	
	
	public enum ScoreboardType {
		PLAYER_SCOREBOARD,
		ARENA_SCOREBOARD,
		NO_SCOREBOARD;
	}
}
