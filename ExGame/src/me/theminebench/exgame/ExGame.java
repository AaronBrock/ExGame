package me.theminebench.exgame;

import me.theminebench.exgame.game.eventgame.EventGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameFactory;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.eventgame.game.lobbygame.game.spleef.SpleefGame;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class ExGame extends JavaPlugin implements Listener {
	
	private static ExGame plugin;
	
	private Arena arena;
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		
		Bukkit.broadcastMessage("we Like TRAINS");
		if (!arena.addPlayer(e.getPlayer().getUniqueId())) {
			e.setJoinMessage("");
			new BukkitRunnable() {
				@Override
				public void run() {
					p.kickPlayer("You ain't good enough!");
				}
			}.runTaskLater(this, 1);
		}
	}

	@Override
	public void onEnable() {
		setPlugin(this);
		
		this.arena = new Arena(new EventGameManager(new LobbyGameManager(new LobbyGameFactory() {
			
			@Override
			public LobbyGame getLobbyGame(LobbyGameManager lobbyGameManager) {
				return new SpleefGame();
			}
		})));
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	public static ExGame getPlugin() {
		return plugin;
	}

	public static void setPlugin(ExGame plugin) {
		ExGame.plugin = plugin;
	}
}
