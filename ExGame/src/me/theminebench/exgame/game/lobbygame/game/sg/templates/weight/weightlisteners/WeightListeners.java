package me.theminebench.exgame.game.lobbygame.game.sg.templates.weight.weightlisteners;

import java.util.Map;
import java.util.UUID;

import me.theminebench.exgame.game.lobbygame.game.sg.templates.weight.WeightManager;
import me.theminebench.exgame.utils.ItemStackUtil;
import me.theminebench.exgame.utils.NumberUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class WeightListeners implements Listener {
	
	private WeightManager weightManager;
	
	public WeightListeners(WeightManager weightManager) {
		this.weightManager = weightManager;
	}
	
	private WeightManager getWeightManager() {
		return weightManager;
	}
	
	private int getItemWeight(ItemStack is) {
		return getWeightManager().getItemDataManager().getItemWeight(is);
	}
	
	private int getPlayerWeight(UUID playersUUID) {
		return getWeightManager().getPlayerDataManager().getPlayerWeight(playersUUID);
	}

	private void setPlayerWeight(UUID playersUUID, int weight) {
		getWeightManager().getPlayerDataManager().setPlayerWeight(playersUUID, weight);
	}

	private boolean hasPlayer(UUID playersUUID) {
		return getWeightManager().getPlayerDataManager().hasPlayer(playersUUID);
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		Player p = (Player) e.getWhoClicked();
		UUID u = p.getUniqueId();
		if (!hasPlayer(u) || e.isCancelled())
			return;

		Map<Integer, ItemStack> items = e.getNewItems();
		
		int itemWeight = getItemWeight(e.getOldCursor());
		int amount;
		int weightToAdd = 0;
		
		for (Integer i : items.keySet()) {
			
			if (i >= e.getInventory().getSize()) {
				
				ItemStack is = items.get(i);
				
				amount = items.get(i).getAmount();
				
				if (p.getInventory().getItem(e.getView().convertSlot(i)) != null)
					amount = is.getAmount() - p.getInventory().getItem(e.getView().convertSlot(i)).getAmount();

				weightToAdd += itemWeight * amount;

			}
		}
		
		if (getPlayerWeight(u) + weightToAdd <= getWeightManager().getPlayerDataManager().getMaxWeight(u)) {
			setPlayerWeight(u, getPlayerWeight(u) + weightToAdd);

		} else
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {

		Player p = (Player) e.getWhoClicked();
		UUID u = p.getUniqueId();

		if (!hasPlayer(u) || e.isCancelled())
			return;

		if (e.getSlotType() != SlotType.RESULT) {

			if (e.getClick().equals(ClickType.NUMBER_KEY)) {
				e.setCancelled(true);
				return;
			}

			if (e.getClick().equals(ClickType.DOUBLE_CLICK)) {
				e.setCancelled(true);
				// getWeightManager().updateWeightLater(u);
				return;
			}

			onClickDefault(e);
		} else {

			if ((e.getInventory().getType().equals(InventoryType.CRAFTING) || e.getInventory().getType().equals(InventoryType.WORKBENCH)))
				// Handled at onCraft
				return;

			if (e.getInventory() instanceof AnvilInventory) {
				onClickAnvil(e);
				return;
			}
			onClickDefault(e);
		}
	}
	
	public void onClickDefault(InventoryClickEvent e) {

		if (e.getClick().equals(ClickType.SHIFT_RIGHT) || e.getClick().equals(ClickType.SHIFT_LEFT)) {
			onClickShift(e);

		} else {

			if (e.getClick().equals(ClickType.RIGHT) || e.getClick().equals(ClickType.LEFT)) {
				onClickNormal(e);
				//((Player) e.getWhoClicked()).sendMessage("-------------");
			}
		}
	}
	
	public void onClickAnvil(InventoryClickEvent e) {

		if (!e.getInventory().getType().equals(InventoryType.ANVIL)) {
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "InventoryType." + e.getInventory().getType() + ChatColor.RED + " is not "
					+ ChatColor.DARK_RED + "InventoryType.ANVIL");
			e.setCancelled(true);
			return;
		}

		if (!e.isShiftClick()) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		UUID u = p.getUniqueId();
		ItemStack i = e.getCurrentItem();

		Inventory pInv = p.getInventory();

		if (ItemStackUtil.hasSpace(pInv, i) && getWeightManager().itemsAllowed(u, i) >= i.getAmount()) {
			setPlayerWeight(u, getPlayerWeight(u) + getItemWeight(i) * i.getAmount());

		} else {
			e.setCancelled(true);
		}
	}
	
	public void onClickNormal(InventoryClickEvent e) {

		if (!e.getClick().equals(ClickType.RIGHT) && !e.getClick().equals(ClickType.LEFT)) {
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "ClickType." + e.getClick().toString() + ChatColor.RED + " is not "
					+ ChatColor.DARK_RED + "ClickType.RIGHT" + ChatColor.RED + " or " + ChatColor.DARK_RED + "ClickType.LEFT");
			e.setCancelled(true);
			return;
		}

		final Player p = (Player) e.getWhoClicked();
		UUID u = p.getUniqueId();
		ItemStack current = e.getCurrentItem();

		ItemStack cursor = e.getCursor();

		//p.sendMessage("/NormalClick");

		if (current != null) {
			if (e.getRawSlot() < e.getInventory().getSize()) {

			} else {

				if (!ItemStackUtil.isItemNothing(cursor)) {
					//p.sendMessage("/Something on cursor");

					if (e.getSlotType().equals(SlotType.ARMOR)) {
						int slot = e.getRawSlot();

						if (slot == 5) {
							// helmet
							if (!ItemStackUtil.isHelmet(cursor.getType())) {
								return;
							}
						} else if (slot == 6) {
							// chestplate
							if (!ItemStackUtil.isChestplate(cursor.getType())) {
								return;
							}
						} else if (slot == 7) {
							// leggings
							if (!ItemStackUtil.isLeggings(cursor.getType())) {
								return;
							}
						} else if (slot == 8) {
							// boots
							if (!ItemStackUtil.isBoots(cursor.getType())) {
								return;
							}
						}
					}

					if (cursor.isSimilar(current) || ItemStackUtil.isItemNothing(current)) {
						// PLACE
						//p.sendMessage("/Place");

						ItemStack itemStack;

						if (e.isLeftClick())
							itemStack = cursor;
						else
							itemStack = ItemStackUtil.newItemStack(cursor, 1);

						int allowedInSlot = 0;

						if (current.getType() == Material.AIR)
							allowedInSlot = itemStack.getAmount();
						else if (current.isSimilar(itemStack)) {

							Material m = current.getType();

							if (current.getAmount() + itemStack.getAmount() <= m.getMaxStackSize())
								allowedInSlot = itemStack.getAmount();
							else
								allowedInSlot = m.getMaxStackSize() - current.getAmount();
						}

						int toAdd = NumberUtil.lower(getWeightManager().itemsAllowedDewToWeight(u, cursor), allowedInSlot);

						if (toAdd != 0) {
							setPlayerWeight(u, getPlayerWeight(u) + (getItemWeight(cursor) * toAdd));
							e.setCancelled(true);

							if (cursor.isSimilar(current)) {

								e.setCurrentItem(ItemStackUtil.newItemStack(cursor, toAdd + current.getAmount()));

							} else {
								e.setCurrentItem(ItemStackUtil.newItemStack(cursor, toAdd));
							}
							//p.sendMessage("items on cursor = " + (cursor.getAmount() - toAdd));
							p.setItemOnCursor(ItemStackUtil.newItemStack(cursor, cursor.getAmount() - toAdd));
						} else
							e.setCancelled(true);

					} else if (!cursor.isSimilar(current)) {
						// SWITCH
						//p.sendMessage("/Switch");

						int switchWeight = (getPlayerWeight(u) - (getWeightManager().getItemDataManager().getItemWeight(current) * current
								.getAmount())) + (getItemWeight(cursor) * cursor.getAmount());

						if (switchWeight <= getWeightManager().getPlayerDataManager().getMaxWeight(u)) {
							setPlayerWeight(u, switchWeight);
							return;
						}
					}

					e.setCancelled(true);

				} else if (ItemStackUtil.isItemNothing(cursor) && !ItemStackUtil.isItemNothing(current)) {
					// PICK UP
					//p.sendMessage("/Pick up");

					if (e.isLeftClick()) {
						e.setCancelled(true);

						setPlayerWeight(u, getPlayerWeight(u) - (getItemWeight(current) * current.getAmount()));

						p.setItemOnCursor(current);

						e.setCurrentItem(new ItemStack(Material.AIR));

					} else {

						getWeightManager().updateWeightLater(u);

					}
				}
			}
		}
	}
	
	public void onClickShift(InventoryClickEvent e) {

		if (!e.getClick().equals(ClickType.SHIFT_RIGHT) && !e.getClick().equals(ClickType.SHIFT_LEFT)) {
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "ClickType." + e.getClick().toString() + ChatColor.RED + " is not "
					+ ChatColor.DARK_RED + "ClickType.SHIFT_RIGHT" + ChatColor.RED + " or " + ChatColor.DARK_RED + "ClickType.SHIFT_LEFT");
			e.setCancelled(true);
			return;
		}

		Player p = (Player) e.getWhoClicked();
		UUID u = p.getUniqueId();
		ItemStack current = e.getCurrentItem();

		if (e.getRawSlot() < e.getInventory().getSize()) {

			e.setCancelled(true);

			int weight = getPlayerWeight(u);

			PlayerInventory pi = p.getInventory();

			int itemWeight = getItemWeight(current);

			int toAdd = 0;

			for (int i = 0; i < current.getAmount(); i++) {

				if (weight + itemWeight > getWeightManager().getPlayerDataManager().getMaxWeight(u))
					break;

				toAdd++;
				weight += itemWeight;
			}

			ItemStack stayItem = current.clone();
			ItemStack addItem = current.clone();

			addItem.setAmount(toAdd);

			if (!ItemStackUtil.hasSpace(pi, addItem))
				e.setCancelled(true);

			pi.addItem(addItem);

			setPlayerWeight(u, getPlayerWeight(u) + (getItemWeight(addItem) * toAdd));

			stayItem.setAmount(current.getAmount() - toAdd);

			e.getView().setItem(e.getRawSlot(), stayItem);
		} else {
			getWeightManager().updateWeightLater(u);
		}
	}
	
	// TODO IF PROBLEM ENABLE
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		UUID u = p.getUniqueId();

		if (!hasPlayer(u))
			return;
		getWeightManager().updateWeightLater(u);

	}
	
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		Player p = (Player) e.getWhoClicked();
		UUID u = p.getUniqueId();
		if (!hasPlayer(u) || !e.isShiftClick()) {
			return;
		}

		if (e.isCancelled())
			return;

		e.setCancelled(true);

		CraftingInventory inv = e.getInventory();

		ItemStack is = e.getRecipe().getResult();

		Inventory pInv = p.getInventory();

		//p.sendMessage(ChatColor.BLUE + "" + getWeightManager().itemsAllowed(u, is) + "");

		//p.sendMessage(ChatColor.YELLOW + "" + (Math.floor(getWeightManager().itemsAllowed(u, is) / is.getAmount()) * is.getAmount()) + "");

		int toAdd = (int) (Math.floor(getWeightManager().itemsAllowed(u, is) / is.getAmount()) * is.getAmount());

		int lowest = 999999;
		// Setting i to 1 is so I dont scan the result
		for (int i = 1; i < inv.getSize(); i++) {

			ItemStack item = inv.getItem(i);

			if (item != null) {

				if (item.getAmount() < lowest)

					lowest = item.getAmount();

			}
		}
		lowest = lowest * is.getAmount();

		toAdd = NumberUtil.lower(toAdd, lowest);

		int subtract = toAdd / is.getAmount();

		for (int i = 1; i < inv.getSize(); i++) {

			ItemStack item = inv.getItem(i);

			if (item != null) {

				if (item.getAmount() - subtract == 0) {
					inv.setItem(i, null);
				} else
					inv.setItem(i, ItemStackUtil.newItemStack(item, item.getAmount() - subtract));
			}
		}

		int itemWeight = getItemWeight(is);

		int weightCounter = 0;

		for (int i = 0; i < toAdd; i++) {
			pInv.addItem(ItemStackUtil.newItemStack(is, 1));

			weightCounter += itemWeight;
		}

		setPlayerWeight(u, getPlayerWeight(u) + weightCounter);

	}
	
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		UUID u = p.getUniqueId();
		if (!hasPlayer(u) || e.isCancelled())
			return;

		getWeightManager().updateWeightLater(u);

	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		UUID u = p.getUniqueId();

		if (!hasPlayer(u) || e.isCancelled() || e.getBlock().equals(null) || e.getBlock().getType().equals(Material.AIR))
			return;
		if (e.canBuild() == false)
			return;
		
		//ItemStack is = p.getItemInHand();
		
		//setPlayerWeight(u, getPlayerWeight(u) - getItemWeight(is));
		
		Bukkit.broadcastMessage("we are here scotty!");
		
		getWeightManager().updateWeightLater(u);
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		UUID u = p.getUniqueId();
		if (!hasPlayer(u) || e.isCancelled())
			return;
		
		e.setCancelled(true);
		Item item = e.getItem();
		ItemStack itemStack = item.getItemStack();
		
		int weight = getPlayerWeight(u);
		int itemWeight = getItemWeight(itemStack);
		
		
		int succesful = 0;
		for (int i = 0; i < itemStack.getAmount(); i++) {
			
			ItemStack newItemStack = ItemStackUtil.newItemStack(itemStack, 1);
			
			if (weight + itemWeight <= getWeightManager().getPlayerDataManager().getMaxWeight(u) && ItemStackUtil.hasSpace(p.getInventory(), newItemStack)) {
				
				p.getInventory().addItem(newItemStack);
				p.playSound(item.getLocation(), Sound.ITEM_PICKUP, 0.75f, succesful + 1);
				succesful++;
				weight = weight + itemWeight;
			} else
				break;
		}
		if (succesful != 0) {
			item.setItemStack(ItemStackUtil.newItemStack(itemStack, itemStack.getAmount() - succesful));
			
			setPlayerWeight(u, getPlayerWeight(u) + succesful * itemWeight);
			
		}
		if (item.getItemStack().getAmount() == 0) {
			System.out.print("Removing item!");
			item.remove();
			//System.out.print();
		}
			
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		UUID u = p.getUniqueId();
		if (!hasPlayer(u) || e.isCancelled())
			return;

		if (ItemStackUtil.isItemNothing(p.getItemOnCursor())) {
			setPlayerWeight(u, getPlayerWeight(u) - (getItemWeight(e.getItemDrop().getItemStack()) * e.getItemDrop().getItemStack().getAmount()));
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLaunch(ProjectileLaunchEvent e) {
		Player p = (Player) e.getEntity().getShooter();
		UUID u = p.getUniqueId();
		if (!(e.getEntity().getShooter() instanceof Player))
			return;

		if (!hasPlayer(u) || e.isCancelled())
			return;

		ItemStack itemStack = new ItemStack(getEntityMaterial(e.getEntityType()));

		if (itemStack != null) {
			setPlayerWeight(u, getPlayerWeight(u) - getItemWeight(itemStack));
		}
	}
	
	public Material getEntityMaterial(EntityType e) {
		switch (e) {
		case EGG:
			return Material.EGG;
		case ARROW:
			return Material.ARROW;
		case SNOWBALL:
			return Material.SNOW_BALL;
		case ENDER_PEARL:
			return Material.ENDER_PEARL;
		case ENDER_SIGNAL:
			return Material.EYE_OF_ENDER;
		default:
			return null;
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {

		Player p = e.getPlayer();
		UUID u = p.getUniqueId();

		if (!hasPlayer(u))
			return;

		if (p.getItemInHand().getType().equals(Material.EYE_OF_ENDER) && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			getWeightManager().updateWeightLater(u);
		}
	}
	
	@EventHandler
	public void onLeash(PlayerLeashEntityEvent e) {

		Player p = e.getPlayer();
		UUID u = p.getUniqueId();
		if (!hasPlayer(u) || e.isCancelled())
			return;

		setPlayerWeight(u, getPlayerWeight(u) - getItemWeight(new ItemStack(Material.LEASH)));
	}
	
}
