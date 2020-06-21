package me.omegaweapon.omegavision.events;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.UpdateChecker;
import me.omegaweapon.omegavision.utils.MessageHandler;
import me.omegaweapon.omegavision.utils.NightVisionToggle;
import me.ou.library.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
    Player player = playerJoinEvent.getPlayer();
    Boolean nightVision = OmegaVision.getPlayerData().getConfig().getBoolean(player.getUniqueId().toString() + ".NightVision");
    
    // Apply / Remove nightvision on player join depending on permissions & config settings
    if(OmegaVision.getConfigFile().getConfig().getBoolean("Night_Vision_Login") && nightVision.equals(true) && Utilities.checkPermission(player, true, "omegavision.login")) {
      // Add the potion effect to the player and send them a message
      NightVisionToggle.nightVisionEnable(player);
    } else if(OmegaVision.getConfigFile().getConfig().getBoolean("Night_Vision_Login") && nightVision.equals(false) && Utilities.checkPermission(player, true, "omegavision.login")) {
      NightVisionToggle.nightvisionDisable(player);
    }
  
    // Send the player a message on join if there is an update for the plugin
    if(Utilities.checkPermission(player, true, "omegavision.update")) {
      new UpdateChecker(OmegaVision.getInstance(), 73013).getVersion(version -> {
        if (!OmegaVision.getInstance().getDescription().getVersion().equalsIgnoreCase(version)) {
          PluginDescriptionFile pdf = OmegaVision.getInstance().getDescription();
          Utilities.message(player,
            "&bA new version of &c" + pdf.getName() + " &bis avaliable!",
            "&bCurrent Version: &c" + pdf.getVersion() + " &b> New Version: &c" + version,
            "&bGrab it here:&c https://spigotmc.org/resources/73013"
          );
        }
      });
    }

    if(OmegaVision.getConfigFile().getConfig().getBoolean("Night_Vision_Limit.Enabled")) {
      if(!player.hasPermission("omegavision.limit.bypass") && (NightVisionToggle.nightvisionLimitReached.get(player.getUniqueId()) != null)) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(OmegaVision.getInstance(), () -> {
          int configResetTimer = OmegaVision.getConfigFile().getConfig().getInt("Night_Vision_Limit.Reset_Timer");

          Long resetTimer = TimeUnit.MILLISECONDS.convert(configResetTimer, TimeUnit.MINUTES);
          Long limitReachedTime = NightVisionToggle.nightvisionLimitReached.get(player.getUniqueId());

          if(System.currentTimeMillis() >= (limitReachedTime + resetTimer)) {
            OmegaVision.getPlayerData().getConfig().set(player.getUniqueId().toString() + ".Limit", 0);
            OmegaVision.getPlayerData().saveConfig();
          }
        }, 20L, 20L * 60L);
      }
    }

  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
    Player player = playerQuitEvent.getPlayer();
    
    // Remove the players from the map when they quit
    NightVisionToggle.playerMap.remove(player.getUniqueId());
  }
  
  @EventHandler
  public void onMilkBucketUse(PlayerItemConsumeEvent playerItemConsumeEvent) {
    Player player = playerItemConsumeEvent.getPlayer();
  
    if(Utilities.checkPermission(player, true, "omegavision.bucket")) {

      // Make sure they are holding a milkbucket
      if(playerItemConsumeEvent.getItem().getType().equals(Material.MILK_BUCKET)) {
        // Check if they have nightvision and the bucket_usage config setting is true
        if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION) && OmegaVision.getConfigFile().getConfig().getBoolean("Bucket_Usage")) {
          // Cancel the default bucket event
          playerItemConsumeEvent.setCancelled(true);
        
          // Get all the potion effects the player has then remove them
          for(PotionEffect effects : player.getActivePotionEffects()) {
            Utilities.removePotionEffect(player, effects.getType());
          }
          // Re-apply the nightvision effect but without the potion effects
          Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1, false, false, false);
          // Check if the bucket empty config setting is true
          if(OmegaVision.getConfigFile().getConfig().getBoolean("Bucket_Empty")) {
            // Remove the milk bucket and give them an empty bucket
            player.getInventory().getItemInMainHand().setType(Material.BUCKET);
          }
          // Send the player a message to inform them that the milk bucket has worked
          Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.bucketMessage());
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerDeath(PlayerRespawnEvent e) {
    Player player = e.getPlayer();
    Boolean nightVision = OmegaVision.getPlayerData().getConfig().getBoolean(player.getUniqueId().toString() + ".NightVision");
  
    if(OmegaVision.getConfigFile().getConfig().getBoolean("Keep_NightVision_On_Death") && nightVision.equals(true) && Utilities.checkPermission(player, true, "omegavision.death")) {
      Bukkit.getScheduler().runTaskLater(OmegaVision.getInstance(), () -> {
        Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1,
          OmegaVision.getConfigFile().getConfig().getBoolean("Particle_Ambient"),
          OmegaVision.getConfigFile().getConfig().getBoolean("Particle_Effects"),
          OmegaVision.getConfigFile().getConfig().getBoolean("NightVision_Icon")
        );
      }, (1));
    }
  }
}