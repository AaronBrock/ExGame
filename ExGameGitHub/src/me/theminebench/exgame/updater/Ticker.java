package me.theminebench.exgame.updater;

import java.util.HashSet;

import me.theminebench.exgame.ExGame;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Ticker extends BukkitRunnable {
	
	private HashSet<TickListener> listeners = new HashSet<TickListener>();

	private Ticker() {
		runTaskTimer(ExGame.getPlugin(), 0L, 1L);
	}
	
	//Register
	public static void registerListener(TickListener tickListener) {
		getInstance().getListeners().add(tickListener);
		
	}
	
	//Unregister
	public static void unregisterListener(TickListener tickListener) {
		getInstance().getListeners().remove(tickListener);
		if (getInstance().getListeners().isEmpty()) {
			getInstance().cancel();
			instance = null;
		}
	}
	
	public static boolean isRegistered(TickListener tickListener) {
		return getInstance().getListeners().contains(tickListener);
	}
	
	
	@Override
	public void run() {
		for (TickListener tickListener : new HashSet<TickListener>(getListeners())) {
			tickListener.tick();
		}
	}
	
	
	//getListeners
	private HashSet<TickListener> getListeners() {
		return listeners;
	}
	
	//Shhhh...
	private static Ticker instance = null;
	public static Ticker getInstance() { if (instance == null) instance = new Ticker(); return instance;}
}