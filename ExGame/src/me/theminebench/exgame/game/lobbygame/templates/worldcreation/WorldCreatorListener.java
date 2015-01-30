package me.theminebench.exgame.game.lobbygame.templates.worldcreation;

import java.io.File;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public interface WorldCreatorListener {
	
	public void worldCreated(World world, YamlConfiguration mapData, WorldCreationTemplate worldCreationTemplate);
	
}
