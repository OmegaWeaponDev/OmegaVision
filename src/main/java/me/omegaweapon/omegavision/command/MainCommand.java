package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand extends GlobalCommand {

	@Override
	protected void execute(final CommandSender sender, final String[] strings) {
		
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
      Utilities.message(player, MessageHandler.prefix() + "&b Running version: &c" + OmegaVision.getInstance().getDescription().getVersion());
    } else {
      Utilities.logInfo(true, "&bRunning version: &c" + OmegaVision.getInstance().getDescription().getVersion());
    }
	}

	private void helpCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      Utilities.message(player,
        MessageHandler.prefix() + " &bToggle command: &c/nv & /nv <player>",
        MessageHandler.prefix() + " &bList command: &c/nvlist",
        MessageHandler.prefix() + " &bReload command: &c/omegavision reload"
      );
    } else {
      Utilities.logInfo(true,
        "&bToggle command: &c/nv & /nv <player>",
        "&bList command: &c/nvlist",
        "&bReload command: &c/omegavision reload"
      );
    }
	}

	private void reloadCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      if(Utilities.checkPermission(player, true, "omegavision.reload")) {
        OmegaVision.getInstance().onReload();
        Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.reloadMessage());
      } else {
        Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.noPermission());
      }
    } else {
      OmegaVision.getInstance().onReload();
      Utilities.logInfo(true, MessageHandler.reloadMessage());
    }
	}

  private void invalidArgsCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      Utilities.message(player,
        MessageHandler.prefix() + " &bRunning version: &c" + OmegaVision.getInstance().getDescription().getVersion(),
        MessageHandler.prefix() + " &c/omegavision help &bto display all the commands"
      );
    } else {
      Utilities.logInfo(true,
        "&bRunning version: &c" + OmegaVision.getInstance().getDescription().getVersion(),
        "&c/omegavision help &bto display all the commands"
      );
    }
  }
}