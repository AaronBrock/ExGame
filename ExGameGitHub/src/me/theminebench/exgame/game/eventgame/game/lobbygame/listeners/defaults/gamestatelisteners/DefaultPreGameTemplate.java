package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.defaults.gamestatelisteners;

import java.util.List;
import java.util.UUID;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameEndEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.PlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class DefaultPreGameTemplate implements Listener {
	
	private LobbyGameManager lobbyGameManager;
	
	private PlayerManager playerManager;
	
	
	public DefaultPreGameTemplate(LobbyGameManager lobbyGameManager) {
		this.lobbyGameManager = lobbyGameManager;
		getLobbyGameManager().registerListener(this);
	}
	
	@GameEventHandler
	public void onPlayerManager(PlayerManager e) {
		this.playerManager = e;
	}
	
	@GameEventHandler
	public void gameStateChange(GameStateChangeEvent e) {

		if (e.getCurrentGameState().equals(GameState.PRE_GAME)) {
			Bukkit.getPluginManager().registerEvents(this, ExGame.getPlugin());
		} else if(e.getOldGameState().equals(GameState.PRE_GAME)) {
			HandlerList.unregisterAll(this);
		}
	}
	
	@GameEventHandler
	public void onEnd(GameEndEvent e) {
		getLobbyGameManager().unregisterListener(this);
		HandlerList.unregisterAll(this);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		Entity entity = e.getEntity();
		if (!(entity instanceof Player) || !hasPlayer(((Player) entity).getUniqueId()))
			return;
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		
		if (hasPlayer(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		if (hasPlayer(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if (hasPlayer(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (hasPlayer(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		Player p = (Player) e.getEntity();
		if (hasPlayer(p.getUniqueId())) {
			e.setCancelled(true);
		}
	}
	
	private boolean hasPlayer(UUID u) {
		return getPlayers().contains(u);
	}
	
	private List<UUID> getPlayers() {
		if (playerManager == null)
			return getLobbyGameManager().getPlayers();
		else
			return playerManager.getPlayers();
	}
	
	
	
	
	public LobbyGameManager getLobbyGameManager() {
		return lobbyGameManager;
	}
	
}
