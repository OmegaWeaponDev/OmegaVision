package me.omegaweapon.omegavision.utils;

import me.omegaweapon.omegavision.OmegaVision;
import org.bukkit.entity.Player;

public class MessageHandler {

  public static String prefix() {
    if(OmegaVision.getMessagesFile().getConfig().getString("Prefix") == null) {
      return "&7[&9&lOV&7]";
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("Prefix");
    }
  }

  public static String nightvisionApplied() {
    if(OmegaVision.getMessagesFile().getConfig().getString("NightVision_Applied") == null) {
      return "&9Night Vision has been applied!";
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("NightVision_Applied");
    }
  }

  public static String nightvisionRemoved() {
    if(OmegaVision.getMessagesFile().getConfig().getString("NightVision_Removed") == null) {
      return "&cNight Vision has been removed!";
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("NightVision_Removed");
    }
  }

  public static String nightvisionActionBarApplied() {
    if(OmegaVision.getMessagesFile().getConfig().getString("ActionBar_NightVision_Applied") == null) {
      return "&9Nightvision has been applied!";
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("ActionBar_NightVision_Applied");
    }
  }

  public static String nightvisionActionBarRemoved() {
    if(OmegaVision.getMessagesFile().getConfig().getString("ActionBar_NightVision_Removed") == null) {
      return "&cNightvision has been removed!";
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("ActionBar_NightVision_Removed");
    }
  }

  public static String bucketMessage() {
    if(OmegaVision.getMessagesFile().getConfig().getString("Bucket_Message") == null) {
      return "&9Particle Effects and the icon have been removed!";
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("Bucket_Message");
    }
  }

  public static String blindnessMessage() {
    if(OmegaVision.getMessagesFile().getConfig().getString("Blindness_Message") == null) {
      return "&cYou have been using nightvision for too long, you are now blind";
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("Blindness_Message");
    }
  }

  public static String reloadMessage() {
    if(OmegaVision.getMessagesFile().getConfig().getString("Reload_Message") == null) {
      return "&cOmegaVision has successfully been reloaded";
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("Reload_Message");
    }
  }

  public static String noPermission() {
    if(OmegaVision.getMessagesFile().getConfig().getString("No_Permission") == null) {
      return "&cSorry, but you do not have the required permission to do that.";
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("No_Permission");
    }
  }

  public static String limitReached() {
    if(OmegaVision.getMessagesFile().getConfig().getString("Night_Vision_Limit.Limit_Reached") == null) {
      return "&cSorry, you have reached the limit for the nightvision command!";
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("Night_Vision_Limit.Limit_Reached");
    }
  }

  public static String limitCheck(final Player target) {
    final int configLimitAmount = OmegaVision.getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit");
    int playerLimitAmount = OmegaVision.getPlayerData().getConfig().getInt(target.getUniqueId().toString() + ".Limit");

    if(OmegaVision.getMessagesFile().getConfig().getString("Night_Vision_Limit.Limit_Check") == null) {
      return "&bYour limit amount currently stands at: &c" + playerLimitAmount + " / &c" + configLimitAmount;
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("Night_Vision_Limit.Limit_Check")
        .replace("%currentLimitAmount%", String.valueOf(playerLimitAmount))
        .replace("%maxLimitAmount%", String.valueOf(configLimitAmount));
    }
  }

  public static String limitCheckOther(final Player target) {
    final int configLimitAmount = OmegaVision.getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit");
    int playerLimitAmount = OmegaVision.getPlayerData().getConfig().getInt(target.getUniqueId().toString() + ".Limit");

    return "&c" + target.getName() +  "'s &blimit amount currently stands at: &c" + playerLimitAmount + " / " + configLimitAmount;
  }

  public static String limitReset() {
    if(OmegaVision.getMessagesFile().getConfig().getString("Night_Vision_Limit.Limit_Reset") == null) {
      return "&bYour limit have been reset! You can use the nightvision command again!";
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("Night_Vision_Limit.Limit_Reset");
    }
  }

  public static String limitResetOthers(final Player target) {
    return "&c" + target.getName() + "'s &blimit have been reset! They can use the nightvision command again!";
  }

  public static String limitIncreased(Player player) {
    final int configLimitAmount = OmegaVision.getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit");
    int playerLimitAmount = OmegaVision.getPlayerData().getConfig().getInt(player.getUniqueId().toString() + ".Limit");

    if(OmegaVision.getMessagesFile().getConfig().getString("Night_Vision_Limit.Limit_Amount_Increased") == null) {
      return "&bYour limit amount now stands at: &c" + playerLimitAmount + " / " + configLimitAmount;
    } else {
      return OmegaVision.getMessagesFile().getConfig().getString("Night_Vision_Limit.Limit_Amount_Increased");
    }
  }
}
