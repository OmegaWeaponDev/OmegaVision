package me.omegaweapon.omegavision.settings.utils;

import me.omegaweapon.omegavision.OmegaUpdater;
import me.omegaweapon.omegavision.OmegaVision;
import org.bukkit.ChatColor;

public class MessageUtils {

	private static String prefix =  ColourUtils.Colourize(OmegaVision.getInstance().getMessagesConfig().getString("Prefix"));
	private static String noPermission = ColourUtils.Colourize(OmegaVision.getInstance().getMessagesConfig().getString("No_Permission"));
	private static String reloadCommand = ColourUtils.Colourize(OmegaVision.getInstance().getMessagesConfig().getString("Reload_Message"));
	private static String nightvisionApplied = ColourUtils.Colourize(OmegaVision.getInstance().getMessagesConfig().getString("NightVision_Applied"));
	private static String nightvisionRemoved = ColourUtils.Colourize(OmegaVision.getInstance().getMessagesConfig().getString("NightVision_Removed"));
	private static String actionbarNightVisionApplied = ColourUtils.Colourize(OmegaVision.getInstance().getMessagesConfig().getString("ActionBar_NightVision_Applied"));
	private static String actionbarNightVisionRemoved = ColourUtils.Colourize(OmegaVision.getInstance().getMessagesConfig().getString("ActionBar_NightVision_Removed"));

	private static String[] updateMessage = new String[] {
		getPrefix() + ChatColor.RED + OmegaVision.getInstance().getDescription().getName() + ChatColor.DARK_AQUA + " has been updated!",
		getPrefix() + ChatColor.DARK_AQUA + "Your current version: " + ChatColor.RED + OmegaVision.getInstance().getDescription().getVersion(),
		getPrefix() + ChatColor.DARK_AQUA + "Latest version: " + ChatColor.RED + OmegaUpdater.getLatestVersion(),
		getPrefix() + ChatColor.DARK_AQUA + "Get the update here: " + ChatColor.RED + " https://spigotmc.org/resources/" + OmegaUpdater.getProjectId()
	};

	public static String getPrefix() {
		return prefix + " ";
	}

	public static String getNoPermission() {
		return noPermission;
	}

	public static String getReloadCommand() {
		return reloadCommand;
	}

	public static String getActionbarNightVisionApplied() {
		return actionbarNightVisionApplied;
	}

	public static String getActionbarNightVisionRemoved() {
		return actionbarNightVisionRemoved;
	}


	public static String getNightvisionApplied() {
		return nightvisionApplied;
	}

	public static String getNightvisionRemoved() {
		return nightvisionRemoved;
	}

	public static String[] getUpdateMessage() {
		return updateMessage;
	}
}
