package me.theminebench.exgame.game.eventgame.game.lobbygame.game.sg.listeners.thirst;

import java.util.UUID;

import me.theminebench.exgame.game.eventgame.GameEventHandler;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager;
import me.theminebench.exgame.game.eventgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.playerdata.PlayerDataManager;
import me.theminebench.exgame.updater.TickListener;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ThirstManager implements TickListener {
	
	private LobbyGameManager lobbyGameManager;
	private PlayerDataManager playerDataManager;
	
	public final float maxThirst = 100;
	
	public final String thirst = "thirst";
	public final String thirstCounter = "thirstCounter";
	
	public ThirstManager(LobbyGameManager lobbyGameManager) {
		this.lobbyGameManager = lobbyGameManager;
		getLobbyGameManager().registerListener(this);
	}
	
	@GameEventHandler
	public void onGameStateChange(GameStateChangeEvent e) {
		if (e.getCurrentGameState().equals(GameState.IN_GAME)) {
			for (UUID playersUUID : getPlayerDataManager().getPlayers()) {
				getPlayerDataManager().storeData(playersUUID, thirst, maxThirst);
				getPlayerDataManager().storeData(playersUUID, thirstCounter, 0);
			}
		}
	}
	
	
	private long ticks = 0;
	
	@Override
	public void tick() {
		ticks++;
		boolean second = ticks % 20 == 0;
		for (UUID playersUUID : getPlayerDataManager().getPlayers()) {
			if (getPlayerDataManager().getData(playersUUID, thirstCounter, Integer.class) >= 1) {
				getPlayerDataManager().storeData(playersUUID, thirstCounter, getPlayerDataManager().getData(playersUUID, thirstCounter, Integer.class) - 1);
			} else if (second) {
				Player p = Bukkit.getPlayer(playersUUID);
				if (p.isSprinting()) {
					
				} else if (p.isSprinting()) {
					
				} else {
					
				}
				
			}
		}
		
		
	}
	
	@GameEventHandler
	public void setPlayerManager(PlayerDataManager playerManager) {
		this.playerDataManager = playerManager;
	}
	
	public PlayerDataManager getPlayerDataManager() {
		Validate.notNull(playerDataManager);
		return playerDataManager;
	}
	
	public LobbyGameManager getLobbyGameManager() {
		return lobbyGameManager;
	}




}
