package me.theminebench.exgame.game.lobbygame.events;

public enum LobbyEventPriority {
  LOWEST(0), 

  LOW(1), 

  NORMAL(2),

  HIGH(3), 

  HIGHEST(4);


  private final int slot;

  private LobbyEventPriority(int slot) {
    this.slot = slot;
  }

  public int getSlot() {
    return this.slot;
  }
}
