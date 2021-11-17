package me.omegaweapondev.omegavision.events;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessagesHandler;
import me.omegaweapondev.omegavision.utils.NightVisionToggle;
import me.omegaweapondev.omegavision.utils.UserDataHandler;
import me.ou.library.SpigotUpdater;
import me.ou.library.Utilities;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * The class that handles all the events that the plugin listens to
 *
 * @author OmegaWeaponDev
 */
public class PlayerListener implements Listener {
  private final OmegaVision pluginInstance;
  private final FileConfiguration configFile;
  private final MessagesHandler messagesHandler;

  private final UserDataHandler userDataHandler;
  private final boolean particleEffects;
  private final boolean ambientEffects;
  private final boolean nightvisionIcon;

  /**
   *
   * The public constructor for the player listener class
   *
   * @param pluginInstance (The plugin's instance)
   */
  public PlayerListener(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    configFile = pluginInstance.getStorageManager().getConfigFile().getConfig();
    userDataHandler = pluginInstance.getUserDataHandler();
    messagesHandler = pluginInstance.getMessagesHandler();

    particleEffects = configFile.getBoolean("Night_Vision_Settings.Particle_Effects");
    ambientEffects = configFile.getBoolean("Night_Vision_Settings.Particle_Ambient");
    nightvisionIcon = configFile.getBoolean("Night_Vision_Settings.Night_Vision_Icon");
  }

