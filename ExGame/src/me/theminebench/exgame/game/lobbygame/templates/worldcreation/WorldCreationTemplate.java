package me.theminebench.exgame.game.lobbygame.templates.worldcreation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.theminebench.exgame.game.lobbygame.LobbyGameCreater.GameState;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.utils.ServerUtil;
import me.theminebench.exgame.utils.WorldUtil;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class WorldCreationTemplate implements LobbyGameTemplate, WorldMannager {
	
	private String worldName = "Game";
	private LobbyGame lobbyGame;
	private File saveFile;
	
	private HashSet<WorldCreatorListener> listeners = new HashSet<WorldCreatorListener>();
	
	public WorldCreationTemplate(LobbyGame lobbyGame) {
		this.lobbyGame = lobbyGame;
		this.saveFile = getWorldFile();
		if (saveFile == null)
			ServerUtil.shutdown("Scotty beamed you up cause there wern't no world!", "Could not find a world for the game " + lobbyGame.getName());
	}
	
	public boolean registerListener(WorldCreatorListener wcl) {
		return listeners.add(wcl);
	}
	
	@Override
	public void gameStateChange(GameState oldGameState, GameState newGameState) {
		if (newGameState.equals(GameState.IN_LOBBY)) {
			if (WorldUtil.isWorldLoaded(worldName)) {
				WorldUtil.deleteWorld(worldName, new Runnable() {
					@Override
					public void run() {
						createWorld();
					}
				});
			} else {
				if (WorldUtil.hasWorldFile(worldName)) {
					try {
						WorldUtil.deleteFile(WorldUtil.getWorldFile(worldName));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				createWorld();
			}
		}
	}
	
	private void createWorld() {
		World world = WorldUtil.createWorld(getSaveFile(), getWorldName());
		
		System.out.print("we are here!");
		
		for (LobbyGameTemplate lgt : lobbyGame.getLobbyGameCreater().getTemplates()) {
			System.out.print("we are now here!");
			if (lgt instanceof WorldCreatorListener){
				System.out.print("we even got to here!");
				WorldCreatorListener wcl = (WorldCreatorListener) lgt;
				if (!listeners.contains(wcl))
					wcl.worldCreated(world, getMapData(), this);
			}
		}
		
		for (WorldCreatorListener wcl : listeners) {
			wcl.worldCreated(world, getMapData(), this);
		}
	}
	
	private File getWorldFile() {
		List<File> possableWorlds = new ArrayList<File>();
		
		for (File f : new File(lobbyGame.getLobbyGameCreater().getLobbyDataFile(), "Worlds").listFiles()) {
			
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
	
	@Override
	public boolean canJoin(UUID playersUUID) {return true;}
	@Override
	public void playerJoin(UUID playersUUID) {}
	@Override
	public void playerQuit(UUID playersUUID) {}

}
