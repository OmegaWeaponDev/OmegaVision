package me.omegaweapondev.omegavision;

import me.omegaweapondev.omegavision.command.*;
import me.omegaweapondev.omegavision.events.PlayerListener;
import me.omegaweapondev.omegavision.utils.MessagesHandler;
import me.omegaweapondev.omegavision.utils.Placeholders;
import me.omegaweapondev.omegavision.utils.StorageManager;
import me.omegaweapondev.omegavision.utils.UserDataHandler;
import me.ou.library.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class OmegaVision extends JavaPlugin {
  private OmegaVision pluginInstance;
  private StorageManager storageManager;
  private MessagesHandler messagesHandler;
  private UserDataHandler userDataHandler;

  @Override
  public void onEnable() {
    pluginInstance = this;
    Utilities.setInstance(pluginInstance);

    storageManager = new StorageManager(pluginInstance);
    getStorageManager().setupConfigs();
    getStorageManager().configUpdater();

    userDataHandler = new UserDataHandler(pluginInstance);
    messagesHandler = new MessagesHandler(pluginInstance);

    Utilities.logInfo(false,
      "  ____ _   __",
      " / __ \\ | / /   OmegaVision v" + pluginInstance.getDescription().getVersion() + " by OmegaWeaponDev",
      "/ /_/ / |/ /    Running on version: " + Bukkit.getVersion(),
      "\\____/|___/",
      ""
    );

    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
      Utilities.logWarning(true,
        "OmegaVision requires PlaceholderAPI to be installed if you are wanting to use any of the placeholders",
        "You can install PlaceholderAPI here: https://www.spigotmc.org/resources/placeholderapi.6245/ "
      );
    } else {
      new Placeholders(pluginInstance).register();
    }

    registerCommands();
    registerEvents();

    getUserDataHandler().populateUserDataMap();
  }

  @Override
  public void onDisable() {
    getUserDataHandler().saveUserDataToFile();
  }

  public void onReload() {
    getStorageManager().reloadFiles();
  }

  private void registerCommands() {
    Utilities.logInfo(true, "OmegaVision is now attempting to register it's commands...");

    Utilities.setCommand().put("omegavision", new PluginCommand(pluginInstance));
    Utilities.setCommand().put("nightvision", new NightVisionCommand(pluginInstance));
    Utilities.setCommand().put("nightvisionlimit", new LimitCommand(pluginInstance));
    Utilities.setCommand().put("nightvisionlist", new ListCommand(pluginInstance));

    Utilities.registerCommands();
    Utilities.logInfo(true, "OmegaVision has successfully registered all of it's commands.");
  }

  private void registerEvents() {
    Utilities.registerEvent(new PlayerListener(pluginInstance));
  }

  public StorageManager getStorageManager() {
    return storageManager;
  }

  public MessagesHandler getMessagesHandler() {
    return messagesHandler;
  }

  public UserDataHandler getUserDataHandler() {
    return userDataHandler;
  }
}
