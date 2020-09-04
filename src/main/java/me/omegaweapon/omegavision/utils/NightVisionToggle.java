package me.omegaweapon.omegavision.utils;

import me.omegaweapon.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NightVisionToggle {

  private final MessageHandler messageHandler = new MessageHandler(OmegaVision.getInstance().getMessagesFile().getConfig());
  private final FileConfiguration configFile = OmegaVision.getInstance().getConfigFile().getConfig();
  private final FileConfiguration playerData = OmegaVision.getInstance().getPlayerData().getConfig();
  private final Player player;
  public Map<UUID, String> playerMap = new HashMap<>();
  public Map<UUID, Long> nightvisionAppliedTime = new HashMap<>();
  public Map<UUID, Long> nightvisionLimitReached = new HashMap<>();

  public NightVisionToggle(final Player player) {
    this.player = player;
  }

  public void nightVisionEnable() {

    if(nightvisionLimitReached.containsKey(player.getUniqueId()) && playerData.getInt(player.getUniqueId().toString() + ".Limit") == configFile.getInt("Night_Vision_Limit.Limit")) {
      Utilities.message(player, messageHandler.string("Night_Vision_Limit.Limit_Reached", "&cSorry, you have reached the limit for the nightvision command!"));
      return;
    }

    // Add the nightvision effect to the player
    Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1,
      configFile.getBoolean("Particle_Ambient"),
      configFile.getBoolean("Particle_Effects"),
      configFile.getBoolean("NightVision_Icon")
    );

    // Add the player to the maps
    playerMap.put(player.getUniqueId(), player.getName());
    nightvisionAppliedTime.put(player.getUniqueId(), System.currentTimeMillis());

    // Send the player the nightvision applied message
    Utilities.message(player, messageHandler.string("NightVision_Applied", "&9Night Vision has been applied!"));

    // Add the players nightvision status to playerData.yml if they have the login permission
    if(Utilities.checkPermissions(player, true,"omegavision.login", "omegavision.*")) {

      // Check if they have been added to the file, if not, add them
      if (!playerData.isConfigurationSection(player.getUniqueId().toString())) {
        playerData.createSection(player.getUniqueId().toString());
      }

      playerData.set(player.getUniqueId().toString() + ".NightVision.Enabled", true);
      playerData.set(player.getUniqueId().toString() + ".NightVision.Last Used", System.currentTimeMillis());
      OmegaVision.getInstance().getPlayerData().saveConfig();
    }

    // If enabled, send the player an action bar when toggling nightvision
    if (configFile.getBoolean("ActionBar_Messages")) {
      Utilities.sendActionBar(player, messageHandler.string("ActionBar_NightVision_Applied", "&9Nightvision has been applied!"));
    }

    if(configFile.getBoolean("Night_Vision_Limit.Enabled")) {
      if(!Utilities.checkPermissions(player, true, "omegavision.limit.bypass", "omegavision.limit.*", "omegavision.*")) {
        limitIncrease();
      }
    }

    if(configFile.getBoolean("Sound_Effects.Night_Vision_Enable.Enabled")) {
      player.playSound(player.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Night_Vision_Enable.Sound")), 1, 1);
    }
  }

  public void nightvisionDisable() {
    // Remove the nightvision effect from the player
    Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);
    Utilities.message(player, messageHandler.string("NightVision_Removed", "&cNight Vision has been removed!"));

    // Trigger the blindness method
    if(configFile.getBoolean("Blindness_Effect.Enabled") && player.isOnline()) {
      final NightVisionConditions nvConditions = new NightVisionConditions(player);

      nvConditions.nightvisionBlindness();
    }

    // If enabled, send the player an action bar when toggling nightvision
    if(configFile.getBoolean("ActionBar_Messages")) {
      Utilities.sendActionBar(player, messageHandler.string("ActionBar_NightVision_Removed", "&cNightvision has been removed!"));
    }

    // Remove the player from the playerMap
    playerMap.remove(player.getUniqueId());

    // Add the players nightvision status to playerData.yml if they have the login permission
    if(!Utilities.checkPermissions(player, true, "omegavision.login", "omegavision.admin")) {
      return;
    }

    // Check if they have been added to the file, if not, add them
    if (!playerData.contains(player.getUniqueId().toString())) {
      playerData.createSection(player.getUniqueId().toString());
    }

    if(configFile.getBoolean("Sound_Effects.Night_Vision_Disable.Enabled")) {
      player.playSound(player.getLocation(), Sound.valueOf(OmegaVision.getInstance().getConfigFile().getConfig().getString("Sound_Effects.Night_Vision_Disable.Sound")), 1, 1);
    }

    playerData.set(player.getUniqueId().toString() + ".NightVision.Enabled", false);
    OmegaVision.getInstance().getPlayerData().saveConfig();
  }

  public void nightvisionEnableOthers() {
    nightVisionEnable();
    Utilities.message(player, messageHandler.string("NightVision_Applied", "&9Night Vision has been applied!"));

    if(configFile.getBoolean("Sound_Effects.Night_Vision_Enable.Enabled")) {
      player.playSound(player.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Night_Vision_Enable.Sound")), 1, 1);
    }
  }

  public void nightvisionDisableOthers(final Player target) {
    nightvisionDisable();
    Utilities.message(target, messageHandler.string("NightVision_Removed", "&cNight Vision has been removed!"));

    if(configFile.getBoolean("Sound_Effects.Night_Vision_Disable.Enable")) {
      target.playSound(target.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Night_Vision_Disable.Sound")), 1, 1);
    }
  }

  private int limitCheck(final Player player) {
    int playerLimitAmount = playerData.getInt(player.getUniqueId().toString() + ".Limit");

    if(playerData.getInt(player.getUniqueId().toString() + ".Limit") > 0) {
      return playerLimitAmount ;
    }

    return 0;
  }

  private void limitIncrease() {
    int playerLimitAmount = limitCheck(player);

    if((playerLimitAmount + 1) > configFile.getInt("Night_Vision_Limit.Limit")) {
      Utilities.message(player, messageHandler.string("Night_Vision_Limit.Limit_Reached", "&cSorry, you have reached the limit for the nightvision command!"));
      nightvisionLimitReached.put(player.getUniqueId(), System.currentTimeMillis());

      if(configFile.getBoolean("Sound_Effects.Limit_Reached.Enabled")) {
        player.playSound(player.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Limit_Reached.Sound")), 1, 1);
      }
      return;
    }

    if(playerLimitAmount < configFile.getInt("Night_Vision_Limit.Limit")) {
      playerData.set(player.getUniqueId().toString() + ".Limit", playerLimitAmount + 1);
      OmegaVision.getInstance().getPlayerData().saveConfig();

      Utilities.message(player, messageHandler.string("Night_Vision_Limit.Limit_Amount_Increased", "&bYour limit amount now stands at: &c%currentLimitAmount% / %maxLimitAmount%"));
    }
  }
}
