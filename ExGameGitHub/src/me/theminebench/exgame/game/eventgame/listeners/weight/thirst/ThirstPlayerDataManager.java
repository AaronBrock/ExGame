package me.theminebench.exgame.game.eventgame.listeners.weight.thirst;

import java.util.List;
import java.util.UUID;

public interface ThirstPlayerDataManager {
	
	public List<UUID> getPlayers();
	
	public float getPlayerThirst(UUID playersUUID);
	public void setPlayerThirst(UUID playersUUID, float weight);
	
}
