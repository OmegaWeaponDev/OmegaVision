package me.omegaweapondev.omegavision;

import me.omegaweapondev.omegavision.command.*;
import me.omegaweapondev.omegavision.events.PlayerListener;
import me.omegaweapondev.omegavision.utils.MessagesHandler;
import me.omegaweapondev.omegavision.utils.Placeholders;
import me.omegaweapondev.omegavision.utils.StorageManager;
import me.omegaweapondev.omegavision.utils.UserDataHandler;
import me.ou.library.SpigotUpdater;
import me.ou.library.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * The core class for OmegaVision
 *
 * @author OmegaWeaponDev
 *
 */
public class OmegaVision extends JavaPlugin {
  private OmegaVision pluginInstance;
  private StorageManager storageManager;
  private MessagesHandler messagesHandler;
  private UserDataHandler userDataHandler;

  /**
   *
   * Allows for enabling of the plugin once the server has started
   *
   */
  @Override
  public void onEnable() {
    // Set the plugin and Library instance to this.
    pluginInstance = this;
    Utilities.setInstance(pluginInstance);

    // Setup the storage manager and configuration files
    storageManager = new StorageManager(pluginInstance);
    storageManager.setupConfigs();
    storageManager.configUpdater();

    // Setup the user data and messages handler
    userDataHandler = new UserDataHandler(pluginInstance);
    messagesHandler = new MessagesHandler(pluginInstance, storageManager.getMessagesFile().getConfig());

    // Print a message to console once the plugin has enabled
    Utilities.logInfo(false,
      "  ____ _   __",
      " / __ \\ | / /   OmegaVision v" + pluginInstance.getDescription().getVersion() + " by OmegaWeaponDev",
      "/ /_/ / |/ /    Running on version: " + Bukkit.getVersion(),
      "\\____/|___/",
      ""
    );

    // Check if PlaceholderAPI is installed. If so, register the plugins placeholders
    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
      Utilities.logWarning(true,
        "OmegaVision requires PlaceholderAPI to be installed if you are wanting to use any of the placeholders",
        "You can install PlaceholderAPI here: https://www.spigotmc.org/resources/placeholderapi.6245/ "
      );
    } else {
      new Placeholders(pluginInstance).register();
    }

    // Register the commands and events
    registerCommands();
    registerEvents();

    // Populate the user data map with entries from the user data file
    getUserDataHandler().populateUserDataMap();

    // Send a message in console if there is a new version of the plugin
    if(getStorageManager().getConfigFile().getConfig().getBoolean("Update_Notify")) {
      new SpigotUpdater(pluginInstance, 73013).getVersion(version -> {
        int spigotVersion = Integer.parseInt(version.replace(".", ""));
        int pluginVersion = Integer.parseInt(pluginInstance.getDescription().getVersion().replace(".", ""));

        if(pluginVersion >= spigotVersion) {
          Utilities.logInfo(true, "You are already running the latest version");
          return;
        }

        PluginDescriptionFile pdf = pluginInstance.getDescription();
        Utilities.logWarning(true,
          "A new version of " + pdf.getName() + " is avaliable!",
          "Current Version: " + pdf.getVersion() + " > New Version: " + version,
          "Grab it here: https://www.spigotmc.org/resources/omegavision.73013/"
        );
      });
    }
  }

  /**
   *
   * Handles disabling the plugin and saving the user data to the files
   *
   */
  @Override
  public void onDisable() {
    getUserDataHandler().saveUserDataToFile();
    Bukkit.getScheduler().cancelTasks(pluginInstance);
  }

  /**
   *
   * Handles reloading the plugin files
   *
   */
  public void onReload() {
    getStorageManager().reloadFiles();
  }

  /**
   *
   * Handles registering the commands for the plugin
   *
   */
  private void registerCommands() {
    Utilities.logInfo(true, "OmegaVision is now attempting to register it's commands...");

    Utilities.setCommand().put("omegavision", new PluginCommand(pluginInstance));
    Utilities.setCommand().put("nightvision", new NightVisionCommand(pluginInstance));
    Utilities.setCommand().put("nightvisionlimit", new LimitCommand(pluginInstance));
    Utilities.setCommand().put("nightvisionlist", new ListCommand(pluginInstance));

    Utilities.registerCommands();
    Utilities.logInfo(true, "OmegaVision has successfully registered all of it's commands.");
  }

  /**
   *
   * Handles registering the events that the plugin listens to
   *
   */
  private void registerEvents() {
    Utilities.registerEvent(new PlayerListener(pluginInstance));
  }

  /**
   *
   * Getter for the StorageManager
   *
   * @return storageManager
   */
  public StorageManager getStorageManager() {
    return storageManager;
  }

  /**
   *
   * Getter for the messagesHandler
   *
   * @return messageHandler
   */
  public MessagesHandler getMessagesHandler() {
    return messagesHandler;
  }

  /**
   *
   * Getter for the userDataHandler
   *
   * @return userDataHandler
   */
  public UserDataHandler getUserDataHandler() {
    return userDataHandler;
  }
}
