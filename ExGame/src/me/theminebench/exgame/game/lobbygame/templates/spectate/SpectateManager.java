package me.theminebench.exgame.game.lobbygame.templates.spectate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.lobbygame.events.LobbyEventHandler;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.GameStateChangeEvent;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.PlayerJoinArenaEvent;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.PlayerQuitArenaEvent;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SpectateManager implements LobbyGameTemplate {

	List<UUID> spectaters = new ArrayList<UUID>();

	private LobbyGame lobbyGame;
	
	public SpectateManager(LobbyGame lobbyGame) {
		this.lobbyGame = lobbyGame;
		lobbyGame.getLobbyGameManager().registerLobbyListener(this);
	}
	
	public LobbyGame getLobbyGame() {
		return lobbyGame;
	}

	@LobbyEventHandler
	public void gameStateChange(GameStateChangeEvent e) {
		if (e.getCurrentGameState().equals(GameState.PRE_GAME)) {
			Bukkit.getPluginManager().registerEvents(this, ExGame.getPlugin());
		} else if (e.getCurrentGameState().equals(GameState.RESTARTING)) {
			getLobbyGame().getLobbyGameManager().unregisterLobbyListener(this);

			HandlerList.unregisterAll(this);
		}
	}
	

	@LobbyEventHandler
	public void playerJoin(PlayerJoinArenaEvent e) {
		if (getLobbyGame().getLobbyGameManager().getGameState().equals(GameState.PRE_GAME, GameState.POST_GAME, GameState.IN_GAME))
			enableSpectate(e.getPlayersUUID());
	}

	@LobbyEventHandler
	public void playerQuit(PlayerQuitArenaEvent e) {
		disableSpectate(e.getPlayersUUID());
	}
	
	public void enableSpectate(UUID playersUUID) {
		if (!spectaters.contains(playersUUID)) {
			
			Player p = Bukkit.getPlayer(playersUUID);
			
			PlayerEnableSpectateEvent playerSpectateEvent = new PlayerEnableSpectateEvent(p, p.getLocation(), this);
			
			getLobbyGame().getLobbyGameManager().fireEvent(playerSpectateEvent);
			spectaters.add(playersUUID);
			p.setGameMode(GameMode.SPECTATOR);
			p.teleport(playerSpectateEvent.getPlayerTeleportLocation());

		}
	}
	
	public void disableSpectate(UUID playersUUID) {
		if (spectaters.contains(playersUUID)) {
			Player p = Bukkit.getPlayer(playersUUID);
			
			PlayerDisableSpectateEvent playerSpectateEvent = new PlayerDisableSpectateEvent(p, p.getLocation(), GameMode.SURVIVAL, this);
			
			getLobbyGame().getLobbyGameManager().fireEvent(playerSpectateEvent);
			
			spectaters.add(playersUUID);
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
		List<UUID> newPlayerList = new ArrayList<UUID>(getLobbyGame().getLobbyGameManager().getArena().getPlayers());
		newPlayerList.removeAll(getSpectaters());
		return newPlayerList;
	}

	public boolean isSpectating(UUID playersUUID) {
		return spectaters.contains(playersUUID);
	}

	public boolean hasPlayer(UUID playersUUID) {
		return getLobbyGame().getLobbyGameManager().getArena().getPlayers().contains(playersUUID);
	}
}
