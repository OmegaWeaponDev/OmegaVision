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

    if(strings.length == 0) {

      if(!Utilities.checkPermissions(player, true, "omegavision.toggle", "omegavision.*", "omegavision.toggle.*")) {
        Utilities.message(player, MessageHandler.playerMessage("No_Permission", "&cSorry, you do not have permission to use this command."));
        return;
      }

      if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
        NightVisionToggle.nightvisionDisable(player);
        return;
      }

      if(!OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("Night_Vision_Limit.Enabled")) {
        NightVisionToggle.nightVisionEnable(player);
        return;
      }

      if(NightVisionConditions.limitChecker(player)) {
        Utilities.message(player, MessageHandler.playerMessage("Night_Vision_Limit.Limit_Reached", "&cSorry, you have reached the limit for the nightvision command!"));
        return;
      }

      NightVisionToggle.nightVisionEnable(player);
      return;
    }

    if(strings.length == 1) {

      if(!Utilities.checkPermissions(player, true, "omegavision.toggle.others", "omegavision.toggle.*", "omegavision.*")) {
        Utilities.message(player, MessageHandler.playerMessage("No_Permission", "&cSorry, you do not have permission to use this command."));
        return;
      }

      Player target = Bukkit.getPlayer(strings[0]);

      if(target == null) {
        Utilities.message(player, MessageHandler.pluginPrefix() + " &cSorry, but that player does not exist or is offline." );
        return;
      }

      if (!target.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
        NightVisionToggle.nightvisionEnableOthers(player, target);
        return;
      }

      NightVisionToggle.nightvisionDisableOthers(player, target);
    }
  }
}

