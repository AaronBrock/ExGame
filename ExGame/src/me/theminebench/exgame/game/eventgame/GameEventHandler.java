package me.theminebench.exgame.game.eventgame;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GameEventHandler {
	
	public abstract GameEventPriority priority() default GameEventPriority.NORMAL;
	
	public abstract boolean ignoreCancelled() default false;
	
}
