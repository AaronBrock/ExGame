package me.theminebench.exgame.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemStackUtil {
	private ItemStackUtil() {}
	
	public static ItemStack newItemStack(ItemStack itemStack, int amount) {
		ItemStack is = new ItemStack(itemStack);
		is.setAmount(amount);
		return is;
	}
	
	public static boolean isItemNothing(ItemStack i) {
		return (i == null || i.getType() == Material.AIR);
	}
	
	public static boolean hasSpace(Inventory inv, ItemStack i) {
		return spaceAllowed(inv, i) >= i.getAmount();
	}
	
	public static int spaceAllowed(Inventory inv, ItemStack i) {
		int count = 0;
		
		for (ItemStack is : inv.getContents()) {
				if (isItemNothing(is))
					count += i.getMaxStackSize();
				else if (is.isSimilar(i))
					count += i.getMaxStackSize() - is.getAmount();

		}
		return count;
	}
	
	public static boolean isArmor(Material material) {
		return (isHelmet(material) || isChestplate(material) || isLeggings(material) || isBoots(material));
	}
	
	public static boolean isHelmet(Material material) {
		return material.toString().toLowerCase().contains("Helmet".toLowerCase());
	}
	
	public static boolean isChestplate(Material material) {
		return material.toString().toLowerCase().contains("Chestplate".toLowerCase());
	}
	
	public static boolean isLeggings(Material material) {
		return material.toString().toLowerCase().contains("Leggings".toLowerCase());
	}
	
	public static boolean isBoots(Material material) {
		return material.toString().toLowerCase().contains("Boots".toLowerCase());
	}
	
	
	public static boolean isSword(Material material) {
		return material.toString().toLowerCase().contains("_Sword".toLowerCase());
	}
	public static boolean isAxe(Material material) {
		return material.toString().toLowerCase().contains("_Axe".toLowerCase());
	}
	public static boolean isPickaxe(Material material) {
		return material.toString().toLowerCase().contains("_Pickaxe".toLowerCase());
	}
	public static boolean isSpade(Material material) {
		return material.toString().toLowerCase().contains("_Spade".toLowerCase());
	}
	public static boolean isHoe(Material material) {
		return material.toString().toLowerCase().contains("_Hoe".toLowerCase());
	}
	
	public ItemStack[] getPlayerInventoryItems(PlayerInventory pi) {
		return ArrayUtils.addAll(pi.getArmorContents(), pi.getContents());
	}
	
	
	public static int getWeight(ItemStack is) {
		return (int) getDoubleWeight(is);
	}
	
	@SuppressWarnings("incomplete-switch")
	public static double getDoubleWeight(ItemStack is) {
		if (is == null)
			return 0;

		switch (is.getType()) {
	        case GRASS:
	               return 1;
	        case ANVIL:
	               return 50;
	        case APPLE:
	               return 0.5;
	        case ARROW:
	               return 0.3;
	        case BAKED_POTATO:
	               return 0.1;
	        case BEDROCK:
	               return 100;
	        case BOAT:
	               return 6;
	        case BONE:
	               return 3;
	        case BOOK:
	               return 5;
	        case BOOK_AND_QUILL:
	               return 5;
	        case BOW:
	               return 7;
	        case BOWL:
	               return 1;
	        case BREAD:
	               return 3;
	        case BROWN_MUSHROOM:
	               return 0;

	        case BUCKET:
	               return 6;
	        case BURNING_FURNACE:
	               return 20;
	        case CACTUS:
	               return 5;
	        case CAKE:
	               return 8;
	        case CAKE_BLOCK:
	               return 8;
	        case CARROT:
	               return 0.4;
	        case CARROT_STICK:
	               return 6;
	        case CHAINMAIL_BOOTS:
	               return 8;
	        case CHAINMAIL_CHESTPLATE:
	               return 25;
	        case CHAINMAIL_HELMET:
	               return 7;
	        case CHAINMAIL_LEGGINGS:
	               return 14;
	        case CHEST:
	               return 10;
	        case COAL:
	               return 3;
	        case COAL_BLOCK:
	               return 11;
	        case COAL_ORE:
	               return 3;
	        case COCOA:
	               return 0.3;
	        case COMPASS:
	               return 5;
	        case COOKED_BEEF:
	               return 5;
	        case COOKED_CHICKEN:
	               return 5;
	        case COOKED_FISH:
	               return 3;
	        case COOKED_MUTTON:
	               return 6;
	        case COOKED_RABBIT:
	               return 4;
	        case COOKIE:
	               return 0.3;
	        case CROPS:
	               return 0;
	        case DIAMOND:
	               return 6;
	        case DIAMOND_AXE:
	               return 16;
	        case DIAMOND_BARDING:
	               return 0;
	        case DIAMOND_BLOCK:
	               return 80;
	        case DIAMOND_BOOTS:
	               return 17;
	        case DIAMOND_CHESTPLATE:
	               return 56;
	        case DIAMOND_HELMET:
	               return 15;
	        case DIAMOND_HOE:
	               return 14;
	        case DIAMOND_LEGGINGS:
	               return 27;
	        case DIAMOND_ORE:
	               return 10;
	        case DIAMOND_PICKAXE:
	               return 20;
	        case DIAMOND_SPADE:
	               return 10;
	        case DIAMOND_SWORD:
	               return 15;
	        case DIRT:
	               return 4;
	        case EGG:
	               return 0.2;
	        case EMERALD:
	               return 6;
	        case EMERALD_BLOCK:
	               return 20;
	        case ENCHANTED_BOOK:
	               return 2;
	        case ENCHANTMENT_TABLE:
	               return 16;
	        case ENDER_CHEST:
	               return 100;
	        case ENDER_PEARL:
	               return 25;
	        case EXPLOSIVE_MINECART:
	               return 16;
	        case EXP_BOTTLE:
	               return 3;
	        case FEATHER:
	               return 0;
	        case FISHING_ROD:
	               return 6;
	        case FLINT:
	               return 3;
	        case FLINT_AND_STEEL:
	               return 7;
	        case GLASS_BOTTLE:
	               return 2;
	        case GOLDEN_CARROT:
	               return 7;
	        case GOLD_AXE:
	               return 6;
	        case GOLD_BARDING:
	               return 0;
	        case GOLD_BLOCK:
	               return 24;
	        case GOLD_BOOTS:
	               return 16;
	        case GOLD_CHESTPLATE:
	               return 22;
	        case GOLD_HELMET:
	               return 16;
	        case GOLD_HOE:
	               return 8;
	        case GOLD_INGOT:
	               return 6;
	        case GOLD_LEGGINGS:
	               return 19;
	        case GOLD_NUGGET:
	               return 1;
	        case GOLD_ORE:
	               return 8;
	        case GOLD_PICKAXE:
	               return 13;
	        case GOLD_PLATE:
	               return 9;
	        case GOLD_RECORD:
	               return 8;
	        case GOLD_SPADE:
	               return 9;
	        case GOLD_SWORD:
	               return 12;
	        case IRON_AXE:
	               return 13;
	        case IRON_BARDING:
	               return 16;
	        case IRON_BLOCK:
	               return 38;
	        case IRON_BOOTS:
	               return 24;
	        case IRON_CHESTPLATE:
	               return 35;
	        case IRON_DOOR:
	               return 16;
	        case IRON_DOOR_BLOCK:
	               return 16;
	        case IRON_FENCE:
	               return 16;
	        case IRON_HELMET:
	               return 21;
	        case IRON_HOE:
	               return 13;
	        case IRON_INGOT:
	               return 10;
	        case IRON_LEGGINGS:
	               return 28;
	        case IRON_ORE:
	               return 16;
	        case IRON_PICKAXE:
	               return 17;
	        case IRON_PLATE:
	               return 14;
	        case IRON_SPADE:
	               return 10;
	        case IRON_SWORD:
	               return 13;
	        case ITEM_FRAME:
	               return 6;
	        case LAVA_BUCKET:
	               return 16;
	        case LEASH:
	               return 1;
	        case LEATHER:
	               return 4;
	        case LEATHER_BOOTS:
	               return 14;
	        case LEATHER_CHESTPLATE:
	               return 23;
	        case LEATHER_HELMET:
	               return 13;
	        case LEATHER_LEGGINGS:
	               return 16;
	        case MAP:
	               return 3;
	        case MELON:
	               return 1;
	        case MELON_BLOCK:
	               return 4;
	        case MELON_SEEDS:
	               return 0.3;
	        case MILK_BUCKET:
	               return 5;
	        case MUSHROOM_SOUP:
	               return 10;
	        case MUTTON:
	               return 7;
	        case POISONOUS_POTATO:
	               return 6;
	        case PORK:
	               return 6;
	        case POTATO:
	               return 3;
	        case POTATO_ITEM:
	               return 3;
	        case POTION:
	               return 3;
	        case PUMPKIN:
	               return 7;
	        case PUMPKIN_PIE:
	               return 8;
	        case PUMPKIN_SEEDS:
	               return 0.3;
	        case RABBIT:
	               return 16;
	        case RABBIT_FOOT:
	               return 6;
	        case RABBIT_HIDE:
	               return 5;
	        case RABBIT_STEW:
	               return 5;
	        case RAILS:
	               return 17;
	        case RAW_BEEF:
	               return 5;
	        case RAW_CHICKEN:
	               return 6;
	        case RAW_FISH:
	               return 5;
	        case REDSTONE:
	               return 3;
	        case REDSTONE_BLOCK:
	               return 10;
	        case REDSTONE_TORCH_ON:
	               return 7;
	        case REDSTONE_WIRE:
	               return 1;
	        case RED_MUSHROOM:
	               return 1;
	        case RED_ROSE:
	               return 0.3;
	        case SADDLE:
	               return 13;
	        case SKULL:
	               return 16;
	        case SKULL_ITEM:
	               return 16;
	        case SLIME_BLOCK:
	               return 23;
	        case SNOW_BALL:
	               return 7;
	        case STICK:
	               return 1;
	        case STONE:
	               return 1;
	        case STONE_AXE:
	               return 8;
	        case STONE_BUTTON:
	               return 4;
	        case STONE_HOE:
	               return 6;
	        case STONE_PICKAXE:
	               return 9;
	        case STONE_PLATE:
	               return 9;
	        case STONE_SPADE:
	               return 7;
	        case STONE_SWORD:
	               return 10;
	        case STORAGE_MINECART:
	               return 16;
	        case STRING:
	               return 0;
	        case SUGAR:
	               return 1;
	        case SULPHUR:
	               return 6;
	        case TNT:
	               return 16;
	        case TORCH:
	               return 6;
	        case TRAPPED_CHEST:
	               return 50;
	        case TRIPWIRE:
	               return 6;
	        case TRIPWIRE_HOOK:
	               return 3;
	        case WATCH:
	               return 16;
	        case WATER_BUCKET:
	               return 14;
	        case WEB:
	               return 1;
	        case WHEAT:
	               return 1;
	        case WOODEN_DOOR:
	               return 10;
	        case WOOD_AXE:
	               return 5;
	        case WOOD_DOOR:
	               return 10;
	        case WOOD_HOE:
	               return 5;
	        case WOOD_PICKAXE:
	               return 5;
	        case WOOD_SPADE:
	               return 5;
	        case WOOD_SWORD:
	               return 5;
	        case WOOL:
	               return 1;
	        case WORKBENCH:
	               return 7;
	        case WRITTEN_BOOK:
	               return 1;
	        case YELLOW_FLOWER:
	               return 0.2;
		}
		return 0;
	}
}