package me.theminebench.exgame.game.lobbygame.templates.worldcreation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.lobbygame.events.LobbyEventHandler;
import me.theminebench.exgame.game.lobbygame.events.LobbyEventPriority;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.utils.ServerUtil;
import me.theminebench.exgame.utils.WorldUtil;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class WorldCreationTemplate implements WorldManager {
	
	private String worldName = "Game";
	private LobbyGame lobbyGame;
	private File saveFile;
	
	public WorldCreationTemplate(LobbyGame lobbyGame) {
		this.lobbyGame = lobbyGame;
		this.saveFile = getWorldFile();
		if (saveFile == null)
			ServerUtil.shutdown("Scotty beamed you up cause there wern't no world!", "Could not find a world for the game " + lobbyGame.getName());
		
		getLobbyGame().getLobbyGameManager().getLobbyEventManager().registerLobbyListener(this);
	}
	
	
	@LobbyEventHandler(priority=LobbyEventPriority.HIGHEST)
	public void gameStateChange(GameStateChangeEvent e) {
		
		if (e.getCurrentGameState().equals(GameState.IN_LOBBY)) {
			WorldUtil.deleteWorld(worldName, new Runnable() {
				@Override
				public void run() {
					createWorld();
				}
			});

		} else if (e.getCurrentGameState().equals(GameState.RESTARTING)) {
			getLobbyGame().getLobbyGameManager().getLobbyEventManager().unregisterLobbyListener(this);
		}
	}
	
	private void createWorld() {
		WorldUtil.createWorld(getSaveFile(), getWorldName());
		
		getLobbyGame().getLobbyGameManager().getLobbyEventManager().fireEvent(this);
	}
	
	private File getWorldFile() {
		List<File> possableWorlds = new ArrayList<File>();
		
		for (File f : new File(lobbyGame.getLobbyGameManager().getLobbyDataFile(), "Worlds").listFiles()) {
			
			File mapDataFile = new File(f, "mapdata.yml");
			
			if (!mapDataFile.exists())
				continue;
			
			YamlConfiguration mapData = YamlConfiguration.loadConfiguration(mapDataFile);
			
			if (mapData.getStringList("games").contains(lobbyGame.getName()))
				possableWorlds.add(f);
		}
		
		if (possableWorlds.isEmpty())
			return null;
		//TODO Share the randomness you fool!
		File file = possableWorlds.get(new Random().nextInt(possableWorlds.size()));
		
		return file;
	}
	
	public YamlConfiguration getMapData() {
		return YamlConfiguration.loadConfiguration(new File(getSaveFile(), "mapdata.yml"));
	}
	
	public File getSaveFile() {
		return saveFile;
	}
	public String getWorldName() {
		return worldName;
	}
	
	public World getGameWorld() {
		return WorldUtil.getWorld(worldName);
	}
	
	public LobbyGame getLobbyGame() {
		return lobbyGame;
	}
	

}
