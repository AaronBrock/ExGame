package me.theminebench.exgame.game.lobbygame.game.spleef;

import java.util.UUID;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.eventgame.events.PlayerQuitArenaEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spectate.GameModeSpectateManager;
import me.theminebench.exgame.game.lobbygame.LobbyGameManager.GameState;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class SpleefTemplate {
	
	private SpleefGame spleefGame;
	
	private GameModeSpectateManager spectateManager;
	
	private World gameWorld;
	
	public SpleefTemplate(SpleefGame spleefGame, GameModeSpectateManager spectateManager) {
		this.spleefGame = spleefGame;
		this.spectateManager = spectateManager;
		spleefGame.getLobbyGameManager().registerLobbyListener(this);
	}
	
	public GameModeSpectateManager getSpectateManager() {
		return this.spectateManager;
	}
	
	public SpleefGame getSpleefGame() {
		return spleefGame;
	}
	
	@LobbyEventHandler
	public void createWorld(WorldCreateEvent e) {
		this.gameWorld = e.getWorld();
	}
	
	@LobbyEventHandler
	public void gameStateChange(GameStateChangeEvent e) {
		
		if (e.getCurrentGameState().equals(GameState.IN_GAME)) {
			Bukkit.getPluginManager().registerEvents(this, ExGame.getPlugin());
		} else if(e.getOldGameState().equals(GameState.IN_GAME)) {
			HandlerList.unregisterAll(this);
		} else if (e.getCurrentGameState().equals(GameState.RESTARTING)) {
			getSpleefGame().getLobbyGameManager().unregisterLobbyListener(this);
		}
	}

	@LobbyEventHandler
	public void playerQuit(PlayerQuitArenaEvent e) {
		getSpleefGame().checkGameEnd();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent e) {
		
		if (e.isCancelled())
			return;
		
		int foodChange = 1;
		
		if (!hasPlayer(e.getPlayer().getUniqueId()))
			return;
		
		if (!(e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) 
			return;
		
		Block block = e.getClickedBlock();
		
		Player p = e.getPlayer();
		
		if (block.getType().equals(Material.TNT)) {
			
			TNTPrimed tnt = (TNTPrimed) getGameWorld().spawnEntity(block.getLocation().add(0.5, 0.5, 0.5), EntityType.PRIMED_TNT);
			
			tnt.setFuseTicks(30);
			
			block.setType(Material.AIR);
			return;
		}
		
		
		
		if (p.getFoodLevel() + foodChange >= 20)
			p.setFoodLevel(20);
		else
			p.setFoodLevel(p.getFoodLevel() + foodChange);
		
		
		block.getLocation().getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
		block.setType(Material.AIR);
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.isCancelled())
			return;
		
		if (!(e.getEntity() instanceof Player))
			return;
		
		Player p = (Player) e.getEntity();
		
		if (!hasPlayer(p.getUniqueId()))
			return;
		
		
		//Note this is a get a round of p.getHealth();
		double health = ((CraftPlayer) p).getHandle().getHealth();
		
		if (health <= e.getDamage() || e.getCause().equals(DamageCause.VOID)) {
			e.setCancelled(true);
			getSpectateManager().enableSpectate(p.getUniqueId());
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamagerByEnttiy(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;
		
		//final double divVector = 2;
		if (!(e.getDamager() instanceof Player && e.getEntity() instanceof Player) )
			return;
		
		Player damager = (Player) e.getDamager();
		//final Player player = (Player) e.getEntity();
		
		if (!hasPlayer(damager.getUniqueId()) && !hasPlayer(damager.getUniqueId()))
			return;
		
		e.setDamage(0);
		
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e){
		if (e.isCancelled())
			return;
		if (!e.getLocation().getWorld().getName().equals(getGameWorld().getName()))
			return;
		
		e.setCancelled(true);
		
		double blockFlyChance = 0.3;
		
		for(Block block : e.blockList()) {
			
			if (Math.random() > blockFlyChance) {
				block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
				block.setType(Material.AIR);
				continue;
			}
			if (block.getType().equals(Material.TNT)) {
				block.setType(Material.AIR);
				TNTPrimed tnt = (TNTPrimed) getGameWorld().spawnEntity(block.getLocation().add(0.5, 0.5, 0.5), EntityType.PRIMED_TNT);
				
				tnt.setFuseTicks(5);
				
				tnt.setVelocity(bounceVector());
				block.setType(Material.AIR);
			}
			bounceBlock(block);
		}
		
		final double amount = 4;
		
		for (UUID u : getSpleefGame().getLobbyGameManager().getArena().getPlayers()) {
			
			Player p = Bukkit.getPlayer(u);
			
			Location first_location = e.getLocation();
			
			Location second_location = p.getLocation();
			
			Vector from = new Vector(first_location.getX(), first_location.getY(), first_location.getZ());
			
			Vector to = new Vector(second_location.getX(), second_location.getY(), second_location.getZ());
			
			Vector vector = to.subtract(from);
			
			double magnitude = amount/(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2) + Math.pow(vector.getZ(), 2));
			
			if (magnitude <= 0.05)
				continue;
			
			vector = vector.multiply(magnitude);
			
			
			p.setVelocity(vector);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void bounceBlock(Block block){
		if(block == null) return;
		FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
		fallingBlock.setDropItem(false);
		fallingBlock.setVelocity(bounceVector());
		block.setType(Material.AIR);
	}
	
	public Vector bounceVector() {
		float x = (float) (Math.random() - 0.5);
		float y = (float) (Math.random() - 0.2);
		float z = (float) (Math.random() - 0.5);
		return new Vector(x, y, z);
	}
	
	@EventHandler
	public void EntityChangeBlockEvent(EntityChangeBlockEvent e) {
		if (e.getEntityType().equals(EntityType.FALLING_BLOCK)) {
			e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.STEP_SOUND, ((FallingBlock) e.getEntity()).getMaterial());
				
			e.getEntity().remove();
		
			e.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void ArrowDamage(ProjectileHitEvent e) {
		
		final Arrow arrow = (Arrow) e.getEntity();
		
		Location loc = arrow.getLocation();
		
		TNTPrimed tnt = (TNTPrimed) getGameWorld().spawnEntity(loc.add(0.5, 0.5, 0.5), EntityType.PRIMED_TNT);
		
		loc.getWorld().createExplosion(loc, 0.5f);
		
		tnt.setFuseTicks(0);
		
		arrow.remove();
	}
	
	public boolean hasPlayer(UUID playersUUID) {
		return getSpectateManager().getPlayers().contains(playersUUID);
	}
	
	public World getGameWorld() {
		return gameWorld;
	}
	
}
