package me.omegaweapon.omegavision.utils;

import me.omegaweapon.omegavision.OmegaVision;

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
      return OmegaVision.getMessagesFile().getConfig().getString("NightVision_Applied");
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
}
