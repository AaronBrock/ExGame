package me.theminebench.exgame.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerUtil {
	private ServerUtil() {}
	
	@SuppressWarnings("deprecation")
	public static void shutdown(String kickMessage, String... MessageLogs) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.kickPlayer(kickMessage);
		}
		for (String line : MessageLogs) {
			Bukkit.getServer().getLogger().warning(line);
		}
		Bukkit.getServer().shutdown();
	}
}
