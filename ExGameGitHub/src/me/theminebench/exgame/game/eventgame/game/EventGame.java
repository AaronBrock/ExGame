package me.theminebench.exgame.game.eventgame.game;

import me.theminebench.exgame.game.eventgame.EventGameManager;


public interface EventGame {
	
	public void setEventGame(EventGameManager eventGameManager);
	
	public void start();
	
	public String getName();
	
}
