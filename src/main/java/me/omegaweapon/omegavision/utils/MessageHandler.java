package me.omegaweapon.omegavision.utils;

import me.omegaweapon.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.entity.Player;

public class MessageHandler {

  public static String pluginPrefix() {
    if(OmegaVision.getInstance().getMessagesFile().getConfig().getString("Prefix") == null) {
      Utilities.logWarning(true,
        "There was an error getting the prefix message from the messages.yml.",
        "I have set a fallback message to take it's place until the issue is fixed.",
        "To resolve this, please locate prefix in the messages.yml and fix the issue."
      );

      return "&7&l[&aOmegaVision&7&l]";
    }

    return OmegaVision.getInstance().getMessagesFile().getConfig().getString("Prefix");
  }

  public static String playerMessage(final String message, final String fallbackMessage) {
    if(OmegaVision.getInstance().getMessagesFile().getConfig().getString(message) == null) {
      Utilities.logWarning(true,
        "There was an error getting the " + message + " message from the messages.yml.",
        "I have set a fallback message to take it's place until the issue is fixed.",
        "To resolve this, please locate " + message + " in the messages.yml and fix the issue."
      );
      return pluginPrefix() + " " + fallbackMessage;
    }

    return pluginPrefix() + " " + OmegaVision.getInstance().getMessagesFile().getConfig().getString(message);
  }

  public static String consoleMessage(final String message, final String fallbackMessage) {
    if(OmegaVision.getInstance().getMessagesFile().getConfig().getString(message) == null) {
      Utilities.logWarning(true,
        "There was an error getting the " + message + " message from the messages.yml.",
        "I have set a fallback message to take it's place until the issue is fixed.",
        "To resolve this, please locate " + message + " in the messages.yml and fix the issue."
      );
      return fallbackMessage;
    }

    return OmegaVision.getInstance().getMessagesFile().getConfig().getString(message);
  }

  public static String limitCheck(final Player target) {
    final int configLimitAmount = OmegaVision.getInstance().getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit");
    int playerLimitAmount = OmegaVision.getInstance().getPlayerData().getConfig().getInt(target.getUniqueId().toString() + ".Limit");

    if(OmegaVision.getInstance().getMessagesFile().getConfig().getString("Night_Vision_Limit.Limit_Check") == null) {
      Utilities.logWarning(true,
        "There was an error getting the Limit_Check message from the messages.yml.",
        "I have set a fallback message to take it's place until the issue is fixed.",
        "To resolve this, please locate Limit_Check in the messages.yml and fix the issue."
      );
      return "&bYour limit amount currently stands at: &c" + playerLimitAmount + " / &c" + configLimitAmount;
    }

    String limitCheckMessage = OmegaVision.getInstance().getMessagesFile().getConfig().getString("Night_Vision_Limit.Limit_Check");
    String formattedLimitCheckMessage = limitCheckMessage.replace("%maxLimitAmount%", String.valueOf(configLimitAmount));

    return formattedLimitCheckMessage.replace("%currentLimitAmount%", String.valueOf(playerLimitAmount));
  }

  public static String limitCheckOther(final Player target) {
    final int configLimitAmount = OmegaVision.getInstance().getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit");
    int playerLimitAmount = OmegaVision.getInstance().getPlayerData().getConfig().getInt(target.getUniqueId().toString() + ".Limit");

    return "&c" + target.getName() +  "'s &blimit amount currently stands at: &c" + playerLimitAmount + " / " + configLimitAmount;
  }


  public static String limitResetOthers(final Player target) {
    if(target == null) {
      return "&cSorry, that player does not exist";
    }

    return "&c" + target.getName() + "'s &blimit have been reset! They can use the nightvision command again!";
  }

  public static String limitIncreased(final Player player) {
    final int configLimitAmount = OmegaVision.getInstance().getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit");
    int playerLimitAmount = OmegaVision.getInstance().getPlayerData().getConfig().getInt(player.getUniqueId().toString() + ".Limit");

    if(OmegaVision.getInstance().getMessagesFile().getConfig().getString("Night_Vision_Limit.Limit_Amount_Increased") == null) {
      Utilities.logWarning(true,
        "There was an error getting the Limit_Check message from the messages.yml.",
        "I have set a fallback message to take it's place until the issue is fixed.",
        "To resolve this, please locate Limit_Check in the messages.yml and fix the issue."
      );
      return "&bYour limit amount now stands at: &c" + playerLimitAmount + " / " + configLimitAmount;
    }

    String limitCheckMessage = OmegaVision.getInstance().getMessagesFile().getConfig().getString("Night_Vision_Limit.Limit_Amount_Increased");
    String formattedLimitCheckMessage = limitCheckMessage.replace("%maxLimitAmount%", String.valueOf(configLimitAmount));

    return formattedLimitCheckMessage.replace("%currentLimitAmount%", String.valueOf(playerLimitAmount));
  }
}
