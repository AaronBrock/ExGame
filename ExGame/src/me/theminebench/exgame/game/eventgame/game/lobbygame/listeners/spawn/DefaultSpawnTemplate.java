package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spawn;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameEndEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.worldcreation.GameWorldCreater;
import me.theminebench.exgame.utils.LocationUtils;
import me.theminebench.exgame.utils.PlayerUtil;
import me.theminebench.exgame.utils.ServerUtil;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class DefaultSpawnTemplate {
	
	private List<Location> locations = new ArrayList<Location>();
	
	private LobbyGameManager lobbyGameManager;
	
	private int counter = 0;
	
	public DefaultSpawnTemplate(LobbyGameManager lobbyGameManager) {
		this.lobbyGameManager = lobbyGameManager;
		getLobbyGameManager().registerListener(this);
	}
	
	@GameEventHandler
	public void gameStateChange(GameStateChangeEvent e) {
		if (e.getCurrentGameState().equals(GameState.PRE_GAME)) {
			for (UUID u : getLobbyGameManager().getPlayers()) {
				sendPlayerToSpawn(u);
			}
		}
	}
	
	@GameEventHandler
	public void endGame(GameEndEvent e) {
		getLobbyGameManager().unregisterListener(this);
	}
	
	public List<Location> getSpawns() {
		return locations;
	}
	
	public void sendPlayerToSpawn(UUID playersUUID) {
		Bukkit.getPlayer(playersUUID).teleport(locations.get(counter));
		Player p = Bukkit.getPlayer(playersUUID);
		p.setFallDistance(0);
		p.setFireTicks(0);
		PlayerUtil.resetMaxHealth(playersUUID);
		PlayerUtil.resetMaxHunger(playersUUID);
		p.setGameMode(GameMode.SURVIVAL);
		counter++;
		if (counter == locations.size())
			counter = 0;
	}
	
	@GameEventHandler
	public void worldCreated(GameWorldCreater e) {
		
		System.out.print("-----------------------I AM HERE----------------------");
		for (String stringLocation : e.getMapData().getStringList("spawnpoints")) {
			
			Location l = LocationUtils.toLocation(e.getWorldName(), stringLocation);
			if (l != null)
				locations.add(l);
			
		}
		
		if (locations.isEmpty()) {
			ServerUtil.shutdown("Scotty did not know where to beam you down to!", "There where no valid spawnpoints found for the map " + e.getWorldName() + "!");
			return;
		}
	}
	
	public LobbyGameManager getLobbyGameManager() {
		return lobbyGameManager;
	}
}
