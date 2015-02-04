package me.theminebench.exgame.game.lobbygame.templates.scoreboard;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class ScoreData {
	private ScoreboardData master;
	
	private Score score;
	
	public ScoreData(ScoreboardData master, String score, int scoreNumber) {
		this.id = id;
		this.master = master;
		Objective o = master.getScoreboard().getObjective(DisplaySlot.SIDEBAR);	
		
		
	}
	
	public void setScore(String scoreDisplayName, int scoreNumber) {
		if (score != null) {
			master.getScoreboard().resetScores(score.getEntry());
		}
		Objective o = master.getObjective();
		score = o.getScore(scoreDisplayName);
		score.setScore(scoreNumber);
	}
	
	
}
