package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.settings.ConfigFile;
import me.omegaweapon.omegavision.settings.MessagesFile;
import me.omegaweapon.omegavision.settings.PlayerData;
import me.omegaweapon.omegavision.utils.ColourUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class MainCommand extends Command {
	OmegaVision plugin;
	private static Map<UUID, String> playerMap = new HashMap<>();
	
	public static Map<UUID, String> getPlayerMap() {
		return playerMap;
	}
	
	public MainCommand(OmegaVision plugin) {
		super("omegavision");
		this.plugin = plugin;
		
		setAliases(Arrays.asList(
			"nv",
			"ov"
		));
	}
	
	@Override
	public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {

		if(sender instanceof Player) {
			Player player = (Player) sender;
			String playerUUID = player.getUniqueId().toString();
			Boolean nightVision = PlayerData.getPlayerData().getBoolean(player.getUniqueId() + ".NightVision");
			
			if (args.length == 0) {
				player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " &c/omegavision toggle - Toggle your own nightvision"));
				player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " &c/omegavision toggle <player> - Toggle nightvision for another player>"));
				player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " &c/omegavision reload - Reload all the plugins files"));
			}
			
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (player.hasPermission("omegavision.reload") || player.isOp()) {
						plugin.onReload();
						player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.RELOAD_MESSAGE));
					} else {
						player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.NO_PERMISSION));
					}
				}
				
				if (args[0].equalsIgnoreCase("toggle")) {
					if (player.hasPermission("omegavision.toggle") || player.isOp()) {
						if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
							player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, ConfigFile.PARTICLE_AMBIENT, ConfigFile.PARTICLE_EFFECTS, ConfigFile.NIGHTVISION_ICON));
							playerMap.put(player.getUniqueId(), player.getName());
							player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.NIGHTVISION_APPLIED));
							if (nightVision.equals(false) && player.hasPermission("omegavision.login")) {
								PlayerData.getPlayerData().createSection(playerUUID + ".NightVision");
								PlayerData.getPlayerData().set(playerUUID + ".NightVision", true);
								PlayerData.savePlayerData();
							}
							if (ConfigFile.ACTIONBAR_MESSAGES.equals(true)) {
								player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.ACTIONBAR_NIGHTVISION_APPLIED)));
							}
						} else {
							playerMap.remove(player.getUniqueId());
							player.removePotionEffect(PotionEffectType.NIGHT_VISION);
							player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.NIGHTVISION_REMOVED));
							if (nightVision.equals(true) && player.hasPermission("omegavision.login")) {
								PlayerData.getPlayerData().createSection(playerUUID + ".NightVision");
								PlayerData.getPlayerData().set(playerUUID + ".NightVision", false);
								PlayerData.savePlayerData();
							}
							if (ConfigFile.ACTIONBAR_MESSAGES.equals(true)) {
								player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.ACTIONBAR_NIGHTVISION_REMOVED)));
							}
						}
					} else {
						player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.NO_PERMISSION));
					}
				}
				
				if (args[0].equalsIgnoreCase("list")) {
					if (player.hasPermission("omegavision.list") || player.isOp()) {
						player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + "&bThe following players have nightvision enabled:"));
						for (Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
							if(onlinePlayers.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
								player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + "&c" + playerMap.get(onlinePlayers.getUniqueId())));
							}
						}
					} else {
						player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.NO_PERMISSION));
					}
				}
			}
			
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("toggle")) {
					Player target = Bukkit.getServer().getPlayer(args[1]);
					
					if(player.hasPermission("omegavision.toggle.others")) {
						assert target != null;
						if(!target.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
							target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, ConfigFile.PARTICLE_AMBIENT, ConfigFile.PARTICLE_EFFECTS, ConfigFile.NIGHTVISION_ICON));
							target.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.NIGHTVISION_APPLIED));
							playerMap.put(target.getUniqueId(), target.getName());
							player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.NIGHTVISION_APPLIED));
						} else {
							target.removePotionEffect(PotionEffectType.NIGHT_VISION);
							playerMap.remove(target.getUniqueId());
							target.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.NIGHTVISION_REMOVED));
							player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.NIGHTVISION_REMOVED));
						}
					} else {
						player.sendMessage(ColourUtils.Colourize(MessagesFile.PREFIX + " " + MessagesFile.NO_PERMISSION));
					}
				}
			}
		}
		return true;
	}
}