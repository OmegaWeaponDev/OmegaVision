package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessagesHandler;
import me.omegaweapondev.omegavision.utils.UserDataHandler;
import me.ou.library.Utilities;
import me.ou.library.builders.TabCompleteBuilder;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * The class to control the plugins Limit command `/nightvisionlimit`
 *
 * @author OmegaWeaponDev
 *
 */
public class LimitCommand extends GlobalCommand implements TabCompleter {
  private final OmegaVision pluginInstance;
  private final MessagesHandler messagesHandler;
  private final FileConfiguration configFile;
  private final UserDataHandler userDataHandler;

  /**
   *
   * Public constructor for the class
   *
   * @param pluginInstance (The plugin's instance)
   */
  public LimitCommand(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    messagesHandler = pluginInstance.getMessagesHandler();
    configFile = pluginInstance.getStorageManager().getConfigFile().getConfig();
    userDataHandler = pluginInstance.getUserDataHandler();
  }

  /**
   *
   * Handles the execution of the Limit command
   *
   * @param sender (The CommandSender)
   * @param strings (The args passed into the command)
   */
  @Override
  protected void execute(CommandSender sender, String[] strings) {
    // Checks if the commandSender is a Player
    if(sender instanceof Player) {
      final Player player = ((Player) sender).getPlayer();

      // Checks if the first arg for the command is `check`
      if(strings.length == 1 && strings[0].equalsIgnoreCase("check")) {
        // Check to see if the player has permission to use the limit check command
        if(checkPermission(player, "check")) {
          return;
        }

        // Send the player a message with their current limit status
        Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Check", "#1fe3e0Your limit amount currently stands at: #f63e3e%currentLimitAmount% / %maxLimitAmount%")
          .replace("%currentLimitAmount%", String.valueOf(userDataHandler.getEffectStatus(player.getUniqueId(), UserDataHandler.LIMIT)))
          .replace("%maxLimitAmount%", String.valueOf(configFile.getInt("Night_Vision_Limit.Limit")))
        );
        return;
      }

      // Checks if the first arg for the command is `check` and if there is a second arg
      if(strings.length == 2 && strings[0].equalsIgnoreCase("check")) {
        // Checks if the player has permission to use the check others limit command
        if(checkPermission(player, "check.others")) {
          return;
        }

        // Gets the target from the second arg in the command
        Player target = Bukkit.getPlayer(strings[1]);

        // Check if the player is not null
        if(target != null) {
          // Send the player a message to inform them of the targets limit status
          Utilities.message(player, messagesHandler.string("Night_Vision_Limit.Limit_Check_Others", "#f63e3e%player%'s #1fe3e0limit amount currently stands at: #f63e3e%currentLimitAmount% / %maxLimitAmount%")
            .replace("%player%", target.getDisplayName())
            .replace("%currentLimitAmount%", String.valueOf(userDataHandler.getEffectStatus(target.getUniqueId(), UserDataHandler.LIMIT)))
            .replace("%maxLimitAmount%", String.valueOf(configFile.getInt("Night_Vision_Limit.Limit")))
          );
          return;
        }
        // If null, Send a message to the player informing them that the target is an invalid player
        Utilities.message(player, messagesHandler.string("Invalid_Player", "#f63e3eSorry, that player cannot be found."));
        return;
      }

      // Checks if the first arg passed into the command is `reset` and there is a second arg
      if(strings.length == 2 && strings[0].equalsIgnoreCase("reset")) {
        // Checks if the player has permission to use the limit reset command
        if(checkPermission(player, "reset")) {
          return;
        }

        // Gets the target from the second arg in the command
        Player target = Bukkit.getPlayer(strings[1]);
        if(target != null) {
          // Resets the players limit status back to 0
          userDataHandler.setEffectStatus(target.getUniqueId(), 0, UserDataHandler.LIMIT);
          Utilities.message(target, messagesHandler.string("Night_Vision_Limit.Limit_Reset", "#1fe3e0Your limit's have been reset! You can use the night vision command again!"));

          if(configFile.getBoolean("Sound_Effects.Enabled") && configFile.getBoolean("Sound_Effects.Limit_Reset.Enabled")) {
            target.playSound(target.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Limit_Reset.Sound")), 1, 1);
          }
          return;
        }
      }

      return;
    }

    // Same as above, just handles the command if the CommandSender is the server's console
    if(strings.length == 2 && strings[0].equalsIgnoreCase("check")) {

      Player target = Bukkit.getPlayer(strings[1]);
      if(target != null) {
        Utilities.logInfo(true, messagesHandler.console("Night_Vision_Limit.Limit_Check_Others", "#f63e3e%player%'s #1fe3e0limit amount currently stands at: #f63e3e%currentLimitAmount% / %maxLimitAmount%")
          .replace("%player%", target.getDisplayName())
          .replace("%currentLimitAmount%", String.valueOf(userDataHandler.getEffectStatus(target.getUniqueId(), UserDataHandler.LIMIT)))
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
        userDataHandler.setEffectStatus(target.getUniqueId(), 0, UserDataHandler.LIMIT);
        Utilities.message(target, messagesHandler.string("Night_Vision_Limit.Limit_Reset", "#1fe3e0Your limit's have been reset! You can use the night vision command again!"));
      }

      if(configFile.getBoolean("Sound_Effects.Enabled") && configFile.getBoolean("Sound_Effects.Limit_Reset.Enabled")) {
        target.playSound(target.getLocation(), Sound.valueOf(configFile.getString("Sound_Effects.Limit_Reset.Sound")), 1, 1);
      }
    }
  }

  /**
   *
   * Handles checking if the player has a specific permission relating to the limit command
   *
   * @param player (The player to check for permission)
   * @param perm (The permission that needs to be checked)
   * @return (True/False depending on if the player has permission or not.)
   */
  private boolean checkPermission(@NotNull final Player player, @NotNull final String perm) {
    if (Utilities.checkPermissions(player, true, "omegavision.limit." + perm, "omegavision.limit.admin", "omegavision.admin")) {
      return true;
    }

    Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
    return false;
  }

  /**
   *
   * Sets up the command tab completion based on player's permissions
   *
   * @param commandSender (Who sent the command)
   * @param command (The argument to add into the tab completion list)
   * @param strings (The command arguments)
   * @return (The completed tab completion list)
   */
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
