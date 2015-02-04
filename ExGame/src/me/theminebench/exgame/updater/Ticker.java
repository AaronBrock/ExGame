package me.theminebench.exgame.updater;

import java.util.HashMap;
import java.util.Map.Entry;

import me.theminebench.exgame.ExGame;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class Ticker implements Runnable {
	
	private HashMap<TickListener, TickerData> listeners = new HashMap<TickListener, TickerData>();
	
	private Ticker() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(ExGame.getPlugin(), this, 0L, 1L);
	}
	
	//Register
	public static void registerListener(TickListener tickListener, TickerData tickerData) {
		getInstance().getListeners().put(tickListener, tickerData);
	}
	
	//Unregister
	public static void unregisterListener(TickListener tickListener) {
		getInstance().getListeners().remove(tickListener);
	}
	
	
	@Override
	public void run() {
		for (Entry<TickListener, TickerData> entry : new HashMap<TickListener, TickerData>(getListeners()).entrySet()) {
			if (entry.getValue().canTick())
				entry.getKey().tick();
		}
		new YamlConfiguration().getKeys(false).isEmpty();
	}
	
	
	//getListeners
	private HashMap<TickListener, TickerData> getListeners() {
		return listeners;
	}
	
	//Shhhh...
	private static Ticker instance = null;
	private static Ticker getInstance() { if (instance == null) instance = new Ticker(); return instance;}
}