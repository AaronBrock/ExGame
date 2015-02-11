package me.theminebench.exgame.game.lobbygame.game.sg.templates.chests;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

public interface ChestsManager {
	public Inventory getChestInventory(UUID playersUUID, Location l, int tier);
	public boolean hasPlayer(UUID playersUUID);
}
