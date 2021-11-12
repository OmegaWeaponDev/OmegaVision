package me.omegaweapondev.omegavision.utils;

import me.omegaweapondev.omegavision.OmegaVision;
import me.ou.library.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserDataHandler {
  public static final String NIGHT_VISION = "Night_Vision";
  public static final String LIMIT = "Limit";
  private final OmegaVision pluginInstance;
  private final FileConfiguration userDataFile;
  private final Map<UUID, Map<String, Object>> userDataMap = new ConcurrentHashMap<>();

  public UserDataHandler(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    userDataFile = pluginInstance.getStorageManager().getUserDataFile().getConfig();
  }

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

  public void saveUserDataToFile() {
    for (UUID userUUID : getUserDataMap().keySet()) {
      userDataFile.set("Users." + userUUID + "." + NIGHT_VISION, getUserDataMap().get(userUUID).getOrDefault(NIGHT_VISION, false));
      userDataFile.set("Users." + userUUID + "." + LIMIT, getUserDataMap().get(userUUID).getOrDefault(LIMIT, 0));
    }
    pluginInstance.getStorageManager().getUserDataFile().saveConfig();
    userDataMap.clear();
  }



  public void saveUserDataToFile(@NotNull final UUID playerUUID) {
    Bukkit.getScheduler().runTaskAsynchronously(pluginInstance, () -> {
      userDataFile.set("Users." + playerUUID + "." + NIGHT_VISION, getUserDataMap().get(playerUUID).getOrDefault(NIGHT_VISION, false));
      userDataFile.set("Users." + playerUUID + "." + LIMIT, getUserDataMap().get(playerUUID).getOrDefault(LIMIT, 0));
      pluginInstance.getStorageManager().getUserDataFile().saveConfig();
      userDataMap.remove(playerUUID);
    });
  }

  public void setEffectStatus(@NotNull UUID uuid, boolean status) {
    getUserDataMap().get(uuid).put(NIGHT_VISION, status);
  }

  public void setLimitStatus(@NotNull UUID uuid, int limitAmount) {
    getUserDataMap().get(uuid).put(LIMIT, limitAmount);
  }

  public boolean getEffectStatus(@NotNull UUID uuid) {
    return (boolean) getUserDataMap().get(uuid).getOrDefault(NIGHT_VISION, false);
  }

  public int getLimitStatus(@NotNull UUID uuid) {
    return (int) getUserDataMap().get(uuid).getOrDefault(LIMIT, 0);
  }

  public Map<UUID, Map<String, Object>> getUserDataMap() {
    return userDataMap;
  }
}
