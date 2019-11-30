package me.omegaweapon.omegavision.settings.utils;

import me.omegaweapon.omegavision.OmegaVision;
import org.bukkit.ChatColor;

public class MessageUtils {

	private static String prefix =  OmegaVision.getInstance().getMessagesConfig().getString("Prefix");
	private static String noPermission = OmegaVision.getInstance().getMessagesConfig().getString("No_Permission");
	private static String reloadCommand = OmegaVision.getInstance().getMessagesConfig().getString("Reload_Message");
	private static String nightvisionApplied = OmegaVision.getInstance().getMessagesConfig().getString("NightVision_Applied");
	private static String nightvisionRemoved = OmegaVision.getInstance().getMessagesConfig().getString("NightVision_Removed");

	public static String getPrefix() {
		return prefix;
	}

	public static String getNoPermission() {
		return noPermission;
	}

	public static String getReloadCommand() {
		return reloadCommand;
	}

	public static String getNightvisionApplied() {
		return nightvisionApplied;
	}

	public static String getNightvisionRemoved() {
		return nightvisionRemoved;
	}
}
