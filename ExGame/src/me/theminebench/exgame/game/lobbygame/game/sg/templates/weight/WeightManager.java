package me.theminebench.exgame.game.lobbygame.game.sg.templates.weight;

import java.util.UUID;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.game.sg.templates.weight.weightlisteners.WeightListeners;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;
import me.theminebench.exgame.utils.ItemStackUtil;
import me.theminebench.exgame.utils.NumberUtil;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class WeightManager implements LobbyGameTemplate{
	private WeightListeners weightListeners;
	private LobbyGame lobbyGame;
	private WeightPlayerDataManager pdm;
	private WeightItemDataManager mdm;
	
	public WeightManager(LobbyGame lobbyGame, WeightPlayerDataManager pdm, WeightItemDataManager mdm) {
		this.lobbyGame = lobbyGame;
		this.mdm = mdm;
		this.pdm = pdm;
		this.weightListeners = new WeightListeners(this);
	}
	
	public LobbyGame getLobbyGame() {
		return this.lobbyGame;
	}
	public WeightPlayerDataManager getPlayerDataManager() {
		return this.pdm;
	}
	public WeightItemDataManager getItemDataManager() {
		return this.mdm;
	}
	public void updateWeight(UUID u) {
		Player p = Bukkit.getPlayer(u);
		
		if (!getPlayerDataManager().hasPlayer(u))
			return;
		PlayerInventory pi = p.getInventory();
		
		ItemStack[] items = ArrayUtils.addAll(pi.getArmorContents(), pi.getContents());
		
		int counter = 0;
		for (ItemStack i : items) {
			if (i != null) {
				counter = counter + (getItemDataManager().getItemWeight(i) * i.getAmount());
			}
		}
		
		getPlayerDataManager().setPlayerWeight(u, counter);
	}
	public void updateWeightLater(final UUID u) {
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				updateWeight(u);
				
			}
		}.runTaskLater(ExGame.getPlugin(), 1);
		
		/*
		Bukkit.broadcastMessage(Bukkit.getPlayer(u).getName());
		Updater.instance().registerListener(new UpdateListener() {
			@Override
			public void update(long l) {
				updateWeight(u);
				Updater.instance().unregisterListener(this);
				
			}
		}, 1);
		*/
	}
	public int itemsAllowedUsingAmount(UUID u, ItemStack itemStack) {
		return NumberUtil.lower(itemStack.getAmount(), itemsAllowed(u, itemStack));
	}
	
	public int itemsAllowed(UUID u, ItemStack itemStack) {
		return NumberUtil.lower(itemsAllowedDewToSpace(u, itemStack), itemsAllowedDewToWeight(u, itemStack));
	}
	
	public int itemsAllowedDewToWeight(UUID u, ItemStack itemStack) {
		int itemWeight = getItemDataManager().getItemWeight(itemStack);
		if (itemWeight != 0) {
			int weightCounter = 0;
			int weight = getPlayerDataManager().getPlayerWeight(u);
			int toAdd = 0;
			weightCounter += itemWeight;
			while (weight + weightCounter <= getPlayerDataManager().getMaxWeight(u)) {
				toAdd++;
				weightCounter += itemWeight;
			}
			return toAdd;
		} else
			return Integer.MAX_VALUE;
	}

	public int itemsAllowedDewToSpace(UUID u, ItemStack itemStack) {
		return ItemStackUtil.spaceAllowed(Bukkit.getServer().getPlayer(u).getInventory(), itemStack);
	}
	
	public WeightListeners getWeightListeners() {
		return weightListeners;
	}
	
	@Override
	public void gameStateChange(GameState oldGameState, GameState newGameState) {
		if (newGameState.equals(GameState.IN_GAME)) {
			for (UUID u : lobbyGame.getLobbyGameManager().getArena().getPlayers())
				updateWeight(u);
			Bukkit.getPluginManager().registerEvents(getWeightListeners(), ExGame.getPlugin());
		} else
			if (oldGameState.equals(GameState.IN_GAME)) {
				HandlerList.unregisterAll(getWeightListeners());
			}
		
	}
	@Override
	public boolean canJoin(UUID playersUUID) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void playerJoin(UUID playersUUID) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void playerQuit(UUID playersUUID) {
		// TODO Auto-generated method stub
		
	}
}

