package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.builders.TabCompleteBuilder;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class PluginCommand extends GlobalCommand implements TabCompleter {
  private final OmegaVision plugin;
  private final MessageHandler messagesHandler;

  public PluginCommand(final OmegaVision plugin) {
    this.plugin = plugin;
    messagesHandler = plugin.getMessageHandler();
  }

	@Override
	protected void execute(final CommandSender sender, final String[] strings) {

	  if(strings.length != 1) {
	    invalidArgsCommand(sender);
	    return;
    }
		
		switch(strings[0]) {
			case "version":
			  versionCommand(sender);
				break;
			case "help":
			  helpCommand(sender);
				break;
			case "reload":
			  reloadCommand(sender);
				break;
			default:
			  invalidArgsCommand(sender);
				break;
		}
	}

	private void versionCommand(final CommandSender sender) {
	  if(sender instanceof Player) {
	    Player player = (Player) sender;
      Utilities.message(player, messagesHandler.getPrefix() + "#00D4FFOmegaVision #FF4A4Av" + plugin.getDescription().getVersion() + " #00D4FFBy OmegaWeaponDev");
      return;
    }

	  Utilities.logInfo(true, "#00D4FFRunning version: #FF4A4A" + plugin.getDescription().getVersion());
	}

	private void helpCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      Utilities.message(player,
        messagesHandler.getPrefix() + "#00D4FFToggle command: #FF4A4A/nv & /nv <player>",
        messagesHandler.getPrefix() + "#00D4FFList command: #FF4A4A/nvlist",
        messagesHandler.getPrefix() + "#00D4FFReload command: #FF4A4A/omegavision reload"
      );
      return;
    }

    Utilities.logInfo(true,
      "#00D4FFToggle command: #FF4A4A/nv & /nv <player>",
      "#00D4FFList command: #FF4A4A/nvlist",
      "#00D4FFReload command: #FF4A4A/omegavision reload"
    );
	}

	private void reloadCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      if(!Utilities.checkPermissions(player, true, "omegavision.reload", "omegavision.admin")) {
        Utilities.message(player, messagesHandler.string("No_Permission", "#FF4A4ASorry, you do not have permission to use this command."));
        return;
      }

      plugin.onReload();
      Utilities.message(player, messagesHandler.string("Reload_Message", "#FF4A4AOmegaVision has successfully been reloaded"));
      return;
    }

    plugin.onReload();
    Utilities.logInfo(true, messagesHandler.console("Reload_Message", "OmegaVision has successfully been reloaded"));
	}

  private void invalidArgsCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      Utilities.message(player,
        messagesHandler.getPrefix() + "#00D4FFOmegaVision #FF4A4Av" + plugin.getDescription().getVersion() + " #00D4FFBy OmegaWeaponDev",
        messagesHandler.getPrefix() + "#FF4A4A/omegavision help #00D4FFto display all the commands"
      );
      return;
    }

    Utilities.logInfo(true,
      "#00D4FFRunning version: #FF4A4A" + plugin.getDescription().getVersion(),
      "#FF4A4A/omegavision help #00D4FFto display all the commands"
    );
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if(strings.length <= 1) {
      return new TabCompleteBuilder(commandSender)
        .checkCommand("help", true, "omegavision.admin")
        .checkCommand("reload", true, "omegavision.reload", "omegavision.admin")
        .checkCommand("version", true, "omegavision.admin")
        .build(strings[0]);
    }

    return Collections.emptyList();
  }
}