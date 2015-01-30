package me.theminebench.exgame.game.lobbygame.templates.spectate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.UpdateListener;
import me.theminebench.exgame.game.lobbygame.LobbyGameCreater.GameState;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.worldcreation.WorldMannager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SpectateManager implements LobbyGameTemplate {

	List<UUID> spectaters = new ArrayList<UUID>();

	private LobbyGame lobbyGame;

	private HashSet<SpectateListener> listeners = new HashSet<SpectateListener>();
	
	private WorldMannager worldMannager;
	
	public SpectateManager(LobbyGame lobbyGame, WorldMannager worldMannager, SpectateListener... listeners) {
		this.lobbyGame = lobbyGame;
		this.worldMannager = worldMannager;
		for (SpectateListener sl : listeners) {
			this.listeners.add(sl);
		}
	}
	

	public boolean isRegistered(SpectateListener spectateListener) {
		return listeners.contains(spectateListener);
	}
	
	public void registerListener(SpectateListener spectateListener) {
		listeners.add(spectateListener);
	}

	public void unregisterListener(UpdateListener updateReader) {
		listeners.remove(updateReader);
	}
	
	
	public LobbyGame getLobbyGame() {
		return lobbyGame;
	}

	@Override
	public void gameStateChange(GameState oldGameState, GameState newGameState) {
		if (newGameState.equals(GameState.PRE_GAME)) {
			Bukkit.getPluginManager().registerEvents(this, ExGame.getPlugin());
		} else if(oldGameState.equals(GameState.POST_GAME)) {
			HandlerList.unregisterAll(this);
		}
	}

	@Override
	public boolean canJoin(UUID playersUUID) {
		return true;
	}
	
	@Override
	public void playerJoin(UUID playersUUID) {
		if (getLobbyGame().getLobbyGameCreater().getGameState().equals(GameState.PRE_GAME, GameState.POST_GAME, GameState.IN_GAME))
			enableSpectate(playersUUID);
	}

	@Override
	public void playerQuit(UUID playersUUID) {
		disableSpectate(playersUUID);
	}
	//TODO get location from mapdata.yml
	public void enableSpectate(UUID playersUUID) {
		if (!spectaters.contains(playersUUID)) {
			spectaters.add(playersUUID);
			Player p = Bukkit.getPlayer(playersUUID);
			p.setGameMode(GameMode.SPECTATOR);
			p.teleport(worldMannager.getGameWorld().getSpawnLocation());
			if (!listeners.isEmpty())
				for (SpectateListener sl : listeners) {
					sl.enabledSpectate(playersUUID);
				}
		}
	}
	public void disableSpectate(UUID playersUUID) {
		
		if (spectaters.contains(playersUUID)) {
			spectaters.remove(playersUUID);
			Player p = Bukkit.getPlayer(playersUUID);
			p.setGameMode(GameMode.SURVIVAL);
			if (!listeners.isEmpty())
				for (SpectateListener sl : listeners) {
					sl.disabledSpectate(playersUUID);
				}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Bukkit.broadcastMessage("this is your captain sneaking!");
		e.getEntity().setHealth(20.);
		if (hasPlayer(e.getEntity().getUniqueId())) {
			enableSpectate(e.getEntity().getUniqueId());
		}
	}
	
	public List<UUID> getSpectaters() {
		return new ArrayList<UUID>(this.spectaters);
	}

	public List<UUID> getPlayers() {
		List<UUID> newPlayerList = new ArrayList<UUID>(getLobbyGame().getLobbyGameCreater().getArena().getPlayers());
		newPlayerList.removeAll(getSpectaters());
		return newPlayerList;
	}

	public boolean isSpectating(UUID playersUUID) {
		return spectaters.contains(playersUUID);
	}

	public boolean hasPlayer(UUID playersUUID) {
		return getLobbyGame().getLobbyGameCreater().getArena().getPlayers().contains(playersUUID);
	}
}
