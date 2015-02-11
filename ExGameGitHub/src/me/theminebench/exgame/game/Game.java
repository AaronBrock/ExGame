package me.theminebench.exgame.game;

import java.util.UUID;

import me.theminebench.exgame.Arena;

public interface Game {
	
	public void setArena(Arena arena);
	
	public void start();
	
	public String getName();
	
	public boolean canJoin(UUID playersUUID);
	
	public void playerJoin(UUID playersUUID);
	
	public void playerQuit(UUID playersUUID);
	
}
