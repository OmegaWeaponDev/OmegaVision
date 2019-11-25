package me.omegaweapon.omegavision;

import me.omegaweapon.omegavision.command.OmegaVisionCommand;
import me.omegaweapon.omegavision.events.PlayerListener;
import me.omegaweapon.omegavision.settings.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class OmegaVision extends JavaPlugin {
	private static OmegaVision instance;
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

		new OmegaUpdater(271) {

			@Override
			public void onUpdateAvailable() {
				getLogger().info( OmegaVision.getInstance().getDescription().getName() + " has been updated!");
				getLogger().info("Your current version is: " + OmegaVision.getInstance().getDescription().getVersion());
				getLogger().info("The latest version is: " + getLatestVersion());
				getLogger().info("You can grab the update here: https://spigotmc.org/resources/" + getProjectId());
			}
		}.runTaskAsynchronously(this);
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

	public static OmegaVision getInstance() {
		return instance;
	}
}
