package me.theminebench.exgame.game.eventgame;

public enum GameEventPriority {

	LOWEST(0),

	LOW(1),

	NORMAL(2),

	HIGH(3),

	HIGHEST(4);

	private final int slot;

	private GameEventPriority(int slot) {
		this.slot = slot;
	}

	public int getSlot() {
		return this.slot;
	}
}
