package me.theminebench.exgame.game.eventgame;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ClassUtils;
import org.bukkit.event.Cancellable;


public class GameEventManager {
	
	private ConcurrentHashMap<Class<?>, ConcurrentHashMap<Object, HashSet<Method>>> methods = new ConcurrentHashMap<Class<?>, ConcurrentHashMap<Object, HashSet<Method>>>();
	
	public void registerListener(Object listener) {
		
		
		for (Method m : listener.getClass().getMethods()) {
			
			if (m.isAnnotationPresent(GameEventHandler.class)) {
				
				if (m.getParameterCount() == 1) {
					
					// if
					// (Object.class.isAssignableFrom(m.getParameterTypes()[0]))
					// {
					
					
					if (!methods.containsKey(m.getParameterTypes()[0])) {

						methods.put(m.getParameterTypes()[0], new ConcurrentHashMap<Object, HashSet<Method>>());

					}
					if (!methods.get(m.getParameterTypes()[0]).containsKey(listener)) {

						methods.get(m.getParameterTypes()[0]).put(listener, new HashSet<Method>());
					}

					methods.get(m.getParameterTypes()[0]).get(listener).add(m);

					// }
				}
			}
		}
	}

	public void unregisterListener(Object listener) {
		System.out.print("=========================================================");
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
		
		for (Class<?> eventClass : methods.keySet()) {
			
			
			System.out.print(methods.get(eventClass).containsKey(listener));
			
		}
		
	}
	
	public void fireEvent(Object event) {

		List<ConcurrentHashMap<Object, HashSet<Method>>> hashMaps = new ArrayList<ConcurrentHashMap<Object,HashSet<Method>>>();
		
		List<Class<?>> moo = ClassUtils.getAllInterfaces(event.getClass());
		moo.addAll(ClassUtils.getAllSuperclasses(event.getClass()));
		moo.add(event.getClass());
		

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