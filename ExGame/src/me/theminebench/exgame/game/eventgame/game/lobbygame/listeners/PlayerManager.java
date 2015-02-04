package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners;

import java.util.List;
import java.util.UUID;

public interface PlayerManager {
	public List<UUID> getPlayers();

	public List<UUID> getGamePlayers();

}
