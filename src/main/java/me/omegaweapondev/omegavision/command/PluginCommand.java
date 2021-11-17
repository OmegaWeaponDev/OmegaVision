package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessagesHandler;
import me.ou.library.Utilities;
import me.ou.library.builders.TabCompleteBuilder;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 *
 * The main command for the plugin `/omegavision`
 *
 * @author OmegaWeaponDev
 */
public class PluginCommand extends GlobalCommand implements TabCompleter {
  private final OmegaVision pluginInstance;
  private final MessagesHandler messagesHandler;

  /**
   *
   * The public constructor for the main plugin command
   *
   * @param pluginInstance (The plugin's instance)
   */
  public PluginCommand(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    messagesHandler = pluginInstance.getMessagesHandler();
  }

  /**
   *
   * Handles the execution of the main plugin command
   *
   * @param sender (The CommandSender who is trying to execute the command)
   * @param strings (The arguments that were passed into the command)
   */
	@Override
	protected void execute(final CommandSender sender, final String[] strings) {
	  // If no arguments as passed into the command, send the command help message
    if(strings.length != 1) {
      helpCommand(sender);
      return;
    }

    // Call the correct method based on the first arg passed into the command
    switch(strings[0]) {
      case "version" -> versionCommand(sender);
      case "reload" -> reloadCommand(sender);
      case "debug" -> debugCommand(sender);
      default -> helpCommand(sender);
    }
	}

  /**
   *
   * Method to handle the plugin's reload command
   *
   * @param commandSender (The CommandSender who is trying to execute the command)
   */
  private void reloadCommand(final CommandSender commandSender) {
    // Check if the CommandSender is a player
    if(commandSender instanceof final Player player) {

      // Check if the player has permission to reload the plugin
      if(!Utilities.checkPermissions(player, true, "omegavision.reload", "omegavision.admin")) {
        Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
        return;
      }

      // Reload the plugin and send a message to the player telling them the plugin has reloaded.
      pluginInstance.onReload();
      Utilities.message(player, messagesHandler.string("Plugin_Reload", "#f63e3eOmegaVision has successfully been reloaded."));
      return;
    }

    // If the CommandSender is the server console, skip other checks and just reload the plugin
    if(commandSender instanceof ConsoleCommandSender) {
      pluginInstance.onReload();
      Utilities.logInfo(true, "OmegaVision has successfully been reloaded.");
    }
  }

  /**
   *
   * Method to handle the plugin's version command
   *
   * @param sender (The CommandSender who is trying to execute the command)
   */
  private void versionCommand(final CommandSender sender) {
    // Check if the CommandSender is a player
    if(sender instanceof Player) {
      final Player player = (Player) sender;

      // Check if the player has permission to view the plugins version
      if(!Utilities.checkPermission(player, true, "omegavision.admin")) {
        return;
      }

      Utilities.message(player, messagesHandler.getPrefix() + "#86DE0FOmegaVision #CA002Ev" + pluginInstance.getDescription().getVersion() + " #86DE0FBy OmegaWeaponDev");
      return;
    }

    if(sender instanceof ConsoleCommandSender) {
      Utilities.logInfo(true, "OmegaVision v" + pluginInstance.getDescription().getVersion() + " By OmegaWeaponDev");
    }
  }

