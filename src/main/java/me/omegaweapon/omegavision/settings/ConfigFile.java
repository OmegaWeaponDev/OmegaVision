package me.omegaweapon.omegavision.settings;

public class ConfigFile extends FileManager {
	
	private ConfigFile(String fileName) {
		super(fileName);
		
		setHeader(new String[] {
			"-------------------------------------------------------------------------------------------",
			" ",
			" Welcome to OmegaVision's main configuration file.",
			" ",
			" Here you'll find of the settings and options that you can",
			" customize to your server needs. Most features are customizable",
			" to an extent.",
			" ",
			"-------------------------------------------------------------------------------------------"
		});
	}
	
	public static Boolean NIGHT_VISION_LOGIN;
	public static Boolean PARTICLE_EFFECTS;
	public static Boolean PARTICLE_AMBIENT;
	public static Boolean NIGHTVISION_ICON;
	public static Boolean ACTIONBAR_MESSAGES;
	public static Boolean BUCKET_USAGE;
	public static Boolean BUCKET_EMPTY;
	public static Boolean KEEP_NIGHTVISION_ON_DEATH;
	public static Boolean UPDATE_NOTIFY;
	
	private void onLoad() {
		NIGHT_VISION_LOGIN = getBoolean("Night_Vision_Login");
		PARTICLE_EFFECTS = getBoolean("Particle_Effects");
		PARTICLE_AMBIENT = getBoolean("Particle_Ambient");
		NIGHTVISION_ICON = getBoolean("NightVision_Icon");
		ACTIONBAR_MESSAGES = getBoolean("ActionBar_Messages");
		BUCKET_USAGE = getBoolean("Bucket_Usage");
		BUCKET_EMPTY = getBoolean("Bucket_Empty");
		KEEP_NIGHTVISION_ON_DEATH = getBoolean("Keep_NightVision_On_Death");
		UPDATE_NOTIFY = getBoolean("Update_Notify");
	}
	
	
	public static void init() {
		new ConfigFile("config.yml").onLoad();
	}
}
