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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ToggleCommand extends PlayerCommand {

  @Override
  protected void onCommand(final Player player, final String[] strings) {

    if (strings.length == 0) {
      Utilities.message(player, MessageHandler.prefix() + " &bToggle Command: &c/nv toggle");
      Utilities.message(player, MessageHandler.prefix() + " &bToggle Other Command: &c/nv toggle <playername>");
      return;
    }

    if (strings.length == 1) {
      // Makes sure they are using the toggle argument
      if (strings[0].equalsIgnoreCase("toggle")) {
        // Make sure the player does not have nightvision enabled
        if(!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
          // If don't have nightvision, then enable nightvision
          if(Utilities.checkPermission(player, "omegavision.toggle", true)) {
            NightVisionToggle.nightVisionEnable(player);
          } else {
            Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.noPermission());
          }

        } else {
          // They already have nightvision, so disable it
          if(Utilities.checkPermission(player, "omegavision.toggle", true)) {
            NightVisionToggle.nightvisionDisable(player);
          } else {
            Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.noPermission());
          }
        }
      }
    }

    if (strings.length == 2) {
      // Makes sure they are using the toggle argument
      if (strings[0].equalsIgnoreCase("toggle")) {
        // Gets the target player
        Player target = Bukkit.getPlayer(strings[1]);
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
}

