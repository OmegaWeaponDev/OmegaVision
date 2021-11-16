package me.omegaweapondev.omegavision.events;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessagesHandler;
import me.omegaweapondev.omegavision.utils.NightVisionToggle;
import me.omegaweapondev.omegavision.utils.UserDataHandler;
import me.ou.library.Utilities;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerListener implements Listener {
  private final OmegaVision pluginInstance;
  private final FileConfiguration configFile;
  private final MessagesHandler messagesHandler;

  private final UserDataHandler userDataHandler;
  private final boolean particleEffects;
  private final boolean ambientEffects;
  private final boolean nightvisionIcon;

  public PlayerListener(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    configFile = pluginInstance.getStorageManager().getConfigFile().getConfig();
    userDataHandler = pluginInstance.getUserDataHandler();
    messagesHandler = pluginInstance.getMessagesHandler();

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
  public void onWorldChange(PlayerChangedWorldEvent playerChangedWorldEvent) {
    final Player player = playerChangedWorldEvent.getPlayer();

    if(!configFile.getBoolean("World_Disable.Enabled")) {
      return;
    }

    if(Utilities.checkPermissions(player, true, "omegavision.nightvision.world.bypass", "omegavision.nightvision.admin", "omegavision.admin")) {
      return;
    }

    for(String worldName : configFile.getStringList("World_Disable.Worlds")) {
      if(!userDataHandler.getEffectStatus(player.getUniqueId())) {
        return;
      }

      if(player.getWorld().getName().equalsIgnoreCase(worldName)) {
        userDataHandler.setEffectStatus(player.getUniqueId(), false);
        Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);
        return;
      }
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

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBucketUse(PlayerItemConsumeEvent playerItemConsumeEvent) {
    final Player player = playerItemConsumeEvent.getPlayer();
    final ItemStack item = playerItemConsumeEvent.getItem();

    if(!configFile.getBoolean("Night_Vision_Settings.Bucket_Usage")) {
      return;
    }

    if(!item.getData().getItemType().equals(Material.MILK_BUCKET)) {
      return;
    }

    if(!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
      return;
    }

    if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.bucket", "omegavision.nightvision.admin", "omegavision.admin")) {
      return;
    }
    playerItemConsumeEvent.setCancelled(true);

    for(PotionEffect activeEffects : player.getActivePotionEffects()) {
      Utilities.removePotionEffect(player, activeEffects.getType());
    }
    Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1, false, false, false);
    Utilities.message(player, messagesHandler.string("Night_Vision_Messages.Bucket_Message", "#2b9bbfThe particle effects and the icon have been removed!"));

    if(!configFile.getBoolean("Night_Vision_Settings.Bucket_Empty")) {
      return;
    }

    player.getInventory().getItemInMainHand().setType(Material.BUCKET);
  }
}