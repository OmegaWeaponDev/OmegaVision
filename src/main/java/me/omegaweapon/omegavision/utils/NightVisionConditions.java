package me.omegaweapon.omegavision.utils;

import me.omegaweapon.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public class NightVisionConditions {

  public static void nightvisionBlindness(Player player) {

    if(Utilities.checkPermission(player, true, "omegavision.blindness.bypass")) {
      return;
    }

    if(!OmegaVision.getPlayerData().getConfig().isConfigurationSection(player.getUniqueId().toString())) {
      return;
    }

    if(!NightVisionToggle.nightvisionAppliedTime.containsKey(player.getUniqueId())) {
      return;
    }

    long timeRemoved = TimeUnit.MINUTES.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    int configTimer = OmegaVision.getConfigFile().getConfig().getInt("Blindness_Effect.Timer");
    int configDuration = OmegaVision.getConfigFile().getConfig().getInt("Blindness_Effect.Duration");
    long timeApplied = TimeUnit.MINUTES.convert(NightVisionToggle.nightvisionAppliedTime.get(player.getUniqueId()), TimeUnit.MILLISECONDS);

    if((timeRemoved - timeApplied) >= configTimer) {
      Utilities.addPotionEffect(player, PotionEffectType.BLINDNESS, configDuration, 1, true, true, true);
      NightVisionToggle.nightvisionAppliedTime.remove(player.getUniqueId());
      Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.blindnessMessage());
    }
  }

  public static boolean limitChecker(Player player) {
    final boolean configLimitEnabled = OmegaVision.getConfigFile().getConfig().getBoolean("Night_Vision_Limit.Enabled");
    final int configLimitAmount = OmegaVision.getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit");
    int playerLimitAmount = OmegaVision.getPlayerData().getConfig().getInt(player.getUniqueId().toString() + ".Limit");

    if(configLimitEnabled) {
      if(!Utilities.checkPermission(player, true,"omegavision.limit.bypass")) {
        return playerLimitAmount >= configLimitAmount;
      }
    }
    return false;
  }
}
