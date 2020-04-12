package me.omegaweapon.omegavision.utils;

import me.omegaweapon.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NightVisionToggle {
  public static Map<UUID, String> playerMap = new HashMap<>();
  public static Map<UUID, Long> nightvisionAppliedTime = new HashMap<>();

  public static void nightVisionEnable(Player player) {

    // Add the nightvision effect to the player
    Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1,
      OmegaVision.getConfigFile().getConfig().getBoolean("Particle_Ambient"),
      OmegaVision.getConfigFile().getConfig().getBoolean("Particle_Effects"),
      OmegaVision.getConfigFile().getConfig().getBoolean("NightVision_Icon")
    );

    // Add the player to the maps
    playerMap.put(player.getUniqueId(), player.getName());
    nightvisionAppliedTime.put(player.getUniqueId(), System.currentTimeMillis());

    // Send the player the nightvision applied message
    Utilities.message(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("NightVision_Applied"));

    // Add the players nightvision status to playerData.yml if they have the login permission
    if (player.hasPermission("omegavision.login")) {

      // Check if they have been added to the file, if not, add them
      if (!OmegaVision.getPlayerData().getConfig().contains(player.getUniqueId().toString())) {
        OmegaVision.getPlayerData().getConfig().createSection(player.getUniqueId().toString());
      }

      OmegaVision.getPlayerData().getConfig().set(player.getUniqueId().toString() + ".NightVision.Enabled", true);
      OmegaVision.getPlayerData().getConfig().set(player.getUniqueId().toString() + ".NightVision.Last Used", System.currentTimeMillis());
      try {
        OmegaVision.getPlayerData().saveConfig();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    // If enabled, send the player an action bar when toggling nightvision
    if (OmegaVision.getConfigFile().getConfig().getBoolean("ActionBar_Messages")) {
      Utilities.sendActionBar(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("ActionBar_NightVision_Applied"));
    }

  }

  public static void nightvisionDisable(Player player) {
    // Remove the nightvision effect from the player
    Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);
    Utilities.message(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("NightVision_Removed"));

    // Trigger the blindness method
    if(OmegaVision.getConfigFile().getConfig().getBoolean("Blindness_Effect.Enabled") && player.isOnline()) {
      NightVisionConditions.nightvisionBlindness(player);
    }


    // Remove the player from the playerMap
    playerMap.remove(player.getUniqueId());

    // Add the players nightvision status to playerData.yml if they have the login permission
    if (player.hasPermission("omegavision.login")) {
      // Check if they have been added to the file, if not, add them
      if (!OmegaVision.getPlayerData().getConfig().contains(player.getUniqueId().toString())) {
        OmegaVision.getPlayerData().getConfig().createSection(player.getUniqueId().toString());
      }
      OmegaVision.getPlayerData().getConfig().set(player.getUniqueId().toString() + ".NightVision.Enabled", false);
      try {
        OmegaVision.getPlayerData().saveConfig();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public static void nightvisionEnableOthers(final Player player, final Player target) {
    nightVisionEnable(target);
    Utilities.message(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("NightVision_Applied"));
  }

  public static void nightvisionDisableOthers(Player player, Player target) {
    nightvisionDisable(target);
    Utilities.message(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("NightVision_Removed"));
  }
}
