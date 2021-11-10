package me.omegaweapondev.omegavision.utils;

import me.omegaweapondev.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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
    nightVisionIcon = configFile.getBoolean("Night_Vision_Settings.Night_Vision_Icon");
    actionBarMessages = configFile.getBoolean("Night_Vision_Settings.ActionBar_Messages");

    nightVisionApplied = messagesHandler.string("Night_Vision_Messages.Night_Vision_Applied", "#2b9bbfNight Vision has been applied!");
    nightVisionRemoved = messagesHandler.string("Night_Vision_Messages.Night_Vision_Removed", "#f63e3eNight Vision has been removed!");
    nightVisionAppliedActionbar = messagesHandler.string("Night_Vision_Messages.ActionBar_Night_Vision_Applied", "#2b9bbfNight vision has been applied!");
    nightVisionRemovedActionbar = messagesHandler.string("Night_Vision_Messages.ActionBar_Night_Vision_Removed", "#f63e3eNight Vision has been removed!");

    hasNightVision = userDataHandler.getEffectStatus(player.getUniqueId());
  }

  public void nightVisionToggle() {
    // Check if the player has permission
    if(!toggleSelfPerm(player)) {
      return;
    }

    // Check if the player currently has night vision enabled
    if(hasNightVision) {
      // Remove night vision from the player
      userDataHandler.setEffectStatus(player.getUniqueId(), false);
      Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);

      // Send night vision removal messages
      sendNightVisionRemovedMessages(player);
      toggleSoundEffect(player, "Night_Vision_Disabled");
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
    if(userDataHandler.getEffectStatus(target.getUniqueId())) {
      // Remove night vision from the target
      userDataHandler.setEffectStatus(target.getUniqueId(), false);
      Utilities.removePotionEffect(target, PotionEffectType.NIGHT_VISION);

      // Send night vision removal messages
      sendNightVisionRemovedMessages(target);
      toggleSoundEffect(player, "Night_Vision_Disabled");
      return;
    }

    // Check if the target has particle bypass perm and apply correct night vision effect
    applyNightVision(target, 60 * 60 * 24 * 100);

    // Send night vision applied messages
    sendNightVisionAppliedMessages(target);
  }

  public void nightVisionToggleTemp(final Player target, final int seconds) {
    // Check if the player has permission
    if(!toggleTempPerm(player)) {
      return;
    }

    // Check if the target currently has night vision enabled
    if(userDataHandler.getEffectStatus(target.getUniqueId())) {
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

    if(Bukkit.getOnlinePlayers().size() == 0){
      Utilities.logWarning(true, "There are currently no players online!");
      return;
    }

    for(Player player : Bukkit.getOnlinePlayers()) {
      if(action.equalsIgnoreCase("remove")) {
        // Remove night vision from the target
        userDataHandler.setEffectStatus(player.getUniqueId(), false);
        Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);

        Utilities.broadcast(false, messagesHandler.string("Night_Vision_Messages.Night_Vision_Removed_Global", "#2b9bbfNight Vision has been removed for all players!"));
        toggleSoundEffect(player, "Night_Vision_Disabled");
        return;
      }

      if(action.equalsIgnoreCase("add")) {
        // Check if the target has particle bypass perm and apply correct night vision effect
        applyNightVisionGlobal(player);

        // Send night vision applied messages
        Utilities.broadcast(false, messagesHandler.string("Night_Vision_Messages.Night_Vision_Applied_Global", "#2b9bbfNight Vision has been applied for all players!"));
      }
    }
  }

  private void applyNightVision(final Player player, final int duration) {
    if(checkLimitStatus(player)) {
      userDataHandler.setEffectStatus(player.getUniqueId(), true);
      if(Utilities.checkPermissions(player, false, "omegavision.nightvision.particles.bypass")) {
        Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, duration ,1, false, false, false);
      } else {
        Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, duration ,1, particleEffects, particleAmbients, nightVisionIcon);
      }
      toggleSoundEffect(player, "Night_Vision_Applied");
      increaseLimitAmount(player);
      return;
    }
    Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Reached", "#f63e3eSorry, you have reached the limit for the night vision command!"));
    toggleSoundEffect(player, "Limit_Reached");
  }

  public void applyNightVisionGlobal(final Player player) {
    userDataHandler.setEffectStatus(player.getUniqueId(), true);
    if(Utilities.checkPermissions(player, false, "omegavision.nightvision.particles.bypass")) {
      Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100 ,1, false, false, false);
    } else {
      Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100 ,1, particleEffects, particleAmbients, nightVisionIcon);
    }
    toggleSoundEffect(player, "Night_Vision_Applied");
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

  private boolean checkLimitStatus(@NotNull final Player player) {
    if(!configFile.getBoolean("Night_Vision_Limit.Enabled")) {
      return true;
    }

    if(Utilities.checkPermissions(player, true, "omegavision.limit.bypass", "omegavision.limit.admin", "omegavision.admin")) {
      return true;
    }

    return userDataHandler.getLimitStatus(player.getUniqueId()) < configFile.getInt("Night_Vision_Limit.Limit");
  }

  private void increaseLimitAmount(@NotNull final Player player) {
    if(!configFile.getBoolean("Night_Vision_Limit.Enabled")) {
      return;
    }

    if(Utilities.checkPermissions(player, true, "omegavision.limit.bypass", "omegavision.limit.admin", "omegavision.admin")) {
      return;
    }
    int currentLimitCount = userDataHandler.getLimitStatus(player.getUniqueId());

    if(currentLimitCount + 1 >= configFile.getInt("Night_Vision_Limit.Limit")) {
      userDataHandler.setLimitStatus(player.getUniqueId(), configFile.getInt("Night_Vision_Limit.Limit"));
      Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Reached", "#f63e3eSorry, you have reached the limit for the night vision command!"));
      toggleSoundEffect(player, "Limit_Reached");
      return;
    }

    userDataHandler.setLimitStatus(player.getUniqueId(), currentLimitCount + 1);
    Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Amount_Increased", "#1fe3e0Your limit amount now stands at: #f63e3e%currentLimitAmount% / %maxLimitAmount%")
      .replace("%currentLimitAmount%", String.valueOf(currentLimitCount))
      .replace("%maxLimitAmount%", String.valueOf(configFile.getInt("Night_Vision_Limit.Limit")))
    );
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

  private void toggleSoundEffect(final Player player, final String soundEffect) {

    if(!configFile.getBoolean("Sound_Effects.Enabled")) {
      return;
    }

    switch(soundEffect) {
      case "Night_Vision_Applied":
        if(!configFile.getBoolean("Sound_Effects.Night_Vision_Enable.Enabled")) {
          break;
        }
        player.playSound(player.getLocation(), configFile.getString("Sound_Effects.Night_Vision_Enable.Sound"), 1, 1);
        break;
      case "Night_Vision_Disabled:":
        if(!configFile.getBoolean("Sound_Effects.Night_Vision_Disable.Enabled")) {
          break;
        }
        player.playSound(player.getLocation(), configFile.getString("Sound_Effects.Night_Vision_Disable.Sound"), 1, 1);
        break;
      case "Limit_Reached":
        if(!configFile.getBoolean("Sound_Effects.Limit_Reached.Enabled")) {
          break;
        }
        player.playSound(player.getLocation(), configFile.getString("Sound_Effects.Limit_Reached.Sound"), 1, 1);
        break;
      default:
        break;
    }
  }
}
