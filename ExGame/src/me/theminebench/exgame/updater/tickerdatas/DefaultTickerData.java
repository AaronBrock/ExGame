package me.theminebench.exgame.updater.tickerdatas;

import me.theminebench.exgame.updater.TickerData;

public class DefaultTickerData implements TickerData {
	
	@Override
	public boolean canTick() {
		return true;
	}
	
}
