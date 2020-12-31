package me.omegaweapondev.omegavision.events;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessageHandler;
import me.omegaweapondev.omegavision.utils.NightVisionToggle;
import me.ou.library.SpigotUpdater;
import me.ou.library.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {
  private final FileConfiguration configFile = OmegaVision.getInstance().getConfigFile().getConfig();
  private final MessageHandler messageHandler = new MessageHandler(OmegaVision.getInstance().getMessagesFile().getConfig());
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
    Player player = playerJoinEvent.getPlayer();

    nightVisionLogin(player);
    nightVisionLimitReset(player);

    if(!Utilities.checkPermissions(player, true, "omegavision.update", "omegavision.admin")) {
      return;
    }

    new SpigotUpdater(OmegaVision.getInstance(), 73013).getVersion(version -> {
      if (!OmegaVision.getInstance().getDescription().getVersion().equalsIgnoreCase(version)) {
        PluginDescriptionFile pdf = OmegaVision.getInstance().getDescription();
        Utilities.message(player,
          "&bA new version of &c" + pdf.getName() + " &bis avaliable!",
          "&bCurrent Version: &c" + pdf.getVersion() + " &b> New Version: &c" + version,
          "&bGrab it here:&c https://github.com/OmegaWeaponDev/OmegaVision"
        );
      }
    });
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
    Player player = playerQuitEvent.getPlayer();
    final NightVisionToggle nvToggle = new NightVisionToggle(player);
    
    // Remove the players from the map when they quit
    nvToggle.playerMap.remove(player.getUniqueId());
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onMilkBucketUse(PlayerItemConsumeEvent playerItemConsumeEvent) {
    Player player = playerItemConsumeEvent.getPlayer();


    if(!playerItemConsumeEvent.getItem().getType().equals(Material.MILK_BUCKET)) {
      return;
    }

    if(!configFile.getBoolean("Bucket_Usage")) {
      return;
    }

    if(!Utilities.checkPermissions(player, true, "omegavision.bucket", "omegavision.admin")) {
      return;
    }
  
    if(!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
      return;
    }

    playerItemConsumeEvent.setCancelled(true);

    for(PotionEffect effects : player.getActivePotionEffects()) {
      Utilities.removePotionEffect(player, effects.getType());
    }

    Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1, false, false, false);
    Utilities.message(player, messageHandler.string("Bucket_Message", "&9Particle Effects and the icon have been removed!"));

    if(!configFile.getBoolean("Bucket_Empty")) {
      return;
    }

    player.getInventory().getItemInMainHand().setType(Material.BUCKET);
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerDeath(PlayerRespawnEvent e) {
    Player player = e.getPlayer();
    Boolean nightVision = configFile.getBoolean(player.getUniqueId().toString() + ".NightVision");

    if(!configFile.getBoolean("Keep_NightVision_On_Death")) {
      return;
    }

    if(!Utilities.checkPermissions(player, true, "omegavision.death", "omegavision.admin")) {
      return;
    }

  
    if(!nightVision.equals(true)) {
      return;
    }

    Bukkit.getScheduler().runTaskLater(OmegaVision.getInstance(), () -> {
      Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1,
        configFile.getBoolean("Particle_Ambient"),
        configFile.getBoolean("Particle_Effects"),
        configFile.getBoolean("NightVision_Icon")
      );
    }, (1));
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onWorldChange(PlayerChangedWorldEvent changedWorldEvent) {
    final Player player = changedWorldEvent.getPlayer();

    if(!configFile.getBoolean("World_Disable.Enabled")) {
      return;
    }

    if(Utilities.checkPermissions(player, true, "omegavision.nightvision.worldbypass", "omegavision.admin")) {
      return;
    }

    if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
      Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);
      Utilities.message(player, messageHandler.string("NightVision_World_Disable", "&cYour nightvision has been disabled."));
    }
  }

  private void nightVisionLogin(final Player player) {
    final NightVisionToggle nvToggle = new NightVisionToggle(player);
    Boolean nightVision = OmegaVision.getInstance().getPlayerData().getConfig().getBoolean(player.getUniqueId().toString() + ".NightVision.Enabled");

    if(!configFile.getBoolean("Night_Vision_Login")) {
      return;
    }

    if(!Utilities.checkPermissions(player, true, "omegavision.login", "omegavision.admin")) {
      nvToggle.nightvisionDisable();
      return;
    }

    if(nightVision.equals(true)) {
      nvToggle.nightVisionEnable();
      return;
    }

    nvToggle.nightvisionDisable();
  }

  private void nightVisionLimitReset(final Player player) {
    final NightVisionToggle nvToggle = new NightVisionToggle(player);

    if(!configFile.getBoolean("Night_Vision_Limit.Enabled")) {
      return;
    }

    if(Utilities.checkPermissions(player, true, "omegavision.limit.all", "omegavision.limit.bypass", "omegavision.admin")) {
      return;
    }

    if(nvToggle.nightvisionLimitReached.get(player.getUniqueId()) == null) {
      return;
    }

    Bukkit.getScheduler().scheduleSyncRepeatingTask(OmegaVision.getInstance(), () -> {
      int configResetTimer = configFile.getInt("Night_Vision_Limit.Reset_Timer");

      Long resetTimer = TimeUnit.MILLISECONDS.convert(configResetTimer, TimeUnit.MINUTES);
      Long limitReachedTime = nvToggle.nightvisionLimitReached.get(player.getUniqueId());

      if(!(System.currentTimeMillis() >= (limitReachedTime + resetTimer))) {
        return;
      }

      configFile.set(player.getUniqueId().toString() + ".Limit", 0);
      OmegaVision.getInstance().getPlayerData().saveConfig();
    }, 20L, 20L * 60L);

    if(configFile.getBoolean("Sound_Effects.Limit_Reset.Enabled")) {
      player.playSound(player.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Limit_Reset.Sound")), 1, 1);
    }
  }
}