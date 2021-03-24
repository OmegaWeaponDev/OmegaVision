package me.omegaweapondev.omegavision.utils;

import me.omegaweapondev.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;

public class NightVisionToggle {
  private final OmegaVision plugin;

  private final SettingsHandler settingsHandler;
  private final UserDataHandler userData;
  private final MessageHandler messageHandler;
  private final Player player;
  private final NightVisionConditions nightVisionConditions;

  private final FileConfiguration playerData;
  private final FileConfiguration configFile;

  private final Map<UUID, Boolean> playerMap;
  private final Map<UUID, Long> nightvisionAppliedTime;
  private final Map<UUID, Long> nightvisionLimitReached;

  public NightVisionToggle(final OmegaVision plugin, final Player player) {
    this.plugin = plugin;
    this.player = player;

    settingsHandler = plugin.getSettingsHandler();
    userData = plugin.getUserData();
    nightVisionConditions = new NightVisionConditions(plugin, player);

    messageHandler = new MessageHandler(plugin, settingsHandler.getMessagesFile().getConfig());
    configFile = settingsHandler.getConfigFile().getConfig();
    playerData = userData.getPlayerData();

    playerMap = userData.getPlayerMap();
    nightvisionAppliedTime = userData.getNightvisionAppliedTime();
    nightvisionLimitReached = userData.getNightvisionLimitReached();
  }

  public void nightVisionEnable() {
    // Check if the user has night vision
    if(userData.hasNightNightVision(player.getUniqueId())) {
      nightVisionDisable();
      return;
    }

    // Check the status of the nightvision usage limit
    if(!checkLimitStatus()) {
      return;
    }

    // Apply nightvision to use the user
    Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1, configFile.getBoolean("Particle_Ambient"), configFile.getBoolean("Particle_Effects"), configFile.getBoolean("NightVision_Icon"));
    increaseLimit();

    // Add the user to the maps
    playerMap.put(player.getUniqueId(), true);
    nightvisionAppliedTime.put(player.getUniqueId(), System.currentTimeMillis());

    // Save user to file, if have login permission
    nightVisionLogin(player);

    if(configFile.getBoolean("Sound_Effects.Night_Vision_Enabled.Enabled")) {
      player.playSound(player.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Night_Vision_Enable.Sound")), 1, 1);
    }

    playerData.set(player.getUniqueId().toString() + ".NightVision.Enabled", true);

    if(configFile.getBoolean("ActionBar_Message")) {
      Utilities.sendActionBar(player, messageHandler.string("ActionBar_NightVision_Applied", "&9Nightvision has been applied!"));
    }

    Utilities.message(player, messageHandler.string("NightVision_Applied", "#53B1D5Nightvision has successfully been applied!"));
  }

  public void nightVisionDisable() {
    if(!userData.hasNightNightVision(player.getUniqueId())) {
      return;
    }

    Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);
    if(configFile.getBoolean("Blindness_Effect.Enabled") && player.isOnline()) {
      nightVisionConditions.nightvisionBlindness();
    }

    playerMap.remove(player.getUniqueId());

    if(configFile.getBoolean("Sound_Effects.Night_Vision_Disable.Enabled")) {
      player.playSound(player.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Night_Vision_Disable.Sound")), 1, 1);
    }

    nightVisionLogin(player);
  }

  public void nightVisionDisableOthers(final Player target) {
    if(!userData.hasNightNightVision(target.getUniqueId())) {
      return;
    }

    Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);
    playerMap.remove(target.getUniqueId());

    if(configFile.getBoolean("Sound_Effects.Night_Vision_Disable.Enabled")) {
      target.playSound(target.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Night_Vision_Disable.Sound")), 1, 1);
    }

    nightVisionLogin(target);
  }

  public void nightVisionEnableOthers(final Player target) {
    if(userData.hasNightNightVision(target.getUniqueId())) {
      nightVisionDisableOthers(target);
      return;
    }

    Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1, configFile.getBoolean("Particle_Ambient"), configFile.getBoolean("Particle_Effects"), configFile.getBoolean("NightVision_Icon"));


    playerMap.put(target.getUniqueId(), true);
    nightvisionAppliedTime.put(target.getUniqueId(), System.currentTimeMillis());

    // Save user to file, if have login permission
    nightVisionLogin(target);

    if(configFile.getBoolean("Sound_Effects.Night_Vision_Enabled.Enabled")) {
      target.playSound(target.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Night_Vision_Enable.Sound")), 1, 1);
    }

    if(configFile.getBoolean("ActionBar_Message")) {
      Utilities.sendActionBar(target, messageHandler.string("ActionBar_NightVision_Applied", "&9Nightvision has been applied!"));
    }

    Utilities.message(target, messageHandler.string("NightVision_Applied", "#53B1D5Nightvision has successfully been applied!"));
  }

  private void nightVisionLogin(final Player target) {
    if(!Utilities.checkPermissions(target, true, "omegavision.login", "omegavision.admin")) {
      return;
    }

    if(!userData.getPlayerData().getConfigurationSection("Users").getKeys(false).contains(target.getUniqueId().toString())) {
      userData.getPlayerData().createSection("Users." + target.getUniqueId().toString());
    }

    userData.getPlayerData().set("Users." + target.getUniqueId().toString() + ".Enabled", true);
    userData.getPlayerData().set("Users." + target.getUniqueId().toString() + ".NightVision_Last_Used", System.currentTimeMillis());
    userData.saveUserFile();
  }

  private void increaseLimit() {
    if(Utilities.checkPermissions(player, true, "omegavision.limit.bypass", "omegavision.limit.admin", "omegavision.admin")) {
      return;
    }

    userData.getPlayerData().set("Users." + player.getUniqueId().toString() + ".Limit", userData.getCurrentUsageAmount(player.getUniqueId()) + 1);
    Utilities.message(player,
      messageHandler.string("Limit_Amount_Increased", "&bYour limit amount now stands at: &c%currentLimitAmount% / %maxLimitAmount%")
      .replace("%currentLimitAmount%", String.valueOf(userData.getCurrentUsageAmount(player.getUniqueId())))
      .replace("maxLimitAmount", String.valueOf(configFile.getInt("Night_Vision_Limit.Limit")))
    );
  }

  private boolean checkLimitStatus() {
    if(!configFile.getBoolean("Night_Vision_Limit.Enabled")) {
      return true;
    }

    if(!nightvisionLimitReached.containsKey(player.getUniqueId())) {
      return true;
    }

    Utilities.message(player, messageHandler.string("Night_Vision_Limit.Limit_Reached", "#FF4A4ASorry, you have reached the limit for the nightvision command!"));
    return false;
  }
}
