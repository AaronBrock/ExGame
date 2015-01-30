package me.theminebench.exgame.utils;

import java.lang.reflect.Field;

import net.minecraft.server.v1_8_R1.Block;
import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.Packet;
import net.minecraft.server.v1_8_R1.PacketPlayOutBlockAction;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class PacketUtil {

	public void openChest(Location location){
		Material blockType =  location.getBlock().getType();
		if (!blockType.toString().toLowerCase().contains("chest"))
			return;
		BlockFace[] sides = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

		for(BlockFace blockFace : sides){
			if(location.getBlock().getRelative(blockFace, 1).getType().equals(blockType)){
				PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction();
				setPrivateField(PacketPlayOutBlockAction.class, packet, "a", 
						toBlockPositon(location.getBlock().getRelative(blockFace, 1).getLocation()));
				setPrivateField(PacketPlayOutBlockAction.class, packet, "b", (int) 1);
				setPrivateField(PacketPlayOutBlockAction.class, packet, "d", toBlock(blockType));
				setPrivateField(PacketPlayOutBlockAction.class, packet, "c", (int) 1);
				sendPacket(packet, Bukkit.getOnlinePlayers());
				break;
			}
		}

		PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction();
		setPrivateField(PacketPlayOutBlockAction.class, packet, "a", toBlockPositon(location));
		setPrivateField(PacketPlayOutBlockAction.class, packet, "b", (int) 1);
		setPrivateField(PacketPlayOutBlockAction.class, packet, "d", toBlock(blockType));
		setPrivateField(PacketPlayOutBlockAction.class, packet, "c", (int) 1);
		sendPacket(packet, Bukkit.getOnlinePlayers());
	}

	public void closeChest(Location location){
		Material blockType =  location.getBlock().getType();
		if (!blockType.toString().toLowerCase().contains("chest"))
			return;
		BlockFace[] sides = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

		for(BlockFace blockFace : sides){
			if(location.getBlock().getRelative(blockFace, 1).getType().equals(blockType)){
				PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction();
				setPrivateField(PacketPlayOutBlockAction.class, packet, "a", 
						toBlockPositon(location.getBlock().getRelative(blockFace, 1).getLocation()));
				setPrivateField(PacketPlayOutBlockAction.class, packet, "b", (int) 1);
				setPrivateField(PacketPlayOutBlockAction.class, packet, "d", toBlock(blockType));
				setPrivateField(PacketPlayOutBlockAction.class, packet, "c", (int) 0);
				sendPacket(packet, Bukkit.getOnlinePlayers());
				break;
			}
		}
		PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction();
		setPrivateField(PacketPlayOutBlockAction.class, packet, "a", toBlockPositon(location));
		setPrivateField(PacketPlayOutBlockAction.class, packet, "b", (int) 1);
		setPrivateField(PacketPlayOutBlockAction.class, packet, "d", toBlock(blockType));
		setPrivateField(PacketPlayOutBlockAction.class, packet, "c", (int) 0);
		sendPacket(packet, Bukkit.getOnlinePlayers());
	}


	private Block toBlock(Material material){
		return Block.getById(material.getId());
	}

	private BlockPosition toBlockPositon(Location location){
		return new BlockPosition(
				location.getBlockX(), 
				location.getBlockY(), 
				location.getBlockZ());
	}


	private void sendPacket(Packet packet, Player[] newPlayers){
		for (Player player : newPlayers) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	private void setPrivateField(Class<?> type, Object object,
			String name, Object value) {
		try {
			Field f = type.getDeclaredField(name);
			f.setAccessible(true);
			f.set(object, value);
			f.setAccessible(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
