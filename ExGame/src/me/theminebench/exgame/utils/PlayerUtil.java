package me.theminebench.exgame.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerUtil {
	private PlayerUtil() {}
	
	public static void resetMaxHealth(UUID playersUUID) {
		if (!hasPlayer(playersUUID))
			return;
		
		Player p = Bukkit.getPlayer(playersUUID);
		
		p.setHealth(((CraftPlayer)p).getMaxHealth());
	}
	
	public static void resetMaxHunger(UUID playersUUID) {
		if (!hasPlayer(playersUUID))
			return;
		
		Player p = Bukkit.getPlayer(playersUUID);
		
		p.setFoodLevel(20);
		p.setSaturation(20);
	}
	
	public static void clearInv(UUID playersUUID) {
		ItemStackUtil.clearInv(playersUUID);
	}
	
	public static boolean hasPlayer(UUID playersUUID) {
		return Bukkit.getPlayer(playersUUID) != null;
	}
	
}
