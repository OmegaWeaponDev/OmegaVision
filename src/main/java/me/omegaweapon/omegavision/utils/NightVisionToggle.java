package me.omegaweapon.omegavision.utils;

import me.omegaweapon.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NightVisionToggle {
  public static Map<UUID, String> playerMap = new HashMap<>();
  public static Map<UUID, Long> nightvisionAppliedTime = new HashMap<>();
  public static Map<UUID, Long> nightvisionLimitReached = new HashMap<>();

  public static void nightVisionEnable(Player player) {

    if(nightvisionLimitReached.containsKey(player.getUniqueId()) && OmegaVision.getInstance().getPlayerData().getConfig().getInt(player.getUniqueId().toString() + ".Limit") == OmegaVision.getInstance().getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit")) {
      Utilities.message(player, MessageHandler.playerMessage("Night_Vision_Limit.Limit_Reached", "&cSorry, you have reached the limit for the nightvision command!"));
      return;
    }

    // Add the nightvision effect to the player
    Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1,
      OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("Particle_Ambient"),
      OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("Particle_Effects"),
      OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("NightVision_Icon")
    );

    // Add the player to the maps
    playerMap.put(player.getUniqueId(), player.getName());
    nightvisionAppliedTime.put(player.getUniqueId(), System.currentTimeMillis());

    // Send the player the nightvision applied message
    Utilities.message(player, MessageHandler.playerMessage("NightVision_Applied", "&9Night Vision has been applied!"));

    // Add the players nightvision status to playerData.yml if they have the login permission
    if(Utilities.checkPermissions(player, true,"omegavision.login", "omegavision.*")) {

      // Check if they have been added to the file, if not, add them
      if (!OmegaVision.getInstance().getPlayerData().getConfig().isConfigurationSection(player.getUniqueId().toString())) {
        OmegaVision.getInstance().getPlayerData().getConfig().createSection(player.getUniqueId().toString());
      }

      OmegaVision.getInstance().getPlayerData().getConfig().set(player.getUniqueId().toString() + ".NightVision.Enabled", true);
      OmegaVision.getInstance().getPlayerData().getConfig().set(player.getUniqueId().toString() + ".NightVision.Last Used", System.currentTimeMillis());
      OmegaVision.getInstance().getPlayerData().saveConfig();
    }

    // If enabled, send the player an action bar when toggling nightvision
    if (OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("ActionBar_Messages")) {
      Utilities.sendActionBar(player, MessageHandler.playerMessage("ActionBar_NightVision_Applied", "&9Nightvision has been applied!"));
    }

    if(OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("Night_Vision_Limit.Enabled")) {
      if(!Utilities.checkPermissions(player, true, "omegavision.limit.bypass", "omegavision.limit.*", "omegavision.*")) {
        limitIncrease(player);
      }
    }

    if(OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("Sound_Effects.Night_Vision_Enable.Enabled")) {
      player.playSound(player.getLocation(), Sound.valueOf(OmegaVision.getInstance().getConfigFile().getConfig().getString("Sound_Effects.Night_Vision_Enable.Sound")), 1, 1);
    }
  }

  public static void nightvisionDisable(Player player) {
    // Remove the nightvision effect from the player
    Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);
    Utilities.message(player, MessageHandler.playerMessage("NightVision_Removed", "&cNight Vision has been removed!"));

    // Trigger the blindness method
    if(OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("Blindness_Effect.Enabled") && player.isOnline()) {
      NightVisionConditions.nightvisionBlindness(player);
    }

    // If enabled, send the player an action bar when toggling nightvision
    if(OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("ActionBar_Messages")) {
      Utilities.sendActionBar(player, MessageHandler.playerMessage("ActionBar_NightVision_Removed", "&cNightvision has been removed!"));
    }

    // Remove the player from the playerMap
    playerMap.remove(player.getUniqueId());

    // Add the players nightvision status to playerData.yml if they have the login permission
    if (Utilities.checkPermissions(player, true, "omegavision.login", "omegavision.*")) {
      return;
    }

    // Check if they have been added to the file, if not, add them
    if (!OmegaVision.getInstance().getPlayerData().getConfig().contains(player.getUniqueId().toString())) {
      OmegaVision.getInstance().getPlayerData().getConfig().createSection(player.getUniqueId().toString());
    }

    if(OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("Sound_Effects.Night_Vision_Disable.Enabled")) {
      player.playSound(player.getLocation(), Sound.valueOf(OmegaVision.getInstance().getConfigFile().getConfig().getString("Sound_Effects.Night_Vision_Disable.Sound")), 1, 1);
    }

    OmegaVision.getInstance().getPlayerData().getConfig().set(player.getUniqueId().toString() + ".NightVision.Enabled", false);
    OmegaVision.getInstance().getPlayerData().saveConfig();
  }

  public static void nightvisionEnableOthers(final Player target) {
    nightVisionEnable(target);
    Utilities.message(target, MessageHandler.playerMessage("NightVision_Applied", "&9Night Vision has been applied!"));

    if(OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("Sound_Effects.Night_Vision_Enable.Enabled")) {
      target.playSound(target.getLocation(), Sound.valueOf(OmegaVision.getInstance().getConfigFile().getConfig().getString("Sound_Effects.Night_Vision_Enable.Sound")), 1, 1);
    }
  }

  public static void nightvisionDisableOthers(final Player target) {
    nightvisionDisable(target);
    Utilities.message(target, MessageHandler.playerMessage("NightVision_Removed", "&cNight Vision has been removed!"));

    if(OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("Sound_Effects.Night_Vision_Disable.Enable")) {
      target.playSound(target.getLocation(), Sound.valueOf(OmegaVision.getInstance().getConfigFile().getConfig().getString("Sound_Effects.Night_Vision_Disable.Sound")), 1, 1);
    }
  }

  private static int limitCheck(final Player player) {
    int playerLimitAmount = OmegaVision.getInstance().getPlayerData().getConfig().getInt(player.getUniqueId().toString() + ".Limit");

    if(OmegaVision.getInstance().getPlayerData().getConfig().getInt(player.getUniqueId().toString() + ".Limit") > 0) {
      return playerLimitAmount ;
    }

    return 0;
  }

  private static void limitIncrease(final Player player) {
    int playerLimitAmount = limitCheck(player);

    if((playerLimitAmount + 1) > OmegaVision.getInstance().getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit")) {
      Utilities.message(player, MessageHandler.playerMessage("Night_Vision_Limit.Limit_Reached", "&cSorry, you have reached the limit for the nightvision command!"));
      nightvisionLimitReached.put(player.getUniqueId(), System.currentTimeMillis());

      if(OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("Sound_Effects.Limit_Reached.Enabled")) {
        player.playSound(player.getLocation(), Sound.valueOf(OmegaVision.getInstance().getConfigFile().getConfig().getString("Sound_Effects.Limit_Reached.Sound")), 1, 1);
      }
      return;
    }

    if(playerLimitAmount < OmegaVision.getInstance().getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit")) {
      OmegaVision.getInstance().getPlayerData().getConfig().set(player.getUniqueId().toString() + ".Limit", playerLimitAmount + 1);
      OmegaVision.getInstance().getPlayerData().saveConfig();

      Utilities.message(player, MessageHandler.playerMessage("Night_Vision_Limit.Limit_Amount_Increased", "&bYour limit amount now stands at: &c%currentLimitAmount% / %maxLimitAmount%"));
    }
  }
}
