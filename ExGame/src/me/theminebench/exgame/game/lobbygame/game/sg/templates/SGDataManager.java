package me.theminebench.exgame.game.lobbygame.game.sg.templates;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import me.theminebench.exgame.game.eventgame.game.lobbygame.listeners.spectate.GameModeSpectateManager;
import me.theminebench.exgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.game.sg.templates.weight.WeightItemDataManager;
import me.theminebench.exgame.game.lobbygame.game.sg.templates.weight.WeightPlayerDataManager;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.game.lobbygame.templates.spectate.SpectateListener;
import me.theminebench.exgame.utils.ItemStackUtil;

public class SGDataManager implements LobbyGameTemplate, WeightPlayerDataManager, WeightItemDataManager, SpectateListener {
	
	private HashMap<UUID, PlayerData> playerDatas = new HashMap<UUID, PlayerData>();
	
	
	
	private LobbyGame lobbyGame;
	
	private GameModeSpectateManager sm;
	
	public SGDataManager(LobbyGame lobbyGame, GameModeSpectateManager sm) {
		this.sm = sm;
		this.lobbyGame = lobbyGame;
		getSpectateManager().registerListener(this);
	}
	
	public GameModeSpectateManager getSpectateManager() {
		return this.sm;
	}
	
	public LobbyGame getLobbyGame() {
		return this.lobbyGame;
	}
	
	@Override
	public void gameStateChange(GameState oldGameState, GameState newGameState) {
		if (getLobbyGame().getLobbyGameManager().getGameState().equals(GameState.IN_LOBBY)) {
			for (UUID u : getLobbyGame().getLobbyGameManager().getArena().getPlayers()) {
				getPlayerDatas().put(u, new PlayerData(u));
			}
		}
	}
	
	//TODO
	
	@Override
	public boolean canJoin(UUID playersUUID) {return true;}
	
	@Override
	public void playerJoin(UUID playersUUID) {}
	
	@Override
	public void playerQuit(UUID playersUUID) {
		removePlayerData(playersUUID);
	}
	
	@Override
	public void enabledSpectate(UUID playersUUID) {
		removePlayerData(playersUUID);
	}
	
	@Override
	public void disabledSpectate(UUID playersUUID) {
		getPlayerDatas().put(playersUUID, new PlayerData(playersUUID));
	}
	
	private void removePlayerData(UUID playersUUID) {
		getPlayerDatas().remove(playersUUID);
	}
	
	public HashMap<UUID, PlayerData> getPlayerDatas() {
		return playerDatas;
	}
	
	public PlayerData getPlayerData(UUID playersUUID) {
		return getPlayerDatas().get(playersUUID);
	}
	
	@Override
	public int getPlayerWeight(UUID playersUUID) {
		return getPlayerData(playersUUID).getWeight();
	}

	@Override
	public void setPlayerWeight(UUID playersUUID, int weight) {
		getPlayerData(playersUUID).setWeight(weight);
		Bukkit.getPlayer(playersUUID).sendMessage(ChatColor.GREEN + "" + weight + "/" + getMaxWeight(playersUUID));
	}

	@Override
	public boolean hasPlayer(UUID playersUUID) {
		return getSpectateManager().getPlayers().contains(playersUUID);
	}
	
	@Override
	public int getMaxWeight(UUID playersUUID) {
		return 100;
	}

	@Override
	public int getItemWeight(ItemStack is) {
		
		return ItemStackUtil.getWeight(is);
	}
	
}