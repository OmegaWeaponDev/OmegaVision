package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.utils.MessageHandler;
import me.omegaweapon.omegavision.utils.NightVisionConditions;
import me.omegaweapon.omegavision.utils.NightVisionToggle;
import me.ou.library.Utilities;
import me.ou.library.commands.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ToggleCommand extends PlayerCommand {

  @Override
  protected void execute(final Player player, final String[] strings) {

    if (strings.length == 0) {
      // Make sure the player does not have nightvision enabled
      if(!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
        // If don't have nightvision, then enable nightvision
        if(Utilities.checkPermission(player, true, "omegavision.toggle")) {
          // Check if they have reached the limit or not
          // If limits have been enabled and they don't have bypass permission
          if(OmegaVision.getConfigFile().getConfig().getBoolean("Night_Vision_Limit.Enabled")) {
            if(NightVisionConditions.limitChecker(player)) {
              // Players have reached the limit so they cannot use the command
              Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.limitReached());
              return;
            } else {
              // Players have not reached the limit so they can use the command
              NightVisionToggle.nightVisionEnable(player);
            }
          } else {
            // Limits have not been enabled to skip checking and let them use the command
            NightVisionToggle.nightVisionEnable(player);
          }
        } else {
          Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.noPermission());
        }

      } else {
        // They already have nightvision, so disable it
        if(Utilities.checkPermission(player, true, "omegavision.toggle")) {
          NightVisionToggle.nightvisionDisable(player);
        } else {
          Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.noPermission());
        }
      }

    }

    if (strings.length == 1) {
      // Gets the target player
      Player target = Bukkit.getPlayer(strings[0]);
      // Make sure the target actually exists an the player has permissions to apply nightvision to others
      if (target != null && player.hasPermission("omegavision.toggle.others")) {
        // Check if the target does not already have nightvision applied
        if (!target.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
          // Target does not have nightvision, so enable it
          NightVisionToggle.nightvisionEnableOthers(player, target);
        } else {
          // Target has nightvision, so disable it
          NightVisionToggle.nightvisionDisableOthers(player, target);
        }
      } else if(target == null) {
        Utilities.message(player, MessageHandler.prefix() + " &cSorry, but that player does not exist." );
      } else {
        Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.noPermission());
      }
    }
  }
}

