package me.theminebench.exgame;

public class UpdaterData {
	
	private long startTime;
	
	private long[] ticks;
	
	public UpdaterData(long startTime, long[] ticks) {
		this.startTime = startTime;
		this.ticks = ticks;
	}

	public long[] getTicks() {
		return ticks;
	}
	
	public long getStartTime() {
		return startTime;
	}
}
