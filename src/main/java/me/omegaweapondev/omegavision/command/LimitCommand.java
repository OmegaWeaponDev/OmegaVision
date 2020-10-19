package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LimitCommand extends GlobalCommand {
  private final MessageHandler messagesHandler = new MessageHandler(OmegaVision.getInstance().getMessagesFile().getConfig());
  private final FileConfiguration playerData = OmegaVision.getInstance().getPlayerData().getConfig();

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

        if(!Utilities.checkPermissions(player, true, "omegavision.limit.check", "omegavision.limit.all", "omegavision.admin")) {
          Utilities.message(player, messagesHandler.string("No_Permission", "&cSorry, you do not have permission to use that command."));
          return;
        }

        if(!strings[0].equalsIgnoreCase("check")) {
          helpCommand(sender);
          return;
        }

        Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Check", "Your limit amount currently stands at: %currentLimitAmount% / %maxLimitAmount%")
          .replace("%currentLimitAmount%", String.valueOf(playerData.getInt(player.getUniqueId().toString() + ".Limit")))
          .replace("maxLimitAmount", String.valueOf(OmegaVision.getInstance().getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit"))));
        return;
      }

      if(strings.length == 2) {
        Player target = Bukkit.getPlayer(strings[1]);

        if(!strings[0].equalsIgnoreCase("check")) {
          helpCommand(sender);
          return;
        }

        if(target == null) {
          Utilities.message(player, messagesHandler.string("Invalid_Player", "&cSorry, that player cannot be found."));
          return;
        }

        if(!Utilities.checkPermissions(player, true, "omegavision.limit.checkothers", "omegavision.limit.all", "omegavision.admin")) {
          Utilities.message(player, messagesHandler.string("No_Permission", "&cSorry, you do not have permission to use that command."));
          return;
        }

        Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Check_Others", "&c&player&'s limit amount currently stands at: %currentLimitAmount% / %maxLimitAmount%")
          .replace("%player%", target.getName())
          .replace("%currentLimitAmount%", String.valueOf(playerData.getInt(player.getUniqueId().toString() + ".Limit")))
          .replace("maxLimitAmount", String.valueOf(OmegaVision.getInstance().getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit")))
        );
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
      Utilities.logInfo(true, messagesHandler.console("Invalid_Player", "&cSorry, that player cannot be found."));
      return;
    }

    if(strings.length == 2) {
      Utilities.logInfo(true, messagesHandler.console("Night_Vision_Limit.Limit_Check_Others", "&c&player&'s limit amount currently stands at: %currentLimitAmount% / %maxLimitAmount%")
        .replace("%player%", target.getName())
        .replace("%currentLimitAmount%", String.valueOf(playerData.getInt(target.getUniqueId().toString() + ".Limit")))
        .replace("maxLimitAmount", String.valueOf(OmegaVision.getInstance().getConfigFile().getConfig().getInt("Night_Vision_Limit.Limit"))));
    }
  }

  private void limitResetCommand(final CommandSender sender, final String[] strings) {
    if(sender instanceof Player) {
      Player player = (Player) sender;
      Player target = Bukkit.getPlayer(strings[1]);

      if(!Utilities.checkPermissions(player, true, "omegavision.admin", "omegavision.limit.all", "omegavision.limit.reset")) {
        Utilities.message(player, messagesHandler.string("No_Permission", "&cSorry, you do not have permission to use that command."));
        return;
      }

      if(target == null) {
        Utilities.message(player, messagesHandler.string("Invalid_Player", "&cSorry, that player cannot be found."));
        return;
      }

      if(strings.length != 2) {
        helpCommand(sender);
        return;
      }

      OmegaVision.getInstance().getPlayerData().getConfig().set(target.getUniqueId().toString() + ".Limit", 0);
      OmegaVision.getInstance().getPlayerData().saveConfig();

      Utilities.message(target, messagesHandler.string("Night_Vision_Limit.Limit_Reset", "&bYour limit's have been reset! You can use the nightvision command again!"));
      return;
    }

    Player target = Bukkit.getPlayer(strings[1]);

    OmegaVision.getInstance().getPlayerData().getConfig().set(target.getUniqueId().toString() + ".Limit", 0);
    OmegaVision.getInstance().getPlayerData().saveConfig();

    Utilities.message(target, messagesHandler.string("Night_Vision_Limit.Limit_Reset", "&bYour limit's have been reset! You can use the nightvision command again!"));
  }

  private void helpCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      Utilities.message(player,
        messagesHandler.getPrefix() + "&bLimit Check command: &c/nvlimit check & /nvlimit check <player>",
        messagesHandler.getPrefix() + "&bLimit Reset command: &c/nvlimit reset <player>"
      );
      return;
    }

    Utilities.logInfo(true,
      "Limit Check command: /nvlimit check & /nvlimit check <player>",
      "Limit Reset command: /nvlimit reset <player>"
    );
  }
}
