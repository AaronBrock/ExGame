package me.theminebench.exgame.game.lobbygame.templates.spawn;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.theminebench.exgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.lobbygame.events.LobbyEventHandler;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.GameStateChangeEvent;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.worldcreation.WorldCreateEvent;
import me.theminebench.exgame.utils.LocationUtils;
import me.theminebench.exgame.utils.PlayerUtil;
import me.theminebench.exgame.utils.ServerUtil;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DefaultSpawnTemplate implements LobbyGameTemplate {
	
	private List<Location> locations = new ArrayList<Location>();
	
	private LobbyGame lobbyGame;
	
	private int counter = 0;
	
	public DefaultSpawnTemplate(LobbyGame lobbyGame) {
		this.lobbyGame = lobbyGame;
		lobbyGame.getLobbyGameManager().registerLobbyListener(this);
	}
	
	public LobbyGame getLobbyGame() {
		return this.lobbyGame;
	}
	
	@LobbyEventHandler
	public void gameStateChange(GameStateChangeEvent e) {
		if (e.getCurrentGameState().equals(GameState.PRE_GAME)) {
			for (UUID u : getLobbyGame().getLobbyGameManager().getArena().getPlayers()) {
				sendPlayerToSpawn(u);
			}
		} else if (e.getCurrentGameState().equals(GameState.RESTARTING)) {
			getLobbyGame().getLobbyGameManager().unregisterLobbyListener(this);

		}
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

	@LobbyEventHandler
	public void worldCreated(WorldCreateEvent e) {
		for (String stringLocation : e.getMapData().getStringList("spawnpoints")) {
			//System.out.println(" - - - - -");
			//System.out.println(stringLocation);
			
			Location l = LocationUtils.toLocation(e.getWorld().getName(), stringLocation);
			if (l != null)
				locations.add(l);
			//else
				//System.out.println("That one ins null!");
			//System.out.println(" - - - - - ");
		}
		System.out.print("SIZE = " + getSpawns().size());
		if (locations.isEmpty()) {
			ServerUtil.shutdown("Scotty did not know where to beam you down to!", "There where no valid spawnpoints found for the map " + e.getWorldCreationTemplate().getWorldName() + "!");
			return;
		}	
		
	}
}
