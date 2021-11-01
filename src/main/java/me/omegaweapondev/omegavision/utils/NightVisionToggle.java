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
  private final OmegaVision pluginInstance;
  private final FileConfiguration configFile;
  private final MessagesHandler messagesHandler;
  private final UserDataHandler userDataHandler;

  private final boolean particleEffects;
  private final boolean particleAmbients;
  private final boolean nightVisionIcon;
  private final boolean hasNightVision;

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

    hasNightVision = userDataHandler.getEffectStatus(player.getUniqueId(), UserDataHandler.NIGHT_VISION);
  }

  public void nightVisionToggle() {
    if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.toggle", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
      return;
    }

    if(hasNightVision) {
      userDataHandler.setEffectStatus(player.getUniqueId(), false, UserDataHandler.NIGHT_VISION);
      Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);
      return;
    }

    userDataHandler.setEffectStatus(player.getUniqueId(), true, UserDataHandler.NIGHT_VISION);

    if(Utilities.checkPermissions(player, false, "omegavision.nightvision.particles.bypass")) {
      Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100 ,1, false, false, false);

      Utilities.message(player, messagesHandler.string("Night_Vision_Messages.Night_Vision_Removed", "#f63e3eNight Vision has been removed!"));

      if(configFile.getBoolean("Night_Vision_Settings.ActionBar_Messages")) {
        Utilities.sendActionBar(player, messagesHandler.string("Night_Vision_Messages.ActionBar_Night_Vision_Removed", "#f63e3eNight Vision has been removed!"));
      }
      return;
    }

    Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100 ,1, particleEffects, particleAmbients, nightVisionIcon);

    Utilities.message(player, messagesHandler.string("Night_Vision_Messages.Night_Vision_Applied", "#2b9bbfNight Vision has been applied!"));

    if(configFile.getBoolean("Night_Vision_Settings.ActionBar_Messages")) {
      Utilities.sendActionBar(player, messagesHandler.string("Night_Vision_Messages.ActionBar_Night_Vision_Applied", "#2b9bbfNight vision has been applied!"));
    }
  }
}
