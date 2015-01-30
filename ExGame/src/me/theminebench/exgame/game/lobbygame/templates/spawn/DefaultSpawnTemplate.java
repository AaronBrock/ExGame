package me.theminebench.exgame.game.lobbygame.templates.spawn;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.theminebench.exgame.game.lobbygame.LobbyGameCreater.GameState;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.worldcreation.WorldCreationTemplate;
import me.theminebench.exgame.game.lobbygame.templates.worldcreation.WorldCreatorListener;
import me.theminebench.exgame.utils.LocationUtils;
import me.theminebench.exgame.utils.ServerUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class DefaultSpawnTemplate implements LobbyGameTemplate, WorldCreatorListener {
	
	private List<Location> locations = new ArrayList<Location>();
	
	private LobbyGame lobbyGame;
	
	private int counter = 0;
	
	public DefaultSpawnTemplate(LobbyGame lobbyGame) {
		this.lobbyGame = lobbyGame;
	}
	
	public LobbyGame getLobbyGame() {
		return this.lobbyGame;
	}
	
	@Override
	public void gameStateChange(GameState oldGameState, GameState newGameState) {
		if (newGameState.equals(GameState.PRE_GAME)) {
			for (UUID u : getLobbyGame().getLobbyGameCreater().getArena().getPlayers()) {
				sendPlayerToSpawn(u);
			}
		}
	}

	@Override
	public boolean canJoin(UUID playersUUID) {
		return true;
	}

	@Override
	public void playerJoin(UUID playersUUID) {
	}

	@Override
	public void playerQuit(UUID playersUUID) {
	}
	
	public List<Location> getSpawns() {
		return locations;
	}
	
	public void sendPlayerToSpawn(UUID playersUUID) {
		System.out.print("SIZE = " + getSpawns().size());
		Bukkit.getPlayer(playersUUID).teleport(locations.get(counter));
		counter++;
		if (counter == locations.size())
			counter = 0;
	}

	@Override
	public void worldCreated(World world, YamlConfiguration mapData, WorldCreationTemplate worldCreationTemplate) {

		for (String stringLocation : mapData.getStringList("spawnpoints")) {
			System.out.println(" - - - - -");
			System.out.println(stringLocation);
			
			Location l = LocationUtils.toLocation(world.getName(), stringLocation);
			if (l != null)
				locations.add(l);
			else
				System.out.println("That one ins null!");
			System.out.println(" - - - - - ");
		}
		System.out.print("SIZE = " + getSpawns().size());
		if (locations.isEmpty()) {
			ServerUtil.shutdown("Scotty did not know where to beam you down to!", "There where no valid spawnpoints found for the map " + worldCreationTemplate.getWorldName() + "!");
			return;
		}	
		
	}
}
