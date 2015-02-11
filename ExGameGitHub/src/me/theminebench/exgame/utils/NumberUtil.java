package me.theminebench.exgame.utils;

public class NumberUtil {
	
	private NumberUtil() {
	}
	
	public static int lower(int a, int b) {
		return a < b ? a : b;
	}
	
	public static float lower(float a, float b) {
		return a < b ? a : b;
	}
	
	public static double lower(double a, double b) {
		return a < b ? a : b;
	}
	
	public static boolean isInt(String str) {
		try {  
			Integer.parseInt(str);  
		}  
		catch(NumberFormatException nfe) {  
			return false;  
		}  
		return true;  
	}

	public static boolean isFloat(String str) {
		try {  
			Float.parseFloat(str);  
		}  
		catch(NumberFormatException nfe) {  
			return false;  
		}
		return true;  
	}
	
	public static boolean isDouble(String str) {
		try {  
			Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe) {  
			return false;  
		}
		return true;  
	}
}
