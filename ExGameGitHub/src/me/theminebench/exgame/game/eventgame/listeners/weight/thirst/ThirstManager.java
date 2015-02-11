package me.theminebench.exgame.game.eventgame.listeners.weight.thirst;

import me.theminebench.exgame.updater.TickListener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;



public class ThirstManager implements Listener, TickListener {
	
	private ThirstPlayerDataManager thirstPlayerDataManager;
	
	private boolean enabled;
	
	private final int maxThirst = 100;
	
	public ThirstManager(ThirstPlayerDataManager thirstPlayerDataManager) {
		this.thirstPlayerDataManager = thirstPlayerDataManager;
	}

	@Override
	public void tick() {
		
	}
	
	@EventHandler
	public void onPotionDrink(PlayerItemConsumeEvent event){
		ItemStack item = event.getItem();
		if(item.getType().equals(Material.POTION)){
			if(item.getData().getData() == 0){
				PlayerData pd = data.get(event.getPlayer().getUniqueId());
				pdataManager getThirstPlayerDataManager() {
		return thirstPlayerDataManager;
	}




	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}
	
}
