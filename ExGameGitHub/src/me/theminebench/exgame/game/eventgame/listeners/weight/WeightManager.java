package me.theminebench.exgame.game.eventgame.listeners.weight;

import java.util.UUID;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.eventgame.listeners.weight.weightlisteners.WeightListeners;
import me.theminebench.exgame.utils.ItemStackUtil;
import me.theminebench.exgame.utils.NumberUtil;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class WeightManager {
	private WeightListeners weightListeners;
	private WeightPlayerDataManager pdm;
	private WeightItemDataManager mdm;
	
	public WeightManager(WeightPlayerDataManager pdm, WeightItemDataManager mdm) {
		this.mdm = mdm;
		this.pdm = pdm;
		this.weightListeners = new WeightListeners(this);
	}

	public void start() {
		Bukkit.getPluginManager().registerEvents(getWeightListeners(), ExGame.getPlugin());
		for (UUID u : getPlayerDataManager().getPlayers()) {
			updateWeight(u);
		}
	}

	public void stop() {
		HandlerList.unregisterAll(getWeightListeners());
	}
	
	public WeightPlayerDataManager getPlayerDataManager() {
		return this.pdm;
	}
	public WeightItemDataManager getItemDataManager() {
		return this.mdm;
	}
	
	public void updateWeight(UUID u) {
		Player p = Bukkit.getPlayer(u);
		
		if (!getPlayerDataManager().getPlayers().contains(u))
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
		Bukkit.broadcastMessage(Bukkit.getPlayer(u).getName());
		new BukkitRunnable() {
			@Override
			public void run() {
				updateWeight(u);
			}
		}.runTaskLater(ExGame.getPlugin(), 1);
	}
	
	public int itemsAllowedUsingAmount(UUID u, ItemStack itemStack) {
		return NumberUtil.lower(itemStack.getAmount(), itemsAllowed(u, itemStack));
	}
	
	public int itemsAllowed(UUID u, ItemStack itemStack) {
		return NumberUtil.lower(itemsAllowedDueToSpace(u, itemStack), itemsAllowedDueToWeight(u, itemStack));
	}
	
	public int itemsAllowedDueToWeight(UUID u, ItemStack itemStack) {
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

	public int itemsAllowedDueToSpace(UUID u, ItemStack itemStack) {
		return ItemStackUtil.spaceAllowed(Bukkit.getServer().getPlayer(u).getInventory(), itemStack);
	}
	
	public WeightListeners getWeightListeners() {
		return weightListeners;
	}
	
}