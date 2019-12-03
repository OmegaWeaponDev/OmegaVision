package me.omegaweapon.omegavision.settings.utils;

import me.omegaweapon.omegavision.OmegaVision;

public class ConfigSettings {

	private static Boolean nightVisionLogin = OmegaVision.getInstance().getConfig().getBoolean("Night_Vision_Login");
	private static Boolean particles = OmegaVision.getInstance().getConfig().getBoolean("Particle_Effects");
	private static Boolean ambient = OmegaVision.getInstance().getConfig().getBoolean("Particle_Ambient");
	private static Boolean nightVisionIcon = OmegaVision.getInstance().getConfig().getBoolean("NightVision_Icon");
	private static Boolean updateNotify = OmegaVision.getInstance().getConfig().getBoolean("Update_Notify");
	private static Boolean keepNightVision = OmegaVision.getInstance().getConfig().getBoolean("Keep_NightVision_On_Death");
	private static Boolean actionBarMessages = OmegaVision.getInstance().getConfig().getBoolean("ActionBar_Message");

	public static Boolean getActionBarMessages() {
		return actionBarMessages;
	}


	public static Boolean getKeepNightVision() {
		return keepNightVision;
	}

	public static Boolean getUpdateNotify() {
		return updateNotify;
	}

	public static Boolean getNightVisionLogin() {
		return nightVisionLogin;
	}

	public static Boolean getParticles() {
		return particles;
	}

	public static Boolean getAmbient() {
		return ambient;
	}

	public static Boolean getNightVisionIcon() {
		return nightVisionIcon;
	}
}
