package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.utils.MessageHandler;
import me.omegaweapon.omegavision.utils.NightVisionConditions;
import me.omegaweapon.omegavision.utils.NightVisionToggle;
import me.ou.library.Utilities;
import me.ou.library.commands.GlobalCommand;
import me.ou.library.commands.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ToggleCommand extends GlobalCommand {
  private final MessageHandler messageHandler = new MessageHandler(OmegaVision.getInstance().getMessagesFile().getConfig());

  @Override
  protected void execute(final CommandSender commandSender, final String[] strings) {

    if(strings.length == 0) {
      if(!(commandSender instanceof Player)) {
        Utilities.logWarning(true, "Only a player can use this command!");
        return;
      }

      final Player player = (Player) commandSender;
      toggleCommand(player);
      return;
    }

    if(strings.length == 2) {
      toggleOthersCommand(commandSender, strings);
    }

  }

  private void toggleCommand(final Player player) {
    final NightVisionToggle nvToggle = new NightVisionToggle(player);
    final NightVisionConditions nvCondiitions = new NightVisionConditions(player);

    if(!Utilities.checkPermissions(player, true, "omegavision.toggle", "omegavision.admin", "omegavision.toggle.all")) {
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
  }

  private void toggleOthersCommand(final CommandSender commandSender, final String[] strings) {

    if(!(commandSender instanceof Player)) {
      final Player target = Bukkit.getPlayer(strings[0]);
      final NightVisionToggle nvToggle = new NightVisionToggle(target);

      if(target == null) {
        Utilities.logWarning(true, messageHandler.string("Invalid_Player", "&cSorry, but that player does not exist or is offline."));
        return;
      }

      if(strings[1].equalsIgnoreCase("on")) {
        nvToggle.nightvisionEnableOthers(target);
        return;
      }

      if (strings[1].equalsIgnoreCase("off")) {
        nvToggle.nightvisionDisableOthers(target);
      }

      return;
    }

    final Player player = (Player) commandSender;
    final NightVisionToggle nvToggle = new NightVisionToggle(player);

    if(!Utilities.checkPermissions(player, true, "omegavision.toggle.others", "omegavision.toggle.all", "omegavision.admin")) {
      Utilities.message(player, messageHandler.string("No_Permission", "&cSorry, you do not have permission to use this command."));
      return;
    }

    Player target = Bukkit.getPlayer(strings[0]);

    if(target == null) {
      Utilities.message(player, messageHandler.string("Invalid_Player", "&cSorry, but that player does not exist or is offline."));
      return;
    }

    if(strings[1].equalsIgnoreCase("on")) {
      nvToggle.nightvisionEnableOthers(target);
      return;
    }

    if (strings[1].equalsIgnoreCase("off")) {
      nvToggle.nightvisionDisableOthers(target);
    }
  }
}

