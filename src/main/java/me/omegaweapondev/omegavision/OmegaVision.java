package me.omegaweapondev.omegavision;

import me.omegaweapondev.omegavision.command.*;
import me.omegaweapondev.omegavision.events.PlayerListener;
import me.omegaweapondev.omegavision.utils.MessageHandler;
import me.omegaweapondev.omegavision.utils.Placeholders;
import me.omegaweapondev.omegavision.utils.SettingsHandler;
import me.omegaweapondev.omegavision.utils.UserDataHandler;
import me.ou.library.SpigotUpdater;
import me.ou.library.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;

public class OmegaVision extends JavaPlugin {
  private OmegaVision plugin;
  private SettingsHandler settingsHandler;
  private MessageHandler messageHandler;
  private UserDataHandler userData;

  @Override
  public void onEnable() {
    plugin = this;
    settingsHandler = new SettingsHandler(plugin);
    messageHandler = new MessageHandler(plugin);
    userData = new UserDataHandler(plugin);

    initialSetup();
    getSettingsHandler().setupConfigs();
    getSettingsHandler().configUpdater();
    getUserData().createUserFile();
    setupCommands();
    setupEvents();
    spigotUpdater();
    getUserData().populateMapsOnEnable();
  }

  @Override
  public void onDisable() {
    plugin = null;

    getUserData().clearMapsOnDisable();
    super.onDisable();
  }

  public void onReload() {
    // Reload all the config files.
    getSettingsHandler().reloadFiles();
  }


  private void initialSetup() {

    // Set the instance for the plugin and the OU library
    plugin = this;
    Utilities.setInstance(this);

    // Setup bStats
    final int bstatsPluginId = 7489;
    Metrics metrics = new Metrics(plugin, bstatsPluginId);

    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      new Placeholders(plugin).register();
    }

    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
      Utilities.logWarning(true,
        "DeathWarden requires PlaceholderAPI to be installed if you are wanting to use the placeholders",
        "You can install PlaceholderAPI here: https://www.spigotmc.org/resources/placeholderapi.6245/ "
      );
    }

    // Logs a message to console, saying that the plugin has enabled correctly.
    Utilities.logInfo(true,
      "  ____ _   __",
      " / __ \\ | / /   OmegaVision v" + plugin.getDescription().getVersion() + " by OmegaWeaponDev",
      "/ /_/ / |/ /    A modern nightvision plugin",
      "\\____/|___/     Currently supporting Spigot 1.13 - 1.16",
      ""
    );
  }

  private void spigotUpdater() {

    // The Updater
    new SpigotUpdater(this, 73013).getVersion(version -> {
      int spigotVersion = Integer.parseInt(version.replace(".", ""));
      int pluginVersion = Integer.parseInt(this.getDescription().getVersion().replace(".", ""));

      if(pluginVersion >= spigotVersion) {
        Utilities.logInfo(true, "You are already running the latest version");
        return;
      }

      PluginDescriptionFile pdf = this.getDescription();
      Utilities.logWarning(true,
        "A new version of " + pdf.getName() + " is avaliable!",
        "Current Version: " + pdf.getVersion() + " > New Version: " + version,
        "Grab it here: https://github.com/OmegaWeaponDev/OmegaVision"
      );
    });
  }

  private void setupCommands() {
    // Register the commands
    Utilities.logInfo(true, "Registering Commands...");

    Utilities.setCommand().put("omegavision", new PluginCommand(plugin));
    Utilities.setCommand().put("nightvision", new ToggleCommand(plugin));
    Utilities.setCommand().put("nightvisionlist", new ListCommand(plugin));
    Utilities.setCommand().put("nightvisionlimit", new LimitCommand(plugin));
    Utilities.setCommand().put("nightvisiontemp", new ToggleTempCommand(plugin));
    Utilities.setCommand().put("nightvisionall", new ToggleAllCommand(plugin));

    Utilities.registerCommands();
    Utilities.logInfo(true, "Commands Registered: " + Utilities.setCommand().size());
  }

  private void setupEvents() {
    // Register events
    Utilities.registerEvents(new PlayerListener(plugin));
  }

  public SettingsHandler getSettingsHandler() {
    return settingsHandler;
  }

  public MessageHandler getMessageHandler() {
    return messageHandler;
  }

  public UserDataHandler getUserData() {
    return userData;
  }
}
