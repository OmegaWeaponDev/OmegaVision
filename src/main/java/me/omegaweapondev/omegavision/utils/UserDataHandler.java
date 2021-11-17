package me.omegaweapondev.omegavision.utils;

import me.omegaweapondev.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * The UserDataHandler class that handles all the user data for the plugin
 *
 * @author OmegaWeaponDev
 */
public class UserDataHandler {
  public static final String NIGHT_VISION = "Night_Vision";
  public static final String LIMIT = "Limit";
  private final OmegaVision pluginInstance;
  private final FileConfiguration userDataFile;
  private final Map<UUID, Map<String, Object>> userDataMap = new ConcurrentHashMap<>();

  /**
   *
   * The public constructor for the User Data Handler class
   *
   * @param pluginInstance (The plugin's instance)
   */
  public UserDataHandler(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    userDataFile = pluginInstance.getStorageManager().getUserDataFile().getConfig();
  }

  /**
   *
   * Grabs all the entries from the userData.yml file and
   * adds the players into the map
   *
   */
  public void populateUserDataMap() {
    Bukkit.getScheduler().runTaskAsynchronously(pluginInstance, () -> {
      if(userDataFile.getConfigurationSection("Users.").getKeys(false).isEmpty()) {
        return;
      }

      getUserDataMap().clear();
      for(String user : userDataFile.getConfigurationSection("Users.").getKeys(false)) {
        getUserDataMap().put(UUID.fromString(user), new ConcurrentHashMap<>());
      }
    });
  }

  /**
   *
   * Adds a specific user's data from the userData.yml and populates their map entry
   *
   */
  public void addUserToMap(@NotNull final UUID playerUUID) {
    getUserDataMap().put(playerUUID, new ConcurrentHashMap<>());
    setEffectStatus(playerUUID, userDataFile.getBoolean("Users." + playerUUID + "." + NIGHT_VISION, false));

    if(!pluginInstance.getStorageManager().getConfigFile().getConfig().getBoolean("Night_Vision_Limit.Enabled")) {
      return;
    }

    if(Utilities.checkPermissions(Bukkit.getPlayer(playerUUID), true, "omegavision.limit.bypass", "omegavision.limit.admin", "omegavision.admin")) {
      return;
    }

    setLimitStatus(playerUUID, userDataFile.getInt("Users." + playerUUID + "." + LIMIT, 0));
  }

  /**
   *
   * Saves all the current entries from the user data map into the userData.yml
   *
   */
  public void saveUserDataToFile() {
    for (UUID userUUID : getUserDataMap().keySet()) {
      userDataFile.set("Users." + userUUID + "." + NIGHT_VISION, getUserDataMap().get(userUUID).getOrDefault(NIGHT_VISION, false));
      userDataFile.set("Users." + userUUID + "." + LIMIT, getUserDataMap().get(userUUID).getOrDefault(LIMIT, 0));
    }
    pluginInstance.getStorageManager().getUserDataFile().saveConfig();
    userDataMap.clear();
  }

  /**
   *
   * Saves a specific players entry in the user data map into the userData.yml
   *
   * @param playerUUID (The player that has their user data saved)
   */
  public void saveUserDataToFile(@NotNull final UUID playerUUID) {
    Bukkit.getScheduler().runTaskAsynchronously(pluginInstance, () -> {
      userDataFile.set("Users." + playerUUID + "." + NIGHT_VISION, getUserDataMap().get(playerUUID).getOrDefault(NIGHT_VISION, false));
      userDataFile.set("Users." + playerUUID + "." + LIMIT, getUserDataMap().get(playerUUID).getOrDefault(LIMIT, 0));
      pluginInstance.getStorageManager().getUserDataFile().saveConfig();
      userDataMap.remove(playerUUID);
    });
  }

  /**
   *
   * Sets the player's effect status to either true or false in the map
   *
   * @param uuid (The player whose entry in the map is to be modified)
   * @param status (The status effect to modify the value for)
   */
  public void setEffectStatus(@NotNull UUID uuid, boolean status) {
    getUserDataMap().get(uuid).put(NIGHT_VISION, status);
  }

  /**
   *
   * Sets the player's limit status in the user data map
   *
   * @param uuid (The player whose entry in the map is to be modified)
   * @param limitAmount (The value to set the limit status to)
   */
  public void setLimitStatus(@NotNull UUID uuid, int limitAmount) {
    getUserDataMap().get(uuid).put(LIMIT, limitAmount);
  }

  /**
   *
   * Gets a specific player's effect status
   *
   * @param uuid (The player whose data needs to be retrieved from the user data map)
   * @return (The value of the effect status)
   */
  public boolean getEffectStatus(@NotNull UUID uuid) {
    return (boolean) getUserDataMap().get(uuid).getOrDefault(NIGHT_VISION, false);
  }

  /**
   *
   * Gets the specific players current limit status from the map
   *
   * @param uuid The player whose data needs to be retrieved from the user data map)
   * @return (The current limit status value)
   */
  public int getLimitStatus(@NotNull UUID uuid) {
    return (int) getUserDataMap().get(uuid).getOrDefault(LIMIT, 0);
  }

  /**
   *
   * Getter for the userDataMap
   *
   * @return userDataMap
   */
  public Map<UUID, Map<String, Object>> getUserDataMap() {
    return userDataMap;
  }
}
