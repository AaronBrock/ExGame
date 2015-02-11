package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.scoreboard;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardData {
	
	private Scoreboard scoreboard;
	private HashMap<String, Score> scores = new HashMap<String, Score>();
	
	public ScoreboardData() {
		this("");
	}
	
	public ScoreboardData(String displayName) {
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective o = getScoreboard().registerNewObjective(displayName, "dummy");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public void setScore(String id, String scoreName, int scoreNumber) {
		if (scores.get(id) != null) {
			getScoreboard().resetScores(scores.get(id).getEntry());
		}
		Score score = getObjective().getScore(scoreName);
		
		score.setScore(scoreNumber);
		
		scores.put(id, score);
	}

	public void removeScore(String id) {
		if (scores.get(id) == null)
			return;
		getScoreboard().resetScores(scores.get(id).getEntry());
		scores.remove(id);
	}
	
	protected Scoreboard getScoreboard() {
		return scoreboard;
	}
	
	public void setDisplayName(String name) {
		getObjective().setDisplayName(name);
	}
	
	public String getDisplayName() {
		return getObjective().getDisplayName();
	}
	
	protected Objective getObjective() {
		return getScoreboard().getObjective(DisplaySlot.SIDEBAR);
	}
}
