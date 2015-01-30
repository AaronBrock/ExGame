package me.theminebench.exgame.game.lobbygame.game.sg.templates.weight;

import java.util.UUID;

public interface WeightPlayerDataManager {
	
	public boolean hasPlayer(UUID playersUUID);
	
	public int getMaxWeight(UUID playersUUID);
	
	public int getPlayerWeight(UUID playersUUID);
	
	public void setPlayerWeight(UUID playersUUID, int weight);
	
}
