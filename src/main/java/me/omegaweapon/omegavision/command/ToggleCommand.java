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
  private final MessageHandler messageHandler = new MessageHandler(OmegaVision.getInstance().getMessagesFile().getConfig());

  @Override
  protected void execute(final Player player, final String[] strings) {
    final NightVisionToggle nvToggle = new NightVisionToggle(player);
    final NightVisionConditions nvCondiitions = new NightVisionConditions(player);

    if(strings.length == 0) {

      if(!Utilities.checkPermissions(player, true, "omegavision.toggle", "omegavision.*", "omegavision.toggle.*")) {
        Utilities.message(player, messageHandler.string("No_Permission", "&cSorry, you do not have permission to use this command."));
        return;
      }

      if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
        nvToggle.nightvisionDisable();
        return;
      }

      if(!OmegaVision.getInstance().getConfigFile().getConfig().getBoolean("Night_Vision_Limit.Enabled")) {
        nvToggle.nightVisionEnable();
        return;
      }

      if(nvCondiitions.limitChecker()) {
        Utilities.message(player, messageHandler.string("Night_Vision_Limit.Limit_Reached", "&cSorry, you have reached the limit for the nightvision command!"));
        return;
      }

      nvToggle.nightVisionEnable();
      return;
    }

    if(strings.length == 1) {

      if(!Utilities.checkPermissions(player, true, "omegavision.toggle.others", "omegavision.toggle.*", "omegavision.*")) {
        Utilities.message(player, messageHandler.string("No_Permission", "&cSorry, you do not have permission to use this command."));
        return;
      }

      Player target = Bukkit.getPlayer(strings[0]);

      if(target == null) {
        Utilities.message(player, messageHandler.string("Invalid_Player", "&cSorry, but that player does not exist or is offline."));
        return;
      }

      if (!target.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
        nvToggle.nightvisionEnableOthers();
        return;
      }

      nvToggle.nightvisionDisableOthers(target);
    }
  }
}

