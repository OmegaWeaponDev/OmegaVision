package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.builders.TabCompleteBuilder;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LimitCommand extends GlobalCommand implements TabCompleter {
  private final OmegaVision plugin;
  private final MessageHandler messagesHandler;
  private final FileConfiguration playerData;
  private final FileConfiguration configFile;

  public LimitCommand(final OmegaVision plugin) {
    this.plugin = plugin;
    messagesHandler = plugin.getMessageHandler();
    playerData = plugin.getUserData().getPlayerData();
    configFile = plugin.getSettingsHandler().getConfigFile().getConfig();
  }

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
          Utilities.message(player, messagesHandler.string("No_Permission", "#FF4A4ASorry, you do not have permission to use that command."));
          return;
        }

        if(!strings[0].equalsIgnoreCase("check")) {
          helpCommand(sender);
          return;
        }

        Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Check", "Your limit amount currently stands at: %currentLimitAmount% / %maxLimitAmount%")
          .replace("%currentLimitAmount%", String.valueOf(playerData.getInt(player.getUniqueId().toString() + ".Limit")))
          .replace("maxLimitAmount", String.valueOf(configFile.getInt("Night_Vision_Limit.Limit"))));
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
          Utilities.message(player, messagesHandler.string("No_Permission", "#FF4A4ASorry, you do not have permission to use that command."));
          return;
        }

        Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Check_Others", "#FF4A4A%player%'s limit amount currently stands at: %currentLimitAmount% / %maxLimitAmount%")
          .replace("%player%", target.getName())
          .replace("%currentLimitAmount%", String.valueOf(playerData.getInt(player.getUniqueId().toString() + ".Limit")))
          .replace("maxLimitAmount", String.valueOf(configFile.getInt("Night_Vision_Limit.Limit")))
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
      Utilities.logInfo(true, messagesHandler.console("Invalid_Player", "#FF4A4ASorry, that player cannot be found."));
      return;
    }

    if(strings.length == 2) {
      Utilities.logInfo(true, messagesHandler.console("Night_Vision_Limit.Limit_Check_Others", "#FF4A4A&player&'s limit amount currently stands at: %currentLimitAmount% / %maxLimitAmount%")
        .replace("%player%", target.getName())
        .replace("%currentLimitAmount%", String.valueOf(playerData.getInt(target.getUniqueId().toString() + ".Limit")))
        .replace("maxLimitAmount", String.valueOf(configFile.getInt("Night_Vision_Limit.Limit"))));
    }
  }

  private void limitResetCommand(final CommandSender sender, final String[] strings) {
    if(sender instanceof Player) {
      Player player = (Player) sender;
      Player target = Bukkit.getPlayer(strings[1]);

      if(!Utilities.checkPermissions(player, true, "omegavision.admin", "omegavision.limit.all", "omegavision.limit.reset")) {
        Utilities.message(player, messagesHandler.string("No_Permission", "#FF4A4ASorry, you do not have permission to use that command."));
        return;
      }

      if(target == null) {
        Utilities.message(player, messagesHandler.string("Invalid_Player", "#FF4A4ASorry, that player cannot be found."));
        return;
      }

      if(strings.length != 2) {
        helpCommand(sender);
        return;
      }

      playerData.set(target.getUniqueId().toString() + ".Limit", 0);
      plugin.getUserData().saveUserFile();

      Utilities.message(target, messagesHandler.string("Night_Vision_Limit.Limit_Reset", "#00D4FFYour limit's have been reset! You can use the nightvision command again!"));
      return;
    }

    Player target = Bukkit.getPlayer(strings[1]);

    playerData.set(target.getUniqueId().toString() + ".Limit", 0);
    plugin.getUserData().saveUserFile();

    Utilities.message(target, messagesHandler.string("Night_Vision_Limit.Limit_Reset", "&bYour limit's have been reset! You can use the nightvision command again!"));
  }

  private void helpCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      Utilities.message(player,
        messagesHandler.getPrefix() + "#00D4FFLimit Check command: #FF4A4A/nvlimit check & /nvlimit check <player>",
        messagesHandler.getPrefix() + "#00D4FFLimit Reset command: #FF4A4A/nvlimit reset <player>"
      );
      return;
    }

    Utilities.logInfo(true,
      "Limit Check command: /nvlimit check & /nvlimit check <player>",
      "Limit Reset command: /nvlimit reset <player>"
    );
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if(strings.length <= 1) {
      return new TabCompleteBuilder(commandSender)
        .checkCommand("check", true, "omegavision.limit.check", "omegavision.limit.checkothers", "omegavision.limit.all", "omegavision.admin")
        .checkCommand("reset", true, "omegavision.limit.reset", "omegavision.limit.all", "omegavision.admin")
        .build(strings[0]);
    }

    if(strings.length == 2 && strings[0].equalsIgnoreCase("check")) {
      if(!Utilities.checkPermissions(commandSender, true, "omegavision.limit.checkothers", "omegavision.limit.all", "omegavision.admin")) {
        return Collections.emptyList();
      }

      List<String> players = new ArrayList<>();

      for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        players.add(onlinePlayer.getName());
      }
      return new TabCompleteBuilder(commandSender)
        .addCommand(players).build(strings[1]);
    }

    if(strings.length == 2 && strings[0].equalsIgnoreCase("reset")) {
      if(!Utilities.checkPermissions(commandSender, true, "omegavision.limit.reset", "omegavision.limit.all", "omegavision.admin")) {
        return Collections.emptyList();
      }

      List<String> players = new ArrayList<>();

      for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        players.add(onlinePlayer.getName());
      }
      return new TabCompleteBuilder(commandSender)
        .addCommand(players).build(strings[1]);
    }

    return Collections.emptyList();
  }
}
