package me.theminebench.exgame.game.eventgame;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import me.theminebench.exgame.ExGame;

import org.apache.commons.lang3.ClassUtils;
import org.bukkit.event.Cancellable;
import org.bukkit.scheduler.BukkitRunnable;

public class GameEventManager {
	
	private ConcurrentHashMap<Class<?>, ConcurrentHashMap<Object, HashSet<Method>>> methods = new ConcurrentHashMap<Class<?>, ConcurrentHashMap<Object, HashSet<Method>>>();
	
	
	
	public void registerListener(Object listener) {
		for (Method m : listener.getClass().getMethods()) {
			if (m.isAnnotationPresent(GameEventHandler.class)) {
				if (m.getParameterCount() == 1) {
					if (!methods.containsKey(m.getParameterTypes()[0])) {
						methods.put(m.getParameterTypes()[0], new ConcurrentHashMap<Object, HashSet<Method>>());
					}
					if (!methods.get(m.getParameterTypes()[0]).containsKey(listener)) {
						methods.get(m.getParameterTypes()[0]).put(listener, new HashSet<Method>());
					}
					methods.get(m.getParameterTypes()[0]).get(listener).add(m);
				}
			}
		}
	}

	public void unregisterListener(Object listener) {
		HashSet<Class<?>> toRemove = new HashSet<Class<?>>();

		for (Class<?> eventClass : methods.keySet()) {
			methods.get(eventClass).remove(listener);

			if (methods.get(eventClass).isEmpty()) {
				toRemove.add(eventClass);
			}
		}

		for (Class<?> eventClass : toRemove) {
			methods.remove(eventClass);
		}
	}

	public void fireEvent(Object event) {
		List<ConcurrentHashMap<Object, HashSet<Method>>> hashMaps = new ArrayList<ConcurrentHashMap<Object, HashSet<Method>>>();
		Class<?> eventClass = me.theminebench.exgame.utils.ClassUtils.getTopClass(event.getClass());
		List<Class<?>> moo = ClassUtils.getAllInterfaces(eventClass);
		moo.addAll(ClassUtils.getAllSuperclasses(eventClass));
		moo.add(eventClass);
		
		for (Class<?> i : moo) {
			if (methods.containsKey(i)) {
				hashMaps.add(methods.get(i));
			}
		}

		if (hashMaps.isEmpty()) {
			return;
		}

		// TODO Do this a better way!

		for (GameEventPriority lep : GameEventPriority.values()) {

			for (ConcurrentHashMap<Object, HashSet<Method>> hashMap : hashMaps) {

				for (Object listener : hashMap.keySet()) {

					for (Method m : hashMap.get(listener)) {

						if (((event instanceof Cancellable)) && (((Cancellable) event).isCancelled())
								&& (m.getAnnotation(GameEventHandler.class).ignoreCancelled()))
							continue;

						if (m.getAnnotation(GameEventHandler.class).priority().equals(lep)) {
							try {
								m.invoke(listener, event);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}