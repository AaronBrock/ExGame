package me.theminebench.exgame.game.eventgame.listeners.weight;

import java.util.List;
import java.util.UUID;

public interface WeightPlayerDataManager {
	
	public List<UUID> getPlayers();
	
	public int getMaxWeight(UUID playersUUID);
	
	
	public int getPlayerWeight(UUID playersUUID);
	public void setPlayerWeight(UUID playersUUID, int weight);
}
