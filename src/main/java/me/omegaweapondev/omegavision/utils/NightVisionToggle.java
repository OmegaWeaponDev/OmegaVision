package me.omegaweapondev.omegavision.utils;

import me.omegaweapondev.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
/**
 *
 * The Night Vision Toggle class which handles toggling the night vision potion effect for a player
 *
 * @author OmegaWeaponDev
 */
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

  /**
   *
   * The public constructor for the Night Vision Toggle class
   *
   * @param pluginInstance (The plugin's instance)
   * @param player (The player currently targeted)
   */
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

    hasNightVision = (boolean) userDataHandler.getEffectStatus(player.getUniqueId(), UserDataHandler.NIGHT_VISION);
  }

  /**
   *
   * Handles toggling night vision on|off for a specific player
   *
   */
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
      toggleSoundEffect(player, "Night_Vision_Disabled");
      return;
    }

    // Check if they have particle bypass perm and apply correct night vision effect
    applyNightVision(player, 60 * 60 * 24 * 100);
  }

  /**
   *
   * Handles toggling night vision on|off for a target player
   *
   * @param target (The player whose night vision status is to be modified)
   */
  public void nightVisionToggleOthers(final Player target) {
    // Check if the player has permission
    if(!toggleOthersPerm(player)) {
      return;
    }

    if(target.getName().equals(player.getName())) {
      if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.toggle", "omegavision.nightvision.admin", "omegavision.admin")) {
        Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
        return;
      }
    }

    // Check if the target currently has night vision enabled
    if((boolean) userDataHandler.getEffectStatus(target.getUniqueId(), UserDataHandler.NIGHT_VISION)) {
      // Remove night vision from the target
      userDataHandler.setEffectStatus(target.getUniqueId(), false, UserDataHandler.NIGHT_VISION);
      Utilities.removePotionEffect(target, PotionEffectType.NIGHT_VISION);

      // Send night vision removal messages
      sendNightVisionRemovedMessages(target);
      toggleSoundEffect(target, "Night_Vision_Disabled");
      return;
    }

    // Check if the target has particle bypass perm and apply correct night vision effect
    applyNightVision(target, 60 * 60 * 24 * 100);

    // Send night vision applied messages
    sendNightVisionAppliedMessages(target);
  }

  /**
   *
   * Handles toggling night vision on for a specific amount of time
   *
   * @param target (The player whose night vision status is to be modified)
   * @param seconds (The duration in seconds for how long the night vision will last)
   */
  public void nightVisionToggleTemp(final Player target, final int seconds) {
    // Check if the player has permission
    if(!toggleTempPerm(player)) {
      return;
    }

    // Check if the target currently has night vision enabled
    if((boolean) userDataHandler.getEffectStatus(target.getUniqueId(), UserDataHandler.NIGHT_VISION)) {
      // Remove night vision from the target
      Utilities.removePotionEffect(target, PotionEffectType.NIGHT_VISION);
    }

    // Check if the target has particle bypass perm and apply correct night vision effect
    applyNightVision(target, seconds);

    // Send night vision applied messages
    sendNightVisionAppliedMessages(target);
  }

  /**
   *
   * Handles toggling night vision on|off for all players currently online
   *
   * @param action (Either `add` | `remove`)
   */
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
        userDataHandler.setEffectStatus(player.getUniqueId(), false, UserDataHandler.NIGHT_VISION);
        Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);

        if(Utilities.checkPermissions(player, true, "omegavision.nightvision.global.alert", "omegavision.nightvision.admin", "omegavision.admin")) {
          Utilities.message(player, messagesHandler.string("Night_Vision_Messages.Night_Vision_Removed_Global", "#2b9bbfNight Vision has been removed for all players!"));
        }
        toggleSoundEffect(player, "Night_Vision_Disabled");
        return;
      }

      if(action.equalsIgnoreCase("add")) {
        // Check if the target has particle bypass perm and apply correct night vision effect
        applyNightVisionGlobal(player);

        // Send night vision applied messages
        if(Utilities.checkPermissions(player, true, "omegavision.nightvision.global.alert", "omegavision.nightvision.admin", "omegavision.admin")) {
          Utilities.message(player, messagesHandler.string("Night_Vision_Messages.Night_Vision_Applied_Global", "#2b9bbfNight Vision has been applied for all players!"));
        }
      }
    }
  }

  /**
   *
   * Handles how the night vision effect is applied for the player
   *
   * @param player (The player that night vision is to be applied to)
   * @param duration (The duration in seconds for how long the night vision will last)
   */
  private void applyNightVision(final Player player, final int duration) {
    increaseLimitAmount(player);
    if(!hasReachedLimit(player)) {
      userDataHandler.setEffectStatus(player.getUniqueId(), true, UserDataHandler.NIGHT_VISION);
      userDataHandler.setEffectStatus(player.getUniqueId(), false, UserDataHandler.LIMIT_REACHED);
      if(Utilities.checkPermissions(player, false, "omegavision.nightvision.particles.bypass", "omegavision.nightvision.admin", "omegavision.admin")) {
        Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, duration ,1, false, false, false);
      } else {
        Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, duration ,1, particleEffects, particleAmbients, nightVisionIcon);
      }
      toggleSoundEffect(player, "Night_Vision_Applied");
      sendNightVisionAppliedMessages(player);
      return;
    }
    Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Reached", "#f63e3eSorry, you have reached the limit for the night vision command!"));
    toggleSoundEffect(player, "Limit_Reached");
  }

  /**
   *
   * Handles how the night vision effect is applied for all player currently online
   *
   * @param player (The player that night vision is to be applied to)
   */
  public void applyNightVisionGlobal(final Player player) {
    userDataHandler.setEffectStatus(player.getUniqueId(), true, UserDataHandler.NIGHT_VISION);
    if(Utilities.checkPermissions(player, false, "omegavision.nightvision.particles.bypass", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100 ,1, false, false, false);
    } else {
      Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100 ,1, particleEffects, particleAmbients, nightVisionIcon);
    }
    toggleSoundEffect(player, "Night_Vision_Applied");
  }

  /**
   *
   * Sends the player a message notifying them that night vision has been applied
   *
   * @param player (The player that the message needs to be sent to)
   */
  private void sendNightVisionAppliedMessages(final Player player) {
    Utilities.message(player, nightVisionApplied);

    if(configFile.getBoolean("Night_Vision_Settings.ActionBar_Messages")) {
      Utilities.sendActionBar(player, nightVisionAppliedActionbar);
    }
  }

  /**
   *
   * Sends the player a message notifying them that night vision has been removed
   *
   * @param player (The player that the message needs to be sent to)
   */
  private void sendNightVisionRemovedMessages(final Player player) {
    Utilities.message(player, nightVisionRemoved);
    if(actionBarMessages)
    {
      Utilities.sendActionBar(player, nightVisionRemovedActionbar);
    }
  }

  /**
   *
   * Checks a player's current Night Vision Limit status
   *
   * @param player (The player whose night vision limit status is being checked)
   * @return (The current limit status)
   */
  private boolean hasReachedLimit(@NotNull final Player player) {
    if(!configFile.getBoolean("Night_Vision_Limit.Enabled")) {
      return false;
    }

    if(Utilities.checkPermissions(player, true, "omegavision.limit.bypass", "omegavision.limit.admin", "omegavision.admin")) {
      return false;
    }

    return (boolean) userDataHandler.getEffectStatus(player.getUniqueId(), UserDataHandler.LIMIT_REACHED);
  }

  /**
   *
   * Increase a specific players night vision limit amount
   *
   * @param player (The player whose limit is getting increased)
   */
  private void increaseLimitAmount(@NotNull final Player player) {
    if(!configFile.getBoolean("Night_Vision_Limit.Enabled")) {
      return;
    }

    if(Utilities.checkPermissions(player, true, "omegavision.limit.bypass", "omegavision.limit.admin", "omegavision.admin")) {
      return;
    }
    int currentLimitCount = (int) userDataHandler.getEffectStatus(player.getUniqueId(), UserDataHandler.LIMIT);

    if((currentLimitCount + 1) > configFile.getInt("Night_Vision_Limit.Limit")) {
      userDataHandler.setEffectStatus(player.getUniqueId(), configFile.getInt("Night_Vision_Limit.Limit"), UserDataHandler.LIMIT);
      userDataHandler.setEffectStatus(player.getUniqueId(), true, UserDataHandler.LIMIT_REACHED);
      userDataHandler.setEffectStatus(player.getUniqueId(), System.currentTimeMillis(), UserDataHandler.LIMIT_REACHED_TIME);
      limitResetTimer(player);
      return;
    }

    userDataHandler.setEffectStatus(player.getUniqueId(), currentLimitCount + 1, UserDataHandler.LIMIT);
    Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Amount_Increased", "#1fe3e0Your limit amount now stands at: #f63e3e%currentLimitAmount% / %maxLimitAmount%")
      .replace("%currentLimitAmount%", String.valueOf(currentLimitCount + 1))
      .replace("%maxLimitAmount%", String.valueOf(configFile.getInt("Night_Vision_Limit.Limit")))
    );
  }

  /**
   *
   * Handles resetting a players limit status after a specific timeframe
   * Is triggered once the player has reached the max limit
   *
   * @param player (The player whose night vision limit is being reset)
   */
  public void limitResetTimer(final Player player) {
    if(!(boolean) userDataHandler.getEffectStatus(player.getUniqueId(), UserDataHandler.LIMIT_REACHED)) {
      return;
    }

    final UUID playerUUID = player.getUniqueId();

    long configResetTimer = TimeUnit.MILLISECONDS.convert(configFile.getInt("Night_Vision_Limit.Reset_Timer"), TimeUnit.MINUTES);
    long limitTimeReached = (long) userDataHandler.getEffectStatus(player.getUniqueId(), UserDataHandler.LIMIT_REACHED_TIME);

    if(System.currentTimeMillis() >= (limitTimeReached + configResetTimer)) {
      userDataHandler.setEffectStatus(player.getUniqueId(), 0, UserDataHandler.LIMIT);
      userDataHandler.setEffectStatus(player.getUniqueId(), false, UserDataHandler.LIMIT_REACHED);
      userDataHandler.setEffectStatus(player.getUniqueId(), 0, UserDataHandler.LIMIT_REACHED_TIME);
      userDataHandler.setEffectStatus(player.getUniqueId(), false, UserDataHandler.RESET_TIMER_ACTIVE);
      if(player.isOnline()) {
        Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Reset", "#1fe3e0Your night vision limits have reset! You can use the night vision command again!"));
      }
      return;
    }

    if((boolean) userDataHandler.getEffectStatus(player.getUniqueId(), UserDataHandler.RESET_TIMER_ACTIVE)) {
      return;
    }

    userDataHandler.setEffectStatus(player.getUniqueId(), true, UserDataHandler.RESET_TIMER_ACTIVE);
    Bukkit.getScheduler().runTaskLaterAsynchronously(pluginInstance, () -> {
      if(player.isOnline()) {
        userDataHandler.setEffectStatus(playerUUID, 0, UserDataHandler.LIMIT);
        userDataHandler.setEffectStatus(playerUUID, false, UserDataHandler.LIMIT_REACHED);
        userDataHandler.setEffectStatus(playerUUID, 0, UserDataHandler.LIMIT_REACHED_TIME);
        userDataHandler.setEffectStatus(playerUUID, false, UserDataHandler.RESET_TIMER_ACTIVE);

        Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Reset", "#1fe3e0Your night vision limits have reset! You can use the night vision command again!"));
      } else {
        pluginInstance.getStorageManager().getUserDataFile().getConfig().set("Users." + playerUUID + "." + UserDataHandler.LIMIT_REACHED, false);
        pluginInstance.getStorageManager().getUserDataFile().getConfig().set("Users." + playerUUID + "." + UserDataHandler.RESET_TIMER_ACTIVE, false);
        pluginInstance.getStorageManager().getUserDataFile().getConfig().set("Users." + playerUUID + "." + UserDataHandler.LIMIT_REACHED_TIME, 0);
        pluginInstance.getStorageManager().getUserDataFile().getConfig().set("Users." + playerUUID + "." + UserDataHandler.LIMIT, 0);
      }

    }, configResetTimer / 50);
  }

  /**
   *
   * Checks the player's permission for the toggle-self command
   *
   * @param player (The player who is being checked for permission)
   * @return (True | false depending on the permission)
   */
  private boolean toggleSelfPerm(final Player player) {
    if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.toggle", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
      return false;
    }
    return true;
  }

  /**
   *
   * Checks the player's permission for the toggle-global command
   *
   * @param player (The player who is being checked for permission)
   * @return (True | false depending on the permission)
   */
  private boolean toggleGlobalPerm(final Player player) {
    if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.global", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
      return false;
    }
    return true;
  }

  /**
   *
   * Checks the player's permission for the toggle-temp command
   *
   * @param player (The player who is being checked for permission)
   * @return (True | false depending on the permission)
   */
  private boolean toggleTempPerm(final Player player) {
    if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.temp", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
      return false;
    }
    return true;
  }

  /**
   *
   * Checks the player's permission for the toggle-others command
   *
   * @param player (The player who is being checked for permission)
   * @return (True | false depending on the permission)
   */
  private boolean toggleOthersPerm(final Player player) {
    if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.toggle.others", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
      return false;
    }
    return true;
  }

  /**
   *
   * Toggles a sound effect depending on the action taken for a specific player
   *
   * @param player (The player to play the sound effect for)
   * @param soundEffect (The specific sound effect that needs to be played)
   */
  public void toggleSoundEffect(final Player player, final String soundEffect) {

    if(!configFile.getBoolean("Sound_Effects.Enabled")) {
      return;
    }

    switch(soundEffect) {
      case "Night_Vision_Applied":
        if(!configFile.getBoolean("Sound_Effects.Night_Vision_Enable.Enabled")) {
          break;
        }
        player.playSound(player.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Night_Vision_Enable.Sound")) , 1, 1);
        break;
      case "Night_Vision_Disabled:":
        if(!configFile.getBoolean("Sound_Effects.Night_Vision_Disable.Enabled")) {
          break;
        }
        player.playSound(player.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Night_Vision_Disable.Sound")), 1, 1);
        break;
      case "Limit_Reached":
        if(!configFile.getBoolean("Sound_Effects.Limit_Reached.Enabled")) {
          break;
        }
        player.playSound(player.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Limit_Reached.Sound")), 1, 1);
        break;
      default:
        break;
    }
  }
}
