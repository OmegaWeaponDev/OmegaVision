package me.omegaweapon.omegavision;

import me.omegaweapon.omegavision.command.OmegaVisionCommand;
import me.omegaweapon.omegavision.events.PlayerListener;
import me.omegaweapon.omegavision.settings.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class OmegaVision extends JavaPlugin {
	private static OmegaVision instance;
	private File messagesFile = new File(getDataFolder(), "messages.yml");
	private FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

	private File configFile = new File(getDataFolder(), "config.yml");
	private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

	@Override
	public void onEnable() {
		instance = this;

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

		new OmegaUpdater(73013) {
			final PluginDescriptionFile pdf = OmegaVision.getInstance().getDescription();

			@Override
			public void onUpdateAvailable() {
				logger.info(pdf.getName() + " has been updated!");
				logger.info("Your current version: " + pdf.getVersion());
				logger.info("Latest version: " + getLatestVersion());
				logger.info("Get the update here: https://spigotmc.org/resources/" + getProjectId());
			}
		}.runTaskAsynchronously(this);
	}

	@Override
	public void onDisable() {
		instance = null;
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

	public static OmegaVision getInstance() {
		return instance;
	}
}
