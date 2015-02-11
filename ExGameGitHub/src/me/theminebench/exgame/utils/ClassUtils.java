package me.theminebench.exgame.utils;

public class ClassUtils {
	private ClassUtils() {}
	
	public static Class<?> getTopClass(Class<?> clazz) {
		
		while (clazz.getEnclosingClass() != null) {
			clazz = clazz.getEnclosingClass();
		}
		
		return clazz;
		
	}
}
