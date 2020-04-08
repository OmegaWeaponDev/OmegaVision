package me.omegaweapon.omegavision.utils;

import me.omegaweapon.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public class NightVisionConditions {

  public static void nightvisionBlindness(Player player) {

    if(OmegaVision.getPlayerData().getConfig().isSet(player.getUniqueId().toString())) {
      if(!player.hasPermission("omegavision.blindness.bypass")) {
        long timeRemoved = TimeUnit.MINUTES.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        int configTimer = OmegaVision.getConfigFile().getConfig().getInt("Blindness_Effect.Timer");
        int configDuration = OmegaVision.getConfigFile().getConfig().getInt("Blindness_Effect.Duration");
        long timeApplied = TimeUnit.MINUTES.convert(NightVisionToggle.nightvisionAppliedTime.get(player.getUniqueId()), TimeUnit.MILLISECONDS);

        if((timeRemoved - timeApplied) >= configTimer) {
          Utilities.addPotionEffect(player, PotionEffectType.BLINDNESS, configDuration, 1, true, true, true);
          NightVisionToggle.nightvisionAppliedTime.remove(player.getUniqueId());
          Utilities.message(player, OmegaVision.getMessagesFile().getConfig().getString("Prefix") + " " + OmegaVision.getMessagesFile().getConfig().getString("Blindness_Message"));
        }
      }
    }
  }
}
