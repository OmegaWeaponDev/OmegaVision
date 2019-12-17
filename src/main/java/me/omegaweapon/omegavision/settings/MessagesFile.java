package me.omegaweapon.omegavision.settings;

public class MessagesFile extends FileManager {
  
  private MessagesFile(String fileName) {
    super(fileName);
    
    setHeader(new String[] {
      " -------------------------------------------------------------------------------------------",
      " ",
      " Welcome to OmegaVision 's messages file.",
      " ",
      " Here you'll find all of the messages that you can",
      " customize to your server needs.",
      " ",
      " -------------------------------------------------------------------------------------------"
    });
  }
  
  public static String PREFIX;
  public static String NIGHTVISION_APPLIED;
  public static String NIGHTVISION_REMOVED;
  public static String ACTIONBAR_NIGHTVISION_APPLIED;
  public static String ACTIONBAR_NIGHTVISION_REMOVED;
  public static String BUCKET_MESSAGE;
  public static String NO_PERMISSION;
  public static String RELOAD_MESSAGE;
  
  private void onLoad() {
    PREFIX = getString("Prefix");
    NIGHTVISION_APPLIED = getString("NightVision_Applied");
    NIGHTVISION_REMOVED = getString("NightVision_Removed");
    ACTIONBAR_NIGHTVISION_APPLIED = getString("ActionBar_NightVision_Applied");
    ACTIONBAR_NIGHTVISION_REMOVED = getString("ActionBar_NightVision_Removed");
    BUCKET_MESSAGE = getString("Bucket_Message");
    NO_PERMISSION = getString("No_Permission");
    RELOAD_MESSAGE = getString("Reload_Message");
  }
  
  public static void init() {
    new MessagesFile("messages.yml").onLoad();
  }
}
