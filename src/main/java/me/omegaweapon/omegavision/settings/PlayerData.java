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
		playerFile = new File(Bukkit.getPluginManager().getPlugin("Omegavision").getDataFolder(), "playerData.yml");

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
			"" +
				"\n This file contains all the stored nightvision data for all" +
				"\n the players with the permission omegavision.login" +
				"\n "
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
