package me.omegaweapondev.omegavision.utils;

import me.omegaweapondev.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class MessagesHandler {
  private final OmegaVision pluginInstance;
  private final FileConfiguration messagesConfig;
  private final String configName;

  public MessagesHandler(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    this.messagesConfig = YamlConfiguration.loadConfiguration(new File(pluginInstance.getDataFolder() + File.separator + "messages.yml"));
    this.configName = pluginInstance.getStorageManager().getMessagesFile().getFileName();
  }

  public String string(final String message, final String fallbackMessage) {
    if(messagesConfig.getString(message) == null) {
      getErrorMessage(message);
      return getPrefix() + fallbackMessage;
    }
    return getPrefix() + messagesConfig.getString(message);
  }

  public List<String> stringList(final String message, final List<String> fallbackMessage) {
    if(messagesConfig.getStringList(message).isEmpty()) {
      getErrorMessage(message);
      return fallbackMessage;
    }
    return messagesConfig.getStringList(message);
  }

  public List<String> consoleStringList(final String message, final List<String> fallbackMessage) {
    if(messagesConfig.getStringList(message).isEmpty()) {
      getErrorMessage(message);
      return fallbackMessage;
    }
    return messagesConfig.getStringList(message);
  }

  public String console(final String message, final String fallbackMessage) {
    if(messagesConfig.getString(message) == null) {
      getErrorMessage(message);
      return fallbackMessage;
    }
    return messagesConfig.getString(message);
  }

  public String getPrefix() {
    if(messagesConfig.getString("Plugin_Prefix") == null) {
      getErrorMessage("Prefix");
      return "#8c8c8c[#2b9bbf&lOV#8c8c8c]" + " ";
    }
    return messagesConfig.getString("Plugin_Prefix") + " ";
  }

  private void getErrorMessage(final String message) {
    Utilities.logInfo(true,
      "There was an error getting the " + message + " message from the " + configName + ".",
      "I have set a fallback message to take it's place until the issue is fixed.",
      "To resolve this, please locate " + message + " in the " + configName + " and fix the issue."
    );
  }
}
