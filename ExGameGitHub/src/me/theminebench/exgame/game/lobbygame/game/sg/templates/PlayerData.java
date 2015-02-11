package me.theminebench.exgame.game.lobbygame.game.sg.templates;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerData {
	
	private int weight;
	private UUID playersUUID;
	
	
	//This is just notes
	
	
	
	public PlayerData(UUID playersUUID) {
		
	}
	
	public UUID getPlayersUUID() {
		return playersUUID;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(getPlayersUUID());
	}
	
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
}