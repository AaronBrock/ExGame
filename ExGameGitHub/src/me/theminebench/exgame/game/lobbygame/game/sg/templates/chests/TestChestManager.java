package me.theminebench.exgame.game.lobbygame.game.sg.templates.chests;
		
import java.util.UUID;

import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spectate.GameModeSpectateManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
		
		
public class TestChestManager implements ChestsManager {
		
	private GameModeSpectateManager sm;
		
	public TestChestManager(GameModeSpectateManager sm) {
		this.sm = sm;
	}	
	
	public final int defaultWeaponChance = 1;
	
	
	@Override
	public Inventory getChestInventory(UUID playersUUID, Location l, int tier) {
		Inventory inv = Bukkit.createInventory(null, 9, "I am a trains!");
		inv.addItem(new ItemStack(Material.APPLE), new ItemStack(Material.ANVIL));
		return inv;
	}	
		
		
		
		
		
	public ItemStack[] getItems() {
		return null;
	}	
		
	@Override
	public boolean hasPlayer(UUID playersUUID) {
		return sm.getPlayers().contains(playersUUID);
	}
}		
		