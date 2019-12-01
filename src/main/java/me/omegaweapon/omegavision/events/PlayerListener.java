package me.omegaweapon.omegavision.events;

import me.omegaweapon.omegavision.OmegaUpdater;
import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.settings.PlayerData;
import me.omegaweapon.omegavision.settings.utils.ConfigSettings;
import me.omegaweapon.omegavision.settings.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PlayerListener implements Listener {
	OmegaVision plugin;
	String version = Bukkit.getVersion();

	public PlayerListener(OmegaVision plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
		Player player = playerJoinEvent.getPlayer();

		Boolean nightVision = PlayerData.getPlayerData().getBoolean(player.getUniqueId() + ".NightVision");

		// Applies or removes nightvision onjoin
		if(nightVision.equals(true) && ConfigSettings.getNightVisionLogin().equals(true)) {
			if(version.contains("1.7") || version.contains("1.8") || version.contains("1.9") || version.contains("1.10") || version.contains("1.11") || version.contains("1.12")) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
				player.sendMessage(MessageUtils.getPrefix() + MessageUtils.getNightvisionApplied());
			} else {
				player.addPotionEffect(
					new PotionEffect(
						PotionEffectType.NIGHT_VISION,
						Integer.MAX_VALUE,
						1,
						ConfigSettings.getAmbient(),
						ConfigSettings.getParticles(),
						ConfigSettings.getNightVisionIcon())
				);
				player.sendMessage(MessageUtils.getPrefix() + MessageUtils.getNightvisionApplied());
			}
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

		// Add nightvision to player after the respawn, depending on config and playerdata settings
		if(ConfigSettings.getKeepNightVision().equals(true) && nightVision.equals(true) && player.hasPermission("omegavision.death")) {
			Bukkit.getScheduler().runTaskLater(plugin, () -> {
					if(version.contains("1.7") || version.contains("1.8") || version.contains("1.9") || version.contains("1.10") || version.contains("1.11") || version.contains("1.12")) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
						player.sendMessage(MessageUtils.getPrefix() + MessageUtils.getNightvisionApplied());
					} else {
						player.addPotionEffect(
							new PotionEffect(
								PotionEffectType.NIGHT_VISION,
								Integer.MAX_VALUE,
								1,
								ConfigSettings.getAmbient(),
								ConfigSettings.getParticles(),
								ConfigSettings.getNightVisionIcon())
						);
						player.sendMessage(MessageUtils.getPrefix() + MessageUtils.getNightvisionApplied());
					}
			}, (1));
			player.sendMessage(MessageUtils.getPrefix() + MessageUtils.getNightvisionApplied());
		}
	}
}
