package me.omegaweapondev.omegavision.utils;

import me.omegaweapondev.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class NightVisionToggle {
  private final OmegaVision pluginInstance;
  private final FileConfiguration configFile;
  private final MessagesHandler messagesHandler;
  private final UserDataHandler userDataHandler;

  private final boolean particleEffects;
  private final boolean particleAmbients;
  private final boolean nightVisionIcon;
  private final boolean hasNightVision;
  private final boolean actionBarMessages;

  private final String nightVisionApplied;
  private final String nightVisionRemoved;
  private final String nightVisionAppliedActionbar;
  private final String nightVisionRemovedActionbar;

  private final Player player;

  public NightVisionToggle(final OmegaVision pluginInstance, final Player player) {
    this.pluginInstance = pluginInstance;
    this.player = player;
    configFile = pluginInstance.getStorageManager().getConfigFile().getConfig();
    messagesHandler = pluginInstance.getMessagesHandler();
    userDataHandler = pluginInstance.getUserDataHandler();

    particleEffects = configFile.getBoolean("Night_Vision_Settings.Particle_Effects");
    particleAmbients = configFile.getBoolean("Night_Vision_Settings.Particle_Ambient");
    nightVisionIcon = configFile.getBoolean("Night_Vision_Settings.NightVision_Icon");
    actionBarMessages = configFile.getBoolean("Night_Vision_Settings.ActionBar_Messages");

    nightVisionApplied = messagesHandler.string("Night_Vision_Messages.Night_Vision_Applied", "#2b9bbfNight Vision has been applied!");
    nightVisionRemoved = messagesHandler.string("Night_Vision_Messages.Night_Vision_Removed", "#f63e3eNight Vision has been removed!");
    nightVisionAppliedActionbar = messagesHandler.string("Night_Vision_Messages.ActionBar_Night_Vision_Applied", "#2b9bbfNight vision has been applied!");
    nightVisionRemovedActionbar = messagesHandler.string("Night_Vision_Messages.ActionBar_Night_Vision_Removed", "#f63e3eNight Vision has been removed!");

    hasNightVision = userDataHandler.getEffectStatus(player.getUniqueId(), UserDataHandler.NIGHT_VISION);
  }

  public void nightVisionToggle() {
    // Check if the player has permission
    if(!toggleSelfPerm(player)) {
      return;
    }

    // Check if the player currently has night vision enabled
    if(hasNightVision) {
      // Remove night vision from the player
      userDataHandler.setEffectStatus(player.getUniqueId(), false, UserDataHandler.NIGHT_VISION);
      Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);

      // Send night vision removal messages
      sendNightVisionRemovedMessages(player);
      return;
    }

    // Check if they have particle bypass perm and apply correct night vision effect
    applyNightVision(player, 60 * 60 * 24 * 100);

    // Send night vision applied messages
    sendNightVisionAppliedMessages(player);
  }

  public void nightVisionToggleOthers(final Player target) {
    // Check if the player has permission
    if(!toggleOthersPerm(player)) {
      return;
    }

    // Check if the target currently has night vision enabled
    if(userDataHandler.getEffectStatus(target.getUniqueId(), UserDataHandler.NIGHT_VISION)) {
      // Remove night vision from the target
      userDataHandler.setEffectStatus(target.getUniqueId(), false, UserDataHandler.NIGHT_VISION);
      Utilities.removePotionEffect(target, PotionEffectType.NIGHT_VISION);

      // Send night vision removal messages
      sendNightVisionRemovedMessages(target);
      return;
    }

    // Check if the target has particle bypass perm and apply correct night vision effect
    applyNightVision(player, 60 * 60 * 24 * 100);

    // Send night vision applied messages
    sendNightVisionAppliedMessages(player);
  }

  public void nightVisionToggleTemp(final Player target, final int seconds) {
    // Check if the player has permission
    if(!toggleTempPerm(player)) {
      return;
    }

    // Check if the target currently has night vision enabled
    if(userDataHandler.getEffectStatus(target.getUniqueId(), UserDataHandler.NIGHT_VISION)) {
      // Remove night vision from the target
      Utilities.removePotionEffect(target, PotionEffectType.NIGHT_VISION);
    }

    // Check if the target has particle bypass perm and apply correct night vision effect
    applyNightVision(target, seconds);

    // Send night vision applied messages
    sendNightVisionAppliedMessages(target);
  }

  public void nightVisionToggleGlobal(final String action) {
    // Check if the player has permission
    if(!toggleGlobalPerm(player)) {
      return;
    }

    for(Player player : Bukkit.getOnlinePlayers()) {
      if(action.equalsIgnoreCase("remove")) {
        // Remove night vision from the target
        userDataHandler.setEffectStatus(player.getUniqueId(), false, UserDataHandler.NIGHT_VISION);
        Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);

        Utilities.broadcast(false, messagesHandler.string("Night_Vision_Removed_Global", "#2b9bbfNight Vision has been removed for all players!"));
        return;
      }

      if(action.equalsIgnoreCase("add")) {
        // Check if the target has particle bypass perm and apply correct night vision effect
        applyNightVision(player, 60 * 60 * 24 * 100);

        // Send night vision applied messages
        Utilities.broadcast(false, messagesHandler.string("Night_Vision_Applied_Global", "#2b9bbfNight Vision has been applied for all players!"));
      }
    }
  }

  private void applyNightVision(final Player player, final int duration) {
    userDataHandler.setEffectStatus(player.getUniqueId(), true, UserDataHandler.NIGHT_VISION);
    if(Utilities.checkPermissions(player, false, "omegavision.nightvision.particles.bypass")) {
      Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, duration ,1, false, false, false);
    } else {
      Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, duration ,1, particleEffects, particleAmbients, nightVisionIcon);
    }
  }

  private void sendNightVisionAppliedMessages(final Player player) {
    Utilities.message(player, nightVisionApplied);

    if(configFile.getBoolean("Night_Vision_Settings.ActionBar_Messages")) {
      Utilities.sendActionBar(player, nightVisionAppliedActionbar);
    }
  }

  private void sendNightVisionRemovedMessages(final Player player) {
    Utilities.message(player, nightVisionRemoved);
    if(actionBarMessages)
    {
      Utilities.sendActionBar(player, nightVisionRemovedActionbar);
    }
  }

  private boolean toggleSelfPerm(final Player player) {
    if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.toggle", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
      return false;
    }
    return true;
  }

  private boolean toggleGlobalPerm(final Player player) {
    if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.global", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
      return false;
    }
    return true;
  }

  private boolean toggleTempPerm(final Player player) {
    if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.temp", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
      return false;
    }
    return true;
  }

  private boolean toggleOthersPerm(final Player player) {
    if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.toggle.others", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
      return false;
    }
    return true;
  }
}
