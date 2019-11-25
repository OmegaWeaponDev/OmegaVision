package me.omegaweapon.omegavision.events;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.settings.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {
	OmegaVision plugin;

	public PlayerListener(OmegaVision plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
		Player player = playerJoinEvent.getPlayer();
		Boolean nightVision = PlayerData.getPlayerData().getBoolean(player.getUniqueId() + ".NightVision");
		Boolean nightVisionLogin = plugin.getConfig().getBoolean("Night_Vision_Login");
		Boolean particles = plugin.getConfig().getBoolean("Particle_Effects");
		Boolean ambient = plugin.getConfig().getBoolean("Particle_Ambient");
		Boolean nightVisionIcon = plugin.getConfig().getBoolean("NightVision_Icon");

		if(nightVision.equals(true) && nightVisionLogin.equals(true)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, ambient, particles, nightVisionIcon));
		} else if (nightVision.equals(false) ||  nightVisionLogin.equals(false)) {
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			if(player.hasPermission("omegavision.login")) {
				PlayerData.getPlayerData().createSection(player.getUniqueId().toString());
				PlayerData.getPlayerData().set(player.getUniqueId().toString() + "." + "NightVision", false);
				PlayerData.savePlayerData();
			}
		}
	}
}
