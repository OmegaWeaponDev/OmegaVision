package me.omegaweapondev.omegavision.utils;

import me.omegaweapondev.omegavision.OmegaVision;
import me.ou.library.Utilities;
import me.ou.library.configs.ConfigCreator;
import me.ou.library.configs.ConfigUpdater;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 * The Storage Manager class that handles all the plugins files
 *
 * @author OmegaWeaponDev
 */
public class StorageManager {
  private final OmegaVision plugin;

  private final ConfigCreator configFile = new ConfigCreator("config.yml");
  private final ConfigCreator messagesFile = new ConfigCreator("messages.yml");
  private final ConfigCreator userDataFile = new ConfigCreator("userData.yml");

  /**
   *
   * The public constructor for the Storage Manager class
   *
   * @param pluginInstance (The plugin's instance)
   */
  public StorageManager(final OmegaVision pluginInstance) {
    this.plugin = pluginInstance;
  }

  /**
   *
   * Handles creating all the files and the data folder for OmegaVision
   *
   */
  public void setupConfigs() {
    getConfigFile().createConfig();
    getMessagesFile().createConfig();
    getUserDataFile().createConfig();

  }

  /**
   *
   * Handles making sure all the files are up-to-date against the default in the resources folder
   *
   */
  public void configUpdater() {
    Utilities.logInfo(true, "Attempting to update the config files....");

    try {
      if(getConfigFile().getConfig().getDouble("Config_Version") != 2.0) {
        getConfigFile().getConfig().set("Config_Version", 2.0);
        getConfigFile().saveConfig();
        ConfigUpdater.update(plugin, "config.yml", getConfigFile().getFile(), Arrays.asList("none"));
        Utilities.logInfo(true, "The config.yml has successfully been updated!");
      }

      if(getMessagesFile().getConfig().getDouble("Config_Version") != 2.0) {
        getMessagesFile().getConfig().set("Config_Version", 2.0);
        getMessagesFile().saveConfig();
        ConfigUpdater.update(plugin, "messages.yml", getMessagesFile().getFile(), Arrays.asList("none"));
        Utilities.logInfo(true, "The messages.yml has successfully been updated!");
      }
      plugin.onReload();
    } catch(IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   *
   * Handles reloading all the files
   *
   */
  public void reloadFiles() {
    getConfigFile().reloadConfig();
    getMessagesFile().reloadConfig();
  }

  /**
   *
   * A getter for the configuration file
   *
   * @return configFile
   */
  public ConfigCreator getConfigFile() {
    return configFile;
  }

  /**
   *
   * A getter for the messages file
   *
   * @return messagesFile
   */
  public ConfigCreator getMessagesFile() {
    return messagesFile;
  }

  /**
   *
   * A getter for the user data file
   *
   * @return userDataFile
   */
  public ConfigCreator getUserDataFile() {
    return userDataFile;
  }
}
