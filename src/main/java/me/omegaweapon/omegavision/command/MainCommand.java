package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.PlayerCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MainCommand extends PlayerCommand {

	@Override
	protected void onCommand(final Player player, final String[] strings) {
		
		if (strings.length == 0) {
			Utilities.message(player, MessageHandler.prefix() + " Running version: &c" + OmegaVision.getInstance().getDescription().getVersion());
		}
		
		if (strings.length == 1) {
			switch(strings[0]) {
				case "reload":
					if(Utilities.checkPermission(player, "omegavision.reload", true)) {
						OmegaVision.getInstance().onReload();
						Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.reloadMessage());
					} else {
						Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.noPermission());
					}
					break;
				case "version":
					Utilities.message(player, MessageHandler.prefix() + " Currently running version: &c" + OmegaVision.getInstance().getDescription().getVersion());
					break;
				case "help":
					Utilities.message(player,
						MessageHandler.prefix() + "Toggle command: &c/nv toggle & /nv toggle <player>",
						MessageHandler.prefix() + "List command: &c/nvlist",
						MessageHandler.prefix() + "Reload command: &c/omegavision reload"
					);
					break;
			}
		}
	}
}