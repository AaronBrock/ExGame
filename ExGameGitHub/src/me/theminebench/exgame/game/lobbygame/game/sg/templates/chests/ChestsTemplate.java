package me.theminebench.exgame.game.lobbygame.game.sg.templates.chests;

import java.util.HashMap;
import java.util.UUID;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.game.sg.templates.chests.data.ChestData;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.utils.LocationUtils;
import me.theminebench.exgame.utils.NumberUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestsTemplate implements LobbyGameTemplate {
	
	private LobbyGame lobbyGame;
	private ChestsManager chestsManager;
	
	private HashMap<Location, ChestData> chestDatas = new HashMap<Location, ChestData>();
	
	public ChestsTemplate(LobbyGame lobbyGame, ChestsManager chestsManager) {
		this.lobbyGame = lobbyGame;
		this.chestsManager = chestsManager;
	}
	
	@Override
	public void gameStateChange(GameState oldGameState, GameState newGameState) {
		if (newGameState.equals(GameState.IN_LOBBY)) {
			
			YamlConfiguration mapdata = getLobbyGame().getLobbyGameManager().getMapData();
			
			if (mapdata.getConfigurationSection("chests") != null) {
				ConfigurationSection chests = mapdata.getConfigurationSection("chests");
				for (String key : chests.getKeys(false)) {
					if (!NumberUtil.isInt(key))
						return;
					
					int tier = Integer.parseInt(key);
					
					ExGame.getPlugin().getLogger().warning("Key = " + key);
					ExGame.getPlugin().getLogger().warning(chests.getString(key));
					
					for (String s : chests.getStringList(key)) {
						Location l = LocationUtils.toLocation(getLobbyGame().getLobbyGameManager().getGameWorldName(), s);
						ExGame.getPlugin().getLogger().warning("s = " + s);
						if (l == null)
							continue;
						
						chestDatas.put(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ()), new ChestData(tier));
					}

				}
				if (!chestDatas.isEmpty())
					return;
				ExGame.getPlugin().getLogger().warning("WE are traped, help us please!");
			} else
				ExGame.getPlugin().getLogger().warning("DOGS!");
			ExGame.getPlugin().getLogger().warning("There was no chest locations found!");
			return;
		}
		if (newGameState.equals(GameState.IN_GAME)) {
			Bukkit.getPluginManager().registerEvents(this, ExGame.getPlugin());
		}
		if (oldGameState.equals(GameState.IN_GAME)) {
			HandlerList.unregisterAll(this);
		}
	}

	@Override
	public boolean canJoin(UUID playersUUID) {return true;}

	@Override
	public void playerJoin(UUID playersUUID) {}

	@Override
	public void playerQuit(UUID playersUUID) {}
	
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if (e.isCancelled() || !getChestsManager().hasPlayer(p.getUniqueId())) 
			return;
		
		Block b = e.getClickedBlock();
		
		if (b == null)
			return;
		
		ChestData cd = chestDatas.get(b.getLocation());
		System.out.print("WE are here!");
		if (cd == null)
			return;
		System.out.print("we are now here, YAAY!");
		if (cd.getInventory() == null)
			cd.setInventory(getChestsManager().getChestInventory(p.getUniqueId(), b.getLocation(), cd.getTier()));
		
		p.openInventory(cd.getInventory());
		e.setCancelled(true);
	}
	
	public LobbyGame getLobbyGame() {
		return lobbyGame;
	}
	
	public ChestsManager getChestsManager() {
		return chestsManager;
	}
}
