package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class LimitCommand extends GlobalCommand {

  @Override
  protected void execute(CommandSender commandSender, String[] strings) {
    if(commandSender instanceof Player) {
      Player player = (Player) commandSender;

      if(strings.length == 0) {
        helpCommand(player);
      }

      if(strings.length == 1) {
        switch(strings[0]) {
          case "check":
            limitCheckCommand(player, strings);
            break;
          case "reset":
            limitResetCommand(player, strings);
            break;
          default:
            helpCommand(player);
            break;
        }
      }
    }

    if(commandSender instanceof ConsoleCommandSender) {
      if(strings.length == 0) {
        helpCommand(commandSender);
      }

      if(strings.length == 1) {
        switch(strings[0]) {
          case "check":
            limitCheckCommand(commandSender, strings);
            break;
          case "reset":
            limitResetCommand(commandSender, strings);
            break;
          default:
            helpCommand(commandSender);
            break;
        }
      }
    }
  }

  private void limitCheckCommand(final CommandSender sender, final String[] strings) {
    if(sender instanceof Player) {
      Player player = (Player) sender;


      if(strings.length == 1) {
        if(Utilities.checkPermission(player, true, "omegavision.limit.check")) {
          Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.limitCheck(player));
        }
      }

      if(strings.length == 2) {
        Player target = Bukkit.getPlayer(strings[1]);

        if(target != null && Utilities.checkPermission(player, true, "omegavision.limit.checkothers")) {

          Utilities.message(player, MessageHandler.limitCheckOther(target));
        } else if(!player.hasPermission("omegavision.limit.checkothers")) {
          Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.noPermission());
        } else if(target == null) {
          Utilities.message(player, MessageHandler.prefix() + "&cSorry, that player does not exist or is offline!");
        }
      }

    } else {
      Player target = Bukkit.getPlayer(strings[1]);

      if(target != null && strings.length == 2) {
        Utilities.logInfo(true, MessageHandler.limitCheckOther(target));
      } else {
        Utilities.logInfo(true, MessageHandler.prefix() + "&cSorry, that player does not exist or is offline!");
      }
    }
  }

  private void limitResetCommand(final CommandSender sender, final String[] strings) {
    if(sender instanceof Player) {
      Player player = (Player) sender;
      Player target = Bukkit.getPlayer(strings[1]);

      if(Utilities.checkPermission(player, true, "omegavision.limit.reset")) {
        if(strings.length == 2 && target != null) {
          OmegaVision.getPlayerData().getConfig().set(target.getUniqueId().toString() + ".Limit", 0);
          OmegaVision.getPlayerData().saveConfig();

          Utilities.message(player, MessageHandler.prefix() + MessageHandler.limitResetOthers(target));
        } else if(target == null) {
          Utilities.message(player, MessageHandler.prefix() + "&cSorry, that player does not exist or is offline!");
        }
      }
    } else {
      Player target = Bukkit.getPlayer(strings[1]);

      if(strings.length == 2 && target != null) {
        OmegaVision.getPlayerData().getConfig().set(target.getUniqueId().toString() + ".Limit", 0);
        OmegaVision.getPlayerData().saveConfig();

        Utilities.logInfo(true, MessageHandler.limitResetOthers(target));
      } else if(target == null ) {
        Utilities.logInfo(true, MessageHandler.prefix() + "&cSorry, that player does not exist or is offline!");
      }
    }
  }

  private void helpCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      Utilities.message(player,
        MessageHandler.prefix() + " &bLimit Check command: &c/nvlimit check & /nvlimit check <player>",
        MessageHandler.prefix() + " &bLimit Reset command: &c/nvlimit reset <player>"
      );
    } else {
      Utilities.logInfo(true,
        "&bLimit Check command: &c/nvlimit check & /nvlimit check <player>",
        "&bLimit Reset command: &c/nvlimit reset <player>"
      );
    }
  }
}
