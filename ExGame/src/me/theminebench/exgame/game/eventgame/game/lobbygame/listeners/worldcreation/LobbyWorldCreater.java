package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.worldcreation;

import java.io.File;
import java.io.IOException;

import org.bukkit.World;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.GameEventPriority;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameEndEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.utils.ServerUtil;
import me.theminebench.exgame.utils.WorldUtil;

public class LobbyWorldCreater {
	
	private LobbyGameManager lobbyGameManager; 
	
	private final String worldName = "Lobby";
	
	private File saveFile;
	
	public LobbyWorldCreater(LobbyGameManager lobbyGameManager) {
		this.lobbyGameManager = lobbyGameManager;
		this.saveFile = new File(ExGame.getPlugin().getDataFolder(), "Lobby");
		
		if (getSaveFile() == null)
			ServerUtil.shutdown("Scotty beamed you up cause there wern't no world!", "Could not find a world for the game " + getLobbyGameManager().getName());
		
		getLobbyGameManager().registerListener(this);
	}
	
	@GameEventHandler
	public void onEnd(GameEndEvent e) {
		getLobbyGameManager().unregisterListener(this);
	}
	
	@GameEventHandler(priority = GameEventPriority.LOWEST)
	public void onGameState(GameStateChangeEvent e) {
		if (e.getCurrentGameState().equals(GameState.IN_LOBBY)) {
			if (!WorldUtil.isWorldLoaded(getWorldName())) {
				if (WorldUtil.hasWorldFile(getWorldName())) {
					try {
						WorldUtil.deleteFile(WorldUtil.getWorldFile(getWorldName()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				System.out.print("---------we are trying to load the lobby!----------");
				WorldUtil.createWorld(getSaveFile(), getWorldName());
			}
			System.out.print("WE are a moose pice");
			getLobbyGameManager().fireEvent(this);
		}
	}

	
	public String getWorldName() {
		return worldName;
	}
	
	public LobbyGameManager getLobbyGameManager() {
		return lobbyGameManager;
	}
	public File getSaveFile() {
		return saveFile;
	}
	public World getWorld() {
		return WorldUtil.getWorld(getWorldName());
	}
}