  /**
   *
   * Listens for the Player Join Event
   * @see org.bukkit.event.player.PlayerJoinEvent
   *
   * @param playerJoinEvent (The player join event that was triggered)
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
    final Player player = playerJoinEvent.getPlayer();

    // Checks if the update notify setting was enabled and if the player has permission to see update notifications
    if(configFile.getBoolean("Update_Notify") && Utilities.checkPermissions(player, true, "omegavision.update", "omegavision.admin")) {
      // Check the plugins version against the spigotMC and see if it is up-to-date
      new SpigotUpdater(pluginInstance, 73013).getVersion(version -> {
        int spigotVersion = Integer.parseInt(version.replace(".", ""));
        int pluginVersion = Integer.parseInt(pluginInstance.getDescription().getVersion().replace(".", ""));

        if(pluginVersion >= spigotVersion) {
          Utilities.message(player, "#00D4FFYou are already running the latest version");
          return;
        }

        PluginDescriptionFile pdf = pluginInstance.getDescription();
        Utilities.message(player,
          "#00D4FFA new version of #FF4A4A" + pdf.getName() + " #00D4FFis avaliable!",
          "#00D4FFCurrent Version: #FF4A4A" + pdf.getVersion() + " #00D4FF> New Version: #FF4A4A" + version,
          "#00D4FFGrab it here:#FF4A4A https://www.spigotmc.org/resources/omegavision.73013/"
        );
      });
    }

    // Add the user to the user data map if they aren't currently in it.
    if(player.getFirstPlayed() == System.currentTimeMillis()) {
      userDataHandler.getUserDataMap().putIfAbsent(player.getUniqueId(), new ConcurrentHashMap<>());
    } else {
      userDataHandler.addUserToMap(player.getUniqueId());
    }

    // Check if the Night Vision Login setting was enabled and the player has a true night vision status in the map
    // If so, Apply night vision to them once they have logged in
    if(configFile.getBoolean("Night_Vision_Settings.Night_Vision_Login") && (boolean) userDataHandler.getEffectStatus(player.getUniqueId(), UserDataHandler.NIGHT_VISION) && Utilities.checkPermissions(player, true, "omegavision.nightvision.login", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100 ,1, particleEffects, ambientEffects, nightvisionIcon);
    }
  }

  /**
   *
   * Listens for the Player Changed World Event
   * @see org.bukkit.event.player.PlayerChangedWorldEvent
   *
   * @param playerChangedWorldEvent (The player changed world event that was triggered)
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onWorldChange(PlayerChangedWorldEvent playerChangedWorldEvent) {
    final Player player = playerChangedWorldEvent.getPlayer();

    // Checks if the world disabled setting has been enabled.
    if(!configFile.getBoolean("World_Disable.Enabled")) {
      return;
    }

    // Checks if the player has permission to bypass the night vision change world feature
    if(Utilities.checkPermissions(player, true, "omegavision.nightvision.world.bypass", "omegavision.nightvision.admin", "omegavision.admin")) {
      return;
    }

    // Checks the players new world against the list of worlds in the config
    for(String worldName : configFile.getStringList("World_Disable.Worlds")) {
      if(!(boolean) userDataHandler.getEffectStatus(player.getUniqueId(), UserDataHandler.NIGHT_VISION)) {
        return;
      }

      // If the world name is in the config world list, remove night vision from the player
      if(player.getWorld().getName().equalsIgnoreCase(worldName)) {
        userDataHandler.setEffectStatus(player.getUniqueId(), false, UserDataHandler.NIGHT_VISION);
        Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);
        return;
      }
    }
  }

  /**
   *
   * Listens for the Player Quit Event
   * @see org.bukkit.event.player.PlayerQuitEvent
   *
   * @param playerQuitEvent (The Player Quit Event that was triggered)
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
    // Save the user's data to the file
    userDataHandler.saveUserDataToFile(playerQuitEvent.getPlayer().getUniqueId());
  }

  /**
   *
   * Listens for the Player Respawn Event
   *
   * @param playerRespawnEvent (The Player Respawn Event that was triggered)
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerRespawn(PlayerRespawnEvent playerRespawnEvent) {
    Player player = playerRespawnEvent.getPlayer();

    // Checks if the keep on death feature has been enabled
    if(!configFile.getBoolean("Keep_Night_Vision_On_Death")) {
      return;
    }

    // Checks if the player has permission to keep their night vision when they die
    if(!Utilities.checkPermissions(player, false, "omegavision.nightvision.keepondeath", "omegavision.nightvision.admin", "omegavision.admin")) {
     return;
    }

    // Re-applies night vision to the player after they respawn
    userDataHandler.setEffectStatus(player.getUniqueId(), true, UserDataHandler.NIGHT_VISION);
    if(Utilities.checkPermissions(player, false, "omegavision.nightvision.particles.bypass", "omegavision.nightvision.admin", "omegavision.admin")) {
      Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100 ,1, false, false, false);
    } else {
      Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100 ,1, particleEffects, ambientEffects, nightvisionIcon);
    }
    NightVisionToggle nightVisionToggle = new NightVisionToggle(pluginInstance, player);
    nightVisionToggle.toggleSoundEffect(player, "Night_Vision_Applied");

  }

  /**
   *
   * Listens for the Player Item Consume Event
   * @see org.bukkit.event.player.PlayerItemConsumeEvent
   *
   * @param playerItemConsumeEvent (The Player Item Consume Event that was triggered)
   */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBucketUse(PlayerItemConsumeEvent playerItemConsumeEvent) {
    final Player player = playerItemConsumeEvent.getPlayer();
    final ItemStack item = playerItemConsumeEvent.getItem();

    // Check if the bucket usage feature was enabled.
    if(!configFile.getBoolean("Night_Vision_Settings.Bucket_Usage")) {
      userDataHandler.setEffectStatus(player.getUniqueId(), false, UserDataHandler.NIGHT_VISION);
      return;
    }

    // Check if the item consumed was a milk bucket
    if(!item.getType().equals(Material.MILK_BUCKET)) {
      userDataHandler.setEffectStatus(player.getUniqueId(), false, UserDataHandler.NIGHT_VISION);
      return;
    }

    // Check if the player currently has night vision
    if(!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
      userDataHandler.setEffectStatus(player.getUniqueId(), false, UserDataHandler.NIGHT_VISION);
      return;
    }

    // Check if the player has permission to use the bucket feature
    if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.bucket", "omegavision.nightvision.admin", "omegavision.admin")) {
      userDataHandler.setEffectStatus(player.getUniqueId(), false, UserDataHandler.NIGHT_VISION);
      return;
    }
    // Set the event to cancelled, so we can change how it's handled
    playerItemConsumeEvent.setCancelled(true);

    // Removes all the potion effects from the player then re-applies night vision without the particle effects
    for(PotionEffect activeEffects : player.getActivePotionEffects()) {
      Utilities.removePotionEffect(player, activeEffects.getType());
    }
    Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1, false, false, false);
    Utilities.message(player, messagesHandler.string("Night_Vision_Messages.Bucket_Message", "#2b9bbfThe particle effects and the icon have been removed!"));

    // Checks if the bucket empty setting has been enabled
    if(!configFile.getBoolean("Night_Vision_Settings.Bucket_Empty")) {
      return;
    }

    // Replace the milk bucket with an empty bucket
    player.getInventory().getItemInMainHand().setType(Material.BUCKET);
  }
}