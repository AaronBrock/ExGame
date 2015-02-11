package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.scoreboard;

import java.util.HashMap;

import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameEndEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStartEvent;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;

public class ScoreboardManager {
	
	private HashMap<Object, ScoreboardData> scoreboards = new HashMap<Object, ScoreboardData>();
	
	private LobbyGameManager lobbyGameManager;
	
	public ScoreboardManager(LobbyGameManager lobbyGameManager) {
		this.lobbyGameManager = lobbyGameManager;
		lobbyGameManager.registerListener(this);
	}
	
	@GameEventHandler
	public void onStart(GameStartEvent e) {
		lobbyGameManager.fireEvent(this);
	}
	
	@GameEventHandler
	public void onEnd(GameEndEvent e) {
		lobbyGameManager.unregisterListener(this);
	}
	
	public ScoreboardData createScoreboard(Object key) {
		if (!scoreboards.containsKey(key))
			scoreboards.put(key, new ScoreboardData());
		return scoreboards.get(key);
	}
	
	public ScoreboardData getScoreboardData(Object key) {
		return scoreboards.get(key);
	}
	
	public boolean deleteScoreboard(Object key) {
		ScoreboardData sd = scoreboards.get(key);
		if (sd == null)
			return false;
		ScoreboardData sbd = scoreboards.get(key);
		for (Objective o : sbd.getScoreboard().getObjectives()) {
			o.unregister();
		}
		scoreboards.remove(key);
		return true;
	}
	
	public boolean setScoreboard(Player p, Object key) {
		if (!scoreboards.containsKey(key))
			return false;
		p.setScoreboard(scoreboards.get(key).getScoreboard());
		return true;
	}
	
	
}