  /**
   *
   * Method to handle the plugin's help command
   *
   * @param sender (The CommandSender who is trying to execute the command)
   */
  private void helpCommand(final CommandSender sender) {

    if(sender instanceof Player) {
      final Player player = (Player) sender;

      versionCommand(player);
      Utilities.message(player,
        messagesHandler.getPrefix() + "#86DE0FReload Command: #CA002E/omegavision reload",
        messagesHandler.getPrefix() + "#86DE0FVersion Command: #CA002E/omegavision version",
        messagesHandler.getPrefix() + "#86DE0FHelp Command: #CA002E/omegavision help",
        messagesHandler.getPrefix() + "#86DE0FNight Vision Toggle Command: #CA002E/nightvision",
        messagesHandler.getPrefix() + "#86DE0FNight Vision Toggle Others Command: #CA002E/nightvision <player>",
        messagesHandler.getPrefix() + "#86DE0FNight Vision Global Command: #CA002E/nightvision global add|remove",
        messagesHandler.getPrefix() + "#86DE0FNight Vision Temp Command: #CA002E/nightvision <player> <time>",
        messagesHandler.getPrefix() + "#86DE0FNight Vision List Command: #CA002E/nightvisionlist",
        messagesHandler.getPrefix() + "#86DE0FNight Vision Limit Check Command: #CA002E/nightvisionlimit check",
        messagesHandler.getPrefix() + "#86DE0FNight Vision Limit Check Others Command: #CA002E/nightvisionlimit check <player>",
        messagesHandler.getPrefix() + "#86DE0FNight Vision Limit Reset Command: #CA002E/nightvisionlimit reset <player>"
      );
      return;
    }

    if(sender instanceof ConsoleCommandSender) {
      versionCommand(sender);
      Utilities.logInfo(true,
        "Reload Command: /omegavision reload",
        "Version Command: /omegavision version",
        "Help Command: /omegavision help",
        "Night Vision Toggle Others Command: /nightvision <player>",
        "Night Vision Global Command: /nightvision global add|remove",
        "Night Vision Temp Command: /nightvision <player> <time>",
        "Night Vision List Command: /nightvisionlist",
        "Night Vision Limit Check Others Command: /nightvisionlimit check <player>",
        "Night Vision Limit Reset Command: /nightvisionlimit reset <player>"
      );
    }
  }

  /**
   *
   * Method to handle the plugin's debug command
   *
   * @param commandSender (The CommandSender who is trying to execute the command)
   */
  private void debugCommand(final CommandSender commandSender) {
    StringBuilder plugins = new StringBuilder();
    
    if(commandSender instanceof Player) {
      Player player = (Player) commandSender;

      for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
        plugins.append("#ff4a4a").append(plugin.getName()).append(" ").append(plugin.getDescription().getVersion()).append("#14abc9, ");
      }

      Utilities.message(player,
        "#14abc9===========================================",
        " #6928f7OmegaVision #ff4a4av" + pluginInstance.getDescription().getVersion() + " #14abc9By OmegaWeaponDev",
        "#14abc9===========================================",
        " #14abc9Server Brand: #ff4a4a" + Bukkit.getName(),
        " #14abc9Server Version: #ff4a4a" + Bukkit.getServer().getVersion(),
        " #14abc9Online Mode: #ff4a4a" + Bukkit.getOnlineMode(),
        " #14abc9Players Online: #ff4a4a" + Bukkit.getOnlinePlayers().size() + " / " + Bukkit.getMaxPlayers(),
        " #14abc9OmegaVision Commands: #ff4a4a" + Utilities.setCommand().size() + " / 6 #14abc9registered",
        " #14abc9Currently Installed Plugins...",
        " " + plugins,
        "#14abc9==========================================="
      );
      return;
    }

    for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
      plugins.append(plugin.getName()).append(" ").append(plugin.getDescription().getVersion()).append(", ");
    }

    Utilities.logInfo(true,
      "===========================================",
      " OmegaVision v" + pluginInstance.getDescription().getVersion() + " By OmegaWeaponDev",
      "===========================================",
      " Server Brand: " + Bukkit.getName(),
      " Server Version: " + Bukkit.getServer().getVersion(),
      " Online Mode: " + Bukkit.getOnlineMode(),
      " Players Online: " + Bukkit.getOnlinePlayers().size() + " / " + Bukkit.getMaxPlayers(),
      " OmegaVision Commands: " + Utilities.setCommand().size() + " / 4 registered",
      " Currently Installed Plugins...",
      " " + plugins,
      "==========================================="
    );
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
        .checkCommand("version", true, "omegavision.admin")
        .checkCommand("reload", true, "omegavision.reload", "omegavision.admin")
        .checkCommand("debug", true, "omegavision.admin")
        .build(strings[0]);
    }
    return Collections.emptyList();
  }
}