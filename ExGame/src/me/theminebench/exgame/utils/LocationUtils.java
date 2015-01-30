package me.theminebench.exgame.utils;

import org.bukkit.Location;

public class LocationUtils {
	private LocationUtils() {}
	
	public static String toString(Location loc) {
		
		String world = loc.getWorld().getName();
		
		double x = Math.floor(loc.getX()) + 0.5;
		
		double y = loc.getY();
		
		double z = Math.floor(loc.getZ()) + 0.5;
		
		float yaw = Math.round((loc.getYaw()) / 10) * 10;
		
		float pitch = Math.round((loc.getPitch()) / 10) * 10;
		
		String str = world + ", " + x + ", " + y + ", " + z + ", " + yaw + ", " + pitch;
		
		return str;
	}
	
	public static String toStringExact(Location loc) {
		String world = loc.getWorld().getName();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		String str = world + ", " + x + ", " + y + ", " + z + ", " + yaw + ", " + pitch;
		return str;
	}
	
	public static Location toLocation(String worldName, String str) {
		if (!WorldUtil.isWorldLoaded(worldName)) {
			System.out.println("world aint loaded!");
			return null;
		}
		if (isLocationData(str)) {
			String[] args = str.replace(" ", "").split(",");
			double x = Double.parseDouble(args[0]);
			double y = Double.parseDouble(args[1]);
			double z = Double.parseDouble(args[2]);
			float yaw = Float.parseFloat(args[3]);
			float pitch = Float.parseFloat(args[4]);
			return new Location(WorldUtil.getWorld(worldName), x, y, z, yaw, pitch);
		} else if (isBlockLocationData(str)) {
			String[] args = str.replace(" ", "").split(",");
			int x = (int) Double.parseDouble(args[0]);
			int y = (int) Double.parseDouble(args[1]);
			int z = (int) Double.parseDouble(args[2]);
			return new Location(WorldUtil.getWorld(worldName), x, y, z);
		}
		System.out.println("That aint data!");
		return null;
	}
	
	public static Location toBlockLocation(String worldName, String str) {
		if (!WorldUtil.isWorldLoaded(worldName)) {
			System.out.println("world aint loaded!");
			return null;
		}
		if (isBlockLocationData(str)) {
			String[] args = str.replace(" ", "").split(",");
			double x = Double.parseDouble(args[0]);
			double y = Double.parseDouble(args[1]);
			double z = Double.parseDouble(args[2]);
			return new Location(WorldUtil.getWorld(worldName), x, y, z);
		}
		
		System.out.println("That aint data!");
		return null;
	}
	
	public static boolean isLocationData(String str) {
		
		String[] args = str.replace(" ", "").split(",");
		
		if ((args.length != 5)
			|| (!NumberUtil.isDouble(args[0]))
			|| (!NumberUtil.isDouble(args[1]))
			|| (!NumberUtil.isDouble(args[2]))
			|| (!NumberUtil.isFloat(args[3]))
			|| (!NumberUtil.isFloat(args[4])))
			return false;
		return true;
	}
	
	public static boolean isBlockLocationData(String str) {
		
		String[] args = str.replace(" ", "").split(",");
		
		if ((args.length != 3)
			|| (!NumberUtil.isDouble(args[0]))
			|| (!NumberUtil.isDouble(args[1]))
			|| (!NumberUtil.isDouble(args[2])))
			return false;
		return true;
	}
}
