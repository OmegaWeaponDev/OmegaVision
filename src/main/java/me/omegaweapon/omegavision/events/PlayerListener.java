package me.omegaweapon.omegavision.events;

import me.omegaweapon.omegavision.OmegaUpdater;
import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.settings.PlayerData;
import me.omegaweapon.omegavision.settings.utils.ConfigSettings;
import me.omegaweapon.omegavision.settings.utils.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {
	OmegaVision plugin;
	final PluginDescriptionFile pdf = OmegaVision.getInstance().getDescription();

	public PlayerListener(OmegaVision plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
		// Setup all the variables
		Player player = playerJoinEvent.getPlayer();
		Boolean nightVision = PlayerData.getPlayerData().getBoolean(player.getUniqueId() + ".NightVision");

		// Applies or removes nightvision onjoin
		if(nightVision.equals(true) && ConfigSettings.getNightVisionLogin().equals(true)) {
			player.addPotionEffect(
				new PotionEffect(
					PotionEffectType.NIGHT_VISION,
					Integer.MAX_VALUE,
					1,
					ConfigSettings.getAmbient(),
					ConfigSettings.getParticles(),
					ConfigSettings.getNightVisionIcon()
				)
			);
		} else if (nightVision.equals(false) ||  ConfigSettings.getNightVisionLogin().equals(false)) {
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			if(player.hasPermission("omegavision.login")) {
				PlayerData.getPlayerData().createSection(player.getUniqueId().toString());
				PlayerData.getPlayerData().set(player.getUniqueId().toString() + "." + "NightVision", false);
				PlayerData.savePlayerData();
			}
		}

		// Sends Update message depending on config setting.
		if(ConfigSettings.getUpdateNotify().equals(true) && player.hasPermission("omegavision.update")) {
			if (OmegaUpdater.isUpdateAvailable()) {
				player.sendMessage(MessageUtils.getPrefix() + ChatColor.RED + " " + pdf.getName() + ChatColor.DARK_AQUA + " has been updated!");
				player.sendMessage(MessageUtils.getPrefix() + ChatColor.DARK_AQUA + " Your current version: " + ChatColor.RED + pdf.getVersion());
				player.sendMessage(MessageUtils.getPrefix() + ChatColor.DARK_AQUA + " Latest version: " + ChatColor.RED + OmegaUpdater.getLatestVersion());
				player.sendMessage(MessageUtils.getPrefix() + ChatColor.DARK_AQUA + " Get the update here:  " + ChatColor.RED + " https://spigotmc.org/resources/" + OmegaUpdater.getProjectId());
			}
		}
	}
}
