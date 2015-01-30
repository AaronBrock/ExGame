package me.theminebench.exgame.game.lobbygame.game.spleef;

import java.util.UUID;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.lobbygame.LobbyGameCreater.GameState;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.spectate.SpectateManager;
import me.theminebench.exgame.game.lobbygame.templates.worldcreation.WorldMannager;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wolf;
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

public class SpleefTemplate implements LobbyGameTemplate {
	
	private SpleefGame spleefGame;
	
	private SpectateManager spectateManager;
	
	private WorldMannager worldMannager;
	
	public SpleefTemplate(SpleefGame spleefGame, SpectateManager spectateManager, WorldMannager worldMannager) {
		this.spleefGame = spleefGame;
		this.spectateManager = spectateManager;
		this.worldMannager = worldMannager;
	}
	
	public SpectateManager getSpectateManager() {
		return this.spectateManager;
	}
	
	public SpleefGame getSpleefGame() {
		return spleefGame;
	}
	
	@Override
	public void gameStateChange(GameState oldGameState, GameState newGameState) {
		
		if (newGameState.equals(GameState.PRE_GAME)) {
			for (UUID u : getSpleefGame().getLobbyGameCreater().getArena().getPlayers()) {
				getSpleefGame().getDefaultSpawnTemplate().sendPlayerToSpawn(u);
			}
		}
		
		if (newGameState.equals(GameState.IN_GAME)) {
			Bukkit.getPluginManager().registerEvents(this, ExGame.getPlugin());
		} else if(oldGameState.equals(GameState.IN_GAME)) {
			HandlerList.unregisterAll(this);
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
			
			TNTPrimed tnt = (TNTPrimed) worldMannager.getGameWorld().spawnEntity(block.getLocation().add(0.5, 0.5, 0.5), EntityType.PRIMED_TNT);
			
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
		if (!e.getLocation().getWorld().getName().equals(worldMannager.getGameWorld().getName()))
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
				TNTPrimed tnt = (TNTPrimed) worldMannager.getGameWorld().spawnEntity(block.getLocation().add(0.5, 0.5, 0.5), EntityType.PRIMED_TNT);
				
				tnt.setFuseTicks(5);
				
				tnt.setVelocity(bounceVector());
				block.setType(Material.AIR);
			}
			bounceBlock(block);
		}
		
		final double amount = 4;
		
		for (UUID u : getSpleefGame().getLobbyGameCreater().getArena().getPlayers()) {
			
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
		
		TNTPrimed tnt = (TNTPrimed) worldMannager.getGameWorld().spawnEntity(loc.add(0.5, 0.5, 0.5), EntityType.PRIMED_TNT);
		
		loc.getWorld().createExplosion(loc, 0.5f);
		
		tnt.setFuseTicks(0);
		
		arrow.remove();
	}
	
	public boolean hasPlayer(UUID playersUUID) {
		return getSpectateManager().getPlayers().contains(playersUUID);
	}

}
