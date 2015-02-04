package me.theminebench.exgame.game.lobbygame.game.testgame;

import java.util.UUID;

import me.theminebench.exgame.ExGame;
import me.theminebench.exgame.game.eventgame.game.lobbygame.events.GameStateChangeEvent;
import me.theminebench.exgame.game.lobbygame.LobbyGameManager.GameState;
import me.theminebench.exgame.game.lobbygame.events.LobbyEventHandler;
import me.theminebench.exgame.game.lobbygame.game.LobbyGame;
import me.theminebench.exgame.game.lobbygame.templates.LobbyGameTemplate;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class TestGameTemplate implements LobbyGameTemplate {
	
	private LobbyGame lobbyGame;
	
	public TestGameTemplate(LobbyGame lobbyGame) {
		this.lobbyGame = lobbyGame;
		Bukkit.getPluginManager().registerEvents(this, ExGame.getPlugin());
	}
	
	
	@Override
	public void gameStateChange(GameState oldGameState, GameState newGameState) {
		
	}

	@Override
	public boolean canJoin(UUID playersUUID) {
		return true;
	}

	@Override
	public void playerJoin(UUID playersUUID) {
		
	}

	@Override
	public void playerQuit(UUID playersUUID) {
		
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		
		p.sendMessage(e.getMessage());
		
		if (e.getMessage().endsWith("train")) {
			p.sendMessage("There is a monkey with a chainsaw!");
			this.lobbyGame.getLobbyGameManager().fireEvent(new GameStateChangeEvent());
		}
	}
	
	@LobbyEventHandler
	public void moo(GameStateChangeEvent e) {
		Bukkit.broadcastMessage("This is the song that TRAINS");
	}
	
}
