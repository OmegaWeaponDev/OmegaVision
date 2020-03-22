package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.ou.library.Utilities;
import me.ou.library.commands.PlayerCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MainCommand extends PlayerCommand {
	private String prefix = OmegaVision.getMessagesFile().getConfig().getString("Prefix");
	
	public MainCommand() {
		super("omegavision");
		
		// Set the permission and permission message
		setPermission("omegavision.admin");
		setPermissionMessage(Utilities.colourise(prefix + " " + OmegaVision.getConfigFile().getConfig().getString("No_Permission")));
		
		// Set the description message
		setDescription("The main commands for the OmegaVision plugin");
		
		// Set the command aliases
		setAliases(Arrays.asList(
			"ov",
			"ovision"
		));
	}
	
	@Override
	protected void onCommand(final Player player, final String[] strings) {
		
		if (strings.length == 0) {
			Utilities.message(player, prefix + "Running version: &c" + OmegaVision.getInstance().getDescription().getVersion());
		}
		
		if (strings.length == 1) {
			switch(strings[0]) {
				case "reload":
					OmegaVision.getInstance().onReload();
					Utilities.message(player, prefix + " " + OmegaVision.getMessagesFile().getConfig().getString("Reload_Message"));
					break;
				case "version":
					Utilities.message(player, prefix + " Currently running version: &c" + OmegaVision.getInstance().getDescription().getVersion());
					break;
				case "help":
					Utilities.message(player,
						prefix + "Toggle command: &c/nv toggle & /nv toggle <player>",
						prefix + "List command: &c/nvlist",
						prefix + "Reload command: &c/omegavision reload"
					);
					break;
			}
		}
	}
}