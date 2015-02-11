package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.worldcreation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.GameEventPriority;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameEndEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.utils.RandomUtil;
import me.theminebench.exgame.utils.ServerUtil;
import me.theminebench.exgame.utils.WorldUtil;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class GameWorldCreater {
	
	private LobbyGameManager lobbyGameManager; 
	
	private final String worldName = "Game";
	
	private File saveFile;
	
	public GameWorldCreater(LobbyGameManager lobbyGameManager) {
		this.lobbyGameManager = lobbyGameManager;
		this.saveFile = getWorldFile();
		
		if (getSaveFile() == null)
			ServerUtil.shutdown("Scotty beamed you up cause there wern't no world!", "Could not find a world for the game " + getLobbyGameManager().getName());
		
		getLobbyGameManager().registerListener(this);
	}
	
	@GameEventHandler
	public void onEnd(GameEndEvent e) {
		getLobbyGameManager().unregisterListener(this);
	}
	
	@GameEventHandler(priority = GameEventPriority.HIGHEST)
	public void onGameState(GameStateChangeEvent e) {
		if (e.getCurrentGameState().equals(GameState.IN_LOBBY)) {
			WorldUtil.deleteWorld(getWorldName(), new Runnable() {
				@Override
				public void run() {
					WorldUtil.createWorld(getSaveFile(), getWorldName());
					fireEvent();
					//getLobbyGameManager().fireEvent(this);
				}
			});
		}
	}
	
	private void fireEvent() {
		getLobbyGameManager().fireEvent(this);
	}
	
	private File getWorldFile() {
		List<File> possibleWorlds = new ArrayList<File>();
		
		for (File f : new File(ExGame.getPlugin().getDataFolder(), "Worlds" ).listFiles()) {
			
			File mapDataFile = new File(f, "mapdata.yml");
			
			if (!mapDataFile.exists())
				continue;
			
			YamlConfiguration mapData = YamlConfiguration.loadConfiguration(mapDataFile);
			
			if (mapData.getStringList("games").contains(getLobbyGameManager().getName()))
				possibleWorlds.add(f);
		}
		
		if (possibleWorlds.isEmpty())
			return null;
		
		File file = possibleWorlds.get(RandomUtil.getRandom().nextInt(possibleWorlds.size()));
		
		return file;
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
	public YamlConfiguration getMapData() {
		return YamlConfiguration.loadConfiguration(new File(getSaveFile(), "mapdata.yml"));
	}
}
