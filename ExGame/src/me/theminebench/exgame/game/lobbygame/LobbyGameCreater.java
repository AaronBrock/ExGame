package me.theminebench.exgame.game.lobbygame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.theminebench.exgame.Arena;
import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.UpdateListener;
import me.theminebench.exgame.Updater;
import me.theminebench.exgame.game.Game;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.utils.ServerUtil;
import me.theminebench.exgame.utils.WorldUtil;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LobbyGameCreater implements Game {
	
	private Arena arena;
	private GameState gameState= GameState.RESTARTING;
	
	private LobbyGame currentLobbyGame;
	
	private LobbyGameFactory lobbyGameFactory;

	private Random random = new Random();

	private LobbyGameTemplate[] lobbyGameTemplates;
	private File lobbyDataFile;
	private File lobbyWorldsFile;
	
	private final String lobbyWorldName = "Lobby";
	
	public LobbyGameCreater(LobbyGameFactory lobbyGameFactory) {
		
		this.lobbyGameFactory = lobbyGameFactory;
		
		this.lobbyDataFile = new File(ExGame.getPlugin().getDataFolder(), "LobbyGame");
		
		this.lobbyWorldsFile = new File(getLobbyDataFile(), "Worlds");
		
		if (!getLobbyWorldsFile().exists()) {
			getLobbyWorldsFile().mkdirs();
			ServerUtil.shutdown("The server is not setup.", "Created files.");
			return;
		}
	}
	
	@Override
	public void start() {
		startNewGame();
	}
	
	public void startNewGame() {
		if (!getGameState().equals(GameState.RESTARTING))
			return;
		
		if (getLobbyWorld() == null)
			WorldUtil.createWorld(new File(getLobbyDataFile(), "Lobby"), getLobbyWorldName());
		

		this.currentLobbyGame = getLobbyGameFactory().getLobbyGame();
		
		if (getCurrentGame() == null) {
			ServerUtil.shutdown("Sorry, the game be NULL matey!", "The game created was NULL");
			return;
		}
		
		getCurrentGame().setLobbyGameCreater(this);
		
		this.lobbyGameTemplates = getCurrentGame().getTemplates();
		
		for (UUID u : getArena().getPlayers()) {
			Player p = Bukkit.getPlayer(u);
			p.teleport(getLobbyWorld().getSpawnLocation());
			p.setFallDistance(0);
			p.setGameMode(GameMode.SURVIVAL);
			((CraftPlayer) p).setHealth(20);
		}
		
			setGameState(GameState.IN_LOBBY);

	}
	
	private File getWorldFile() {
		List<File> possableWorlds = new ArrayList<File>();
		
		for (File f : getLobbyWorldsFile().listFiles()) {
			
			File mapDataFile = new File(f, "mapdata.yml");
			
			if (!mapDataFile.exists())
				continue;
			
			YamlConfiguration mapData = YamlConfiguration.loadConfiguration(mapDataFile);
			
			if (mapData.getStringList("games").contains(getCurrentGame().getName()))
				possableWorlds.add(f);
		}
		
		if (possableWorlds.isEmpty())
			return null;
		
		File file = possableWorlds.get(random.nextInt(possableWorlds.size()));
		
		return file;
	}
	
	
	
	@Override
	public String getName() {
		if (getCurrentGame() != null)
			return getCurrentGame().getName();
		else 
			return "Restarting";
	}
	
	@Override
	public boolean canJoin(UUID playersUUID) {
		for (LobbyGameTemplate template : getTemplates()) {
			if (!template.canJoin(playersUUID)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void playerJoin(UUID playersUUID) {
		for (LobbyGameTemplate template : getTemplates()) {
			template.playerJoin(playersUUID);
		}
		if (getGameState().equals(GameState.IN_LOBBY)) {
			Player p = Bukkit.getPlayer(playersUUID);
			p.teleport(getLobbyWorld().getSpawnLocation());
			p.setFallDistance(0);
			p.setGameMode(GameMode.SURVIVAL);
			((CraftPlayer) p).setHealth(20);
		}
	}

	@Override
	public void playerQuit(UUID playersUUID) {
		for (LobbyGameTemplate template : getTemplates()) {
			template.playerQuit(playersUUID);
		}
	}

	public LobbyGame getCurrentGame() {
		return currentLobbyGame;
	}
	
	public void setGameState(GameState newGameState) {
		GameState oldGameState = getGameState();
		this.gameState = newGameState;
		for (LobbyGameTemplate template : getTemplates()) {
			template.gameStateChange(oldGameState, newGameState);
		}
		if (gameState.equals(GameState.RESTARTING)) {
			for (LobbyGameTemplate template : getTemplates()) {
				HandlerList.unregisterAll(template);
				if (template instanceof UpdateListener) {
					Updater.instance().unregisterListener((UpdateListener) template);
				}
			}
			startNewGame();
		}
		
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	public File getLobbyDataFile() {
		return lobbyDataFile;
	}
	
	public File getLobbyWorldsFile() {
		return lobbyWorldsFile;
	}
	
	public LobbyGameTemplate[] getTemplates() {
		return this.lobbyGameTemplates;
	}
	
	@Override
	public void setArena(Arena arena) {this.arena = arena;}
	
	public Arena getArena() {return arena;}
	
	private LobbyGameFactory getLobbyGameFactory() {
		return lobbyGameFactory;
	}
	
	public void setCurrentLobbyGame(LobbyGame currentLobbyGame) {
		this.currentLobbyGame = currentLobbyGame;
	}
	
	public LobbyGameTemplate[] getLobbyGameTemplates() {
		return lobbyGameTemplates;
	}

	
	public String getLobbyWorldName() {
		return lobbyWorldName;
	}
	
	public World getLobbyWorld() {
		return WorldUtil.getWorld(getLobbyWorldName());
	}
	
	public enum GameState {
		IN_LOBBY,
		PRE_GAME,
		IN_GAME,
		POST_GAME,
		RESTARTING;
		
		public boolean equals(GameState... gameStates) {
			for (GameState gs : gameStates) {
				if (this.equals(gs))
					return true;
			}
			return false;
		}
	}
}
