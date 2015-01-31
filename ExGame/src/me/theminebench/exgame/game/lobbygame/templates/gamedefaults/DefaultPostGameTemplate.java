package me.theminebench.exgame.game.lobbygame.templates.gamedefaults;

import java.util.UUID;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.lobbygame.events.LobbyEventHandler;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.GameStateChangeEvent;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class DefaultPostGameTemplate implements LobbyGameTemplate {
	
	private LobbyGame lobbyGame;
	
	public DefaultPostGameTemplate(LobbyGame lobbyGame) {
		this.lobbyGame = lobbyGame;
		lobbyGame.getLobbyGameManager().registerLobbyListener(this);
	}
	
	@LobbyEventHandler
	public void gameStateChange(GameStateChangeEvent e) {
		if (e.getCurrentGameState().equals(GameState.POST_GAME)) {
			Bukkit.getPluginManager().registerEvents(this, ExGame.getPlugin());
		} else if(e.getOldGameState().equals(GameState.POST_GAME)) {
			HandlerList.unregisterAll(this);
		} if (e.getCurrentGameState().equals(GameState.RESTARTING)) {
			lobbyGame.getLobbyGameManager().unregisterLobbyListener(this);
		}
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
		return lobbyGame.getLobbyGameManager().getArena().getPlayers().contains(u);
	}
}
