package me.omegaweapon.omegavision.settings;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerData {
	private static FileConfiguration playerData;
	private static File playerFile;

	public static void setupPlayerData() {
		playerFile = new File(Bukkit.getPluginManager().getPlugin("OmegaVision").getDataFolder(), "playerData.yml");

		if(!playerFile.exists()) {
			try {
				playerFile.createNewFile();
			} catch (IOException ignored) {
			}
		} else {
			reloadPlayerData();
		}
		playerData  = YamlConfiguration.loadConfiguration(playerFile);
		playerData.options().header(
			" -------------------------------------------------------------------------------------------\n" +
			" \n" +
			" Welcome to OmegaVision 's main configuration file.\n" +
			" \n" +
			" Here you'll find of the settings and options that you can\n" +
			" customize to your server needs. Most features are customizable\n" +
			" to an extent.\n" +
			" \n" +
			" -------------------------------------------------------------------------------------------"
		);
	}

	public static FileConfiguration getPlayerData() {
		return playerData;
	}

	public static void savePlayerData() {
		try {
			playerData.save(playerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void reloadPlayerData() {
		playerData = YamlConfiguration.loadConfiguration(playerFile);
	}
}
