package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessagesHandler;
import me.omegaweapondev.omegavision.utils.UserDataHandler;
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
  private final OmegaVision pluginInstance;
  private final MessagesHandler messagesHandler;
  private final FileConfiguration configFile;
  private final UserDataHandler userDataHandler;

  public LimitCommand(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    messagesHandler = pluginInstance.getMessagesHandler();
    configFile = pluginInstance.getStorageManager().getConfigFile().getConfig();
    userDataHandler = pluginInstance.getUserDataHandler();
  }

  @Override
  protected void execute(CommandSender sender, String[] strings) {
    if(sender instanceof Player) {
      final Player player = ((Player) sender).getPlayer();

      if(strings.length == 1 && strings[0].equalsIgnoreCase("check")) {
        if(checkPermission(player, "check")) {
          return;
        }

        Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Check", "#1fe3e0Your limit amount currently stands at: #f63e3e%currentLimitAmount% / %maxLimitAmount%")
          .replace("%currentLimitAmount%", String.valueOf(userDataHandler.getLimitStatus(player.getUniqueId())))
          .replace("%maxLimitAmount%", String.valueOf(configFile.getInt("Night_Vision_Limit.Limit")))
        );
        return;
      }

      if(strings.length == 2 && strings[0].equalsIgnoreCase("check")) {
        if(checkPermission(player, "check.others")) {
          return;
        }

        Player target = Bukkit.getPlayer(strings[1]);
        if(target != null) {
          Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Check_Others", "#f63e3e%player%'s #1fe3e0limit amount currently stands at: #f63e3e%currentLimitAmount% / %maxLimitAmount%")
            .replace("%player%", target.getDisplayName())
            .replace("%currentLimitAmount%", String.valueOf(userDataHandler.getLimitStatus(target.getUniqueId())))
            .replace("%maxLimitAmount%", String.valueOf(configFile.getInt("Night_Vision_Limit.Limit")))
          );
          return;
        }
        Utilities.message(player, messagesHandler.string("Invalid_Player", "#f63e3eSorry, that player cannot be found."));
        return;
      }

      if(strings.length == 2 && strings[0].equalsIgnoreCase("reset")) {
        if(checkPermission(player, "reset")) {
          return;
        }

        Player target = Bukkit.getPlayer(strings[1]);
        if(target != null) {
          userDataHandler.setLimitStatus(target.getUniqueId(), 0);
          Utilities.message(target, messagesHandler.string("Night_Vision_Limit.Limit_Reset", "#1fe3e0Your limit's have been reset! You can use the night vision command again!"));

          if(configFile.getBoolean("Sound_Effects.Enabled") && configFile.getBoolean("Sound_Effects.Limit_Reset.Enabled")) {
            target.playSound(target.getLocation(), configFile.getString("Sound_Effects.Limit_Reset.Sound"), 1, 1);
          }
          return;
        }
      }

      return;
    }

    if(strings.length == 2 && strings[0].equalsIgnoreCase("check")) {


      Player target = Bukkit.getPlayer(strings[1]);
      if(target != null) {
        Utilities.logInfo(true, messagesHandler.console("Night_Vision_Limit.Limit_Check_Others", "#f63e3e%player%'s #1fe3e0limit amount currently stands at: #f63e3e%currentLimitAmount% / %maxLimitAmount%")
          .replace("%player%", target.getDisplayName())
          .replace("%currentLimitAmount%", String.valueOf(userDataHandler.getLimitStatus(target.getUniqueId())))
          .replace("%maxLimitAmount%", String.valueOf(configFile.getInt("Night_Vision_Limit.Limit")))
        );
        return;
      }
      Utilities.logInfo(true, messagesHandler.console("Invalid_Player", "#f63e3eSorry, that player cannot be found."));
      return;
    }

    if(strings.length == 2 && strings[0].equalsIgnoreCase("reset")) {
      Player target = Bukkit.getPlayer(strings[1]);
      if(target != null) {
        userDataHandler.setLimitStatus(target.getUniqueId(), 0);
        Utilities.message(target, messagesHandler.string("Night_Vision_Limit.Limit_Reset", "#1fe3e0Your limit's have been reset! You can use the night vision command again!"));
      }

      if(configFile.getBoolean("Sound_Effects.Enabled") && configFile.getBoolean("Sound_Effects.Limit_Reset.Enabled")) {
        target.playSound(target.getLocation(), configFile.getString("Sound_Effects.Limit_Reset.Sound"), 1, 1);
      }
    }
  }

  private boolean checkPermission(@NotNull final Player player, @NotNull final String perm) {
    if (Utilities.checkPermissions(player, true, "omegavision.limit." + perm, "omegavision.limit.admin", "omegavision.admin")) {
      return true;
    }

    Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
    return false;
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if(strings.length <= 1) {
      return new TabCompleteBuilder(commandSender)
        .checkCommand("check", true, "omegavision.limit.check", "omegavision.limit.admin", "omegavision.admin")
        .checkCommand("reset", true, "omegavision.limit.reset", "omegavision.limit.admin", "omegavision.admin")
        .build(strings[0]);
    }

    if(strings.length == 2) {
      List<String> onlinePlayers = new ArrayList<>();
      for(Player onlineplayer : Bukkit.getOnlinePlayers()) {
        onlinePlayers.add(onlineplayer.getName());
      }

      return new TabCompleteBuilder(commandSender).addCommand(onlinePlayers).build(strings[1]);
    }
    return Collections.emptyList();
  }
}
