package me.omegaweapondev.omegavision.events;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessagesHandler;
import me.omegaweapondev.omegavision.utils.NightVisionToggle;
import me.omegaweapondev.omegavision.utils.UserDataHandler;
import me.ou.library.Utilities;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerListener implements Listener {
  private final OmegaVision pluginInstance;
  private final FileConfiguration configFile;

  private final UserDataHandler userDataHandler;
  private final boolean particleEffects;
  private final boolean ambientEffects;
  private final boolean nightvisionIcon;

  public PlayerListener(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    configFile = pluginInstance.getStorageManager().getConfigFile().getConfig();
    userDataHandler = pluginInstance.getUserDataHandler();

    particleEffects = configFile.getBoolean("Night_Vision_Settings.Particle_Effects");
    ambientEffects = configFile.getBoolean("Night_Vision_Settings.Particle_Ambient");
    nightvisionIcon = configFile.getBoolean("Night_Vision_Settings.Night_Vision_Icon");
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
    final Player player = playerJoinEvent.getPlayer();

    if(player.getFirstPlayed() == System.currentTimeMillis()) {
      userDataHandler.getUserDataMap().putIfAbsent(player.getUniqueId(), new ConcurrentHashMap<>());
    } else {
      userDataHandler.addUserToMap(player.getUniqueId());
    }

    if(configFile.getBoolean("Night_Vision_Settings.Night_Vision_Login") && userDataHandler.getEffectStatus(player.getUniqueId()) && Utilities.checkPermissions(player, true, "omegavision.nightvision.login", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100 ,1, particleEffects, ambientEffects, nightvisionIcon);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
    userDataHandler.saveUserDataToFile(playerQuitEvent.getPlayer().getUniqueId());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerRespawn(PlayerRespawnEvent playerRespawnEvent) {
    Player player = playerRespawnEvent.getPlayer();

    if(!Utilities.checkPermissions(player, false, "omegavision.nightvision.keepondeath", "omegavision.nightvision.admin", "omegavision.admin")) {
     return;
    }

    if(userDataHandler.getEffectStatus(player.getUniqueId())) {
      NightVisionToggle nightVisionToggle = new NightVisionToggle(pluginInstance, player);
      nightVisionToggle.applyNightVisionGlobal(player);
    }
  }
}