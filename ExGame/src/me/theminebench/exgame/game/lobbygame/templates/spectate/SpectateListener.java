package me.theminebench.exgame.game.lobbygame.templates.spectate;

import java.util.UUID;

public interface SpectateListener {
	public void enabledSpectate(UUID playersUUID);
	public void disabledSpectate(UUID playersUUID);
}
