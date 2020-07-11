package me.omegaweapon.omegavision;

import me.omegaweapon.omegavision.command.LimitCommand;
import me.omegaweapon.omegavision.command.ListCommand;
import me.omegaweapon.omegavision.command.MainCommand;
import me.omegaweapon.omegavision.command.ToggleCommand;
import me.omegaweapon.omegavision.events.PlayerListener;
import me.ou.library.Utilities;
import me.ou.library.configs.ConfigCreator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;

public class OmegaVision extends JavaPlugin {
  private static final ConfigCreator configFile = new ConfigCreator("config.yml");
  private static final ConfigCreator messagesFile = new ConfigCreator("messages.yml");
  private static final ConfigCreator playerData = new ConfigCreator("playerData.yml");
  private static OmegaVision instance;
  
  public static ConfigCreator getConfigFile() {
    return configFile;
  }
  
  @Override
  public void onDisable() {
    instance = null;
    super.onDisable();
  }
  
  public void onReload() {
    configFile.reloadConfig();
    messagesFile.reloadConfig();
    playerData.reloadConfig();
  }

  public static ConfigCreator getMessagesFile() {
    return messagesFile;
  }

  public static ConfigCreator getPlayerData() {
    return playerData;
  }

  public static OmegaVision getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;
    Utilities.setInstance(this);

    // Logs a message to console, saying that the plugin has enabled correctly.
    Utilities.logInfo(true,
      "  ____ _   __",
      " / __ \\ | / /   OmegaVision v" + OmegaVision.getInstance().getDescription().getVersion() + " by OmegaWeaponDev",
      "/ /_/ / |/ /    A modern nightvision plugin",
      "\\____/|___/     Currently supporting Spigot 1.13 - 1.15.2",
      ""
    );

    getConfigFile().createConfig();
    getMessagesFile().createConfig();
    getPlayerData().createConfig();

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

    // Register the commands
    Utilities.registerCommand("omegavision", new MainCommand());
    Utilities.registerCommand("nightvision", new ToggleCommand());
    Utilities.registerCommand("nightvisionlist", new ListCommand());
    Utilities.registerCommand("nvlimit", new LimitCommand());

    // Register the player Listener
    Utilities.registerEvent(new PlayerListener());

    // Setup bStats
    final int bstatsPluginId = 7489;
    Metrics metrics = new Metrics(getInstance(), bstatsPluginId);

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
}
