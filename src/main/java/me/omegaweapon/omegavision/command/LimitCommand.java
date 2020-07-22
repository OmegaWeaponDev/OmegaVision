package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LimitCommand extends GlobalCommand {

  @Override
  protected void execute(CommandSender sender, String[] strings) {

    if(strings.length == 0) {
      helpCommand(sender);
      return;
    }

    switch(strings[0]) {
      case "check":
        limitCheckCommand(sender, strings);
        break;
      case "reset":
        if(strings.length != 2) {
          break;
        }
        limitResetCommand(sender, strings);
        break;
      default:
        helpCommand(sender);
        break;
    }

  }

  private void limitCheckCommand(final CommandSender sender, final String[] strings) {
    if(sender instanceof Player) {
      Player player = (Player) sender;


      if(strings.length == 1) {

        if(!Utilities.checkPermissions(player, true, "omegavision.limit.check", "omegavision.limit.*", "omegavision.*")) {
          Utilities.message(player, MessageHandler.playerMessage("No_Permission", "&cSorry, you do not have permission to use that command."));
          return;
        }

        if(!strings[0].equalsIgnoreCase("check")) {
          helpCommand(sender);
          return;
        }

        Utilities.message(player, MessageHandler.limitCheck(player));
        return;
      }

      if(strings.length == 2) {
        Player target = Bukkit.getPlayer(strings[1]);

        if(!strings[0].equalsIgnoreCase("check")) {
          helpCommand(sender);
          return;
        }

        if(target == null) {
          Utilities.message(player, MessageHandler.pluginPrefix() + " &cSorry, that player does not exist.");
          return;
        }

        if(!Utilities.checkPermissions(player, true, "omegavision.limit.checkothers", "omegavision.limit.*", "omegavision.*")) {
          Utilities.message(player, MessageHandler.playerMessage("No_Permission", "&cSorry, you do not have permission to use that command."));
          return;
        }

        Utilities.message(player, MessageHandler.limitCheckOther(target));
        return;
      }
      return;
    }

    Player target = Bukkit.getPlayer(strings[1]);

    if(!strings[0].equalsIgnoreCase("check")) {
      helpCommand(sender);
      return;
    }

    if(target == null) {
      Utilities.logInfo(true, "Sorry, that player does not exist.");
      return;
    }

    if(strings.length == 2) {
      Utilities.logInfo(true, MessageHandler.limitCheckOther(target));
    }
  }

  private void limitResetCommand(final CommandSender sender, final String[] strings) {
    if(sender instanceof Player) {
      Player player = (Player) sender;
      Player target = Bukkit.getPlayer(strings[1]);

      if(!Utilities.checkPermissions(player, true, "omegavision.*", "omegavision.limit.*", "omegavision.limit.reset")) {
        Utilities.message(player, MessageHandler.playerMessage("No_Permission", "&cSorry, you do not have permission to use that command."));
        return;
      }

      if(target == null) {
        Utilities.message(player, MessageHandler.pluginPrefix() + "&cSorry, that user does not exist.");
        return;
      }

      if(strings.length != 2) {
        helpCommand(sender);
        return;
      }

      OmegaVision.getInstance().getPlayerData().getConfig().set(target.getUniqueId().toString() + ".Limit", 0);
      OmegaVision.getInstance().getPlayerData().saveConfig();

      Utilities.message(target, MessageHandler.playerMessage("Night_Vision_Limit.Limit_Reset", "&bYour limit's have been reset! You can use the nightvision command again!"));
      Utilities.message(player, MessageHandler.pluginPrefix() + " " + MessageHandler.limitResetOthers(target));
      return;
    }

    Player target = Bukkit.getPlayer(strings[1]);

    OmegaVision.getInstance().getPlayerData().getConfig().set(target.getUniqueId().toString() + ".Limit", 0);
    OmegaVision.getInstance().getPlayerData().saveConfig();

    Utilities.message(target, MessageHandler.playerMessage("Night_Vision_Limit.Limit_Reset", "&bYour limit's have been reset! You can use the nightvision command again!"));
    Utilities.logInfo(true, MessageHandler.limitResetOthers(target));
  }

  private void helpCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      Utilities.message(player,
        MessageHandler.pluginPrefix() + " &bLimit Check command: &c/nvlimit check & /nvlimit check <player>",
        MessageHandler.pluginPrefix() + " &bLimit Reset command: &c/nvlimit reset <player>"
      );
      return;
    }

    Utilities.logInfo(true,
      "Limit Check command: /nvlimit check & /nvlimit check <player>",
      "Limit Reset command: /nvlimit reset <player>"
    );
  }
}
