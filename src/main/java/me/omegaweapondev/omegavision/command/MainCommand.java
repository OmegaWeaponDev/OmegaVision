package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand extends GlobalCommand {
  private final MessageHandler messagesHandler = new MessageHandler(OmegaVision.getInstance().getMessagesFile().getConfig());

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
      Utilities.message(player, messagesHandler.getPrefix() + "&bOmegaVision &cv" + OmegaVision.getInstance().getDescription().getVersion() + " &bBy OmegaWeaponDev");
      return;
    }

	  Utilities.logInfo(true, "&bRunning version: &c" + OmegaVision.getInstance().getDescription().getVersion());
	}

	private void helpCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      Utilities.message(player,
        messagesHandler.getPrefix() + "&bToggle command: &c/nv & /nv <player>",
        messagesHandler.getPrefix() + "&bList command: &c/nvlist",
        messagesHandler.getPrefix() + "&bReload command: &c/omegavision reload"
      );
      return;
    }

    Utilities.logInfo(true,
      "&bToggle command: &c/nv & /nv <player>",
      "&bList command: &c/nvlist",
      "&bReload command: &c/omegavision reload"
    );
	}

	private void reloadCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      if(!Utilities.checkPermissions(player, true, "omegavision.reload", "omegavision.admin")) {
        Utilities.message(player, messagesHandler.string("No_Permission", "&cSorry, you do not have permission to use this command."));
        return;
      }

      OmegaVision.getInstance().onReload();
      Utilities.message(player, messagesHandler.string("Reload_Message", "&cOmegaVision has successfully been reloaded"));
      return;
    }

    OmegaVision.getInstance().onReload();
    Utilities.logInfo(true, messagesHandler.console("Reload_Message", "OmegaVision has successfully been reloaded"));
	}

  private void invalidArgsCommand(final CommandSender sender) {
    if(sender instanceof Player) {
      Player player = (Player) sender;

      Utilities.message(player,
        messagesHandler.getPrefix() + "&bOmegaVision &cv" + OmegaVision.getInstance().getDescription().getVersion() + " &bBy OmegaWeaponDev",
        messagesHandler.getPrefix() + "&c/omegavision help &bto display all the commands"
      );
      return;
    }

    Utilities.logInfo(true,
      "&bRunning version: &c" + OmegaVision.getInstance().getDescription().getVersion(),
      "&c/omegavision help &bto display all the commands"
    );
  }
}