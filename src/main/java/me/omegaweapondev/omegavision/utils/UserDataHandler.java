package me.omegaweapondev.omegavision.utils;

import me.omegaweapondev.omegavision.OmegaVision;
import me.ou.library.Utilities;
import me.ou.library.configs.ConfigCreator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserDataHandler {
  public static final String NIGHT_VISION = "Night_Vision";
  public static final String BLINDNESS = "Blindness";
  private final OmegaVision pluginInstance;
  private final Set<String> userData;
  private final FileConfiguration userDataFile;
  private final Map<UUID, Map<String, Boolean>> userDataMap = new ConcurrentHashMap<>();

  public UserDataHandler(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    userDataFile = pluginInstance.getStorageManager().getUserDataFile().getConfig();
    userData = userDataFile.getConfigurationSection("Users").getKeys(false);
  }

  public void populateUserDataMap() {
    if(userData.isEmpty()) {
      return;
    }

    getUserDataMap().clear();
    for(String user : userData) {
      getUserDataMap().put(UUID.fromString(user), new ConcurrentHashMap<>());
    }
  }

  public void addUserToMap(@NotNull final UUID playerUUID) {
    getUserDataMap().put(playerUUID, new ConcurrentHashMap<>());
    setEffectStatus(playerUUID, userDataFile.getBoolean("Users." + playerUUID + "." + NIGHT_VISION), NIGHT_VISION);
    setEffectStatus(playerUUID, userDataFile.getBoolean("Users." + playerUUID + "." + BLINDNESS), BLINDNESS);
  }

  public void saveUserDataToFile() {
    for(UUID userUUID : getUserDataMap().keySet()) {
      userDataFile.set("Users." + userUUID + "." + NIGHT_VISION, getUserDataMap().get(userUUID).getOrDefault(NIGHT_VISION, false));
      userDataFile.set("Users." + userUUID + "." + BLINDNESS, getUserDataMap().get(userUUID).getOrDefault(BLINDNESS, false));
    }
    pluginInstance.getStorageManager().getUserDataFile().saveConfig();
    userDataMap.clear();
  }

  public void saveUserDataToFile(@NotNull final UUID playerUUID) {
    userDataFile.set("Users." + playerUUID + "." + NIGHT_VISION, getUserDataMap().get(playerUUID).getOrDefault(NIGHT_VISION, false));
    userDataFile.set("Users." + playerUUID + "." + BLINDNESS, getUserDataMap().get(playerUUID).getOrDefault(BLINDNESS, false));
    pluginInstance.getStorageManager().getUserDataFile().saveConfig();
    userDataMap.remove(playerUUID);
  }

  public void setEffectStatus(@NotNull UUID uuid, boolean status, @NotNull String effect) {
    getUserDataMap().get(uuid).put(effect, status);
  }

  public boolean getEffectStatus(@NotNull UUID uuid, @NotNull String effect) {
    return getUserDataMap().get(uuid).getOrDefault(effect, false);
  }

  public Map<UUID, Map<String, Boolean>> getUserDataMap() {
    return userDataMap;
  }
}
