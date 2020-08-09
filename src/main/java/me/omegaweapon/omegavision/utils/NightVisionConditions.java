package me.omegaweapon.omegavision.utils;

import me.omegaweapon.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public class NightVisionConditions {
  private static final FileConfiguration configFile = OmegaVision.getInstance().getConfigFile().getConfig();

  public static void nightvisionBlindness(Player player) {

    if(Utilities.checkPermissions(player, true, "omegavision.blindness.bypass", "omegavision.*")) {
      return;
    }

    if(!OmegaVision.getInstance().getPlayerData().getConfig().isConfigurationSection(player.getUniqueId().toString())) {
      return;
    }

    if(!NightVisionToggle.nightvisionAppliedTime.containsKey(player.getUniqueId())) {
      return;
    }

    long timeRemoved = TimeUnit.MINUTES.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    int configTimer = configFile.getInt("Blindness_Effect.Timer");
    int configDuration = configFile.getInt("Blindness_Effect.Duration");
    long timeApplied = TimeUnit.MINUTES.convert(NightVisionToggle.nightvisionAppliedTime.get(player.getUniqueId()), TimeUnit.MILLISECONDS);

    if((timeRemoved - timeApplied) >= configTimer) {
      Utilities.addPotionEffect(player, PotionEffectType.BLINDNESS, configDuration, 1, true, true, true);
      NightVisionToggle.nightvisionAppliedTime.remove(player.getUniqueId());
      Utilities.message(player, MessageHandler.playerMessage("Blindness_Message", "&cYou have been using nightvision for too long, you are now blind"));
    }

    if(configFile.getBoolean("Sound_Effects.Blindess_Effect.Enabled")) {
      player.playSound(player.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Blindness_Effect.Sound")), 1, 1);
    }
  }

  public static boolean limitChecker(Player player) {
    final boolean configLimitEnabled = configFile.getBoolean("Night_Vision_Limit.Enabled");
    final int configLimitAmount = configFile.getInt("Night_Vision_Limit.Limit");
    int playerLimitAmount = OmegaVision.getInstance().getPlayerData().getConfig().getInt(player.getUniqueId().toString() + ".Limit");

    if(!configLimitEnabled) {
      return false;
    }

    if(!Utilities.checkPermissions(player, true,"omegavision.limit.bypass", "omegavision.*", "omegavision.limit.*")) {
      return playerLimitAmount >= configLimitAmount;
    }

    return false;
  }
}
