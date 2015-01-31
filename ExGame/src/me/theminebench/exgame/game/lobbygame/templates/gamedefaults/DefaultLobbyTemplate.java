package me.theminebench.exgame.game.lobbygame.templates.gamedefaults;

import java.util.UUID;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.lobbygame.events.LobbyEventHandler;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.GameStateChangeEvent;
import me.theminebench.exgame.game.lobbygame.events.defaultEvents.PlayerJoinArenaEvent;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.utils.PlayerUtil;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class DefaultLobbyTemplate implements LobbyGameTemplate {
	
	private LobbyGame lobbyGame;
	
	public DefaultLobbyTemplate(LobbyGame lobbyGame) {
		this.lobbyGame = lobbyGame;
		lobbyGame.getLobbyGameManager().registerLobbyListener(this);
	}
	
	@LobbyEventHandler
	public void gameStateChange(GameStateChangeEvent e) {
		if (e.getCurrentGameState().equals(GameState.IN_LOBBY)) {
			Bukkit.getPluginManager().registerEvents(this, ExGame.getPlugin());
			for (UUID playersUUID : lobbyGame.getLobbyGameManager().getArena().getPlayers()) {
				sendPlayerToLobby(playersUUID);
			}
		} else if(e.getOldGameState().equals(GameState.IN_LOBBY)) {
			HandlerList.unregisterAll(this);
		} else if (e.getCurrentGameState().equals(GameState.RESTARTING)) {
			lobbyGame.getLobbyGameManager().unregisterLobbyListener(this);
		}
	}
	
	@LobbyEventHandler
	public void onJoin(PlayerJoinArenaEvent e) {
		if (lobbyGame.getLobbyGameManager().getGameState().equals(GameState.IN_LOBBY)) {
			sendPlayerToLobby(e.getPlayersUUID());
		}
			
	}


	@EventHandler
	public void onMoveEvent(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (!hasPlayer(p.getUniqueId()))
			return;
		
		Location to = e.getTo();
		
		double x = to.getX();
		
		double y = to.getY();
		
		double z = to.getZ();
		
		Location hell = new Location( getLobbyWorld(), -5.5, 65, -1.3, 97f, 0f);
		
		Location grass = new Location(getLobbyWorld(), 6.5, 65, 0, -90f, 0f);
		
		Location cloud = new Location(getLobbyWorld(), 1, 65, -5.5, 180f, 0);
		
		Location wood = new Location(getLobbyWorld(), 0.5, 65, 7.5, 9f, 0f);
		
		Location loc = null;
		
		if (to.getBlock().getType().equals(Material.STATIONARY_LAVA)) {
			//Hell
			loc = hell;
		} else if (y < 59.5) {
			
			if        (7 >= z && z >= -6 && x < -6) {
				//Hell
				loc = hell;
			} else if (7 >= z && z >= -6 && x > 7) {
				//Grass
				loc = grass;
			} else if (7 >= x && x >= -6 && z < -6) {
				//Cloud
				loc = cloud;
			} else if (7 >= x && x >= -6 && z > 7) {
				//Wood
				loc = wood;
			} else {
				loc = getLobbyWorld().getSpawnLocation();
			}
		}
		
		if (loc != null) {
			p.teleport(loc);
			p.setFireTicks(0);
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
	
	@EventHandler
	public void onPlayerPickupItem(FoodLevelChangeEvent e) {
		if (e.getEntity() instanceof Player == false)
			return;
		
		if (hasPlayer(((Player) e.getEntity()).getUniqueId())) {
			((Player) e.getEntity()).setFoodLevel(20);
			e.setCancelled(true);
		}
	}
	
	public void sendPlayerToLobby(UUID playersUUID) {
		Player p = Bukkit.getPlayer(playersUUID);
		p.setFallDistance(0);
		p.setFireTicks(0);
		PlayerUtil.resetMaxHealth(playersUUID);
		PlayerUtil.resetMaxHunger(playersUUID);
		PlayerUtil.clearInv(playersUUID);
		p.setGameMode(GameMode.ADVENTURE);
		p.teleport(getLobbyWorld().getSpawnLocation());
	}
	
	private boolean hasPlayer(UUID u) {
		return lobbyGame.getLobbyGameManager().getArena().getPlayers().contains(u);
	}
	
	public World getLobbyWorld() {
		return lobbyGame.getLobbyGameManager().getLobbyWorld();
	}
	
	
}
