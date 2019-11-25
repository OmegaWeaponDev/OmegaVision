package me.omegaweapon.omegavision;

import me.omegaweapon.omegavision.command.OmegaVisionCommand;
import me.omegaweapon.omegavision.events.PlayerListener;
import me.omegaweapon.omegavision.settings.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class OmegaVision extends JavaPlugin {
	private File messagesFile = new File(getDataFolder(), "messages.yml");
	private FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

	@Override
	public void onEnable() {
		// Logs a message to console, saying that the plugin has enabled correctly.
		getLogger().info("OmegaVision has been enabled.");

		// Creates the config.yml
		this.saveDefaultConfig();

		// Creates the playerdata.yml file
		PlayerData.setupPlayerData();

		// Checks if the messages file exists, if not, creates it.
		if(!messagesFile.exists()) {
			saveResource("messages.yml", false);
		}

		// Register the main command
		this.getCommand("omegavision").setExecutor(new OmegaVisionCommand(this));

		// Registers the playerlistener event
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

		// Plugin Updater
		Logger logger = this.getLogger();

//		new OmegaUpdater(this, 12345).getVersion(version -> {
//			if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
//				logger.info("There is not a new update available.");
//			} else {
//				logger.info("There is a new update available.");
//			}
//		});
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void reloadConfig() {
		super.reloadConfig();
		messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
	}

	public FileConfiguration getMessagesConfig() {
		return messagesConfig;
	}

	public File getMessagesFile() {
		return messagesFile;
	}
}
