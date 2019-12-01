package me.omegaweapon.omegavision.events;

import me.omegaweapon.omegavision.OmegaUpdater;
import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.settings.PlayerData;
import me.omegaweapon.omegavision.settings.utils.ConfigSettings;
import me.omegaweapon.omegavision.settings.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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
				player.sendMessage(MessageUtils.getUpdateMessage());
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		Boolean nightVision = PlayerData.getPlayerData().getBoolean(player.getUniqueId() + ".NightVision");

		player.sendMessage("NightVision = " + ConfigSettings.getKeepNightVision());

		// Add nightvision to player after the respawn, depending on config and playerdata settings
		if(ConfigSettings.getKeepNightVision().equals(true) && nightVision.equals(true) && player.hasPermission("omegavision.death")) {
			Bukkit.getScheduler().runTaskLater(plugin, () -> player.addPotionEffect(new PotionEffect(
				PotionEffectType.NIGHT_VISION,
				Integer.MAX_VALUE,
				1,
				ConfigSettings.getAmbient(),
				ConfigSettings.getParticles(),
				ConfigSettings.getNightVisionIcon()
			)), (1));
			player.sendMessage(MessageUtils.getPrefix() + MessageUtils.getNightvisionApplied());
		}
	}
}
