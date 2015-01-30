package me.theminebench.exgame.game.lobbygame.game.sg.templates.chests.data;

import org.bukkit.inventory.Inventory;

public class ChestData {
	
	private int tier;
	
	private Inventory inventory;
	
	public ChestData(int tier) {
		this(tier, null);
	}
	
	public ChestData(int tier, Inventory inventory) {
		this.tier = tier;
		this.inventory = inventory;
	}
	
	public int getTier() {
		return tier;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
}
