package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.settings.PlayerData;
import me.omegaweapon.omegavision.settings.utils.ColourUtils;
import me.omegaweapon.omegavision.settings.utils.ConfigSettings;
import me.omegaweapon.omegavision.settings.utils.MessageUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
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
	public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			String version = Bukkit.getVersion();

			if (command.getName().equalsIgnoreCase("omegavision") && (player.hasPermission("omegavision.toggle") || player.isOp())) {
				if (args.length != 1) {
					player.sendMessage(MessageUtils.getPrefix() + ColourUtils.Colourize("&b/ausvision toggle - Toggle your nightvision on or off"));
					player.sendMessage(MessageUtils.getPrefix() + ColourUtils.Colourize("&b/ausvision reload - Reload the plugin"));
					return true;
				} else if (args[0].equalsIgnoreCase("toggle")) {
					if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
						if (version.contains("1.7") || version.contains("1.8") || version.contains("1.9") || version.contains("1.10") || version.contains("1.11") || version.contains("1.12")) {
							player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
							player.sendMessage(MessageUtils.getPrefix() + MessageUtils.getNightvisionApplied());
							if (ConfigSettings.getNightVisionLogin().equals(true) && (player.hasPermission("omegavision.login") || player.isOp())) {
								PlayerData.getPlayerData().createSection(player.getUniqueId().toString());
								PlayerData.getPlayerData().set(player.getUniqueId().toString() + "." + "NightVision", false);
								PlayerData.savePlayerData();
							}
						} else {
							player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, ConfigSettings.getAmbient(), ConfigSettings.getParticles(), ConfigSettings.getNightVisionIcon()));
							player.sendMessage(MessageUtils.getPrefix() + MessageUtils.getNightvisionApplied());
							if(ConfigSettings.getActionBarMessages().equals(true)) {
								player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessageUtils.getActionbarNightVisionApplied()));
							}
							if (ConfigSettings.getNightVisionLogin().equals(true) && (player.hasPermission("omegavision.login") || player.isOp())) {
								PlayerData.getPlayerData().createSection(player.getUniqueId().toString());
								PlayerData.getPlayerData().set(player.getUniqueId().toString() + "." + "NightVision", false);
								PlayerData.savePlayerData();
							}
						}
						return true;
					} else {
						player.removePotionEffect(PotionEffectType.NIGHT_VISION);
						player.sendMessage(MessageUtils.getPrefix() + MessageUtils.getNightvisionRemoved());
						if(ConfigSettings.getActionBarMessages().equals(true)) {
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(MessageUtils.getActionbarNightVisionRemoved()));
						}
						if (ConfigSettings.getNightVisionLogin().equals(true) && (player.hasPermission("omegavision.login") || player.isOp())) {
							PlayerData.getPlayerData().createSection(player.getUniqueId().toString());
							PlayerData.getPlayerData().set(player.getUniqueId().toString() + "." + "NightVision", false);
							PlayerData.savePlayerData();
						}
						return true;
					}
				} else if(args[0].equalsIgnoreCase("reload")) {
					if(player.hasPermission("omegavision.reload") || player.isOp())
						plugin.reloadConfig();
					PlayerData.reloadPlayerData();
					player.sendMessage(MessageUtils.getPrefix() + MessageUtils.getReloadCommand());
					return true;
				} else {
					player.sendMessage(MessageUtils.getPrefix() + MessageUtils.getNoPermission());
				}
			}
		}
		return true;
	}
}