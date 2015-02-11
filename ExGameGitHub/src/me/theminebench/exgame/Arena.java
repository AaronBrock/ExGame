package me.theminebench.exgame;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.theminebench.exgame.game.Game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Arena implements Listener {
	private List<UUID> players = new ArrayList<UUID>();
	
	private Game game;
	
	public Arena(Game game) {
		this.game = game;
		getGame().setArena(this);
		getGame().start();
		Bukkit.getPluginManager().registerEvents(this, ExGame.getPlugin());
	}
	
	public boolean removePlayer(UUID playersUUID) {
		if (getGame() == null) return false;
		
		if (!players.contains(playersUUID)) return false;
		
		players.remove(playersUUID);
		getGame().playerQuit(playersUUID);
		return true;
	}
	
	public boolean addPlayer(UUID playersUUID) {
		
		if (getGame() == null) return false;

		if (players.contains(playersUUID)) return false;

		if (!getGame().canJoin(playersUUID)) return false;

		players.add(playersUUID);
		getGame().playerJoin(playersUUID);
		return true;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		removePlayer(e.getPlayer().getUniqueId());
	}
	
	public List<UUID> getPlayers() {
		return players;
	}
	
	public void broadcast(String msg) {
		for(UUID u : getPlayers()) {
			Player p = Bukkit.getPlayer(u);
			p.sendMessage(msg);
		}
	}
	
	public void formattedBroadcast(String format, String msg) {
		broadcast(ChatColor.DARK_GREEN + format + " > " + ChatColor.GREEN + msg);
	}

	public Game getGame() {
		return game;
	}
}
