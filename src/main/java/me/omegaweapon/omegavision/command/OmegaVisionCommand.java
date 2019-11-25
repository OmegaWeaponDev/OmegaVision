package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.settings.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OmegaVisionCommand implements CommandExecutor {
	OmegaVision plugin;

	public OmegaVisionCommand(OmegaVision pl) {
		this.plugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		// Setting the variables
		Boolean nightVisionLogin = plugin.getConfig().getBoolean("Night_Vision_Login");
		Boolean particles = plugin.getConfig().getBoolean("Particle_Effects");
		Boolean ambient = plugin.getConfig().getBoolean("Particle_Ambient");
		Boolean nightVisionIcon = plugin.getConfig().getBoolean("NightVision_Icon");

		String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Prefix"));
		String noPermission = ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("No_Permission"));
		String reloadCommand = ChatColor.translateAlternateColorCodes('&',plugin.getMessagesConfig().getString("Reload_Message"));
		String nightvisionApplied = ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("NightVision_Applied"));
		String nightvisionRemoved = ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("NightVision_Removed"));

		if(commandSender instanceof Player) {
			if(command.getName().equalsIgnoreCase("omegavision")) {
				Player player = (Player) commandSender;
				if(args.length != 1) {
					player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', "&b /ausvision toggle - Toggle your nightvision on or off"));
					player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', "&b /ausvision reload - Reload the plugin"));
					return true;
				} else if (args[0].equalsIgnoreCase("toggle")) {
					if(!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, ambient, particles, nightVisionIcon));
						player.sendMessage(prefix + " " + nightvisionApplied);
						if(player.hasPermission("omegavision.login") && nightVisionLogin.equals(true)) {
							PlayerData.getPlayerData().createSection(player.getUniqueId().toString());
							PlayerData.getPlayerData().set(player.getUniqueId().toString() + "." + "NightVision", true);
							PlayerData.savePlayerData();
						}
						return true;
					} else {
						player.removePotionEffect(PotionEffectType.NIGHT_VISION);
						player.sendMessage(prefix + " " + nightvisionRemoved);
						if(nightVisionLogin.equals(true) && player.hasPermission("omegavision.login")) {
							PlayerData.getPlayerData().createSection(player.getUniqueId().toString());
							PlayerData.getPlayerData().set(player.getUniqueId().toString() + "." + "NightVision", false);
							PlayerData.savePlayerData();
						}
						return true;
					}
				} else if(args[0].equalsIgnoreCase("reload")) {
					plugin.reloadConfig();
					PlayerData.reloadPlayerData();
					player.sendMessage(prefix + " " + reloadCommand);
					return true;
				}
			} else {
				commandSender.sendMessage(prefix + " " + noPermission);
				return true;
			}
		}
		return true;
	}
}
