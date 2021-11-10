package me.omegaweapondev.omegavision.utils;

import me.omegaweapondev.omegavision.OmegaVision;
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
  private final Set<String> userData;
  private final FileConfiguration userDataFile;
  private final Map<UUID, Map<String, Object>> userDataMap = new ConcurrentHashMap<>();
  private final Map<UUID, Integer> userLimitMap = new ConcurrentHashMap<>();

  public UserDataHandler(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    userDataFile = pluginInstance.getStorageManager().getUserDataFile().getConfig();
    userData = userDataFile.getConfigurationSection("Users").getKeys(false);
  }

  public void populateUserDataMap() {
    if(userData.isEmpty()) {
      return;
    }

    new BukkitRunnable() {
      @Override
      public void run() {
        getUserDataMap().clear();
        for(String user : userData) {
          getUserDataMap().put(UUID.fromString(user), new ConcurrentHashMap<>());
          getUserLimitMap().put(UUID.fromString(user), 0);
        }
      }
    }.runTaskAsynchronously(pluginInstance);

  }

  public void addUserToMap(@NotNull final UUID playerUUID) {
    new BukkitRunnable() {
      @Override
      public void run() {
        getUserDataMap().put(playerUUID, new ConcurrentHashMap<>());
        setEffectStatus(playerUUID, userDataFile.getBoolean("Users." + playerUUID + "." + NIGHT_VISION, false));

      }
    }.runTaskAsynchronously(pluginInstance);
  }

  public void saveUserDataToFile() {
    new BukkitRunnable() {
      @Override
      public void run() {
        for(UUID userUUID : getUserDataMap().keySet()) {
          userDataFile.set("Users." + userUUID + "." + NIGHT_VISION, getUserDataMap().get(userUUID).getOrDefault(NIGHT_VISION, false));
          userDataFile.set("Users." + userUUID + "." + LIMIT, getUserDataMap().get(userUUID).getOrDefault(LIMIT, 0));
        }
        pluginInstance.getStorageManager().getUserDataFile().saveConfig();
        userDataMap.clear();
      }
    }.runTaskAsynchronously(pluginInstance);

  }

  public void saveUserDataToFile(@NotNull final UUID playerUUID) {
    new BukkitRunnable() {
      @Override
      public void run() {
        userDataFile.set("Users." + playerUUID + "." + NIGHT_VISION, getUserDataMap().get(playerUUID).getOrDefault(NIGHT_VISION, false));
        pluginInstance.getStorageManager().getUserDataFile().saveConfig();
        userDataMap.remove(playerUUID);
      }
    }.runTaskAsynchronously(pluginInstance);

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

  public Map<UUID, Integer> getUserLimitMap() {
    return userLimitMap;
  }
}
