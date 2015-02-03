package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spectate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.events.PlayerJoinArenaEvent;
import me.theminebench.exgame.game.eventgame.events.PlayerQuitArenaEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStartEvent;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameModeSpectateManager implements SpectateManager, Listener {

	List<UUID> spectaters = new ArrayList<UUID>();

	private LobbyGameManager lobbyGameManager;
	
	public GameModeSpectateManager(LobbyGameManager lobbyGameManager) {
		this.lobbyGameManager = lobbyGameManager;
		getLobbyGameManager().registerListener(this);
		Bukkit.getPluginManager().registerEvents(this, ExGame.getPlugin());
	}
	
	@GameEventHandler
	public void onStart(GameStartEvent e) {
		getLobbyGameManager().fireEvent(this);
	}
	
	@GameEventHandler
	public void onEnd() {
		getLobbyGameManager().unregisterListener(this);

		HandlerList.unregisterAll(this);
	}
	
	
	
	@GameEventHandler
	public void playerJoin(PlayerJoinArenaEvent e) {		
		if (getLobbyGameManager().getGameState().equals(GameState.PRE_GAME, GameState.POST_GAME, GameState.IN_GAME))
			enableSpectate(e.getPlayersUUID());
	}

	@GameEventHandler
	public void playerQuit(PlayerQuitArenaEvent e) {
		disableSpectate(e.getPlayersUUID());
	}
	
	public void enableSpectate(UUID playersUUID) {
		if (!spectaters.contains(playersUUID)) {
			
			Player p = Bukkit.getPlayer(playersUUID);
			
			PlayerEnableSpectateEvent playerSpectateEvent = new PlayerEnableSpectateEvent(p, p.getLocation(), this);
			
			spectaters.add(playersUUID);
			getLobbyGameManager().fireEvent(playerSpectateEvent);
			p.setGameMode(GameMode.SPECTATOR);
			p.teleport(playerSpectateEvent.getPlayerTeleportLocation());

		}
	}
	
	
	public void disableSpectate(UUID playersUUID) {
		if (spectaters.contains(playersUUID)) {
			Player p = Bukkit.getPlayer(playersUUID);
			
			PlayerDisableSpectateEvent playerSpectateEvent = new PlayerDisableSpectateEvent(p, p.getLocation(), GameMode.SURVIVAL, this);
			
			spectaters.add(playersUUID);
			getLobbyGameManager().fireEvent(playerSpectateEvent);
			p.setGameMode(playerSpectateEvent.getGameMode());
			p.teleport(playerSpectateEvent.getPlayerTeleportLocation());

		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.getEntity().setHealth(((CraftPlayer)e.getEntity()).getMaxHealth());
		if (hasPlayer(e.getEntity().getUniqueId())) {
			enableSpectate(e.getEntity().getUniqueId());
		}
	}
	
	public List<UUID> getSpectaters() {
		return new ArrayList<UUID>(this.spectaters);
	}
	
	
	public List<UUID> getPlayers() {
		return getLobbyGameManager().getPlayers();
	}

	public boolean isSpectating(UUID playersUUID) {
		return spectaters.contains(playersUUID);
	}

	public boolean hasPlayer(UUID playersUUID) {
		return getPlayers().contains(playersUUID);
	}
	
	public boolean hasGamePlayer(UUID playersUUID) {
		return getGamePlayers().contains(playersUUID);
	}
	
	@Override
	public List<UUID> getGamePlayers() {
		List<UUID> newPlayerList = new ArrayList<UUID>(getPlayers());
		newPlayerList.removeAll(getSpectaters());
		return newPlayerList;
	}	
	
	public LobbyGameManager getLobbyGameManager() {
		return lobbyGameManager;
	}

}
