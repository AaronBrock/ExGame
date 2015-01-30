package me.theminebench.exgame;

import java.util.HashMap;

import org.bukkit.Bukkit;

public class Updater implements Runnable {
	
	private HashMap<UpdateListener, UpdaterData> listeners = new HashMap<UpdateListener, UpdaterData>();
	
	private long tickCounter = 0l;
	
	private static Updater instance = null;
	
	private Updater() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ExGame.getPlugin(), this, 0L, 1L);
	}
	
	public static Updater instance() {
		if (instance == null) {
			instance = new Updater();
		}
		
		return instance;
	}
	
	public boolean isRegistered(UpdateListener updateListener) {
		return listeners.containsKey(updateListener);
	}
	
	public void registerListener(UpdateListener updateListener, long... ticks) {
		if (ticks.length != 0)
			listeners.put(updateListener, new UpdaterData(tickCounter, ticks));
		else {
			listeners.put(updateListener, new UpdaterData(tickCounter, new long[]{1}));
		}
	}

	public void unregisterListener(UpdateListener updateReader) {
		listeners.remove(updateReader);
	}
	
	@Override
	public void run() {
		for (UpdateListener reader : listeners.keySet()) {
			if (reader == null) {
				listeners.remove(reader);
				continue;
			}
			for (UpdateListener ul : listeners.keySet()) {
				UpdaterData ud = listeners.get(ul);
				for (long l : ud.getTicks()) {
					
					if ((tickCounter - ud.getStartTime()) % l == 0)
						reader.update(l);
				}
				
			}
			
			if (tickCounter == Long.MAX_VALUE)
				tickCounter = 0l;
			tickCounter++;
		}
	}
}
