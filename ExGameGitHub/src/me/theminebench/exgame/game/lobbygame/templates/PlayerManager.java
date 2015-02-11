package me.theminebench.exgame.game.lobbygame.templates;

import java.util.List;
import java.util.UUID;

public interface PlayerManager {

	public boolean hasPlayer(UUID playersUUID);

	public boolean hasGamePlay(UUID playersUUID);

	public List<UUID> getPlayers();

	public List<UUID> getGamePlayers();

}