package me.omegaweapon.omegavision;

import me.omegaweapon.omegavision.command.*;
import me.omegaweapon.omegavision.events.PlayerListener;
import me.ou.library.Utilities;
import me.ou.library.configs.ConfigCreator;
import me.ou.library.configs.ConfigUpdater;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;

import java.io.IOException;
import java.util.Arrays;

public class OmegaVision extends JavaPlugin {
  private static OmegaVision instance;

  private final ConfigCreator configFile = new ConfigCreator("config.yml");
  private final ConfigCreator messagesFile = new ConfigCreator("messages.yml");
  private final ConfigCreator playerData = new ConfigCreator("playerData.yml");

  public static OmegaVision getInstance() {
    return instance;
  }

  private void initialSetup() {

    // Set the instance for the plugin and the OU library
    instance = this;
    Utilities.setInstance(this);

    // Setup bStats
    final int bstatsPluginId = 7489;
    Metrics metrics = new Metrics(getInstance(), bstatsPluginId);

    // Logs a message to console, saying that the plugin has enabled correctly.
    Utilities.logInfo(true,
      "  ____ _   __",
      " / __ \\ | / /   OmegaVision v" + OmegaVision.getInstance().getDescription().getVersion() + " by OmegaWeaponDev",
      "/ /_/ / |/ /    A modern nightvision plugin",
      "\\____/|___/     Currently supporting Spigot 1.13 - 1.16.1",
      ""
    );
  }

  private void setupConfigs() {
    // Create all the config files
    getConfigFile().createConfig();
    getMessagesFile().createConfig();
    getPlayerData().createConfig();

    // Add a header to the playerData file.
    getPlayerData().getConfig().options().header(
      " -------------------------------------------------------------------------------------------\n" +
        " \n" +
        " Welcome to OmegaVision's Player Data file.\n" +
        " \n" +
        " This file contains all the uuids and nightivision status\n" +
        " for all the players who have the permission omegavision.login\n" +
        " \n" +
        " -------------------------------------------------------------------------------------------"
    );
  }

  @Override
  public void onEnable() {
    initialSetup();
    setupConfigs();
    configUpdater();
    setupCommands();
    setupEvents();
    spigotUpdater();
  }

  private void setupEvents() {
    // Register events
    Utilities.registerEvents(new PlayerListener());
  }

  private void spigotUpdater() {
    // The Updater
    new UpdateChecker(this, 73013).getVersion(version -> {
      if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
        Utilities.logInfo(true, "You are already running the latest version");
      } else {
        PluginDescriptionFile pdf = this.getDescription();
        Utilities.logWarning(true,
          "A new version of " + pdf.getName() + " is avaliable!",
          "Current Version: " + pdf.getVersion() + " > New Version: " + version,
          "Grab it here: https://spigotmc.org/resources/73013"
        );
      }
    });
  }

  private void setupCommands() {
    // Register the commands
    Utilities.logInfo(true, "Registering Commands...");

    Utilities.setCommand().put("omegavision", new MainCommand());
    Utilities.setCommand().put("nightvision", new ToggleCommand());
    Utilities.setCommand().put("nightvisionlist", new ListCommand());
    Utilities.setCommand().put("nvlimit", new LimitCommand());
    Utilities.setCommand().put("nvtemp", new ToggleTempCommand());
    Utilities.setCommand().put("nvall", new ToggleAllCommand());

    Utilities.registerCommands();
    Utilities.logInfo(true, "Commands Registered: " + Utilities.setCommand().size());
  }

  @Override
  public void onDisable() {
    instance = null;
    super.onDisable();
  }

  public void onReload() {
    // Reload all the config files.
    configFile.reloadConfig();
    messagesFile.reloadConfig();
    playerData.reloadConfig();
  }

  private void configUpdater() {
    Utilities.logInfo(true, "Attempting to update the config files....");

    try {
      if(getConfigFile().getConfig().getDouble("Config_Version") != 1.1) {
        getConfigFile().getConfig().set("Config_Version", 1.1);
        getConfigFile().saveConfig();
        ConfigUpdater.update(OmegaVision.getInstance(), "config.yml", getConfigFile().getFile(), Arrays.asList("none"));
      }

      if(getMessagesFile().getConfig().getDouble("Config_Version") != 1.1) {
        getMessagesFile().getConfig().set("Config_Version", 1.1);
        getMessagesFile().saveConfig();
        ConfigUpdater.update(OmegaVision.getInstance(), "messages.yml", getMessagesFile().getFile(), Arrays.asList("none"));
      }
      onReload();
      Utilities.logInfo(true, "Config Files have successfully been updated!");
    } catch(IOException ex) {
      ex.printStackTrace();
    }
  }

  public ConfigCreator getConfigFile() {
    return configFile;
  }

  public ConfigCreator getMessagesFile() {
    return messagesFile;
  }

  public ConfigCreator getPlayerData() {
    return playerData;
  }
}
