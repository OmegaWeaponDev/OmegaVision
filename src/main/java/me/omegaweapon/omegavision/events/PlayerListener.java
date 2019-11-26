package me.omegaweapon.omegavision.events;

import me.omegaweapon.omegavision.OmegaUpdater;
import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.settings.PlayerData;
import org.bukkit.ChatColor;
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
		Boolean nightVisionLogin = plugin.getConfig().getBoolean("Night_Vision_Login");
		Boolean particles = plugin.getConfig().getBoolean("Particle_Effects");
		Boolean ambient = plugin.getConfig().getBoolean("Particle_Ambient");
		Boolean nightVisionIcon = plugin.getConfig().getBoolean("NightVision_Icon");
		Boolean updateNotify = plugin.getConfig().getBoolean("Update_Notify");

		String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Prefix"));

		// Applies or removes nightvision onjoin
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

		// Sends Update message depending on config setting.
		if(updateNotify.equals(true) && player.hasPermission("omegavision.update")) {
			if (OmegaUpdater.isUpdateAvailable()) {
				player.sendMessage(prefix + ChatColor.RED + " " + pdf.getName() + ChatColor.DARK_AQUA + " has been updated!");
				player.sendMessage(prefix + ChatColor.DARK_AQUA + " Your current version: " + ChatColor.RED + pdf.getVersion());
				player.sendMessage(prefix + ChatColor.DARK_AQUA + " Latest version: " + ChatColor.RED + OmegaUpdater.getLatestVersion());
				player.sendMessage(prefix + ChatColor.DARK_AQUA + " Get the update here:  " + ChatColor.RED + " https://spigotmc.org/resources/" + OmegaUpdater.getProjectId());
			}
		}
	}
}
