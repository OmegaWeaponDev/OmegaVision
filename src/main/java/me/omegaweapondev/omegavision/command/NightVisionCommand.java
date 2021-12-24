package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessagesHandler;
import me.omegaweapondev.omegavision.utils.NightVisionToggle;
import me.ou.library.Utilities;
import me.ou.library.builders.TabCompleteBuilder;
import me.ou.library.commands.GlobalCommand;
import me.ou.library.commands.PlayerCommand;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * The class to control the plugins Night Vision command `/nightvision`
 *
 * @author OmegaWeaponDev
 *
 */
public class NightVisionCommand extends GlobalCommand implements TabCompleter {
  private final OmegaVision pluginInstance;
  private final FileConfiguration configFile;
  private final MessagesHandler messagesHandler;

  /**
   *
   * The public constructor for the Night Vision command
   *
   * @param pluginInstance (The plugin's instance)
   */
  public NightVisionCommand(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    configFile = pluginInstance.getStorageManager().getConfigFile().getConfig();
    messagesHandler = pluginInstance.getMessagesHandler();
  }

  /**
   *
   * Handles the execution of the Night Vision Command
   *
   * @param commandSender (The Executor for the command that trying to execute the night vision command)
   * @param strings (The arguments passed into the command)
   */
  @Override
  protected void execute(final CommandSender commandSender, final String[] strings) {

    NightVisionToggle nightVisionToggle = new NightVisionToggle(pluginInstance, commandSender);

    // If there are no args, simply call the night vision toggle method
    if(strings.length == 0) {
      nightVisionToggle.nightVisionToggle();
     return;
    }

    if(strings.length == 1) {
      // If there is 1 arg in the command, check if that player is valid
      Player target = Bukkit.getPlayer(strings[0]);

      if(target == null) {
        return;
      }

      // Call to the toggle others method
      nightVisionToggle.nightVisionToggleOthers(target);
    }

    if(strings.length == 2) {
      // Checks if the first arg in the command is `global`
      if(!strings[0].equalsIgnoreCase("global")) {
        return;
      }

      if(!strings[1].equalsIgnoreCase("add") && !strings[1].equalsIgnoreCase("remove")) {
        if(commandSender instanceof Player player) {
          Utilities.message(player,
            "#2b9bbfNight Vision Global Command: #f63e3e/nightvision global add #2b9bbf- Adds night vision to add online players",
            "#2b9bbfNight Vision Global Command: #f63e3e/nightvision global remove #2b9bbf- Removes night vision from all online players"
          );
        } else {
          Utilities.logWarning(true,
            "Night Vision Global Command: /nightvision global add - Adds night vision to add online players",
            "Night Vision Global Command: /nightvision global remove - Removes night vision from all online players"
          );
        }

      }
      // Call the night vision global method and pass it the second arg in the command
      nightVisionToggle.nightVisionToggleGlobal(strings[1]);
      return;
    }

    if(strings.length == 3) {
      // Checks if the first arg in the command is `temp`
      if(!strings[0].equalsIgnoreCase("temp")) {
        return;
      }
      // Call to the night vision temp method and pass in the second and third args
      nightVisionToggle.nightVisionToggleTemp(Bukkit.getPlayer(strings[1]), Integer.parseInt(strings[2]));
    }
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
    if(strings.length == 0) {
      return Collections.emptyList();
    }

    List<String> onlinePlayers = new ArrayList<>();
    for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      onlinePlayers.add(onlinePlayer.getName());
    }

    if(strings.length == 1) {

      return new TabCompleteBuilder(commandSender)
        .checkCommand("global", true, "omegavision.nightvision.global", "omegavision.nightvision.admin", "omegavision.admin")
        .checkCommand("temp", true, "omegavision.nightvision.temp", "omegavision.nightvision.admin", "omegavision.admin")
        .checkCommand(onlinePlayers, true, "omegavision.nightvision.toggle.others", "omegavision.nightvision.admin", "omegavision.admin")
        .build(strings[0]);
    }

    if(strings.length == 2) {
      if(strings[0].equalsIgnoreCase("temp")) {
        return new TabCompleteBuilder(commandSender)
          .checkCommand(onlinePlayers, true, "omegavision.nightvision.temp", "omegavision.nightvision.admin", "omegavision.admin")
          .build(strings[1]);
      }

      if(strings[0].equalsIgnoreCase("global")) {
        return new TabCompleteBuilder(commandSender)
          .checkCommand("add", true, "omegavision.nightvision.global", "omegavision.nightvision.admin", "omegavision.admin")
          .checkCommand("remove", true, "omegavision.nightvision.global", "omegavision.nightvision.admin", "omegavision.admin")
          .build(strings[1]);
      }
    }

    return Collections.emptyList();
  }
}

