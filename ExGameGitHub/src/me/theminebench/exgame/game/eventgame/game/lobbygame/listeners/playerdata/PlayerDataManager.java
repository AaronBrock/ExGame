package me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.playerdata;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.event.Listener;

import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.events.PlayerQuitArenaEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameEndEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.PlayerManager;

public class PlayerDataManager {
	
	private HashMap<UUID, HashMap<Object, Object>> datas = new HashMap<UUID, HashMap<Object,Object>>();
	
	private LobbyGameManager lobbyGameManager;
	
	private PlayerManager playerManager;
	
	public PlayerDataManager(LobbyGameManager lobbyGameManager) {
		this.lobbyGameManager = lobbyGameManager;
		getLobbyGameManager().registerListener(this);
	}
	
	public Set<UUID> getPlayers() {
		return datas.keySet();
	}
	
	public boolean hasPlayer(UUID playersUUID) {
		return getPlayers().contains(playersUUID);
	}
	
	public void storeData(UUID playersUUID, Object key, Object value) {
		if (datas.containsKey(playersUUID)) {
			datas.get(playersUUID).put(key, value);
		}
	}
	
	public Object getData(UUID playersUUID, Object key) {
		return datas.get(playersUUID).get(key);
	}
	
	public <T> T getData(UUID playersUUID, Object key, Class<T> type) {
		if (type.isInstance(getData(playersUUID, key))) {
			return type.cast(getData(playersUUID, key));
		}
		return null;
	}
	
	
	
	@GameEventHandler
	public void onPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
		for (UUID u : getPlayerManager().getGamePlayers()) {
			datas.put(u, new HashMap<Object, Object>());
		}
		getLobbyGameManager().fireEvent(this);
	}
	
	@GameEventHandler
	public void onLeave(PlayerQuitArenaEvent e) {
		
	}
	
	
	@GameEventHandler
	public void onEnd(GameEndEvent e) {getLobbyGameManager().unregisterListener(this);}
	
	
	
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	public LobbyGameManager getLobbyGameManager() {
		return lobbyGameManager;
	}
}
