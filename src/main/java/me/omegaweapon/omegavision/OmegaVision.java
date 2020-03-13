package me.omegaweapon.omegavision;

import me.omegaweapon.omegavision.command.MainCommand;
import me.omegaweapon.omegavision.events.PlayerListener;
import me.omegaweapon.omegavision.settings.ConfigFile;
import me.omegaweapon.omegavision.settings.MessagesFile;
import me.omegaweapon.omegavision.settings.PlayerData;
import me.omegaweapon.omegavision.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class OmegaVision extends JavaPlugin {
	private static OmegaVision instance;

	@Override
	public void onEnable() {
		instance = this;
		Logger logger = this.getLogger();
		
		// Logs a message to console, saying that the plugin has enabled correctly.
		logger.info("OmegaVision has been enabled.");
		
		// Register the command
		Utilities.registerCommand(new MainCommand(this));
		
		// Register the player Listener
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		
		// Setup the files
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		
		ConfigFile.init();
		MessagesFile.init();
		PlayerData.setupPlayerData();
		
		// Update Checker
		new OmegaUpdater(73013) {
			
			@Override
			public void onUpdateAvailable() {
				logger.info("A new update has been released!");
				logger.info("Your current version is: " + getDescription().getVersion());
				logger.info("The latest version is: " + OmegaUpdater.getLatestVersion());
				logger.info("You can update here: https://www.spigotmc.org/resources/omegavision." + OmegaUpdater.getProjectId());
			}
		}.runTaskAsynchronously(this);
	}

	@Override
	public void onDisable() {
		instance = null;
		super.onDisable();
	}
	
	public void onReload() {
		ConfigFile.init();
		MessagesFile.init();
		PlayerData.reloadPlayerData();
	}

	public static OmegaVision getInstance() {
		return instance;
	}
}
