package me.omegaweapon.omegavision.events;

import me.omegaweapon.omegavision.OmegaUpdater;
import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.command.MainCommand;
import me.omegaweapon.omegavision.settings.ConfigFile;
import me.omegaweapon.omegavision.settings.MessagesFile;
import me.omegaweapon.omegavision.settings.PlayerData;
import me.omegaweapon.omegavision.utils.ColourUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {
  private OmegaVision plugin;
  
  public PlayerListener(OmegaVision pl) {
    this.plugin = pl;
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
    Player player = playerJoinEvent.getPlayer();
    Boolean nightVision = PlayerData.getPlayerData().getBoolean(player.getUniqueId().toString() + ".NightVision");
    
    // Apply/Remove nightvision on join
    if(ConfigFile.NIGHT_VISION_LOGIN.equals(true) && nightVision.equals(true) && (player.hasPermission("omegavision.login") || player.isOp())) {
      player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, ConfigFile.PARTICLE_AMBIENT, ConfigFile.PARTICLE_EFFECTS, ConfigFile.NIGHTVISION_ICON));
      MainCommand.getPlayerMap().put(player.getUniqueId(), player.getName());
      player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.NIGHTVISION_APPLIED));
    } else {
      player.removePotionEffect(PotionEffectType.NIGHT_VISION);
      MainCommand.getPlayerMap().remove(player.getUniqueId());
    }
  
    // Sends Update message depending on config setting.
    // Update Checker
    if(player.hasPermission("omegavision.update") || player.isOp()) {
      new OmegaUpdater(73013) {
    
        @Override
        public void onUpdateAvailable() {
          player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + "&9 A new update has been released!"));
          player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + "&9 Your current version is: &c" + OmegaVision.getInstance().getDescription().getVersion()));
          player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + "&9 The latest version is: &c" + OmegaUpdater.getLatestVersion()));
          player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + "&9 You can update here: &chttps://www.spigotmc.org/resources/omegavision." + OmegaUpdater.getProjectId()));
        }
      }.runTaskAsynchronously(OmegaVision.getInstance());
    }

  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
    Player player = playerQuitEvent.getPlayer();
    
    MainCommand.getPlayerMap().remove(player.getUniqueId());
  }
  
  @EventHandler
  public void onMilkBucketUse(PlayerItemConsumeEvent playerItemConsumeEvent) {
    Player player = playerItemConsumeEvent.getPlayer();
    
    if(player.hasPermission("omegavision.bucket") || player.isOp()) {
      if(playerItemConsumeEvent.getItem().getType().equals(Material.MILK_BUCKET)) {
        if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION) && ConfigFile.BUCKET_USAGE.equals(true)) {
          playerItemConsumeEvent.setCancelled(true);
          
          for(PotionEffect effects : player.getActivePotionEffects()) {
            player.removePotionEffect(effects.getType());
          }
          player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false , false));
          if(ConfigFile.BUCKET_EMPTY.equals(true)) {
            player.getInventory().getItemInMainHand().setType(Material.BUCKET);
          }
          player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.BUCKET_MESSAGE));
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerDeath(PlayerRespawnEvent e) {
    Player player = e.getPlayer();
    Boolean nightVision = PlayerData.getPlayerData().getBoolean(player.getUniqueId() + ".NightVision");
  
    if(ConfigFile.KEEP_NIGHTVISION_ON_DEATH.equals(true) && nightVision.equals(true) && player.hasPermission("omegavision.death")) {
      Bukkit.getScheduler().runTaskLater(plugin, () -> {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, ConfigFile.PARTICLE_AMBIENT, ConfigFile.PARTICLE_EFFECTS, ConfigFile.NIGHTVISION_ICON));
      }, (1));
    }
  }
}
