package me.omegaweapondev.omegavision.utils;

import me.omegaweapondev.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class MessageHandler {
  private final OmegaVision plugin;
  private final FileConfiguration messagesConfig;
  private final String configName;

  public MessageHandler(final OmegaVision plugin) {
    this.plugin = plugin;
    this.messagesConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "messages.yml"));
    this.configName = plugin.getSettingsHandler().getMessagesFile().getFileName();
  }

  public String string(final String message, final String fallbackMessage) {
    if(messagesConfig.getString(message) == null) {
      getErrorMessage(message);
      return getPrefix() + fallbackMessage;
    }
    return getPrefix() + messagesConfig.getString(message);
  }

  public String console(final String message, final String fallbackMessage) {
    if(messagesConfig.getString(message) == null) {
      getErrorMessage(message);
      return fallbackMessage;
    }
    return messagesConfig.getString(message);
  }

  public String getPrefix() {
    if(messagesConfig.getString("Prefix") == null) {
      getErrorMessage("Prefix");
      return "&7&l[&aOV&7&l]" + " ";
    }
    return messagesConfig.getString("Prefix") + " ";
  }

  private void getErrorMessage(final String message) {
    Utilities.logInfo(true,
      "There was an error getting the " + message + " message from the " + configName + ".",
      "I have set a fallback message to take it's place until the issue is fixed.",
      "To resolve this, please locate " + message + " in the " + configName + " and fix the issue."
    );
  }
}
